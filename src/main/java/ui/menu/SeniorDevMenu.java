package ui.menu;

import java.util.Scanner;

import dao.UserDAO;
import model.User;
import service.ContactService;
import service.UserService;

public class SeniorDevMenu extends BaseMenu {

    private final ContactService contactService;
    private final UserService userService;

    public SeniorDevMenu(User currentUser, Scanner scanner) {
        super(currentUser, scanner);
        this.contactService = new ContactService();
        this.userService = new UserService(new UserDAO());
    }

    @Override
    protected String getTitle() {
        return "Senior Developer Menu";
    }

    @Override
    protected void printOptions() {
        System.out.println("1 - Change password");
        System.out.println("2 - List all contacts");
        System.out.println("3 - Search contacts by selected field(s)");
        System.out.println("4 - Sort contacts by selected field (ascending / descending)");
        System.out.println("5 - Update existing contact");
        System.out.println("6 - Add new contact(s)");
        System.out.println("7 - Delete existing contact(s)");
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
            case "5":
                contactService.updateContactInteractive(scanner);
                break;
            case "6":
                contactService.addContactInteractive(scanner);
                break;
            case "7":
                contactService.deleteContactInteractive(scanner);
                break;
            default:
                System.out.println("\nInvalid choice. Please select one of the options above.");
        }
    }
}
