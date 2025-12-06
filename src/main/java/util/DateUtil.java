package util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

/**
 * Handles strict date validation logic using modern Java Time API.
 * Ensures data integrity for birth dates (No future dates, no invalid calendar days).
 */
public class DateUtil {

    // Format: Year-Month-Day (e.g., 2000-11-25)
    // CRITICAL: ResolverStyle.STRICT mode, prevents Java's automatic rounding.
    // Throws error for invalid dates like "2023-02-30".
    // Leap Year does automatic calculation (rejects 2023-02-29, accepts 2024-02-29).
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("uuuu-MM-dd").withResolverStyle(ResolverStyle.STRICT);

    /**
     * Returns the standard date format string used in the application.
     */
    public static String getDateFormat() {
        return "yyyy-MM-dd";
    }

    /**
     * Checks if the date string is valid based on logical rules.
     * Rules:
     * 1. Must match yyyy-MM-dd format.
     * 2. Must be a real calendar date (No Feb 30, No Feb 29 on non-leap years).
     * 3. Cannot be in the future.
     * 4. Cannot be before 1900.
     * * @param dateStr The input string (e.g., "2000-11-25")
     * @return Error message (String) if invalid, NULL if valid.
     */
    public static String checkDateValidity(String dateStr) {
        try {
            // 1. Parse : Format and Calendar check
            // (Leap year and non-existent days are automatically checked here, if error occurs it falls into catch)
            LocalDate date = LocalDate.parse(dateStr, FORMATTER);
            LocalDate now = LocalDate.now();
            
            // 2. Future Date Check
            if (date.isAfter(now)) {
                return "Date cannot be in the future!";
            }
            
            // 3. Date Limit (Before 1900 not allowed)
            if (date.getYear() < 1900) {
                return "Date is too old! Year must be 1900 or later.";
            }

            return null; // Valid (No error)

        } catch (DateTimeParseException e) {
            // Format error or non-existent day in calendar (e.g., 2023-02-30)
            return "Invalid date! Please ensure:\n" +
                   "- Format is yyyy-MM-dd (e.g. 1990-12-31)\n" +
                   "- Day exists in the calendar (e.g., No Feb 30)\n" +
                   "- Leap years are respected (Feb 29 only in 2024, 2028...)";
        }
    }
    
    /**
     * Parses a valid string into a LocalDate object.
     * Should be called ONLY after checkDateValidity returns null.
     */
    public static LocalDate parse(String dateStr) {
        return LocalDate.parse(dateStr, FORMATTER);
    }
}