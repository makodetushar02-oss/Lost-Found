import java.util.Scanner;

public class Main {
    // FIX: admin password moved to one constant. Better: read from an env variable or store
    // a hashed password / "Admin" role in the users table later.
    private static final String ADMIN_PASSWORD = System.getenv("LF_ADMIN_PASSWORD") != null ? System.getenv("LF_ADMIN_PASSWORD") : "Tushar@123";

    // FIX: main must be "public static void main(String[] args)". Without "public" the JVM
    // may not find it (older Java versions give "Main method not found").
    public static void main(String[] args) {
        int choice = 0;
        DAO dao = new DAO();
        Scanner sc = new Scanner(System.in);
        ItemManager im = new ItemManager(dao, sc);
        // FIX: ClaimManager now receives dao and scanner (it had dao = null before -> NullPointerException).
        ClaimManager cm = new ClaimManager(dao, sc);

        do {
            System.out.println("-----Welcome to Lost & Found Platform------");
            System.out.println("1. User registration");
            System.out.println("2. Report missing item");
            System.out.println("3. Report found item");
            System.out.println("4. Claim a lost item");
            System.out.println("5. Admin Panel");
            System.out.println("6. Exit from the program");
            // FIX: readInt avoids InputMismatchException and the leftover-newline problem.
            choice = InputUtil.readInt(sc, "Enter your choice : 1-6");

            switch (choice) {
                case 1: {
                    System.out.println("----Welcome to user registration----");
                    // FIX: removed the stray sc.nextLine() that was needed only because of nextInt().
                    String name = InputUtil.readLine(sc, "Enter your name:");
                    String phNo = InputUtil.readLine(sc, "Enter your phone number:");
                    String email = InputUtil.readLine(sc, "Enter your email:");
                    User user = new User(name, phNo, email, "Student");
                    dao.addUser(user);
                    break;
                }
                case 2:
                    im.reportMissing();
                    break;
                case 3:
                    im.reportFound();
                    break;
                case 4:
                    cm.claim();
                    break;
                case 5: {
                    // FIX: old code used sc.nextLine() right after nextInt(), so the password was
                    // always read as an empty string and login never worked.
                    String entered = InputUtil.readLine(sc, "Enter your password:");
                    if (ADMIN_PASSWORD.equals(entered)) {
                        // FIX: claim ID was read but never used. Now passed to dao.updateClaim().
                        int claimId = InputUtil.readInt(sc, "Enter claim id to approve/reject:");
                        int action = InputUtil.readInt(sc, "Enter 1 to approve claim or 2 to reject claim");
                        if (action == 1) dao.updateClaim(claimId, "Approved");
                        else if (action == 2) dao.updateClaim(claimId, "Rejected");
                        else System.out.println("Invalid option.");
                    } else {
                        System.out.println("Unauthorized access");
                    }
                    // FIX: missing 'break' - after approving a claim the code fell through into
                    // case 6 and exited the whole program.
                    break;
                }
                case 6:
                    System.out.println("Exiting......");
                    break; // FIX: loop ends naturally; no System.exit(0) needed.
                default:
                    // FIX: added default so invalid numbers (e.g. 9) give a message.
                    System.out.println("Invalid choice, enter a number from 1 to 6.");
            }
        } while (choice != 6);

        sc.close();
    }
}
