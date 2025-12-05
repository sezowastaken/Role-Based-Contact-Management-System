package service;

import dao.ContactDAO;
import model.Contact;
import util.InputHelper;
import util.ConsoleColors;
import util.DateUtil;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.time.format.DateTimeFormatter; // Eklendi
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
            System.out.println(ConsoleColors.RED + "No contacts found." + ConsoleColors.RESET);
            return;
        }
        System.out.println(ConsoleColors.BLUE + "\nCONTACTS LIST" + ConsoleColors.RESET);
        System.out.printf(ConsoleColors.CYAN + "%-5s %-15s %-15s %-15s %-40s %-40s", "ID", "FIRST NAME", "LAST NAME",
                "PHONE", "EMAIL",
                "LINKEDIN URL" + ConsoleColors.RESET);
        System.out.println(ConsoleColors.WHITE +
                "\n-------------------------------------------------------------------------------------------------------------------------------"
                + ConsoleColors.RESET);

        for (Contact contact : contacts) {
            int id = contact.getContactId();
            String firstName = contact.getFirstName() != null ? contact.getFirstName() : "-";
            String lastName = contact.getLastName() != null ? contact.getLastName() : "-";
            String phone = contact.getPhoneNumber() != null ? contact.getPhoneNumber() : "-";
            String email = contact.getEmail() != null ? contact.getEmail() : "-";
            String url = contact.getLinkedinUrl() != null ? contact.getLinkedinUrl() : "-";

            System.out.printf(ConsoleColors.CYAN + "%-5d %-15s %-15s %-15s %-40s %-40s%n", id, firstName, lastName,
                    phone, email, url + ConsoleColors.RESET);

        }
        System.out.println(
                ConsoleColors.GREEN + "\n Total " + contacts.size() + " contact(s) found." + ConsoleColors.RESET);
    }

    public void displayAllContacts() {
        List<Contact> contacts = getAllContacts();
        printContactsList(contacts);
    }

    // ===================== SEARCH =====================
    public void searchContactsInteractive(Scanner scanner) {
        System.out.println(ConsoleColors.CYAN + "\n=== Search Contacts ===");
        System.out.println("Single-Field Search:");
        System.out.println("1 - Search by first name");
        System.out.println("2 - Search by last name");
        System.out.println("3 - Search by phone number");
        System.out.println("4 - First Name + Birth Month");
        System.out.println("5 - Phone Number + Email");
        System.out.println("6 - First Name + Last Name");
        System.out.println();
        System.out.println("0 - Cancel" + ConsoleColors.RESET);

        int choice = InputHelper.readIntInRange(
                scanner,
                ConsoleColors.YELLOW + "Choice: " + ConsoleColors.RESET,
                0,
                6);

        List<Contact> results;

        switch (choice) {
            case 0:
                System.out.println(ConsoleColors.RED + "Search cancelled." + ConsoleColors.RESET);
                return;
            case 1: {
                String first = InputHelper.readValidName(scanner, ConsoleColors.CYAN + "First name contains: ");
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
                String lastPart = InputHelper.readValidName(scanner, "Last name contains: " + ConsoleColors.RESET);
                results = searchByFirstAndLastName(firstPart, lastPart);
                break;
            }
            default:
                System.out.println(ConsoleColors.RED + "Invalid choice." + ConsoleColors.RESET);
                return;
        }

        printContactsList(results);
    }

    // ===================== SORT =====================
    public void sortContactsInteractive(Scanner scanner) {
        System.out.println(ConsoleColors.CYAN + "\n=== Sort Contacts ===");
        System.out.println("1 - Sort by first name");
        System.out.println("2 - Sort by last name");
        System.out.println("3 - Sort by phone number");
        System.out.println("0 - Cancel");

        int field = InputHelper.readIntInRange(scanner, "Field: ", 0, 3);
        if (field == 0) {
            System.out.println(ConsoleColors.RED + "Sort cancelled." + ConsoleColors.RESET);
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
                System.out.println(ConsoleColors.RED + "Invalid field." + ConsoleColors.RESET);
                return;
        }

        boolean ascending = (order == 1);
        List<Contact> contacts = getAllSorted(sortField, ascending);
        printContactsList(contacts);
    }

    // ===================== UPDATE (DÜZELTİLMİŞ) =====================
    public void updateContactInteractive(Scanner scanner) {
        System.out.println(ConsoleColors.CYAN + "\n=== Update Contact ===");
        int id = InputHelper.readIntInRange(scanner, "Contact ID to update (0 = cancel): " + ConsoleColors.RESET, 0,
                Integer.MAX_VALUE);
        if (id == 0) {
            System.out.println(ConsoleColors.RED + "Update cancelled." + ConsoleColors.RESET);
            return;
        }

        Contact existing = contactDAO.getContactById(id);
        if (existing == null) {
            System.out.println(ConsoleColors.YELLOW + "Contact not found with ID: " + ConsoleColors.RESET + id);
            return;
        }

        // Display contact in table format
        System.out.println(ConsoleColors.BLUE + "\nContact to update:" + ConsoleColors.RESET);
        System.out.printf(ConsoleColors.CYAN + "%-5s %-15s %-15s %-15s %-40s %-40s%n", "ID", "FIRST NAME", "LAST NAME",
                "PHONE", "EMAIL",
                "LINKEDIN URL" + ConsoleColors.RESET);
        System.out.println(ConsoleColors.WHITE +
                "-------------------------------------------------------------------------------------------------------------------------------"
                + ConsoleColors.RESET);
        
        System.out.printf(ConsoleColors.CYAN + "%-5d %-15s %-15s %-15s %-40s %-40s%n", existing.getContactId(),
                existing.getFirstName(), existing.getLastName(), existing.getPhoneNumber(),
                existing.getEmail(), existing.getLinkedinUrl() + ConsoleColors.RESET);

        // Confirmation before update
        if (!InputHelper.readYesNo(scanner, ConsoleColors.BLUE + "\nDo you want to update this contact?" + ConsoleColors.RESET)) {
            System.out.println(ConsoleColors.RED + "Update cancelled." + ConsoleColors.RESET);
            return;
        }

        System.out.println(ConsoleColors.YELLOW
                + "\nEnter new values (Press Enter to keep current)." + ConsoleColors.RESET);

        // --- 1. İSİM ---
        while (true) {
            System.out.print(ConsoleColors.CYAN + "First name [" + existing.getFirstName() + "]: ");
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) break;
            
            if (input.matches("[\\p{L}çğıöşüÇĞİÖŞÜ '\\-]+")) {
                existing.setFirstName(input);
                break;
            } else {
                System.out.println(ConsoleColors.RED + "Error: Name must contain only letters." + ConsoleColors.RESET);
            }
        }

        // --- 2. SOYİSİM ---
        while (true) {
            System.out.print("Last name [" + existing.getLastName() + "]: ");
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) break;
            
            if (input.matches("[\\p{L}çğıöşüÇĞİÖŞÜ '\\-]+")) {
                existing.setLastName(input);
                break;
            } else {
                System.out.println(ConsoleColors.RED + "Error: Name must contain only letters." + ConsoleColors.RESET);
            }
        }

        // Nickname
        String nick = InputHelper.readLine(scanner,
                "Nickname [" + (existing.getNickname() == null ? "" : existing.getNickname()) + "]: ");
        if (!nick.isEmpty())
            existing.setNickname(nick);

        // --- 3. TELEFON (+90) ---
        while (true) {
            System.out.print("Phone [" + existing.getPhoneNumber() + "] (+90) ");
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) break;
            
            String clean = input.replaceAll("[^0-9]", "");
            if (clean.length() == 11 && clean.startsWith("0")) clean = clean.substring(1);
            
            if (clean.matches("^5[0-9]{9}$")) {
                existing.setPhoneNumber(clean);
                break;
            } else {
                System.out.println(ConsoleColors.RED + "Error: Invalid phone! Must be 5xxxxxxxxx." + ConsoleColors.RESET);
            }
        }

        // --- 4. EMAIL ---
        while (true) {
            System.out.print("Email [" + existing.getEmail() + "]: ");
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) break;
            
            if (input.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
                existing.setEmail(input);
                break;
            } else {
                System.out.println(ConsoleColors.RED + "Error: Invalid email format!" + ConsoleColors.RESET);
            }
        }

        // --- 5. LINKEDIN ---
        String currentLi = existing.getLinkedinUrl() == null ? "" : existing.getLinkedinUrl();
        String linkedin = InputHelper.readValidLinkedin(scanner, "LinkedIn [" + currentLi + "] (or 'skip')");
        if (linkedin != null) existing.setLinkedinUrl(linkedin);

        // --- 6. TARIH ---
        String currentBirth = existing.getBirthDate() == null ? "" : existing.getBirthDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        while (true) {
            System.out.print("Birth Date [" + currentBirth + "] (dd.MM.yyyy or 'skip'): ");
            String input = scanner.nextLine().trim();
            
            if (input.isEmpty() || input.equalsIgnoreCase("skip")) break;
            
            String error = DateUtil.checkDateValidity(input);
            if (error == null) {
                existing.setBirthDate(DateUtil.parse(input));
                break;
            } else {
                System.out.println(ConsoleColors.RED + error + ConsoleColors.RESET);
            }
        }

        boolean ok = contactDAO.updateContact(existing);
        if (ok) {
            System.out.println(ConsoleColors.GREEN + "\nContact updated successfully." + ConsoleColors.RESET);
        } else {
            System.out.println(ConsoleColors.RED + "\nFailed to update contact." + ConsoleColors.RESET);
        }
    }

    // ===================== ADD =====================
    public void addContactInteractive(Scanner scanner) {
        System.out.println(ConsoleColors.WHITE + "\n=== Add New Contact ===");

        boolean adding = true;
        while (adding) {
            String first = InputHelper.readValidName(scanner, "First name: ");
            String last = InputHelper.readValidName(scanner, "Last name: ");

            String nick = InputHelper.readLine(scanner, "Nickname (optional, or 'skip'): ");
            if (nick.equalsIgnoreCase("skip")) {
                nick = null;
            }

            String phone = InputHelper.readValidPhoneTR(scanner, "Phone number");
            String email = InputHelper.readValidEmail(scanner, "Email: ");

            // Akıllı LinkedIn
            String linkedin = InputHelper.readValidLinkedin(scanner, "LinkedIn URL (optional)");

            LocalDate birthDate = InputHelper.readValidPastDate(scanner, "Birth date");

            // Display preview
            System.out.println("\nContact preview:");
            System.out.printf("  First Name: %s%n", first);
            System.out.printf("  Last Name: %s%n", last);
            System.out.printf("  Nickname: %s%n", nick == null ? "-" : nick);
            System.out.printf("  Phone: %s%n", phone);
            System.out.printf("  Email: %s%n", email);
            System.out.printf("  LinkedIn: %s%n", linkedin == null ? "-" : linkedin);
            System.out.printf("  Birth Date: %s%n", birthDate);

            if (InputHelper.readYesNo(scanner, "\nConfirm adding this contact?")) {
                Contact c = new Contact();
                c.setFirstName(first);
                c.setLastName(last);
                c.setNickname(nick);
                c.setPhoneNumber(phone);
                c.setEmail(email);
                c.setLinkedinUrl(linkedin);
                c.setBirthDate(birthDate);

                boolean ok = contactDAO.insertContact(c);
                if (ok) {
                    System.out.println(ConsoleColors.GREEN + "\n✓ Contact added successfully." + ConsoleColors.RESET);
                    adding = false;
                } else {
                    System.out.println(ConsoleColors.RED + "\n✗ Failed to add contact." + ConsoleColors.RESET);
                    if (!InputHelper.readYesNo(scanner, "Try again?")) {
                        adding = false;
                    }
                }
            } else {
                if (!InputHelper.readYesNo(scanner, "Edit again?")) {
                    System.out.println("Add contact cancelled.");
                    adding = false;
                }
            }
        }
    }

    // ===================== DELETE =====================
    public void deleteContactInteractive(Scanner scanner) {
        System.out.println("\n=== Delete Contact ===");
        
        displayAllContacts(); 

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

        if (!InputHelper.readYesNo(scanner, ConsoleColors.RED + "\nAre you sure you want to delete this contact?" + ConsoleColors.RESET)) {
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
            System.out.println(ConsoleColors.GREEN + "\n✓ Contact deleted successfully." + ConsoleColors.RESET);
        } else {
            System.out.println(ConsoleColors.RED + "\n✗ Failed to delete contact." + ConsoleColors.RESET);
        }
    }

}