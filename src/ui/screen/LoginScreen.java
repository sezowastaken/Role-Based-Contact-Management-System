package ui.screen;

import java.util.Scanner;

import model.User;
import model.Tester;
import model.JuniorDeveloper;
import model.SeniorDeveloper;
import model.Manager;

import service.AuthService;
import ui.menu.BaseMenu;
import ui.menu.TesterMenu;
import ui.menu.JuniorDevMenu;
import ui.menu.SeniorDevMenu;
import ui.menu.ManagerMenu;

public class LoginScreen {

    // Basit ANSI renkleri (ileride istersen ConsoleColors'a taşıyabilirsin)
    private static final String RESET = "\u001B[0m";
    private static final String RED = "\u001B[31m";
    private static final String GREEN = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String CYAN = "\u001B[36m";

    private final Scanner scanner;
    private final AuthService authService;

    public LoginScreen(AuthService authService) {
        this.authService = authService;
        this.scanner = new Scanner(System.in);
    }

    /**
     * Giriş ekranını başlatır.
     * q/Q girilince programdan çıkar.
     * Başarılı login sonrası rol bazlı menüyü açar.
     */
    public void start() {
        boolean exitRequested = false;

        while (!exitRequested) {
            clearScreen();
            printHeader();

            System.out.print(CYAN + "Kullanıcı adı (çıkmak için q): " + RESET);
            String username = scanner.nextLine().trim();

            if (username.equalsIgnoreCase("q")) {
                System.out.println(YELLOW + "\nProgramdan çıkılıyor..." + RESET);
                exitRequested = true;
                continue;
            }

            System.out.print(CYAN + "Şifre: " + RESET);
            String password = scanner.nextLine();

            // Service katmanına login isteği at
            // NOT: Burada AuthService içinde "User login(String, String)" metodu olduğu
            // varsayılıyor.
            User loggedInUser = authService.login(username, password);

            if (loggedInUser == null) {
                System.out.println(RED + "\nHatalı kullanıcı adı veya şifre. Tekrar deneyin." + RESET);
                pressEnterToContinue();
                continue;
            }

            System.out.println(GREEN + "\nGiriş başarılı!" + RESET);
            pressEnterToContinue();

            // Rol bazlı menüyü aç
            openRoleMenu(loggedInUser);
            // Menüden logout edilince buraya geri düşüp tekrar login ekranı açılır
        }
    }

    private void printHeader() {
        System.out.println("===========================================");
        System.out.println("   ROLE-BASED CONTACT MANAGEMENT SYSTEM");
        System.out.println("===========================================");
        System.out.println("Lütfen giriş bilgilerinizi giriniz.\n");
    }

    /**
     * Kullanıcının tipine göre ilgili rol menüsünü açar.
     * Şu anlık sadece iskelet (placeholder) menüler.
     */
    private void openRoleMenu(User user) {
        BaseMenu menu = null;

        if (user instanceof Tester) {
            menu = new TesterMenu(user, scanner);
        } else if (user instanceof JuniorDeveloper) {
            menu = new JuniorDevMenu(user, scanner);
        } else if (user instanceof SeniorDeveloper) {
            menu = new SeniorDevMenu(user, scanner);
        } else if (user instanceof Manager) {
            menu = new ManagerMenu(user, scanner);
        } else {
            System.out.println(RED + "Kullanıcının rolü tanımlı değil. Login ekranına dönülüyor." + RESET);
            pressEnterToContinue();
            return;
        }

        // Menüden çıkınca (0 - Logout) show() biter ve login ekranına geri dönülür
        menu.show();
    }

    private void pressEnterToContinue() {
        System.out.print(YELLOW + "\nDevam etmek için Enter'a basın..." + RESET);
        scanner.nextLine();
    }

    private void clearScreen() {
        // Konsolu temizlemek için klasik escape kodu
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}
