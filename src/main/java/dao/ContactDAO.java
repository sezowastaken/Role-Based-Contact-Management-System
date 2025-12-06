package dao;

import model.Contact;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Data Access Object (DAO) class for Contact entities.
 * Provides methods to perform CRUD operations, search, sort, and statistical queries
 * on the contacts table in the database.
 */
public class ContactDAO {

    /**
     * Retrieves all contacts from the database.
     * @return List of all contacts.
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
     * Retrieves a contact by its ID.
     * @param id the contact ID to search for
     * @return Contact object.
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
     * Inserts a new contact record into the database.
     * @param contact the Contact object to insert
     * @return true if successful, false otherwise.
     */
    public boolean insertContact(Contact contact) {
        String sql = "INSERT INTO contacts " +
                "(first_name, last_name, nickname, phone_number, email, linkedin_url, birth_date) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try {
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS);
            String firstName = normalizeName(contact.getFirstName());
            String lastName = normalizeName(contact.getLastName());

            ps.setString(1, firstName);
            ps.setString(2, lastName);
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
            if (affected == 0) {
                return false;
            }

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    contact.setContactId(keys.getInt(1));
                }
            }

            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Updates an existing contact record in the database.
     * @param contact the Contact object with updated information
     * @return true if successful, false otherwise
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

            String firstName = normalizeName(contact.getFirstName());
            String lastName = normalizeName(contact.getLastName());

            ps.setString(1, firstName);
            ps.setString(2, lastName);
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
     * Normalizes a name field: first letter uppercase, rest lowercase.
     * Examples: "aHMeT" -> "Ahmet", "YILMAZ" -> "Yilmaz"
     * @param name the name string to normalize
     * @return normalized name string, or null if input is null
     */
    private String normalizeName(String name) {
        if (name == null) {
            return null;
        }

        name = name.trim();
        if (name.isEmpty()) {
            return name;
        }

        if (name.length() == 1) {
            return name.toUpperCase();
        }

        String firstChar = name.substring(0, 1).toUpperCase();
        String rest = name.substring(1).toLowerCase();
        return firstChar + rest;
    }

    /**
     * Deletes a contact from the database by its ID.
     * @param id the contact ID to delete
     * @return true if at least one row was deleted, false otherwise
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

    /**
     * Silinmiş bir contact kaydını, eski contact_id değeri ile birlikte geri yüklemek için kullanılır.
     * Özellikle UNDO işlemlerinde çağrılır.
     *
     * NOT:
     *  - Burada contact.getContactId() veritabanındaki contact_id kolonu olarak kullanılır.
     */
    public boolean restoreContact(Contact contact) {
        if (contact == null) {
            return false;
        }

        String sql = "INSERT INTO contacts " +
                "(contact_id, first_name, last_name, nickname, phone_number, email, linkedin_url, birth_date) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, contact.getContactId());
            ps.setString(2, contact.getFirstName());
            ps.setString(3, contact.getLastName());
            ps.setString(4, contact.getNickname());
            ps.setString(5, contact.getPhoneNumber());
            ps.setString(6, contact.getEmail());
            ps.setString(7, contact.getLinkedinUrl());

            if (contact.getBirthDate() != null) {
                ps.setDate(8, Date.valueOf(contact.getBirthDate()));
            } else {
                ps.setNull(8, Types.DATE);
            }

            int affected = ps.executeUpdate();
            return affected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * Searches contacts by first name and last name using LIKE pattern matching.
     * Supports Turkish character case-insensitive search.
     * @param query the search string (partial match supported)
     * @return List of matching contacts
     */
    public List<Contact> searchByFirstName(String query) {
        String sql = "SELECT * FROM contacts WHERE first_name LIKE ? COLLATE utf8mb4_unicode_ci";
        return searchListWithSingleLike(sql, query);
    }

    public List<Contact> searchByLastName(String query) {
        String sql = "SELECT * FROM contacts WHERE last_name LIKE ? COLLATE utf8mb4_unicode_ci";
        return searchListWithSingleLike(sql, query);
    }

    /**
     * Searches contacts by phone number using LIKE pattern matching.
     * @param digits the phone number substring to search for
     * @return List of matching contacts
     */
    public List<Contact> searchByPhoneContains(String digits) {
        String sql = "SELECT * FROM contacts WHERE phone_number LIKE ?";
        return searchListWithSingleLike(sql, digits);
    }

    /**
     * Searches contacts by birth month only.
     * @param month the birth month (1-12)
     * @return List of matching contacts
     */
    public List<Contact> searchByBirthMonth(int month) {
        List<Contact> results = new ArrayList<>();
        String sql = "SELECT * FROM contacts WHERE birth_date IS NOT NULL AND MONTH(birth_date) = ?";

        try {
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, month);

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
     * Searches contacts by birth year only.
     * @param year the birth year
     * @return List of matching contacts
     */
    public List<Contact> searchByBirthYear(int year) {
        List<Contact> results = new ArrayList<>();
        String sql = "SELECT * FROM contacts WHERE birth_date IS NOT NULL AND YEAR(birth_date) = ?";

        try {
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, year);

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
     * Searches contacts by first name and birth month.
     * Matches first_name LIKE pattern AND birth month equals specified month.
     * @param namePart the first name substring to search for
     * @param month the birth month (1-12)
     * @return List of matching contacts
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
     * Searches contacts by phone number and email using LIKE pattern matching.
     * Both conditions must match.
     * @param phonePart the phone number substring to search for
     * @param emailPart the email substring to search for
     * @return List of matching contacts
     */
    public List<Contact> searchByPhonePrefixAndBirthYear(String phonePrefix, int year) {
        List<Contact> results = new ArrayList<>();
        String sql = "SELECT * FROM contacts WHERE phone_number LIKE ? AND YEAR(birth_date) = ?";

        try {
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, phonePrefix + "%");
            ps.setInt(2, year);

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
     * Searches contacts by both first name and last name using LIKE pattern matching.
     * @param firstPart the first name substring to search for
     * @param lastPart the last name substring to search for
     * @return List of matching contacts
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

    /**
     * Returns all contacts sorted by the specified field.
     * Supported fields: "first_name", "last_name", "phone", "birth_date", "created_at", "age".
     * @param sortField the field name to sort by
     * @param ascending true for ascending order, false for descending
     * @return List of contacts sorted by the specified field
     */
    public List<Contact> getAllSorted(String sortField, boolean ascending) {
        
        if (sortField.equals("age") || sortField.equals("Age")) {
            return getAllSortedByAge(ascending);
        }

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

    /**
     * Returns all contacts sorted by age (calculated from birth_date).
     * Contacts without birth_date are placed at the end of the list.
     * @param ascending true for youngest first, false for oldest first
     * @return List of contacts sorted by age
     */
    private List<Contact> getAllSortedByAge(boolean ascending) {
        List<Contact> contacts = new ArrayList<>();
        String direction = ascending ? "DESC" : "ASC";
        String sql = "SELECT * FROM contacts " +
                "ORDER BY " +
                "CASE WHEN birth_date IS NULL THEN 1 ELSE 0 END, " + // NULLs go to end
                "birth_date " + direction;

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

    /**
     * Calculates the average age of all contacts.
     * Only considers contacts with a valid birth date.
     * @return average age as a double, 0.0 if no contacts with birth date found
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
     * Returns the youngest and oldest contact.
     * @return youngest Contact object, null if no contacts with birth date found
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
     * Returns the count of contacts that have and not have a non-empty Linkedin URL.
     * @return number of contacts with Linkedin URL
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
     * Returns the count of contacts with the specified first name.
     * (case-sensitive)
     * @param firstName the first name to count
     * @return number of contacts with the given first name
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

    /**
     * Returns a map of all first names and their occurrence counts.
     * Shows how many contacts share each first name.
     * @return LinkedHashMap with first names as keys and counts as values,
     *         ordered by count descending, then by first name ascending
     */
    public Map<String, Integer> getAllFirstNameCounts() {
        Map<String, Integer> nameCounts = new LinkedHashMap<>();
        String sql = "SELECT first_name, COUNT(*) AS cnt FROM contacts GROUP BY first_name ORDER BY cnt DESC, first_name ASC";
        try {
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String firstName = rs.getString("first_name");
                int count = rs.getInt("cnt");
                nameCounts.put(firstName, count);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return nameCounts;
    }

    /**
     * Returns the total number of contacts in the database.
     * @return total contact count
     */
    public int getTotalContactCount() {
        String sql = "SELECT COUNT(*) AS cnt FROM contacts";
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
     * Returns a map showing the distribution of birth months.
     * @return LinkedHashMap with month names as keys and counts as values,
     *         ordered by count descending, then by month number ascending
     */
    public Map<String, Integer> getBirthMonthDistribution() {
        Map<String, Integer> monthCounts = new LinkedHashMap<>();
        String sql = "SELECT MONTH(birth_date) AS month_num, COUNT(*) AS cnt " +
                "FROM contacts WHERE birth_date IS NOT NULL " +
                "GROUP BY MONTH(birth_date) ORDER BY cnt DESC, month_num ASC";

        String[] monthNames = { "", "January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December" };

        try {
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int monthNum = rs.getInt("month_num");
                int count = rs.getInt("cnt");
                if (monthNum >= 1 && monthNum <= 12) {
                    monthCounts.put(monthNames[monthNum], count);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return monthCounts;
    }

    /**
     * Returns a map showing the distribution of contacts by age groups.
     * Only considers contacts with valid birth date information.
     * @return LinkedHashMap with age group names as keys and counts as values,
     *         ordered by age group
     */
    public Map<String, Integer> getAgeGroupDistribution() {
        Map<String, Integer> ageGroups = new LinkedHashMap<>();
        ageGroups.put("0-18", 0);
        ageGroups.put("19-30", 0);
        ageGroups.put("31-50", 0);
        ageGroups.put("50+", 0);

        String sql = "SELECT birth_date FROM contacts WHERE birth_date IS NOT NULL";
        try {
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            LocalDate today = LocalDate.now();
            while (rs.next()) {
                Date birthDate = rs.getDate("birth_date");
                if (birthDate != null) {
                    LocalDate birth = birthDate.toLocalDate();
                    int age = Period.between(birth, today).getYears();

                    if (age >= 0 && age <= 18) {
                        ageGroups.put("0-18", ageGroups.get("0-18") + 1);
                    } else if (age >= 19 && age <= 30) {
                        ageGroups.put("19-30", ageGroups.get("19-30") + 1);
                    } else if (age >= 31 && age <= 50) {
                        ageGroups.put("31-50", ageGroups.get("31-50") + 1);
                    } else if (age > 50) {
                        ageGroups.put("50+", ageGroups.get("50+") + 1);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ageGroups;
    }

    /**
     * Helper method to execute a single LIKE search query.
     * @param sql the SQL query with one LIKE parameter
     * @param value the search value to match
     * @return List of matching contacts
     */
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

    /**
     * Maps a ResultSet row to a Contact object.
     * @param rs the ResultSet containing contact data
     * @return Contact object created from the ResultSet
     * @throws SQLException if database access error occurs
     */
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
