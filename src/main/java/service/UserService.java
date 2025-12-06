package service;

import dao.UserDAO;
import model.Role;
import model.User;
import undo.UndoAction;
import undo.UndoManager;
import util.HashUtil;
import util.InputHelper;
import util.ConsoleColors;

import java.util.List;
import java.util.Scanner;

public class UserService {

    private final UserDAO userDAO;
    private final UndoManager undoManager;

    public UserService() {
        this(new UserDAO(), null);
    }

    // --------------------
    // Interactive wrappers
    // --------------------

    /**
     * Print a user list to console (menu-facing helper).
     */
    public void listAllUsersInteractive() {
        try {
            java.util.List<User> users = findAllUsers();
            System.out.printf("%5s | %-15s | %-15s | %-15s | %-10s\n", "ID", "Username", "First Name", "Last Name", "Role");
            System.out.println("---------------------------------------------------------------");
            for (User u : users) {
                System.out.printf("%5d | %-15s | %-15s | %-15s | %-10s\n",
                        u.getId(), u.getUsername(), u.getName(), u.getSurname(), u.getRole());
            }
        } catch (Exception e) {
            System.out.println(ConsoleColors.RED + "Failed to fetch users: " + e.getMessage() + ConsoleColors.RESET);
        }
    }

    /**
     * Interactive flow to create a new user (uses InputHelper and delegates to createUser()).
     */
    public void addUserInteractive(Scanner scanner) {
        System.out.println("\n=== Add New User ===");
        try {
            String username = InputHelper.readNonEmptyLine(scanner, "Username: ");
            String password = InputHelper.readNonEmptyLine(scanner, "Password: ");
            String firstName = InputHelper.readValidName(scanner, "First name: ");
            String lastName = InputHelper.readValidName(scanner, "Last name: ");

            System.out.println("Select role:");
            Role[] roles = Role.values();
            for (int i = 0; i < roles.length; i++) {
                System.out.printf("%d) %s\n", i + 1, roles[i]);
            }
            int r = InputHelper.readIntInRange(scanner, "Choice (1-" + roles.length + "): ", 1, roles.length);
            Role role = roles[r - 1];

            createUser(username, password, firstName, lastName, role);
            System.out.println(ConsoleColors.GREEN + "User created successfully." + ConsoleColors.RESET);
        } catch (IllegalArgumentException ex) {
            System.out.println(ConsoleColors.RED + ex.getMessage() + ConsoleColors.RESET);
        } catch (Exception ex) {
            System.out.println(ConsoleColors.RED + "Failed to create user: " + ex.getMessage() + ConsoleColors.RESET);
        }
    }

    /**
     * Interactive flow to update an existing user.
     */
    public void updateUserInteractive(Scanner scanner) {
        System.out.println("\n=== Update User ===");
        try {
            int uid = InputHelper.readInt(scanner, "User ID to update: ");
            User existing = findUserById(uid);
            if (existing == null) {
                System.out.println(ConsoleColors.RED + "User not found with ID: " + uid + ConsoleColors.RESET);
                return;
            }

            System.out.println("Leave a field empty to keep current value.");
            String newUsername = InputHelper.readLine(scanner, "New username (current: " + existing.getUsername() + "): ");
            String newFirst = InputHelper.readLine(scanner, "New first name (current: " + existing.getName() + "): ");
            String newLast = InputHelper.readLine(scanner, "New last name (current: " + existing.getSurname() + "): ");

            System.out.println("Select new role or press Enter to keep current (current: " + existing.getRole() + "):");
            Role[] roles = Role.values();
            for (int i = 0; i < roles.length; i++) {
                System.out.printf("%d) %s\n", i + 1, roles[i]);
            }
            String roleInput = InputHelper.readLine(scanner, "Choice (1-" + roles.length + "): ");
            Role newRole = null;
            if (!roleInput.isEmpty()) {
                try {
                    int idx = Integer.parseInt(roleInput.trim()) - 1;
                    if (idx >= 0 && idx < roles.length) newRole = roles[idx];
                } catch (NumberFormatException ignored) { }
            }

            if (newUsername != null && newUsername.trim().isEmpty()) newUsername = null;
            if (newFirst != null && newFirst.trim().isEmpty()) newFirst = null;
            if (newLast != null && newLast.trim().isEmpty()) newLast = null;

            updateUser(uid, newUsername, newFirst, newLast, newRole);
        } catch (IllegalArgumentException ex) {
            System.out.println(ConsoleColors.RED + ex.getMessage() + ConsoleColors.RESET);
        } catch (Exception ex) {
            System.out.println(ConsoleColors.RED + "Failed to update user: " + ex.getMessage() + ConsoleColors.RESET);
        }
    }

    /**
     * Interactive flow to delete a user (checks self-delete and confirms).
     */
    public void deleteUserInteractive(Scanner scanner, User currentUser) {
        System.out.println("\n=== Delete User ===");
        try {
            int uid = InputHelper.readInt(scanner, "User ID to delete: ");
            boolean confirm = InputHelper.readYesNo(scanner, "Are you sure you want to delete user ID " + uid + "?");
            if (!confirm) {
                System.out.println("Deletion cancelled.");
                return;
            }
            deleteUser(uid, currentUser != null ? currentUser.getId() : -1);
            System.out.println(ConsoleColors.GREEN + "User deleted successfully." + ConsoleColors.RESET);
        } catch (IllegalArgumentException ex) {
            System.out.println(ConsoleColors.RED + ex.getMessage() + ConsoleColors.RESET);
        } catch (Exception ex) {
            System.out.println(ConsoleColors.RED + "Failed to delete user: " + ex.getMessage() + ConsoleColors.RESET);
        }
    }

    public UserService(UserDAO userDAO) {
        this(userDAO, null);
    }

    public UserService(UndoManager undoManager) {
        this(new UserDAO(), undoManager);
    }

    public UserService(UserDAO userDAO, UndoManager undoManager) {
        this.userDAO = userDAO;
        this.undoManager = undoManager;
    }

    // --- Simple finders ---
    public User findUserById(int id) {
        return userDAO.getById(id);
    }

    public User findUserByUsername(String username) {
        return userDAO.findByUsername(username);
    }

    public List<User> findAllUsers() {
        return userDAO.getAll();
    }

    // --- Change password (non-interactive) ---
    /**
     * Low-level password change (no Scanner).
     * Verifies old password and updates DB if valid.
     */
    public void changePassword(User user, String oldPassword, String newPassword) {
        if (user == null) throw new IllegalArgumentException("User cannot be null.");
        if (oldPassword == null || newPassword == null) throw new IllegalArgumentException("Passwords cannot be null.");

        String currentHash = user.getPasswordHash();
        if (!HashUtil.verify(oldPassword, currentHash)) {
            throw new IllegalArgumentException("Old password is incorrect!");
        }

        String newHash = HashUtil.hash(newPassword);

        // persist
        boolean ok = userDAO.updatePassword(user.getId(), newHash);
        if (!ok) {
            throw new IllegalStateException("Database error: Failed to update password.");
        }

        // push undo: restore old hash if undo requested later
        if (undoManager != null) {
            undoManager.push(UndoAction.forPasswordChange(userDAO, user.getId(), currentHash, newHash));
        }

        // update in-memory
        user.setPasswordHash(newHash);
    }

    // Interactive helper used by menus
    public void changeOwnPasswordInteractive(User currentUser, Scanner scanner) {
        System.out.println(ConsoleColors.CYAN + "\n--- Change Password ---" + ConsoleColors.RESET);
        System.out.println(ConsoleColors.YELLOW + "Enter '0' at any time to cancel." + ConsoleColors.RESET);

        String oldPass = InputHelper.readNonEmptyLine(scanner, "Current Password (0 to cancel): ");
        if (oldPass.equals("0")) {
            System.out.println(ConsoleColors.YELLOW + "Password change cancelled." + ConsoleColors.RESET);
            return;
        }

        String newPass = InputHelper.readNonEmptyLine(scanner, "New Password (0 to cancel): ");
        if (newPass.equals("0")) {
            System.out.println(ConsoleColors.YELLOW + "Password change cancelled." + ConsoleColors.RESET);
            return;
        }

        try {
            changePassword(currentUser, oldPass, newPass);
            System.out.println(ConsoleColors.GREEN + "Password updated successfully." + ConsoleColors.RESET);
        } catch (Exception e) {
            System.out.println(ConsoleColors.RED + "Error: " + e.getMessage() + ConsoleColors.RESET);
        }
    }


    // --- Create user (with incoming validations) ---
    public void createUser(String username, String rawPassword,
                           String firstName, String lastName,
                           Role role) {
        // Validation
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty.");
        }
        if (!username.matches("^[A-Za-z0-9_.]+$")) {
            throw new IllegalArgumentException("Username contains invalid characters (Only letters, numbers, _, . allowed).");
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
        if (firstName != null && !firstName.matches("[\\p{L}çğıöşüÇĞİÖŞÜ '\\-]+")) {
            throw new IllegalArgumentException("First name must contain only letters.");
        }
        if (lastName != null && !lastName.matches("[\\p{L}çğıöşüÇĞİÖŞÜ '\\-]+")) {
            throw new IllegalArgumentException("Last name must contain only letters.");
        }

        String hash = HashUtil.hash(rawPassword);

        User user = new User(0, username.trim(), hash,
                firstName != null ? firstName.trim() : "",
                lastName != null ? lastName.trim() : "",
                role,
                java.time.LocalDateTime.now());

        boolean ok = userDAO.insert(user);
        if (!ok) {
            throw new IllegalStateException("Failed to create user in database.");
        }

        if (undoManager != null) {
            // user.getId() should be set by DAO.insert (generated keys)
            undoManager.push(UndoAction.forUserInsert(userDAO, user.getId()));
        }
    }

    // --- Update user (keep previousState for undo) ---
    public void updateUser(int userId, String newUsername,
                           String newFirstName, String newLastName,
                           Role newRole) {

        User existing = userDAO.getById(userId);
        if (existing == null) {
            throw new IllegalArgumentException("User not found with ID: " + userId);
        }

        // capture previous state
        User previousState = new User();
        previousState.setId(existing.getId());
        previousState.setUsername(existing.getUsername());
        previousState.setPasswordHash(existing.getPasswordHash());
        previousState.setName(existing.getName());
        previousState.setSurname(existing.getSurname());
        previousState.setRole(existing.getRole());
        previousState.setCreatedAt(existing.getCreatedAt());

        // Username Update + validation
        if (newUsername != null && !newUsername.trim().isEmpty()) {
            if (!newUsername.matches("^[A-Za-z0-9_.]+$")) {
                throw new IllegalArgumentException("Username format is invalid.");
            }
            if (userDAO.existsByUsername(newUsername, userId)) {
                throw new IllegalArgumentException("Username already exists: " + newUsername);
            }
            existing.setUsername(newUsername.trim());
        }

        // Name Validation & Update
        if (newFirstName != null) {
            if (!newFirstName.matches("[\\p{L}çğıöşüÇĞİÖŞÜ '\\-]+")) {
                throw new IllegalArgumentException("First name must contain only letters.");
            }
            existing.setName(newFirstName.trim());
        }

        if (newLastName != null) {
            if (!newLastName.matches("[\\p{L}çğıöşüÇĞİÖŞÜ '\\-]+")) {
                throw new IllegalArgumentException("Last name must contain only letters.");
            }
            existing.setSurname(newLastName.trim());
        }

        if (newRole != null) {
            existing.setRole(newRole);
        }

        boolean ok = userDAO.update(existing);
        if (!ok) {
            throw new IllegalStateException("Failed to update user.");
        }

        if (undoManager != null) {
            undoManager.push(UndoAction.forUserUpdate(userDAO, previousState));
        }

        System.out.println("\n✓ User updated successfully.");
    }

    // --- Delete user (snapshot before delete; prevent self-delete) ---
    public void deleteUser(int userId, int managerUserId) {
        if (userId == managerUserId) {
            throw new IllegalArgumentException("You cannot delete your own account.");
        }

        User toDelete = userDAO.getById(userId);
        if (toDelete == null) {
            throw new IllegalArgumentException("User not found with ID: " + userId);
        }

        boolean ok = userDAO.delete(userId);
        if (!ok) {
            throw new IllegalStateException("Delete operation failed or user not found.");
        }

        if (undoManager != null) {
            undoManager.push(UndoAction.forUserDelete(userDAO, toDelete));
        }
    }
}