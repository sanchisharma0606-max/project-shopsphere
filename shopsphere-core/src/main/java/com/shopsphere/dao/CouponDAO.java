package com.shopsphere.dao;

import com.shopsphere.model.Coupon;
import com.shopsphere.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Data Access Object for Coupons and Coupon Usage tracking.
 */
public class CouponDAO {

    public int createCoupon(Coupon coupon) throws SQLException {
        String sql = "INSERT INTO coupons (code, discount_type, discount_value, minimum_order, maximum_discount, expiry_date, status) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, coupon.getCode().toUpperCase().trim());
            ps.setString(2, coupon.getDiscountType().name());
            ps.setBigDecimal(3, coupon.getDiscountValue());
            ps.setBigDecimal(4, coupon.getMinimumOrder());
            if (coupon.getMaximumDiscount() != null) {
                ps.setBigDecimal(5, coupon.getMaximumDiscount());
            } else {
                ps.setNull(5, Types.DECIMAL);
            }
            ps.setDate(6, coupon.getExpiryDate());
            ps.setString(7, coupon.getStatus().name());

            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int id = rs.getInt(1);
                    coupon.setCouponId(id);
                    return id;
                }
            }
        }
        return -1;
    }

    public Optional<Coupon> findByCode(String code) throws SQLException {
        String sql = "SELECT * FROM coupons WHERE UPPER(code) = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, code.toUpperCase().trim());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToCoupon(rs));
                }
            }
        }
        return Optional.empty();
    }

    public Optional<Coupon> findById(int couponId) throws SQLException {
        String sql = "SELECT * FROM coupons WHERE coupon_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, couponId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToCoupon(rs));
                }
            }
        }
        return Optional.empty();
    }

    public List<Coupon> findAll() throws SQLException {
        List<Coupon> list = new ArrayList<>();
        String sql = "SELECT * FROM coupons ORDER BY coupon_id DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapResultSetToCoupon(rs));
            }
        }
        return list;
    }

    public boolean updateCoupon(Coupon coupon) throws SQLException {
        String sql = "UPDATE coupons SET code = ?, discount_type = ?, discount_value = ?, " +
                     "minimum_order = ?, maximum_discount = ?, expiry_date = ?, status = ? WHERE coupon_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, coupon.getCode().toUpperCase().trim());
            ps.setString(2, coupon.getDiscountType().name());
            ps.setBigDecimal(3, coupon.getDiscountValue());
            ps.setBigDecimal(4, coupon.getMinimumOrder());
            if (coupon.getMaximumDiscount() != null) {
                ps.setBigDecimal(5, coupon.getMaximumDiscount());
            } else {
                ps.setNull(5, Types.DECIMAL);
            }
            ps.setDate(6, coupon.getExpiryDate());
            ps.setString(7, coupon.getStatus().name());
            ps.setInt(8, coupon.getCouponId());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean deleteCoupon(int couponId) throws SQLException {
        String sql = "DELETE FROM coupons WHERE coupon_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, couponId);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean recordUsage(Connection conn, int couponId, int userId, int orderId) throws SQLException {
        String sql = "INSERT INTO coupon_usage (coupon_id, user_id, order_id) VALUES (?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, couponId);
            ps.setInt(2, userId);
            ps.setInt(3, orderId);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean hasUserUsedCoupon(int couponId, int userId) throws SQLException {
        String sql = "SELECT 1 FROM coupon_usage WHERE coupon_id = ? AND user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, couponId);
            ps.setInt(2, userId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    private Coupon mapResultSetToCoupon(ResultSet rs) throws SQLException {
        return new Coupon(
                rs.getInt("coupon_id"),
                rs.getString("code"),
                Coupon.DiscountType.valueOf(rs.getString("discount_type")),
                rs.getBigDecimal("discount_value"),
                rs.getBigDecimal("minimum_order"),
                rs.getBigDecimal("maximum_discount"),
                rs.getDate("expiry_date"),
                Coupon.Status.valueOf(rs.getString("status"))
        );
    }
}
