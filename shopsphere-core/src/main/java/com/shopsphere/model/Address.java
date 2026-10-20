package com.shopsphere.model;

import java.io.Serial;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Model representing a customer's postal address.
 */
public class Address implements Serializable {
    @Serial
    private static final long serialVersionUID = 1002L;

    public enum AddressType {
        HOME, WORK, OTHER
    }

    private int addressId;
    private int userId;
    private String addressLine;
    private String city;
    private String state;
    private String pincode;
    private AddressType addressType = AddressType.HOME;
    private Timestamp createdAt;

    public Address() {}

    public Address(int addressId, int userId, String addressLine, String city, String state, String pincode, AddressType addressType, Timestamp createdAt) {
        this.addressId = addressId;
        this.userId = userId;
        this.addressLine = addressLine;
        this.city = city;
        this.state = state;
        this.pincode = pincode;
        this.addressType = addressType != null ? addressType : AddressType.HOME;
        this.createdAt = createdAt;
    }

    public Address(int userId, String addressLine, String city, String state, String pincode, AddressType addressType) {
        this.userId = userId;
        this.addressLine = addressLine;
        this.city = city;
        this.state = state;
        this.pincode = pincode;
        this.addressType = addressType != null ? addressType : AddressType.HOME;
    }

    public int getAddressId() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getAddressLine() {
        return addressLine;
    }

    public void setAddressLine(String addressLine) {
        this.addressLine = addressLine;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public AddressType getAddressType() {
        return addressType;
    }

    public void setAddressType(AddressType addressType) {
        this.addressType = addressType;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public String getFormattedAddress() {
        return addressLine + ", " + city + ", " + state + " - " + pincode + " (" + addressType + ")";
    }

    @Override
    public String toString() {
        return getFormattedAddress();
    }
}
