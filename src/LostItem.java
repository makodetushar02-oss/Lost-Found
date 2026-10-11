public class LostItem extends Item {
    public LostItem(String itemName, String description, String color,
                    String category, int userId, String location) {
        super(itemName, description, color, category, userId, location);
    }

    public LostItem(int itemId, String itemName, String description, String color,
                    String category, int userId, String location) {
        super(itemId, itemName, description, color, category, userId, location);
    }

    @Override
    public String getType() {
        return "LOST_ITEM";
    }
}
