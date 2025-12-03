package model;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Rehberdeki kişiyi temsil eder.
 * Tablodaki karşılığı: contacts
 */
public class Contact {

    private int contactId;
    private String firstName;
    private String middleName; // optional
    private String lastName;
    private String nickname; // optional
    private String phonePrimary;
    private String phoneSecondary; // optional
    private String email;
    private String linkedinUrl; // optional
    private LocalDate birthDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Contact() {
    }

    public Contact(int contactId,
            String firstName,
            String middleName,
            String lastName,
            String nickname,
            String phonePrimary,
            String phoneSecondary,
            String email,
            String linkedinUrl,
            LocalDate birthDate,
            LocalDateTime createdAt,
            LocalDateTime updatedAt) {
        this.contactId = contactId;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.nickname = nickname;
        this.phonePrimary = phonePrimary;
        this.phoneSecondary = phoneSecondary;
        this.email = email;
        this.linkedinUrl = linkedinUrl;
        this.birthDate = birthDate;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getter & Setter'lar

    public int getContactId() {
        return contactId;
    }

    public void setContactId(int contactId) {
        this.contactId = contactId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPhonePrimary() {
        return phonePrimary;
    }

    public void setPhonePrimary(String phonePrimary) {
        this.phonePrimary = phonePrimary;
    }

    public String getPhoneSecondary() {
        return phoneSecondary;
    }

    public void setPhoneSecondary(String phoneSecondary) {
        this.phoneSecondary = phoneSecondary;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLinkedinUrl() {
        return linkedinUrl;
    }

    public void setLinkedinUrl(String linkedinUrl) {
        this.linkedinUrl = linkedinUrl;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "Contact{" +
                "contactId=" + contactId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phonePrimary='" + phonePrimary + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
