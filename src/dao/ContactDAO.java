package dao;

import model.Contact;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ContactDAO {

    // Tüm contact’ları listele
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

    // ID’ye göre tek contact getir
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

    // Yeni contact ekle
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

    // Var olan contact’ı güncelle
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

    // ID’ye göre sil
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

    // -----------------------------
    // Helper: ResultSet -> Contact
    // -----------------------------
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
