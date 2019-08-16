package com.example.chatup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

public class DarkMode extends AppCompatActivity {
    Button darkmode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            setTheme(R.style.dark_theme);
        } else {
            setTheme(R.style.AppTheme);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dark_mode);
        darkmode=findViewById(R.id.dark_mode);
        darkmode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES || UserDetails.getDarkSwitch() == 0) {
                    darkMode();
                } else {
                   lightMode();
                }
                finish();
                startActivity(new Intent(DarkMode.this,DarkMode.this.getClass()));
            }
        });
    }

    private void darkMode(){
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setTheme(R.style.dark_theme);
        UserDetails.setDarkSwitch(1);
    }

    private void lightMode(){
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        setTheme(R.style.AppTheme);
        UserDetails.setDarkSwitch(0);

    }
}
