package app;

import service.AuthService;
import ui.screen.*;


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
        AsciiAnimator.runIntro();
        loginScreen.start();
    }
}
