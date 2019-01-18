package iitbbs.iitbhubaneswar;

/*This is the item class for upload/viewing of a lost/lost and found item*/
public class LostAndFoundItems {
    String itemName;
//    public int imageID;
    public LostAndFoundItems(String t)
    {
        itemName = t;
//        imageID = i;

    }
    public LostAndFoundItems(){
        itemName = "NaN";
    }
    public String toString() {
        return itemName;
    }
}
