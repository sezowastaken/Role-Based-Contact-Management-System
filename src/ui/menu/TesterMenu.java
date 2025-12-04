package ui.menu;

import java.util.Scanner;
import model.User;

public class TesterMenu extends BaseMenu {

    public TesterMenu(User currentUser, Scanner scanner) {
        super(currentUser, scanner);
    }

    @Override
    protected String getTitle() {
        return "Tester Menu";
    }

    @Override
    protected void printOptions() {
        System.out.println("1 - (Placeholder) Test case listesini görüntüle");
        System.out.println("2 - (Placeholder) Bug raporlarını görüntüle");
        System.out.println("0 - Logout");
    }
}
