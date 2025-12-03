package model;

import java.time.LocalDateTime;

public class JuniorDeveloper extends User {

    public JuniorDeveloper(int id,
            String username,
            String passwordHash,
            String name,
            String surname,
            LocalDateTime createdAt) {
        super(id, username, passwordHash, name, surname, Role.JUNIOR_DEVELOPER, createdAt);
    }
}
