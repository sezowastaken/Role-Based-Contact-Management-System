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

public class Main {

    public static void main(String[] args) {
        AuthService authService = new AuthService();
        LoginScreen loginScreen = new LoginScreen(authService);
        loginScreen.start();
        //System.out.println("Default charset = " + Charset.defaultCharset());
    }
    
}
