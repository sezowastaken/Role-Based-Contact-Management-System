package util;

import java.util.Scanner;

/**
 * Helper methods for safely and consistently reading input from the console.
 * Prevents crashes such as NumberFormatException by validating input here.
 */
public class InputHelper {

    /**
     * Reads a non-empty string. Keeps asking until a valid non-empty input is
     * given.
     */
    public static String readNonEmptyLine(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine();
            if (line == null) {
                line = "";
            }
            line = line.trim();

            if (!line.isEmpty()) {
                return line;
            }

            // Error message for empty input
            System.out.println(
                    util.ConsoleColors.RED + "Input cannot be empty. Please try again." + util.ConsoleColors.RESET);
        }
    }

    /**
     * Reads a line which may be empty. Simply trims and returns the value.
     */
    public static String readLine(Scanner scanner, String prompt) {
        System.out.print(prompt);
        String line = scanner.nextLine();
        if (line == null) {
            return "";
        }
        return line.trim();
    }

    /**
     * Reads an integer within the given range (min–max).
     * Does not crash on invalid input (e.g., non-numeric characters).
     */
    public static int readIntInRange(Scanner scanner, String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine();
            if (line == null) {
                line = "";
            }
            line = line.trim();

            // Error: not a valid integer
            if (!line.matches("-?\\d+")) {
                System.out
                        .println(util.ConsoleColors.RED + "Please enter a valid number." + util.ConsoleColors.RESET);
                continue;
            }

            try {
                int value = Integer.parseInt(line);

                // Error: value is out of allowed range
                if (value < min || value > max) {
                    System.out.printf(util.ConsoleColors.RED
                            + "Please enter a number between %d and %d.\n"
                            + util.ConsoleColors.RESET, min, max);
                    continue;
                }

                return value;

            } catch (NumberFormatException e) {
                // Error: integer overflow or too large number
                System.out.println(util.ConsoleColors.RED + "Number is too large. Please enter a smaller value."
                        + util.ConsoleColors.RESET);
            }
        }
    }

    /**
     * Reads a yes/no answer from the user.
     * Keeps asking until the user enters 'y' or 'n'.
     */
    public static boolean readYesNo(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt + " (y/n): ");
            String input = scanner.nextLine();
            if (input == null) {
                input = "";
            }
            input = input.trim().toLowerCase();

            if (input.equals("y")) {
                return true;
            } else if (input.equals("n")) {
                return false;
            } else {
                System.out.println(util.ConsoleColors.RED + "Please enter 'y' or 'n'." + util.ConsoleColors.RESET);
            }
        }
    }

    /**
     * Sadece ad / soyad gibi alanlar için geçerli bir string okur.
     * - Boş bırakılamaz
     * - Sadece harf, boşluk, ' ve - karakterlerine izin verir
     * - Türkçe kurallara göre ilk harf BÜYÜK, kalan harfler küçük yapılır
     * (I / İ / ı / i dönüşümleri dahil)
     * Örn: "ahMEt" -> "Ahmet", "ışIK" -> "Işık", "iSMAİL" -> "İsmail"
     */
    public static String readValidName(Scanner scanner, String prompt) {
        java.util.Locale tr = java.util.Locale.forLanguageTag("tr-TR");
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine();

            if (input == null) {
                input = "";
            }
            input = input.trim();

            if (input.isEmpty()) {
                System.out.println("⚠ İsim/soyisim boş olamaz. Lütfen tekrar deneyin.");
                continue;
            }

            // Sadece harf, boşluk, ', - izin veriyoruz (Türkçe harfler dahil)
            if (!input.matches("[\\p{L}çğıöşüÇĞİÖŞÜ '\\-]+")) {
                System.out.println("⚠ İsim/soyisim yalnızca harf içermelidir. Lütfen tekrar deneyin.");
                continue;
            }

            // TÜRKÇE locale ile küçük harfe çevir
            String lowerAll = input.toLowerCase(tr);

            // Birden fazla kelime (örn: "Ali Can", "Ayşe Nur") için her kelimenin başını
            // büyüt
            String[] parts = lowerAll.split("\\s+");
            StringBuilder sb = new StringBuilder();

            for (String part : parts) {
                if (part.isEmpty()) {
                    continue;
                }
                String first = part.substring(0, 1).toUpperCase(tr);
                String rest = "";
                if (part.length() > 1) {
                    rest = part.substring(1); // zaten lowerAll olduğu için küçük kalacak
                }
                if (sb.length() > 0) {
                    sb.append(' ');
                }
                sb.append(first).append(rest);
            }

            return sb.toString();
        }
    }

    private InputHelper() {
        // static util; instance oluşturulmasın
    }

    public static void clearScreen() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                new ProcessBuilder("clear").inheritIO().start().waitFor();
            }
        } catch (Exception e) {
            System.out.println("Console is cleared: " + e.getMessage());
        }
    }
}
