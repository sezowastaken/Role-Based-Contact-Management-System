package service;

import dao.ContactDAO;
import model.Contact;
import util.InputHelper;
import util.ConsoleColors;
import util.DateUtil;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
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
        System.out.printf(ConsoleColors.CYAN + "%-5s %-15s %-15s %-15s %-12s %-40s %-40s%n", "ID", "FIRST NAME", "LAST NAME",
                "PHONE", "BIRTH DATE", "EMAIL", "LINKEDIN URL" + ConsoleColors.RESET);
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

            System.out.printf(ConsoleColors.CYAN + "%-5d %-15s %-15s %-15s %-12s %-40s %-40s%n", id, firstName, lastName,
                    phone, birthDate, email, url + ConsoleColors.RESET);

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
        System.out.println("4 - Sort by age");
        System.out.println("0 - Cancel");

        int field = InputHelper.readIntInRange(scanner, "Field: ", 0, 4);
        if (field == 0) {
            System.out.println(ConsoleColors.RED + "Sort cancelled." + ConsoleColors.RESET);
            return;
        }

        String orderPrompt;
        if (field == 4) {
            // Age-specific prompts
            System.out.println("1 - Youngest first (Ascending by age)");
            System.out.println("2 - Oldest first (Descending by age)");
            orderPrompt = "Order: ";
        } else {
            System.out.println("1 - Ascending");
            System.out.println("2 - Descending");
            orderPrompt = "Order: ";
        }
        
        int order = InputHelper.readIntInRange(scanner, orderPrompt, 1, 2);

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
                sortField = "age";
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
        
        displayAllContacts(); // Listeyi göster ki ID seçebilsin

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
        System.out.printf(ConsoleColors.CYAN + "%-5s %-15s %-15s %-15s %-12s %-40s %-40s%n", "ID", "FIRST NAME", "LAST NAME",
                "PHONE", "BIRTH DATE", "EMAIL",
                "LINKEDIN URL" + ConsoleColors.RESET);
        System.out.println(ConsoleColors.WHITE +
                "----------------------------------------------------------------------------------------------------------------------------------------------------------------"
                + ConsoleColors.RESET);
        
        String birthDateStr = existing.getBirthDate() != null ? existing.getBirthDate().toString() : "-";
        System.out.printf(ConsoleColors.CYAN + "%-5d %-15s %-15s %-15s %-12s %-40s %-40s%n", existing.getContactId(),
                existing.getFirstName(), existing.getLastName(), existing.getPhoneNumber(),
                birthDateStr, existing.getEmail(), existing.getLinkedinUrl() + ConsoleColors.RESET);

        // Confirmation before update
        if (!InputHelper.readYesNo(scanner, ConsoleColors.BLUE + "\nDo you want to update this contact?" + ConsoleColors.RESET)) {
            System.out.println(ConsoleColors.RED + "Update cancelled." + ConsoleColors.RESET);
            return;
        }

        System.out.println(ConsoleColors.YELLOW
                + "\nEnter new values (Press Enter to keep current)." + ConsoleColors.RESET);

        // --- 1. İSİM (Zorunlu Döngü) ---
        while (true) {
            System.out.print(ConsoleColors.CYAN + "First name [" + existing.getFirstName() + "]: ");
            String input = scanner.nextLine().trim();
            
            if (input.isEmpty()) break; // Boşsa eskiyi koru, döngüden çık
            
            if (input.matches("[\\p{L}çğıöşüÇĞİÖŞÜ '\\-]+")) {
                existing.setFirstName(input);
                break; // Validasyon geçti, döngüden çık
            }
            System.out.println(ConsoleColors.RED + "Error: Name must contain only letters! Try again." + ConsoleColors.RESET);
        }

        // --- 2. SOYİSİM (Zorunlu Döngü) ---
        while (true) {
            System.out.print("Last name [" + existing.getLastName() + "]: ");
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) break;
            
            if (input.matches("[\\p{L}çğıöşüÇĞİÖŞÜ '\\-]+")) {
                existing.setLastName(input);
                break;
            }
            System.out.println(ConsoleColors.RED + "Error: Name must contain only letters! Try again." + ConsoleColors.RESET);
        }

        // Nickname (Opsiyonel)
        String nick = InputHelper.readLine(scanner,
                "Nickname [" + (existing.getNickname() == null ? "" : existing.getNickname()) + "]: ");
        if (!nick.isEmpty())
            existing.setNickname(nick);

        // --- 3. TELEFON (+90 ve Döngü) ---
        while (true) {
            System.out.print("Phone [" + existing.getPhoneNumber() + "] (+90) ");
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) break;
            
            String clean = input.replaceAll("[^0-9]", "");
            if (clean.length() == 11 && clean.startsWith("0")) clean = clean.substring(1);
            
            if (clean.matches("^5[0-9]{9}$")) {
                existing.setPhoneNumber(clean);
                break;
            }
            System.out.println(ConsoleColors.RED + "Error: Invalid phone! Must be 10 digits starting with 5. Try again." + ConsoleColors.RESET);
        }

        // --- 4. EMAIL (Zorunlu Döngü) ---
        while (true) {
            System.out.print("Email [" + existing.getEmail() + "]: ");
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) break;
            
            if (input.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
                existing.setEmail(input);
                break;
            }
            System.out.println(ConsoleColors.RED + "Error: Invalid email format! Try again." + ConsoleColors.RESET);
        }

        // --- 5. LINKEDIN (Akıllı Tamamlama) ---
        String currentLi = existing.getLinkedinUrl() == null ? "" : existing.getLinkedinUrl();
        String linkedin = InputHelper.readValidLinkedin(scanner, "LinkedIn [" + currentLi + "] (or 'skip')");
        if (linkedin != null) {
            existing.setLinkedinUrl(linkedin);
        }

        // --- 6. TARIH (KRİTİK DÜZELTME: yyyy-MM-dd) ---
        // Veritabanındaki LocalDate.toString() zaten yyyy-MM-dd verir
        String currentBirth = existing.getBirthDate() == null ? "" : existing.getBirthDate().toString(); 
        
        while (true) {
            // Kullanıcıya DOĞRU FORMATI (yyyy-MM-dd) gösteriyoruz
            System.out.print("Birth Date [" + currentBirth + "] (" + DateUtil.getDateFormat() + " or 'skip'): ");
            String input = scanner.nextLine().trim();
            
            if (input.isEmpty() || input.equalsIgnoreCase("skip")) break; // Boşsa geç

            // DateUtil artık yyyy-MM-dd bekliyor
            String err = DateUtil.checkDateValidity(input);
            if (err == null) {
                existing.setBirthDate(DateUtil.parse(input));
                break;
            } else {
                // Hata varsa yazdırıp döngü başa döner
                System.out.println(ConsoleColors.RED + err + ConsoleColors.RESET);
            }
        }

        boolean ok = contactDAO.updateContact(existing);
        if (ok) {
            System.out.println(ConsoleColors.GREEN + "\nContact updated successfully." + ConsoleColors.RESET);
        } else {
            System.out.println(ConsoleColors.RED + "\nFailed to update contact." + ConsoleColors.RESET);
        }
    }

    // ===================== ADD (Validasyonlu) =====================
    public void addContactInteractive(Scanner scanner) {
        System.out.println(ConsoleColors.WHITE + "\n=== Add New Contact ===");
        System.out.println(ConsoleColors.YELLOW + "Enter '0' at any time to cancel." + ConsoleColors.RESET);

        boolean adding = true;
        while (adding) {
            // First name with cancel check
            System.out.print("First name (0 to cancel): ");
            String firstInput = scanner.nextLine().trim();
            if (firstInput.equals("0")) {
                System.out.println(ConsoleColors.YELLOW + "Add contact cancelled." + ConsoleColors.RESET);
                return;
            }
            String first = InputHelper.readValidName(scanner, "");

            // Last name with cancel check  
            System.out.print("Last name (0 to cancel): ");
            String lastInput = scanner.nextLine().trim();
            if (lastInput.equals("0")) {
                System.out.println(ConsoleColors.YELLOW + "Add contact cancelled." + ConsoleColors.RESET);
                return;
            }
            String last = InputHelper.readValidName(scanner, "");

            // Nickname
            String nick = InputHelper.readLine(scanner, "Nickname (optional, 'skip', or 0 to cancel): ");
            if (nick.equals("0")) {
                System.out.println(ConsoleColors.YELLOW + "Add contact cancelled." + ConsoleColors.RESET);
                return;
            }
            if (nick.equalsIgnoreCase("skip")) {
                nick = null;
            }

            // Phone
            System.out.print("Phone number (0 to cancel) (+90) ");
            String phoneInput = scanner.nextLine().trim();
            if (phoneInput.equals("0")) {
                System.out.println(ConsoleColors.YELLOW + "Add contact cancelled." + ConsoleColors.RESET);
                return;
            }
            String phone = InputHelper.readValidPhoneTR(scanner, "");

            // Email
            System.out.print("Email (0 to cancel): ");
            String emailInput = scanner.nextLine().trim();
            if (emailInput.equals("0")) {
                System.out.println(ConsoleColors.YELLOW + "Add contact cancelled." + ConsoleColors.RESET);
                return;
            }
            String email = InputHelper.readValidEmail(scanner, "");

            // LinkedIn
            String linkedin = InputHelper.readValidLinkedin(scanner, "LinkedIn URL (optional, 0 to cancel)");
            if (linkedin != null && linkedin.equals("CANCEL")) {
                System.out.println(ConsoleColors.YELLOW + "Add contact cancelled." + ConsoleColors.RESET);
                return;
            }

            // Birth date
            System.out.print("Birth date (0 to cancel) (" + DateUtil.getDateFormat() + "): ");
            String dateInput = scanner.nextLine().trim();
            if (dateInput.equals("0")) {
                System.out.println(ConsoleColors.YELLOW + "Add contact cancelled." + ConsoleColors.RESET);
                return;
            }
            LocalDate birthDate = InputHelper.readValidPastDate(scanner, "");

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
        System.out.printf("%-5s %-15s %-15s %-15s %-12s %-40s %-40s%n", "ID", "FIRST NAME", "LAST NAME", "PHONE", "BIRTH DATE", "EMAIL",
                "LINKEDIN URL");
        System.out.println(
                "----------------------------------------------------------------------------------------------------------------------------------------------------------------");
        String firstName = existing.getFirstName() != null ? existing.getFirstName() : "-";
        String lastName = existing.getLastName() != null ? existing.getLastName() : "-";
        String phone = existing.getPhoneNumber() != null ? existing.getPhoneNumber() : "-";
        String email = existing.getEmail() != null ? existing.getEmail() : "-";
        String url = existing.getLinkedinUrl() != null ? existing.getLinkedinUrl() : "-";
        String birthDate = existing.getBirthDate() != null ? existing.getBirthDate().toString() : "-";
        System.out.printf("%-5d %-15s %-15s %-15s %-12s %-40s %-40s%n", existing.getContactId(), firstName, lastName, phone,
                birthDate, email, url);

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