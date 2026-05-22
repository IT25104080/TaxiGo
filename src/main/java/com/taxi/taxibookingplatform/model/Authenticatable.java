package com.taxi.taxibookingplatform.model;

public interface Authenticatable {
    boolean authenticate(String password);
    String getRole();
}
