package iitbbs.iitbhubaneswar;

import android.support.annotation.Keep;

import com.google.firebase.database.IgnoreExtraProperties;


/*This is the item class for upload/viewing of a lost/lost and found item*/
@IgnoreExtraProperties
public class LostAndFoundItems  {

    public String itemName;
//    public int imageID;

    public LostAndFoundItems(String t)
    {
        itemName = t;
//        imageID = i;

    }

    public String getItemName() {
        return itemName;
    }

    public LostAndFoundItems(){

        itemName = "NaN";
    }

    public String toString() {
        return itemName;
    }
}
