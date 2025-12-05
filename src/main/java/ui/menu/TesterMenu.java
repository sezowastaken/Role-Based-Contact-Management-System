package ui.menu;

import java.util.Scanner;

import model.User;
import service.ContactService;
import service.UserService;

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
        return "Tester Menu";
    }

    @Override
    protected void printOptions() {
        System.out.println("1 - Change password");
        System.out.println("2 - List all contacts");
        System.out.println("3 - Search contacts by selected field(s)");
        System.out.println("4 - Sort contacts by selected field (ascending / descending)");
        System.out.println("0 - Logout");
    }

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
            default:
                System.out.println("\nInvalid choice. Please select one of the options above.");
        }
    }
}
