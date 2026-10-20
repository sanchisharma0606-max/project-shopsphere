package com.shopsphere.model;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Model representing a payment transaction record.
 */
public class Payment implements Serializable {
    @Serial
    private static final long serialVersionUID = 1009L;

    public enum Status {
        PENDING, COMPLETED, FAILED, REFUNDED
    }

    private int paymentId;
    private int orderId;
    private String paymentMethod;
    private String transactionReference;
    private BigDecimal amount = BigDecimal.ZERO;
    private Status paymentStatus = Status.PENDING;
    private Timestamp createdAt;

    public Payment() {}

    public Payment(int paymentId, int orderId, String paymentMethod, String transactionReference,
                   BigDecimal amount, Status paymentStatus, Timestamp createdAt) {
        this.paymentId = paymentId;
        this.orderId = orderId;
        this.paymentMethod = paymentMethod;
        this.transactionReference = transactionReference;
        this.amount = amount != null ? amount : BigDecimal.ZERO;
        this.paymentStatus = paymentStatus != null ? paymentStatus : Status.PENDING;
        this.createdAt = createdAt;
    }

    public int getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getTransactionReference() {
        return transactionReference;
    }

    public void setTransactionReference(String transactionReference) {
        this.transactionReference = transactionReference;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount != null ? amount : BigDecimal.ZERO;
    }

    public Status getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(Status paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Payment{" +
                "paymentId=" + paymentId +
                ", orderId=" + orderId +
                ", transactionReference='" + transactionReference + '\'' +
                ", amount=" + amount +
                ", paymentStatus=" + paymentStatus +
                '}';
    }
}
