package ui.screen;

import java.io.Console;
import java.util.Scanner;

import model.Role;
import model.User;
import service.AuthService;
import ui.menu.BaseMenu;
import ui.menu.TesterMenu;
import util.ConsoleColors;
import ui.menu.JuniorDevMenu;
import ui.menu.SeniorDevMenu;
import ui.menu.ManagerMenu;

public class LoginScreen {

    private final AuthService authService;
    private final Scanner scanner;

    public LoginScreen(AuthService authService) {
        this.authService = authService;
        this.scanner = new Scanner(System.in);
    }

    /**
     * Starts the login flow of the application.
     * Once the user successfully logs in, opens the menu according to their role.
     */
    public void start() {
        while (true) {
            clearScreen();
            System.out.println(ConsoleColors.MAGENTA + "===========================================");
            System.out.println("  ROLE-BASED CONTACT MANAGEMENT SYSTEM");
            System.out.println("===========================================\n" + ConsoleColors.RESET);
            System.out.println("Enter your username and password to log in.");
            System.out.println("(To exit, type 'q' as the username.)\n");

            System.out.print("Username: ");
            String username = scanner.nextLine();
            if (username == null) {
                username = "";
            }
            username = username.trim();

            if (username.equalsIgnoreCase("q")) {
                System.out.println("\nExiting the application. See you next time!");
                return;
            }

            if (username.isEmpty()) {
                System.out.println("\n Username cannot be empty. Please try again.");
                pressEnterToContinue();
                continue;
            }

            System.out.print("Password: ");
            String password = scanner.nextLine();
            if (password == null) {
                password = "";
            }

            User loggedIn = authService.login(username, password);
            if (loggedIn == null) {
                System.out.println("\n Login failed. Incorrect username or password.");
                pressEnterToContinue();
                continue;
            }

            System.out.println("\n Login successful. Welcome, "
                    + loggedIn.getName() + " " + loggedIn.getSurname() + "!");
            pressEnterToContinue();

            // Open role-specific menu
            openMenuForUser(loggedIn);
            // After logout, return to login screen
        }
    }

    private void openMenuForUser(User user) {
        Role role = user.getRole();

        if (role == null) {
            System.out.println(" The user's role is undefined. Cannot navigate to menus.");
            pressEnterToContinue();
            return;
        }

        BaseMenu menu;
        switch (role) {
            case TESTER:
                menu = new TesterMenu(user, scanner);
                break;
            case JUNIOR_DEV:
                menu = new JuniorDevMenu(user, scanner);
                break;
            case SENIOR_DEV:
                menu = new SeniorDevMenu(user, scanner);
                break;
            case MANAGER:
                menu = new ManagerMenu(user, scanner);
                break;
            default:
                System.out.println(" Unsupported role: " + role);
                pressEnterToContinue();
                return;
        }

        // Start role menu
        menu.show();
    }

    private void pressEnterToContinue() {
        System.out.print("\nPress Enter to continue...");
        scanner.nextLine();
    }

    private void clearScreen() {
        // Console clear – acts like empty lines on unsupported environments
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}
