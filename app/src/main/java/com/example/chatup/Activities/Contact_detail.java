package com.example.chatup.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.chatup.Activities.FAQ;
import com.example.chatup.R;

public class Contact_detail extends AppCompatActivity {
    TextView faq;
    TextView contact_us;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_detail);
        faq = findViewById(R.id.FAQ);
        contact_us=findViewById(R.id.ContactUS);
        faq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), FAQ.class));
            }
        });
        contact_us.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto","bhavnaharitsa@gmail.com", null));
                intent.putExtra(Intent.EXTRA_SUBJECT, "Please mention your name");
                intent.putExtra(Intent.EXTRA_TEXT, "Hello Hello");
                startActivity(Intent.createChooser(intent, "Choose an Email client :"));
            }
        });
    }
}
