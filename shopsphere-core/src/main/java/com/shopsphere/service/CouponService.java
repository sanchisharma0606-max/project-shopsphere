package com.shopsphere.service;

import com.shopsphere.dao.CouponDAO;
import com.shopsphere.model.Coupon;
import com.shopsphere.util.ValidationUtil;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Service managing discount coupons and eligibility validation.
 */
public class CouponService {

    private final CouponDAO couponDAO;

    public CouponService() {
        this.couponDAO = new CouponDAO();
    }

    public CouponService(CouponDAO couponDAO) {
        this.couponDAO = couponDAO;
    }

    public Optional<Coupon> validateCoupon(String code, BigDecimal orderTotal, int userId) throws SQLException {
        if (!ValidationUtil.isNotEmpty(code)) {
            return Optional.empty();
        }

        Optional<Coupon> couponOpt = couponDAO.findByCode(code.trim());
        if (couponOpt.isEmpty()) {
            throw new IllegalArgumentException("Invalid coupon code.");
        }

        Coupon coupon = couponOpt.get();
        if (coupon.getStatus() != Coupon.Status.ACTIVE) {
            throw new IllegalArgumentException("This coupon is no longer active.");
        }

        if (coupon.isExpired()) {
            throw new IllegalArgumentException("This coupon has expired.");
        }

        if (orderTotal.compareTo(coupon.getMinimumOrder()) < 0) {
            throw new IllegalArgumentException("Minimum order amount of ₹" + coupon.getMinimumOrder() + " required to use this coupon.");
        }

        if (couponDAO.hasUserUsedCoupon(coupon.getCouponId(), userId)) {
            throw new IllegalArgumentException("You have already used this coupon.");
        }

        return Optional.of(coupon);
    }

    public List<Coupon> getAllCoupons() throws SQLException {
        return couponDAO.findAll();
    }

    public Optional<Coupon> getCouponById(int couponId) throws SQLException {
        return couponDAO.findById(couponId);
    }

    public int saveCoupon(Coupon coupon) throws SQLException {
        if (!ValidationUtil.isNotEmpty(coupon.getCode())) {
            throw new IllegalArgumentException("Coupon code is required.");
        }
        if (coupon.getDiscountValue() == null || coupon.getDiscountValue().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Discount value must be greater than 0.");
        }
        if (coupon.getExpiryDate() == null) {
            throw new IllegalArgumentException("Expiry date is required.");
        }

        if (coupon.getCouponId() > 0) {
            boolean updated = couponDAO.updateCoupon(coupon);
            return updated ? coupon.getCouponId() : -1;
        } else {
            return couponDAO.createCoupon(coupon);
        }
    }

    public boolean deleteCoupon(int couponId) throws SQLException {
        return couponDAO.deleteCoupon(couponId);
    }
}
