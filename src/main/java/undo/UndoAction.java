package undo;

import dao.ContactDAO;
import dao.UserDAO;
import model.Contact;
import model.User;
import model.Role;

import java.time.LocalDate;

/**
 * Represents a single undo operation.
 * Stores the necessary information to reverse database write operations
 * (insert, update, delete, password change).
 */
public class UndoAction {

    /**
     * Types of undoable actions.
     */
    public enum Type {
        CONTACT_INSERT,
        CONTACT_UPDATE,
        CONTACT_DELETE,
        USER_INSERT,
        USER_UPDATE,
        USER_DELETE,
        PASSWORD_CHANGE
    }

    private final Type type;

    private final ContactDAO contactDAO;
    private final UserDAO userDAO;

    private final Integer contactId;
    private final Contact contactSnapshot;

    private final Integer userId;
    private final User userSnapshot;

    private final String oldPasswordHash;
    private final String newPasswordHash;

    /**
     * Private constructor. Use factory methods instead.
     */
    private UndoAction(
            Type type,
            ContactDAO contactDAO,
            Integer contactId,
            Contact contactSnapshot,
            UserDAO userDAO,
            Integer userId,
            User userSnapshot,
            String oldPasswordHash,
            String newPasswordHash
    ) {
        this.type = type;
        this.contactDAO = contactDAO;
        this.contactId = contactId;
        this.contactSnapshot = contactSnapshot;
        this.userDAO = userDAO;
        this.userId = userId;
        this.userSnapshot = userSnapshot;
        this.oldPasswordHash = oldPasswordHash;
        this.newPasswordHash = newPasswordHash;
    }

    // ---------- FACTORY METHODS ----------

    /**
     * Creates an undo action for contact insertion.
     * @param contactDAO the ContactDAO instance
     * @param newContactId the ID of the newly inserted contact
     * @return UndoAction for contact insert
     */
    public static UndoAction forContactInsert(ContactDAO contactDAO, int newContactId) {
        return new UndoAction(
                Type.CONTACT_INSERT,
                contactDAO,
                newContactId,
                null,
                null,
                null,
                null,
                null,
                null
        );
    }

    /**
     * Creates an undo action for contact deletion.
     * @param contactDAO the ContactDAO instance
     * @param deletedContact the contact that was deleted (snapshot)
     * @return UndoAction for contact delete
     */
    public static UndoAction forContactDelete(ContactDAO contactDAO, Contact deletedContact) {
        return new UndoAction(
                Type.CONTACT_DELETE,
                contactDAO,
                deletedContact != null ? deletedContact.getContactId() : null,
                deletedContact != null ? cloneContact(deletedContact) : null,
                null,
                null,
                null,
                null,
                null
        );
    }

    /**
     * Creates an undo action for contact update.
     * @param contactDAO the ContactDAO instance
     * @param previousState the previous state of the contact before update
     * @return UndoAction for contact update
     */
    public static UndoAction forContactUpdate(ContactDAO contactDAO, Contact previousState) {
        return new UndoAction(
                Type.CONTACT_UPDATE,
                contactDAO,
                previousState != null ? previousState.getContactId() : null,
                previousState != null ? cloneContact(previousState) : null,
                null,
                null,
                null,
                null,
                null
        );
    }

    /**
     * Creates an undo action for user insertion.
     * @param userDAO the UserDAO instance
     * @param newUserId the ID of the newly inserted user
     * @return UndoAction for user insert
     */
    public static UndoAction forUserInsert(UserDAO userDAO, int newUserId) {
        return new UndoAction(
                Type.USER_INSERT,
                null,
                null,
                null,
                userDAO,
                newUserId,
                null,
                null,
                null
        );
    }

    /**
     * Creates an undo action for user deletion.
     * @param userDAO the UserDAO instance
     * @param deletedUser the user that was deleted (snapshot)
     * @return UndoAction for user delete
     */
    public static UndoAction forUserDelete(UserDAO userDAO, User deletedUser) {
        return new UndoAction(
                Type.USER_DELETE,
                null,
                null,
                null,
                userDAO,
                deletedUser != null ? deletedUser.getId() : null,
                deletedUser != null ? cloneUser(deletedUser) : null,
                null,
                null
        );
    }

    /**
     * Creates an undo action for user update.
     * @param userDAO the UserDAO instance
     * @param previousUser the previous state of the user before update
     * @return UndoAction for user update
     */
    public static UndoAction forUserUpdate(UserDAO userDAO, User previousUser) {
        return new UndoAction(
                Type.USER_UPDATE,
                null,
                null,
                null,
                userDAO,
                previousUser != null ? previousUser.getId() : null,
                previousUser != null ? cloneUser(previousUser) : null,
                null,
                null
        );
    }

    /**
     * Creates an undo action for password change.
     * @param userDAO the UserDAO instance
     * @param userId the ID of the user whose password was changed
     * @param oldPasswordHash the previous password hash
     * @param newPasswordHash the new password hash
     * @return UndoAction for password change
     */
    public static UndoAction forPasswordChange(
            UserDAO userDAO,
            int userId,
            String oldPasswordHash,
            String newPasswordHash
    ) {
        return new UndoAction(
                Type.PASSWORD_CHANGE,
                null,
                null,
                null,
                userDAO,
                userId,
                null,
                oldPasswordHash,
                newPasswordHash
        );
    }

    // ---------- PUBLIC API ----------

    /**
     * Performs the actual undo operation.
     * If DAO methods return false, prints a simple console message.
     */
    public void undo() {
        if (type == null) {
            System.out.println("Undo action type is null, nothing to do.");
            return;
        }

        switch (type) {
            case CONTACT_INSERT -> undoContactInsert();
            case CONTACT_DELETE -> undoContactDelete();
            case CONTACT_UPDATE -> undoContactUpdate();
            case USER_INSERT     -> undoUserInsert();
            case USER_DELETE     -> undoUserDelete();
            case USER_UPDATE     -> undoUserUpdate();
            case PASSWORD_CHANGE -> undoPasswordChange();
            default -> System.out.println("Unknown undo action type: " + type);
        }
    }

    /**
     * Returns a human-readable description of this undo action.
     * @return description string for console display
     */
    public String getDescription() {
        return switch (type) {
            case CONTACT_INSERT ->
                    "Undo: delete newly inserted contact (ID=" + contactId + ")";
            case CONTACT_DELETE ->
                    "Undo: restore deleted contact (ID=" + contactId + ")";
            case CONTACT_UPDATE ->
                    "Undo: revert contact update (ID=" + contactId + ")";
            case USER_INSERT ->
                    "Undo: delete newly inserted user (ID=" + userId + ")";
            case USER_DELETE ->
                    "Undo: restore deleted user (ID=" + userId + ")";
            case USER_UPDATE ->
                    "Undo: revert user update (ID=" + userId + ")";
            case PASSWORD_CHANGE ->
                    "Undo: restore previous password (user ID=" + userId + ")";
            default ->
                    "Undo: unknown action";
        };
    }

    /**
     * Gets the type of this undo action.
     * @return the action type
     */
    public Type getType() {
        return type;
    }

    // ---------- PRIVATE UNDO HELPER METHODS ----------

    /**
     * Undoes a contact insert operation by deleting the contact.
     */
    private void undoContactInsert() {
        if (contactDAO == null || contactId == null) {
            System.out.println("Cannot undo contact insert: missing DAO or contact ID.");
            return;
        }
        boolean success = contactDAO.deleteContact(contactId);
        if (!success) {
            System.out.println("Undo failed: contact could not be deleted (ID=" + contactId + ").");
        }
    }

    /**
     * Undoes a contact delete operation by restoring the deleted contact.
     */
    private void undoContactDelete() {
        if (contactDAO == null || contactSnapshot == null) {
            System.out.println("Cannot undo contact delete: missing DAO or contact snapshot.");
            return;
        }

        boolean success = contactDAO.restoreContact(contactSnapshot);
        if (!success) {
            System.out.println(
                    "Undo failed: contact could not be re-inserted (ID=" + contactSnapshot.getContactId() + ")."
            );
        }
    }


    /**
     * Undoes a contact update by reverting to the previous state.
     */
    private void undoContactUpdate() {
        if (contactDAO == null || contactSnapshot == null) {
            System.out.println("Cannot undo contact update: missing DAO or previous state.");
            return;
        }
        boolean success = contactDAO.updateContact(contactSnapshot);
        if (!success) {
            System.out.println("Undo failed: contact could not be reverted (ID="
                    + contactSnapshot.getContactId() + ").");
        }
    }

    /**
     * Undoes a user insert operation by deleting the user.
     */
    private void undoUserInsert() {
        if (userDAO == null || userId == null) {
            System.out.println("Cannot undo user insert: missing DAO or user ID.");
            return;
        }
        boolean success = userDAO.delete(userId);
        if (!success) {
            System.out.println("Undo failed: user could not be deleted (ID=" + userId + ").");
        }
    }

    /**
     * Undoes a user delete operation by restoring the deleted user.
     */
    private void undoUserDelete() {
        if (userDAO == null || userSnapshot == null) {
            System.out.println("Cannot undo user delete: missing DAO or user snapshot.");
            return;
        }

        boolean success = userDAO.restoreUser(userSnapshot);
        if (!success) {
            System.out.println(
                    "Undo failed: user could not be re-inserted (ID=" + userSnapshot.getId() + ")."
            );
        }
    }


    /**
     * Undoes a user update by reverting to the previous state.
     */
    private void undoUserUpdate() {
        if (userDAO == null || userSnapshot == null) {
            System.out.println("Cannot undo user update: missing DAO or previous state.");
            return;
        }
        boolean success = userDAO.update(userSnapshot);
        if (!success) {
            System.out.println("Undo failed: user could not be reverted (ID="
                    + userSnapshot.getId() + ").");
        }
    }

    /**
     * Undoes a password change by restoring the old password.
     */
    private void undoPasswordChange() {
        if (userDAO == null || userId == null || oldPasswordHash == null) {
            System.out.println("Cannot undo password change: missing DAO, user ID or old password hash.");
            return;
        }
        boolean success = userDAO.updatePassword(userId, oldPasswordHash);
        if (!success) {
            System.out.println("Undo failed: password could not be restored (user ID=" + userId + ").");
        }
    }

    // ---------- CLONE HELPERS (DEEP COPY) ----------

    /**
     * Creates a deep copy of a Contact object.
     * @param original the Contact to clone
     * @return a new Contact with the same values, or null if original is null
     */
    public static Contact cloneContact(Contact original) {
        if (original == null) {
            return null;
        }
        Contact copy = new Contact();
        copy.setContactId(original.getContactId());
        copy.setFirstName(original.getFirstName());
        copy.setLastName(original.getLastName());
        copy.setNickname(original.getNickname());
        copy.setPhoneNumber(original.getPhoneNumber());
        copy.setEmail(original.getEmail());
        copy.setLinkedinUrl(original.getLinkedinUrl());

        LocalDate birth = original.getBirthDate();
        if (birth != null) {
            copy.setBirthDate(birth);
        }

        return copy;
    }

    /**
     * Creates a deep copy of a User object.
     * @param original the User to clone
     * @return a new User with the same values, or null if original is null
     */
    public static User cloneUser(User original) {
        if (original == null) {
            return null;
        }
        User copy = new User();
        copy.setId(original.getId());
        copy.setUsername(original.getUsername());
        copy.setPasswordHash(original.getPasswordHash());
        copy.setName(original.getName());
        copy.setSurname(original.getSurname());

        Role role = original.getRole();
        if (role != null) {
            copy.setRole(role);
        }

        return copy;
    }
}
