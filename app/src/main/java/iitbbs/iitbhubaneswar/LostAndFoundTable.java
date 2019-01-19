package iitbbs.iitbhubaneswar;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/*This class displays the table of lost items requested/ lost and found items*/
public class LostAndFoundTable extends Fragment {
    public Button lostUpload;
    public EditText lostInput;
    public ListView lostOutput;
    public FrameLayout framelayout;
    public DatabaseReference databaseItems;
    public List<LostAndFoundItems> lostItemList;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_table_lost_and_found, container, false);
        framelayout = (FrameLayout) v.findViewById(R.id.framelayout);
        getActivity().setTitle("Lost and Found");
        databaseItems = FirebaseDatabase.getInstance().getReference("Lost Items");
        lostUpload = v.findViewById(R.id.lost_upload);
        lostInput = v.findViewById(R.id.lost_input);
        lostOutput = v.findViewById(R.id.lost_output);
        lostItemList = new ArrayList<>();
        lostUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItem();
            }
        });
        return v;
    }
    @Override
    public void onStart(){
        super.onStart();
        databaseItems.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange( DataSnapshot dataSnapshot) {
                lostItemList.clear();
                for(DataSnapshot itemSnapshot : dataSnapshot.getChildren()){
                    LostAndFoundItems item = itemSnapshot.getValue(LostAndFoundItems.class);
                    lostItemList.add(item);
                }
                LostAndFoundItemsList adapter = new LostAndFoundItemsList(getActivity(),lostItemList);
                lostOutput.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void addItem(){
        String lostItem = lostInput.getText().toString();

        if(!lostItem.isEmpty()) {
            String id = databaseItems.push().getKey();
            LostAndFoundItems item = new LostAndFoundItems(lostItem);
            databaseItems.child(id).setValue(item);
        }
        else
        lostInput.setError("This field should not be empty");
    }
}
