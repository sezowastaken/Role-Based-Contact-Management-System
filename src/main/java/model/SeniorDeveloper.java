package model;

import java.time.LocalDateTime;

/**
 * Represents a Senior Developer user in the system.
 * Senior Developers have full contact management permissions: view, search, update, add, and delete.
 */
public class SeniorDeveloper extends User {

    /**
     * Creates a new Senior Developer user.
     */
    public SeniorDeveloper(int id,
            String username,
            String passwordHash,
            String name,
            String surname,
            LocalDateTime createdAt) {
        super(id, username, passwordHash, name, surname, Role.SENIOR_DEV, createdAt);
    }
}
