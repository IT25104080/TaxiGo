package com.taxi.taxibookingplatform.service;

import com.taxi.taxibookingplatform.model.User;
import com.taxi.taxibookingplatform.model.Passenger;
import com.taxi.taxibookingplatform.model.PremiumPassenger;
import org.springframework.stereotype.Service;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserFileHandler {

    private final String filePath = "data/users.txt";

    public void addUser(User user) throws IOException {
        new File("data").mkdirs();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(user.toFileString());
            writer.newLine();
        }
    }

    public List<User> getAllUsers() throws IOException {
        List<User> userList = new ArrayList<>();
        File file = new File(filePath);
        if (!file.exists())
            return userList;

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty())
                    continue;
                String[] parts = line.split(",");
                if (parts.length < 2)
                    continue;
                String type = parts[0];

                if (type.equals("PASSENGER") && parts.length >= 9) {
                    Passenger p = new Passenger(
                            parts[1], parts[2], parts[3], parts[4],
                            parts[5], LocalDate.parse(parts[6]),
                            parts[7], parts[8]);
                    userList.add(p);
                } else if (type.equals("PREMIUM") && parts.length >= 10) {
                    PremiumPassenger pp = new PremiumPassenger(
                            parts[1], parts[2], parts[3], parts[4],
                            parts[5], LocalDate.parse(parts[6]),
                            parts[7], Double.parseDouble(parts[8]),
                            Integer.parseInt(parts[9]));
                    userList.add(pp);
                }
            }
        }
        return userList;
    }

    public User getUserById(String userId) throws IOException {
        for (User u : getAllUsers()) {
            if (u.getUserId().equals(userId))
                return u;
        }
        return null;
    }

    public User getUserByEmail(String email) throws IOException {
        for (User u : getAllUsers()) {
            if (u.getEmail().equalsIgnoreCase(email))
                return u;
        }
        return null;
    }

    public void updateUser(User updatedUser) throws IOException {
        List<User> allUsers = getAllUsers();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, false))) {
            for (User u : allUsers) {
                writer.write(u.getUserId().equals(updatedUser.getUserId())
                        ? updatedUser.toFileString()
                        : u.toFileString());
                writer.newLine();
            }
        }
    }

    public void deleteUser(String userId) throws IOException {
        List<User> allUsers = getAllUsers();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, false))) {
            for (User u : allUsers) {
                if (!u.getUserId().equals(userId)) {
                    writer.write(u.toFileString());
                    writer.newLine();
                }
            }
        }
    }
}
