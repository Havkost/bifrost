package ASTVisitor;

public class ANSI {
    public static final String RESET = "\u001B[0m";
    public static final String BLACK = "\u001B[30m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String PURPLE = "\u001B[35m";
    public static final String CYAN = "\u001B[36m";
    public static final String WHITE = "\u001B[37m";

    public static String green(String str) {
        return GREEN + str + RESET;
    }

    public static String black(String str) {
        return BLACK + str + RESET;
    }

    public static String red(String str) {
        return RED + str + RESET;
    }

    public static String yellow(String str) {
        return YELLOW + str + RESET;
    }

    public static String blue(String str) {
        return BLUE + str + RESET;
    }

    public static String purple(String str) {
        return PURPLE + str + RESET;
    }

    public static String cyan(String str) {
        return CYAN + str + RESET;
    }

    public static String white(String str) {
        return WHITE + str + RESET;
    }
}
