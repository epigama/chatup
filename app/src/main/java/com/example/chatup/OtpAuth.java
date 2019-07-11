package com.example.chatup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

import es.dmoral.toasty.Toasty;
import in.aabhasjindal.otptextview.OTPListener;
import in.aabhasjindal.otptextview.OtpTextView;

public class OtpAuth extends AppCompatActivity {
    private OtpTextView otpTextView;
    private TextView otpSentToText;
    private String codeSent;
    private String TAG = this.getClass().getSimpleName();
    FirebaseAuth mAuth;
    String phoneNum = "";
    Button verifyBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_auth);

        mAuth = FirebaseAuth.getInstance();

        verifyBtn = findViewById(R.id.verify_and_proceed);

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        getSupportActionBar().hide();

        otpTextView = findViewById(R.id.otp_view);
        otpSentToText = findViewById(R.id.otpSentToText);

        try {
            Intent intent = getIntent();
            phoneNum = intent.getStringExtra("phone");
            otpSentToText.append(" " + phoneNum);
        }
        catch (Exception e){

        }

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNum,        // Phone number to verify
                20,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);

        otpTextView.requestFocusOTP();
        otpTextView.setOtpListener(new OTPListener() {
            @Override
            public void onInteractionListener() {

            }

            @Override
            public void onOTPComplete(String otp) {
             //   Toasty.success(OtpAuth.this, "The OTP is " + otp, Toast.LENGTH_SHORT).show();
            }
        });

        verifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifySignInCode(otpTextView.getOTP());
            }
        });


    }

    private void verifySignInCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeSent, code);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //here you can open new activity
                            Toast.makeText(getApplicationContext(),
                                    "Successfull", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),ProfileActivity.class));
                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(getApplicationContext(),
                                        "Unsuccessful", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
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