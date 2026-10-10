// FIX: removed "import com.mysql.cj.xdevapi.JsonParser" - unused, and it breaks compilation
// if the MySQL connector jar is not on the classpath.
import java.util.Scanner;

public class ItemManager {
    private DAO dao;
    private Scanner sc;

    public ItemManager() {}

    public ItemManager(DAO dao, Scanner sc) {
        this.dao = dao;
        this.sc = sc;
    }

    public void reportMissing() {
        System.out.println("---Report Missing Item---");
        // FIX: the old code called sc.nextLine() right after the first prompt, which swallowed
        // the user's real input (item name) and made them type it twice. Removed.
        String itemName = InputUtil.readLine(sc, "Enter Item Name: ");
        String description = InputUtil.readLine(sc, "Enter Item Description or Proof of Ownership: ");
        String color = InputUtil.readLine(sc, "Enter Item Color :");
        String category = InputUtil.readLine(sc, "Enter Item Category :");
        int userId = InputUtil.readInt(sc, "Enter your user ID :");
        String location = InputUtil.readLine(sc, "Enter Location of Item :");
        LostItem lostItem = new LostItem(itemName, description, color, category, userId, location);
        dao.addItem(lostItem);
    }

    public void reportFound() {
        System.out.println("---Report Found Item---");
        String itemName = InputUtil.readLine(sc, "Enter Item Name: ");
        String description = InputUtil.readLine(sc, "Enter Item Description: ");
        String color = InputUtil.readLine(sc, "Enter Item Color :");
        String category = InputUtil.readLine(sc, "Enter Item Category :");
        int userId = InputUtil.readInt(sc, "Enter your user ID :");
        String location = InputUtil.readLine(sc, "Enter Location where item was found :");
        FoundItem foundItem = new FoundItem(itemName, description, color, category, userId, location);
        dao.addItem(foundItem);
    }
}
