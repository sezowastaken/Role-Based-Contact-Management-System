package ui.menu;

import model.User;
import util.InputHelper;

import java.util.Scanner;

/**
 * BaseMenu provides a common console menu framework for all role-based menus.
 *
 * It is responsible for:
 *  - Showing the menu in a loop
 *  - Displaying the header and logged-in user info
 *  - Safely reading user input (no crashes on bad input)
 *  - Handling the generic "0 - Logout" option
 *
 * Concrete menus only need to:
 *  - Provide a title via getTitle()
 *  - Print their own options via printOptions()
 *  - Implement behavior for each choice in handleOption()
 */
public abstract class BaseMenu {

    protected final User currentUser;
    protected final Scanner scanner;

    protected BaseMenu(User currentUser, Scanner scanner) {
        this.currentUser = currentUser;
        this.scanner = scanner;
    }

    /**
     * Main loop of the menu.
     * Shows the menu until the user chooses to logout (option "0").
     */
    public final void show() {
        while (true) {
            clearScreen();
            printHeader();
            printUserInfo();
            printOptions();

            System.out.println(); // spacing
            String choice = InputHelper.readLine(scanner, "Select an option: ");

            if ("0".equals(choice)) {
                System.out.println("\nLogging out... See you soon, "
                        + currentUser.getName() + "!");
                pause();
                return;
            }

            handleOption(choice);
            pause();
        }
    }

    /**
     * Title that will be displayed at the top of the menu.
     */
    protected abstract String getTitle();

    /**
     * Prints the role-specific options of the menu (including "0 - Logout").
     * Implementations should only print, not read input.
     */
    protected abstract void printOptions();

    /**
     * Handles the selected option (except "0", which is handled by BaseMenu).
     *
     * @param choice the user input representing the selected option
     */
    protected abstract void handleOption(String choice);

    // ----------------- Helper methods for subclasses -----------------

    protected void printHeader() {
        String title = getTitle();
        String line = "=".repeat(Math.max(10, title.length() + 8));

        System.out.println(line);
        System.out.println("  " + title);
        System.out.println(line);
        System.out.println();
    }

    protected void printUserInfo() {
        System.out.println("Logged in as : " + currentUser.getUsername());
        System.out.println("Role         : " + currentUser.getRole());
        System.out.println();
    }

    /**
     * Waits for the user to press Enter before continuing.
     * Useful after executing an action, so the user can read the message.
     */
    protected void pause() {
        System.out.print("\nPress Enter to continue...");
        scanner.nextLine();
    }

    /**
     * Tries to clear the console screen. On unsupported terminals,
     * it will simply behave like several empty lines.
     */
    protected void clearScreen() {
        try {
            System.out.print("\033[H\033[2J");
            System.out.flush();
        } catch (Exception ignored) {
            // Fallback: do nothing, it's not critical
        }
    }
}
