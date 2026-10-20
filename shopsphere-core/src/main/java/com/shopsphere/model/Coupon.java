package com.shopsphere.model;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.time.LocalDate;

/**
 * Model representing a discount Coupon in ShopSphere.
 */
public class Coupon implements Serializable {
    @Serial
    private static final long serialVersionUID = 1011L;

    public enum DiscountType {
        PERCENTAGE, FIXED
    }

    public enum Status {
        ACTIVE, INACTIVE
    }

    private int couponId;
    private String code;
    private DiscountType discountType = DiscountType.PERCENTAGE;
    private BigDecimal discountValue = BigDecimal.ZERO;
    private BigDecimal minimumOrder = BigDecimal.ZERO;
    private BigDecimal maximumDiscount; // nullable for unlimited or capped
    private Date expiryDate;
    private Status status = Status.ACTIVE;

    public Coupon() {}

    public Coupon(int couponId, String code, DiscountType discountType, BigDecimal discountValue,
                  BigDecimal minimumOrder, BigDecimal maximumDiscount, Date expiryDate, Status status) {
        this.couponId = couponId;
        this.code = code;
        this.discountType = discountType != null ? discountType : DiscountType.PERCENTAGE;
        this.discountValue = discountValue != null ? discountValue : BigDecimal.ZERO;
        this.minimumOrder = minimumOrder != null ? minimumOrder : BigDecimal.ZERO;
        this.maximumDiscount = maximumDiscount;
        this.expiryDate = expiryDate;
        this.status = status != null ? status : Status.ACTIVE;
    }

    public int getCouponId() {
        return couponId;
    }

    public void setCouponId(int couponId) {
        this.couponId = couponId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public DiscountType getDiscountType() {
        return discountType;
    }

    public void setDiscountType(DiscountType discountType) {
        this.discountType = discountType;
    }

    public BigDecimal getDiscountValue() {
        return discountValue;
    }

    public void setDiscountValue(BigDecimal discountValue) {
        this.discountValue = discountValue != null ? discountValue : BigDecimal.ZERO;
    }

    public BigDecimal getMinimumOrder() {
        return minimumOrder;
    }

    public void setMinimumOrder(BigDecimal minimumOrder) {
        this.minimumOrder = minimumOrder != null ? minimumOrder : BigDecimal.ZERO;
    }

    public BigDecimal getMaximumDiscount() {
        return maximumDiscount;
    }

    public void setMaximumDiscount(BigDecimal maximumDiscount) {
        this.maximumDiscount = maximumDiscount;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public boolean isExpired() {
        if (expiryDate == null) {
            return false;
        }
        return expiryDate.toLocalDate().isBefore(LocalDate.now());
    }

    public boolean isValidForTotal(BigDecimal orderTotal) {
        if (status != Status.ACTIVE || isExpired()) {
            return false;
        }
        if (orderTotal == null) {
            return false;
        }
        return orderTotal.compareTo(minimumOrder != null ? minimumOrder : BigDecimal.ZERO) >= 0;
    }

    public BigDecimal calculateDiscount(BigDecimal orderTotal) {
        if (!isValidForTotal(orderTotal)) {
            return BigDecimal.ZERO;
        }

        BigDecimal calculated;
        if (discountType == DiscountType.PERCENTAGE) {
            calculated = orderTotal.multiply(discountValue)
                    .divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
        } else {
            calculated = discountValue;
        }

        if (maximumDiscount != null && maximumDiscount.compareTo(BigDecimal.ZERO) > 0) {
            if (calculated.compareTo(maximumDiscount) > 0) {
                calculated = maximumDiscount;
            }
        }

        // Discount cannot exceed the order total
        if (calculated.compareTo(orderTotal) > 0) {
            calculated = orderTotal;
        }

        return calculated.setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public String toString() {
        return "Coupon{" +
                "code='" + code + '\'' +
                ", discountType=" + discountType +
                ", discountValue=" + discountValue +
                '}';
    }
}
