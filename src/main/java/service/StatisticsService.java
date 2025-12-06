package service;

import dao.ContactDAO;
import model.Contact;
import util.ConsoleColors;

import java.time.LocalDate;
import java.time.Period;
import java.util.Map;

/**
 * Service class for managing contact statistics and reporting.
 * Provides comprehensive statistical information about contacts.
 */
public class StatisticsService {

    private final ContactDAO contactDAO;

    public StatisticsService() {
        this.contactDAO = new ContactDAO();
    }

    /**
     * Displays comprehensive contact statistics for Manager role.
     * Includes: average age, youngest/oldest contacts, LinkedIn counts, and name
     * frequency.
     */
    public void displayContactStatistics() {
        System.out.println(ConsoleColors.BLUE + "\n=== CONTACTS STATISTICAL INFORMATION ===\n" + ConsoleColors.RESET);

        // Total Contact Count
        int totalCount = contactDAO.getTotalContactCount();
        System.out.println(ConsoleColors.YELLOW + "Total Contact Count:" + ConsoleColors.RESET);
        System.out.printf(ConsoleColors.WHITE + "%d contact(s)%n%n", totalCount + ConsoleColors.RESET);

        // Average Age
        double avgAge = contactDAO.getAverageAge();
        System.out.println(ConsoleColors.YELLOW + "Average Age:" + ConsoleColors.RESET);
        if (avgAge > 0) {
            System.out.printf(ConsoleColors.WHITE + "%.2f years%n%n", avgAge + ConsoleColors.WHITE);
        } else {
            System.out.println(ConsoleColors.RED + "No contacts with birth date information.\n" + ConsoleColors.RESET);
        }

        // Youngest Contact
        Contact youngest = contactDAO.getYoungestContact();
        System.out.println(ConsoleColors.YELLOW + "Youngest Contact:" + ConsoleColors.RESET);
        if (youngest != null && youngest.getBirthDate() != null) {
            Period period = Period.between(youngest.getBirthDate(), LocalDate.now());
            int age = period.getYears();
            System.out.printf("  %s %s (Age: %d)%n%n",
                    youngest.getFirstName() != null ? youngest.getFirstName() : "-",
                    youngest.getLastName() != null ? youngest.getLastName() : "-",
                    age);
        } else {
            System.out.println(ConsoleColors.RED + "No contact with birth date information.\n" + ConsoleColors.RESET);
        }

        // Oldest Contact
        Contact oldest = contactDAO.getOldestContact();
        System.out.println(ConsoleColors.YELLOW + "Oldest Contact:" + ConsoleColors.RESET);
        if (oldest != null && oldest.getBirthDate() != null) {
            Period period = Period.between(oldest.getBirthDate(), LocalDate.now());
            int age = period.getYears();
            System.out.printf("  %s %s (Age: %d)%n%n",
                    oldest.getFirstName() != null ? oldest.getFirstName() : "-",
                    oldest.getLastName() != null ? oldest.getLastName() : "-",
                    age);
        } else {
            System.out.println(ConsoleColors.RED + "No contact with birth date information.\n" + ConsoleColors.RESET);
        }

        // LinkedIn Statistics
        int withLinkedin = contactDAO.countWithLinkedin();
        int withoutLinkedin = contactDAO.countWithoutLinkedin();
        System.out.println(ConsoleColors.YELLOW + "LinkedIn Profile Statistics:" + ConsoleColors.RESET);
        System.out.printf(ConsoleColors.WHITE + "Contacts with LinkedIn: %d%n", withLinkedin);
        System.out.printf("Contacts without LinkedIn: %d%n%n", withoutLinkedin + ConsoleColors.RESET);

        // Birth Month Distribution
        Map<String, Integer> birthMonthDist = contactDAO.getBirthMonthDistribution();
        System.out.println(ConsoleColors.YELLOW + "Birth Month Distribution:" + ConsoleColors.RESET);
        if (birthMonthDist.isEmpty()) {
            System.out.println(ConsoleColors.RED + "No contacts with birth date information.\n" + ConsoleColors.RESET);
        } else {
            for (Map.Entry<String, Integer> entry : birthMonthDist.entrySet()) {
                System.out.printf(ConsoleColors.WHITE + "%s: %d person(s)%n", entry.getKey(),
                        entry.getValue() + ConsoleColors.RESET);
            }
            System.out.println();
        }

        // Age Group Distribution
        Map<String, Integer> ageGroups = contactDAO.getAgeGroupDistribution();
        System.out.println(ConsoleColors.YELLOW + "Age Group Distribution:" + ConsoleColors.RESET);
        int totalWithBirthDate = 0;
        for (int count : ageGroups.values()) {
            totalWithBirthDate += count;
        }
        if (totalWithBirthDate == 0) {
            System.out.println(ConsoleColors.RED + "No contacts with birth date information.\n" + ConsoleColors.RESET);
        } else {
            for (Map.Entry<String, Integer> entry : ageGroups.entrySet()) {
                int count = entry.getValue();
                if (count > 0) {
                    System.out.printf(ConsoleColors.WHITE + "%s: %d person(s)%n", entry.getKey(),
                            count + ConsoleColors.RESET);
                }
            }
            System.out.println();
        }

        // Same First Name Statistics
        Map<String, Integer> nameCounts = contactDAO.getAllFirstNameCounts();
        System.out.println(ConsoleColors.YELLOW + "First Name Frequency (same name count):" + ConsoleColors.RESET);
        if (nameCounts.isEmpty()) {
            System.out.println(ConsoleColors.RED + "No contacts found.\n" + ConsoleColors.RESET);
        } else {
            boolean hasDuplicates = false;
            for (Map.Entry<String, Integer> entry : nameCounts.entrySet()) {
                String name = entry.getKey() != null ? entry.getKey() : "(null)";
                int count = entry.getValue();
                if (count > 1) {
                    System.out.printf(ConsoleColors.WHITE + "%s: %d persons%n", name, count + ConsoleColors.WHITE);
                    hasDuplicates = true;
                }
            }
            if (!hasDuplicates) {
                System.out.println(
                        ConsoleColors.YELLOW + "All first names are unique (no duplicates).\n" + ConsoleColors.RESET);
            } else {
                System.out.println();
            }
        }

        System.out.println(ConsoleColors.GREEN + "=== End of Statistics ===\n" + ConsoleColors.RESET);
    }
}
