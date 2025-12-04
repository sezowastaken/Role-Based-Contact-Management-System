package ui.menu;

import java.util.Scanner;
import model.User;

public abstract class BaseMenu {

    protected final User currentUser;
    protected final Scanner scanner;

    public BaseMenu(User currentUser, Scanner scanner) {
        this.currentUser = currentUser;
        this.scanner = scanner;
    }

    /**
     * Menü ana döngüsü.
     * 0 - Logout seçilene kadar devam eder.
     * Sayı olmayan veya tanımsız bir giriş geldiğinde hata fırlatmaz,
     * sadece uyarı verip tekrar seçim ister.
     */
    public void show() {
        boolean logout = false;

        while (!logout) {
            clearScreen();
            printHeader();
            printOptions();

            System.out.print("Seçiminiz: ");
            String choice = scanner.nextLine().trim();

            // Logout
            if ("0".equals(choice)) {
                System.out.println("\nLogout ediliyor, giriş ekranına dönülüyor...");
                logout = true;
                continue;
            }

            // Boş ya da sayı olmayan girişler
            if (choice.isEmpty() || !choice.matches("\\d+")) {
                System.out.println("\nGeçersiz seçim! Lütfen listede olan bir sayı girin.");
                pressEnterToContinue();
                continue;
            }

            // Buraya geldiysek giriş sayısal, ama hangi menüde geçerli olduğunu
            // alt sınıflar (TesterMenu, JuniorDevMenu vs.) belirleyecek.
            handleOption(choice);
            pressEnterToContinue();
        }
    }

    /**
     * Menü başlığı (örn. "Tester Menu").
     */
    protected abstract String getTitle();

    /**
     * Rol'e özel menü seçeneklerini yazdırır.
     */
    protected abstract void printOptions();

    /**
     * Alt sınıflar isterse override eder.
     * Override etmezse burada sadece placeholder mesajı gösterilir.
     * Geçersiz değeri de burada yakalayabiliriz.
     */
    protected void handleOption(String choice) {
        System.out.println("Bu menü seçeneği henüz implemente edilmedi. (placeholder) Seçim: " + choice);
    }

    protected void printHeader() {
        System.out.println("===========================================");
        System.out.println(" " + getTitle());
        System.out.println("===========================================");
    }

    protected void pressEnterToContinue() {
        System.out.print("\nDevam etmek için Enter'a basın...");
        scanner.nextLine();
    }

    private void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}
