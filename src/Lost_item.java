public class Lost_item extends Item{
    public Lost_item( String itemName, String itemDescription, String color, String category,String Location, int userId){
        super(itemName , itemDescription , color , category, Location, userId);
    }
    public Lost_item(int itemId, String itemName, String itemDescription, String color, String category,String Location, int userId){
        super(itemId , itemName , itemDescription , color , category, Location, userId);
    }
    @Override
    public String getType() {
        return "Lost_item";
    }

    @Override
    public void getDescription() {
        System.out.println( ItemId + "||" + ItemId + "||" + ItemDescription + "||" + color + "||" + userId + "||" + category + "||" + Location);
    }
}
