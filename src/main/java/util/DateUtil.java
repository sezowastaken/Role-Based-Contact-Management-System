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

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("uuuu-MM-dd").withResolverStyle(ResolverStyle.STRICT);

    /**
     * Returns the standard date format string used in the application.
     */
    public static String getDateFormat() {
        return "yyyy-MM-dd";
    }

    /**
     * Checks if the date string is valid (format, calendar date, not future, after 1900).
     * @param dateStr the input string (e.g., "2000-11-25")
     * @return error message if invalid, null if valid
     */
    public static String checkDateValidity(String dateStr) {
        try {
            LocalDate date = LocalDate.parse(dateStr, FORMATTER);
            LocalDate now = LocalDate.now();
            
            if (date.isAfter(now)) {
                return "Date cannot be in the future!";
            }
            
            if (date.getYear() < 1900) {
                return "Date is too old! Year must be 1900 or later.";
            }

            return null;

        } catch (DateTimeParseException e) {
            return "Invalid date! Please ensure:\n" +
                   "- Format is yyyy-MM-dd (e.g. 1990-12-31)\n" +
                   "- Day exists in the calendar (e.g., No Feb 30)\n" +
                   "- Leap years are respected (Feb 29 only in 2024, 2028...)";
        }
    }
    
    /**
     * Parses a valid date string into LocalDate.
     * Call only after checkDateValidity returns null.
     */
    public static LocalDate parse(String dateStr) {
        return LocalDate.parse(dateStr, FORMATTER);
    }
}