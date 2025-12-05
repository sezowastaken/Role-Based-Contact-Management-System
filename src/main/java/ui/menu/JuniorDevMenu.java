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
        System.out.println("1 - Change password");
        System.out.println("2 - List all contacts");
        System.out.println("3 - Search contacts");
        System.out.println("4 - Sort contacts");
        System.out.println("5 - Update existing contact"); // Junior yetkisi
        System.out.println("0 - Logout");
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
                System.out.println(ConsoleColors.RED + "Invalid choice. Please select one of the options above." + ConsoleColors.RESET);
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