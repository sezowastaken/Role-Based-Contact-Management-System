package service;

import dao.UserDAO;
import model.Role;
import model.User;
import util.HashUtil;

import java.util.List;

public class UserService {

    private final UserDAO userDAO;

    public UserService() {
        this.userDAO = new UserDAO();
    }

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public User findUserById(int id) {
        return userDAO.getById(id);
    }

    public User findUserByUsername(String username) {
        return userDAO.findByUsername(username);
    }

    public List<User> findAllUsers() {
        return userDAO.getAll();
    }

    /**
     * Handles password change logic.
     */
    public void changePassword(User user, String oldPassword, String newPassword) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null.");
        }
        if (oldPassword == null || newPassword == null) {
            throw new IllegalArgumentException("Passwords cannot be null.");
        }

        // 1. Eski şifreyi doğrula
        String currentHash = user.getPasswordHash();
        if (!HashUtil.verify(oldPassword, currentHash)) {
            throw new IllegalArgumentException("Old password is incorrect!");
        }

        // 2. Yeni şifreyi hashle
        String newHash = HashUtil.hash(newPassword);
        
        // 3. Veritabanını güncelle ([DÜZELTME] getUserId() -> getId())
        boolean ok = userDAO.updatePassword(user.getId(), newHash); 
        if (!ok) {
            throw new IllegalStateException("Database error: Failed to update password.");
        }

        // 4. Bellekteki objeyi güncelle
        user.setPasswordHash(newHash);
    }

    /**
     * Creates a new user (Used by Manager).
     */
    public void createUser(String username, String rawPassword,
                           String firstName, String lastName,
                           Role role) {
        
        // --- VALIDATION ---
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

        // --- DATABASE CHECK ---
        if (userDAO.existsByUsername(username, null)) {
            throw new IllegalArgumentException("Username already exists: " + username);
        }

        // --- NAME CHECK ---
        if (firstName != null && !firstName.matches("[\\p{L}çğıöşüÇĞİÖŞÜ '\\-]+")) {
            throw new IllegalArgumentException("First name must contain only letters.");
        }
        if (lastName != null && !lastName.matches("[\\p{L}çğıöşüÇĞİÖŞÜ '\\-]+")) {
            throw new IllegalArgumentException("Last name must contain only letters.");
        }

        // --- CREATE ---
        String hash = HashUtil.hash(rawPassword);
        
        // ID 0 veriyoruz (DB auto-increment)
        // User constructor'ına createdAt (LocalDateTime) eklendiği için null veya now() göndermeliyiz
        // Eğer User constructor'ın (int, String, String, String, String, Role, LocalDateTime) ise:
        User user = new User(0, username.trim(), hash, 
                             firstName != null ? firstName.trim() : "", 
                             lastName != null ? lastName.trim() : "", 
                             role, 
                             java.time.LocalDateTime.now());

        boolean ok = userDAO.insert(user);
        if (!ok) {
            throw new IllegalStateException("Failed to create user in database.");
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
            throw new IllegalArgumentException("User not found with ID: " + userId);
        }

        // Username Update
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
    }

    public void deleteUser(int userId, int managerUserId) {
        if (userId == managerUserId) {
            throw new IllegalArgumentException("You cannot delete your own account.");
        }
        
        boolean ok = userDAO.delete(userId);
        if (!ok) {
            throw new IllegalStateException("Delete operation failed or user not found.");
        }
    }
}