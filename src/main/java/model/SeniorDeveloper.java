package model;

import java.time.LocalDateTime;

public class SeniorDeveloper extends User {

    public SeniorDeveloper(int id,
            String username,
            String passwordHash,
            String name,
            String surname,
            LocalDateTime createdAt) {
        super(id, username, passwordHash, name, surname, Role.SENIOR_DEV, createdAt);
    }
}
