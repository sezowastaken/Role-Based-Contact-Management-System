package ui.menu;

import java.util.Scanner;
import model.User;
import service.UserService;
import service.StatisticsService;
import model.Role;
import util.InputHelper;
import util.ConsoleColors;

public class ManagerMenu extends BaseMenu {

    private final UserService userService;
    private final StatisticsService statisticsService;

    public ManagerMenu(User currentUser, Scanner scanner, UserService userService) {
        super(currentUser, scanner);
        this.userService = userService;
        this.statisticsService = new StatisticsService();
    }

    @Override
    protected String getTitle() {
        return "Manager Panel";
    }

    @Override
    protected void printOptions() {
        System.out.println("┌──────────────────────────────────────────────────────────────────────┐");
        System.out.println("│                             MANAGER MENU                             │");
        System.out.println("├──────────────────────────────────────────────────────────────────────┤");
        System.out.println("│ 1 - Change password                                                  │");
        System.out.println("│ 2 - View contacts statistical info                                   │");
        System.out.println("│ 3 - List all users                                                   │");
        System.out.println("│ 4 - Update existing user                                             │");
        System.out.println("│ 5 - Add/employ new user                                              │");
        System.out.println("│ 6 - Delete/fire existing user                                        │");
        if (isUndoAvailable()) {
            System.out.println("│ U - Undo last operation                                              │");
        }
        System.out.println("│ 0 - Logout                                                           │");
        System.out.println("└──────────────────────────────────────────────────────────────────────┘");
    }

    @Override
    protected void handleOption(String choice) {
        switch (choice) {
            case "1":
                handleChangePassword();
                break;
            case "2":
                handleContactStatistics();
                break;
            case "3":
                handleListUsers();
                break;
            case "4":
                handleUpdateUser();
                break;
            case "5":
                handleAddUser();
                break;
            case "6":
                handleDeleteUser();
                break;
            case "0":
                return;
            default:
                printError("Invalid choice. Please select one of the options above.");
        }
    }

    private void handleChangePassword() {
        System.out.println(ConsoleColors.CYAN + "\n--- Change Password ---" + ConsoleColors.RESET);
        System.out.println(ConsoleColors.YELLOW + "Enter '0' at any time to cancel." + ConsoleColors.RESET);

        String oldPassword = InputHelper.readNonEmptyLine(scanner, "Enter old password (0 to cancel): ");
        if (oldPassword.equals("0")) {
            System.out.println(ConsoleColors.YELLOW + "Password change cancelled." + ConsoleColors.RESET);
            return;
        }

        String newPassword = InputHelper.readNonEmptyLine(scanner, "Enter new password (0 to cancel): ");
        if (newPassword.equals("0")) {
            System.out.println(ConsoleColors.YELLOW + "Password change cancelled." + ConsoleColors.RESET);
            return;
        }

        try {
            userService.changePassword(currentUser, oldPassword, newPassword);
            printSuccess("Password updated successfully.");
        } catch (Exception e) {
            printError(e.getMessage());
        }
    }

    private void handleContactStatistics() {
        statisticsService.displayContactStatistics();
    }

    private void handleListUsers() {
        System.out.println(ConsoleColors.CYAN + "\n--- System Users List ---" + ConsoleColors.RESET);

        var users = userService.findAllUsers();

        System.out.printf(ConsoleColors.YELLOW + "%-5s %-15s %-25s %-15s%n" + ConsoleColors.RESET, "ID", "USERNAME",
                "FULL NAME", "ROLE");
        System.out.println("----------------------------------------------------------------");

        for (User u : users) {
            // [DÜZELTME] getUserId() yerine getId() kullanıldı
            System.out.printf("%-5d %-15s %-25s %-15s%n",
                    u.getId(),
                    u.getUsername(),
                    u.getName() + " " + u.getSurname(),
                    u.getRole());
        }
        System.out.println("----------------------------------------------------------------");
        printSuccess("Total users: " + users.size());
    }

    private void handleUpdateUser() {
        System.out.println(ConsoleColors.CYAN + "\n--- Update Existing User ---" + ConsoleColors.RESET);
        System.out.println(ConsoleColors.YELLOW + "Enter '0' at any time to cancel." + ConsoleColors.RESET);

        handleListUsers();

        int userId = InputHelper.readIntInRange(scanner, "User ID to update (0 to cancel): ", 0, Integer.MAX_VALUE);
        if (userId == 0) {
            System.out.println(ConsoleColors.YELLOW + "Update cancelled." + ConsoleColors.RESET);
            return;
        }
        
        User target = userService.findUserById(userId);

        if (target == null) {
            printError("User not found with ID " + userId);
            return;
        }

        System.out.println("Selected: " + target.getName() + " (" + target.getUsername() + ")");

        boolean confirm = InputHelper.readYesNo(scanner, "Do you want to update this user?");
        if (!confirm) {
            printError("Update cancelled.");
            return;
        }

        String newUsername = InputHelper.readNonEmptyLine(scanner, "New username (0 to cancel): ");
        if (newUsername.equals("0")) {
            System.out.println(ConsoleColors.YELLOW + "Update cancelled." + ConsoleColors.RESET);
            return;
        }
        
        String newFirstname = InputHelper.readValidName(scanner, "New first name (0 to cancel): ");
        if (newFirstname.equals("0")) {
            System.out.println(ConsoleColors.YELLOW + "Update cancelled." + ConsoleColors.RESET);
            return;
        }
        
        String newLastname = InputHelper.readValidName(scanner, "New lastname (0 to cancel): ");
        if (newLastname.equals("0")) {
            System.out.println(ConsoleColors.YELLOW + "Update cancelled." + ConsoleColors.RESET);
            return;
        }

        Role role = selectRole();
        if (role == null)
            return;

        try {
            userService.updateUser(userId, newUsername, newFirstname, newLastname, role);
            printSuccess("User updated successfully.");
        } catch (Exception e) {
            printError(e.getMessage());
        }
    }

    private void handleAddUser() {
        System.out.println(ConsoleColors.CYAN + "\n--- Hire New User ---" + ConsoleColors.RESET);
        System.out.println(ConsoleColors.YELLOW + "Enter '0' at any time to cancel." + ConsoleColors.RESET);

        String username = InputHelper.readNonEmptyLine(scanner, "Username (0 to cancel): ");
        if (username.equals("0")) {
            System.out.println(ConsoleColors.YELLOW + "Add user cancelled." + ConsoleColors.RESET);
            return;
        }
        
        String password = InputHelper.readNonEmptyLine(scanner, "Password (0 to cancel): ");
        if (password.equals("0")) {
            System.out.println(ConsoleColors.YELLOW + "Add user cancelled." + ConsoleColors.RESET);
            return;
        }

        String first = InputHelper.readValidName(scanner, "First name (0 to cancel): ");
        if (first.equals("0")) {
            System.out.println(ConsoleColors.YELLOW + "Add user cancelled." + ConsoleColors.RESET);
            return;
        }
        
        String last = InputHelper.readValidName(scanner, "Last name (0 to cancel): ");
        if (last.equals("0")) {
            System.out.println(ConsoleColors.YELLOW + "Add user cancelled." + ConsoleColors.RESET);
            return;
        }

        Role role = selectRole();
        if (role == null)
            return;

        try {
            userService.createUser(username, password, first, last, role);
            printSuccess("User created successfully.");
        } catch (Exception e) {
            printError(e.getMessage());
        }
    }

    private void handleDeleteUser() {
        System.out.println(ConsoleColors.RED + "\n--- Fire (Delete) User ---" + ConsoleColors.RESET);
        System.out.println(ConsoleColors.YELLOW + "Enter '0' at any time to cancel." + ConsoleColors.RESET);

        handleListUsers();

        int id = InputHelper.readIntInRange(scanner, "User ID to delete (0 to cancel): ", 0, Integer.MAX_VALUE);
        if (id == 0) {
            System.out.println(ConsoleColors.YELLOW + "Delete cancelled." + ConsoleColors.RESET);
            return;
        }

        // [DÜZELTME] getUserId() yerine getId() kullanıldı
        if (id == currentUser.getId()) {
            printError("You cannot delete your own account!");
            return;
        }

        User target = userService.findUserById(id);
        if (target == null) {
            printError("User not found with ID " + id);
            return;
        }

        System.out.println("Selected user: " + target.getName() + " (" + target.getUsername() + ")");
        boolean confirm = InputHelper.readYesNo(scanner,
                ConsoleColors.RED + "Are you SURE you want to delete this user?" + ConsoleColors.RESET);

        if (!confirm) {
            printError("Delete cancelled.");
            return;
        }

        try {
            // [DÜZELTME] getUserId() yerine getId() kullanıldı
            userService.deleteUser(id, currentUser.getId());
            printSuccess("User deleted.");
        } catch (Exception e) {
            printError(e.getMessage());
        }
    }

    private Role selectRole() {
        System.out.println("Select Role:");
        System.out.println("1 - Tester");
        System.out.println("2 - Junior Developer");
        System.out.println("3 - Senior Developer");
        System.out.println("4 - Manager");

        int choice = InputHelper.readIntInRange(scanner, "Role Choice (1-4): ", 1, 4);

        switch (choice) {
            case 1:
                return Role.TESTER;
            case 2:
                return Role.JUNIOR_DEV;
            case 3:
                return Role.SENIOR_DEV;
            case 4:
                return Role.MANAGER;
            default:
                printError("Invalid role.");
                return null;
        }
    }

    private void printError(String msg) {
        System.out.println(ConsoleColors.RED + "ERROR: " + msg + ConsoleColors.RESET);
    }

    private void printSuccess(String msg) {
        System.out.println(ConsoleColors.GREEN + "SUCCESS: " + msg + ConsoleColors.RESET);
    }
}