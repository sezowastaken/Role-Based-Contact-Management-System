package ui.menu;

import java.util.Scanner;
import model.User;
import service.UserService;
import service.StatisticsService;
import model.Role;
import util.InputHelper;
import util.ConsoleColors; 

public class ManagerMenu extends BaseMenu {

    private final UserService userService;
    private final StatisticsService statisticsService;

    public ManagerMenu(User currentUser, Scanner scanner, UserService userService) {
        super(currentUser, scanner);
        this.userService = userService;
        this.statisticsService = new StatisticsService();
    }

    @Override
    protected String getTitle() {
        return "Manager Panel";
    }

    @Override
    protected void printOptions() {
        System.out.println("1 - Change password");
        System.out.println("2 - View contacts statistical info");
        System.out.println("3 - List all users");
        System.out.println("4 - Update existing user");
        System.out.println("5 - Hire new user (Add)");
        System.out.println("6 - Fire existing user (Delete)");
        System.out.println("0 - Logout");
    }

    @Override
    protected void handleOption(String choice) {
        switch (choice) {
            case "1": handleChangePassword(); break;
            case "2": handleContactStatistics(); break;
            case "3": handleListUsers(); break;
            case "4": handleUpdateUser(); break;
            case "5": handleAddUser(); break;
            case "6": handleDeleteUser(); break;
            case "0": return;
            default: printError("Invalid choice. Please select one of the options above.");
        }
    }

    private void handleChangePassword() {
        System.out.println(ConsoleColors.CYAN + "\n--- Change Password ---" + ConsoleColors.RESET);

        // [DEĞİŞİKLİK] InputHelper kullanımı devam ediyor
        String oldPassword = InputHelper.readNonEmptyLine(scanner, "Enter old password: ");
        String newPassword = InputHelper.readNonEmptyLine(scanner, "Enter new password: ");

        try {
            userService.changePassword(currentUser, oldPassword, newPassword);
            printSuccess("Password updated successfully.");
        } catch (Exception e) {
            printError(e.getMessage());
        }
    }

    private void handleContactStatistics() {
        // İstatistik servisini çağır
        statisticsService.displayContactStatistics();
        // Okumak için bekleme ekleyelim
        // pause(); // BaseMenu'de pause varsa otomatik yapar, yoksa kalsın.
    }

    private void handleListUsers() {
        System.out.println(ConsoleColors.CYAN + "\n--- System Users List ---" + ConsoleColors.RESET);

        var users = userService.findAllUsers();
        
        // [DEĞİŞİKLİK] Tablo formatında yazdırma (Daha okunaklı)
        System.out.printf(ConsoleColors.YELLOW + "%-5s %-15s %-25s %-15s%n" + ConsoleColors.RESET, "ID", "USERNAME", "FULL NAME", "ROLE");
        System.out.println("----------------------------------------------------------------");

        for (User u : users) {
            // [NOT] User modelinde getUserId() veya getId() hangisiyse onu kullan. Burada getUserId() varsaydım.
            System.out.printf("%-5d %-15s %-25s %-15s%n", 
                u.getUserId(), 
                u.getUsername(), 
                u.getName() + " " + u.getSurname(), // Full Name birleşimi
                u.getRole());
        }
        System.out.println("----------------------------------------------------------------");
        printSuccess("Total users: " + users.size());
    }

    private void handleUpdateUser() {
        System.out.println(ConsoleColors.CYAN + "\n--- Update Existing User ---" + ConsoleColors.RESET);

        // Listeyi gösterelim ki ID seçebilsin
        handleListUsers();

        int userId = InputHelper.readIntInRange(scanner, "User ID to update: ", 1, Integer.MAX_VALUE);
        User target = userService.findUserById(userId);

        if (target == null) {
            printError("User not found with ID " + userId);
            return;
        }

        System.out.println("Selected: " + target.getName() + " (" + target.getUsername() + ")");
        
        boolean confirm = InputHelper.readYesNo(scanner, "Do you want to update this user?");
        if (!confirm) {
            printError("Update cancelled.");
            return;
        }

        String newUsername = InputHelper.readNonEmptyLine(scanner, "New username: ");
        String newFirstname = InputHelper.readValidName(scanner, "New first name: "); // Sadece harf
        String newLastname = InputHelper.readValidName(scanner, "New lastname: ");   // Sadece harf
        
        // Rol seçimi metodunu aşağıda ayırdım (Temiz kod için)
        Role role = selectRole(); 
        if (role == null) return; // İptal durumu

        try {
            userService.updateUser(userId, newUsername, newFirstname, newLastname, role);
            printSuccess("User updated successfully.");
        } catch (Exception e) {
            printError(e.getMessage());
        }
    }

    private void handleAddUser() {
        System.out.println(ConsoleColors.CYAN + "\n--- Hire New User ---" + ConsoleColors.RESET);

        String username = InputHelper.readNonEmptyLine(scanner, "Username: ");
        String password = InputHelper.readNonEmptyLine(scanner, "Password: ");
        
        String first = InputHelper.readValidName(scanner, "First name: "); 
        String last = InputHelper.readValidName(scanner, "Last name: "); 
        
        Role role = selectRole();
        if (role == null) return;

        try {
            userService.createUser(username, password, first, last, role);
            printSuccess("User created successfully.");
        } catch (Exception e) {
            printError(e.getMessage());
        }
    }

    private void handleDeleteUser() {
        System.out.println(ConsoleColors.RED + "\n--- Fire (Delete) User ---" + ConsoleColors.RESET);

        handleListUsers();

        int id = InputHelper.readIntInRange(scanner, "User ID to delete: ", 1, Integer.MAX_VALUE);
        
        // Kendini silme kontrolü (Service de yapar ama UI'da da olsun)
        if (id == currentUser.getUserId()) {
            printError("You cannot delete your own account!");
            return;
        }

        User target = userService.findUserById(id);
        if (target == null) {
            printError("User not found with ID " + id);
            return;
        }

        System.out.println("Selected user: " + target.getName() + " (" + target.getUsername() + ")");
        boolean confirm = InputHelper.readYesNo(scanner, ConsoleColors.RED + "Are you SURE you want to delete this user?" + ConsoleColors.RESET);
        
        if (!confirm) {
            printError("Delete cancelled.");
            return;
        }

        try {
            userService.deleteUser(id, currentUser.getUserId());
            printSuccess("User deleted.");
        } catch (Exception e) {
            printError(e.getMessage());
        }
    }

    private Role selectRole() {
        System.out.println("Select Role:");
        System.out.println("1 - Tester");
        System.out.println("2 - Junior Developer");
        System.out.println("3 - Senior Developer");
        System.out.println("4 - Manager");
        
        int choice = InputHelper.readIntInRange(scanner, "Role Choice (1-4): ", 1, 4);
        
        switch (choice) {
            case 1: return Role.TESTER;
            case 2: return Role.JUNIOR_DEV;
            case 3: return Role.SENIOR_DEV;
            case 4: return Role.MANAGER;
            default: 
                printError("Invalid role.");
                return null;
        }
    }

    
    private void printError(String msg) {
        System.out.println(ConsoleColors.RED + "ERROR: " + msg + ConsoleColors.RESET);
    }

    private void printSuccess(String msg) {
        System.out.println(ConsoleColors.GREEN + "SUCCESS: " + msg + ConsoleColors.RESET);
    }
}