package service;

import dao.ContactDAO;
import model.Contact;
import util.InputHelper;
import java.util.List;
import java.util.Scanner;
import util.ConsoleColors;

import javax.management.Query;

import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

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
                System.out.println("Default charset = " + Charset.defaultCharset());
                System.out.println("Query = " + first);
                System.out.println("Length = " + first.length());
                for (int i = 0; i < first.length(); i++) {
                    System.out.println(i + " -> " + (int) first.charAt(i));
                }
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

        // Display contact in table format
        System.out.println("\nContact to update:");
        System.out.printf("%-5s %-15s %-15s %-15s %-40s %-40s%n", "ID", "FIRST NAME", "LAST NAME", "PHONE", "EMAIL",
                "LINKEDIN URL");
        System.out.println(
                "---------------------------------------------------------------------------------------------------------------");
        String firstName = existing.getFirstName() != null ? existing.getFirstName() : "-";
        String lastName = existing.getLastName() != null ? existing.getLastName() : "-";
        String phone = existing.getPhoneNumber() != null ? existing.getPhoneNumber() : "-";
        String email = existing.getEmail() != null ? existing.getEmail() : "-";
        String url = existing.getLinkedinUrl() != null ? existing.getLinkedinUrl() : "-";
        System.out.printf("%-5d %-15s %-15s %-15s %-40s %-40s%n", existing.getContactId(), firstName, lastName, phone,
                email, url);

        // Confirmation before update
        String confirm = InputHelper.readLine(scanner, "\nDo you want to update this contact? (y/N): ");
        if (!confirm.trim().equalsIgnoreCase("y")) {
            System.out.println("Update cancelled.");
            return;
        }

        System.out.println("\nPress Enter to keep current value. Enter 'skip' to skip date field.");

        String first = InputHelper.readLine(scanner,
                "First name [" + (existing.getFirstName() == null ? "" : existing.getFirstName()) + "]: ");
        if (!first.isEmpty())
            existing.setFirstName(first);

        String last = InputHelper.readLine(scanner,
                "Last name [" + (existing.getLastName() == null ? "" : existing.getLastName()) + "]: ");
        if (!last.isEmpty())
            existing.setLastName(last);

        String nick = InputHelper.readLine(scanner,
                "Nickname [" + (existing.getNickname() == null ? "" : existing.getNickname()) + "]: ");
        if (!nick.isEmpty())
            existing.setNickname(nick);

        String phoneNew = InputHelper.readLine(scanner,
                "Phone number [" + (existing.getPhoneNumber() == null ? "" : existing.getPhoneNumber()) + "]: ");
        if (!phoneNew.isEmpty())
            existing.setPhoneNumber(phoneNew);

        String emailNew = InputHelper.readLine(scanner,
                "Email [" + (existing.getEmail() == null ? "" : existing.getEmail()) + "]: ");
        if (!emailNew.isEmpty())
            existing.setEmail(emailNew);

        String linkedin = InputHelper.readLine(scanner,
                "LinkedIn URL [" + (existing.getLinkedinUrl() == null ? "" : existing.getLinkedinUrl()) + "]: ");
        if (!linkedin.isEmpty())
            existing.setLinkedinUrl(linkedin);

        String currentBirth = existing.getBirthDate() == null ? "" : existing.getBirthDate().toString();
        while (true) {
            String bd = InputHelper.readLine(scanner,
                    "Birth date (YYYY-MM-DD) [" + currentBirth + "] (empty to keep, 'skip' to proceed): ");
            if (bd.isEmpty() || bd.equalsIgnoreCase("skip"))
                break;
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
        } else {
            System.out.println("\n✗ Failed to update contact.");
        }
    }

    public void addContactInteractive(Scanner scanner) {
        System.out.println("\n=== Add New Contact ===");

        boolean adding = true;
        while (adding) {
            String first = InputHelper.readNonEmptyLine(scanner, "First name: ");
            String last = InputHelper.readNonEmptyLine(scanner, "Last name: ");

            String nick = "";
            while (true) {
                nick = InputHelper.readLine(scanner, "Nickname (optional, or 'skip'): ");
                if (nick.equalsIgnoreCase("skip")) {
                    nick = "";
                    break;
                }
                break;
            }

            String phone = InputHelper.readNonEmptyLine(scanner, "Phone number: ");

            String email = "";
            while (true) {
                email = InputHelper.readLine(scanner, "Email (optional, or 'skip'): ");
                if (email.equalsIgnoreCase("skip")) {
                    email = "";
                    break;
                }
                break;
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
                if (bd.isEmpty() || bd.equalsIgnoreCase("skip"))
                    break;
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

        // Display contact in table format
        System.out.println("\nContact to delete:");
        System.out.printf("%-5s %-15s %-15s %-15s %-40s %-40s%n", "ID", "FIRST NAME", "LAST NAME", "PHONE", "EMAIL",
                "LINKEDIN URL");
        System.out.println(
                "---------------------------------------------------------------------------------------------------------------");
        String firstName = existing.getFirstName() != null ? existing.getFirstName() : "-";
        String lastName = existing.getLastName() != null ? existing.getLastName() : "-";
        String phone = existing.getPhoneNumber() != null ? existing.getPhoneNumber() : "-";
        String email = existing.getEmail() != null ? existing.getEmail() : "-";
        String url = existing.getLinkedinUrl() != null ? existing.getLinkedinUrl() : "-";
        System.out.printf("%-5d %-15s %-15s %-15s %-40s %-40s%n", existing.getContactId(), firstName, lastName, phone,
                email, url);

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
        } else {
            System.out.println("\n✗ Failed to delete contact.");
        }
    }

}
