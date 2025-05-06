package tc.tlouro_c.util;

import java.util.Scanner;
import java.util.Set;

public class InputReader {
    private final Scanner scanner;
    private final String promptStr = "> ";

    public void pressEnterToContinue() {
        System.out.print("Press Enter to continue...");
        scanner.nextLine();
    }

    public String nextLine() {
        System.out.print(promptStr);
        return scanner.nextLine();
    }

    public int nextInt() {
        System.out.print(promptStr);
        while (!scanner.hasNextInt()) {
            System.out.println("Please enter a valid integer.");
            scanner.next();
            System.out.print(promptStr);
        }
        int number = scanner.nextInt();
        scanner.nextLine(); // clear leftover newline
        return number;
    }

    public void close() {
        scanner.close();
    } // TODO Integrate Close

    public int nextIntInRange(int min, int max) {
        int choice;
        while (true) {
            choice = nextInt();
            if (choice >= min && choice <= max) {
                return choice;
            } else {
                System.out.println("⚠️ Please enter a number between " + min + " and " + max + ".");
            }
        }
    }

    public String nextStringInRange(int minLength, int maxLength) {
        String input;
        while (true) {
            input = nextLine();
            if (input.length() >= minLength && input.length() <= maxLength) {
                return input;
            } else {
                System.out.println("⚠️ Please enter a string between " + minLength + " and " + maxLength + " characters.");
            }
        }
    }

    public String nextFromOptionsIgnoreCase(Set<String> options) {
        while (true) {
            String input = nextLine().toLowerCase(); // Read user input and normalize
            for (String option : options) {
                if (option.equalsIgnoreCase(input)) {
                    return option; // Return original case option if matched
                }
            }
            System.out.println("⚠️ Invalid input. Allowed options: " + options);
        }
    }


    // Singleton
    private InputReader() {
        scanner = new Scanner(System.in);
    }

    private static class Holder {
        private static final InputReader INSTANCE = new InputReader();
    }

    public static InputReader getInstance() {
        return Holder.INSTANCE;
    }
}
