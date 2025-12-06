package util;

import java.time.LocalDate;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * Helper methods for safely and consistently reading input from the console.
 * Prevents crashes and enforces validation rules, including database VARCHAR limits.
 */
public class InputHelper {

    // Hata mesajları için standart ön ek (Renkli)
    private static final String ERR_PREFIX = ConsoleColors.RED + " ERROR: " + ConsoleColors.RESET;

    // ==========================================
    // VERİTABANI UZUNLUK SINIRLARI (DB ŞEMASINA GÖRE)
    // ==========================================
    private static final int MAX_NAME_LENGTH = 50; // name, surname, first_name, last_name, nickname
    private static final int MAX_USERNAME_LENGTH = 50; // username
    private static final int MAX_RAW_PASSWORD_LENGTH = 100; // Ham şifre girişi limiti (Hash 255)
    private static final int MAX_EMAIL_LENGTH = 100; // email
    private static final int MAX_LINKEDIN_LENGTH = 255; // linkedin_url
    private static final String NICKNAME_REGEX = "^[a-zA-Z0-9çÇğĞıİöÖşŞüÜ\\s\\-]+$"; // Nickname regex'i

    // ==========================================
    // 1) Boş Olmayan Satır Oku (PASSWORD/GENEL)
    // ==========================================
    public static String readNonEmptyLine(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine();
            if (line == null) line = "";
            line = line.trim();

            if (!line.isEmpty()) {
                // Ham şifre için uzunluk kontrolü
                if (line.length() > MAX_RAW_PASSWORD_LENGTH) {
                    System.out.printf(ERR_PREFIX + "Input exceeds maximum length of %d characters.\n", MAX_RAW_PASSWORD_LENGTH);
                    continue;
                }
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
            String line = scanner.nextLine().trim();
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
    // 6) İsim/Soyisim Doğrulama (UZUNLUK KONTROLLÜ)
    // ==========================================
    public static String readValidName(Scanner scanner, String prompt) {
        java.util.Locale tr = java.util.Locale.forLanguageTag("tr-TR");
        
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine();
            if (input == null) input = "";
            input = input.trim();

            if(input.equals("0")) return "0";

            if (input.isEmpty()) {
                System.out.println(ERR_PREFIX + "Name field cannot be empty.");
                continue;
            }
            
            // UZUNLUK KONTROLÜ (MAX 50)
            if (input.length() > MAX_NAME_LENGTH) {
                 System.out.printf(ERR_PREFIX + "Name exceeds maximum length of %d characters.\n", MAX_NAME_LENGTH);
                 continue;
            }

            // KARAKTER KONTROLÜ
            if (!input.matches("[\\p{L}çğıöşüÇĞİÖŞÜ '\\-]+")) {
                System.out.println(ERR_PREFIX + "Name must contain only letters (no numbers).");
                continue;
            }

            // Baş Harf Büyütme İşlemi
            String lowerAll = input.toLowerCase(tr);
            String[] parts = lowerAll.split("\\s+");
            StringBuilder sb = new StringBuilder();

            for (String part : parts) {
                if (part.isEmpty()) continue;
                String first = part.substring(0, 1).toUpperCase(tr);
                String rest = part.length() > 1 ? part.substring(1) : "";
                
                if (sb.length() > 0) sb.append(' ');
                sb.append(first).append(rest);
            }

            return sb.toString();
        }
    }
    
    // ==========================================
    // 6.5) Nickname Doğrulama (UZUNLUK KONTROLLÜ)
    // ==========================================
    public static String readValidNickname(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();

            if(input.equals("0")) return "0";

            if (input.isEmpty()) {
                System.out.println(ERR_PREFIX + "Nickname cannot be empty.");
                continue;
            }

            if (input.length() > MAX_NAME_LENGTH) { // Nickname de 50 char
                 System.out.printf(ERR_PREFIX + "Nickname exceeds maximum length of %d characters.\n", MAX_NAME_LENGTH);
                 continue;
            }
            
            // Regex: Harf, Rakam, Boşluk, Tire
            if (!input.matches(NICKNAME_REGEX)) {
                System.out.println(ERR_PREFIX + "Nickname can only contain letters, numbers, dot, space, and hyphens.");
                continue;
            }
            return input;
        }
    }
    
    // ==========================================
    // 6.7) Username Doğrulama (UZUNLUK KONTROLLÜ)
    // ==========================================
    public static String readValidUsername(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();

            if(input.equals("0")) return "0";

            if (input.isEmpty()) {
                System.out.println(ERR_PREFIX + "Username cannot be empty.");
                continue;
            }

            if (input.length() > MAX_USERNAME_LENGTH) {
                 System.out.printf(ERR_PREFIX + "Username exceeds maximum length of %d characters.\n", MAX_USERNAME_LENGTH);
                 continue;
            }
            
            // Username Regex: Sadece harf, rakam, alt çizgi ve nokta
            if (!input.matches("^[A-Za-z0-9_.]+$")) {
                System.out.println(ERR_PREFIX + "Username can only contain letters, numbers, dot, and underscore.");
                continue;
            }
            return input;
        }
    }


    // ==========================================
    // 7) Telefon Doğrulama (+90 Türkiye Formatı)
    // ==========================================
    public static String readValidPhoneTR(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt + " (+90) "); 
            String input = scanner.nextLine().trim();

            if(input.equals("0")) return "0";

            String clean = input.replaceAll("[^0-9]", "");
            if (clean.length() == 11 && clean.startsWith("0")) {
                clean = clean.substring(1);
            }

            if (clean.matches("^5[0-9]{9}$")) {
                return clean; 
            }
            
            System.out.println(ERR_PREFIX + "Invalid phone number! Must be 10 digits starting with 5 (e.g. 532xxxxxxx).");
        }
    }

    // ==========================================
    // 8) Email Doğrulama (UZUNLUK KONTROLLÜ)
    // ==========================================
    public static String readValidEmail(Scanner scanner, String prompt) {
        String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();

            if(input.equals("0")) return "0";
            
            if (input.length() > MAX_EMAIL_LENGTH) {
                 System.out.printf(ERR_PREFIX + "Email exceeds maximum length of %d characters.\n", MAX_EMAIL_LENGTH);
                 continue;
            }

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
            System.out.print(prompt + " (" + util.DateUtil.getDateFormat() + "): ");
            String input = scanner.nextLine().trim();

            if(input.equals("0")) return LocalDate.now();
            
            String error = util.DateUtil.checkDateValidity(input);
            if (error == null) {
                return util.DateUtil.parse(input);
            }
            System.out.println(ERR_PREFIX + error);
        }
    }

    // =========================================================================
    // 10) LinkedIn Doğrulama (UZUNLUK KONTROLLÜ)
    // =========================================================================
    public static String readValidLinkedin(Scanner scanner, String prompt) {
        String linkedinRegex = "^https:\\/\\/([a-z]{2,3}\\.)?linkedin\\.com\\/.*$";

        while (true) {
            System.out.print(prompt + " (https://www.linkedin.com/in/______ ) [Type 'skip' to pass]: ");
            String input = scanner.nextLine().trim();
            
            if(input.equals("0")) return "0";

            if (input.equalsIgnoreCase("skip") || input.isEmpty()) {
                return null; 
            }
            
            if (input.length() > MAX_LINKEDIN_LENGTH) {
                 System.out.printf(ERR_PREFIX + "URL exceeds maximum length of %d characters.\n", MAX_LINKEDIN_LENGTH);
                 continue;
            }

            String finalUrl = input;
            if (!input.toLowerCase().contains("linkedin.com")) {
                finalUrl = "https://www.linkedin.com/in/" + input;
            } else {
                if (!finalUrl.startsWith("http")) {
                    finalUrl = "https://" + finalUrl;
                }
            }

            if (finalUrl.matches(linkedinRegex) && !finalUrl.contains(" ")) {
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