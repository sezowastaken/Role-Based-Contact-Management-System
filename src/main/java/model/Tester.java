package model;

import java.time.LocalDateTime;

/**
 * Represents a Tester user in the system.
 * Testers can view and search contacts, but cannot modify them.
 */
public class Tester extends User {

    /**
     * Creates a new Tester user.
     */
    public Tester(int id,
            String username,
            String passwordHash,
            String name,
            String surname,
            LocalDateTime createdAt) {
        super(id, username, passwordHash, name, surname, Role.TESTER, createdAt);
    }
}
