import java.util.Scanner;

public class ClaimManager {
    private final DAO dao;
    private final Scanner sc;

    public ClaimManager(DAO dao, Scanner sc) {
        this.dao = dao;
        this.sc = sc;
    }

    public void claim() {
        System.out.println("---Welcome to claim manager---");
        String proof = InputUtil.readLine(sc, "Enter proof of ownership:");
        int userId = InputUtil.readInt(sc, "Enter your user ID:");
        String itemName = InputUtil.readLine(sc, "Enter your item name:");
        String location = InputUtil.readLine(sc, "Enter location where the item was lost:");
        Claim c = new Claim(proof, userId, itemName, location, "Pending");
        dao.addClaim(c);
    }
}
