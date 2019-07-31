package com.example.chatup;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class ContactList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);
        getSupportActionBar().hide();
    }
}
