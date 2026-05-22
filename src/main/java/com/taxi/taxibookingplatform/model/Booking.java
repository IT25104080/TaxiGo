package com.taxi.taxibookingplatform.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class Booking {

    private final String bookingId;
    private final String userId;
    private final String pickup;
    private final String dropoff;
    private final LocalDate pickupDate;
    private final LocalTime pickupTime;
    private final String vehicleType;
    private final String status;
    private final double fare;
    private final String notes;
    private final LocalDateTime createdAt;

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

    public String toFileString() {
        return "BOOKING," + bookingId + "," + userId + "," + escape(pickup) + "," + escape(dropoff) + ","
                + pickupDate + "," + pickupTime + "," + vehicleType + "," + status + ","
                + fare + "," + escape(notes) + "," + createdAt;
    }

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

    private static String escape(String s) {
        return s == null ? "" : s.replace(",", ";");
    }

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
