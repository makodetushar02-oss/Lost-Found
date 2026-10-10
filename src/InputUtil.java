
    import java.util.Scanner;

    // NEW FILE. Fixes the classic Scanner bug: after sc.nextInt() the "Enter" key stays in the
// buffer, so the next sc.nextLine() returns an empty string. Your code worked around this with
// random sc.nextLine() calls, which broke in several places (e.g. admin password was read as "").
// Rule now: ALWAYS read a full line, then convert it. No stray nextLine() calls needed anywhere.
    public class InputUtil {

        public static int readInt(Scanner sc, String prompt) {
            while (true) {
                System.out.println(prompt);
                String line = sc.nextLine().trim();
                try {
                    return Integer.parseInt(line);
                } catch (NumberFormatException e) {
                    // FIX: typing letters no longer crashes the program with InputMismatchException.
                    System.out.println("Please enter a valid number.");
                }
            }
        }

        public static String readLine(Scanner sc, String prompt) {
            System.out.println(prompt);
            return sc.nextLine().trim();
        }
    }


