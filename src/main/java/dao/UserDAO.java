package dao;

import model.Role;
import model.User;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) class that manages CRUD (Create, Read, Update, Delete)
 * operations on the 'users' table in the database.
 */

public class UserDAO {

      /**
     * Converts the role string in the database to the Role enum
     * @param dbRole Role value in the database
     * @return Role equivalent or null
     */
    private Role mapRoleFromDb(String dbRole) {
        if (dbRole == null) {
            return null;
        }
        return Role.valueOf(dbRole); 
    }

    /**
     * Converts the role string in the database to the Role enum
     * @param role enum Role value
     * @return String to be stored in the DB or null
     */

    private String mapRoleToDb(Role role) {
        if (role == null) {
            return null;
        }
        return role.name();
    }

    private User mapRow(ResultSet rs) throws SQLException {
        int id = rs.getInt("user_id");
        String username = rs.getString("username");
        String passwordHash = rs.getString("password_hash");
        String name = rs.getString("name");
        String surname = rs.getString("surname");
        String roleStr = rs.getString("role");
        Timestamp createdTs = rs.getTimestamp("created_at");

        Role role = mapRoleFromDb(roleStr);
        LocalDateTime createdAt = createdTs != null ? createdTs.toLocalDateTime() : null;

        return new User(id, username, passwordHash, name, surname, role, createdAt);
    }

    /**
     * Finds a user in the database by their username.
     * Typically used for Login operations.
     * @param username The username to search for.
     * @return The User object if found, otherwise null.
     */

    public User findByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";

        try {
            Connection conn = DatabaseConnection.getConnection();
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, username);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return mapRow(rs);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("findByUsername hata: " + e.getMessage());
        }
        return null;
    }

    /**
     * Lists all users from the database.
     * Used to display the user list in the Manager menu.
     * @return A list containing all users.
     */

    public List<User> getAll() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users ORDER BY user_id";

        try {
            Connection conn = DatabaseConnection.getConnection();
            try (PreparedStatement ps = conn.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    users.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("getAll hata: " + e.getMessage());
        }
        return users;
    }

    /**
     * Gets user with given ID (for update/delete screens).
     */
    public User getById(int userId) {
        String sql = "SELECT * FROM users WHERE user_id = ?";

        try {
            Connection conn = DatabaseConnection.getConnection();
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, userId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return mapRow(rs);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("getById hata: " + e.getMessage());
        }
        return null;
    }

    /**
     * Controlling due to user crashing (when adding a new user).
     * If excludeId != null, it checks excluding the user with that ID
     * (for update screen).
     */
    public boolean existsByUsername(String username, Integer excludeId) {
        String sql = "SELECT COUNT(*) FROM users WHERE username = ?";
        if (excludeId != null) {
            sql += " AND user_id <> ?";
        }

        try {
            Connection conn = DatabaseConnection.getConnection();
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, username);
                if (excludeId != null) {
                    ps.setInt(2, excludeId);
                }
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return rs.getInt(1) > 0;
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("existsByUsername hata: " + e.getMessage());
        }
        return false;
    }

    /**
     * New user insertion (Manager → employ new user).
     * The passwordHash field in the user should already be hashed.
     */
    public boolean insert(User user) {
        String sql = """
                INSERT INTO users (username, password_hash, name, surname, role)
                VALUES (?, ?, ?, ?, ?)
                """;

        try {
            Connection conn = DatabaseConnection.getConnection();
            try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

                ps.setString(1, user.getUsername());
                ps.setString(2, user.getPasswordHash());
                ps.setString(3, user.getName());
                ps.setString(4, user.getSurname());
                ps.setString(5, mapRoleToDb(user.getRole()));

                int affected = ps.executeUpdate();
                if (affected == 0) return false;

                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) {
                        user.setId(keys.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("insert hata: " + e.getMessage());
            return false;
        }
    }

    /**
     * Manager → update user information (username, name, surname, role).
     * Does not change the password.
     */
    public boolean update(User user) {
        String sql = """
                UPDATE users
                SET username = ?, name = ?, surname = ?, role = ?
                WHERE user_id = ?
                """;

        try {
            Connection conn = DatabaseConnection.getConnection();
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, user.getUsername());
                ps.setString(2, user.getName());
                ps.setString(3, user.getSurname());
                ps.setString(4, mapRoleToDb(user.getRole()));
                ps.setInt(5, user.getId());

                int affected = ps.executeUpdate();
                return affected > 0;
            }
        } catch (SQLException e) {
            System.err.println("update hata: " + e.getMessage());
            return false;
        }
    }

    /**
     * Change password operation for all roles.
     * The newPasswordHash parameter should be a HASHHed string.
     */
    public boolean updatePassword(int userId, String newPasswordHash) {
        String sql = "UPDATE users SET password_hash = ? WHERE user_id = ?";

        try {
            Connection conn = DatabaseConnection.getConnection();
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, newPasswordHash);
                ps.setInt(2, userId);

                int affected = ps.executeUpdate();
                return affected > 0;
            }
        } catch (SQLException e) {
            System.err.println("updatePassword hata: " + e.getMessage());
            return false;
        }
    }

    /**
     * Manager → delete user (fire existing user).
     */
    public boolean delete(int userId) {
        String sql = "DELETE FROM users WHERE user_id = ?";

        try {
            Connection conn = DatabaseConnection.getConnection();
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, userId);

                int affected = ps.executeUpdate();
                return affected > 0;
            }
        } catch (SQLException e) {
            System.err.println("delete hata: " + e.getMessage());
            return false;
        }
    }
}
