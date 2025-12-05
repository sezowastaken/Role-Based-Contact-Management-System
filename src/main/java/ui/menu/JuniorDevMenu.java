package ui.menu;

import java.util.Scanner;

import dao.UserDAO;
import model.User;
import service.ContactService;
import service.UserService;
import util.ConsoleColors; // Renkler için
import util.InputHelper; // Regexli inputlar için

public class JuniorDevMenu extends BaseMenu {

    private final ContactService contactService;
    private final UserService userService;

    public JuniorDevMenu(User currentUser, Scanner scanner) {
        super(currentUser, scanner);
        this.contactService = new ContactService();
        this.userService = new UserService(new UserDAO());
    }

    @Override
    protected String getTitle() {
        return "Junior Developer Panel";
    }

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
        if (isUndoAvailable()) {
            System.out.println("│ " + ConsoleColors.WHITE + "U - Undo last operation     " + ConsoleColors.BLUE
                    + "                                         │");
        }
        System.out.println(
                "│ " + ConsoleColors.WHITE + "0 - Logout    " + ConsoleColors.BLUE
                        + "                                                       │");
        System.out.println(
                "└──────────────────────────────────────────────────────────────────────┘" + ConsoleColors.RESET);
    }

    @Override
    protected void handleOption(String choice) {
        switch (choice) {
            case "1":
                handleChangePassword();
                break;
            case "2":
                contactService.displayAllContacts();
                break;
            case "3":
                // Regex ve InputHelper ContactService'in içinde
                contactService.searchContactsInteractive(scanner);
                break;
            case "4":
                contactService.sortContactsInteractive(scanner);
                break;
            case "5":
                // Regex ve InputHelper ContactService'in içinde
                contactService.updateContactInteractive(scanner);
                break;
            case "0":
                return;
            default:
                System.out.println(ConsoleColors.RED + "Invalid choice. Please select one of the options above."
                        + ConsoleColors.RESET);
        }
    }

    // Şifre değiştirme işlemini burada InputHelper ile yapıyoruz
    private void handleChangePassword() {
        System.out.println(ConsoleColors.CYAN + "\n--- Change Password ---" + ConsoleColors.RESET);

        String oldPass = InputHelper.readNonEmptyLine(scanner, "Current Password: ");
        String newPass = InputHelper.readNonEmptyLine(scanner, "New Password: ");

        try {
            userService.changePassword(currentUser, oldPass, newPass);
            System.out.println(ConsoleColors.GREEN + "Password updated successfully." + ConsoleColors.RESET);
        } catch (Exception e) {
            System.out.println(ConsoleColors.RED + "Error: " + e.getMessage() + ConsoleColors.RESET);
        }
    }
}