package com.taxi.taxibookingplatform.service;

import com.taxi.taxibookingplatform.model.Booking;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class BookingFileHandler {

    private final String filePath = "data/booking.txt";

    public void addBooking(Booking booking) throws IOException {
        new File("data").mkdirs();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(booking.toFileString());
            writer.newLine();
        }
    }

    public List<Booking> getAllBookings() throws IOException {
        List<Booking> list = new ArrayList<>();
        File file = new File(filePath);
        if (!file.exists())
            return list;

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty())
                    continue;
                Booking b = Booking.fromFileLine(line);
                if (b != null)
                    list.add(b);
            }
        }
        list.sort(Comparator.comparing(Booking::getCreatedAt).reversed());
        return list;
    }

    public List<Booking> getBookingsByUserId(String userId) throws IOException {
        return getAllBookings().stream()
                .filter(b -> b.getUserId().equals(userId))
                .toList();
    }

    public Booking getBookingById(String bookingId) throws IOException {
        return getAllBookings().stream()
                .filter(b -> b.getBookingId().equals(bookingId))
                .findFirst()
                .orElse(null);
    }

    public void updateStatus(String bookingId, String status) throws IOException {
        List<Booking> all = getAllBookings();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, false))) {
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

    public void cancelBooking(String bookingId, String userId) throws IOException {
        Booking b = getBookingById(bookingId);
        if (b != null && b.getUserId().equals(userId) && !"CANCELLED".equals(b.getStatus())) {
            updateStatus(bookingId, "CANCELLED");
        }
    }
}
