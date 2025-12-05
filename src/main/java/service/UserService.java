package service;

import dao.UserDAO;
import model.Role;
import model.User;
import util.HashUtil;
import util.InputHelper;

import java.util.List;
import java.util.Scanner;

public class UserService {

    private final UserDAO userDAO;

    // Convenience constructor
    public UserService() {
        this.userDAO = new UserDAO();
    }

    // Existing constructor (if you want to inject UserDAO manually)
    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    /**
     * Low-level password change (no Scanner).
     * Verifies old password and updates DB if valid.
     */
    public void changePassword(User user, String oldPassword, String newPassword) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null.");
        }
        if (oldPassword == null || newPassword == null) {
            throw new IllegalArgumentException("Passwords cannot be null.");
        }

        String currentHash = user.getPasswordHash();
        if (!HashUtil.verify(oldPassword, currentHash)) {
            throw new IllegalArgumentException("Current password is incorrect.");
        }

        String newHash = HashUtil.hash(newPassword);
        boolean ok = userDAO.updatePassword(user.getId(), newHash);
        if (!ok) {
            throw new IllegalStateException("Failed to update password in database.");
        }

        // update in-memory instance as well
        user.setPasswordHash(newHash);
    }

    /**
     * Creates a new user (for Manager).
     */
    public void createUser(String username, String rawPassword,
                           String firstName, String lastName,
                           Role role) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty.");
        }
        if (rawPassword == null || rawPassword.isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty.");
        }
        if (role == null) {
            throw new IllegalArgumentException("Role cannot be null.");
        }

        if (userDAO.existsByUsername(username, null)) {
            throw new IllegalArgumentException("Username already exists: " + username);
        }

        String hash = HashUtil.hash(rawPassword);

        User user = new User();
        user.setUsername(username.trim());
        user.setPasswordHash(hash);
        user.setName(firstName != null ? firstName.trim() : "");
        user.setSurname(lastName != null ? lastName.trim() : "");
        user.setRole(role);

        boolean ok = userDAO.insert(user);
        if (!ok) {
            throw new IllegalStateException("Failed to create user.");
        }
    }

    /**
     * Updates basic info of an existing user.
     */
    public void updateUser(int userId, String newUsername,
                           String newFirstName, String newLastName,
                           Role newRole) {
        User existing = userDAO.getById(userId);
        if (existing == null) {
            throw new IllegalArgumentException("User not found with id " + userId);
        }

        if (newUsername != null && !newUsername.trim().isEmpty()) {
            if (userDAO.existsByUsername(newUsername, userId)) {
                throw new IllegalArgumentException("Username already exists: " + newUsername);
            }
            existing.setUsername(newUsername.trim());
        }

        if (newFirstName != null) {
            existing.setName(newFirstName.trim());
        }
        if (newLastName != null) {
            existing.setSurname(newLastName.trim());
        }
        if (newRole != null) {
            existing.setRole(newRole);
        }

        boolean ok = userDAO.update(existing);
        if (!ok) {
            throw new IllegalStateException("Failed to update user.");
        }
    }

    /**
     * Deletes a user. managerUserId is kept for future access-control if needed.
     */
    public void deleteUser(int userId, int managerUserId) {
        boolean ok = userDAO.delete(userId);
        if (!ok) {
            throw new IllegalStateException("Delete operation failed!");
        }
    }

    public List<User> findAllUsers() {
        return userDAO.getAll();
    }

    public User findUserByUsername(String username) {
        return userDAO.findByUsername(username);
    }

    // ---------- INTERACTIVE (used by menus) ----------

    /**
     * Interactive "change my own password" flow for any logged-in user.
     * This is what Tester / Junior / Senior / Manager menus will call.
     */
    public void changeOwnPasswordInteractive(User currentUser, Scanner scanner) {
        System.out.println("\n=== Change Password ===");

        if (currentUser == null) {
            System.out.println("Current user is null, cannot change password.");
            return;
        }

        String oldPass = InputHelper.readNonEmptyLine(scanner, "Current password: ");
        String newPass = InputHelper.readNonEmptyLine(scanner, "New password: ");
        String confirm = InputHelper.readNonEmptyLine(scanner, "Confirm new password: ");

        if (!newPass.equals(confirm)) {
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
}
