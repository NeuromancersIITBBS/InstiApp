package neuromancers.iitbbs.iitbhubaneswar;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class NavFragment extends Fragment {

    private int navLayout;

    public void setNewLayout(int navLayout) {
        this.navLayout = navLayout;
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup container, Bundle savedInstanceState) {
        View view = layoutInflater.inflate(navLayout, container, false);
        return view;
    }
}
