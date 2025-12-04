package ui.menu;

import java.util.Scanner;
import model.User;

public class JuniorDevMenu extends BaseMenu {

    public JuniorDevMenu(User currentUser, Scanner scanner) {
        super(currentUser, scanner);
    }

    @Override
    protected String getTitle() {
        return "Junior Developer Menu";
    }

    @Override
    protected void printOptions() {
        System.out.println("1 - (Placeholder) Contact listesini görüntüle");
        System.out.println("2 - (Placeholder) Yeni contact ekle");
        System.out.println("0 - Logout");
    }
}
