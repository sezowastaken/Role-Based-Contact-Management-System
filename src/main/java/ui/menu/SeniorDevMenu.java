package ui.menu;

import java.util.Scanner;
import model.User;
import util.ConsoleColors;

public class SeniorDevMenu extends BaseMenu {

    public SeniorDevMenu(User currentUser, Scanner scanner) {
        super(currentUser, scanner);
    }

    @Override
    protected String getTitle() {
        return "Senior Developer Menu";
    }

    @Override
    protected void printOptions() {
        System.out.println(ConsoleColors.WHITE + "1 - Change password");
        System.out.println("2 - List all contacts");
        System.out.println("3 - Search contacts by selected field(s)");
        System.out.println("4 - Sort results by selected field (ascending / descending)");
        System.out.println("5 - Update existing contact");
        System.out.println("6 - Add new contact(s)");
        System.out.println("7 - Delete existing contact(s)");
        System.out.println("0 - Logout" + ConsoleColors.RESET);
    }

    @Override
    protected void handleOption(String choice) {
        switch (choice) {
            case "1":
                handleChangePassword();
                break;
            case "2":
                handleListAllContacts();
                break;
            case "3":
                handleSearchContacts();
                break;
            case "4":
                handleSortContacts();
                break;
            case "5":
                handleUpdateContact();
                break;
            case "6":
                handleAddContact();
                break;
            case "7":
                handleDeleteContact();
                break;
            default:
                System.out.println(ConsoleColors.RED + "\nInvalid choice. Please select one of the options above."
                        + ConsoleColors.RESET);
        }
    }

    private void handleChangePassword() {
        System.out.println("[Senior] Change password screen (TODO).");
    }

    private void handleListAllContacts() {
        System.out.println("[Senior] List all contacts (TODO).");
    }

    private void handleSearchContacts() {
        System.out.println("[Senior] Search contacts by field(s) (TODO).");
    }

    private void handleSortContacts() {
        System.out.println("[Senior] Sort contacts by selected field (TODO).");
    }

    private void handleUpdateContact() {
        System.out.println("[Senior] Update existing contact (TODO).");
    }

    private void handleAddContact() {
        System.out.println("[Senior] Add new contact(s) (TODO).");
    }

    private void handleDeleteContact() {
        System.out.println("[Senior] Delete contact(s) (TODO).");
    }
}
