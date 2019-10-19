package iitbbs.iitbhubaneswar;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import com.google.android.material.snackbar.Snackbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginScreen extends Activity {

    private static int SPLASH_TIME_OUT = 0;
    private FirebaseAuth firebaseAuth;

    private EditText email = null;
    private EditText password = null;
    private Switch rememberSwitch = null;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editSharedPreferences;
    boolean rememberMe = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);

        firebaseAuth = FirebaseAuth.getInstance();
        email = (EditText) findViewById(R.id.login_id);
        password = (EditText) findViewById(R.id.login_password);
        rememberSwitch = (Switch) findViewById(R.id.remember_me);

        sharedPreferences = getSharedPreferences("credentials", MODE_PRIVATE);
        editSharedPreferences = sharedPreferences.edit();

        rememberMe = sharedPreferences.getBoolean("remember", false);
        if (rememberMe == true) {
            email.setText(sharedPreferences.getString("email", ""));
            password.setText(sharedPreferences.getString("password", ""));
            rememberSwitch.setChecked(true);
        }
    }

    public void guestEntry(View view) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(LoginScreen.this, MainActivity.class);
                startActivity(i);

                finish();
            }
        }, SPLASH_TIME_OUT);
    }

    public void onLogin(View view) {
        String emailText = this.email.getText() + "";
        String passwordText = this.password.getText() + "";

        if (rememberSwitch.isChecked()) {
            editSharedPreferences.putBoolean("remember", true);
            editSharedPreferences.putString("email", emailText);
            editSharedPreferences.putString("password", passwordText);
            editSharedPreferences.commit();
            System.out.println("Done");
        } else {
            editSharedPreferences.clear();
            editSharedPreferences.commit();
        }

        emailText += "@iitbbs.ac.in";

        firebaseAuth.signInWithEmailAndPassword(emailText, passwordText).
                addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = firebaseAuth.getCurrentUser();

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Intent i = new Intent(LoginScreen.this, MainActivity.class);
                                    startActivity(i);

                                    finish();
                                }
                            }, SPLASH_TIME_OUT);

                        } else {
                            Snackbar.make(findViewById(R.id.login_id), "Login Failed", Snackbar.LENGTH_SHORT).show();
                            System.out.println(task.getException());
                        }
                    }
                });
    }

    public void onSignup(View view) {
        String emailText = email.getText() + "";
        String passwordText = password.getText() + "";

        if (rememberSwitch.isChecked()) {
            editSharedPreferences.putBoolean("remember", true);
            editSharedPreferences.putString("email", emailText);
            editSharedPreferences.putString("password", passwordText);
            editSharedPreferences.commit();
        } else {
            editSharedPreferences.clear();
            editSharedPreferences.commit();
        }

        emailText += "@iitbbs.ac.in";

        firebaseAuth.createUserWithEmailAndPassword(emailText, passwordText).
                addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = firebaseAuth.getCurrentUser();

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Intent i = new Intent(LoginScreen.this, MainActivity.class);
                                    startActivity(i);

                                    finish();
                                }
                            }, SPLASH_TIME_OUT);

                        } else {
                            Snackbar.make(findViewById(R.id.login_id), "Sign Up Failed", Snackbar.LENGTH_SHORT).show();
                            System.out.println(task.getException());
                        }
                    }
                });
    }

}
