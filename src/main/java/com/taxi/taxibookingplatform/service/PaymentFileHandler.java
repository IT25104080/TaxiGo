package com.taxi.taxibookingplatform.service;

import com.taxi.taxibookingplatform.model.Payment;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class PaymentFileHandler {

    private final String filePath = "data/payments.txt";

    public synchronized void save(Payment payment) throws IOException {
        new File("data").mkdirs();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(payment.toFileString());
            writer.newLine();
        }
    }

    public List<Payment> getAllPayments() throws IOException {
        List<Payment> list = new ArrayList<>();
        File file = new File(filePath);
        if (!file.exists())
            return list;

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty())
                    continue;
                Payment payment = Payment.fromFileLine(line.trim());
                if (payment != null) {
                    list.add(payment);
                }
            }
        }
        return list;
    }

    public Payment getPaymentById(String paymentId) throws IOException {
        for (Payment p : getAllPayments()) {
            if (p.getPaymentId().equals(paymentId)) {
                return p;
            }
        }
        return null;
    }

    public Payment getPaymentByBookingId(String bookingId) throws IOException {
        for (Payment p : getAllPayments()) {
            if (p.getBookingId().equals(bookingId)) {
                return p;
            }
        }
        return null;
    }

    public List<Payment> getPaymentsByUserId(String userId) throws IOException {
        List<Payment> userPayments = new ArrayList<>();
        for (Payment p : getAllPayments()) {
            if (p.getUserId().equals(userId)) {
                userPayments.add(p);
            }
        }
        return userPayments;
    }
}
