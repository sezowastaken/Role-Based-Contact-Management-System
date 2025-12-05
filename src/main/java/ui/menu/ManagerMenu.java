package ui.menu;

import java.util.Scanner;

import dao.UserDAO;
import model.User;
import service.ContactService;
import service.UserService;
import undo.UndoManager;
import service.StatisticsService;

public class ManagerMenu extends BaseMenu {

    private final ContactService contactService;
    private final UserService userService;
    private final StatisticsService statisticsService;

    public ManagerMenu(User currentUser, Scanner scanner, UndoManager undoManager) {
        super(currentUser, scanner, undoManager);
        this.contactService = new ContactService(undoManager);
        this.userService = new UserService(new UserDAO(), undoManager);
        this.statisticsService = new StatisticsService();
    }

    @Override
    protected String getTitle() {
        return "Manager Menu";
    }

    @Override
    protected void printOptions() {
        System.out.println("1 - Change password");
        System.out.println("2 - Show statistical summary of contacts");
        System.out.println("3 - List all users");
        System.out.println("4 - Add new user");
        System.out.println("5 - Update user");
        System.out.println("6 - Delete user");

        // Undo seçeneğini sadece stack boş değilse gösteriyoruz
        if (undoManager != null && undoManager.canUndo()) {
            System.out.println("7 - Undo last operation");
        }

        System.out.println("0 - Logout");
    }

    @Override
    protected void handleOption(String choice) {
        switch (choice) {
            case "1":
                userService.changeOwnPasswordInteractive(currentUser, scanner);
                break;
            case "2":
                statisticsService.displayContactStatistics();
                break;
            case "3":
                userService.listAllUsers();
                break;
            case "4":
                userService.addUserInteractive(scanner);
                break;
            case "5":
                userService.updateUserInteractive(scanner);
                break;
            case "6":
                userService.deleteUserInteractive(scanner);
                break;

            case "7":
                // Menüde gözükmese bile biri 7 girerse kontrol edelim
                if (undoManager != null && undoManager.canUndo()) {
                    handleUndo(); // BaseMenu'deki ortak undo metodu
                } else {
                    System.out.println("\nThere is nothing to undo.");
                }
                break;

            case "0":
                // BaseMenu'deki show() zaten 0 için logout yapıyor → burada sadece return
                return;

            default:
                System.out.println("\nInvalid choice. Please select one of the options above.");
        }
    }
}
