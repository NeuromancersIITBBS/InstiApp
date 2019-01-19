package iitbbs.iitbhubaneswar;

import android.app.Activity;
import android.support.annotation.Keep;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class LostAndFoundItemsList extends ArrayAdapter<LostAndFoundItems> {
    public Activity context;
    public List<LostAndFoundItems> itemsList;
    @Keep
    public LostAndFoundItemsList(Activity context, List<LostAndFoundItems> itemsList){
        super(context,R.layout.list_layout,itemsList);
        this.context = context;
        this.itemsList = itemsList;
    }
    @Keep
    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.list_layout,null,true);
        TextView itemText = listViewItem.findViewById(R.id.item);
        LostAndFoundItems item = itemsList.get(position);
        itemText.setText(item.itemName);
        return  listViewItem;
    }
}
