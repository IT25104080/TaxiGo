package com.taxi.taxibookingplatform.service;

/**
 * Utility class for calculating and estimating taxi fares based on distance,
 * vehicle type, and passenger membership status.
 */
public final class FareCalculator {

    private static final double BASE_FARE = 500.0;
    private static final double PER_KM_ESTIMATE = 120.0;

    private FareCalculator() {}

    /**
     * Estimates the ride fare based on locations, vehicle type, and user type.
     * Enforces a simulated distance heuristic and applies vehicle-specific multipliers
     * and membership discounts.
     *
     * @param pickup      The pickup location name.
     * @param dropoff     The dropoff location name.
     * @param vehicleType The requested vehicle category (premium, xl, standard).
     * @param premiumUser True if passenger is a premium member (eligible for 10% discount).
     * @return The estimated fare, rounded to two decimal places.
     */
    public static double estimate(String pickup, String dropoff, String vehicleType, boolean premiumUser) {
        // Simulated distance heuristic: computes mock distance in kilometers using location name lengths 
        // as a simple sandbox fallback, enforcing a minimum distance of 3 km.
        int estimatedKm = Math.max(3, (pickup.length() + dropoff.length()) / 8);

        double fare = BASE_FARE + (estimatedKm * PER_KM_ESTIMATE);

        // Apply vehicle multipliers: 1.5x for Luxury/Premium, 2.0x for XL, 1.0x for standard sedan.
        fare *= switch (vehicleType.toLowerCase()) {
            case "premium" -> 1.5;
            case "xl" -> 2.0;
            default -> 1.0;
        };

        // Apply a 10% discount for premium members.
        if (premiumUser) {
            fare *= 0.9;
        }

        // Round the final estimated fare to exactly two decimal places.
        return Math.round(fare * 100.0) / 100.0;
    }
}
