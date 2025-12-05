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
     * Includes: average age, youngest/oldest contacts, LinkedIn counts, and name frequency.
     */
    public void displayContactStatistics() {
        System.out.println(ConsoleColors.CYAN + "\n=== CONTACTS STATISTICAL INFORMATION ===\n" + ConsoleColors.RESET);

        // Total Contact Count
        int totalCount = contactDAO.getTotalContactCount();
        System.out.println(ConsoleColors.YELLOW + "Total Contact Count:" + ConsoleColors.RESET);
        System.out.printf("  %d contact(s)%n%n", totalCount);

        // Average Age
        double avgAge = contactDAO.getAverageAge();
        System.out.println(ConsoleColors.YELLOW + "Average Age:" + ConsoleColors.RESET);
        if (avgAge > 0) {
            System.out.printf("  %.2f years%n%n", avgAge);
        } else {
            System.out.println("  No contacts with birth date information.\n");
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
            System.out.println("  No contact with birth date information.\n");
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
            System.out.println("  No contact with birth date information.\n");
        }

        // LinkedIn Statistics
        int withLinkedin = contactDAO.countWithLinkedin();
        int withoutLinkedin = contactDAO.countWithoutLinkedin();
        System.out.println(ConsoleColors.YELLOW + "LinkedIn Profile Statistics:" + ConsoleColors.RESET);
        System.out.printf("  Contacts with LinkedIn: %d%n", withLinkedin);
        System.out.printf("  Contacts without LinkedIn: %d%n%n", withoutLinkedin);

        // Birth Month Distribution
        Map<String, Integer> birthMonthDist = contactDAO.getBirthMonthDistribution();
        System.out.println(ConsoleColors.YELLOW + "Birth Month Distribution:" + ConsoleColors.RESET);
        if (birthMonthDist.isEmpty()) {
            System.out.println("  No contacts with birth date information.\n");
        } else {
            for (Map.Entry<String, Integer> entry : birthMonthDist.entrySet()) {
                System.out.printf("  %s: %d person(s)%n", entry.getKey(), entry.getValue());
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
            System.out.println("  No contacts with birth date information.\n");
        } else {
            for (Map.Entry<String, Integer> entry : ageGroups.entrySet()) {
                int count = entry.getValue();
                if (count > 0) {
                    System.out.printf("  %s: %d person(s)%n", entry.getKey(), count);
                }
            }
            System.out.println();
        }

        // Same First Name Statistics
        Map<String, Integer> nameCounts = contactDAO.getAllFirstNameCounts();
        System.out.println(ConsoleColors.YELLOW + "First Name Frequency (same name count):" + ConsoleColors.RESET);
        if (nameCounts.isEmpty()) {
            System.out.println("  No contacts found.\n");
        } else {
            boolean hasDuplicates = false;
            for (Map.Entry<String, Integer> entry : nameCounts.entrySet()) {
                String name = entry.getKey() != null ? entry.getKey() : "(null)";
                int count = entry.getValue();
                if (count > 1) {
                    System.out.printf("  %s: %d persons%n", name, count);
                    hasDuplicates = true;
                }
            }
            if (!hasDuplicates) {
                System.out.println("  All first names are unique (no duplicates).\n");
            } else {
                System.out.println();
            }
        }

        System.out.println(ConsoleColors.GREEN + "=== End of Statistics ===\n" + ConsoleColors.RESET);
    }
}
