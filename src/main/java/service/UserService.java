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
    String usernamePattern = "^[a-zA-Z0-9çÇğĞıİöÖşŞüÜ\\s\\-]+$";

    public UserService() {
        this(new UserDAO(), null);
    }

    // --------------------
    // Interactive wrappers (UI + 0 Cancel Support)
    // --------------------

    /**
     * Print a user list to console.
     */
    public void listAllUsersInteractive() {
        try {
            java.util.List<User> users = findAllUsers();
            System.out.printf(ConsoleColors.YELLOW + "%5s | %-15s | %-15s | %-15s | %-10s\n" + ConsoleColors.RESET,
                    "ID", "Username", "First Name", "Last Name", "Role");
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
     * Interactive flow to create a new user (0 to cancel).
     */
    public void addUserInteractive(Scanner scanner) {
        System.out.println(ConsoleColors.CYAN + "\n=== Add New User ===" + ConsoleColors.RESET);
        System.out.println(ConsoleColors.YELLOW + "(Enter '0' at any step to cancel)" + ConsoleColors.RESET);

        try {
            String username = InputHelper.readValidNickname(scanner, ConsoleColors.WHITE + "Username: ");
            if (username.equals("0"))
                return;

            String password = InputHelper.readNonEmptyLine(scanner, "Password: ");
            if (password.equals("0"))
                return;

            String firstName = InputHelper.readValidName(scanner, "First name: ");
            if (firstName.equals("0"))
                return;

            String lastName = InputHelper.readValidName(scanner, "Last name: " + ConsoleColors.RESET);
            if (lastName.equals("0"))
                return;

            System.out.println(ConsoleColors.YELLOW + "Select role (0 to cancel):" + ConsoleColors.RESET);
            Role[] roles = Role.values();
            for (int i = 0; i < roles.length; i++) {
                System.out.printf("%d) %s\n", i + 1, roles[i]);
            }
            int r = InputHelper.readIntInRange(scanner, "Choice (0-" + roles.length + "): ", 0, roles.length);
            if (r == 0)
                return;

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
     * Interactive flow to update an existing user (0 to cancel).
     */
    public void updateUserInteractive(Scanner scanner) {
        System.out.println(ConsoleColors.CYAN + "\n=== Update User ===" + ConsoleColors.RESET);
        listAllUsersInteractive();

        try {
            int uid = InputHelper.readInt(scanner,
                    ConsoleColors.WHITE + "User ID to update (0 to cancel): " + ConsoleColors.RESET);
            if (uid == 0)
                return;

            User existing = findUserById(uid);
            if (existing == null) {
                System.out.println(ConsoleColors.RED + "User not found with ID: " + uid + ConsoleColors.RESET);
                return;
            }

            System.out.println(ConsoleColors.BLUE + "Updating: " + existing.getName() + " (" + existing.getUsername()
                    + ")" + ConsoleColors.RESET);
            System.out.println(ConsoleColors.YELLOW + "Leave empty to keep current. Enter '0' to cancel operation."
                    + ConsoleColors.RESET);

            String newUsername = InputHelper.readLine(scanner,
                    "New username (current: " + existing.getUsername() + "): ");
            if (newUsername.equals("0"))
                return;

            String newFirst = InputHelper.readValidName(scanner, "New first name (current: " + existing.getName() + "): ");
            if (newFirst.equals("0"))
                return;

            String newLast = InputHelper.readValidName(scanner, "New last name (current: " + existing.getSurname() + "): ");
            if (newLast.equals("0"))
                return;

            System.out.println(ConsoleColors.YELLOW + "Select new role or press Enter to keep current (current: "
                    + existing.getRole() + "):" + ConsoleColors.RESET);
            Role[] roles = Role.values();
            for (int i = 0; i < roles.length; i++) {
                System.out.printf("%d) %s\n", i + 1, roles[i]);
            }
            String roleInput = InputHelper.readLine(scanner, "Choice (1-" + roles.length + ") or 0 to cancel: ");
            if (roleInput.equals("0"))
                return;

            Role newRole = null;
            if (!roleInput.isEmpty()) {
                try {
                    int idx = Integer.parseInt(roleInput.trim()) - 1;
                    if (idx >= 0 && idx < roles.length)
                        newRole = roles[idx];
                } catch (NumberFormatException ignored) {
                }
            }

            if (newUsername != null && newUsername.trim().isEmpty())
                newUsername = null;
            if (newFirst != null && newFirst.trim().isEmpty())
                newFirst = null;
            if (newLast != null && newLast.trim().isEmpty())
                newLast = null;

            updateUser(uid, newUsername, newFirst, newLast, newRole);
        } catch (IllegalArgumentException ex) {
            System.out.println(ConsoleColors.RED + ex.getMessage() + ConsoleColors.RESET);
        } catch (Exception ex) {
            System.out.println(ConsoleColors.RED + "Failed to update user: " + ex.getMessage() + ConsoleColors.RESET);
        }
    }

    /**
     * Interactive flow to delete a user (0 to cancel).
     */
    public void deleteUserInteractive(Scanner scanner, User currentUser) {
        System.out.println(ConsoleColors.RED + "\n=== Delete User ===" + ConsoleColors.RESET);
        listAllUsersInteractive();

        try {
            int uid = InputHelper.readInt(scanner, "User ID to delete (0 to cancel): ");
            if (uid == 0)
                return;

            if (uid == currentUser.getId()) {
                System.out.println(ConsoleColors.RED + "You cannot delete your own account!" + ConsoleColors.RESET);
                return;
            }

            boolean confirm = InputHelper.readYesNo(scanner,
                    ConsoleColors.RED + "Are you sure you want to delete user ID " + uid + "?" + ConsoleColors.RESET);
            if (!confirm) {
                System.out.println(ConsoleColors.YELLOW + "Deletion cancelled." + ConsoleColors.RESET);
                return;
            }

            deleteUser(uid, currentUser.getId());
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
    public void changePassword(User user, String oldPassword, String newPassword) {
        if (user == null)
            throw new IllegalArgumentException(ConsoleColors.RED + "User cannot be null.");
        if (oldPassword == null || newPassword == null)
            throw new IllegalArgumentException("Passwords cannot be null.");

        String currentHash = user.getPasswordHash();
        if (!HashUtil.verify(oldPassword, currentHash)) {
            throw new IllegalArgumentException("Old password is incorrect!");
        }

        String newHash = HashUtil.hash(newPassword);

        boolean ok = userDAO.updatePassword(user.getId(), newHash);
        if (!ok) {
            throw new IllegalStateException("Database error: Failed to update password." + ConsoleColors.RESET);
        }

        if (undoManager != null) {
            undoManager.push(UndoAction.forPasswordChange(userDAO, user.getId(), currentHash, newHash));
        }

        user.setPasswordHash(newHash);
    }

    // Interactive helper used by menus (0 Cancel Added)
    public void changeOwnPasswordInteractive(User currentUser, Scanner scanner) {
        System.out.println(ConsoleColors.BLUE + "\n=== Change Password ===" + ConsoleColors.RESET);
        System.out.println(ConsoleColors.YELLOW + "(Enter '0' to cancel)" + ConsoleColors.RESET);

        if (currentUser == null) {
            System.out
                    .println(ConsoleColors.RED + "Current user is null, cannot change password." + ConsoleColors.RESET);
            return;
        }

        String oldPass = InputHelper.readNonEmptyLine(scanner, "Current password: ");
        if (oldPass.equals("0"))
            return;

        String newPass = InputHelper.readNonEmptyLine(scanner, "New password: ");
        if (newPass.equals("0"))
            return;

        String confirm = InputHelper.readNonEmptyLine(scanner, "Confirm new password: ");
        if (confirm.equals("0"))
            return;

        if (!newPass.equals(confirm)) {
            System.out.println(ConsoleColors.RED + "New password and confirmation do not match." + ConsoleColors.RESET);
            return;
        }

        try {
            changePassword(currentUser, oldPass, newPass);
            System.out.println(ConsoleColors.GREEN + "Password changed successfully." + ConsoleColors.RESET);
        } catch (IllegalArgumentException e) {
            System.out.println(ConsoleColors.RED + "Error: " + e.getMessage() + ConsoleColors.RESET);
        } catch (IllegalStateException e) {
            System.out.println(ConsoleColors.RED + "Failed to update password in the system." + ConsoleColors.RESET);
        }
    }

    // --- Create user (Validation) ---
    public void createUser(String username, String rawPassword,
            String firstName, String lastName,
            Role role) {
        if (username == null || username.trim().isEmpty())
            throw new IllegalArgumentException(ConsoleColors.RED + "Username cannot be empty." + ConsoleColors.RESET);
        if (!username.matches(usernamePattern))
            throw new IllegalArgumentException(ConsoleColors.RED +
                    "Username contains invalid characters (Only letters, numbers, _, . allowed)."
                    + ConsoleColors.RESET);
        if (rawPassword == null || rawPassword.isEmpty())
            throw new IllegalArgumentException(ConsoleColors.RED + "Password cannot be empty.");
        if (role == null)
            throw new IllegalArgumentException("Role cannot be null." + ConsoleColors.RESET);

        if (userDAO.existsByUsername(username, null))
            throw new IllegalArgumentException(
                    ConsoleColors.RED + "Username already exists: " + username + ConsoleColors.RESET);

        if (firstName != null && !firstName.matches("[\\p{L}çğıöşüÇĞİÖŞÜ '\\-]+"))
            throw new IllegalArgumentException(
                    ConsoleColors.RED + "First name must contain only letters." + ConsoleColors.RESET);
        if (lastName != null && !lastName.matches("[\\p{L}çğıöşüÇĞİÖŞÜ '\\-]+"))
            throw new IllegalArgumentException(
                    ConsoleColors.RED + "Last name must contain only letters." + ConsoleColors.RESET);

        String hash = HashUtil.hash(rawPassword);

        User user = new User(0, username.trim(), hash,
                firstName != null ? firstName.trim() : "",
                lastName != null ? lastName.trim() : "",
                role,
                java.time.LocalDateTime.now());

        boolean ok = userDAO.insert(user);
        if (!ok)
            throw new IllegalStateException(
                    ConsoleColors.RED + "Failed to create user in database." + ConsoleColors.RESET);

        if (undoManager != null) {
            undoManager.push(UndoAction.forUserInsert(userDAO, user.getId()));
        }
    }

    // --- Update user (Validation + Undo) ---
    public void updateUser(int userId, String newUsername,
            String newFirstName, String newLastName,
            Role newRole) {

        User existing = userDAO.getById(userId);
        if (existing == null)
            throw new IllegalArgumentException(
                    ConsoleColors.RED + "User not found with ID: " + userId + ConsoleColors.RESET);

        User previousState = new User(existing.getId(), existing.getUsername(), existing.getPasswordHash(),
                existing.getName(), existing.getSurname(), existing.getRole(), existing.getCreatedAt());

        if (newUsername != null && !newUsername.trim().isEmpty()) {
            if (!newUsername.matches(usernamePattern))
                throw new IllegalArgumentException(
                        ConsoleColors.RED + "Username format is invalid." + ConsoleColors.RESET);
            if (userDAO.existsByUsername(newUsername, userId))
                throw new IllegalArgumentException(
                        ConsoleColors.RED + "Username already exists: " + newUsername + ConsoleColors.RESET);
            existing.setUsername(newUsername.trim());
        }

        if (newFirstName != null) {
            if (!newFirstName.matches("[\\p{L}çğıöşüÇĞİÖŞÜ '\\-]+"))
                throw new IllegalArgumentException(
                        ConsoleColors.YELLOW + "First name must contain only letters." + ConsoleColors.RESET);
            existing.setName(newFirstName.trim());
        }

        if (newLastName != null) {
            if (!newLastName.matches("[\\p{L}çğıöşüÇĞİÖŞÜ '\\-]+"))
                throw new IllegalArgumentException(
                        ConsoleColors.YELLOW + "Last name must contain only letters." + ConsoleColors.RESET);
            existing.setSurname(newLastName.trim());
        }

        if (newRole != null) {
            existing.setRole(newRole);
        }

        boolean ok = userDAO.update(existing);
        if (!ok)
            throw new IllegalStateException(ConsoleColors.RED + "Failed to update user." + ConsoleColors.RESET);

        if (undoManager != null) {
            undoManager.push(UndoAction.forUserUpdate(userDAO, previousState));
        }
        System.out.println(ConsoleColors.GREEN + "\nUser updated successfully." + ConsoleColors.RESET);
    }

    // --- Delete user (Validation + Undo) ---
    public void deleteUser(int userId, int managerUserId) {
        if (userId == managerUserId)
            throw new IllegalArgumentException(
                    ConsoleColors.RED + "You cannot delete your own account." + ConsoleColors.RESET);

        User toDelete = userDAO.getById(userId);
        if (toDelete == null)
            throw new IllegalArgumentException(ConsoleColors.RED + "User not found with ID: " + userId);

        boolean ok = userDAO.delete(userId);
        if (!ok)
            throw new IllegalStateException("Delete operation failed or user not found." + ConsoleColors.RESET);

        if (undoManager != null) {
            undoManager.push(UndoAction.forUserDelete(userDAO, toDelete));
        }
    }
}