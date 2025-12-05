package ui.menu;

import java.util.Scanner;

import model.User;
import service.ContactService;
import service.UserService;
import util.ConsoleColors; // Renkler
import util.InputHelper; // Güvenli Input

public class TesterMenu extends BaseMenu {

    private final ContactService contactService;
    private final UserService userService;

    public TesterMenu(User currentUser, Scanner scanner) {
        super(currentUser, scanner);
        this.contactService = new ContactService();
        this.userService = new UserService();
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
                // [DÜZELTME] UserService'ten silinen metot yerine bunu kullanıyoruz
                handleChangePassword();
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
            case "0":
                return;
            default:
                System.out.println(ConsoleColors.RED + "Invalid choice. Please select one of the options above."
                        + ConsoleColors.RESET);
        }
    }

    // ==========================================
    // ŞİFRE DEĞİŞTİRME (LOCAL IMPLEMENTATION)
    // ==========================================
    private void handleChangePassword() {
        System.out.println(ConsoleColors.CYAN + "\n--- Change Password ---" + ConsoleColors.RESET);

        // InputHelper ile güvenli okuma
        String oldPass = InputHelper.readNonEmptyLine(scanner, "Current Password: ");
        String newPass = InputHelper.readNonEmptyLine(scanner, "New Password: ");

        try {
            // Saf backend metoduna gönderim
            userService.changePassword(currentUser, oldPass, newPass);
            System.out.println(ConsoleColors.GREEN + "Password updated successfully." + ConsoleColors.RESET);
        } catch (Exception e) {
            System.out.println(ConsoleColors.RED + "Error: " + e.getMessage() + ConsoleColors.RESET);
        }
    }
}