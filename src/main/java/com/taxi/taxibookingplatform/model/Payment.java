package com.taxi.taxibookingplatform.model;

import java.time.LocalDateTime;

public class Payment {

    private final String paymentId;
    private final String bookingId;
    private final String userId;
    private final double amount;
    private final String cardNumber; // Masked for security
    private final String paymentStatus; // SUCCESS, FAILED
    private final LocalDateTime paymentDate;

    public Payment(String paymentId, String bookingId, String userId, double amount,
                   String cardNumber, String paymentStatus, LocalDateTime paymentDate) {
        this.paymentId = paymentId;
        this.bookingId = bookingId;
        this.userId = userId;
        this.amount = amount;
        this.cardNumber = cardNumber;
        this.paymentStatus = paymentStatus;
        this.paymentDate = paymentDate;
    }

    public String toFileString() {
        return "PAYMENT," + paymentId + "," + bookingId + "," + userId + "," + amount + ","
                + escape(cardNumber) + "," + paymentStatus + "," + paymentDate;
    }

    public static Payment fromFileLine(String line) {
        String[] p = line.split(",", -1);
        if (p.length < 8 || !"PAYMENT".equals(p[0])) {
            return null;
        }
        return new Payment(
                p[1], p[2], p[3], Double.parseDouble(p[4]),
                unescape(p[5]), p[6], LocalDateTime.parse(p[7])
        );
    }

    private static String escape(String s) {
        return s == null ? "" : s.replace(",", ";");
    }

    private static String unescape(String s) {
        return s == null ? "" : s.replace(";", ",");
    }

    public String getPaymentId() { return paymentId; }
    public String getBookingId() { return bookingId; }
    public String getUserId() { return userId; }
    public double getAmount() { return amount; }
    public String getCardNumber() { return cardNumber; }
    public String getPaymentStatus() { return paymentStatus; }
    public LocalDateTime getPaymentDate() { return paymentDate; }
}
