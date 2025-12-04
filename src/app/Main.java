package app;

import dao.ContactDAO;
import dao.UserDAO;
import model.Contact;
import model.User;
import service.AuthService;
import ui.screen.*;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        AuthService authService = new AuthService();
        LoginScreen loginScreen = new LoginScreen(authService);
        loginScreen.start();


        // testUsers();
        // System.out.println("\n---------------------------------\n");
        // testContacts();

    }

    private static void testUsers() {
        UserDAO userDAO = new UserDAO();

        System.out.println("\n>>> Tüm kullanıcıları listele:");
        List<User> users = userDAO.getAll();
        for (User u : users) {
            System.out.println(
                    "ID=" + u.getId() +
                            ", username=" + u.getUsername() +
                            ", name=" + u.getName() + " " + u.getSurname() +
                            ", role=" + u.getRole());
        }

        System.out.println("\n>>> Belirli username ile kullanıcı bul (örnek: 'bellingham' ya da kendi koyduğun biri):");
        String testUsername = "bellingham"; // burada DB'de var olan bir username yaz
        User found = userDAO.findByUsername(testUsername);
        if (found != null) {
            System.out.println("Bulundu: " + found.getUsername() +
                    " (" + found.getName() + " " + found.getSurname() +
                    "), role=" + found.getRole());
        } else {
            System.out.println("Kullanıcı bulunamadı: " + testUsername);
        }
    }

    private static void testContacts() {
        ContactDAO contactDAO = new ContactDAO();

        System.out.println("\n>>> Tüm contact'ları listele:");
        List<Contact> contacts = contactDAO.getAllContacts();
        for (Contact c : contacts) {
            System.out.println(
                    "ID=" + c.getContactId() +
                            ", name=" + c.getFirstName() + " " + c.getLastName() +
                            ", nick=" + c.getNickname() +
                            ", phone=" + c.getPhoneNumber() +
                            ", email=" + c.getEmail());
        }
    }
}
