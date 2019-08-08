package com.example.chatup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Settings extends AppCompatActivity {
    TextView edit_profile;
    TextView userName;
    TextView bio;
    TextView invite;
    TextView notifications;
    TextView faq;
    TextView feedback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportActionBar().setTitle("Settings");
        invite=findViewById(R.id.Settings_Invite);
        notifications=findViewById(R.id.Settings_Notifications);
        faq=findViewById(R.id.Settings_Faq);
        feedback=findViewById(R.id.Settings_Feedback);


        userName = findViewById(R.id.Settings_Name);
        bio = findViewById(R.id.Settings_Bio);

        edit_profile=findViewById(R.id.EditProfile);
        edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Settings.this,ProfilePage.class));
            }
        });

        userName.setText(UserDetails.getUsername());
        bio.setText(UserDetails.getBio());
        faq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Settings.this,FAQ.class));
            }
        });

        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new );
            }
        });



    }
}
