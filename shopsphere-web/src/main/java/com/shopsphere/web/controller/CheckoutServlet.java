package com.shopsphere.web.controller;

import com.shopsphere.dao.AddressDAO;
import com.shopsphere.model.*;
import com.shopsphere.service.CartService;
import com.shopsphere.service.CheckoutService;
import com.shopsphere.service.CouponService;
import com.shopsphere.service.OrderService;
import com.shopsphere.util.ValidationUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Controller managing checkout presentation, order placement, and success confirmation.
 */
@WebServlet(name = "CheckoutServlet", urlPatterns = {"/checkout", "/order-success"})
public class CheckoutServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(CheckoutServlet.class.getName());

    private CheckoutService checkoutService;
    private CartService cartService;
    private AddressDAO addressDAO;
    private CouponService couponService;
    private OrderService orderService;

    @Override
    public void init() {
        this.checkoutService = new CheckoutService();
        this.cartService = new CartService();
        this.addressDAO = new AddressDAO();
        this.couponService = new CouponService();
        this.orderService = new OrderService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String servletPath = req.getServletPath();

        if ("/order-success".equals(servletPath)) {
            handleOrderSuccess(req, resp);
            return;
        }

        handleCheckoutPage(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");

        String addressIdParam = req.getParameter("addressId");
        String paymentMethodParam = req.getParameter("paymentMethod");
        String couponCode = (String) session.getAttribute("appliedCouponCode");

        int addressId = ValidationUtil.parsePositiveInt(addressIdParam, -1);
        if (addressId <= 0) {
            req.setAttribute("errorMessage", "Please select a valid shipping address.");
            handleCheckoutPage(req, resp);
            return;
        }

        Order.PaymentMethod paymentMethod;
        try {
            paymentMethod = Order.PaymentMethod.valueOf(paymentMethodParam);
        } catch (Exception e) {
            paymentMethod = Order.PaymentMethod.COD;
        }

        try {
            Order placedOrder = checkoutService.processCheckout(user.getUserId(), addressId, paymentMethod, couponCode);

            // Clean up session cart state
            session.removeAttribute("appliedCouponCode");
            session.setAttribute("cartCount", 0);

            LOGGER.info("Order successfully created: #" + placedOrder.getOrderId());
            resp.sendRedirect(req.getContextPath() + "/order-success?id=" + placedOrder.getOrderId());

        } catch (IllegalStateException | IllegalArgumentException e) {
            req.setAttribute("errorMessage", e.getMessage());
            handleCheckoutPage(req, resp);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Database error during checkout", e);
            req.setAttribute("errorMessage", "Transaction failed: " + e.getMessage());
            handleCheckoutPage(req, resp);
        }
    }

    private void handleCheckoutPage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");

        try {
            List<CartItem> cartItems = cartService.getCart(user.getUserId());
            if (cartItems.isEmpty()) {
                resp.sendRedirect(req.getContextPath() + "/cart");
                return;
            }

            List<Address> addresses = addressDAO.findByUserId(user.getUserId());
            BigDecimal subtotal = cartService.calculateCartTotal(cartItems);

            String couponCode = (String) session.getAttribute("appliedCouponCode");
            BigDecimal discount = BigDecimal.ZERO;
            Coupon coupon = null;

            if (couponCode != null && !couponCode.isEmpty()) {
                Optional<Coupon> cOpt = couponService.validateCoupon(couponCode, subtotal, user.getUserId());
                if (cOpt.isPresent()) {
                    coupon = cOpt.get();
                    discount = coupon.calculateDiscount(subtotal);
                }
            }

            BigDecimal grandTotal = subtotal.subtract(discount);
            if (grandTotal.compareTo(BigDecimal.ZERO) < 0) {
                grandTotal = BigDecimal.ZERO;
            }

            req.setAttribute("cartItems", cartItems);
            req.setAttribute("addresses", addresses);
            req.setAttribute("subtotal", subtotal);
            req.setAttribute("coupon", coupon);
            req.setAttribute("discount", discount);
            req.setAttribute("grandTotal", grandTotal);

            req.getRequestDispatcher("/checkout.jsp").forward(req, resp);

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error loading checkout page", e);
            req.setAttribute("errorMessage", "Error loading checkout details.");
            req.getRequestDispatcher("/checkout.jsp").forward(req, resp);
        }
    }

    private void handleOrderSuccess(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");

        int orderId = ValidationUtil.parsePositiveInt(req.getParameter("id"), -1);
        if (orderId <= 0) {
            resp.sendRedirect(req.getContextPath() + "/orders");
            return;
        }

        try {
            Optional<Order> orderOpt = orderService.getOrderDetails(orderId, user.getUserId(), user.isAdmin());
            if (orderOpt.isEmpty()) {
                resp.sendRedirect(req.getContextPath() + "/orders");
                return;
            }

            req.setAttribute("order", orderOpt.get());
            req.getRequestDispatcher("/order-success.jsp").forward(req, resp);

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error loading order success page", e);
            resp.sendRedirect(req.getContextPath() + "/orders");
        }
    }
}
