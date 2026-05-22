package com.taxi.taxibookingplatform.model;

public class Taxi {

    private final String id;
    private final String model;
    private final String vehicleType; // SEDAN, SUV, LUXURY
    private final String licensePlate;
    private final String driverName;
    private final String driverPhone;
    private final double ratePerKm;
    private final String status; // AVAILABLE, BUSY, OUT_OF_SERVICE
    private final String imageUrl;

    public Taxi(String id, String model, String vehicleType, String licensePlate,
                String driverName, String driverPhone, double ratePerKm, String status) {
        this(id, model, vehicleType, licensePlate, driverName, driverPhone, ratePerKm, status, "");
    }

    public Taxi(String id, String model, String vehicleType, String licensePlate,
                String driverName, String driverPhone, double ratePerKm, String status, String imageUrl) {
        this.id = id;
        this.model = model;
        this.vehicleType = vehicleType;
        this.licensePlate = licensePlate;
        this.driverName = driverName;
        this.driverPhone = driverPhone;
        this.ratePerKm = ratePerKm;
        this.status = status;
        this.imageUrl = imageUrl;
    }

    public String toFileString() {
        return "TAXI," + id + "," + escape(model) + "," + escape(vehicleType) + ","
                + escape(licensePlate) + "," + escape(driverName) + "," + escape(driverPhone)
                + "," + ratePerKm + "," + status + "," + escape(imageUrl);
    }

    public static Taxi fromFileLine(String line) {
        if (line == null || line.trim().isEmpty()) return null;
        String[] parts = line.split(",", 10);
        if (parts.length >= 9 && "TAXI".equals(parts[0])) {
            try {
                String id = parts[1];
                String model = parts[2].replace(";", ",").trim();
                String vehicleType = parts[3].replace(";", ",").trim();
                String licensePlate = parts[4].replace(";", ",").trim();
                String driverName = parts[5].replace(";", ",").trim();
                String driverPhone = parts[6].replace(";", ",").trim();
                double rate = Double.parseDouble(parts[7].trim());
                String status = parts[8].trim();
                String imageUrl = (parts.length >= 10) ? parts[9].replace(";", ",").trim() : "";
                return new Taxi(id, model, vehicleType, licensePlate, driverName, driverPhone, rate, status, imageUrl);
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }

    private static String escape(String s) {
        return s == null ? "" : s.replace(",", ";").replace("\n", " ");
    }

    // Getters
    public String getId() { return id; }
    public String getModel() { return model; }
    public String getVehicleType() { return vehicleType; }
    public String getLicensePlate() { return licensePlate; }
    public String getDriverName() { return driverName; }
    public String getDriverPhone() { return driverPhone; }
    public double getRatePerKm() { return ratePerKm; }
    public String getStatus() { return status; }
    public String getImageUrl() { return imageUrl; }
}
