package com.taxi.taxibookingplatform.model;

import java.time.LocalDateTime;

public class ContactMessage {

    // Unique identifier for the contact message
    private final String id;
    // Sender's name
    private final String name;
    // Sender's email address
    private final String email;
    // Message subject line
    private final String subject;
    // Main message content
    private final String message;
    // Timestamp when the message was created
    private final LocalDateTime createdAt;

    // Constructor to initialize a new contact message with all required fields
    public ContactMessage(String id, String name, String email, String subject,
                          String message, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.subject = subject;
        this.message = message;
        this.createdAt = createdAt;
    }

    // Converts the contact message to a file-format string with escaped special characters
    public String toFileString() {
        return "CONTACT," + id + "," + escape(name) + "," + escape(email) + ","
                + escape(subject) + "," + escape(message) + "," + createdAt;
    }

    // Escapes special characters (commas and newlines) to prevent CSV parsing issues
    private static String escape(String s) {
        return s == null ? "" : s.replace(",", ";").replace("\n", " ");
    }

    // Getter methods for all contact message fields
    public String getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getSubject() { return subject; }
    public String getMessage() { return message; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
