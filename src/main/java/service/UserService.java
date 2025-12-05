package service;

import dao.UserDAO;
import model.User;
import model.Role;
import util.HashUtil;
import java.util.List;

public class UserService {

    private final UserDAO userDAO;

    public UserService() {
        this.userDAO = new UserDAO();
    }

    public void changePassword(User user, String oldPassword, String newPassword) {

        if (!HashUtil.verify(oldPassword, user.getPasswordHash())) {
            throw new IllegalArgumentException("Old password is incorrect!");
        }

        String newHash = HashUtil.hash(newPassword);

        boolean ok = userDAO.updatePassword(user.getId(), newHash);
        if (!ok){
            throw new IllegalArgumentException("Password update failed!");
        }

        user.setPasswordHash(newHash);
    }

    public void createUser(String username, String rawPassword,
                           String firstName, String lastName,
                           Role role) {

        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty!");
        }

        // DAO’nun özel duplicate kontrol metodu kullanılmalı
        if (userDAO.existsByUsername(username, null)) {
            throw new IllegalArgumentException("Username already exists!");
        }

        String hashedPassword = HashUtil.hash(rawPassword);

        User user = new User();
        user.setUsername(username);
        user.setPasswordHash(hashedPassword);
        user.setName(firstName);
        user.setSurname(lastName);
        user.setRole(role);

        boolean inserted = userDAO.insert(user);
        if (!inserted) {
            throw new IllegalStateException("User insert failed!");
        }
    }

    public void updateUser(int userId, String newUsername,
                           String newFirstName, String newLastName,
                           Role newRole) {

        User user = userDAO.getById(userId);
        if (user == null) {
            throw new IllegalArgumentException("User not found!");
        }

        if (newUsername == null || newUsername.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty!");
        }

        // DAO’nun duplicate metodu daha doğru
        if (userDAO.existsByUsername(newUsername, userId)) {
            throw new IllegalArgumentException("Another user already uses this username!");
        }

        user.setUsername(newUsername);
        user.setName(newFirstName);
        user.setSurname(newLastName);
        user.setRole(newRole);

        boolean ok = userDAO.update(user);
        if (!ok) {
            throw new IllegalStateException("User update failed!");
        }
    }

    public void deleteUser(int userId, int managerUserId){
        
        User user = userDAO.getById(userId);
        if(user == null){
            throw new IllegalArgumentException("User not found!");
        }

        if (userId == managerUserId){
            throw new IllegalArgumentException("Managers cannot delete their own account!");
        }

        boolean ok = userDAO.delete(userId);
        if (!ok){
            throw new IllegalStateException("Delete operation failed!");
        }
    }

    public List<User> findAllUsers(){
        return userDAO.getAll();
    }

    public User findUserByUsername(String username){
        return userDAO.findByUsername(username);
    }
}
