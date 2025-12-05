package service;

import dao.ContactDAO;
import model.Contact;
import util.InputHelper;

import java.util.List;
import java.util.Scanner;

public class ContactService {
    private final ContactDAO contactDAO;

    public ContactService() {
        this.contactDAO = new ContactDAO();
    }

    public List<Contact> getAllContacts() {
        return contactDAO.getAllContacts();
    }

    public List<Contact> getAllSorted(String sortField, boolean ascending) {
        return contactDAO.getAllSorted(sortField, ascending);
    }

    public List<Contact> searchByFirstName(String query) {
        return contactDAO.searchByFirstName(query);
    }
    
    public List<Contact> searchByLastName(String query) {
        return contactDAO.searchByLastName(query);
    }
    public List<Contact> searchByPhoneContains(String digits) {
        return contactDAO.searchByPhoneContains(digits);
    }

    // =====================================================
    // MULTI-FIELD SEARCH METHODS
    // =====================================================

    public List<Contact> searchByFirstNameAndBirthMonth(String firstName, int month) {
        return contactDAO.searchByFirstNameAndBirthMonth(firstName, month);
    }

    public List<Contact> searchByPhoneAndEmailContains(String phonePart, String emailPart) {
        return contactDAO.searchByPhoneAndEmailContains(phonePart, emailPart);
    }

    public List<Contact> searchByFirstAndLastName(String firstPart, String lastPart) {
        return contactDAO.searchByFirstAndLastName(firstPart, lastPart);
    }

    public void printContactsList(List<Contact> contacts) {
        if (contacts == null || contacts.isEmpty()) {
            System.out.println("No contacts found.");
            return;
        }
        System.out.println("\nCONTACTS LIST");
        System.out.printf("%-5s %-15s %-15s %-15s %-40s %-40s", "ID", "FIRST NAME", "LAST NAME", "PHONE", "EMAIL", "LINKEDIN URL");
        System.out.println("\n---------------------------------------------------------------------------------------------------------------");
        
        for (Contact contact : contacts) {
            int id = contact.getContactId();
            String firstName = contact.getFirstName() != null ? contact.getFirstName() : "-";
            String lastName = contact.getLastName() != null ? contact.getLastName() : "-";
            String phone = contact.getPhoneNumber() != null ? contact.getPhoneNumber() : "-";
            String email = contact.getEmail() != null ? contact.getEmail() : "-";
            String url = contact.getLinkedinUrl() != null ? contact.getLinkedinUrl() : "-";

            System.out.printf("%-5d %-15s %-15s %-15s %-40s %-40s%n", id, firstName, lastName, phone, email, url);

        }
        System.out.println("\n Total " + contacts.size() + " contact(s) found.");
    }

    public void displayAllContacts() {
        List<Contact> contacts = getAllContacts();
        printContactsList(contacts);
    }

    /**
     * Interactive search flow:
     * Shows all search options (single-field and multi-field) in one menu.
     */
    public void searchContactsInteractive(Scanner scanner) {
        System.out.println("\n=== Search Contacts ===");
        System.out.println("Single-Field Search:");
        System.out.println("1 - Search by first name");
        System.out.println("2 - Search by last name");
        System.out.println("3 - Search by phone number");
        System.out.println();
        System.out.println("Multi-Field Search:");
        System.out.println("4 - First Name + Birth Month");
        System.out.println("5 - Phone Number + Email");
        System.out.println("6 - First Name + Last Name");
        System.out.println();
        System.out.println("0 - Cancel");

        int choice = InputHelper.readIntInRange(scanner, "Choice: ", 0, 6);

        List<Contact> results;

        switch (choice) {
            case 0:
                System.out.println("Search cancelled.");
                return;
            case 1: {
                String first = InputHelper.readNonEmptyLine(scanner, "First name contains: ");
                results = searchByFirstName(first);
                break;
            }
            case 2: {
                String last = InputHelper.readNonEmptyLine(scanner, "Last name contains: ");
                results = searchByLastName(last);
                break;
            }
            case 3: {
                String digits = InputHelper.readNonEmptyLine(scanner, "Phone number contains digits: ");
                results = searchByPhoneContains(digits);
                break;
            }
            case 4: {
                String firstName = InputHelper.readNonEmptyLine(scanner, "First name contains: ");
                int month = InputHelper.readIntInRange(scanner, "Birth month (1-12): ", 1, 12);
                results = searchByFirstNameAndBirthMonth(firstName, month);
                break;
            }
            case 5: {
                String phonePart = InputHelper.readNonEmptyLine(scanner, "Phone number contains: ");
                String emailPart = InputHelper.readNonEmptyLine(scanner, "Email contains: ");
                results = searchByPhoneAndEmailContains(phonePart, emailPart);
                break;
            }
            case 6: {
                String firstPart = InputHelper.readNonEmptyLine(scanner, "First name contains: ");
                String lastPart = InputHelper.readNonEmptyLine(scanner, "Last name contains: ");
                results = searchByFirstAndLastName(firstPart, lastPart);
                break;
            }
            default:
                System.out.println("Invalid choice.");
                return;
        }

        printContactsList(results);
    }

    /**
     * Interactive sort flow:
     * lets the user choose field + order and then calls getAllSorted + printContactsList.
     */
    public void sortContactsInteractive(Scanner scanner) {
        System.out.println("\n=== Sort Contacts ===");
        System.out.println("1 - Sort by first name");
        System.out.println("2 - Sort by last name");
        System.out.println("3 - Sort by phone number");
        System.out.println("0 - Cancel");

        int field = InputHelper.readIntInRange(scanner, "Field: ", 0, 3);
        if (field == 0) {
            System.out.println("Sort cancelled.");
            return;
        }

        System.out.println("1 - Ascending");
        System.out.println("2 - Descending");
        int order = InputHelper.readIntInRange(scanner, "Order: ", 1, 2);

        String sortField;
        switch (field) {
            case 1:
                sortField = "first_name";
                break;
            case 2:
                sortField = "last_name";
                break;
            case 3:
                sortField = "phone_number";
                break;
            default:
                System.out.println("Invalid field.");
                return;
        }

        boolean ascending = (order == 1);

        List<Contact> contacts = getAllSorted(sortField, ascending);
        printContactsList(contacts);
    }

    public void updateContactInteractive(Scanner scanner) {
    System.out.println("[TODO] Update existing contact (Junior/Senior).");
    }

    public void addContactInteractive(Scanner scanner) {
        System.out.println("[TODO] Add new contact(s) (Senior).");
    }

    public void deleteContactInteractive(Scanner scanner) {
        System.out.println("[TODO] Delete existing contact(s) (Senior).");
    }

}
