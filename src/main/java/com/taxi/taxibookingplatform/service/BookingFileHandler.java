package com.taxi.taxibookingplatform.service;

import com.taxi.taxibookingplatform.model.Booking;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class BookingFileHandler {

    private static final String FILE_PATH = "data/booking.txt";

    public static void addBooking(Booking booking) throws IOException {
        new File("data").mkdirs();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            writer.write(booking.toFileString());
            writer.newLine();
        }
    }

    public static List<Booking> getAllBookings() throws IOException {
        List<Booking> list = new ArrayList<>();
        File file = new File(FILE_PATH);
        if (!file.exists()) return list;

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                Booking b = Booking.fromFileLine(line);
                if (b != null) list.add(b);
            }
        }
        list.sort(Comparator.comparing(Booking::getCreatedAt).reversed());
        return list;
    }

    public static List<Booking> getBookingsByUserId(String userId) throws IOException {
        return getAllBookings().stream()
                .filter(b -> b.getUserId().equals(userId))
                .toList();
    }

    public static Booking getBookingById(String bookingId) throws IOException {
        return getAllBookings().stream()
                .filter(b -> b.getBookingId().equals(bookingId))
                .findFirst()
                .orElse(null);
    }

    public static void updateStatus(String bookingId, String status) throws IOException {
        List<Booking> all = getAllBookings();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, false))) {
            for (Booking b : all) {
                Booking out = b.getBookingId().equals(bookingId)
                        ? new Booking(b.getBookingId(), b.getUserId(), b.getPickup(), b.getDropoff(),
                        b.getPickupDate(), b.getPickupTime(), b.getVehicleType(), status,
                        b.getFare(), b.getNotes(), b.getCreatedAt())
                        : b;
                writer.write(out.toFileString());
                writer.newLine();
            }
        }
    }

    public static void cancelBooking(String bookingId, String userId) throws IOException {
        Booking b = getBookingById(bookingId);
        if (b != null && b.getUserId().equals(userId) && !"CANCELLED".equals(b.getStatus())) {
            updateStatus(bookingId, "CANCELLED");
        }
    }
}
