package ui.menu;

import java.util.Scanner;

import dao.UserDAO;
import model.User;
import service.ContactService;
import service.UserService;
import util.ConsoleColors; // Renkler eklendi
import util.InputHelper; // InputHelper eklendi

public class SeniorDevMenu extends BaseMenu {

    private final ContactService contactService;
    private final UserService userService;

    public SeniorDevMenu(User currentUser, Scanner scanner) {
        super(currentUser, scanner);
        this.contactService = new ContactService();
        this.userService = new UserService(new UserDAO());
    }

    @Override
    protected String getTitle() {
        return "Senior Developer Menu";
    }

    @Override
    protected void printOptions() {
        System.out.println("┌──────────────────────────────────────────────────────────────────────┐");
        System.out.println("│                        SENIOR DEVELOPER MENU                         │");
        System.out.println("├──────────────────────────────────────────────────────────────────────┤");
        System.out.println("│ 1 - Change password                                                  │");
        System.out.println("│ 2 - List all contacts                                                │");
        System.out.println("│ 3 - Search contacts by selected field(s)                             │");
        System.out.println("│ 4 - Sort results by selected field (ascending / descending)          │");
        System.out.println("│ 5 - Update existing contact                                          │");
        System.out.println("│ 6 - Add new contact(s)                                               │");
        System.out.println("│ 7 - Delete existing contact(s)                                       │");
        if (isUndoAvailable()) {
            System.out.println("│ U - Undo last operation                                              │");
        }
        System.out.println("│ 0 - Logout                                                           │");
        System.out.println("└──────────────────────────────────────────────────────────────────────┘");
    }

    @Override
    protected void handleOption(String choice) {
        switch (choice) {
            case "1":
                // [DÜZELTME] UserService'ten sildiğimiz metodu burada lokal olarak yapıyoruz
                handleChangePassword();
                break;
            case "2":
                contactService.displayAllContacts();
                break;
            case "3":
                // ContactService içindeki metodun artık InputHelper kullandığını varsayıyoruz
                contactService.searchContactsInteractive(scanner);
                break;
            case "4":
                contactService.sortContactsInteractive(scanner);
                break;
            case "5":
                contactService.updateContactInteractive(scanner);
                break;
            case "6":
                contactService.addContactInteractive(scanner);
                break;
            case "7":
                contactService.deleteContactInteractive(scanner);
                break;
            default:
                System.out.println(ConsoleColors.RED + "\nInvalid choice. Please select one of the options above."
                        + ConsoleColors.RESET);
        }
    }

    // ==========================================
    // ŞİFRE DEĞİŞTİRME (LOCAL IMPLEMENTATION)
    // ==========================================
    private void handleChangePassword() {
        System.out.println(ConsoleColors.CYAN + "\n--- Change Password ---" + ConsoleColors.RESET);
        System.out.println(ConsoleColors.YELLOW + "Enter '0' at any time to cancel." + ConsoleColors.RESET);

        // InputHelper kullanarak güvenli okuma yapıyoruz
        String oldPass = InputHelper.readNonEmptyLine(scanner, "Current password (0 to cancel): ");
        if (oldPass.equals("0")) {
            System.out.println(ConsoleColors.YELLOW + "Password change cancelled." + ConsoleColors.RESET);
            return;
        }

        String newPass = InputHelper.readNonEmptyLine(scanner, "New password (0 to cancel): ");
        if (newPass.equals("0")) {
            System.out.println(ConsoleColors.YELLOW + "Password change cancelled." + ConsoleColors.RESET);
            return;
        }

        // İsteğe bağlı: Yeni şifre tekrarı sorulabilir
        // String confirm = InputHelper.readNonEmptyLine(scanner, "Confirm new password:
        // ");
        // if (!newPass.equals(confirm)) { ... return; }

        try {
            // Saf backend metodunu çağırıyoruz
            userService.changePassword(currentUser, oldPass, newPass);
            System.out.println(ConsoleColors.GREEN + "Password updated successfully." + ConsoleColors.RESET);
        } catch (Exception e) {
            // Service'den gelen hatayı (yanlış eski şifre vb.) basıyoruz
            System.out.println(ConsoleColors.RED + "Error: " + e.getMessage() + ConsoleColors.RESET);
        }
    }
}