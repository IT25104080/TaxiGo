package com.taxi.taxibookingplatform.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Represents a booking entity within the TaxiGo platform.
 * This class is immutable, holding read-only details of a passenger's ride reservation.
 * It also supports custom CSV serialization and deserialization for file-based persistence.
 */
public class Booking {

    // Unique identifier for the booking (e.g., UUID-based)
    private final String bookingId;
    // Identifier of the passenger who created the booking
    private final String userId;
    // Starting address or location of the ride
    private final String pickup;
    // Ending address or destination of the ride
    private final String dropoff;
    // Scheduled date for the ride
    private final LocalDate pickupDate;
    // Scheduled time for the ride
    private final LocalTime pickupTime;
    // The requested vehicle tier or category (e.g., standard, premium, xl)
    private final String vehicleType;
    // Current state of the booking (e.g., PENDING, CONFIRMED, COMPLETED)
    private final String status;
    // The calculated fare for the trip
    private final double fare;
    // Optional passenger notes or driver instructions
    private final String notes;
    // The timestamp when this booking record was created
    private final LocalDateTime createdAt;

    /**
     * Constructs a new Booking instance with all required attributes.
     */
    public Booking(String bookingId, String userId, String pickup, String dropoff,
                   LocalDate pickupDate, LocalTime pickupTime, String vehicleType,
                   String status, double fare, String notes, LocalDateTime createdAt) {
        this.bookingId = bookingId;
        this.userId = userId;
        this.pickup = pickup;
        this.dropoff = dropoff;
        this.pickupDate = pickupDate;
        this.pickupTime = pickupTime;
        this.vehicleType = vehicleType;
        this.status = status;
        this.fare = fare;
        this.notes = notes;
        this.createdAt = createdAt;
    }

    /**
     * Serializes this booking object into a comma-separated values (CSV) string representation.
     * Commas in pickup, dropoff, and notes fields are escaped using a helper method.
     *
     * @return a CSV formatted string representing the booking
     */
    public String toFileString() {
        return "BOOKING," + bookingId + "," + userId + "," + escape(pickup) + "," + escape(dropoff) + ","
                + pickupDate + "," + pickupTime + "," + vehicleType + "," + status + ","
                + fare + "," + escape(notes) + "," + createdAt;
    }

    /**
     * Deserializes a comma-separated values (CSV) string line back into a Booking instance.
     *
     * @param line the raw CSV record line from storage
     * @return a Booking instance, or null if the line is invalid or not a booking record
     */
    public static Booking fromFileLine(String line) {
        String[] p = line.split(",", -1);
        if (p.length < 12 || !"BOOKING".equals(p[0])) {
            return null;
        }
        return new Booking(
                p[1], p[2], unescape(p[3]), unescape(p[4]),
                LocalDate.parse(p[5]), LocalTime.parse(p[6]),
                p[7], p[8], Double.parseDouble(p[9]), unescape(p[10]),
                LocalDateTime.parse(p[11])
        );
    }

    /**
     * Escapes commas in string fields by replacing them with semicolons
     * to prevent interference with CSV parsing.
     */
    private static String escape(String s) {
        return s == null ? "" : s.replace(",", ";");
    }

    /**
     * Unescapes string fields by restoring semicolons back to commas
     * after CSV parsing.
     */
    private static String unescape(String s) {
        return s == null ? "" : s.replace(";", ",");
    }

    public String getBookingId() { return bookingId; }
    public String getUserId() { return userId; }
    public String getPickup() { return pickup; }
    public String getDropoff() { return dropoff; }
    public LocalDate getPickupDate() { return pickupDate; }
    public LocalTime getPickupTime() { return pickupTime; }
    public String getVehicleType() { return vehicleType; }
    public String getStatus() { return status; }
    public double getFare() { return fare; }
    public String getNotes() { return notes; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
