package com.taxi.taxibookingplatform.service;

import com.taxi.taxibookingplatform.model.Booking;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Handles persistence operations for {@link Booking} records.
 * Saves, reads, updates, and deletes booking data in a local text-based flat file database.
 */
public class BookingFileHandler {

    // Relative path to the flat file where booking records are stored in CSV format
    private static final String FILE_PATH = "data/booking.txt";

    /**
     * Appends a new booking record to the end of the flat file database.
     * Automatically creates the parent data directory if it does not exist.
     *
     * @param booking the Booking record to persist
     * @throws IOException if a file write error occurs
     */
    public static void addBooking(Booking booking) throws IOException {
        new File("data").mkdirs();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            writer.write(booking.toFileString());
            writer.newLine();
        }
    }

    /**
     * Reads all booking records from the flat file, parses them, and sorts
     * them by creation timestamp in descending order (newest first).
     *
     * @return a sorted List of Booking records
     * @throws IOException if a file read error occurs
     */
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
        // Sort bookings by creation date in reverse chronological order
        list.sort(Comparator.comparing(Booking::getCreatedAt).reversed());
        return list;
    }

    /**
     * Filters and returns all booking records initiated by a particular passenger/user.
     *
     * @param userId the unique identifier of the passenger
     * @return a List of Bookings corresponding to the specified passenger
     * @throws IOException if a database read error occurs
     */
    public static List<Booking> getBookingsByUserId(String userId) throws IOException {
        return getAllBookings().stream()
                .filter(b -> b.getUserId().equals(userId))
                .toList();
    }

    /**
     * Searches for a booking record by its unique booking ID.
     *
     * @param bookingId the unique booking identifier
     * @return the Booking object if found, or null otherwise
     * @throws IOException if a database read error occurs
     */
    public static Booking getBookingById(String bookingId) throws IOException {
        return getAllBookings().stream()
                .filter(b -> b.getBookingId().equals(bookingId))
                .findFirst()
                .orElse(null);
    }

    /**
     * Updates the processing status of a specific booking.
     * Performs a full rewrite of the database file, updating the targeted booking status.
     *
     * @param bookingId the unique identifier of the booking to update
     * @param status the new status to assign (e.g., CONFIRMED, CANCELLED)
     * @throws IOException if a database rewrite error occurs
     */
    public static void updateStatus(String bookingId, String status) throws IOException {
        List<Booking> all = getAllBookings();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, false))) {
            for (Booking b : all) {
                // If ID matches, create a new Booking instance with updated status; otherwise keep existing
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

    /**
     * Cancels a booking if it is owned by the requesting user and has not been cancelled already.
     *
     * @param bookingId the unique booking identifier to cancel
     * @param userId the user ID initiating the cancellation for security validation
     * @throws IOException if a database update error occurs
     */
    public static void cancelBooking(String bookingId, String userId) throws IOException {
        Booking b = getBookingById(bookingId);
        if (b != null && b.getUserId().equals(userId) && !"CANCELLED".equals(b.getStatus())) {
            updateStatus(bookingId, "CANCELLED");
        }
    }
}
