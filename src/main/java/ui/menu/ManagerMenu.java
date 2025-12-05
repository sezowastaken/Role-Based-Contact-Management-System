package ui.menu;

import java.util.Scanner;
import model.User;
import util.ConsoleColors;

public class ManagerMenu extends BaseMenu {

    public ManagerMenu(User currentUser, Scanner scanner) {
        super(currentUser, scanner);
    }

    @Override
    protected String getTitle() {
        return "Manager Menu";
    }

    @Override
    protected void printOptions() {
        System.out.println(ConsoleColors.WHITE + "1 - Change password");
        System.out.println("2 - View contacts statistical info");
        System.out.println("3 - List all users");
        System.out.println("4 - Update existing user");
        System.out.println("5 - Add/employ new user");
        System.out.println("6 - Delete/fire existing user");
        System.out.println("0 - Logout" + ConsoleColors.RESET);
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
            default:
                System.out.println(ConsoleColors.RED + "\nInvalid choice. Please select one of the options above."
                        + ConsoleColors.RESET);
        }
    }

    private void handleChangePassword() {
        System.out.println("[Manager] Change password screen (TODO).");
    }

    private void handleContactStatistics() {
        System.out.println("[Manager] Contacts statistical info screen (TODO).");
    }

    private void handleListUsers() {
        System.out.println("[Manager] List all users screen (TODO).");
    }

    private void handleUpdateUser() {
        System.out.println("[Manager] Update existing user screen (TODO).");
    }

    private void handleAddUser() {
        System.out.println("[Manager] Add / employ new user screen (TODO).");
    }

    private void handleDeleteUser() {
        System.out.println("[Manager] Delete / fire user screen (TODO).");
    }
}
