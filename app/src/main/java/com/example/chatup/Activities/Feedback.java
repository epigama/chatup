package com.example.chatup.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.chatup.R;

public class Feedback extends AppCompatActivity {
    Button feedback_mail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        feedback_mail=findViewById(R.id.FeedbackMail);
        feedback_mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto","bhavnaharitsa@gmail.com", null));
                intent.putExtra(Intent.EXTRA_SUBJECT, "Feedback");
                intent.putExtra(Intent.EXTRA_TEXT, "My feedback is   ");
                startActivity(Intent.createChooser(intent, "Choose an Email client :"));
            }
        });
    }
}
