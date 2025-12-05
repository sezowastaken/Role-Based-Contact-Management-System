package ui.menu;

import java.util.Scanner;

import dao.UserDAO;
import model.User;
import service.ContactService;
import service.UserService;
import undo.UndoManager;

public class JuniorDevMenu extends BaseMenu {

    private final ContactService contactService;
    private final UserService userService;

    public JuniorDevMenu(User currentUser, Scanner scanner, UndoManager undoManager) {
        super(currentUser, scanner, undoManager);
        // Undo destekli service örnekleri
        this.contactService = new ContactService(undoManager);
        this.userService = new UserService(new UserDAO(), undoManager);
    }

    @Override
    protected String getTitle() {
        return "Junior Developer Menu";
    }

    @Override
    protected void printOptions() {
        System.out.println("1 - Change password");
        System.out.println("2 - List all contacts");
        System.out.println("3 - Search contacts by selected field(s)");
        System.out.println("4 - Sort contacts by selected field (ascending / descending)");
        System.out.println("5 - Update existing contact");

        // Stack'te en az bir undoable action varsa, UNDO seçeneğini göster
        if (undoManager != null && undoManager.canUndo()) {
            System.out.println("6 - Undo last operation");
        }

        System.out.println("0 - Logout");
    }

    @Override
    protected void handleOption(String choice) {
        switch (choice) {
            case "1":
                userService.changeOwnPasswordInteractive(currentUser, scanner);
                break;
            case "2":
                contactService.displayAllContacts();
                break;
            case "3":
                contactService.searchContactsInteractive(scanner);
                break;
            case "4":
                contactService.sortContactsInteractive(scanner);
                break;
            case "5":
                contactService.updateContactInteractive(scanner);
                break;
            case "6":
                // Menüde gösterilmese bile kullanıcı 6 yazarsa kontrol et
                if (undoManager != null && undoManager.canUndo()) {
                    handleUndo(); // BaseMenu'deki ortak UNDO davranışı
                } else {
                    System.out.println("\nThere is nothing to undo.");
                }
                break;
            default:
                System.out.println("\nInvalid choice. Please select one of the options above.");
        }
    }
}
