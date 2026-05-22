package com.taxi.taxibookingplatform.service;

import com.taxi.taxibookingplatform.model.Taxi;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * ============================================================================
 * OOP CONCEPT: ENCAPSULATION & SINGLETON SERVICE PATTERN
 * ============================================================================
 * 1. ENCAPSULATION:
 *    Encapsulates taxi fleet loading, seeding default items ( Toyota, Mercedes, Nissan), 
 *    and modifying vehicle statuses.
 * ============================================================================
 */
@Service
public class TaxiFileHandler {

    private final String filePath = "data/taxi.txt";

    private void ensureFileExists() throws IOException {
        new File("data").mkdirs();
        File file = new File(filePath);
        if (!file.exists()) {
            file.createNewFile();
            // Seed with premium default taxis
            seedDefaultTaxis();
        }
    }

    private void seedDefaultTaxis() throws IOException {
        List<Taxi> defaults = List.of(
            new Taxi("TX1001", "Toyota Prius Hybrid", "SEDAN", "WP CAD-4567", "Rohan Perera", "+94 77 123 4567", 110.0, "AVAILABLE", "https://images.unsplash.com/photo-1549399542-7e3f8b79c341?auto=format&fit=crop&w=600&q=80"),
            new Taxi("TX1002", "Mercedes-Benz E-Class Luxury", "LUXURY", "WP CBA-8899", "Dimuthu Fernando", "+94 77 987 6543", 290.0, "AVAILABLE", "https://images.unsplash.com/photo-1618843479313-40f8afb4b4d8?auto=format&fit=crop&w=600&q=80"),
            new Taxi("TX1003", "Nissan X-Trail Intelligent SUV", "SUV", "WP CAB-9922", "Nimal Siripala", "+94 71 555 4433", 170.0, "AVAILABLE", "https://images.unsplash.com/photo-1533473359331-0135ef1b58bf?auto=format&fit=crop&w=600&q=80")
        );
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            for (Taxi t : defaults) {
                writer.write(t.toFileString());
                writer.newLine();
            }
        }
    }

    public List<Taxi> getAllTaxis() throws IOException {
        ensureFileExists();
        List<Taxi> list = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                Taxi t = Taxi.fromFileLine(line);
                if (t != null) list.add(t);
            }
        }
        list.sort(Comparator.comparing(Taxi::getId));
        return list;
    }

    public void addTaxi(Taxi taxi) throws IOException {
        ensureFileExists();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(taxi.toFileString());
            writer.newLine();
        }
    }

    public void deleteTaxi(String id) throws IOException {
        ensureFileExists();
        List<Taxi> all = getAllTaxis();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, false))) {
            for (Taxi t : all) {
                if (!t.getId().equals(id)) {
                    writer.write(t.toFileString());
                    writer.newLine();
                }
            }
        }
    }

    public void updateTaxi(Taxi updatedTaxi) throws IOException {
        ensureFileExists();
        List<Taxi> all = getAllTaxis();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, false))) {
            for (Taxi t : all) {
                if (t.getId().equals(updatedTaxi.getId())) {
                    writer.write(updatedTaxi.toFileString());
                } else {
                    writer.write(t.toFileString());
                }
                writer.newLine();
            }
        }
    }
}
