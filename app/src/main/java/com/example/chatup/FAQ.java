package com.example.chatup;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class FAQ extends AppCompatActivity {
    TextView faq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);
//        getSupportActionBar().setTitle("FAQ");
        faq=findViewById(R.id.OpenSettings);
        faq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                opendialog();
            }

            private void opendialog() {
                DialogSettings dialog= new DialogSettings();
                dialog.show(getSupportFragmentManager(),"dialog");
            }
        });
    }
}
