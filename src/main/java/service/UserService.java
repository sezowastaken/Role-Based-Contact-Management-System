package service;

import dao.UserDAO;
import model.Role;
import model.User;
import undo.UndoAction;
import undo.UndoManager;
import util.HashUtil;
import util.InputHelper;

import java.util.List;
import java.util.Scanner;

public class UserService {

    private final UserDAO userDAO;
    private final UndoManager undoManager;

    // Convenience constructor'lar – eski kullanımlar bozulmasın diye overload'lar bırakıyoruz.

    /** Varsayılan ctor: DAO oluşturur, undo yoktur. */
    public UserService() {
        this(new UserDAO(), null);
    }

    /** Sadece DAO alan ctor (eski kodlarla uyum için). */
    public UserService(UserDAO userDAO) {
        this(userDAO, null);
    }

    /** Sadece UndoManager alan ctor (yeni menülerde kolay kullanım için). */
    public UserService(UndoManager undoManager) {
        this(new UserDAO(), undoManager);
    }

    /** Asıl ctor: Hem DAO hem UndoManager alır. */
    public UserService(UserDAO userDAO, UndoManager undoManager) {
        this.userDAO = userDAO;
        this.undoManager = undoManager;
    }

    // ===================== TEMEL İŞLEVLER =====================

    public List<User> getAllUsers() {
        return userDAO.getAll();
    }

    public void listAllUsers() {
        List<User> users = getAllUsers();
        if (users == null || users.isEmpty()) {
            System.out.println("\nNo users found in the system.");
            return;
        }

        System.out.println("\n=== Users ===");
        System.out.printf("%-5s %-20s %-15s %-15s %-15s%n",
                "ID", "Username", "Role", "First Name", "Last Name");
        System.out.println("---------------------------------------------------------------------");

        for (User u : users) {
            System.out.printf(
                    "%-5d %-20s %-15s %-15s %-15s%n",
                    u.getId(),
                    u.getUsername(),
                    u.getRole() != null ? u.getRole().name() : "-",
                    u.getName() != null ? u.getName() : "-",
                    u.getSurname() != null ? u.getSurname() : "-"
            );
        }
    }

    // ===================== MANAGER: USER EKLEME =====================

    public void addUserInteractive(Scanner scanner) {
        System.out.println("\n=== Add / Employ New User ===");

        // Username – boş olmasın ve benzersiz olsun
        String username;
        while (true) {
            username = InputHelper.readLine(scanner, "Username: ");
            if (username == null || username.isBlank()) {
                System.out.println("Username cannot be empty.");
                continue;
            }
            User existing = userDAO.findByUsername(username.trim());
            if (existing != null) {
                System.out.println("This username is already taken. Please choose another one.");
                continue;
            }
            username = username.trim();
            break;
        }

        String firstName = InputHelper.readLine(scanner, "First name: ");
        String lastName  = InputHelper.readLine(scanner, "Last name: ");

        Role role = askRoleFromUser(scanner);

        String password = InputHelper.readLine(scanner, "Password: ");
        String passwordAgain = InputHelper.readLine(scanner, "Confirm password: ");
        if (!password.equals(passwordAgain)) {
            System.out.println("New password and confirmation do not match.");
            return;
        }
        if (password.length() < 4) {
            System.out.println("Password must be at least 4 characters long.");
            return;
        }

        String passwordHash = HashUtil.hash(password);

        User newUser = new User();
        newUser.setUsername(username);
        newUser.setName(firstName != null ? firstName.trim() : null);
        newUser.setSurname(lastName != null ? lastName.trim() : null);
        newUser.setRole(role);
        newUser.setPasswordHash(passwordHash);

        boolean ok = userDAO.insert(newUser);
        if (ok) {
            System.out.println("\n✓ User added/employed successfully. (ID=" + newUser.getId() + ")");
            if (undoManager != null) {
                undoManager.push(UndoAction.forUserInsert(userDAO, newUser.getId()));
            }
        } else {
            System.out.println("\n✗ Failed to add/employ user.");
        }
    }

    // ===================== MANAGER: USER GÜNCELLEME =====================

    public void updateUserInteractive(Scanner scanner) {
        System.out.println("\n=== Update Existing User ===");

        int userId = InputHelper.readIntInRange(scanner,
                "User ID to update (0 = cancel): ", 0, Integer.MAX_VALUE);
        if (userId == 0) {
            System.out.println("Update cancelled.");
            return;
        }

        User existing = userDAO.getById(userId);
        if (existing == null) {
            System.out.println("User not found with ID: " + userId);
            return;
        }

        // Undo için eski halin kopyasını alıyoruz
        User previousState = UndoAction.cloneUser(existing);

        System.out.println("\nCurrent user info:");
        System.out.printf("ID: %d | Username: %s | Name: %s %s | Role: %s%n",
                existing.getId(),
                existing.getUsername(),
                existing.getName(),
                existing.getSurname(),
                existing.getRole() != null ? existing.getRole().name() : "-"
        );

        System.out.println("\nLeave input empty to keep the current value.");

        String newUsername = InputHelper.readLine(scanner,
                "New username [" + existing.getUsername() + "]: ");
        if (newUsername != null && !newUsername.isBlank()) {
            newUsername = newUsername.trim();
            if (!newUsername.equals(existing.getUsername())) {
                User other = userDAO.findByUsername(newUsername);
                if (other != null && other.getId() != existing.getId()) {
                    System.out.println("This username is already in use by another user.");
                    return;
                }
                existing.setUsername(newUsername);
            }
        }

        String newFirstName = InputHelper.readLine(scanner,
                "New first name [" + (existing.getName() == null ? "" : existing.getName()) + "]: ");
        if (newFirstName != null && !newFirstName.isBlank()) {
            existing.setName(newFirstName.trim());
        }

        String newLastName = InputHelper.readLine(scanner,
                "New last name [" + (existing.getSurname() == null ? "" : existing.getSurname()) + "]: ");
        if (newLastName != null && !newLastName.isBlank()) {
            existing.setSurname(newLastName.trim());
        }

        System.out.println("Current role: " +
                (existing.getRole() != null ? existing.getRole().name() : "-"));
        String changeRole = InputHelper.readLine(scanner, "Change role? (y/N): ");
        if (changeRole.trim().equalsIgnoreCase("y")) {
            Role newRole = askRoleFromUser(scanner);
            existing.setRole(newRole);
        }

        boolean ok = userDAO.update(existing);
        if (ok) {
            System.out.println("\n✓ User updated successfully.");
            if (undoManager != null) {
                undoManager.push(UndoAction.forUserUpdate(userDAO, previousState));
            }
        } else {
            System.out.println("\n✗ Failed to update user.");
        }
    }

    // ===================== MANAGER: USER SİLME =====================

    public void deleteUserInteractive(Scanner scanner) {
        System.out.println("\n=== Delete / Fire Existing User ===");

        int userId = InputHelper.readIntInRange(scanner,
                "User ID to delete (0 = cancel): ", 0, Integer.MAX_VALUE);
        if (userId == 0) {
            System.out.println("Delete cancelled.");
            return;
        }

        User existing = userDAO.getById(userId);
        if (existing == null) {
            System.out.println("User not found with ID: " + userId);
            return;
        }

        // Undo için snapshot
        User snapshot = UndoAction.cloneUser(existing);

        System.out.println("\nUser to delete:");
        System.out.printf("ID: %d | Username: %s | Name: %s %s | Role: %s%n",
                existing.getId(),
                existing.getUsername(),
                existing.getName(),
                existing.getSurname(),
                existing.getRole() != null ? existing.getRole().name() : "-"
        );

        String confirm = InputHelper.readLine(scanner,
                "Are you sure you want to delete/fire this user? (y/N): ");
        if (!confirm.trim().equalsIgnoreCase("y")) {
            System.out.println("Delete cancelled.");
            return;
        }

        boolean ok = userDAO.delete(userId);
        if (ok) {
            System.out.println("\n✓ User deleted/fired successfully.");
            if (undoManager != null) {
                undoManager.push(UndoAction.forUserDelete(userDAO, snapshot));
            }
        } else {
            System.out.println("\n✗ Failed to delete/fire user.");
        }
    }

    // ===================== ŞİFRE DEĞİŞTİRME (BUSINESS LOGIC) =====================

    /**
     * Gerçek şifre değiştirme iş mantığı.
     * currentUser: oturumdaki kullanıcı (null ise hata)
     * oldPass: eski şifrenin düz hali
     * newPass: yeni şifrenin düz hali
     *
     * Hatalı durumlarda IllegalArgumentException, veritabanı sorunlarında IllegalStateException fırlatır.
     */
    public void changePassword(User currentUser, String oldPass, String newPass) {
        if (currentUser == null) {
            throw new IllegalArgumentException("Current user cannot be null.");
        }
        if (oldPass == null || oldPass.isBlank() ||
                newPass == null || newPass.isBlank()) {
            throw new IllegalArgumentException("Passwords cannot be empty.");
        }

        if (newPass.length() < 4) {
            throw new IllegalArgumentException("New password must be at least 4 characters long.");
        }

        // Veritabanından güncel kullanıcıyı çek
        User fromDb = userDAO.getById(currentUser.getId());
        if (fromDb == null) {
            throw new IllegalStateException("User not found in the database.");
        }

        String oldHash = fromDb.getPasswordHash();
        if (!HashUtil.verify(oldPass, oldHash)) {
            throw new IllegalArgumentException("Old password is incorrect.");
        }

        if (HashUtil.verify(newPass, oldHash)) {
            throw new IllegalArgumentException("New password must be different from the old password.");
        }

        String newHash = HashUtil.hash(newPass);
        boolean ok = userDAO.updatePassword(fromDb.getId(), newHash);
        if (!ok) {
            throw new IllegalStateException("Failed to update password in the database.");
        }

        // Bellekteki currentUser nesnesini de güncelle
        currentUser.setPasswordHash(newHash);

        // UNDO kaydı
        if (undoManager != null) {
            undoManager.push(
                    UndoAction.forPasswordChange(userDAO, fromDb.getId(), oldHash, newHash)
            );
        }
    }

    // ===================== ŞİFRE DEĞİŞTİRME (INTERACTIVE) =====================

    public void changeOwnPasswordInteractive(User currentUser, Scanner scanner) {
        System.out.println("\n=== Change Your Password ===");

        if (currentUser == null) {
            System.out.println("No logged in user.");
            return;
        }

        String oldPass = InputHelper.readLine(scanner, "Current password: ");
        String newPass = InputHelper.readLine(scanner, "New password: ");
        String newPassAgain = InputHelper.readLine(scanner, "Confirm new password: ");

        if (!newPass.equals(newPassAgain)) {
            System.out.println("New password and confirmation do not match.");
            return;
        }

        try {
            changePassword(currentUser, oldPass, newPass);
            System.out.println("Password changed successfully.");
        } catch (IllegalArgumentException e) {
            System.out.println("!!! " + e.getMessage());
        } catch (IllegalStateException e) {
            System.out.println("Failed to update password in the system.");
        }
    }

    // ===================== ROLE SEÇİM YARDIMCISI =====================

    private Role askRoleFromUser(Scanner scanner) {
        System.out.println("\nSelect role:");
        System.out.println("1 - TESTER");
        System.out.println("2 - JUNIOR_DEV");
        System.out.println("3 - SENIOR_DEV");
        System.out.println("4 - MANAGER");

        int choice = InputHelper.readIntInRange(scanner, "Choice: ", 1, 4);
        return switch (choice) {
            case 1 -> Role.TESTER;
            case 2 -> Role.JUNIOR_DEV;
            case 3 -> Role.SENIOR_DEV;
            case 4 -> Role.MANAGER;
            default -> Role.TESTER; // buraya düşmemesi lazım
        };
    }
}
