package com.shopsphere.dao;

import com.shopsphere.model.Payment;
import com.shopsphere.util.DBConnection;

import java.sql.*;
import java.util.Optional;

/**
 * Data Access Object for Payment transaction tracking.
 */
public class PaymentDAO {

    public int createPayment(Connection conn, Payment payment) throws SQLException {
        String sql = "INSERT INTO payments (order_id, payment_method, transaction_reference, amount, payment_status) " +
                     "VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, payment.getOrderId());
            ps.setString(2, payment.getPaymentMethod());
            ps.setString(3, payment.getTransactionReference());
            ps.setBigDecimal(4, payment.getAmount());
            ps.setString(5, payment.getPaymentStatus().name());

            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int id = rs.getInt(1);
                    payment.setPaymentId(id);
                    return id;
                }
            }
        }
        return -1;
    }

    public Optional<Payment> findByOrderId(int orderId) throws SQLException {
        String sql = "SELECT * FROM payments WHERE order_id = ? ORDER BY payment_id DESC LIMIT 1";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new Payment(
                            rs.getInt("payment_id"),
                            rs.getInt("order_id"),
                            rs.getString("payment_method"),
                            rs.getString("transaction_reference"),
                            rs.getBigDecimal("amount"),
                            Payment.Status.valueOf(rs.getString("payment_status")),
                            rs.getTimestamp("created_at")
                    ));
                }
            }
        }
        return Optional.empty();
    }
}
