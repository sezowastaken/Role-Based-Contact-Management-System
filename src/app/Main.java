package app;

import dao.ContactDAO;
import dao.UserDAO;
import model.Contact;
import model.User;
import ui.screen.*;
import service.UserService;
import model.Role;

import java.util.List;

public class Main {

    public static void main(String[] args) {

        //AsciiAnimator.runIntro();

        System.out.println("=== TEST: UserDAO & ContactDAO ===");

        testUsers();
        System.out.println("\n---------------------------------\n");
        testContacts();

                // ======================================================
        // === USER SERVICE TEST BLOĞU (AÇ/KAPA) ================
        // ======================================================
        
        try {
            System.out.println("\n=== USER SERVICE TEST BAŞLIYOR ===");

            UserDAO userDAO = new UserDAO();
            UserService userService = new UserService(userDAO);

            // -----------------------------
            // 1) CREATE USER TEST
            // -----------------------------
            System.out.println("\n[CREATE USER]");
            userService.createUser(
                    "servicetest",
                    "1234",
                    "Service",
                    "Tester",
                    Role.TESTER
            );

            User u = userService.findUserByUsername("servicetest");
            System.out.println("Created user ID = " + u.getId());
            System.out.println("Full name = " + u.getFullName());
            System.out.println("Role = " + u.getRole());


            // -----------------------------
            // 2) UPDATE USER TEST
            // -----------------------------
            System.out.println("\n[UPDATE USER]");
            userService.updateUser(
                    u.getId(),
                    "servicetest_updated",
                    "ServiceUpdated",
                    "TesterUpdated",
                    Role.MANAGER
            );

            User updated = userService.findUserByUsername("servicetest_updated");
            System.out.println("Updated username = " + updated.getUsername());
            System.out.println("Updated name = " + updated.getFullName());
            System.out.println("Updated role = " + updated.getRole());


            // -----------------------------
            // 3) CHANGE PASSWORD TEST
            // -----------------------------
            System.out.println("\n[CHANGE PASSWORD]");
            userService.changePassword(updated, "1234", "abcd1234");
            System.out.println("Password changed successfully!");


            // -----------------------------
            // 4) LIST USERS TEST
            // -----------------------------
            System.out.println("\n[LIST USERS]");
            for (User user : userService.findAllUsers()) {
                System.out.println(user);
            }


            // -----------------------------
            // 5) DELETE USER TEST
            // -----------------------------
            /*System.out.println("\n[DELETE USER]");
            userService.deleteUser(updated.getId(), 999999); // manager id farklı olacak
            System.out.println("User deleted!");*/


            System.out.println("\n=== USER SERVICE TEST BİTTİ ===");

        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // ======================================================
        // === USER SERVICE TEST BLOĞU SONU =====================
        // ======================================================


        System.out.println("\n=== TEST BİTTİ ===");
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
                    ", role=" + u.getRole()
            );
        }

        System.out.println("\n>>> Belirli username ile kullanıcı bul (örnek: 'bellingham' ya da kendi koyduğun biri):");
        String testUsername = "bellingham";   // burada DB'de var olan bir username yaz
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
                    ", email=" + c.getEmail()
            );
        }
    }

    
}
