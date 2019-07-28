package com.example.chatup;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.stfalcon.smsverifycatcher.OnSmsCatchListener;
import com.stfalcon.smsverifycatcher.SmsVerifyCatcher;

import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import in.aabhasjindal.otptextview.OTPListener;
import in.aabhasjindal.otptextview.OtpTextView;

public class OtpAuth extends AppCompatActivity {

    String phoneNum = "";
    Button verifyBtn;
    FirebaseAuth.AuthStateListener mAuthListener;
    //FIREBASE AUTH
    FirebaseAuth mAuth;
    SmsVerifyCatcher smsVerifyCatcher;
    //TAG
    private String TAG = this.getClass().getSimpleName();
    //DECLARING THE VARIABLES
    private OtpTextView otpTextView;
    private TextView otpSentToText;
    private String codeSent;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            Log.d(TAG, "onVerificationCompleted: " + "OTP verification success");
            if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                Log.d(TAG, "onVerificationCompleted: userid: " + FirebaseAuth.getInstance().getCurrentUser().getUid());
                startActivity(new Intent(getApplicationContext(), ProfilePage.class).putExtra("uid", FirebaseAuth.getInstance().getCurrentUser().getUid()));
            }
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
            }
        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            Log.d(TAG, "onCodeSent: " + "OTP = " + s);
            codeSent = s;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_auth);
        mAuth = FirebaseAuth.getInstance();
        verifyBtn = findViewById(R.id.verify_and_proceed);


        //OTP VERIFICATION CODE
        otpTextView = findViewById(R.id.otp_view);
        otpSentToText = findViewById(R.id.otpSentToText);


        try {
            Intent intent = getIntent();
            phoneNum = intent.getStringExtra("phone");
            otpSentToText.append(" " + phoneNum);
        } catch (Exception e) {
            e.printStackTrace();
        }

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNum,        // Phone number to verify
                20,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        getSupportActionBar().hide();


        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) {
                    Log.d(TAG, "onAuthStateChanged: " + firebaseAuth.getCurrentUser().getUid());
                }
            }
        };



        smsVerifyCatcher = new SmsVerifyCatcher(this, new OnSmsCatchListener<String>() {
            @Override
            public void onSmsCatch(String message) {
                String code = parseCode(message);//Parse verification code
                otpTextView.setOTP(code);//set code in edit text
                //then you can send verification code to server
            }
        });


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
                            if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                                Log.d(TAG, "onComplete: " + FirebaseAuth.getInstance().getCurrentUser().getUid());
                                startActivity(new Intent(getApplicationContext(), ProfilePage.class).putExtra("uid", FirebaseAuth.getInstance().getCurrentUser().getUid()));
                            }
                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(getApplicationContext(),
                                        "Unsuccessful", Toast.LENGTH_SHORT).show();
                            }
                            if(task.getException() instanceof FirebaseTooManyRequestsException){
                                Toast.makeText(getApplicationContext(),
                                        "Too many requests", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);

    }

    private String parseCode(String message) {
        Pattern p = Pattern.compile("(|^)\\d{6}");
        Matcher m = p.matcher(message);
        String code = "";
        while (m.find()) {
            code = m.group(0);
        }
        return code;
    }


}