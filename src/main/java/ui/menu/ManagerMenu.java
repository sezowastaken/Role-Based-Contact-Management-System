package ui.menu;

import java.util.Scanner;

import dao.UserDAO;
import model.User;
import service.ContactService;
import service.UserService;
import undo.UndoManager;
import service.StatisticsService;
import model.Role;
import util.InputHelper;
import util.ConsoleColors;

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
        return "Manager Panel";
    }

    @Override
    protected void printOptions() {
        System.out.println(
                ConsoleColors.MAGENTA + "┌──────────────────────────────────────────────────────────────────────┐");
        System.out.println(
                "│ " + ConsoleColors.WHITE + "                            MANAGER MENU                    "
                        + ConsoleColors.MAGENTA + "         │");
        System.out.println("├──────────────────────────────────────────────────────────────────────┤");
        System.out.println("│ " + ConsoleColors.WHITE + "1 - Change password                               "
                + ConsoleColors.MAGENTA + "                   │");
        System.out.println(
                "│ " + ConsoleColors.WHITE + "2 - View contacts statistical info              " + ConsoleColors.MAGENTA
                        + "                     │");
        System.out.println("│" + ConsoleColors.WHITE + " 3 - List all users                          "
                + ConsoleColors.MAGENTA + "                         │");
        System.out.println(
                "│ " + ConsoleColors.WHITE + "4 - Update existing user                           "
                        + ConsoleColors.MAGENTA + "                  │");
        System.out.println(
                "│ " + ConsoleColors.WHITE + "5 - Add/employ new user         " + ConsoleColors.MAGENTA
                        + "                                     │");
        System.out.println("│ " + ConsoleColors.WHITE + "6 - Delete/fire existing user        " + ConsoleColors.MAGENTA
                + "                                │");
        if (undoManager != null && undoManager.canUndo()) {
            System.out.println("|" + ConsoleColors.WHITE + " 7 - Undo last operation                  "
                    + ConsoleColors.MAGENTA + "                            │");
        }
        System.out.println(
                "│ " + ConsoleColors.RED + "0 - Logout                                                  "
                        + ConsoleColors.MAGENTA + "         │");
        System.out.println(
                "└──────────────────────────────────────────────────────────────────────┘" + ConsoleColors.RESET);
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
                // Delegate to service — interactive listing handled inside UserService
                userService.listAllUsersInteractive();
                break;
            case "4":
                // Delegate update interaction to service
                userService.updateUserInteractive(scanner);
                break;
            case "5":
                // Delegate create interaction to service
                userService.addUserInteractive(scanner);
                break;
            case "6":
                // Delegate delete interaction to service (providing current user for
                // self-delete check)
                userService.deleteUserInteractive(scanner, currentUser);
                break;

            case "7":
                // Menüde gözükmese bile biri 7 girerse kontrol edelim
                if (undoManager != null && undoManager.canUndo()) {
                    handleUndo(); // BaseMenu'deki ortak undo metodu
                } else {
                    System.out.println(ConsoleColors.YELLOW + "\nThere is nothing to undo." + ConsoleColors.RESET);
                }
                break;

            case "0":
                // BaseMenu'deki show() zaten 0 için logout yapıyor → burada sadece return
                return;

            default:
                System.out.println(ConsoleColors.RED + "\nInvalid choice. Please select one of the options above."
                        + ConsoleColors.RESET);
        }
    }
}
