package service;

import dao.UserDAO;
import model.User;
import util.HashUtil;

/**
 * Checks username and password against database.
 * Accesses database through UserDAO, verifies password with HashUtil.verify.
 */
public class AuthService {

    private final UserDAO userDAO;

    public AuthService() {
        this.userDAO = new UserDAO();
    }

    /**
     * Login attempt.
     * @return Successful User object, null if failed.
     */
    public User login(String username, String plainPassword) {
        if (username == null || plainPassword == null) {
            return null;
        }

        username = username.trim();
        if (username.isEmpty()) {
            return null;
        }

        User user = userDAO.findByUsername(username);
        if (user == null) {
            return null;
        }

        String storedHash = user.getPasswordHash();
        if (storedHash == null || storedHash.isEmpty()) {
            return null;
        }

        boolean matches = HashUtil.verify(plainPassword, storedHash);
        if (!matches) {
            return null;
        }

        return user;
    }
}
