package com.shopsphere.dao;

import com.shopsphere.model.Address;
import com.shopsphere.model.Order;
import com.shopsphere.util.DBConnection;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Data Access Object for Orders and Administrative Analytics.
 */
public class OrderDAO {

    public int createOrder(Connection conn, Order order) throws SQLException {
        String sql = "INSERT INTO orders (user_id, address_id, total_amount, discount, payment_method, payment_status, order_status) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, order.getUserId());
            if (order.getAddressId() != null) {
                ps.setInt(2, order.getAddressId());
            } else {
                ps.setNull(2, Types.INTEGER);
            }
            ps.setBigDecimal(3, order.getTotalAmount());
            ps.setBigDecimal(4, order.getDiscount());
            ps.setString(5, order.getPaymentMethod().name());
            ps.setString(6, order.getPaymentStatus().name());
            ps.setString(7, order.getOrderStatus().name());

            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int id = rs.getInt(1);
                    order.setOrderId(id);
                    return id;
                }
            }
        }
        return -1;
    }

    public Optional<Order> findById(int orderId) throws SQLException {
        String sql = "SELECT o.*, u.name AS user_name, u.email AS user_email, " +
                     "a.address_line, a.city, a.state, a.pincode, a.address_type " +
                     "FROM orders o " +
                     "JOIN users u ON o.user_id = u.user_id " +
                     "LEFT JOIN addresses a ON o.address_id = a.address_id " +
                     "WHERE o.order_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToOrder(rs));
                }
            }
        }
        return Optional.empty();
    }

    public List<Order> findByUserId(int userId) throws SQLException {
        List<Order> list = new ArrayList<>();
        String sql = "SELECT o.*, u.name AS user_name, u.email AS user_email, " +
                     "a.address_line, a.city, a.state, a.pincode, a.address_type " +
                     "FROM orders o " +
                     "JOIN users u ON o.user_id = u.user_id " +
                     "LEFT JOIN addresses a ON o.address_id = a.address_id " +
                     "WHERE o.user_id = ? ORDER BY o.created_at DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToOrder(rs));
                }
            }
        }
        return list;
    }

    public List<Order> findAll() throws SQLException {
        List<Order> list = new ArrayList<>();
        String sql = "SELECT o.*, u.name AS user_name, u.email AS user_email, " +
                     "a.address_line, a.city, a.state, a.pincode, a.address_type " +
                     "FROM orders o " +
                     "JOIN users u ON o.user_id = u.user_id " +
                     "LEFT JOIN addresses a ON o.address_id = a.address_id " +
                     "ORDER BY o.created_at DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapResultSetToOrder(rs));
            }
        }
        return list;
    }

    public List<Order> findRecentOrders(int limit) throws SQLException {
        List<Order> list = new ArrayList<>();
        String sql = "SELECT o.*, u.name AS user_name, u.email AS user_email, " +
                     "a.address_line, a.city, a.state, a.pincode, a.address_type " +
                     "FROM orders o " +
                     "JOIN users u ON o.user_id = u.user_id " +
                     "LEFT JOIN addresses a ON o.address_id = a.address_id " +
                     "ORDER BY o.created_at DESC LIMIT ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, limit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToOrder(rs));
                }
            }
        }
        return list;
    }

    public boolean updateOrderStatus(int orderId, Order.OrderStatus newStatus) throws SQLException {
        String sql = "UPDATE orders SET order_status = ? WHERE order_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newStatus.name());
            ps.setInt(2, orderId);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean updatePaymentStatus(int orderId, Order.PaymentStatus newStatus) throws SQLException {
        String sql = "UPDATE orders SET payment_status = ? WHERE order_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newStatus.name());
            ps.setInt(2, orderId);
            return ps.executeUpdate() > 0;
        }
    }

    // Analytics queries for Admin Dashboard
    public int countTotalOrders() throws SQLException {
        String sql = "SELECT COUNT(*) FROM orders";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    public int countPendingOrders() throws SQLException {
        String sql = "SELECT COUNT(*) FROM orders WHERE order_status IN ('PLACED', 'CONFIRMED')";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    public BigDecimal calculateTotalSales() throws SQLException {
        String sql = "SELECT COALESCE(SUM(total_amount - discount), 0.00) FROM orders WHERE order_status != 'CANCELLED'";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getBigDecimal(1);
            }
        }
        return BigDecimal.ZERO;
    }

    private Order mapResultSetToOrder(ResultSet rs) throws SQLException {
        Order order = new Order(
                rs.getInt("order_id"),
                rs.getInt("user_id"),
                rs.getObject("address_id") != null ? rs.getInt("address_id") : null,
                rs.getBigDecimal("total_amount"),
                rs.getBigDecimal("discount"),
                Order.PaymentMethod.valueOf(rs.getString("payment_method")),
                Order.PaymentStatus.valueOf(rs.getString("payment_status")),
                Order.OrderStatus.valueOf(rs.getString("order_status")),
                rs.getTimestamp("created_at")
        );

        order.setUserName(rs.getString("user_name"));
        order.setUserEmail(rs.getString("user_email"));

        if (rs.getString("address_line") != null) {
            Address address = new Address();
            address.setAddressLine(rs.getString("address_line"));
            address.setCity(rs.getString("city"));
            address.setState(rs.getString("state"));
            address.setPincode(rs.getString("pincode"));
            address.setAddressType(Address.AddressType.valueOf(rs.getString("address_type")));
            order.setAddress(address);
        }

        return order;
    }
}
