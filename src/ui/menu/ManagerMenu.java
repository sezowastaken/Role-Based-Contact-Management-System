package ui.menu;

import java.util.Scanner;
import model.User;

public class ManagerMenu extends BaseMenu {

    public ManagerMenu(User currentUser, Scanner scanner) {
        super(currentUser, scanner);
    }

    @Override
    protected String getTitle() {
        return "Manager Menu";
    }

    @Override
    protected void printOptions() {
        System.out.println("1 - (Placeholder) Tüm contact listesini görüntüle");
        System.out.println("2 - (Placeholder) Raporları görüntüle");
        System.out.println("3 - (Placeholder) Kullanıcı yönetimi");
        System.out.println("0 - Logout");
    }
}
