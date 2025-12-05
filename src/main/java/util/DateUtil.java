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

    // Format: Gün.Ay.Yıl (Örn: 25.11.2025)
    // CRITICAL: ResolverStyle.STRICT modu, Java'nın otomatik yuvarlama yapmasını engeller.
    // "30 Şubat" girilirse 2 Mart'a yuvarlamaz, direkt HATA verir.
    // Artık Yıl (Leap Year) hesabını otomatik yapar (2023'te 29 Şubat'ı reddeder, 2024'te kabul eder).
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy")
            .withResolverStyle(ResolverStyle.STRICT);

    /**
     * Returns the standard date format string used in the application.
     */
    public static String getDateFormat() {
        return "dd.MM.yyyy";
    }

    /**
     * Checks if the date string is valid based on logical rules.
     * Rules:
     * 1. Must match dd.MM.yyyy format.
     * 2. Must be a real calendar date (No Feb 30, No Feb 29 on non-leap years).
     * 3. Cannot be in the future.
     * 4. Cannot be before 1900.
     * * @param dateStr The input string (e.g., "25.11.2000")
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
            // Format hatası veya takvimde olmayan gün (örn: 30.02.2023)
            return "Invalid date! Please ensure:\n" +
                   "- Format is dd.MM.yyyy\n" +
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