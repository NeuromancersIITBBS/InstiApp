package neuromancers.iitbbs.iitbhubaneswar;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
/*This is the registration class. Registrations can be done currently*/
public class LostAndFoundRegistration extends Fragment {
    private FrameLayout framelayout;
    private EditText loginUser, loginPassword, regUser, regPassword;
    private Button regButton,regBridge;
    private TextView userLogin;
    private FirebaseAuth firebaseAuth;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_reg_lost_and_found, container, false);
        framelayout = (FrameLayout)v.findViewById( R.id.framelayout );
        getActivity().setTitle("Lost and Found");
        setUIRegViews(v);
        firebaseAuth = FirebaseAuth.getInstance();
        regPassword.setTransformationMethod(new AsteriskPasswordTransformationMethod());
        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate()){
                    String userName = regUser.getText().toString().trim();
                    String userPassword = regPassword.getText().toString().trim();
                    firebaseAuth.createUserWithEmailAndPassword(userName,userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(getActivity(),"Registration Successful",Toast.LENGTH_SHORT).show();
                                Fragment lostAndFound = new LostAndFoundTable();
                                if (lostAndFound != null) {
                                    FragmentManager fragmentManager = getFragmentManager();
                                    FragmentTransaction ft = fragmentManager.beginTransaction();
                                    ft.replace(R.id.content_frame, lostAndFound);
                                    ft.commit();
                                }
                            }
                            else
                                Toast.makeText(getActivity(),"Registration Failed",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
        regBridge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment lostAndFound = new LostAndFoundLogin();
                if (lostAndFound != null) {
                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction ft = fragmentManager.beginTransaction();
                    ft.replace(R.id.content_frame, lostAndFound);
                    ft.commit();
                }
            }
        });
        return v;
    }
    private Boolean validate(){
        Boolean result = false;
        String name = regUser.getText().toString();
        String password = regPassword.getText().toString();
        if(name.isEmpty()||password.isEmpty()){
            if(name.isEmpty())
                regUser.setError("Please enter user name");
            else
                regPassword.setError("Please enter password");
        }
        else
            result=true;

        return result;
    }
    private void setUIRegViews(View v){
        loginUser = v.findViewById(R.id.login_name);
        loginPassword = v.findViewById(R.id.login_password);
        regUser = v.findViewById(R.id.reg_name);
        regPassword = v.findViewById(R.id.reg_password);
        regButton = v.findViewById(R.id.reg_button);
        regBridge = v.findViewById(R.id.reg_bridge);
    }
    public class AsteriskPasswordTransformationMethod extends PasswordTransformationMethod {
        @Override
        public CharSequence getTransformation(CharSequence source, View view) {
            return new PasswordCharSequence(source);
        }

        private class PasswordCharSequence implements CharSequence {
            private CharSequence mSource;
            public PasswordCharSequence(CharSequence source) {
                mSource = source; // Store char sequence
            }
            public char charAt(int index) {
                return '*'; // This is the important part
            }
            public int length() {
                return mSource.length(); // Return default
            }
            public CharSequence subSequence(int start, int end) {
                return mSource.subSequence(start, end); // Return default
            }
        }
    }
}
