package service;

import dao.ContactDAO;
import model.Contact;
import util.InputHelper;
import util.ConsoleColors;

import java.util.List;
import java.util.Scanner;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import undo.UndoManager;
import undo.UndoAction;

public class ContactService {

    private final ContactDAO contactDAO = new ContactDAO();
    private final UndoManager undoManager;

    public ContactService(UndoManager undoManager) {
        this.undoManager = undoManager;
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

    // use DAO's searchByPhoneContains
    public List<Contact> searchByPhoneNumber(String query) {
        return contactDAO.searchByPhoneContains(query);
    }

    public List<Contact> searchByFirstNameAndBirthMonth(String firstName, int month) {
        return contactDAO.searchByFirstNameAndBirthMonth(firstName, month);
    }

    // new DAO helper (added below in ContactDAO)
    public List<Contact> searchByPhonePrefixAndBirthYear(String phonePrefix, int year) {
        return contactDAO.searchByPhonePrefixAndBirthYear(phonePrefix, year);
    }

    public List<Contact> searchByFirstAndLastName(String firstNamePart, String lastNamePart) {
        return contactDAO.searchByFirstAndLastName(firstNamePart, lastNamePart);
    }

    // ===================== PRINT HELPER =====================
    public void printContactsList(List<Contact> contacts) {
        if (contacts == null || contacts.isEmpty()) {
            System.out.println(ConsoleColors.YELLOW + "\nNo contacts found." + ConsoleColors.RESET);
            return;
        }

        System.out.println(ConsoleColors.CYAN + "\nContacts:" + ConsoleColors.RESET);
        System.out.printf(
            ConsoleColors.BLUE + "%-5s %-15s %-15s %-15s %-40s %-40s%n" + ConsoleColors.RESET,
            "ID", "First Name", "Last Name", "Phone", "Email", "LinkedIn URL"
        );
        System.out.println("-------------------------------------------------------------------------------------------");

        for (Contact contact : contacts) {
            int id = contact.getContactId();
            String firstName = contact.getFirstName() != null ? contact.getFirstName() : "-";
            String lastName = contact.getLastName() != null ? contact.getLastName() : "-";
            String phone = contact.getPhoneNumber() != null ? contact.getPhoneNumber() : "-";
            String email = contact.getEmail() != null ? contact.getEmail() : "-";
            String url = contact.getLinkedinUrl() != null ? contact.getLinkedinUrl() : "-";

            System.out.printf(
                ConsoleColors.WHITE + "%-5d %-15s %-15s %-15s %-40s %-40s%n" + ConsoleColors.RESET,
                id, firstName, lastName, phone, email, url
            );
        }
        System.out.println(ConsoleColors.GREEN + "\n Total " + contacts.size() + " contact(s) found." + ConsoleColors.RESET);
    }

    public void displayAllContacts() {
        List<Contact> contacts = getAllContacts();
        printContactsList(contacts);
    }

    // ===================== SEARCH =====================
    public void searchContactsInteractive(Scanner scanner) {
        System.out.println("\n=== Search Contacts ===");
        System.out.println("1 - Search by first name");
        System.out.println("2 - Search by last name");
        System.out.println("3 - Search by phone number");
        System.out.println("4 - First Name + Birth Month");
        System.out.println("5 - Phone Number Prefix + Birth Year");
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
                String phone = InputHelper.readLine(scanner, "Phone number contains: ");
                results = searchByPhoneNumber(phone);
                break;
            }
            case 4: {
                String first = InputHelper.readValidName(scanner, "First name: ");
                int month = InputHelper.readIntInRange(scanner, "Birth month (1-12): ", 1, 12);
                results = searchByFirstNameAndBirthMonth(first, month);
                break;
            }
            case 5: {
                String prefix = InputHelper.readLine(scanner, "Phone number prefix: ");
                int year = InputHelper.readIntInRange(scanner, "Birth year (e.g., 1990): ", 1900, 2100);
                results = searchByPhonePrefixAndBirthYear(prefix, year);
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

    // ===================== SORT =====================
    public void sortContactsInteractive(Scanner scanner) {
        System.out.println("\n=== Sort Contacts ===");
        System.out.println("1 - Sort by first name");
        System.out.println("2 - Sort by last name");
        System.out.println("3 - Sort by phone number");
        System.out.println("4 - Sort by email");
        System.out.println("5 - Sort by birth date");
        System.out.println("0 - Cancel");

        int choice = InputHelper.readIntInRange(scanner, "Choice: ", 0, 5);
        if (choice == 0) {
            System.out.println("Sorting cancelled.");
            return;
        }

        String sortField;
        switch (choice) {
            case 1:
                sortField = "first_name";
                break;
            case 2:
                sortField = "last_name";
                break;
            case 3:
                sortField = "phone_number";
                break;
            case 4:
                sortField = "email";
                break;
            case 5:
                sortField = "birth_date";
                break;
            default:
                System.out.println("Invalid choice.");
                return;
        }

        System.out.println("\nOrder:");
        System.out.println("1 - Ascending");
        System.out.println("2 - Descending");

        int order = InputHelper.readIntInRange(scanner, "Choice: ", 1, 2);
        boolean ascending = (order == 1);
        List<Contact> contacts = getAllSorted(sortField, ascending);
        printContactsList(contacts);
    }

    // ===================== UPDATE =====================
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

        // Take a snapshot BEFORE modification for undo
        Contact previousState = UndoAction.cloneContact(existing);

        System.out.println("\nContact to update:");
        System.out.printf("ID: %d | Name: %s %s | Phone: %s | Email: %s | LinkedIn: %s%n",
                existing.getContactId(),
                existing.getFirstName(),
                existing.getLastName(),
                existing.getPhoneNumber(),
                existing.getEmail(),
                existing.getLinkedinUrl());

        System.out.println("\nLeave input empty to keep the current value.");

        // Optional, validated first name
        while (true) {
            String first = InputHelper.readLine(scanner,
                "First name [" + (existing.getFirstName() == null ? "" : existing.getFirstName()) + "]: ");
            if (first.isEmpty()) break; // keep current
            if (first.matches("[a-zA-ZçÇğĞıİöÖşŞüÜ\\s]+")) {
                existing.setFirstName(first);
                break;
            } else {
                System.out.println(ConsoleColors.RED + "Invalid input. Please use only letters and spaces." + ConsoleColors.RESET);
            }
        }

        // Optional, validated last name
        while (true) {
            String last = InputHelper.readLine(scanner,
                "Last name [" + (existing.getLastName() == null ? "" : existing.getLastName()) + "]: ");
            if (last.isEmpty()) break; // keep current
            if (last.matches("[a-zA-ZçÇğĞıİöÖşŞüÜ\\s]+")) {
                existing.setLastName(last);
                break;
            } else {
                System.out.println(ConsoleColors.RED + "Invalid input. Please use only letters and spaces." + ConsoleColors.RESET);
            }
        }

        String nick = InputHelper.readLine(scanner,
            "Nickname [" + (existing.getNickname() == null ? "" : existing.getNickname()) + "]: ");
        if (!nick.isEmpty()) existing.setNickname(nick);

        String phoneNew = InputHelper.readLine(scanner,
            "Phone number [" + (existing.getPhoneNumber() == null ? "" : existing.getPhoneNumber()) + "]: ");
        if (!phoneNew.isEmpty()) existing.setPhoneNumber(phoneNew);

        String emailNew = InputHelper.readLine(scanner,
            "Email [" + (existing.getEmail() == null ? "" : existing.getEmail()) + "]: ");
        if (!emailNew.isEmpty()) existing.setEmail(emailNew);

        String linkedin = InputHelper.readLine(scanner,
            "LinkedIn URL [" + (existing.getLinkedinUrl() == null ? "" : existing.getLinkedinUrl()) + "]: ");
        if (!linkedin.isEmpty()) existing.setLinkedinUrl(linkedin);

        String currentBirth = existing.getBirthDate() == null ? "" : existing.getBirthDate().toString();
        while (true) {
            String bd = InputHelper.readLine(scanner,
                "Birth date (YYYY-MM-DD) [" + currentBirth + "] (empty to keep, 'skip' to proceed): ");
            if (bd.isEmpty() || bd.equalsIgnoreCase("skip")) break;
            try {
                LocalDate d = LocalDate.parse(bd);
                existing.setBirthDate(d);
                break;
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Use YYYY-MM-DD or enter 'skip'.");
            }
        }

        boolean ok = contactDAO.updateContact(existing);
        if (ok) {
            System.out.println("\n✓ Contact updated successfully.");
            if (undoManager != null) {
                undoManager.push(UndoAction.forContactUpdate(contactDAO, previousState));
            }
        } else {
            System.out.println("\n✗ Failed to update contact.");
        }
    }

    // ===================== ADD =====================
    public void addContactInteractive(Scanner scanner) {
        System.out.println("\n=== Add New Contact ===");

        boolean adding = true;
        while (adding) {
            String first = InputHelper.readValidName(scanner, "First name: ");

            String last = InputHelper.readValidName(scanner, "Last name: ");

            String nick = InputHelper.readLine(scanner, "Nickname (optional): ");

            String phone;
            while (true) {
                phone = InputHelper.readLine(scanner, "Phone number (required): ");
                if (phone.isEmpty()) {
                    System.out.println("Phone number is required.");
                    continue;
                }
                break;
            }

            String email = "";
            while (true) {
                email = InputHelper.readLine(scanner, "Email (optional, or 'skip'): ");
                if (email.equalsIgnoreCase("skip")) {
                    email = "";
                    break;
                }
                if (!email.isEmpty() && !email.contains("@")) {
                    System.out.println("Invalid email format. Please include '@' or enter 'skip'.");
                } else {
                    break;
                }
            }

            String linkedin = "";
            while (true) {
                linkedin = InputHelper.readLine(scanner, "LinkedIn URL (optional, or 'skip'): ");
                if (linkedin.equalsIgnoreCase("skip")) {
                    linkedin = "";
                    break;
                }
                break;
            }

            LocalDate birthDate = null;
            while (true) {
                String bd = InputHelper.readLine(scanner, "Birth date (YYYY-MM-DD, or 'skip'): ");
                if (bd.isEmpty() || bd.equalsIgnoreCase("skip")) break;
                try {
                    birthDate = LocalDate.parse(bd);
                    break;
                } catch (DateTimeParseException e) {
                    System.out.println("Invalid date format. Use YYYY-MM-DD or enter 'skip'.");
                }
            }

            // Display preview
            System.out.println("\nContact preview:");
            System.out.printf("  First Name: %s%n", first);
            System.out.printf("  Last Name: %s%n", last);
            System.out.printf("  Nickname: %s%n", nick.isEmpty() ? "-" : nick);
            System.out.printf("  Phone: %s%n", phone);
            System.out.printf("  Email: %s%n", email.isEmpty() ? "-" : email);
            System.out.printf("  LinkedIn: %s%n", linkedin.isEmpty() ? "-" : linkedin);
            System.out.printf("  Birth Date: %s%n", birthDate == null ? "-" : birthDate.toString());

            String confirm = InputHelper.readLine(scanner, "\nConfirm adding this contact? (y/N): ");
            if (confirm.trim().equalsIgnoreCase("y")) {
                Contact c = new Contact();
                c.setFirstName(first);
                c.setLastName(last);
                c.setNickname(nick.isEmpty() ? null : nick);
                c.setPhoneNumber(phone);
                c.setEmail(email.isEmpty() ? null : email);
                c.setLinkedinUrl(linkedin.isEmpty() ? null : linkedin);
                c.setBirthDate(birthDate);

                boolean ok = contactDAO.insertContact(c);
                if (ok) {
                    System.out.println("\n✓ Contact added successfully.");
                    // Register undo: delete the newly inserted contact
                    if (undoManager != null && c.getContactId() != 0) {
                        undoManager.push(UndoAction.forContactInsert(contactDAO, c.getContactId()));
                    }
                    adding = false;
                } else {
                    System.out.println("\n✗ Failed to add contact. Try again? (y/N): ");
                    String retry = InputHelper.readLine(scanner, "");
                    if (!retry.trim().equalsIgnoreCase("y")) {
                        adding = false;
                    }
                }
            } else {
                String continueAdding = InputHelper.readLine(scanner, "Edit again? (y/N): ");
                if (!continueAdding.trim().equalsIgnoreCase("y")) {
                    System.out.println("Add contact cancelled.");
                    adding = false;
                }
            }
        }
    }

    // ===================== DELETE =====================
    public void deleteContactInteractive(Scanner scanner) {
        System.out.println("\n=== Delete Contact ===");
        int id = InputHelper.readIntInRange(scanner, "Contact ID to delete (0 = cancel): ", 0, Integer.MAX_VALUE);
        if (id == 0) {
            System.out.println("Delete cancelled.");
            return;
        }

        Contact existing = contactDAO.getContactById(id);
        if (existing == null) {
            System.out.println("Contact not found with ID: " + id);
            return;
        }

        // Snapshot BEFORE delete for undo
        Contact contactSnapshot = UndoAction.cloneContact(existing);

        // Display contact in table format
        System.out.println("\nContact to delete:");
        System.out.printf("ID: %d | Name: %s %s | Phone: %s | Email: %s | LinkedIn: %s%n",
                existing.getContactId(),
                existing.getFirstName(),
                existing.getLastName(),
                existing.getPhoneNumber(),
                existing.getEmail(),
                existing.getLinkedinUrl());

        // Double confirmation before delete
        String confirm1 = InputHelper.readLine(scanner, "\nAre you sure you want to delete this contact? (y/N): ");
        if (!confirm1.trim().equalsIgnoreCase("y")) {
            System.out.println("Delete cancelled.");
            return;
        }

        String confirm2 = InputHelper.readLine(scanner, "This action cannot be undone. Type 'DELETE' to confirm: ");
        if (!confirm2.trim().equals("DELETE")) {
            System.out.println("Delete cancelled.");
            return;
        }

        boolean ok = contactDAO.deleteContact(id);
        if (ok) {
            System.out.println("\n✓ Contact deleted successfully.");
            if (undoManager != null && contactSnapshot != null) {
                undoManager.push(UndoAction.forContactDelete(contactDAO, contactSnapshot));
            }
        } else {
            System.out.println("\n✗ Failed to delete contact.");
        }
    }

}