package ui.menu;

import java.util.Scanner;
import model.User;

public class JuniorDevMenu extends BaseMenu {

    public JuniorDevMenu(User currentUser, Scanner scanner) {
        super(currentUser, scanner);
    }

    @Override
    protected String getTitle() {
        return "Junior Developer Menu";
    }

    @Override
    protected void printOptions() {
        System.out.println("1 - Change password");
        System.out.println("2 - List all contacts");
        System.out.println("3 - Search contacts by selected field(s)");
        System.out.println("4 - Sort results by selected field (ascending / descending)");
        System.out.println("5 - Update existing contact");
        System.out.println("0 - Logout");
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
            default:
                System.out.println("\nInvalid choice. Please select one of the options above.");
        }
    }

    private void handleChangePassword() {
        System.out.println("[Junior] Change password screen (TODO).");
    }

    private void handleListAllContacts() {
        System.out.println("[Junior] List all contacts (TODO).");
    }

    private void handleSearchContacts() {
        System.out.println("[Junior] Search contacts by field(s) (TODO).");
    }

    private void handleSortContacts() {
        System.out.println("[Junior] Sort contacts by selected field (TODO).");
    }

    private void handleUpdateContact() {
        System.out.println("[Junior] Update existing contact (TODO).");
    }
}
