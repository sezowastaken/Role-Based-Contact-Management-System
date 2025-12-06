package ui.menu;

import java.util.Scanner;

import model.User;
import service.ContactService;
import service.UserService;
import undo.UndoManager;
import util.ConsoleColors; // Renkler
import util.InputHelper; // Güvenli Input

public class TesterMenu extends BaseMenu {

    private final ContactService contactService;
    private final UserService userService;

    public TesterMenu(User currentUser, Scanner scanner, UndoManager undoManager) {
        super(currentUser, scanner, undoManager);
        // UndoManager alan constructor'lar
        this.contactService = new ContactService(undoManager);
        this.userService = new UserService(undoManager);
        // Eğer UserService tarafında UserDAO alan başka bir constructor kullanıyorsan:
        // this.userService = new UserService(new UserDAO(), undoManager);
    }

    @Override
    protected String getTitle() {
        return "Tester Panel";
    }

    @Override
    protected void printOptions() {
        System.out.println("┌──────────────────────────────────────────────────────────────────────┐");
        System.out.println("│                             TESTER MENU                              │");
        System.out.println("├──────────────────────────────────────────────────────────────────────┤");
        System.out.println("│ 1 - Change password                                                  │");
        System.out.println("│ 2 - List all contacts                                                │");
        System.out.println("│ 3 - Search contacts by selected field(s)                             │");
        System.out.println("│ 4 - Sort results by selected field (ascending / descending)          │");
        if (undoManager != null && undoManager.canUndo()) {
            System.out.println("| 5 - Undo last operation |                                          │");
        }
        System.out.println("│ 0 - Logout                                                           │");
        System.out.println("└──────────────────────────────────────────────────────────────────────┘");
    }

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
                    System.out.println("\nThere is nothing to undo.");
                }
                break;
            default:
                System.out.println(ConsoleColors.RED + "Invalid choice. Please select one of the options above."
                        + ConsoleColors.RESET);
        }
    }
}