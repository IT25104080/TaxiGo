package com.taxi.taxibookingplatform.model;

/**
 * ============================================================================
 * OOP CONCEPT: ABSTRACTION (Interfaces)
 * ============================================================================
 * Abstraction is the practice of exposing essential interfaces while hiding 
 * implementation details. 
 * 
 * Here, 'Authenticatable' serves as a pure abstract contract. It declares 
 * 'what' operations any security-related entity must perform (authenticate, getRole) 
 * without specifying 'how' those operations are executed. Concrete classes are 
 * forced to implement these behaviors according to their specific rules.
 * ============================================================================
 */
public interface Authenticatable {
    boolean authenticate(String password);
    String getRole();
}
