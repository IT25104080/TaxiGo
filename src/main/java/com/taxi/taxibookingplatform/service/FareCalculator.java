package com.taxi.taxibookingplatform.service;

public final class FareCalculator {

    private static final double BASE_FARE = 500.0;
    private static final double PER_KM_ESTIMATE = 120.0;

    private FareCalculator() {}

    public static double estimate(String pickup, String dropoff, String vehicleType, boolean premiumUser) {
        int estimatedKm = Math.max(3, (pickup.length() + dropoff.length()) / 8);
        double fare = BASE_FARE + (estimatedKm * PER_KM_ESTIMATE);
        fare *= switch (vehicleType.toLowerCase()) {
            case "premium" -> 1.5;
            case "xl" -> 2.0;
            default -> 1.0;
        };
        if (premiumUser) {
            fare *= 0.9;
        }
        return Math.round(fare * 100.0) / 100.0;
    }
}
