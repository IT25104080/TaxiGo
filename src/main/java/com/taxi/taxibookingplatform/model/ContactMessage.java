package com.taxi.taxibookingplatform.model;

import java.time.LocalDateTime;

public class ContactMessage {

    private final String id;
    private final String name;
    private final String email;
    private final String subject;
    private final String message;
    private final LocalDateTime createdAt;

    public ContactMessage(String id, String name, String email, String subject,
                          String message, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.subject = subject;
        this.message = message;
        this.createdAt = createdAt;
    }

    public String toFileString() {
        return "CONTACT," + id + "," + escape(name) + "," + escape(email) + ","
                + escape(subject) + "," + escape(message) + "," + createdAt;
    }

    private static String escape(String s) {
        return s == null ? "" : s.replace(",", ";").replace("\n", " ");
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getSubject() { return subject; }
    public String getMessage() { return message; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
