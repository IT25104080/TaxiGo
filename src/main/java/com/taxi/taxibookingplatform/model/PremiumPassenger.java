package com.taxi.taxibookingplatform.model;

import java.time.LocalDate;

public class PremiumPassenger extends User {

    private String membershipLevel;
    private double discountRate;
    private int loyaltyPoints;

    public PremiumPassenger(String userId, String name, String email, String password,
            String phone, LocalDate registeredDate,
            String membershipLevel, double discountRate, int loyaltyPoints) {
        super(userId, name, email, password, phone, registeredDate);
        this.membershipLevel = membershipLevel;
        this.discountRate = discountRate;
        this.loyaltyPoints = loyaltyPoints;
    }

    public String getMembershipLevel() {
        return membershipLevel;
    }

    public void setMembershipLevel(String membershipLevel) {
        this.membershipLevel = membershipLevel;
    }

    public double getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(double discountRate) {
        this.discountRate = discountRate;
    }

    public int getLoyaltyPoints() {
        return loyaltyPoints;
    }

    public void setLoyaltyPoints(int loyaltyPoints) {
        this.loyaltyPoints = loyaltyPoints;
    }

    public void addLoyaltyPoints(int points) {
        this.loyaltyPoints += points;
    }

    public double applyDiscount(double originalFare) {
        return originalFare * (1 - discountRate);
    }

    @Override
    public String getRole() {
        return "PREMIUM";
    }

    @Override
    public String getDisplayInfo() {
        return "Premium Customer: " + getName() + " | Level: " + membershipLevel
                + " | Points: " + loyaltyPoints;
    }

    @Override
    public String toFileString() {
        return "PREMIUM," + getUserId() + "," + getName() + "," + getEmail() + ","
                + getPassword() + "," + getPhone() + "," + getRegisteredDate() + ","
                + membershipLevel + "," + discountRate + "," + loyaltyPoints;
    }
}
