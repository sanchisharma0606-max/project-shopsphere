package com.shopsphere.service;

import com.shopsphere.dao.*;
import com.shopsphere.model.*;
import com.shopsphere.util.DBConnection;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Service managing transactional checkout and order placement.
 * Ensures strict atomicity, stock verification, and rollback safety.
 */
public class CheckoutService {

    private static final Logger LOGGER = Logger.getLogger(CheckoutService.class.getName());

    private final CartDAO cartDAO;
    private final AddressDAO addressDAO;
    private final ProductDAO productDAO;
    private final CouponDAO couponDAO;
    private final OrderDAO orderDAO;
    private final OrderItemDAO orderItemDAO;
    private final PaymentDAO paymentDAO;

    public CheckoutService() {
        this.cartDAO = new CartDAO();
        this.addressDAO = new AddressDAO();
        this.productDAO = new ProductDAO();
        this.couponDAO = new CouponDAO();
        this.orderDAO = new OrderDAO();
        this.orderItemDAO = new OrderItemDAO();
        this.paymentDAO = new PaymentDAO();
    }

    public CheckoutService(CartDAO cartDAO, AddressDAO addressDAO, ProductDAO productDAO,
                           CouponDAO couponDAO, OrderDAO orderDAO, OrderItemDAO orderItemDAO, PaymentDAO paymentDAO) {
        this.cartDAO = cartDAO;
        this.addressDAO = addressDAO;
        this.productDAO = productDAO;
        this.couponDAO = couponDAO;
        this.orderDAO = orderDAO;
        this.orderItemDAO = orderItemDAO;
        this.paymentDAO = paymentDAO;
    }

    /**
     * Executes end-to-end transactional checkout.
     *
     * @param userId customer ID
     * @param addressId shipping address ID
     * @param paymentMethod payment method (COD, UPI, CREDIT_CARD, etc.)
     * @param couponCode optional coupon code
     * @return the placed Order
     * @throws IllegalStateException or IllegalArgumentException on business validation failure
     * @throws SQLException on database failure (rolled back)
     */
    public Order processCheckout(int userId, int addressId, Order.PaymentMethod paymentMethod, String couponCode)
            throws SQLException {

        // 1. Validate Address and user ownership
        Optional<Address> addressOpt = addressDAO.findById(addressId);
        if (addressOpt.isEmpty() || addressOpt.get().getUserId() != userId) {
            throw new IllegalArgumentException("Invalid shipping address selected.");
        }
        Address address = addressOpt.get();

        // 2. Load Cart
        List<CartItem> cartItems = cartDAO.getCartByUserId(userId);
        if (cartItems.isEmpty()) {
            throw new IllegalStateException("Your cart is empty. Cannot proceed to checkout.");
        }

        // 3. Validate Stock and calculate server-side subtotal
        BigDecimal subtotal = BigDecimal.ZERO;
        for (CartItem item : cartItems) {
            Product product = item.getProduct();
            if (product == null || product.getStatus() != Product.Status.ACTIVE) {
                throw new IllegalStateException("Product '" + (product != null ? product.getName() : "Item") +
                        "' is no longer available. Please update your cart.");
            }
            if (product.getStock() < item.getQuantity()) {
                throw new IllegalStateException("Insufficient stock for '" + product.getName() +
                        "'. Only " + product.getStock() + " available, but " + item.getQuantity() + " requested.");
            }
            subtotal = subtotal.add(item.getSubtotal());
        }

        // 4. Validate Coupon (if supplied) and compute discount
        BigDecimal discount = BigDecimal.ZERO;
        Coupon appliedCoupon = null;
        if (couponCode != null && !couponCode.trim().isEmpty()) {
            Optional<Coupon> cOpt = couponDAO.findByCode(couponCode.trim());
            if (cOpt.isPresent()) {
                Coupon c = cOpt.get();
                if (c.isValidForTotal(subtotal) && !couponDAO.hasUserUsedCoupon(c.getCouponId(), userId)) {
                    appliedCoupon = c;
                    discount = c.calculateDiscount(subtotal);
                }
            }
        }

        // 5. Calculate Final Total
        BigDecimal totalAmount = subtotal.subtract(discount);
        if (totalAmount.compareTo(BigDecimal.ZERO) < 0) {
            totalAmount = BigDecimal.ZERO;
        }

        // Determine initial payment status
        Order.PaymentStatus paymentStatus = (paymentMethod == Order.PaymentMethod.COD)
                ? Order.PaymentStatus.PENDING
                : Order.PaymentStatus.PAID;

        // 6. Begin Database Transaction
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false); // Enable manual transaction control

            // 6a. Insert Order
            Order order = new Order();
            order.setUserId(userId);
            order.setAddressId(addressId);
            order.setTotalAmount(subtotal);
            order.setDiscount(discount);
            order.setPaymentMethod(paymentMethod != null ? paymentMethod : Order.PaymentMethod.COD);
            order.setPaymentStatus(paymentStatus);
            order.setOrderStatus(Order.OrderStatus.PLACED);

            int orderId = orderDAO.createOrder(conn, order);
            if (orderId <= 0) {
                throw new SQLException("Failed to generate order ID.");
            }
            order.setOrderId(orderId);
            order.setAddress(address);

            // 6b. Insert Order Items and Atomically Deduct Stock
            for (CartItem item : cartItems) {
                Product product = item.getProduct();

                // Atomic stock decrement with check: stock = stock - qty WHERE stock >= qty
                boolean deducted = productDAO.deductStock(conn, product.getProductId(), item.getQuantity());
                if (!deducted) {
                    throw new IllegalStateException("Could not reserve stock for '" + product.getName() +
                            "'. It may have just sold out.");
                }

                OrderItem orderItem = new OrderItem(orderId, product.getProductId(), item.getQuantity(), product.getDiscountedPrice());
                orderItem.setProduct(product);
                int orderItemId = orderItemDAO.createOrderItem(conn, orderItem);
                orderItem.setOrderItemId(orderItemId);
                order.addItem(orderItem);
            }

            // 6c. Record Payment Transaction
            String txnRef = "TXN_" + paymentMethod.name() + "_" + System.currentTimeMillis() + "_" +
                    UUID.randomUUID().toString().substring(0, 8).toUpperCase();
            Payment payment = new Payment();
            payment.setOrderId(orderId);
            payment.setPaymentMethod(paymentMethod.name());
            payment.setTransactionReference(txnRef);
            payment.setAmount(totalAmount);
            payment.setPaymentStatus(paymentStatus == Order.PaymentStatus.PAID
                    ? Payment.Status.COMPLETED
                    : Payment.Status.PENDING);

            paymentDAO.createPayment(conn, payment);

            // 6d. Record Coupon Usage
            if (appliedCoupon != null) {
                couponDAO.recordUsage(conn, appliedCoupon.getCouponId(), userId, orderId);
            }

            // 6e. Clear Cart for this user
            cartDAO.clearCart(conn, userId);

            // 7. Commit Transaction
            conn.commit();
            LOGGER.info("Order #" + orderId + " successfully placed for user #" + userId + " [Total: " + totalAmount + "]");
            return order;

        } catch (Exception e) {
            // Rollback on any failure
            if (conn != null) {
                try {
                    conn.rollback();
                    LOGGER.log(Level.WARNING, "Transaction rolled back during checkout for user #" + userId + ". Reason: " + e.getMessage());
                } catch (SQLException rollbackEx) {
                    LOGGER.log(Level.SEVERE, "Failed to rollback database transaction!", rollbackEx);
                }
            }
            if (e instanceof IllegalStateException) {
                throw (IllegalStateException) e;
            } else if (e instanceof IllegalArgumentException) {
                throw (IllegalArgumentException) e;
            } else if (e instanceof SQLException) {
                throw (SQLException) e;
            } else {
                throw new RuntimeException("Unexpected error processing checkout: " + e.getMessage(), e);
            }
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException closeEx) {
                    LOGGER.log(Level.WARNING, "Failed to close database connection", closeEx);
                }
            }
        }
    }
}
