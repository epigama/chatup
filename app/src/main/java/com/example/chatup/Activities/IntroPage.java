package com.example.chatup.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.chatup.R;


public class IntroPage extends AppCompatActivity {
    int currentdaynight;
    public static final int splash_screen_timer = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
      //  setTheme(R.style.dark_theme);
        super.onCreate(savedInstanceState);
        currentdaynight= AppCompatDelegate.getDefaultNightMode();
        setContentView(R.layout.activity_intro_page);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(IntroPage.this, UsersAndChatsActivity.class);
                startActivity(intent);
            }
        }, splash_screen_timer);
        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);


    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if(currentdaynight!=AppCompatDelegate.getDefaultNightMode()){
            recreate();
        }
    }
}
