package neuromancers.iitbbs.iitbhubaneswar;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
/*This is the login class. Not completed*/
public class LostAndFoundLogin extends Fragment {
    private FrameLayout framelayout;
    private EditText loginUser, loginPassword, regUser, regPassword;
    private Button regButton,regBridge;
    private TextView userLogin;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_login_lost_and_found, container, false);
        framelayout = (FrameLayout)v.findViewById( R.id.framelayout );
        getActivity().setTitle("Lost and Found");
        setUILoginViews(v);
        return v;
    }
    private void setUILoginViews(View v){
        loginUser = v.findViewById(R.id.login_name);
        loginPassword = v.findViewById(R.id.login_password);
        regUser = v.findViewById(R.id.reg_name);
        regPassword = v.findViewById(R.id.reg_password);
        regButton = v.findViewById(R.id.reg_button);
        regButton = v.findViewById(R.id.reg_bridge);
    }
}
