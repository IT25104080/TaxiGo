package com.taxi.taxibookingplatform.util;

import java.util.regex.Pattern;
import java.time.YearMonth;
import java.util.regex.Matcher;

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

    // 13 to 19 digits credit card regex
    private static final Pattern CARD_NUMBER_PATTERN = Pattern.compile("^[0-9]{13,19}$");

    // Expiry date regex (MM/YY or MM / YY)
    private static final Pattern CARD_EXPIRY_PATTERN = Pattern.compile("^(0[1-9]|1[0-2])\\s*/\\s*([0-9]{2})$");

    // CVV regex (3 or 4 digits)
    private static final Pattern CVV_PATTERN = Pattern.compile("^[0-9]{3,4}$");

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

    public static boolean isValidCardNumber(String cardNum) {
        if (cardNum == null) return false;
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

    public static boolean isValidCardExpiry(String expiry) {
        if (expiry == null) return false;
        Matcher m = CARD_EXPIRY_PATTERN.matcher(expiry.trim());
        if (!m.matches()) return false;

        int month = Integer.parseInt(m.group(1));
        int year = Integer.parseInt(m.group(2)) + 2000; // Assuming 21st century

        try {
            YearMonth expDate = YearMonth.of(year, month);
            return expDate.isAfter(YearMonth.now()) || expDate.equals(YearMonth.now());
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isValidCardCVV(String cvv) {
        return cvv != null && CVV_PATTERN.matcher(cvv.trim()).matches();
    }
}
