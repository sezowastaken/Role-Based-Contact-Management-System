package ui.menu;

import java.util.Scanner;

import dao.UserDAO;
import model.User;
import service.ContactService;
import service.UserService;
import undo.UndoManager;
import util.ConsoleColors; // Renkler eklendi
import util.InputHelper; // InputHelper eklendi

public class SeniorDevMenu extends BaseMenu {

    private final ContactService contactService;
    private final UserService userService;

    public SeniorDevMenu(User currentUser, Scanner scanner, UndoManager undoManager) {
        super(currentUser, scanner, undoManager);
        // Undo destekli service örnekleri
        this.contactService = new ContactService(undoManager);
        this.userService = new UserService(new UserDAO(), undoManager);
    }

    @Override
    protected String getTitle() {
        return "Senior Developer Menu";
    }

    @Override
    protected void printOptions() {
        System.out.println(
                ConsoleColors.GREEN + "┌──────────────────────────────────────────────────────────────────────┐");
        System.out.println(
                "│  " + ConsoleColors.WHITE + "                      SENIOR DEVELOPER MENU                "
                        + ConsoleColors.GREEN + "         │");
        System.out.println("├──────────────────────────────────────────────────────────────────────┤");
        System.out.println(
                "│ " + ConsoleColors.WHITE + "1 - Change password                                  "
                        + ConsoleColors.GREEN + "                │");
        System.out.println("│ " + ConsoleColors.WHITE + "2 - List all contacts                    "
                + ConsoleColors.GREEN + "                            │");
        System.out.println(
                "│ " + ConsoleColors.WHITE + "3 - Search contacts by selected field(s)        " + ConsoleColors.GREEN
                        + "                     │");
        System.out.println(
                "│ " + ConsoleColors.WHITE + "4 - Sort results by selected field (ascending / descending)   "
                        + ConsoleColors.GREEN + "       │");
        System.out.println(
                "│ " + ConsoleColors.WHITE + "5 - Update existing contact                " + ConsoleColors.GREEN
                        + "                          │");
        System.out.println(
                "│ " + ConsoleColors.WHITE + "6 - Add new contact(s)                          " + ConsoleColors.GREEN
                        + "                     │");
        System.out.println(
                "│ " + ConsoleColors.WHITE + "7 - Delete existing contact(s)                             "
                        + ConsoleColors.GREEN + "          │");
        if (undoManager != null && undoManager.canUndo()) {
            System.out.println("|" + ConsoleColors.WHITE
                    + " 8 - Undo last operation                             " + ConsoleColors.GREEN
                    + "                 │");
        }
        System.out.println("│ " + ConsoleColors.RED + "0 - Logout           " + ConsoleColors.GREEN
                + "                                                │");
        System.out.println(
                "└──────────────────────────────────────────────────────────────────────┘" + ConsoleColors.RESET);
    }

    @Override
    protected void handleOption(String choice) {
        switch (choice) {
            case "1":
                // [DÜZELTME] UserService'ten sildiğimiz metodu burada lokal olarak yapıyoruz
                userService.changeOwnPasswordInteractive(currentUser, scanner);
                break;
            case "2":
                contactService.displayAllContacts();
                break;
            case "3":
                // ContactService içindeki metodun artık InputHelper kullandığını varsayıyoruz
                contactService.searchContactsInteractive(scanner);
                break;
            case "4":
                contactService.sortContactsInteractive(scanner);
                break;
            case "5":
                contactService.updateContactInteractive(scanner);
                break;
            case "6":
                contactService.addContactInteractive(scanner);
                break;
            case "7":
                contactService.deleteContactInteractive(scanner);
                break;
            case "8":
                // Menüde gösterilmese bile kullanıcı 8 yazarsa kontrol et
                if (undoManager != null && undoManager.canUndo()) {
                    handleUndo(); // BaseMenu'deki ortak UNDO davranışı
                } else {
                    System.out.println(ConsoleColors.YELLOW + "\nThere is nothing to undo." + ConsoleColors.RESET);
                }
                break;
            default:
                System.out.println(ConsoleColors.RED + "\nInvalid choice. Please select one of the options above."
                        + ConsoleColors.RESET);
        }
    }

}