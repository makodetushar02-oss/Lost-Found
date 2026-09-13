public class Found_Item extends Item {
        public Found_Item(String itemName, String itemDescription, String color, String category, String Location , int userId){
            super(itemName , itemDescription , color , category, Location, userId);
        }
        public Found_Item(int ItemId, String ItemName, String itemDescription, String color, String category, String Location, int userId){
            super(ItemId , ItemName , itemDescription , color , category,Location, userId);
        }
        @Override
        public String getType() {
            return "Found_item";
        }

        @Override
        public void getDescription() {
            System.out.println( ItemId + "||" + ItemId + "||" + ItemDescription + "||" + color + "||" + userId + "||" + category + "||" + Location);
        }
    }

}
