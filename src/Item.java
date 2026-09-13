abstract class Item {
    protected int ItemId;
    String ItemName;
    String ItemDescription;
    String color;
    String category;
    String Location;
    int userId;
    public Item(int itemId, String itemName, String itemDescription, String color, String category, String Location ,int userId) {
        this.ItemId = itemId;
        this.ItemName = itemName;
        this.ItemDescription = itemDescription;
        this.color = color;
        this.category = category;
        this.Location = Location;
        this.userId = userId;
    }
    public Item( String itemName, String itemDescription, String color, String category,String Location, int userId) {
        this.ItemName = itemName;
        this.ItemDescription = itemDescription;
        this.color = color;
        this.category = category;
        this.Location = Location;
        this.userId = userId;
    }

    public int getItemId() {
        return ItemId;
    }

    public void setItemId(int itemId) {
        ItemId = itemId;
    }

    public String getItemName() {
        return ItemName;
    }

    public void setItemName(String itemName) {
        ItemName = itemName;
    }

    public String getItemDescription() {
        return ItemDescription;
    }

    public void setItemDescription(String itemDescription) {
        ItemDescription = itemDescription;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public abstract String getType();
    public abstract void getDescription();

}
