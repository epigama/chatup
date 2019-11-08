package com.example.chatup;


import android.app.Application;

import androidx.appcompat.app.AppCompatDelegate;

public class MyApplication extends Application {

    public void onCreate() {
        super.onCreate();
        AppCompatDelegate.setDefaultNightMode(
                AppCompatDelegate.MODE_NIGHT_YES);

    }
}