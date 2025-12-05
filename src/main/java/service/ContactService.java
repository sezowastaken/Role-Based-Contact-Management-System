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
        System.out.printf("%-5s %-15s %-15s %-15s %-40s %-40s",
                "ID", "FIRST NAME", "LAST NAME", "PHONE", "EMAIL", "LINKEDIN URL");
        System.out.println("\n---------------------------------------------------------------------------------------------------------------");

        for (Contact contact : contacts) {
            int id = contact.getContactId();
            String firstName = contact.getFirstName() != null ? contact.getFirstName() : "-";
            String lastName = contact.getLastName() != null ? contact.getLastName() : "-";
            String phone = contact.getPhoneNumber() != null ? contact.getPhoneNumber() : "-";
            String email = contact.getEmail() != null ? contact.getEmail() : "-";
            String url = contact.getLinkedinUrl() != null ? contact.getLinkedinUrl() : "-";

            System.out.printf("%-5d %-15s %-15s %-15s %-40s %-40s%n",
                    id, firstName, lastName, phone, email, url);
        }
        System.out.println("\nTotal " + contacts.size() + " contact(s) found.");
    }

    public void displayAllContacts() {
        List<Contact> contacts = getAllContacts();
        printContactsList(contacts);
    }

    /**
     * Interactive search flow
     */
    public void searchContactsInteractive(Scanner scanner) {
        System.out.println("\n=== Search Contacts ===");
        System.out.println("1 - Search by first name");
        System.out.println("2 - Search by last name");
        System.out.println("3 - Search by phone number");
        System.out.println("4 - First Name + Birth Month");
        System.out.println("5 - Phone Number + Email");
        System.out.println("6 - First Name + Last Name");
        System.out.println("0 - Cancel");

        int choice = InputHelper.readIntInRange(scanner, "Choice: ", 0, 6);
        List<Contact> results;

        switch (choice) {
            case 0:
                System.out.println("Search cancelled.");
                return;
            case 1: {
                String first = InputHelper.readValidName(scanner, "First name contains: ");
                results = searchByFirstName(first);
                break;
            }
            case 2: {
                String last = InputHelper.readValidName(scanner, "Last name contains: ");
                results = searchByLastName(last);
                break;
            }
            case 3: {
                String digits = InputHelper.readNonEmptyLine(scanner, "Phone number contains digits: ");
                results = searchByPhoneContains(digits);
                break;
            }
            case 4: {
                String firstName = InputHelper.readValidName(scanner, "First name contains: ");
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
                String firstPart = InputHelper.readValidName(scanner, "First name contains: ");
                String lastPart = InputHelper.readValidName(scanner, "Last name contains: ");
                results = searchByFirstAndLastName(firstPart, lastPart);
                break;
            }
            default:
                System.out.println("Invalid choice.");
                return;
        }

        printContactsList(results);
    }

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
            case 1: sortField = "first_name"; break;
            case 2: sortField = "last_name"; break;
            case 3: sortField = "phone_number"; break;
            default:
                System.out.println("Invalid field.");
                return;
        }

        boolean ascending = (order == 1);
        List<Contact> contacts = getAllSorted(sortField, ascending);
        printContactsList(contacts);
    }

    public void updateContactInteractive(Scanner scanner) {
        System.out.println("\n=== Update Contact ===");
        int id = InputHelper.readIntInRange(scanner, "Contact ID to update (0 = cancel): ", 0, Integer.MAX_VALUE);

        if (id == 0) {
            System.out.println("Update cancelled.");
            return;
        }

        Contact existing = contactDAO.getContactById(id);
        if (existing == null) {
            System.out.println("Contact not found with ID: " + id);
            return;
        }

        System.out.println("\nContact to update:");
        System.out.printf("%-5s %-15s %-15s %-15s %-40s %-40s%n",
                "ID", "FIRST NAME", "LAST NAME", "PHONE", "EMAIL", "LINKEDIN URL");
        System.out.println("---------------------------------------------------------------------------------------------------------------");
        System.out.printf("%-5d %-15s %-15s %-15s %-40s %-40s%n",
                existing.getContactId(),
                existing.getFirstName(),
                existing.getLastName(),
                existing.getPhoneNumber(),
                existing.getEmail(),
                existing.getLinkedinUrl()
        );

        String confirm = InputHelper.readLine(scanner, "\nDo you want to update this contact? (y/N): ");
        if (!confirm.trim().equalsIgnoreCase("y")) {
            System.out.println("Update cancelled.");
            return;
        }

        System.out.println("\nPress Enter to keep current value.");

        String first = InputHelper.readLine(scanner, "First name [" + existing.getFirstName() + "]: ");
        if (!first.isEmpty()) existing.setFirstName(first);

        String last = InputHelper.readLine(scanner, "Last name [" + existing.getLastName() + "]: ");
        if (!last.isEmpty()) existing.setLastName(last);

        String phone = InputHelper.readLine(scanner, "Phone [" + existing.getPhoneNumber() + "]: ");
        if (!phone.isEmpty()) existing.setPhoneNumber(phone);

        String email = InputHelper.readLine(scanner, "Email [" + existing.getEmail() + "]: ");
        if (!email.isEmpty()) existing.setEmail(email);

        String url = InputHelper.readLine(scanner, "LinkedIn URL [" + existing.getLinkedinUrl() + "]: ");
        if (!url.isEmpty()) existing.setLinkedinUrl(url);

        contactDAO.updateContact(existing);

        System.out.println("Contact updated successfully.");
    }
}
