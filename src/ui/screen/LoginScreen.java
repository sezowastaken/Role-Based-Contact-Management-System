package ui.screen;

import java.util.Scanner;

import model.Role;
import model.User;
import service.AuthService;
import ui.menu.BaseMenu;
import ui.menu.TesterMenu;
import ui.menu.JuniorDevMenu;
import ui.menu.SeniorDevMenu;
import ui.menu.ManagerMenu;

public class LoginScreen {

    private final AuthService authService;
    private final Scanner scanner;

    public LoginScreen(AuthService authService) {
        this.authService = authService;
        this.scanner = new Scanner(System.in);
    }

    /**
     * Uygulamanın login akışını başlatır.
     * Kullanıcı başarıyla giriş yaptıktan sonra rolüne uygun menüyü açar.
     */
    public void start() {
        while (true) {
            clearScreen();
            System.out.println("===========================================");
            System.out.println("  ROLE-BASED CONTACT MANAGEMENT SYSTEM");
            System.out.println("===========================================\n");
            System.out.println("Giriş yapmak için kullanıcı adı ve şifrenizi girin.");
            System.out.println("(Çıkmak için kullanıcı adı kısmına 'q' yazabilirsiniz.)\n");

            System.out.print("Kullanıcı adı: ");
            String username = scanner.nextLine();
            if (username == null) {
                username = "";
            }
            username = username.trim();

            if (username.equalsIgnoreCase("q")) {
                System.out.println("\nUygulamadan çıkılıyor. Görüşmek üzere!");
                return;
            }

            if (username.isEmpty()) {
                System.out.println("\n⚠ Kullanıcı adı boş olamaz. Lütfen tekrar deneyin.");
                pressEnterToContinue();
                continue;
            }

            System.out.print("Şifre: ");
            String password = scanner.nextLine();
            if (password == null) {
                password = "";
            }

            User loggedIn = authService.login(username, password);
            if (loggedIn == null) {
                System.out.println("\n❌ Giriş başarısız. Kullanıcı adı veya şifre hatalı.");
                pressEnterToContinue();
                continue;
            }

            System.out.println("\n✅ Giriş başarılı. Hoş geldin, "
                    + loggedIn.getName() + " " + loggedIn.getSurname() + "!");
            pressEnterToContinue();

            // Rol'e göre menü aç
            openMenuForUser(loggedIn);
            // Menüden logout ile çıkınca tekrar login ekranına döner
        }
    }

    private void openMenuForUser(User user) {
        Role role = user.getRole();

        if (role == null) {
            System.out.println("⚠ Kullanıcının rolü tanımsız. Menülere yönlendirilemiyor.");
            pressEnterToContinue();
            return;
        }

        BaseMenu menu;
        switch (role) {
            case TESTER:
                menu = new TesterMenu(user, scanner);
                break;
            case JUNIOR_DEV:
                menu = new JuniorDevMenu(user, scanner);
                break;
            case SENIOR_DEV:
                menu = new SeniorDevMenu(user, scanner);
                break;
            case MANAGER:
                menu = new ManagerMenu(user, scanner);
                break;
            default:
                System.out.println("⚠ Desteklenmeyen rol: " + role);
                pressEnterToContinue();
                return;
        }

        // Rol menüsünü başlat
        menu.show();
    }

    private void pressEnterToContinue() {
        System.out.print("\nDevam etmek için Enter'a basın...");
        scanner.nextLine();
    }

    private void clearScreen() {
        // Konsol temizleme – desteklenmeyen ortamlarda sadece boş satır gibi davranır
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}
