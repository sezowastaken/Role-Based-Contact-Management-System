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

    // Format: Yıl-Ay-Gün (Örn: 2000-11-25)
    // CRITICAL: ResolverStyle.STRICT modu, Java'nın otomatik yuvarlama yapmasını engeller.
    // "2023-02-30" girilirse hata verir.
    // Artık Yıl (Leap Year) hesabını otomatik yapar (2023-02-29'u reddeder, 2024-02-29'u kabul eder).
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
            // 1. Parse işlemi: Format ve Takvim kontrolü 
            // (Artık yıl ve olmayan günler burada otomatik kontrol edilir, hata varsa catch'e düşer)
            LocalDate date = LocalDate.parse(dateStr, FORMATTER);
            LocalDate now = LocalDate.now();
            
            // 2. Gelecek Tarih Kontrolü
            if (date.isAfter(now)) {
                return "Date cannot be in the future!";
            }
            
            // 3. Tarih Limiti (1900 öncesi yasak)
            if (date.getYear() < 1900) {
                return "Date is too old! Year must be 1900 or later.";
            }

            return null; // Valid (Hata yok)

        } catch (DateTimeParseException e) {
            // Format hatası veya takvimde olmayan gün (örn: 2023-02-30)
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