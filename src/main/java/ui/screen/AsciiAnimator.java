package ui.screen;

/**
 * Handles ASCII art animations displayed at application startup and logout.
 * Shows intro & outro animations with Star Wars-style effects.
 */
public class AsciiAnimator {

    private static final String RESET   = "\u001B[0m";
    private static final String BLUE    = "\u001B[34m";
    private static final String CYAN    = "\u001B[36m";
    private static final String YELLOW  = "\u001B[33m";
    private static final String WHITE   = "\u001B[97m";
    private static final String MAGENTA = "\u001B[35m";

    // ----------------------------------------------------------
    // INTRO ART
    // ----------------------------------------------------------

    private static final String PRELUDE_ART = YELLOW + """
                    ______   ______    _______   _______
                   /      | /  __  \\  |       \\ |   ____|
                  |  ,----'|  |  |  | |  .--.  ||  |__
                  |  |     |  |  |  | |  |  |  ||   __|
                  |  `----.|  `--'  | |  '--'  ||  |____
                   \\______| \\______/  |_______/ |_______|

            ____    __    ____  ___      .______          _______.
            \\   \\  /  \\  /   / /   \\     |   _  \\        /       |
             \\   \\/    \\/   / /  ^  \\    |  |_)  |      |   (----`
              \\            / /  /_\\  \\   |      /        \\   \\
               \\    /\\    / /  _____  \\  |  |\\  \\----.----)   |
                \\__/  \\__/ /__/     \\__\\ | _| `._____|_______/
            """ + RESET;

    private static final String TITLE_TEXT = BLUE + """
              _  __         _ _        _    _             _   _                         _ _            _
             | |/ /        | (_)      | |  | |           (_) (_)                       (_) |          (_)
             | ' / __ _  __| |_ _ __  | |__| | __ _ ___  | | | |_ ____   _____ _ __ ___ _| |_ ___  ___ _
             |  < / _` |/ _` | | '__| |  __  |/ _` / __| | | | | '_ \\ \\ / / _ \\ '__/ __| | __/ _ \\/ __| |
             | . \\ (_| | (_| | | |    | |  | | (_| \\__ \\ | |_| | | | \\ V /  __/ |  \\__ \\ | ||  __/\\__ \\ |
             |_|\\_\\__,_|\\__,_|_|_|    |_|  |_|\\__,_|___/  \\___/|_| |_|\\_/ \\___|_|  |___/_|\\__\\___||___/_|

            """ + RESET
            + YELLOW + center("""
                       CODE WARS - CHAPTER 2
                    DR. ILKTAN AR VS GROUP -25
                        - NOT THIS TIME -
                    """, 80) + RESET;

    private static final String[] CRAWL_LINES = {
            "",
            YELLOW + "A long time ago, in a distant lab of Kadir Has University..." + RESET,
            "",
            WHITE + "A role-based communication system was being developed," + RESET,
            WHITE + "destined to change the fate of its students forever." + RESET,
            "",
            CYAN + "Testers were hunting bugs relentlessly..." + RESET,
            CYAN + "Juniors were full of hope..." + RESET,
            CYAN + "Seniors defended the code with caffeine and wisdom..." + RESET,
            CYAN + "Managers, as always... were in a meeting." + RESET,
            "",
            MAGENTA + "And now, you are about to enter the console version of this system..." + RESET,
            ""
    };


    // ----------------------------------------------------------
    // CORE UTILITIES
    // ----------------------------------------------------------

    public static void clearScreen() {
        try {
            if (System.getProperty("os.name").contains("Windows"))
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            else
                new ProcessBuilder("clear").inheritIO().start().waitFor();
        } catch (Exception e) {
            System.out.println("Console cannot be cleared: " + e.getMessage());
        }
    }

    private static void sleep(long ms) {
        try { Thread.sleep(ms); }
        catch (InterruptedException e) { Thread.currentThread().interrupt(); }
    }

    private static String center(String text, int width) {
        if (text == null) return "";
        int len = text.length();
        if (len >= width) return text;
        return " ".repeat((width - len) / 2) + text;
    }


    // ----------------------------------------------------------
    // INTRO SEQUENCE
    // ----------------------------------------------------------

    private static void showPrelude() {
        clearScreen();
        for (String line : PRELUDE_ART.split("\n")) {
            System.out.println(center(YELLOW + line + RESET, 80));
            sleep(150);
        }
        sleep(1500);
    }

    private static void showStarfield(int frames) {
        int width = 90, height = 25;
        char[] stars = ".+*".toCharArray();

        for (int f = 0; f < frames; f++) {
            clearScreen();
            StringBuilder sb = new StringBuilder();
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    sb.append(Math.random() < 0.02 ? WHITE + stars[(int)(Math.random()*stars.length)] + RESET : " ");
                }
                sb.append("\n");
            }
            System.out.print(sb);
            sleep(80);
        }
    }

    private static void showTitle() {
        clearScreen();
        for (String line : TITLE_TEXT.split("\n")) {
            System.out.println(center(BLUE + line + RESET, 90));
            sleep(200);
        }
        sleep(2500);
    }

    private static void showCrawl() {
        int screenH = 30, total = CRAWL_LINES.length;

        for (int frame = 0; frame < screenH + total; frame++) {
            clearScreen();
            for (int i = 0; i < total; i++) {
                int pos = i + screenH - frame;
                if (pos >= 0 && pos < screenH) {
                    int indent = (int)((pos / (float) screenH) * 22);
                    System.out.println(" ".repeat(indent) + center(CRAWL_LINES[i], 40));
                }
            }
            sleep(500);
        }
    }

    public static void runIntro() {
        showPrelude();
        showStarfield(25);
        showTitle();
        showCrawl();
    }


    // ----------------------------------------------------------
    // OUTRO SEQUENCE
    // ----------------------------------------------------------

    public static void runOutro() {
        showStarfield(15);
        clearScreen();
        showOutroTitle();
        fadeOutGrade();
    }


    private static void showOutroTitle() {

        // === GROUP 25 block ===
        String groupBlock = CYAN + """
 _____                         _____  _____  
|  __ \\                       / __  \\|  ___| 
| |  \\/_ __ ___  _   _ _ __   `' / /'|___ \\  
| | __| '__/ _ \\| | | | '_ \\    / /      \\ \\ 
| |_\\ \\ | | (_) | |_| | |_) | ./ /___/\\__/ / 
 \\____/_|  \\___/ \\__,_| .__/  \\_____/\\____/  
                      | |                    
                      |_|        GROUP 25     
""" + RESET;

        System.out.println(center(groupBlock, 95));
        sleep(700);

        // TEAM NAMES
        String[] names = {
                "SEZAİ ARAPLARLI",
                "TUNAHAN TUZE",
                "AYHAN ÖNER",
                "BİLAL KELEŞ"
        };

        for (String n : names) {
            System.out.println(center(MAGENTA + n + RESET, 95));
            sleep(350);
        }

        // EXTRA SPACING
        System.out.println("\n\n");
        sleep(400);

        // === COMPACT ASCII "MAY THE GRADE BE WITH US" ===
        String mayGrade = CYAN + """
▙▗▌       ▐  ▌                 ▌    ▌          ▗▐  ▌         
▌▘▌▝▀▖▌ ▌ ▜▀ ▛▀▖▞▀▖ ▞▀▌▙▀▖▝▀▖▞▀▌▞▀▖ ▛▀▖▞▀▖ ▌  ▌▄▜▀ ▛▀▖ ▌ ▌▞▀▘
▌ ▌▞▀▌▚▄▌ ▐ ▖▌ ▌▛▀  ▚▄▌▌  ▞▀▌▌ ▌▛▀  ▌ ▌▛▀  ▐▐▐ ▐▐ ▖▌ ▌ ▌ ▌▝▀▖
▘ ▘▝▀▘▗▄▘  ▀ ▘ ▘▝▀▘ ▗▄▘▘  ▝▀▘▝▀▘▝▀▘ ▀▀ ▝▀▘  ▘▘ ▀▘▀ ▘ ▘ ▝▀▘▀▀ 
""" + RESET;

        System.out.println(center(mayGrade, 95));
        sleep(1500);
    }


    private static void fadeOutGrade() {
        for (int i = 0; i < 6; i++) {
            clearScreen();
            String color = switch (i) {
                case 0 -> CYAN;
                case 1 -> BLUE;
                case 2 -> MAGENTA;
                case 3 -> YELLOW;
                case 4 -> WHITE;
                default -> RESET;
            };
            System.out.println("\n".repeat(10));
            System.out.println(center(color + "May the grade be with us..." + RESET, 85));
            sleep(650);
        }
    }
}
