package neuromancers.iitbbs.iitbhubaneswar;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
/*This class displays the table of lost items requested/ lost and found items*/
public class LostAndFoundTable extends Fragment {
    private FrameLayout framelayout;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_table_lost_and_found, container, false);
        framelayout = (FrameLayout)v.findViewById( R.id.framelayout );
        getActivity().setTitle("Lost and Found");
        return v;
    }
}
