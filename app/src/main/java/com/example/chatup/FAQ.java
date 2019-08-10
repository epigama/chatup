package com.example.chatup;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.chatup.Dialog.DialogChangeProfile;
import com.example.chatup.Dialog.DialogSettings;
import com.example.chatup.Dialog.DialogWriteFeedback;

public class FAQ extends AppCompatActivity {
    TextView faq_open_settings;
    TextView faq_change_profile;
    TextView faq_write_feedback;
    TextView write_feedback_1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);
//        getSupportActionBar().setTitle("FAQ");
        faq_open_settings=findViewById(R.id.OpenSettings);
        faq_change_profile=findViewById(R.id.ChangeProfile);
        faq_write_feedback=findViewById(R.id.WriteFeedback);
        faq_write_feedback=findViewById(R.id.WriteFeedback1);


        faq_open_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                opendialog();
            }

            private void opendialog() {
                DialogSettings dialog= new DialogSettings();
                dialog.show(getSupportFragmentManager(),"dialog");
            }
        });

        faq_change_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                opendialog();
            }

            private void opendialog() {
                DialogChangeProfile dialog= new DialogChangeProfile();
                dialog.show(getSupportFragmentManager(),"dialog");
            }
        });

        faq_write_feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                opendialog();
            }

            private void opendialog() {
                DialogWriteFeedback dialog= new DialogWriteFeedback();
                dialog.show(getSupportFragmentManager(),"dialog");
            }
        });
    }
}
