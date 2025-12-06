package ui.screen;

import java.util.Scanner;

import model.Role;
import model.User;
import service.AuthService;
import service.UserService;
import undo.UndoManager;
import ui.menu.BaseMenu;
import ui.menu.TesterMenu;
import ui.menu.JuniorDevMenu;
import ui.menu.SeniorDevMenu;
import ui.menu.ManagerMenu;
import util.ConsoleColors;
import util.InputHelper;

/**
 * Handles the login screen where users enter their credentials.
 * After successful login, redirects users to their role-specific menu.
 */
public class LoginScreen {

    private final AuthService authService;
    private final Scanner scanner;
    
    /**
     * Creates a new LoginScreen with the given authentication service.
     * @param authService the authentication service to use for login
     */
    public LoginScreen(AuthService authService) {
        this.authService = authService;
        this.scanner = new Scanner(System.in);
    }

    /**
     * Starts the login loop. Continues until user successfully logs in or exits.
     * Type 'q' as username to exit the application.
     */
    public void start() {
        while (true) {
            InputHelper.clearScreen();
            System.out.println(ConsoleColors.GREEN + "===========================================");
            System.out.println("  ROLE-BASED CONTACT MANAGEMENT SYSTEM");
            System.out.println("===========================================\n" + ConsoleColors.RESET);
            System.out.println(ConsoleColors.CYAN + "Enter your username and password to log in." + ConsoleColors.RESET);
            System.out.println(ConsoleColors.YELLOW + "(To exit, type 'q' as the username.)\n" + ConsoleColors.RESET);
    
            // ✅ Username girişini InputHelper ile al
            String username = InputHelper.readValidUsername(scanner, ConsoleColors.WHITE + "Username: " + ConsoleColors.RESET);
            if (username.equalsIgnoreCase("q")) {
                InputHelper.clearScreen();
                System.out.println(ConsoleColors.GREEN + "\nExiting the application. See you next time!" + ConsoleColors.RESET);
                return;
            }
    
            // ✅ Password girişini InputHelper ile al
            String password = InputHelper.readNonEmptyLine(scanner, ConsoleColors.WHITE + "Password: " + ConsoleColors.RESET);
    
            // Giriş kontrolü
            User loggedIn = authService.login(username, password);
            if (loggedIn == null) {
                System.out.println(ConsoleColors.RED + "\nLogin failed. Incorrect username or password." + ConsoleColors.RESET);
                pressEnterToContinue();
                continue;
            }
    
            System.out.println(ConsoleColors.GREEN + "\nLogin successful. Welcome, "
                    + loggedIn.getName() + " " + loggedIn.getSurname() + "!" + ConsoleColors.RESET);
            pressEnterToContinue();
    
            openMenuForUser(loggedIn);
        }
    }
    

    /**
     * Opens the appropriate menu based on user's role.
     * @param user the logged-in user
     */
    private void openMenuForUser(User user) {
        InputHelper.clearScreen();
        Role role = user.getRole();

        if (role == null) {
            System.out.println(ConsoleColors.WHITE + "The user's role is undefined. Cannot navigate to menus." + ConsoleColors.RESET);
            pressEnterToContinue();
            return;
        }

        // create a per-session UndoManager and pass it to menus
        UndoManager undoManager = new UndoManager();
        BaseMenu menu;
        switch (role) {
            case TESTER:
                menu = new TesterMenu(user, scanner, undoManager);
                break;
            case JUNIOR_DEV:
                menu = new JuniorDevMenu(user, scanner, undoManager);
                break;
            case SENIOR_DEV:
                menu = new SeniorDevMenu(user, scanner, undoManager);
                break;
            case MANAGER:
                menu = new ManagerMenu(user, scanner, undoManager);
                break;
            default:
                System.out.println(ConsoleColors.RED + "Unsupported role: " + ConsoleColors.RESET + role);
                pressEnterToContinue();
                return;
        }

        menu.show();
    }

    /**
     * Waits for user to press Enter before continuing.
     */
    private void pressEnterToContinue() {
        System.out.print(ConsoleColors.YELLOW + "\nPress Enter to continue..." + ConsoleColors.RESET);
        scanner.nextLine();
    }
}
