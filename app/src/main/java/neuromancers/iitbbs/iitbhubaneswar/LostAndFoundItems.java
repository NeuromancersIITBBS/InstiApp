package neuromancers.iitbbs.iitbhubaneswar;
/*This is the item class for upload/viewing of a lost/lost and found item*/
public class LostAndFoundItems {
    String itemName;
    public int imageID;
    public LostAndFoundItems(String t, int i)
    {
        itemName = t;
        imageID = i;

    }

    public String toString()
    {
        return  itemName+" " + String.valueOf(imageID);
    }
}
