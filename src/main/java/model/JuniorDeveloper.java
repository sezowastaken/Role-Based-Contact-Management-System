package model;

import java.time.LocalDateTime;

/**
 * Represents a Junior Developer user in the system.
 * Junior Developers can view, search, and update contacts.
 */
public class JuniorDeveloper extends User {

    /**
     * Creates a new Junior Developer user.
     */
    public JuniorDeveloper(int id,
            String username,
            String passwordHash,
            String name,
            String surname,
            LocalDateTime createdAt) {
        super(id, username, passwordHash, name, surname, Role.JUNIOR_DEV, createdAt);
    }
}
