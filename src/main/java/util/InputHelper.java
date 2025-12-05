package util;

import java.time.LocalDate;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * Helper methods for safely and consistently reading input from the console.
 * Prevents crashes and enforces validation rules.
 */
public class InputHelper {

    // Hata mesajları için standart ön ek (Renkli)
    private static final String ERR_PREFIX = ConsoleColors.RED + " ERROR: " + ConsoleColors.RESET;

    // ==========================================
    // 1) Boş Olmayan Satır Oku
    // ==========================================
    public static String readNonEmptyLine(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine();
            if (line == null) line = "";
            line = line.trim();

            if (!line.isEmpty()) {
                return line;
            }
            System.out.println(ERR_PREFIX + "Input cannot be empty. Please try again.");
        }
    }

    // ==========================================
    // 2) Satır Oku (Boş olabilir)
    // ==========================================
    public static String readLine(Scanner scanner, String prompt) {
        System.out.print(prompt);
        String line = scanner.nextLine();
        return (line == null) ? "" : line.trim();
    }

    // ==========================================
    // 3) Integer Oku (Aralık Kontrollü)
    // ==========================================
    public static int readIntInRange(Scanner scanner, String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine();
            if (line == null) line = "";
            line = line.trim();

            if (!line.matches("-?\\d+")) {
                System.out.println(ERR_PREFIX + "Please enter a valid number.");
                continue;
            }

            try {
                int value = Integer.parseInt(line);
                if (value < min || value > max) {
                    System.out.printf(ConsoleColors.RED + "Please enter a number between %d and %d.\n" + ConsoleColors.RESET, min, max);
                    continue;
                }
                return value;
            } catch (NumberFormatException e) {
                System.out.println(ERR_PREFIX + "Number is too large.");
            }
        }
    }

    // ==========================================
    // 4) Genel Integer Oku
    // ==========================================
    public static int readInt(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                String line = scanner.nextLine().trim();
                return Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.println(ERR_PREFIX + "Please enter a valid integer.");
            }
        }
    }

    // ==========================================
    // 5) Evet/Hayır Sorusu (y/n)
    // ==========================================
    public static boolean readYesNo(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt + " (y/n): ");
            String input = scanner.nextLine();
            if (input == null) input = "";
            input = input.trim().toLowerCase();

            if (input.equals("y") || input.equals("yes")) {
                return true;
            } else if (input.equals("n") || input.equals("no")) {
                return false;
            } else {
                System.out.println(ERR_PREFIX + "Please enter 'y' or 'n'.");
            }
        }
    }

    // ==========================================
    // 6) İsim Doğrulama (Gelişmiş Türkçe Destekli)
    // ==========================================
    public static String readValidName(Scanner scanner, String prompt) {
        java.util.Locale tr = java.util.Locale.forLanguageTag("tr-TR");
        
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine();
            if (input == null) input = "";
            input = input.trim();

            if (input.isEmpty()) {
                System.out.println(ERR_PREFIX + "Name field cannot be empty.");
                continue;
            }

            if (!input.matches("[\\p{L}çğıöşüÇĞİÖŞÜ '\\-]+")) {
                System.out.println(ERR_PREFIX + "Name must contain only letters (no numbers).");
                continue;
            }

            String lowerAll = input.toLowerCase(tr);
            String[] parts = lowerAll.split("\\s+");
            StringBuilder sb = new StringBuilder();

            for (String part : parts) {
                if (part.isEmpty()) continue;
                String first = part.substring(0, 1).toUpperCase(tr);
                String rest = "";
                if (part.length() > 1) rest = part.substring(1);
                
                if (sb.length() > 0) sb.append(' ');
                sb.append(first).append(rest);
            }

            return sb.toString();
        }
    }

    // ==========================================
    // 7) Telefon Doğrulama (+90 Türkiye Formatı)
    // ==========================================
    public static String readValidPhoneTR(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt + " (+90) ");
            String input = scanner.nextLine().trim();

            String clean = input.replaceAll("[^0-9]", "");
            if (clean.length() == 11 && clean.startsWith("0")) {
                clean = clean.substring(1);
            }

            if (clean.matches("^5[0-9]{9}$")) {
                return clean; 
            }
            System.out.println(ERR_PREFIX + "Invalid phone number! Enter 10 digits starting with 5.");
        }
    }

    // ==========================================
    // 8) Email Doğrulama
    // ==========================================
    public static String readValidEmail(Scanner scanner, String prompt) {
        String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            
            if (input.matches(regex)) {
                return input;
            }
            System.out.println(ERR_PREFIX + "Invalid email format! (e.g. user@domain.com)");
        }
    }

    // ==========================================
    // 9) Tarih Doğrulama (DateUtil Entegrasyonu)
    // ==========================================
    public static LocalDate readValidPastDate(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt + " (" + DateUtil.getDateFormat() + "): ");
            String input = scanner.nextLine().trim();
            
            String error = DateUtil.checkDateValidity(input);
            if (error == null) {
                return DateUtil.parse(input);
            }
            System.out.println(ERR_PREFIX + error);
        }
    }

    // =========================================================================
    // 10) LinkedIn Doğrulama (AKILLI TAMAMLAMA)
    // Kullanıcı sadece "username" yazarsa, sistem başına https://... ekler.
    // =========================================================================
    public static String readValidLinkedin(Scanner scanner, String prompt) {
        while (true) {
            // Kullanıcıya ne yapması gerektiğini görsel olarak anlatıyoruz
            System.out.print(prompt + " (https://www.linkedin.com/in/______ ) [Type 'skip' to pass]: ");
            String input = scanner.nextLine().trim();

            // Opsiyonel olduğu için 'skip' veya boş girişe izin ver (veya null dön)
            if (input.equalsIgnoreCase("skip") || input.isEmpty()) {
                return null; 
            }

            String finalUrl = input;

            // Eğer kullanıcı sadece kullanıcı adını yazdıysa (örn: ahmet-yilmaz)
            if (!input.toLowerCase().contains("linkedin.com")) {
                finalUrl = "https://www.linkedin.com/in/" + input;
            } else {
                // Eğer link yapıştırdıysa ama başında https yoksa ekle
                if (!finalUrl.startsWith("http")) {
                    finalUrl = "https://" + finalUrl;
                }
            }

            // Basit bir URL doğrulama (Boşluk olmamalı, linkedin içermeli)
            if (finalUrl.matches("^https:\\/\\/([a-z]{2,3}\\.)?linkedin\\.com\\/.*$") && !finalUrl.contains(" ")) {
                return finalUrl;
            }

            System.out.println(ERR_PREFIX + "Invalid LinkedIn URL! Please enter a valid username or full URL.");
        }
    }

    // ==========================================
    // 11) Konsol Temizleme
    // ==========================================
    public static void clearScreen() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                new ProcessBuilder("clear").inheritIO().start().waitFor();
            }
        } catch (Exception e) { }
    }
    
    private InputHelper() { }
}