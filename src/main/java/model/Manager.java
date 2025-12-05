package model;

import java.time.LocalDateTime;

public class Manager extends User {

    public Manager(int id,
            String username,
            String passwordHash,
            String name,
            String surname,
            LocalDateTime createdAt) {
        super(id, username, passwordHash, name, surname, Role.MANAGER, createdAt);
    }
}
