package com.example.chatup.Activities;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.chatup.Constants.Constants;
import com.example.chatup.Models.UserDetails;
import com.example.chatup.R;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hbb20.CountryCodePicker;

import java.util.concurrent.TimeUnit;

public class PhoneAuthentication extends AppCompatActivity {
    //TAG
    public String TAG = this.getClass().getSimpleName();

    //DECLARING VARIABLES
    EditText editTextPhone, editTextCode;
    Button signInButton;
    String codeSent;
    private String phoneNum;
    CountryCodePicker cpp;


    //FRIEBASE AUTHENTICATION VARIABLE
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_authentication);

        mAuth = FirebaseAuth.getInstance();

        if(mAuth.getCurrentUser() != null){
            //Already signed in
            Toast.makeText(this, "Signed in already", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "onCreate: " + "Signed in already");
            Intent intent = new Intent(this, UsersAndChatsActivity.class);
            startActivity(intent);
        }

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);


//        editTextCode = findViewById(R.id.editTextCode);
        editTextPhone = findViewById(R.id.editTextPhone);
//
        signInButton = findViewById(R.id.sign_in_btn);
        cpp=findViewById(R.id.CodePicker);
        cpp.registerCarrierNumberEditText(editTextPhone);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendVerificationCode();
                Intent intent = new Intent(PhoneAuthentication.this, OtpAuth.class);
                intent.putExtra("phone", phoneNum);
                UserDetails.phoneNum = phoneNum;
                startActivity(intent);
            }
        });
    }

    private void sendVerificationCode() {
        phoneNum= cpp.getFullNumberWithPlus();

        phoneNum = "+91" + editTextPhone.getText().toString();
        //DEFAULT +91 SO THAT USER SHOULD NOT ENTER EVERYTIME


        //Write to sharedpreferences
        SharedPreferences prefs = getSharedPreferences(Constants.SHARED_PREFS_NAME, MODE_PRIVATE);
        prefs.edit().putString("phone_number", phoneNum).apply();

        if (phoneNum.isEmpty()) {
            editTextPhone.setError("Phone number is required");
            editTextPhone.requestFocus();
            return;
        }

        if (phoneNum.length() < 10) {
            editTextPhone.setError("Please enter a valid phone");
            editTextPhone.requestFocus();
            return;
        }


        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNum,        // Phone number to verify
                20,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
    }


    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            Toast.makeText(getApplicationContext(), "OTP verification success", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "onVerificationCompleted: " + "OTP verification success");
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(getApplicationContext(), "OTP verification failed", Toast.LENGTH_SHORT).show();
            Log.w(TAG, "onVerificationFailed", e);

            if (e instanceof FirebaseAuthInvalidCredentialsException) {
                Log.d(TAG, "onVerificationFailed: " + "Invalid request");
                // ...
            } else if (e instanceof FirebaseTooManyRequestsException) {
                // The SMS quota for the project has been exceeded
                Log.d(TAG, "onVerificationFailed: " + "SMS Quota over");
                // ...
            }
        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            Log.d(TAG, "onCodeSent: " + "OTP = " + s);
            codeSent = s;
        }
    };
}