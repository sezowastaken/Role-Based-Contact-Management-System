package undo;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Tek bir oturum (login) boyunca yapılmış geri alınabilir işlemleri LIFO mantığıyla tutar.
 */
public class UndoManager {

    private final Deque<UndoAction> stack = new ArrayDeque<>();

    /**
     * Null değilse stack'e ekler.
     */
    public void push(UndoAction action) {
        if (action != null) {
            stack.push(action);
        }
    }

    /**
     * Geri alınabilir en az bir işlem var mı?
     */
    public boolean canUndo() {
        return !stack.isEmpty();
    }

    /**
     * Tüm geçmişi temizler (logout'ta çağrılacak).
     */
    public void clear() {
        stack.clear();
    }

    /**
     * Son işlemi geri alır.
     * Stack boşsa kullanıcıya mesaj basar.
     */
    public void undoLast() {
        if (stack.isEmpty()) {
            System.out.println("\nThere is nothing to undo.");
            return;
        }

        UndoAction action = stack.pop();
        System.out.println("\n" + action.getDescription());
        action.undo();
    }
}
