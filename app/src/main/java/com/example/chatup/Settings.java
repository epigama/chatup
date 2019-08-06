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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportActionBar().setTitle("Settings");

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




    }
}
