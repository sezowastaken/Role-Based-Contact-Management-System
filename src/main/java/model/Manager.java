package model;

import java.time.LocalDateTime;

/**
 * Represents a Manager user in the system.
 * Managers can manage users (add, update, delete) and view contact statistics.
 */
public class Manager extends User {

    /**
     * Creates a new Manager user.
     */
    public Manager(int id,
            String username,
            String passwordHash,
            String name,
            String surname,
            LocalDateTime createdAt) {
        super(id, username, passwordHash, name, surname, Role.MANAGER, createdAt);
    }
}
