package util;

import java.util.Scanner;

/**
 * Konsoldan güvenli ve tekrar kullanılabilir şekilde input okuyan yardımcı metotlar.
 * Scanner üzerinden NumberFormatException vs. patlamaması için tüm kontrolleri burada yapıyoruz.
 */
public class InputHelper {

    /**
     * Boş olmasına izin verilmeyen string okur.
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

            System.out.println("⚠ Girdi boş olamaz. Lütfen tekrar deneyin.");
        }
    }

    /**
     * Boş da olabilir; sadece trim edip döner.
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
     * Verilen aralıkta (min–max) bir int okur. Sayı olmayan girişte patlamaz.
     */
    public static int readIntInRange(Scanner scanner, String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine();
            if (line == null) {
                line = "";
            }
            line = line.trim();

            if (!line.matches("-?\\d+")) {
                System.out.println("⚠ Lütfen geçerli bir sayı girin.");
                continue;
            }

            try {
                int value = Integer.parseInt(line);
                if (value < min || value > max) {
                    System.out.printf("⚠ Lütfen %d ile %d arasında bir sayı girin.%n", min, max);
                    continue;
                }
                return value;
            } catch (NumberFormatException e) {
                System.out.println("⚠ Sayı çok büyük. Lütfen daha küçük bir değer girin.");
            }
        }
    }

    private InputHelper() {
        // static util; instance oluşturulmasın
    }
}
