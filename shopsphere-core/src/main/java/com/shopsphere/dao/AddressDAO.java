package com.shopsphere.dao;

import com.shopsphere.model.Address;
import com.shopsphere.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Data Access Object for Address persistence and ownership verification.
 */
public class AddressDAO {

    public int createAddress(Address address) throws SQLException {
        String sql = "INSERT INTO addresses (user_id, address_line, city, state, pincode, address_type) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, address.getUserId());
            ps.setString(2, address.getAddressLine());
            ps.setString(3, address.getCity());
            ps.setString(4, address.getState());
            ps.setString(5, address.getPincode());
            ps.setString(6, address.getAddressType().name());

            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int id = rs.getInt(1);
                    address.setAddressId(id);
                    return id;
                }
            }
        }
        return -1;
    }

    public Optional<Address> findById(int addressId) throws SQLException {
        String sql = "SELECT * FROM addresses WHERE address_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, addressId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToAddress(rs));
                }
            }
        }
        return Optional.empty();
    }

    public List<Address> findByUserId(int userId) throws SQLException {
        List<Address> list = new ArrayList<>();
        String sql = "SELECT * FROM addresses WHERE user_id = ? ORDER BY address_id DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToAddress(rs));
                }
            }
        }
        return list;
    }

    public boolean updateAddress(Address address) throws SQLException {
        // Enforce user_id match for ownership security
        String sql = "UPDATE addresses SET address_line = ?, city = ?, state = ?, pincode = ?, address_type = ? WHERE address_id = ? AND user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, address.getAddressLine());
            ps.setString(2, address.getCity());
            ps.setString(3, address.getState());
            ps.setString(4, address.getPincode());
            ps.setString(5, address.getAddressType().name());
            ps.setInt(6, address.getAddressId());
            ps.setInt(7, address.getUserId());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean deleteAddress(int addressId, int userId) throws SQLException {
        // Ownership check: only the owner can delete
        String sql = "DELETE FROM addresses WHERE address_id = ? AND user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, addressId);
            ps.setInt(2, userId);
            return ps.executeUpdate() > 0;
        }
    }

    private Address mapResultSetToAddress(ResultSet rs) throws SQLException {
        return new Address(
                rs.getInt("address_id"),
                rs.getInt("user_id"),
                rs.getString("address_line"),
                rs.getString("city"),
                rs.getString("state"),
                rs.getString("pincode"),
                Address.AddressType.valueOf(rs.getString("address_type")),
                rs.getTimestamp("created_at")
        );
    }
}
