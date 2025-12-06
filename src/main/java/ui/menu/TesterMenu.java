package ui.menu;

import java.util.Scanner;

import model.User;
import service.ContactService;
import service.UserService;
import undo.UndoManager;
import util.ConsoleColors; // Colors
import util.InputHelper; // Safe Input

/**
 * Menu for Tester role.
 * Allows Tester to change own password, list all contacts, search contacts by selected fields, sort results by selected field, and undo last operation.
 */
public class TesterMenu extends BaseMenu {

    private final ContactService contactService;
    private final UserService userService;

    /**
     * Creates a new TesterMenu with the given current user, scanner, and undo manager.
     * @param currentUser the current user
     * @param scanner the scanner
     * @param undoManager the undo manager
     */
    public TesterMenu(User currentUser, Scanner scanner, UndoManager undoManager) {
        super(currentUser, scanner, undoManager);
        // UndoManager constructor
        this.contactService = new ContactService(undoManager);
        this.userService = new UserService(undoManager);
        // If UserService has another constructor for UserDAO:
        // this.userService = new UserService(new UserDAO(), undoManager);
    }

    /**
     * Returns the title of the menu.
     * @return the title of the menu
     */
    @Override
    protected String getTitle() {
        return "Tester Panel";
    }

    /**
     * Prints the options of the menu.
     */
    @Override
    protected void printOptions() {
        System.out.println(
                ConsoleColors.YELLOW + "┌──────────────────────────────────────────────────────────────────────┐");
        System.out.println("│       " + ConsoleColors.WHITE + "                      TESTER MENU                      "
                + ConsoleColors.YELLOW + "        │");
        System.out.println("├──────────────────────────────────────────────────────────────────────┤");
        System.out.println(
                "│ " + ConsoleColors.WHITE + "1 - Change password                                "
                        + ConsoleColors.YELLOW + "                  │");
        System.out.println(
                "│ " + ConsoleColors.WHITE + "2 - List all contacts            " + ConsoleColors.YELLOW
                        + "                                    │");
        System.out.println(
                "│ " + ConsoleColors.WHITE + "3 - Search contacts by selected field(s)             "
                        + ConsoleColors.YELLOW + "                │");
        System.out.println(
                "│" + ConsoleColors.WHITE + " 4 - Sort results by selected field (ascending / descending)     "
                        + ConsoleColors.YELLOW + "     │");
        if (undoManager != null && undoManager.canUndo()) {
            System.out.println("| " + ConsoleColors.WHITE
                    + "5 - Undo last operation |                       " + ConsoleColors.YELLOW
                    + "                     │");
        }
        System.out.println(
                "│ " + ConsoleColors.RED + "0 - Logout              " + ConsoleColors.YELLOW
                        + "                                             │");
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
                // Tester kendi şifresini değiştirebiliyor
                userService.changeOwnPasswordInteractive(currentUser, scanner);
                break;
            case "2":
                contactService.displayAllContacts();
                break;
            case "3":
                // ContactService içindeki Regex/InputHelper korumalı metodu çağırır
                contactService.searchContactsInteractive(scanner);
                break;
            case "4":
                contactService.sortContactsInteractive(scanner);
                break;
            case "5":
                // Menüde 5 yazmıyorsa da kullanıcı elle girerse:
                if (undoManager != null && undoManager.canUndo()) {
                    handleUndo();
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