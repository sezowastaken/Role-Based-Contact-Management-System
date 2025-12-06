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
     * Displays contact statistics for Manager role.
     */
    public void displayContactStatistics() {
        System.out.println(ConsoleColors.BLUE + "\n=== CONTACTS STATISTICAL INFORMATION ===\n" + ConsoleColors.RESET);

        int totalCount = contactDAO.getTotalContactCount();
        System.out.println(ConsoleColors.YELLOW + "Total Contact Count:" + ConsoleColors.RESET);
        System.out.printf("%s%d contact(s)%s%n%n",
                ConsoleColors.WHITE,
                totalCount,
                ConsoleColors.RESET
        );

        double avgAge = contactDAO.getAverageAge();
        System.out.println(ConsoleColors.YELLOW + "Average Age:" + ConsoleColors.RESET);
        if (avgAge > 0) {
            System.out.printf("%s%.2f years%s%n%n",
                    ConsoleColors.WHITE,
                    avgAge,
                    ConsoleColors.RESET
            );
        } else {
            System.out.println(ConsoleColors.RED + "No contacts with birth date information.\n" + ConsoleColors.RESET);
        }

        Contact youngest = contactDAO.getYoungestContact();
        System.out.println(ConsoleColors.YELLOW + "Youngest Contact:" + ConsoleColors.RESET);
        if (youngest != null && youngest.getBirthDate() != null) {
            Period period = Period.between(youngest.getBirthDate(), LocalDate.now());
            int age = period.getYears();
            System.out.printf("%s %s (Age: %d)%n%n",
                    youngest.getFirstName() != null ? youngest.getFirstName() : "-",
                    youngest.getLastName() != null ? youngest.getLastName() : "-",
                    age
            );
        } else {
            System.out.println(ConsoleColors.RED + "No contact with birth date information.\n" + ConsoleColors.RESET);
        }

        Contact oldest = contactDAO.getOldestContact();
        System.out.println(ConsoleColors.YELLOW + "Oldest Contact:" + ConsoleColors.RESET);
        if (oldest != null && oldest.getBirthDate() != null) {
            Period period = Period.between(oldest.getBirthDate(), LocalDate.now());
            int age = period.getYears();
            System.out.printf("%s %s (Age: %d)%n%n",
                    oldest.getFirstName() != null ? oldest.getFirstName() : "-",
                    oldest.getLastName() != null ? oldest.getLastName() : "-",
                    age
            );
        } else {
            System.out.println(ConsoleColors.RED + "No contact with birth date information.\n" + ConsoleColors.RESET);
        }

        int withLinkedin = contactDAO.countWithLinkedin();
        int withoutLinkedin = contactDAO.countWithoutLinkedin();
        System.out.println(ConsoleColors.YELLOW + "LinkedIn Profile Statistics:" + ConsoleColors.RESET);
        System.out.printf("%sContacts with LinkedIn: %d%s%n",
                ConsoleColors.WHITE,
                withLinkedin,
                ConsoleColors.RESET
        );
        System.out.printf("%sContacts without LinkedIn: %d%s%n%n",
                ConsoleColors.WHITE,
                withoutLinkedin,
                ConsoleColors.RESET
        );

        Map<String, Integer> birthMonthDist = contactDAO.getBirthMonthDistribution();
        System.out.println(ConsoleColors.YELLOW + "Birth Month Distribution:" + ConsoleColors.RESET);
        if (birthMonthDist.isEmpty()) {
            System.out.println(ConsoleColors.RED + "No contacts with birth date information.\n" + ConsoleColors.RESET);
        } else {
            for (Map.Entry<String, Integer> entry : birthMonthDist.entrySet()) {
                System.out.printf("%s%s: %d person(s)%s%n",
                        ConsoleColors.WHITE,
                        entry.getKey(),
                        entry.getValue(),
                        ConsoleColors.RESET
                );
            }
            System.out.println();
        }

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
                    System.out.printf("%s%s: %d person(s)%s%n",
                            ConsoleColors.WHITE,
                            entry.getKey(),
                            count,
                            ConsoleColors.RESET
                    );
                }
            }
            System.out.println();
        }

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
                    System.out.printf("%s%s: %d persons%s%n",
                            ConsoleColors.WHITE,
                            name,
                            count,
                            ConsoleColors.RESET
                    );
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
