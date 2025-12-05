package service;

import dao.ContactDAO;
import model.Contact;
import util.InputHelper;
import java.util.List;
import java.util.Scanner;
import util.ConsoleColors;

import javax.management.Query;

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

    public void printContactsList(List<Contact> contacts) {
        if (contacts == null || contacts.isEmpty()) {
            System.out.println(ConsoleColors.RED + "No contacts found." + ConsoleColors.RESET);
            return;
        }
        System.out.println(ConsoleColors.BLUE + "\nCONTACTS LIST" + ConsoleColors.RESET);
        System.out.printf(ConsoleColors.CYAN + "%-5s %-15s %-15s %-15s %-40s %-40s", "ID", "FIRST NAME", "LAST NAME",
                "PHONE", "EMAIL",
                "LINKEDIN URL");
        System.out.println(
                "\n---------------------------------------------------------------------------------------------------------------"
                        + ConsoleColors.RESET);

        for (Contact contact : contacts) {
            int id = contact.getContactId();
            String firstName = contact.getFirstName() != null ? contact.getFirstName() : "-";
            String lastName = contact.getLastName() != null ? contact.getLastName() : "-";
            String phone = contact.getPhoneNumber() != null ? contact.getPhoneNumber() : "-";
            String email = contact.getEmail() != null ? contact.getEmail() : "-";
            String url = contact.getLinkedinUrl() != null ? contact.getLinkedinUrl() : "-";

            System.out.printf(ConsoleColors.WHITE + "%-5d %-15s %-15s %-15s %-40s %-40s%n", id, firstName, lastName,
                    phone, email, url + ConsoleColors.RESET);

        }
        System.out.println(
                ConsoleColors.GREEN + "\n Total " + contacts.size() + " contact(s) found." + ConsoleColors.RESET);
    }

    public void displayAllContacts() {
        ContactService contactService = new ContactService();
        List<Contact> contacts = contactService.getAllContacts();
        contactService.printContactsList(contacts);
    }

    /**
     * Interactive search flow:
     * lets the user choose which field to search and then delegates to the
     * existing search methods (searchByFirstName, searchByLastName,
     * searchByPhoneContains).
     */
    public void searchContactsInteractive(Scanner scanner) {
        System.out.println("\n=== Search Contacts ===");
        System.out.println("1 - Search by first name");
        System.out.println("2 - Search by last name");
        System.out.println("3 - Search by phone number");
        System.out.println("0 - Cancel");

        int choice = InputHelper.readIntInRange(scanner, "Choice: ", 0, 3);

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
            default:
                System.out.println("Invalid choice.");
                return;
        }

        printContactsList(results);
    }

    /**
     * Interactive sort flow:
     * lets the user choose field + order and then calls getAllSorted +
     * printContactsList.
     */
    public void sortContactsInteractive(Scanner scanner) {
        System.out.println(ConsoleColors.WHITE + "\n=== Sort Contacts ===");
        System.out.println("1 - Sort by first name");
        System.out.println("2 - Sort by last name");
        System.out.println("3 - Sort by phone number");
        System.out.println("0 - Cancel" + ConsoleColors.RESET);

        int field = InputHelper.readIntInRange(scanner, "Field: ", 0, 3);
        if (field == 0) {
            System.out.println(ConsoleColors.RED + "Sort cancelled." + ConsoleColors.RESET);
            return;
        }

        System.out.println(ConsoleColors.YELLOW + "1 - Ascending");
        System.out.println("2 - Descending" + ConsoleColors.RESET);
        int order = InputHelper.readIntInRange(
                scanner,
                ConsoleColors.BLUE + "Order: " + ConsoleColors.RESET,
                1,
                2);

        String sortField;
        switch (field) {
            case 1:
                sortField = ConsoleColors.WHITE + "first_name";
                break;
            case 2:
                sortField = "last_name";
                break;
            case 3:
                sortField = "phone_number" + ConsoleColors.RESET;
                break;
            default:
                System.out.println(ConsoleColors.RED + "Invalid field." + ConsoleColors.RESET);
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
