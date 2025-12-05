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
