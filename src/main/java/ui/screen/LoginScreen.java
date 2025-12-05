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
import util.InputHelper;

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
            InputHelper.clearScreen();
            System.out.println(ConsoleColors.GREEN + "===========================================");
            System.out.println("  ROLE-BASED CONTACT MANAGEMENT SYSTEM");
            System.out.println("===========================================\n" + ConsoleColors.RESET);
            System.out
                    .println(ConsoleColors.CYAN + "Enter your username and password to log in." + ConsoleColors.RESET);
            System.out.println(ConsoleColors.YELLOW + "(To exit, type 'q' as the username.)\n" + ConsoleColors.RESET);

            System.out.print(ConsoleColors.WHITE + "Username: " + ConsoleColors.RESET);
            String username = scanner.nextLine();
            if (username == null) {
                username = "";
            }
            username = username.trim();

            if (username.equalsIgnoreCase("q")) {
                InputHelper.clearScreen();
                System.out.println(
                        ConsoleColors.GREEN + "\nExiting the application. See you next time!" + ConsoleColors.RESET);
                return;
            }

            if (username.isEmpty()) {
                System.out.println(
                        ConsoleColors.RED + "\nUsername cannot be empty. Please try again." + ConsoleColors.RESET);
                pressEnterToContinue();
                continue;
            }

            System.out.print(ConsoleColors.WHITE + "Password: " + ConsoleColors.RESET);
            String password = scanner.nextLine();
            if (password == null) {
                password = "";
            }

            User loggedIn = authService.login(username, password);
            if (loggedIn == null) {
                System.out.println(
                        ConsoleColors.RED + "\nLogin failed. Incorrect username or password." + ConsoleColors.RESET);
                pressEnterToContinue();
                continue;
            }

            System.out.println(ConsoleColors.GREEN + "\nLogin successful. Welcome, "
                    + loggedIn.getName() + " " + loggedIn.getSurname() + "!" + ConsoleColors.RESET);
            pressEnterToContinue();

            // Open role-specific menu
            openMenuForUser(loggedIn);
            // After logout, return to login screen
        }
    }

    private void openMenuForUser(User user) {
        InputHelper.clearScreen();
        Role role = user.getRole();

        if (role == null) {
            System.out.println(ConsoleColors.WHITE + "The user's role is undefined. Cannot navigate to menus."
                    + ConsoleColors.RESET);
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
                System.out.println(ConsoleColors.RED + "Unsupported role: " + ConsoleColors.RESET + role);
                pressEnterToContinue();
                return;
        }

        // Start role menu
        menu.show();

    }

    private void pressEnterToContinue() {
        System.out.print(ConsoleColors.YELLOW + "\nPress Enter to continue..." + ConsoleColors.RESET);
        scanner.nextLine();
    }

}
