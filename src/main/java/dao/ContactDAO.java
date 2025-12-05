package dao;

import model.Contact;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ContactDAO {

    /**
     * Tüm contact kayıtlarını listeler.
     * Hiç kayıt yoksa boş liste döner.
     */
    public List<Contact> getAllContacts() {
        List<Contact> contacts = new ArrayList<>();
        String sql = "SELECT * FROM contacts";

        try {
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Contact contact = mapResultSetToContact(rs);
                contacts.add(contact);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return contacts;
    }

    /**
     * Verilen ID’ye sahip contact’ı döner.
     * Bulamazsa null döner.
     */
    public Contact getContactById(int id) {
        String sql = "SELECT * FROM contacts WHERE contact_id = ?";
        try {
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapResultSetToContact(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Yeni bir contact kaydı ekler.
     * Başarılıysa true, hata varsa false döner.
     */
    public boolean insertContact(Contact contact) {
        String sql = "INSERT INTO contacts " +
                "(first_name, last_name, nickname, phone_number, email, linkedin_url, birth_date) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try {
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, contact.getFirstName());
            ps.setString(2, contact.getLastName());
            ps.setString(3, contact.getNickname());
            ps.setString(4, contact.getPhoneNumber());
            ps.setString(5, contact.getEmail());
            ps.setString(6, contact.getLinkedinUrl());

            if (contact.getBirthDate() != null) {
                ps.setDate(7, Date.valueOf(contact.getBirthDate()));
            } else {
                ps.setNull(7, Types.DATE);
            }

            int affected = ps.executeUpdate();
            return affected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Var olan bir contact kaydını günceller.
     * contactId alanı dolu olmalı.
     */
    public boolean updateContact(Contact contact) {
        String sql = "UPDATE contacts SET " +
                "first_name = ?, " +
                "last_name = ?, " +
                "nickname = ?, " +
                "phone_number = ?, " +
                "email = ?, " +
                "linkedin_url = ?, " +
                "birth_date = ? " +
                "WHERE contact_id = ?";

        try {
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, contact.getFirstName());
            ps.setString(2, contact.getLastName());
            ps.setString(3, contact.getNickname());
            ps.setString(4, contact.getPhoneNumber());
            ps.setString(5, contact.getEmail());
            ps.setString(6, contact.getLinkedinUrl());

            if (contact.getBirthDate() != null) {
                ps.setDate(7, Date.valueOf(contact.getBirthDate()));
            } else {
                ps.setNull(7, Types.DATE);
            }

            ps.setInt(8, contact.getContactId());

            int affected = ps.executeUpdate();
            return affected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Verilen ID’ye göre contact siler.
     * En az bir satır silinirse true döner.
     */
    public boolean deleteContact(int id) {
        String sql = "DELETE FROM contacts WHERE contact_id = ?";
        try {
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);

            int affected = ps.executeUpdate();
            return affected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // =====================================================
    // SINGLE-FIELD SEARCH
    // =====================================================

    /**
     * İsme göre arama yapar (first_name LIKE %query%).
     * Türkçe karakter desteği ile.
     */
    public List<Contact> searchByFirstName(String query) {
        String sql = "SELECT * FROM contacts WHERE first_name LIKE ? COLLATE utf8mb4_unicode_ci";
        return searchListWithSingleLike(sql, query);
    }

    /**
     * Soyisme göre arama yapar (last_name LIKE %query%).
     * Türkçe karakter desteği ile.
     */
    public List<Contact> searchByLastName(String query) {
        String sql = "SELECT * FROM contacts WHERE last_name LIKE ? COLLATE utf8mb4_unicode_ci";
        return searchListWithSingleLike(sql, query);
    }

    /**
     * Telefon numarasına göre arama yapar (phone_number LIKE %digits%).
     */
    public List<Contact> searchByPhoneContains(String digits) {
        String sql = "SELECT * FROM contacts WHERE phone_number LIKE ?";
        return searchListWithSingleLike(sql, digits);
    }

    // =====================================================
    // MULTI-FIELD SEARCH
    // =====================================================

    /**
     * İsim + doğum ayına göre arama yapar.
     * first_name LIKE %name% AND MONTH(birth_date) = month.
     */
    public List<Contact> searchByFirstNameAndBirthMonth(String namePart, int month) {
        List<Contact> results = new ArrayList<>();
        String sql = "SELECT * FROM contacts " +
                "WHERE first_name LIKE ? COLLATE utf8mb4_unicode_ci AND birth_date IS NOT NULL " +
                "AND MONTH(birth_date) = ?";

        try {
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, "%" + namePart + "%");
            ps.setInt(2, month);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                results.add(mapResultSetToContact(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return results;
    }

    /**
     * Telefon ve email substring’ine göre arama yapar.
     * phone_number LIKE %phonePart% AND email LIKE %emailPart%.
     */
    public List<Contact> searchByPhoneAndEmailContains(String phonePart, String emailPart) {
        List<Contact> results = new ArrayList<>();
        String sql = "SELECT * FROM contacts " +
                "WHERE phone_number LIKE ? AND email LIKE ? COLLATE utf8mb4_unicode_ci";

        try {
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, "%" + phonePart + "%");
            ps.setString(2, "%" + emailPart + "%");

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                results.add(mapResultSetToContact(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return results;
    }

    /**
     * İsim + soyisme birlikte bakarak arama yapar.
     * first_name LIKE %first% AND last_name LIKE %last%.
     */
    public List<Contact> searchByFirstAndLastName(String firstPart, String lastPart) {
        List<Contact> results = new ArrayList<>();
        String sql = "SELECT * FROM contacts " +
                "WHERE first_name LIKE ? COLLATE utf8mb4_unicode_ci AND last_name LIKE ? COLLATE utf8mb4_unicode_ci";

        try {
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, "%" + firstPart + "%");
            ps.setString(2, "%" + lastPart + "%");

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                results.add(mapResultSetToContact(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return results;
    }

    // =====================================================
    // SORT
    // =====================================================

    /**
     * Tüm contact’ları seçilen alana göre sıralı döner.
     * sortField: "first_name", "last_name", "phone", "birth_date", "created_at" gibi.
     * Sadece izin verdiğimiz alanlar kullanılır, yoksa contact_id kullanılır.
     */
    public List<Contact> getAllSorted(String sortField, boolean ascending) {
        String column;

        switch (sortField) {
            case "first_name":
            case "firstName":
                column = "first_name";
                break;
            case "last_name":
            case "lastName":
                column = "last_name";
                break;
            case "phone":
            case "phone_number":
                column = "phone_number";
                break;
            case "birth_date":
            case "birthDate":
                column = "birth_date";
                break;
            case "created_at":
                column = "created_at";
                break;
            default:
                column = "contact_id";
        }

        String direction = ascending ? "ASC" : "DESC";
        String sql = "SELECT * FROM contacts ORDER BY " + column + " " + direction;

        List<Contact> contacts = new ArrayList<>();

        try {
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                contacts.add(mapResultSetToContact(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return contacts;
    }

    // =====================================================
    // STATISTICS
    // =====================================================

    /**
     * Tüm contact’lar için ortalama yaşı hesaplar.
     * Doğum tarihi olmayanları hesaba katmaz.
     */
    public double getAverageAge() {
        String sql = "SELECT AVG(TIMESTAMPDIFF(YEAR, birth_date, CURDATE())) AS avg_age " +
                "FROM contacts WHERE birth_date IS NOT NULL";

        try {
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getDouble("avg_age");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    /**
     * En genç contact kaydını döner (birth_date en büyük olan).
     */
    public Contact getYoungestContact() {
        String sql = "SELECT * FROM contacts " +
                "WHERE birth_date IS NOT NULL " +
                "ORDER BY birth_date DESC LIMIT 1";
        try {
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapResultSetToContact(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * En yaşlı contact kaydını döner (birth_date en küçük olan).
     */
    public Contact getOldestContact() {
        String sql = "SELECT * FROM contacts " +
                "WHERE birth_date IS NOT NULL " +
                "ORDER BY birth_date ASC LIMIT 1";
        try {
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapResultSetToContact(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * LinkedIn URL’i dolu olan kişi sayısını döner.
     */
    public int countWithLinkedin() {
        String sql = "SELECT COUNT(*) AS cnt FROM contacts " +
                "WHERE linkedin_url IS NOT NULL AND linkedin_url <> ''";
        try {
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("cnt");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * LinkedIn URL’i boş olan kişi sayısını döner.
     */
    public int countWithoutLinkedin() {
        String sql = "SELECT COUNT(*) AS cnt FROM contacts " +
                "WHERE linkedin_url IS NULL OR linkedin_url = ''";
        try {
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("cnt");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Verilen isimde kaç kişi olduğunu döner.
     * Tam eşleşme yapar (LIKE değil, =).
     */
    public int countByFirstName(String firstName) {
        String sql = "SELECT COUNT(*) AS cnt FROM contacts WHERE first_name = ?";
        try {
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, firstName);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("cnt");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // =====================================================
    // PRIVATE HELPER METOTLAR
    // =====================================================

    private List<Contact> searchListWithSingleLike(String sql, String value) {
        List<Contact> results = new ArrayList<>();
        try {
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, "%" + value + "%");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                results.add(mapResultSetToContact(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }

    private Contact mapResultSetToContact(ResultSet rs) throws SQLException {
        Contact c = new Contact();
        c.setContactId(rs.getInt("contact_id"));
        c.setFirstName(rs.getString("first_name"));
        c.setLastName(rs.getString("last_name"));
        c.setNickname(rs.getString("nickname"));
        c.setPhoneNumber(rs.getString("phone_number"));
        c.setEmail(rs.getString("email"));
        c.setLinkedinUrl(rs.getString("linkedin_url"));

        Date birth = rs.getDate("birth_date");
        if (birth != null) {
            c.setBirthDate(birth.toLocalDate());
        } else {
            c.setBirthDate(null);
        }

        return c;
    }
}
