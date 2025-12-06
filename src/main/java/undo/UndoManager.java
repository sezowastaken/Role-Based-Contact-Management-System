package undo;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Manages undo operations using a stack (LIFO - Last In First Out).
 * Stores undoable actions during a single login session.
 */
public class UndoManager {

    private final Deque<UndoAction> stack = new ArrayDeque<>();

    /**
     * Adds an undo action to the stack.
     * @param action the action to add
     */
    public void push(UndoAction action) {
        if (action != null) {
            stack.push(action);
        }
    }

    /**
     * Checks if there are any undoable actions.
     * @return true if undo is possible, false otherwise
     */
    public boolean canUndo() {
        return !stack.isEmpty();
    }

    /**
     * Clears all undo history.
     * Should be called when user logs out.
     */
    public void clear() {
        stack.clear();
    }

    /**
     * Undoes the last operation.
     * If stack is empty, prints a message to the user.
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
