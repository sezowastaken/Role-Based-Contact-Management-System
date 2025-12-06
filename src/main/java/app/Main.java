package app;

import dao.ContactDAO;
import dao.UserDAO;
import model.Contact;
import model.User;
import service.AuthService;
import ui.screen.*;
import service.UserService;
import model.Role;

import java.util.List;
import java.nio.charset.Charset;

/**
 * Main entry point for the Role-Based Contact Management System application.
 * Initializes the authentication service and launches the login screen.
 * @author Group 25
 */
public class Main {

    /**
     * Main method that starts the application.
     * Creates an authentication service instance and displays the login screen.
     * The application runs continuously until the user chooses to exit.
     */
    public static void main(String[] args) {
        AuthService authService = new AuthService();
        LoginScreen loginScreen = new LoginScreen(authService);
        loginScreen.start();
        //System.out.println("Default charset = " + Charset.defaultCharset());
    }
}
