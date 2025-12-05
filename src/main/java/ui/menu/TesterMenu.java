package ui.menu;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import model.Contact;
import model.User;
import service.ContactService;

public class TesterMenu extends BaseMenu {

    private final ContactService contactService;

    public TesterMenu(User currentUser, Scanner scanner) {
        super(currentUser, scanner);
        this.contactService = new ContactService();
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
        System.out.println("4 - Sort results by selected field (ascending / descending)");
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
            default:
                System.out.println("\nInvalid choice. Please select one of the options above.");
        }
    }

    private void handleChangePassword() {
        System.out.println("[Tester] Change password screen (TODO).");
    }

    private void handleListAllContacts() {
        List<Contact> allContacts = contactService.getAllContacts();
        contactService.printContactsList(allContacts);
    }

    private void handleSearchContacts() {
        System.out.println("\n=== SEARCH CONTACTS ===");
        System.out.println("1 - Search by First Name");
        System.out.println("2 - Search by Last Name");
        System.out.println("3 - Search by Phone Number");
        System.out.print("Select search option: ");
        
        String searchChoice = scanner.nextLine().trim();
        List<Contact> results = new ArrayList<>();
        
        switch (searchChoice) {
            case "1":
                System.out.print("Enter first name to search: ");
                String firstName = scanner.nextLine().trim();
                results = contactService.searchByFirstName(firstName);
                break;
                
            case "2":
                System.out.print("Enter last name to search: ");
                String lastName = scanner.nextLine().trim();
                results = contactService.searchByLastName(lastName);
                break;
                
            case "3":
                System.out.print("Enter phone number to search: ");
                String phone = scanner.nextLine().trim();
                results = contactService.searchByPhoneContains(phone);
                break;
                
            default:
                System.out.println("Invalid choice!");
                return;
        }
        
        contactService.printContactsList(results);
    }

    private void handleSortContacts() {
        System.out.println("\n=== SORT CONTACTS ===");
        System.out.println("Sort by:");
        System.out.println("1 - First Name");
        System.out.println("2 - Last Name");
        System.out.println("3 - Phone Number");
        System.out.println("4 - Birth Date");
        System.out.println("5 - Created Date");
        System.out.print("Select sort field: ");
        
        String fieldChoice = scanner.nextLine().trim();
        String sortField = "contact_id";
        
        switch (fieldChoice) {
            case "1":
                sortField = "first_name";
                break;
            case "2":
                sortField = "last_name";
                break;
            case "3":
                sortField = "phone_number";
                break;
            case "4":
                sortField = "birth_date";
                break;
            case "5":
                sortField = "created_at";
                break;
            default:
                System.out.println("Invalid choice, sorting by ID as default.");
        }
        
        System.out.println("\nSort order:");
        System.out.println("1 - Ascending (A-Z / Smallest to Largest)");
        System.out.println("2 - Descending (Z-A / Largest to Smallest)");
        System.out.print("Select sort order: ");
        
        String orderChoice = scanner.nextLine().trim();
        boolean ascending = !"2".equals(orderChoice);
        
        List<Contact> sortedContacts = contactService.getAllSorted(sortField, ascending);
        contactService.printContactsList(sortedContacts);
    }
}
