package com.taxi.taxibookingplatform.model;

import java.time.LocalDate;

/**
 * ============================================================================
 * OOP CONCEPTS: INHERITANCE, ENCAPSULATION & POLYMORPHISM
 * ============================================================================
 * 1. INHERITANCE:
 *    The 'Passenger' class extends 'User' ('Passenger extends User'). This means 
 *    it inherits all fields (userId, name, etc.) and methods from 'User', 
 *    eliminating code duplication and establishing an "IS-A" relationship 
 *    (a Passenger IS-A User).
 * 
 * 2. ENCAPSULATION:
 *    The class encapsulates specific fields 'address' and 'preferredPayment' 
 *    privately, providing public accessors to prevent unauthorized tampering.
 * 
 * 3. POLYMORPHISM:
 *    The methods 'getRole()', 'getDisplayInfo()', and 'toFileString()' override 
 *    the base class/interface signatures. When these methods are invoked on a 
 *    'User' reference dynamically containing a 'Passenger' object, runtime 
 *    polymorphism guarantees that 'Passenger''s implementations are executed.
 * ============================================================================
 */
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

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getPreferredPayment() { return preferredPayment; }
    public void setPreferredPayment(String preferredPayment) { this.preferredPayment = preferredPayment; }

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
