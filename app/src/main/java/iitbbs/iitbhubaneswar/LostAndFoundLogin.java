package iitbbs.iitbhubaneswar;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
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
import com.google.firebase.auth.FirebaseUser;

/*This is the login class. Not completed*/
public class LostAndFoundLogin extends Fragment {
    private FrameLayout framelayout;
    private EditText loginUser, loginPassword;
    private Button loginButton, regBridge;
    private TextView userLogin;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_login_lost_and_found, container, false);
        framelayout = (FrameLayout) v.findViewById(R.id.framelayout);
        getActivity().setTitle("Lost and Found");
        setUILoginViews(v);
        loginPassword.setTransformationMethod(new AsteriskPasswordTransformationMethod());
        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(getActivity());
        FirebaseUser user = firebaseAuth.getCurrentUser();
        //This code keeps the user logged in.(working)

       /* if(user != null){
            Fragment lostAndFound = new LostAndFoundTable();
            if (lostAndFound != null) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction ft = fragmentManager.beginTransaction();
                ft.replace(R.id.content_frame, lostAndFound);
                ft.commit();
            }
        }*/
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate();
            }
        });
        return v;
    }

    private void setUILoginViews(View v) {
        loginUser = v.findViewById(R.id.login_name);
        loginPassword = v.findViewById(R.id.login_password);
        loginButton = v.findViewById(R.id.login_button);
    }

    private Boolean validate() {
        String name = loginUser.getText().toString();
        String password = loginPassword.getText().toString();
        progressDialog.setMessage("Signing In");
        progressDialog.show();
        firebaseAuth.signInWithEmailAndPassword(name, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Fragment lostAndFound = new LostAndFoundTable();
                    if (lostAndFound != null) {
                        progressDialog.dismiss();
                        FragmentManager fragmentManager = getFragmentManager();
                        FragmentTransaction ft = fragmentManager.beginTransaction();
                        ft.replace(R.id.content_frame, lostAndFound);
                        ft.commit();
                    }
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(getActivity(), "Login Failed, Check user name and password", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return null;
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
