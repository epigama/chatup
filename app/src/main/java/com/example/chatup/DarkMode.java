package com.example.chatup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.mikepenz.materialize.color.Material;

public class DarkMode extends AppCompatActivity {
    Switch darkmode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(AppCompatDelegate.getDefaultNightMode()==AppCompatDelegate.MODE_NIGHT_YES){
           // setTheme(R.style.lightTheme);
        }
        else{
            setTheme((R.style.AppTheme));
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dark_mode);
        darkmode=findViewById(R.id.DarkSwitch);
        if(AppCompatDelegate.getDefaultNightMode()==AppCompatDelegate.MODE_NIGHT_YES){
            darkmode.setChecked(true);
        }
        darkmode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    restartApp();
                }
                else{
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    restartApp();
                }
            }
        });


    }
    public void restartApp(){
        startActivity(new Intent(DarkMode.this,UsersAndChatsActivity.class));
    }
}
