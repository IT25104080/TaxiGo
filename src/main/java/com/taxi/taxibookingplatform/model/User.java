package com.taxi.taxibookingplatform.model;

import java.time.LocalDate;

/**
 * ============================================================================
 * OOP CONCEPT: ABSTRACTION (Abstract Classes) & ENCAPSULATION
 * ============================================================================
 * 1. ABSTRACTION: 
 *    An abstract class (like 'User') provides a common framework and partial 
 *    implementation for a family of classes, but cannot be directly instantiated 
 *    using the 'new' keyword. It defines abstract methods ('toFileString' and 
 *    'getDisplayInfo') which subclasses MUST implement, defining a blueprint 
 *    for specialized user types.
 * 
 * 2. ENCAPSULATION:
 *    The data members (userId, name, email, password, phone, registeredDate) 
 *    are declared 'private'. This restricts direct access from external classes, 
 *    preventing arbitrary modification. The only way to access or mutate these 
 *    attributes is through public accessor (getter) and mutator (setter) methods, 
 *    which can control validations and safeguard state integrity.
 * ============================================================================
 */
public abstract class User implements Authenticatable {

    private String userId;
    private String name;
    private String email;
    private String password;
    private String phone;
    private LocalDate registeredDate;

    public User(String userId, String name, String email, String password,
                String phone, LocalDate registeredDate) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.registeredDate = registeredDate;
    }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public LocalDate getRegisteredDate() { return registeredDate; }
    public void setRegisteredDate(LocalDate registeredDate) { this.registeredDate = registeredDate; }

    @Override
    public boolean authenticate(String password) {
        return this.password.equals(password);
    }

    public abstract String toFileString();
    public abstract String getDisplayInfo();

    @Override
    public String toString() {
        return userId + " | " + name + " | " + email + " | " + phone + " | " + registeredDate;
    }
}
