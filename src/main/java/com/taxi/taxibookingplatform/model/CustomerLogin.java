package com.taxi.taxibookingplatform.model;

public class CustomerLogin {

    public boolean login(User user, String email, String password) {
        if (user.getEmail().equals(email) && user.authenticate(password)) {
            return true;
        }
        return false;
    }
}
