package model;

import java.time.LocalDateTime;

public class Tester extends User {

    public Tester(int id,
            String username,
            String passwordHash,
            String name,
            String surname,
            LocalDateTime createdAt) {
        super(id, username, passwordHash, name, surname, Role.TESTER, createdAt);
    }
}
