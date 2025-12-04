//package ui.screen;
public class AsciiAnimator {
    // === ANSI Colors ===
    private static final String RESET = "\u001B[0m";
    private static final String BLUE = "\u001B[34m";
    private static final String CYAN = "\u001B[36m";
    private static final String YELLOW = "\u001B[33m";
    private static final String WHITE = "\u001B[97m";
    private static final String MAGENTA = "\u001B[35m";

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

    // === KADIR HAS ÜNİVERSİTESİ BÜYÜK ASCII BAŞLIK ===
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

    // === Opening Crawl Lines (yavaş yavaş yukarı kayan metin) ===
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

    // === Konsolu temizle ===
    private static void clear() {
        System.out.print("\u001B[H\u001B[2J");
        System.out.flush();
    }

    private static void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    // === Ortaya hizalama ===
    private static String center(String text, int width) {
        if (text == null)
            return "";
        int len = text.length();
        if (len >= width)
            return text;
        int left = (width - len) / 2;
        return " ".repeat(Math.max(left, 0)) + text;
    }

    // === Girişte CODE WARS çerçevesi ===
    private static void showPrelude() {
        clear();
        String[] lines = PRELUDE_ART.split("\n");
        for (String line : lines) {
            System.out.println(center(YELLOW + line + RESET, 80));
            sleep(150); // satır satır yavaş akış
        }
        sleep(1500);
    }

    // === Yıldızlı arka plan ===
    private static void showStarfield(int frames) {
        int width = 90;
        int height = 25;
        char[] stars = ".+*".toCharArray();

        for (int f = 0; f < frames; f++) {
            clear();
            StringBuilder sb = new StringBuilder();

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    if (Math.random() < 0.02) {
                        char c = stars[(int) (Math.random() * stars.length)];
                        sb.append(WHITE).append(c).append(RESET);
                    } else {
                        sb.append(' ');
                    }
                }
                sb.append('\n');
            }

            System.out.print(sb);
            sleep(80);
        }
    }

    // === Kadir Has ASCII başlığı ===
    private static void showTitle() {
        clear();
        String[] lines = TITLE_TEXT.split("\n");
        for (String line : lines) {
            System.out.println(center(BLUE + line + RESET, 90));
            sleep(200); // satır satır yavaşça gelsin
        }
        sleep(2500);
    }

    // === Star Wars crawl efekt ===
    private static void showCrawl() {
        int screenH = 30;
        int total = CRAWL_LINES.length;

        for (int frame = 0; frame < screenH + total; frame++) {
            clear();

            for (int i = 0; i < total; i++) {
                int pos = i + screenH - frame;

                if (pos >= 0 && pos < screenH) {
                    int indent = (int) ((pos / (float) screenH) * 22);
                    String line = CRAWL_LINES[i];
                    System.out.println(" ".repeat(indent) + center(line, 40));
                }
            }

            sleep(500);
        }
    }

    // === Tüm intro akışı ===
    public static void runIntro() {

        showPrelude();
        showStarfield(25);
        showTitle();
        showCrawl();
    }

    public static void main(String[] args) {
        runIntro();
    }
}
