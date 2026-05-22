package com.taxi.taxibookingplatform.util;

import java.util.regex.Pattern;

public class ValidationUtils {

    // Alphabetic characters and spaces only, length 2 to 50
    private static final Pattern NAME_PATTERN = Pattern.compile("^[a-zA-Z\\s]{2,50}$");

    // Standard email matching
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$");

    // Sri Lankan standard phone number format (optional +94 or 0, followed by 9 digits starting with a non-zero)
    private static final Pattern PHONE_PATTERN = Pattern.compile("^(?:\\+94|0)?[1-9][0-9]{8}$");

    // Strong password pattern: min 8 chars, 1 uppercase, 1 lowercase, 1 digit, 1 special char (@$!%*?&_#)
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&_#])[A-Za-z\\d@$!%*?&_#]{8,}$");

    // Sri Lankan vehicle license plate pattern (optional province code, letters/numbers, hyphen, 4 digits)
    private static final Pattern LICENSE_PLATE_PATTERN = Pattern.compile("^(?:[A-Za-z]{1,3}\\s+)?[A-Za-z0-9]{1,3}\\s*-\\s*[0-9]{4}$");

    public static boolean isValidName(String name) {
        return name != null && NAME_PATTERN.matcher(name.trim()).matches();
    }

    public static boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email.trim()).matches();
    }

    public static boolean isValidPhone(String phone) {
        if (phone == null) return false;
        // Strip any inner spaces or hyphens to make it user-friendly
        String normalized = phone.replace(" ", "").replace("-", "");
        return PHONE_PATTERN.matcher(normalized).matches();
    }

    public static boolean isValidPassword(String password) {
        return password != null && PASSWORD_PATTERN.matcher(password).matches();
    }

    public static boolean isValidLicensePlate(String plate) {
        return plate != null && LICENSE_PLATE_PATTERN.matcher(plate.trim()).matches();
    }
}
