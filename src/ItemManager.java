import java.util.Scanner;

public class ItemManager {
    private final DAO dao;
    private final Scanner sc;

    public ItemManager(DAO dao, Scanner sc) {
        this.dao = dao;
        this.sc = sc;
    }

    public void reportMissing() {
        System.out.println("---Report Missing Item---");
        String itemName = InputUtil.readLine(sc, "Enter Item Name: ");
        String description = InputUtil.readLine(sc, "Enter Item Description or Proof of Ownership: ");
        String color = InputUtil.readLine(sc, "Enter Item Color :");
        String category = InputUtil.readLine(sc, "Enter Item Category :");
        int userId = InputUtil.readInt(sc, "Enter your user ID :");
        String location = InputUtil.readLine(sc, "Enter Location of Item :");
        dao.addItem(new LostItem(itemName, description, color, category, userId, location));
    }

    public void reportFound() {
        System.out.println("---Report Found Item---");
        String itemName = InputUtil.readLine(sc, "Enter Item Name: ");
        String description = InputUtil.readLine(sc, "Enter Item Description: ");
        String color = InputUtil.readLine(sc, "Enter Item Color :");
        String category = InputUtil.readLine(sc, "Enter Item Category :");
        int userId = InputUtil.readInt(sc, "Enter your user ID :");
        String location = InputUtil.readLine(sc, "Enter Location where item was found :");
        dao.addItem(new FoundItem(itemName, description, color, category, userId, location));
    }
}
