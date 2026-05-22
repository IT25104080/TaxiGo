package com.taxi.taxibookingplatform.service;

import com.taxi.taxibookingplatform.model.User;
import com.taxi.taxibookingplatform.model.Passenger;
import com.taxi.taxibookingplatform.model.PremiumPassenger;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class UserFileHandler {

    private static final String FILE_PATH = "data/users.txt";

    public static void addUser(User user) throws IOException {
        new File("data").mkdirs();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            writer.write(user.toFileString());
            writer.newLine();
        }
    }

    public static List<User> getAllUsers() throws IOException {
        List<User> userList = new ArrayList<>();
        File file = new File(FILE_PATH);
        if (!file.exists()) return userList;

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(",");
                if (parts.length < 2) continue;
                String type = parts[0];

                if (type.equals("PASSENGER") && parts.length >= 9) {
                    Passenger p = new Passenger(
                        parts[1], parts[2], parts[3], parts[4],
                        parts[5], LocalDate.parse(parts[6]),
                        parts[7], parts[8]
                    );
                    userList.add(p);
                } else if (type.equals("PREMIUM") && parts.length >= 10) {
                    PremiumPassenger pp = new PremiumPassenger(
                        parts[1], parts[2], parts[3], parts[4],
                        parts[5], LocalDate.parse(parts[6]),
                        parts[7], Double.parseDouble(parts[8]),
                        Integer.parseInt(parts[9])
                    );
                    userList.add(pp);
                }
            }
        }
        return userList;
    }

    public static User getUserById(String userId) throws IOException {
        for (User u : getAllUsers()) {
            if (u.getUserId().equals(userId)) return u;
        }
        return null;
    }

    public static User getUserByEmail(String email) throws IOException {
        for (User u : getAllUsers()) {
            if (u.getEmail().equalsIgnoreCase(email)) return u;
        }
        return null;
    }

    public static void updateUser(User updatedUser) throws IOException {
        List<User> allUsers = getAllUsers();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, false))) {
            for (User u : allUsers) {
                writer.write(u.getUserId().equals(updatedUser.getUserId())
                             ? updatedUser.toFileString() : u.toFileString());
                writer.newLine();
            }
        }
    }

    public static void deleteUser(String userId) throws IOException {
        List<User> allUsers = getAllUsers();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, false))) {
            for (User u : allUsers) {
                if (!u.getUserId().equals(userId)) {
                    writer.write(u.toFileString());
                    writer.newLine();
                }
            }
        }
    }
}
