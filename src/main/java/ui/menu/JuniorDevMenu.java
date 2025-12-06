package ui.menu;

import java.util.Scanner;

import dao.UserDAO;
import model.User;
import service.ContactService;
import service.UserService;
import undo.UndoManager;
import util.ConsoleColors;
import util.InputHelper;

/**
 * Menu for Junior Developer role.
 * Allows Junior Developer to change own password, list all contacts, search contacts by selected fields, sort results by selected field, update existing contact, and undo last operation.
 */
public class JuniorDevMenu extends BaseMenu {

    private final ContactService contactService;
    private final UserService userService;

    /**
     * Creates a new JuniorDevMenu with the given current user, scanner, and undo manager.
     * @param currentUser the current user
     * @param scanner the scanner
     * @param undoManager the undo manager
     */
    public JuniorDevMenu(User currentUser, Scanner scanner, UndoManager undoManager) {
        super(currentUser, scanner, undoManager);
        this.contactService = new ContactService(undoManager);
        this.userService = new UserService(new UserDAO(), undoManager);
    }

    /**
     * Returns the title of the menu.
     * @return the title of the menu
     */
    @Override
    protected String getTitle() {
        return "Junior Developer Panel";
    }

    /**
     * Prints the options of the menu.
     */
    @Override
    protected void printOptions() {
        System.out.println(
                ConsoleColors.BLUE + "┌──────────────────────────────────────────────────────────────────────┐");
        System.out.println("│ " + ConsoleColors.WHITE
                + "                       JUNIOR DEVELOPER MENU                       " + ConsoleColors.BLUE + "  │");
        System.out.println("├──────────────────────────────────────────────────────────────────────┤");
        System.out.println(
                "│" + ConsoleColors.WHITE + " 1 - Change password    " + ConsoleColors.BLUE
                        + "                                              │");
        System.out.println(
                "│" + ConsoleColors.WHITE + " 2 - List all contacts                                "
                        + ConsoleColors.BLUE + "                │");
        System.out.println(
                "│ " + ConsoleColors.WHITE + "3 - Search contacts by selected field(s)   " + ConsoleColors.BLUE
                        + "                          │");
        System.out.println("│" + ConsoleColors.WHITE + " 4 - Sort results by selected field (ascending / descending)  "
                + ConsoleColors.BLUE + "        │");
        System.out.println("│ " + ConsoleColors.WHITE + "5 - Update existing contact    " + ConsoleColors.BLUE
                + "                                      │");
        if (undoManager != null && undoManager.canUndo()) {
            System.out.println("│ " + ConsoleColors.WHITE + "6 - Undo last operation     " + ConsoleColors.BLUE
                    + "                                         │");
        }
        System.out.println(
                "│ " + ConsoleColors.RED + "0 - Logout    " + ConsoleColors.BLUE
                        + "                                                       │");
        System.out.println(
                "└──────────────────────────────────────────────────────────────────────┘" + ConsoleColors.RESET);
    }

    /**
     * Handles the option selected by the user.
     * @param choice the choice selected by the user
     */
    @Override
    protected void handleOption(String choice) {
        switch (choice) {
            case "1":
                userService.changeOwnPasswordInteractive(currentUser, scanner);
                break;
            case "2":
                contactService.displayAllContacts();
                break;
            case "3":
                contactService.searchContactsInteractive(scanner);
                break;
            case "4":
                contactService.sortContactsInteractive(scanner);
                break;
            case "5":
                contactService.updateContactInteractive(scanner);
                break;
            case "6":
                if (undoManager != null && undoManager.canUndo()) {
                    handleUndo(); // Common UNDO behavior in BaseMenu
                } else {
                    System.out.println(ConsoleColors.YELLOW + "\nThere is nothing to undo." + ConsoleColors.RESET);
                }
                break;
            default:
                System.out.println(ConsoleColors.RED + "Invalid choice. Please select one of the options above."
                        + ConsoleColors.RESET);
        }
    }
}