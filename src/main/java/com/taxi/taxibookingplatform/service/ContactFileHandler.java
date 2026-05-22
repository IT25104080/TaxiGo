package com.taxi.taxibookingplatform.service;

import com.taxi.taxibookingplatform.model.ContactMessage;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ContactFileHandler {

    private static final String FILE_PATH = "data/contacts.txt";

    public static void save(ContactMessage msg) throws IOException {
        new File("data").mkdirs();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            writer.write(msg.toFileString());
            writer.newLine();
        }
    }

    public static List<ContactMessage> getAllMessages() throws IOException {
        List<ContactMessage> list = new ArrayList<>();
        File file = new File(FILE_PATH);
        if (!file.exists()) return list;

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(",", 7);
                if (parts.length >= 7 && "CONTACT".equals(parts[0])) {
                    String id = parts[1];
                    String name = parts[2].replace(";", ",").trim();
                    String email = parts[3].replace(";", ",").trim();
                    String subject = parts[4].replace(";", ",").trim();
                    String message = parts[5].replace(";", ",").trim();
                    LocalDateTime createdAt = LocalDateTime.parse(parts[6].trim());
                    list.add(new ContactMessage(id, name, email, subject, message, createdAt));
                }
            }
        }
        list.sort(Comparator.comparing(ContactMessage::getCreatedAt).reversed());
        return list;
    }

    public static void deleteMessage(String id) throws IOException {
        List<ContactMessage> all = getAllMessages();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, false))) {
            for (ContactMessage msg : all) {
                if (!msg.getId().equals(id)) {
                    writer.write(msg.toFileString());
                    writer.newLine();
                }
            }
        }
    }
}
