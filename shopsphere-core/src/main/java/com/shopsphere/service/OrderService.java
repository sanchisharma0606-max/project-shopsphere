package com.shopsphere.service;

import com.shopsphere.dao.*;
import com.shopsphere.model.Order;
import com.shopsphere.model.OrderItem;
import com.shopsphere.util.DBConnection;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Service managing order viewing, cancellation with inventory replenishment,
 * and admin dashboard analytical aggregations.
 */
public class OrderService {

    private static final Logger LOGGER = Logger.getLogger(OrderService.class.getName());

    private final OrderDAO orderDAO;
    private final OrderItemDAO orderItemDAO;
    private final ProductDAO productDAO;
    private final UserDAO userDAO;

    public OrderService() {
        this.orderDAO = new OrderDAO();
        this.orderItemDAO = new OrderItemDAO();
        this.productDAO = new ProductDAO();
        this.userDAO = new UserDAO();
    }

    public OrderService(OrderDAO orderDAO, OrderItemDAO orderItemDAO, ProductDAO productDAO, UserDAO userDAO) {
        this.orderDAO = orderDAO;
        this.orderItemDAO = orderItemDAO;
        this.productDAO = productDAO;
        this.userDAO = userDAO;
    }

    public List<Order> getCustomerOrders(int userId) throws SQLException {
        List<Order> orders = orderDAO.findByUserId(userId);
        for (Order o : orders) {
            o.setItems(orderItemDAO.findByOrderId(o.getOrderId()));
        }
        return orders;
    }

    public Optional<Order> getOrderDetails(int orderId, int requestingUserId, boolean isAdmin) throws SQLException {
        Optional<Order> orderOpt = orderDAO.findById(orderId);
        if (orderOpt.isEmpty()) {
            return Optional.empty();
        }

        Order order = orderOpt.get();
        // Security check: non-admin can only view their own order
        if (!isAdmin && order.getUserId() != requestingUserId) {
            return Optional.empty();
        }

        order.setItems(orderItemDAO.findByOrderId(orderId));
        return Optional.of(order);
    }

    public List<Order> getAllOrders() throws SQLException {
        List<Order> orders = orderDAO.findAll();
        for (Order o : orders) {
            o.setItems(orderItemDAO.findByOrderId(o.getOrderId()));
        }
        return orders;
    }

    public List<Order> getRecentOrders(int limit) throws SQLException {
        List<Order> orders = orderDAO.findRecentOrders(limit);
        for (Order o : orders) {
            o.setItems(orderItemDAO.findByOrderId(o.getOrderId()));
        }
        return orders;
    }

    public boolean updateOrderStatus(int orderId, Order.OrderStatus newStatus) throws SQLException {
        return orderDAO.updateOrderStatus(orderId, newStatus);
    }

    public boolean updatePaymentStatus(int orderId, Order.PaymentStatus newStatus) throws SQLException {
        return orderDAO.updatePaymentStatus(orderId, newStatus);
    }

    /**
     * Cancels an order and replenishes inventory back to the warehouse inside a transaction.
     */
    public boolean cancelOrder(int orderId, int requestingUserId, boolean isAdmin) throws SQLException {
        Optional<Order> orderOpt = orderDAO.findById(orderId);
        if (orderOpt.isEmpty()) {
            return false;
        }

        Order order = orderOpt.get();
        if (!isAdmin && order.getUserId() != requestingUserId) {
            throw new SecurityException("Unauthorized access to cancel order #" + orderId);
        }

        if (order.getOrderStatus() == Order.OrderStatus.DELIVERED || order.getOrderStatus() == Order.OrderStatus.CANCELLED) {
            throw new IllegalStateException("Order cannot be cancelled in state " + order.getOrderStatus());
        }

        List<OrderItem> items = orderItemDAO.findByOrderId(orderId);

        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            // 1. Update order status to CANCELLED
            String updateSql = "UPDATE orders SET order_status = 'CANCELLED' WHERE order_id = ?";
            try (var ps = conn.prepareStatement(updateSql)) {
                ps.setInt(1, orderId);
                ps.executeUpdate();
            }

            // 2. Replenish inventory for all items
            for (OrderItem item : items) {
                productDAO.replenishStock(conn, item.getProductId(), item.getQuantity());
            }

            conn.commit();
            return true;
        } catch (Exception e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ignored) {}
            }
            LOGGER.log(Level.SEVERE, "Failed to cancel order #" + orderId, e);
            throw new SQLException("Failed to cancel order: " + e.getMessage(), e);
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException ignored) {}
            }
        }
    }

    /**
     * Aggregates real administrative dashboard metrics.
     */
    public Map<String, Object> getAdminDashboardStats() throws SQLException {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalProducts", productDAO.countTotalProducts());
        stats.put("totalCustomers", userDAO.countTotalCustomers());
        stats.put("totalOrders", orderDAO.countTotalOrders());
        stats.put("pendingOrders", orderDAO.countPendingOrders());
        stats.put("totalSales", orderDAO.calculateTotalSales());
        stats.put("lowStockCount", productDAO.findLowStock(10).size());
        stats.put("recentOrders", getRecentOrders(5));
        return stats;
    }
}
