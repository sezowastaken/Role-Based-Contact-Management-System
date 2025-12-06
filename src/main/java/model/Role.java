package model;

/**
 * Enum representing different user roles in the system.
 * Each role has different permissions and access levels.
 */
public enum Role {
    /** Tester role - can view and search contacts */
    TESTER,
    /** Junior Developer role - can view, search, and update contacts */
    JUNIOR_DEV,
    /** Senior Developer role - can view, search, update, add, and delete contacts */
    SENIOR_DEV,
    /** Manager role - can manage users and view statistics */
    MANAGER
}

