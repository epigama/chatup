package com.example.chatup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

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
        faq=findViewById(R.id.textView);
        faq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                opendialog();
            }

            private void opendialog() {
                Dialog dialog= new Dialog();
                dialog.show(getSupportFragmentManager(),"dialog");
            }
        });
    }
}
