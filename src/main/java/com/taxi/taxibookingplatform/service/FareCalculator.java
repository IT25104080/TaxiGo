package com.taxi.taxibookingplatform.service;

/**
 * Utility class for calculating and estimating taxi fares based on distance,
 * vehicle type, and user membership status.
 */
public final class FareCalculator {

    /** The base fare charge applied to all rides. */
    private static final double BASE_FARE = 500.0;

    /** The estimated cost per kilometer in standard currency units. */
    private static final double PER_KM_ESTIMATE = 120.0;

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private FareCalculator() {}

    /**
     * Estimates the fare for a ride based on details such as pickup/dropoff locations,
     * vehicle type, and whether the user is a premium member.
     *
     * @param pickup      The pickup location name.
     * @param dropoff     The dropoff location name.
     * @param vehicleType The type of vehicle requested (e.g., "premium", "xl", "standard").
     * @param premiumUser True if the passenger is a premium member eligible for a discount.
     * @return The estimated fare, rounded to two decimal places.
     */
    public static double estimate(String pickup, String dropoff, String vehicleType, boolean premiumUser) {
        // Estimate the distance in kilometers based on the length of the pickup and dropoff strings.
        // A minimum distance of 3 km is enforced.
        int estimatedKm = Math.max(3, (pickup.length() + dropoff.length()) / 8);

        // Calculate initial fare using base fare and distance-based fare.
        double fare = BASE_FARE + (estimatedKm * PER_KM_ESTIMATE);

        // Apply a multiplier depending on the type of vehicle.
        fare *= switch (vehicleType.toLowerCase()) {
            case "premium" -> 1.5; // 50% extra charge for premium vehicles
            case "xl" -> 2.0;      // 100% extra charge (double fare) for XL vehicles
            default -> 1.0;        // Standard rate
        };

        // Apply a 10% discount for premium users.
        if (premiumUser) {
            fare *= 0.9;
        }

        // Round the final estimated fare to 2 decimal places.
        return Math.round(fare * 100.0) / 100.0;
    }
}
