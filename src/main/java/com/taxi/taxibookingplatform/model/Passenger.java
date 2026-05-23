package com.taxi.taxibookingplatform.model;

import java.time.LocalDate;

public class Passenger extends User {

    private String address;
    private String preferredPayment;

    public Passenger(String userId, String name, String email, String password,
            String phone, LocalDate registeredDate,
            String address, String preferredPayment) {
        super(userId, name, email, password, phone, registeredDate);
        this.address = address;
        this.preferredPayment = preferredPayment;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPreferredPayment() {
        return preferredPayment;
    }

    public void setPreferredPayment(String preferredPayment) {
        this.preferredPayment = preferredPayment;
    }

    @Override
    public String getRole() {
        return "CUSTOMER";
    }

    @Override
    public String getDisplayInfo() {
        return "Customer: " + getName() + " | Email: " + getEmail();
    }

    @Override
    public String toFileString() {
        return "PASSENGER," + getUserId() + "," + getName() + "," + getEmail() + ","
                + getPassword() + "," + getPhone() + "," + getRegisteredDate() + ","
                + address + "," + preferredPayment;
    }
}
