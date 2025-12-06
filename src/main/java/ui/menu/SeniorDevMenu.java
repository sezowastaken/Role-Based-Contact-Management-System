package ui.menu;

import java.util.Scanner;

import dao.UserDAO;
import model.User;
import service.ContactService;
import service.UserService;
import undo.UndoManager;
import util.ConsoleColors; // Colors
import util.InputHelper; // Safe Input

/**
 * Menu for Senior Developer role.
 * Allows Senior Developer to change own password, list all contacts, search contacts by selected fields, sort results by selected field, update existing contact, add new contact, delete existing contact, and undo last operation.
 */
public class SeniorDevMenu extends BaseMenu {

    private final ContactService contactService;
    private final UserService userService;

    /**
     * Creates a new SeniorDevMenu with the given current user, scanner, and undo manager.
     * @param currentUser the current user
     * @param scanner the scanner
     * @param undoManager the undo manager
     */
    public SeniorDevMenu(User currentUser, Scanner scanner, UndoManager undoManager) {
        super(currentUser, scanner, undoManager);
        // Undo supported service examples
        this.contactService = new ContactService(undoManager);
        this.userService = new UserService(new UserDAO(), undoManager);
    }

    /**
     * Returns the title of the menu.
     * @return the title of the menu
     */
    @Override
    protected String getTitle() {
        return "Senior Developer Menu";
    }

    /**
     * Prints the options of the menu.
     */
    @Override
    protected void printOptions() {
        System.out.println(
                ConsoleColors.GREEN + "┌──────────────────────────────────────────────────────────────────────┐");
        System.out.println(
                "│  " + ConsoleColors.WHITE + "                      SENIOR DEVELOPER MENU                "
                        + ConsoleColors.GREEN + "         │");
        System.out.println("├──────────────────────────────────────────────────────────────────────┤");
        System.out.println(
                "│ " + ConsoleColors.WHITE + "1 - Change password                                  "
                        + ConsoleColors.GREEN + "                │");
        System.out.println("│ " + ConsoleColors.WHITE + "2 - List all contacts                    "
                + ConsoleColors.GREEN + "                            │");
        System.out.println(
                "│ " + ConsoleColors.WHITE + "3 - Search contacts by selected field(s)        " + ConsoleColors.GREEN
                        + "                     │");
        System.out.println(
                "│ " + ConsoleColors.WHITE + "4 - Sort results by selected field (ascending / descending)   "
                        + ConsoleColors.GREEN + "       │");
        System.out.println(
                "│ " + ConsoleColors.WHITE + "5 - Update existing contact                " + ConsoleColors.GREEN
                        + "                          │");
        System.out.println(
                "│ " + ConsoleColors.WHITE + "6 - Add new contact(s)                          " + ConsoleColors.GREEN
                        + "                     │");
        System.out.println(
                "│ " + ConsoleColors.WHITE + "7 - Delete existing contact(s)                             "
                        + ConsoleColors.GREEN + "          │");
        if (undoManager != null && undoManager.canUndo()) {
            System.out.println("|" + ConsoleColors.WHITE
                    + " 8 - Undo last operation                             " + ConsoleColors.GREEN
                    + "                 │");
        }
        System.out.println("│ " + ConsoleColors.RED + "0 - Logout           " + ConsoleColors.GREEN
                + "                                                │");
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
                // Change password using UserService
                userService.changeOwnPasswordInteractive(currentUser, scanner);
                break;
            case "2":
                contactService.displayAllContacts();
                break;
            case "3":
                // ContactService now uses InputHelper
                contactService.searchContactsInteractive(scanner);
                break;
            case "4":
                contactService.sortContactsInteractive(scanner);
                break;
            case "5":
                contactService.updateContactInteractive(scanner);
                break;
            case "6":
                contactService.addContactInteractive(scanner);
                break;
            case "7":
                contactService.deleteContactInteractive(scanner);
                break;
            case "8":
                // Even if the option is not shown, check if the user can undo
                if (undoManager != null && undoManager.canUndo()) {
                    handleUndo(); // Common UNDO behavior in BaseMenu
                } else {
                    System.out.println(ConsoleColors.YELLOW + "\nThere is nothing to undo." + ConsoleColors.RESET);
                }
                break;
            default:
                System.out.println(ConsoleColors.RED + "\nInvalid choice. Please select one of the options above."
                        + ConsoleColors.RESET);
        }
    }

}