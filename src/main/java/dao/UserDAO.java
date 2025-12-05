package dao;

import model.Role;
import model.User;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    // --- TEMEL YARDIMCI METOTLAR ---

    private Role mapRoleFromDb(String dbRole) {
        if (dbRole == null) {
            return null;
        }
        return Role.valueOf(dbRole); 
    }

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

    // --- KULLANILACAK OPERASYONLAR ---

    /**
     * Login için: username'e göre kullanıcıyı bulur.
     * Bulamazsa null döner.
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
     * Manager menüsü için: tüm kullanıcıları döner.
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
     * ID ile tek kullanıcıyı getirir (update/delete ekranları için).
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
     * Username çakışmasın diye kontrol (yeni user eklerken).
     * excludeId != null ise, o ID'li kullanıcı hariç kontrol eder
     * (update ekranı için).
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
     * Yeni user ekleme (Manager → employ new user).
     * user içindeki passwordHash alanı zaten HASH edilmiş olmalı!
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
     * Manager → kullanıcı bilgilerini güncelle (username, name, surname, role).
     * Şifreyi değiştirmez.
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
     * Tüm roller için “change password” işlemi.
     * newPasswordHash parametresi HASH edilmiş string olmalı.
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
     * Manager → kullanıcı silme (fire existing user).
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
