package service;

import dao.ContactDAO;
import model.Contact;
import util.InputHelper;
import util.ConsoleColors;
import util.DateUtil;
import undo.UndoManager;
import undo.UndoAction;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class ContactService {

    private final ContactDAO contactDAO = new ContactDAO();
    private final UndoManager undoManager;

    public ContactService() {
        this(null);
    }

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

    public List<Contact> searchByPhoneNumber(String query) {
        return contactDAO.searchByPhoneContains(query);
    }

    public List<Contact> searchByFirstNameAndBirthMonth(String firstName, int month) {
        return contactDAO.searchByFirstNameAndBirthMonth(firstName, month);
    }

    public List<Contact> searchByPhonePrefixAndBirthYear(String phonePrefix, int year) {
        return contactDAO.searchByPhonePrefixAndBirthYear(phonePrefix, year);
    }

    public List<Contact> searchByFirstAndLastName(String firstNamePart, String lastNamePart) {
        return contactDAO.searchByFirstAndLastName(firstNamePart, lastNamePart);
    }

    // ----------------- Printing -----------------
    public void printContactsList(List<Contact> contacts) {
        if (contacts == null || contacts.isEmpty()) {
            System.out.println(ConsoleColors.YELLOW + "\nNo contacts found." + ConsoleColors.RESET);
            return;
        }

        System.out.println(ConsoleColors.BLUE + "\nCONTACTS LIST" + ConsoleColors.RESET);
        System.out.printf(
                ConsoleColors.YELLOW + "%-5s %-15s %-15s %-15s %-12s %-40s %-40s%n",
                "ID", "FIRST NAME", "LAST NAME", "PHONE", "BIRTH DATE", "EMAIL", "LINKEDIN URL" + ConsoleColors.RESET);
        System.out.println(ConsoleColors.WHITE +
                "----------------------------------------------------------------------------------------------------------------------------------------------------------------"
                + ConsoleColors.RESET);

        for (Contact contact : contacts) {
            int id = contact.getContactId();
            String firstName = contact.getFirstName() != null ? contact.getFirstName() : "-";
            String lastName = contact.getLastName() != null ? contact.getLastName() : "-";
            String phone = contact.getPhoneNumber() != null ? contact.getPhoneNumber() : "-";
            String email = contact.getEmail() != null ? contact.getEmail() : "-";
            String url = contact.getLinkedinUrl() != null ? contact.getLinkedinUrl() : "-";
            String birthDate = contact.getBirthDate() != null ? contact.getBirthDate().toString() : "-";

            System.out.printf(ConsoleColors.CYAN + "%-5d %-15s %-15s %-15s %-12s %-40s %-40s%n" + ConsoleColors.RESET,
                    id, firstName, lastName,
                    phone, birthDate, email, url);

        }

        System.out.println(
                ConsoleColors.GREEN + "\nTotal " + contacts.size() + " contact(s) found." + ConsoleColors.RESET);
    }

    public void displayAllContacts() {
        List<Contact> contacts = getAllContacts();
        printContactsList(contacts);
    }

    // ----------------- Search (interactive) -----------------
    public void searchContactsInteractive(Scanner scanner) {
        System.out.println(ConsoleColors.CYAN + "\n=== Search Contacts ===" + ConsoleColors.RESET);
        System.out.println(ConsoleColors.WHITE + "1 - Search by first name");
        System.out.println("2 - Search by last name");
        System.out.println("3 - Search by phone number");
        System.out.println("4 - First name + birth month");
        System.out.println("5 - Phone prefix + birth year");
        System.out.println("6 - First name + last name" + ConsoleColors.RESET);
        System.out.println(ConsoleColors.RED + "0 - Cancel" + ConsoleColors.RESET);

        int choice = InputHelper.readIntInRange(scanner, ConsoleColors.YELLOW + "Choice: " + ConsoleColors.RESET, 0, 6);

        List<Contact> results;

        switch (choice) {
            case 0:
                System.out.println(ConsoleColors.RED + "Search cancelled." + ConsoleColors.RESET);
                return;
            case 1: {
                String first = InputHelper.readValidName(scanner,
                        ConsoleColors.WHITE + "First name contains: " + ConsoleColors.RESET);
                results = searchByFirstName(first);
                break;
            }
            case 2: {
                String last = InputHelper.readValidName(scanner,
                        ConsoleColors.WHITE + "Last name contains: " + ConsoleColors.RESET);
                results = searchByLastName(last);
                break;
            }
            case 3: {
                String phone = InputHelper.readLine(scanner,
                        ConsoleColors.WHITE + "Phone number contains: " + ConsoleColors.RESET);
                results = searchByPhoneNumber(phone);
                break;
            }
            case 4: {
                String first = InputHelper.readValidName(scanner,
                        ConsoleColors.WHITE + "First name: " + ConsoleColors.RESET);
                int month = InputHelper.readIntInRange(scanner, "Birth month (1-12): ", 1, 12);
                results = searchByFirstNameAndBirthMonth(first, month);
                break;
            }
            case 5: {
                String prefix = InputHelper.readLine(scanner,
                        ConsoleColors.WHITE + "Phone number prefix: " + ConsoleColors.RESET);
                int year = InputHelper.readIntInRange(scanner,
                        ConsoleColors.WHITE + "Birth year (e.g., 1990): " + ConsoleColors.RESET, 1900, 2100);
                results = searchByPhonePrefixAndBirthYear(prefix, year);
                break;
            }
            case 6: {
                String firstPart = InputHelper.readValidName(scanner,
                        ConsoleColors.WHITE + "First name contains: " + ConsoleColors.RESET);
                String lastPart = InputHelper.readValidName(scanner,
                        ConsoleColors.WHITE + "Last name contains: " + ConsoleColors.RESET);
                results = searchByFirstAndLastName(firstPart, lastPart);
                break;
            }
            default:
                System.out.println(ConsoleColors.RED + "Invalid choice." + ConsoleColors.RESET);
                return;
        }

        printContactsList(results);
    }

    // ----------------- Sort (interactive) -----------------
    public void sortContactsInteractive(Scanner scanner) {
        System.out.println(ConsoleColors.CYAN + "\n=== Sort Contacts ===" + ConsoleColors.RESET);
        System.out.println(ConsoleColors.WHITE + "1 - Sort by first name");
        System.out.println("2 - Sort by last name");
        System.out.println("3 - Sort by phone number");
        System.out.println("4 - Sort by birth date" + ConsoleColors.RESET);
        System.out.println(ConsoleColors.RED + "0 - Cancel" + ConsoleColors.RESET);

        int field = InputHelper.readIntInRange(scanner, "Field: ", 0, 4);
        if (field == 0) {
            System.out.println(ConsoleColors.RED + "Sort cancelled." + ConsoleColors.RESET);
            return;
        }

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
            case 4:
                sortField = "birth_date";
                break;
            default:
                System.out.println(ConsoleColors.RED + "Invalid choice." + ConsoleColors.RESET);
                return;
        }

        System.out.println(ConsoleColors.YELLOW + "\nOrder:");
        System.out.println("1 - Ascending");
        System.out.println("2 - Descending" + ConsoleColors.RESET);

        int order = InputHelper.readIntInRange(scanner, "Choice: ", 1, 2);
        boolean ascending = (order == 1);
        List<Contact> contacts = getAllSorted(sortField, ascending);
        printContactsList(contacts);
    }

    // ----------------- Update (interactive) -----------------
    public void updateContactInteractive(Scanner scanner) {
        System.out.println(ConsoleColors.CYAN + "\n=== Update Contact ===" + ConsoleColors.RESET);

        displayAllContacts();

        int id = InputHelper.readIntInRange(scanner,
                ConsoleColors.YELLOW + "Contact ID to update (0 = cancel): " + ConsoleColors.RESET, 0,
                Integer.MAX_VALUE);
        if (id == 0) {
            System.out.println(ConsoleColors.RED + "Update cancelled." + ConsoleColors.RESET);
            return;
        }

        Contact existing = contactDAO.getContactById(id);
        if (existing == null) {
            System.out.println(ConsoleColors.YELLOW + "Contact not found with ID: " + id + ConsoleColors.RESET);
            return;
        }

        // Snapshot BEFORE modification for undo
        Contact previousState = UndoAction.cloneContact(existing);

        // Display current contact
        System.out.println(ConsoleColors.BLUE + "\nContact to update:" + ConsoleColors.RESET);
        System.out.printf(ConsoleColors.YELLOW + "%-5s %-15s %-15s %-15s %-12s %-40s %-40s%n" + ConsoleColors.RESET,
                "ID", "FIRST NAME", "LAST NAME", "PHONE", "BIRTH DATE", "EMAIL", "LINKEDIN URL");
        System.out.println(ConsoleColors.WHITE +
                "----------------------------------------------------------------------------------------------------------------------------------------------------------------"
                + ConsoleColors.RESET);

        String birthDateStr = existing.getBirthDate() != null ? existing.getBirthDate().toString() : "-";
        System.out.printf(ConsoleColors.WHITE + "%-5d %-15s %-15s %-15s %-12s %-40s %-40s%n" + ConsoleColors.RESET,
                existing.getContactId(),
                existing.getFirstName() != null ? existing.getFirstName() : "-",
                existing.getLastName() != null ? existing.getLastName() : "-",
                existing.getPhoneNumber() != null ? existing.getPhoneNumber() : "-",
                birthDateStr,
                existing.getEmail() != null ? existing.getEmail() : "-",
                existing.getLinkedinUrl() != null ? existing.getLinkedinUrl() : "-");

        if (!InputHelper.readYesNo(scanner,
                ConsoleColors.BLUE + "\nDo you want to update this contact?" + ConsoleColors.RESET)) {
            System.out.println(ConsoleColors.RED + "Update cancelled." + ConsoleColors.RESET);
            return;
        }

        System.out.println(
                ConsoleColors.YELLOW + "\nEnter new values (Press Enter to keep current)." + ConsoleColors.RESET);

        // First name
        while (true) {
            System.out.print(ConsoleColors.WHITE + "First name ["
                    + (existing.getFirstName() == null ? "" : existing.getFirstName()) + "]: " + ConsoleColors.RESET);
            String input = scanner.nextLine().trim();
            if (input.isEmpty())
                break;
            if (input.matches("[\\p{L}çğıöşüÇĞİÖŞÜ '\\-]+")) {
                existing.setFirstName(input);
                break;
            }
            System.out.println(
                    ConsoleColors.RED + "Error: Name must contain only letters! Try again." + ConsoleColors.RESET);
        }

        // Last name
        while (true) {
            System.out.print(ConsoleColors.WHITE + "Last name ["
                    + (existing.getLastName() == null ? "" : existing.getLastName()) + "]: " + ConsoleColors.RESET);
            String input = scanner.nextLine().trim();
            if (input.isEmpty())
                break;
            if (input.matches("[\\p{L}çğıöşüÇĞİÖŞÜ '\\-]+")) {
                existing.setLastName(input);
                break;
            }
            System.out.println(
                    ConsoleColors.RED + "Error: Name must contain only letters! Try again." + ConsoleColors.RESET);
        }

        // Nickname
        String nickPrompt = ConsoleColors.WHITE + "Nickname ["
                + (existing.getNickname() == null ? "" : existing.getNickname()) + "]: ";
        String nick = InputHelper.readLine(scanner, ConsoleColors.CYAN + nickPrompt + ConsoleColors.RESET);
        if (!nick.isEmpty())
            existing.setNickname(nick);

        // Phone
        while (true) {
            System.out.print(ConsoleColors.WHITE + "Phone ["
                    + (existing.getPhoneNumber() == null ? "" : existing.getPhoneNumber()) + "] (+90): "
                    + ConsoleColors.RESET);
            String input = scanner.nextLine().trim();
            if (input.isEmpty())
                break;
            String clean = input.replaceAll("[^0-9]", "");
            if (clean.length() == 11 && clean.startsWith("0"))
                clean = clean.substring(1);
            if (clean.matches("^5[0-9]{9}$")) {
                existing.setPhoneNumber(clean);
                break;
            }
            System.out.println(ConsoleColors.RED + "Error: Invalid phone! Must be 10 digits starting with 5. Try again."
                    + ConsoleColors.RESET);
        }

        // Email
        while (true) {
            System.out.print(ConsoleColors.CYAN + "Email [" + (existing.getEmail() == null ? "" : existing.getEmail())
                    + "]: " + ConsoleColors.RESET);
            String input = scanner.nextLine().trim();
            if (input.isEmpty())
                break;
            if (input.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
                existing.setEmail(input);
                break;
            }
            System.out.println(ConsoleColors.RED + "Error: Invalid email format! Try again." + ConsoleColors.RESET);
        }

        // LinkedIn
        String currentLi = existing.getLinkedinUrl() == null ? "" : existing.getLinkedinUrl();
        String linkedin = InputHelper.readValidLinkedin(scanner,
                ConsoleColors.WHITE + "LinkedIn [" + currentLi + "] (or 'skip'): " + ConsoleColors.RESET);
        if (linkedin != null)
            existing.setLinkedinUrl(linkedin);

        // Birth date
        String currentBirth = existing.getBirthDate() == null ? "" : existing.getBirthDate().toString();
        while (true) {
            System.out.print(ConsoleColors.WHITE + "Birth Date [" + currentBirth + "] (" + DateUtil.getDateFormat()
                    + " or 'skip'): " + ConsoleColors.RESET);
            String input = scanner.nextLine().trim();
            if (input.isEmpty() || input.equalsIgnoreCase("skip"))
                break;
            String err = DateUtil.checkDateValidity(input);
            if (err == null) {
                existing.setBirthDate(DateUtil.parse(input));
                break;
            } else {
                System.out.println(ConsoleColors.RED + err + ConsoleColors.RESET);
            }
        }

        boolean ok = contactDAO.updateContact(existing);
        if (ok) {
            System.out.println(ConsoleColors.GREEN + "\nContact updated successfully." + ConsoleColors.RESET);
            if (undoManager != null) {
                undoManager.push(UndoAction.forContactUpdate(contactDAO, previousState));
            }
        } else {
            System.out.println(ConsoleColors.RED + "\nFailed to update contact." + ConsoleColors.RESET);
        }
    }

    // ----------------- Add (interactive) -----------------
    public void addContactInteractive(Scanner scanner) {
        System.out.println(ConsoleColors.WHITE + "\n=== Add New Contact ===");
        System.out.println(ConsoleColors.RED + "Enter '0' at any time to cancel." + ConsoleColors.RESET);

        boolean adding = true;
        while (adding) {
            // First name with cancel check
            System.out.print(ConsoleColors.WHITE + "First name (0 to cancel): " + ConsoleColors.RESET);
            String firstInput = scanner.nextLine().trim();
            if (firstInput.equals("0")) {
                System.out.println(ConsoleColors.RED + "Add contact cancelled." + ConsoleColors.RESET);
                return;
            }
            // Use InputHelper to validate and read (it will prompt)
            String first = InputHelper.readValidName(scanner, "First name: ");

            // Last name with cancel check
            System.out.print(ConsoleColors.WHITE + "Last name (0 to cancel): " + ConsoleColors.RESET);
            String lastInput = scanner.nextLine().trim();
            if (lastInput.equals("0")) {
                System.out.println(ConsoleColors.RED + "Add contact cancelled." + ConsoleColors.RESET);
                return;
            }
            String last = InputHelper.readValidName(scanner, ConsoleColors.WHITE + "Last name: " + ConsoleColors.RESET);

            // Nickname
            String nick = InputHelper.readLine(scanner,
                    ConsoleColors.WHITE + "Nickname (optional, 'skip', or 0 to cancel): " + ConsoleColors.RESET);
            if (nick.equals("0")) {
                System.out.println(ConsoleColors.RED + "Add contact cancelled." + ConsoleColors.RESET);
                return;
            }
            if (nick.equalsIgnoreCase("skip")) {
                nick = null;
            }

            // Phone
            System.out.print(ConsoleColors.WHITE + "Phone number (0 to cancel) (+90) " + ConsoleColors.RESET);
            String phoneInput = scanner.nextLine().trim();
            if (phoneInput.equals("0")) {
                System.out.println(ConsoleColors.YELLOW + "Add contact cancelled." + ConsoleColors.RESET);
                return;
            }
            String phone = InputHelper.readValidPhoneTR(scanner,
                    ConsoleColors.WHITE + "Phone number: " + ConsoleColors.RESET);

            // Email
            System.out.print("Email (0 to cancel): ");
            String emailInput = scanner.nextLine().trim();
            if (emailInput.equals("0")) {
                System.out.println(ConsoleColors.YELLOW + "Add contact cancelled." + ConsoleColors.RESET);
                return;
            }
            String email = InputHelper.readValidEmail(scanner, "Email: ");

            // LinkedIn
            String linkedin = InputHelper.readValidLinkedin(scanner,
                    ConsoleColors.WHITE + "LinkedIn URL (optional, 0 to cancel): " + ConsoleColors.RESET);
            if (linkedin != null && linkedin.equals("CANCEL")) {
                System.out.println(ConsoleColors.YELLOW + "Add contact cancelled." + ConsoleColors.RESET);
                return;
            }

            // Birth date
            System.out.print(ConsoleColors.WHITE + "Birth date (0 to cancel) (" + DateUtil.getDateFormat() + "): "
                    + ConsoleColors.RESET);
            String dateInput = scanner.nextLine().trim();
            if (dateInput.equals("0")) {
                System.out.println(ConsoleColors.YELLOW + "Add contact cancelled." + ConsoleColors.RESET);
                return;
            }
            LocalDate birthDate = InputHelper.readValidPastDate(scanner,
                    "Birth date (" + DateUtil.getDateFormat() + "): ");

            // Preview
            System.out.println(ConsoleColors.BLUE + "\nContact preview:" + ConsoleColors.RESET);
            System.out.printf(ConsoleColors.WHITE + "  First Name: %s%n", first);
            System.out.printf("  Last Name: %s%n", last);
            System.out.printf("  Nickname: %s%n", nick == null ? "-" : nick);
            System.out.printf("  Phone: %s%n", phone);
            System.out.printf("  Email: %s%n", email);
            System.out.printf("  LinkedIn: %s%n", linkedin == null ? "-" : linkedin);
            System.out.printf("  Birth Date: %s%n", birthDate + ConsoleColors.RESET);

            if (InputHelper.readYesNo(scanner,
                    ConsoleColors.YELLOW + "\nConfirm adding this contact?" + ConsoleColors.RESET)) {
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
                    System.out.println(ConsoleColors.GREEN + "\nContact added successfully." + ConsoleColors.RESET);
                    if (undoManager != null && c.getContactId() != 0) {
                        undoManager.push(UndoAction.forContactInsert(contactDAO, c.getContactId()));
                    }
                    adding = false;
                } else {
                    System.out.println(ConsoleColors.RED + "\nFailed to add contact." + ConsoleColors.RESET);
                    if (!InputHelper.readYesNo(scanner, ConsoleColors.YELLOW + "Try again?" + ConsoleColors.RESET)) {
                        adding = false;
                    }
                }
            } else {
                if (!InputHelper.readYesNo(scanner, ConsoleColors.YELLOW + "Edit again?" + ConsoleColors.RESET)) {
                    System.out.println(ConsoleColors.RED + "Add contact cancelled." + ConsoleColors.RESET);
                    adding = false;
                }
            }
        }
    }

    // ----------------- Delete (interactive) -----------------
    public void deleteContactInteractive(Scanner scanner) {
        System.out.println(ConsoleColors.BLUE + "\n=== Delete Contact ===" + ConsoleColors.RESET);

        displayAllContacts();

        int id = InputHelper.readIntInRange(scanner,
                ConsoleColors.YELLOW + "Contact ID to delete (0 = cancel): " + ConsoleColors.RESET, 0,
                Integer.MAX_VALUE);
        if (id == 0) {
            System.out.println(ConsoleColors.RED + "Delete cancelled." + ConsoleColors.RESET);
            return;
        }

        Contact existing = contactDAO.getContactById(id);
        if (existing == null) {
            System.out.println(ConsoleColors.YELLOW + "Contact not found with ID: " + id + ConsoleColors.RESET);
            return;
        }

        // Snapshot BEFORE delete for undo
        Contact contactSnapshot = UndoAction.cloneContact(existing);

        System.out.println(ConsoleColors.BLUE + "\nContact to delete:" + ConsoleColors.RESET);
        System.out.printf(ConsoleColors.YELLOW + "%-5s %-15s %-15s %-15s %-12s %-40s %-40s%n", "ID", "FIRST NAME",
                "LAST NAME", "PHONE",
                "BIRTH DATE", "EMAIL",
                "LINKEDIN URL" + ConsoleColors.RESET);
        System.out.println(ConsoleColors.WHITE +
                "----------------------------------------------------------------------------------------------------------------------------------------------------------------"
                + ConsoleColors.RESET + ConsoleColors.CYAN);
        String firstName = existing.getFirstName() != null ? existing.getFirstName() : "-";
        String lastName = existing.getLastName() != null ? existing.getLastName() : "-";
        String phone = existing.getPhoneNumber() != null ? existing.getPhoneNumber() : "-";
        String email = existing.getEmail() != null ? existing.getEmail() : "-";
        String url = existing.getLinkedinUrl() != null ? existing.getLinkedinUrl() : "-";
        String birthDate = existing.getBirthDate() != null ? existing.getBirthDate().toString() : "-";
        System.out.printf("%-5d %-15s %-15s %-15s %-12s %-40s %-40s%n", existing.getContactId(), firstName, lastName,
                phone,
                birthDate, email, url + ConsoleColors.RESET);

        if (!InputHelper.readYesNo(scanner,
                ConsoleColors.YELLOW + "\nAre you sure you want to delete this contact?" + ConsoleColors.RESET)) {
            System.out.println(ConsoleColors.RED + "Delete cancelled." + ConsoleColors.RESET);
            return;
        }

        String confirm2 = InputHelper.readLine(scanner, ConsoleColors.YELLOW
                + "This action cannot be undone. Type 'DELETE' to confirm: " + ConsoleColors.RESET);
        if (!confirm2.trim().equals("DELETE")) {
            System.out.println(ConsoleColors.RED + "Delete cancelled." + ConsoleColors.RESET);
            return;
        }

        boolean ok = contactDAO.deleteContact(id);
        if (ok) {
            System.out.println(ConsoleColors.GREEN + "\nContact deleted successfully." + ConsoleColors.RESET);
            if (undoManager != null && contactSnapshot != null) {
                undoManager.push(UndoAction.forContactDelete(contactDAO, contactSnapshot));
            }
        } else {
            System.out.println(ConsoleColors.RED + "\nFailed to delete contact." + ConsoleColors.RESET);
        }
    }
}