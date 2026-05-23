package com.taxi.taxibookingplatform.util;

import java.util.regex.Pattern;
import java.time.YearMonth;
import java.util.regex.Matcher;

public class ValidationUtils {

    // Validates name: alphabetic characters and spaces only, 2 to 50 characters
    private static final Pattern NAME_PATTERN = Pattern.compile("^[a-zA-Z\\s]{2,50}$");

    // Validates email format: standard email regex with domain and extension
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$");

    // Validates Sri Lankan phone number: +94 or 0 prefix, followed by 9 digits
    private static final Pattern PHONE_PATTERN = Pattern.compile("^(?:\\+94|0)?[1-9][0-9]{8}$");

    // Validates strong password: minimum 8 characters with uppercase, lowercase, digit, and special character
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&_#])[A-Za-z\\d@$!%*?&_#]{8,}$");

    // Validates Sri Lankan vehicle license plate: optional province code, alphanumeric, hyphen, and 4 digits
    private static final Pattern LICENSE_PLATE_PATTERN = Pattern.compile("^(?:[A-Za-z]{1,3}\\s+)?[A-Za-z0-9]{1,3}\\s*-\\s*[0-9]{4}$");

    // Validates credit card number: 13 to 19 digits for various card types
    private static final Pattern CARD_NUMBER_PATTERN = Pattern.compile("^[0-9]{13,19}$");

    // Validates card expiry date: MM/YY or MM / YY format
    private static final Pattern CARD_EXPIRY_PATTERN = Pattern.compile("^(0[1-9]|1[0-2])\\s*/\\s*([0-9]{2})$");

    // Validates CVV security code: 3 or 4 digits
    private static final Pattern CVV_PATTERN = Pattern.compile("^[0-9]{3,4}$");

    // Validates user name: alphabetic characters and spaces, 2-50 character length
    public static boolean isValidName(String name) {
        return name != null && NAME_PATTERN.matcher(name.trim()).matches();
    }

    // Validates email address format using standard email regex
    public static boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email.trim()).matches();
    }

    // Validates Sri Lankan phone number format, removes spaces and hyphens for flexibility
    public static boolean isValidPhone(String phone) {
        if (phone == null) return false;
        // Strip any inner spaces or hyphens to make it user-friendly
        String normalized = phone.replace(" ", "").replace("-", "");
        return PHONE_PATTERN.matcher(normalized).matches();
    }

    // Validates password strength: min 8 characters with uppercase, lowercase, digit, and special character
    public static boolean isValidPassword(String password) {
        return password != null && PASSWORD_PATTERN.matcher(password).matches();
    }

    // Validates Sri Lankan vehicle license plate format
    public static boolean isValidLicensePlate(String plate) {
        return plate != null && LICENSE_PLATE_PATTERN.matcher(plate.trim()).matches();
    }

    // Validates credit card number: checks format and applies Luhn's algorithm for authenticity
    public static boolean isValidCardNumber(String cardNum) {
        if (cardNum == null) return false;
        // Remove spaces and hyphens for standard processing
        String cleaned = cardNum.replace(" ", "").replace("-", "");
        if (!CARD_NUMBER_PATTERN.matcher(cleaned).matches()) return false;

        // Luhn's algorithm for checking card number validity
        int sum = 0;
        boolean alternate = false;
        for (int i = cleaned.length() - 1; i >= 0; i--) {
            int n = Integer.parseInt(cleaned.substring(i, i + 1));
            if (alternate) {
                n *= 2;
                if (n > 9) {
                    n = (n % 10) + 1;
                }
            }
            sum += n;
            alternate = !alternate;
        }
        return (sum % 10 == 0);
    }

    // Validates card expiry date: checks format (MM/YY) and ensures date is not in the past
    public static boolean isValidCardExpiry(String expiry) {
        if (expiry == null) return false;
        Matcher m = CARD_EXPIRY_PATTERN.matcher(expiry.trim());
        if (!m.matches()) return false;

        // Extract month and year, convert year to full format (20XX)
        int month = Integer.parseInt(m.group(1));
        int year = Integer.parseInt(m.group(2)) + 2000; // Assuming 21st century

        try {
            // Create expiry date object and verify it's current or future
            YearMonth expDate = YearMonth.of(year, month);
            return expDate.isAfter(YearMonth.now()) || expDate.equals(YearMonth.now());
        } catch (Exception e) {
            return false;
        }
    }

    // Validates CVV security code: 3 or 4 digit format
    public static boolean isValidCardCVV(String cvv) {
        return cvv != null && CVV_PATTERN.matcher(cvv.trim()).matches();
    }
}
