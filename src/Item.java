public abstract class Item {
    protected int itemId;
    protected String itemName;
    protected String description;
    protected String color;
    protected String category;
    protected int userId;
    protected String location;

    // Used when reading from the database (id already known).
    public Item(int itemId, String itemName, String description, String color,
                String category, int userId, String location) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.description = description;
        this.color = color;
        this.category = category;
        this.userId = userId;
        this.location = location;
    }

    // Used when creating a new item (database generates the id).
    public Item(String itemName, String description, String color,
                String category, int userId, String location) {
        this(0, itemName, description, color, category, userId, location);
    }

    public int getItemId() { return itemId; }
    public void setItemId(int itemId) { this.itemId = itemId; }

    public String getItemName() { return itemName; }
    public void setItemName(String itemName) { this.itemName = itemName; }

    // DAO and WebServer call getDes(); getItemDescription() kept as an alias.
    public String getDes() { return description; }
    public String getItemDescription() { return description; }
    public void setItemDescription(String description) { this.description = description; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    // Must return "LOST_ITEM" or "FOUND_ITEM" (matches the ENUM in schema.sql).
    public abstract String getType();

    public void printDetails() {
        System.out.println(itemId + " || " + itemName + " || " + description + " || " + color
                + " || " + category + " || user " + userId + " || " + location);
    }
}
