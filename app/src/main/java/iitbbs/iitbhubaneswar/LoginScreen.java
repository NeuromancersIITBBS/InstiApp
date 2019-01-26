package iitbbs.iitbhubaneswar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginScreen extends Activity {

    private static int SPLASH_TIME_OUT = 0;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);

        firebaseAuth = FirebaseAuth.getInstance();
    }

    public void onLogin(View view) {
        String email = ((TextView) findViewById(R.id.login_id)).getText() + "@iitbbs.ac.in";
        String password = ((TextView) findViewById(R.id.login_password)).getText() + "";

        firebaseAuth.signInWithEmailAndPassword(email, password).
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
        String email = ((TextView) findViewById(R.id.login_id)).getText() + "@iitbbs.ac.in";
        String password = ((TextView) findViewById(R.id.login_password)).getText() + "";

        firebaseAuth.createUserWithEmailAndPassword(email, password).
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
