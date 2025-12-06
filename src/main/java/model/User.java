package model;

import java.time.LocalDateTime;

/**
 * Represents a user in the system.
 * Base class for different user roles (Tester, Developer, Manager).
 * Stores user credentials and personal information.
 */
public class User {

    private int id;
    private String username;
    private String passwordHash;
    private String name;
    private String surname;
    private Role role;
    private LocalDateTime createdAt;

    /**
     * Default constructor.
     */
    public User() {
    }

    /**
     * Constructor with all fields.
     */
    public User(int id,
            String username,
            String passwordHash,
            String name,
            String surname,
            Role role,
            LocalDateTime createdAt) {
        this.id = id;
        this.username = username;
        this.passwordHash = passwordHash;
        this.name = name;
        this.surname = surname;
        this.role = role;
        this.createdAt = createdAt;
    }

    /**
     * Gets the user ID.
     * @return user ID
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the user ID.
     * @param id the user ID to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the username.
     * @return username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username.
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets the password hash.
     * @return password hash
     */
    public String getPasswordHash() {
        return passwordHash;
    }

    /**
     * Sets the password hash.
     * @param passwordHash the password hash to set
     */
    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    /**
     * Gets the name.
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name.
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the surname.
     * @return surname
     */
    public String getSurname() {
        return surname;
    }

    /**
     * Sets the surname.
     * @param surname the surname to set
     */
    public void setSurname(String surname) {
        this.surname = surname;
    }

    /**
     * Gets the role.
     * @return role
     */
    public Role getRole() {
        return role;
    }

    /**
     * Sets the role.
     * @param role the role to set
     */
    public void setRole(Role role) {
        this.role = role;
    }

    /**
     * Gets the creation timestamp.
     * @return creation timestamp
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * Sets the creation timestamp.
     * @param createdAt the creation timestamp to set
     */
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Returns the full name by combining first and last name.
     * @return full name as "FirstName LastName"
     */
    public String getFullName() {
        return name + " " + surname;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", role=" + role +
                '}';
    }
}
