package ui.menu;

import java.util.Scanner;
import model.User;
import service.UserService;
import model.Role;
import util.InputHelper;

public class ManagerMenu extends BaseMenu {

    private final UserService userService;

    public ManagerMenu(User currentUser, Scanner scanner, UserService userService) {
        super(currentUser, scanner);
        this.userService = userService; 
    }

    @Override
    protected String getTitle() {
        return "Manager Menu";
    }

    @Override
    protected void printOptions() {
        System.out.println("1 - Change password");
        System.out.println("2 - View contacts statistical info");
        System.out.println("3 - List all users");
        System.out.println("4 - Update existing user");
        System.out.println("5 - Add/employ new user");
        System.out.println("6 - Delete/fire existing user");
        System.out.println("0 - Logout");
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
                break;
        }
    }

    private void handleChangePassword() {
        System.out.println("[Manager] Change password screen.");

        String oldPassword = InputHelper.readNonEmptyLine(scanner, "Enter oldest version of password: ");
        String newPassword = InputHelper.readNonEmptyLine(scanner, "Enter a new password: ");

        try {
            userService.changePassword(currentUser, oldPassword, newPassword);
            printSuccess("Password updated successfully.");
        } catch (Exception e) {
            printError(e.getMessage());
        }
    }

    private void handleContactStatistics() {
        System.out.println("[Manager] Contacts statistical info screen.");
    }

    private void handleListUsers() {
        System.out.println("[Manager] List all users screen.");

        var users = userService.findAllUsers();
        for (User u : users) {
            System.out.println(
                "ID=" + u.getId() +
                ", username=" + u.getUsername() +
                ", name=" + u.getFullName() +
                ", role=" + u.getRole()
            );
        }
        printSuccess("Total users: " + users.size());
    }

    private void handleUpdateUser() {
        System.out.println("[Manager] Update existing user screen.");

        int userId = InputHelper.readIntInRange(scanner, "User ID to update: ", 1, Integer.MAX_VALUE);
        User target = userService.findUserById(userId);

        if (target == null) {
            printError("User not found with ID " + userId);
            return;
        }

        System.out.println("Selected user: " + target.getFullName() + " (" + target.getUsername() + ")");
        boolean confirm = InputHelper.readYesNo(scanner, "Do you want to update this user?");
        if (!confirm) {
            printError("Update cancelled.");
            return;
        }

        String newUsername = InputHelper.readNonEmptyLine(scanner, "New username: ");
        String newFirstname = InputHelper.readNonEmptyLine(scanner, "New first name: ");
        String newLastname = InputHelper.readNonEmptyLine(scanner, "New lastname: ");
        String r = InputHelper.readNonEmptyLine(scanner, "Role (1=Tester,2=Junior,3=Senior,4=Manager): ");

        Role role;
        switch (r) {
            case "1": role = Role.TESTER; break;
            case "2": role = Role.JUNIOR_DEV; break;
            case "3": role = Role.SENIOR_DEV; break;
            case "4": role = Role.MANAGER; break;
            default:
                printError("Invalid role selection!");
                return;
        }

        try {
            userService.updateUser(userId, newUsername, newFirstname, newLastname, role);
            printSuccess("User updated successfully.");
        } catch (Exception e) {
            printError(e.getMessage());
        }
    }

    private void handleAddUser() {
        System.out.println("[Manager] Add new user screen.");

        String username = InputHelper.readNonEmptyLine(scanner, "Username: ");
        String password = InputHelper.readNonEmptyLine(scanner, "Password: ");
        String first = InputHelper.readNonEmptyLine(scanner, "First name: ");
        String last = InputHelper.readNonEmptyLine(scanner, "Last name: ");
        String r = InputHelper.readNonEmptyLine(scanner, "Role (1=Tester,2=Junior,3=Senior,4=Manager): ");

        Role role;
        switch (r) {
            case "1": role = Role.TESTER; break;
            case "2": role = Role.JUNIOR_DEV; break;
            case "3": role = Role.SENIOR_DEV; break;
            case "4": role = Role.MANAGER; break;
            default:
                printError("Invalid role selection!");
                return;
        }

        try {
            userService.createUser(username, password, first, last, role);
            printSuccess("User created.");
        } catch (Exception e) {
            printError(e.getMessage());
        }
    }

    private void handleDeleteUser() {
        System.out.println("[Manager] Delete user screen.");

        int id = InputHelper.readIntInRange(scanner, "User ID to delete: ", 1, Integer.MAX_VALUE);
        User target = userService.findUserById(id);

        if (target == null) {
            printError("User not found with ID " + id);
            return;
        }

        System.out.println("Selected user: " + target.getFullName() + " (" + target.getUsername() + ")");
        boolean confirm = InputHelper.readYesNo(scanner, "Are you sure you want to delete this user?");
        if (!confirm) {
            printError("Delete cancelled.");
            return;
        }

        try {
            userService.deleteUser(id, currentUser.getId());
            printSuccess("User deleted.");
        } catch (Exception e) {
            printError(e.getMessage());
        }
    }

    // Renkli mesaj yardımcıları 
    private void printError(String msg) {
        System.out.println("\u001B[31m" + msg + "\u001B[0m");
    }

    private void printSuccess(String msg) {
        System.out.println("\u001B[32m" + msg + "\u001B[0m");
    }
}
