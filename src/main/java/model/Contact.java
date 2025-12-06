package model;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Represents a contact in the contact management system.
 * Stores personal information like name, phone, email, and birth date.
 */
public class Contact {

    private int contactId;
    private String firstName;
    private String lastName;
    private String nickname;
    private String phoneNumber;
    private String email;
    private String linkedinUrl;
    private LocalDate birthDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * Default constructor for Contact.
     */
    public Contact() {
    }

    /**
     * Constructor with all fields.
     */
    public Contact(int contactId,
            String firstName,
            String lastName,
            String nickname,
            String phoneNumber,
            String email,
            String linkedinUrl,
            LocalDate birthDate,
            LocalDateTime createdAt,
            LocalDateTime updatedAt) {
        this.contactId = contactId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.nickname = nickname;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.linkedinUrl = linkedinUrl;
        this.birthDate = birthDate;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    /**
     * Gets the contact ID.
     * @return contact ID
     */
    public int getContactId() {
        return contactId;
    }

    /**
     * Sets the contact ID.
     * @param contactId the contact ID to set
     */
    public void setContactId(int contactId) {
        this.contactId = contactId;
    }

    /**
     * Gets the first name.
     * @return first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the first name.
     * @param firstName the first name to set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Gets the last name.
     * @return last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the last name.
     * @param lastName the last name to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Gets the nickname.
     * @return nickname
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * Sets the nickname.
     * @param nickname the nickname to set
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     * Gets the phone number.
     * @return phone number
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Sets the phone number.
     * @param phoneNumber the phone number to set
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * Gets the email address.
     * @return email address
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email address.
     * @param email the email address to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the LinkedIn URL.
     * @return LinkedIn URL
     */
    public String getLinkedinUrl() {
        return linkedinUrl;
    }

    /**
     * Sets the LinkedIn URL.
     * @param linkedinUrl the LinkedIn URL to set
     */
    public void setLinkedinUrl(String linkedinUrl) {
        this.linkedinUrl = linkedinUrl;
    }

    /**
     * Gets the birth date.
     * @return birth date
     */
    public LocalDate getBirthDate() {
        return birthDate;
    }

    /**
     * Sets the birth date.
     * @param birthDate the birth date to set
     */
    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
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
     * Gets the last update timestamp.
     * @return last update timestamp
     */
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    /**
     * Sets the last update timestamp.
     * @param updatedAt the update timestamp to set
     */
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "Contact{" +
                "contactId=" + contactId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
