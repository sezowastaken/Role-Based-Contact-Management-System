package ui.menu;

import java.util.Scanner;
import model.User;

public class SeniorDevMenu extends BaseMenu {

    public SeniorDevMenu(User currentUser, Scanner scanner) {
        super(currentUser, scanner);
    }

    @Override
    protected String getTitle() {
        return "Senior Developer Menu";
    }

    @Override
    protected void printOptions() {
        System.out.println("1 - (Placeholder) Gelişmiş contact arama");
        System.out.println("2 - (Placeholder) İstatistikleri görüntüle");
        System.out.println("0 - Logout");
    }
}
