import java.util.Scanner;

public class Item_Manager {
    private DAO dao;
    private Scanner sc;
    public Item_Manager(DAO dao, Scanner sc) {
        this.dao = dao;
        this.sc = sc;
    }
    public void reportMissing(){
        System.out.println("--- Report Missing Items ---");
        System.out.println("Enter Item Name : ");
        String itemName = sc.nextLine();
        System.out.println("Enter Item Description or Proof of Ownership: ");
        String itemDescription = sc.nextLine();
        System.out.println("Enter Item Color : ");
        String itemColor = sc.nextLine();
        System.out.println("Enter Item Category : ");
        String itemCategory = sc.nextLine();
        System.out.println("Enter user ID : ");
        int userID = sc.nextInt();
        System.out.println("Enter Location of item : ");
        String location = sc.nextLine();
        Lost_item lostItem = new Lost_item(itemName , itemDescription, itemColor, itemCategory , userID , location)
        dao.additem(lostItem);
    }
    public void reportFound(){
        dao.additem();
    }
}
