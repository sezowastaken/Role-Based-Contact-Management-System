package model;

import java.time.LocalDateTime;

/**
 * Sisteme giriş yapan kullanıcıyı temsil eder.
 * Tablodaki karşılığı: users
 */
public class User {

    private int id;
    private String username;
    private String passwordHash;
    private String name;
    private String surname;
    private Role role;
    private LocalDateTime createdAt;

    public User() {
    }

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

    // Getter & Setter'lar

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

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
