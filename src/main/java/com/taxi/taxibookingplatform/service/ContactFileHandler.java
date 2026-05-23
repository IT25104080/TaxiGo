package com.taxi.taxibookingplatform.service;

import com.taxi.taxibookingplatform.model.ContactMessage;
import org.springframework.stereotype.Service;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * ============================================================================
 * OOP CONCEPT: ENCAPSULATION & SINGLETON SERVICE PATTERN
 * ============================================================================
 * 1. ENCAPSULATION:
 *    Encapsulates contact messages persistent operations and list sorting algorithms.
 *    External classes have no access to the underlying flat file structure.
 * ============================================================================
 */
@Service
public class ContactFileHandler {

    private final String filePath = "data/contacts.txt";

    public void save(ContactMessage msg) throws IOException {
        // Create the data directory if it doesn't exist
        new File("data").mkdirs();
        // Append the contact message to the file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(msg.toFileString());
            writer.newLine();
        }
    }

    public List<ContactMessage> getAllMessages() throws IOException {
        // Initialize list to store contact messages
        List<ContactMessage> list = new ArrayList<>();
        File file = new File(filePath);
        // Return empty list if file doesn't exist
        if (!file.exists()) return list;

        // Read all lines from the contacts file
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                // Parse CSV-formatted line into components
                String[] parts = line.split(",", 7);
                // Validate line format and extract contact message
                if (parts.length >= 7 && "CONTACT".equals(parts[0])) {
                    // Extract and decode fields from parsed parts
                    String id = parts[1];
                    String name = parts[2].replace(";", ",").trim();
                    String email = parts[3].replace(";", ",").trim();
                    String subject = parts[4].replace(";", ",").trim();
                    String message = parts[5].replace(";", ",").trim();
                    LocalDateTime createdAt = LocalDateTime.parse(parts[6].trim());
                    // Add newly created contact message to list
                    list.add(new ContactMessage(id, name, email, subject, message, createdAt));
                }
            }
        }
        // Sort messages by creation date in descending order
        list.sort(Comparator.comparing(ContactMessage::getCreatedAt).reversed());
        return list;
    }

    public void deleteMessage(String id) throws IOException {
        // Retrieve all existing contact messages
        List<ContactMessage> all = getAllMessages();
        // Rewrite file with all messages except the one to delete
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, false))) {
            for (ContactMessage msg : all) {
                // Skip the message with matching ID
                if (!msg.getId().equals(id)) {
                    writer.write(msg.toFileString());
                    writer.newLine();
                }
            }
        }
    }
}
