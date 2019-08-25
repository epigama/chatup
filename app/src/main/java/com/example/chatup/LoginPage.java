package com.example.chatup;

import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginPage extends AppCompatActivity {
    int currentdaynight;
    private String TAG = this.getClass().getSimpleName();
    EditText email, password;
    Button signin;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_via_email);
        getSupportActionBar().hide();
        currentdaynight= AppCompatDelegate.getDefaultNightMode();

        //intialising the variables to the xml ids
        email = findViewById(R.id.login_email);
        password = findViewById(R.id.login_password);
        signin = findViewById(R.id.login_using_email);

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInWithEmail();
                Toast.makeText(getApplicationContext(), "Btn clicked", Toast.LENGTH_SHORT).show();
            }
        });
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
    }
    //SignInWithEmail OPTION
    private void signInWithEmail(){
        mAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            Toast.makeText(LoginPage.this, "Sign is success", Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                            //TODO 2 updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginPage.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //TODO 3 updateUI(null);
                        }
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //TODO updateUI(currentUser);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if(currentdaynight!=AppCompatDelegate.getDefaultNightMode()){
            recreate();
        }
    }
}
