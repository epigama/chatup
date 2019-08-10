package com.example.chatup;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.chatup.Dialog.DialogChangeProfile;
import com.example.chatup.Dialog.DialogExtra;
import com.example.chatup.Dialog.DialogFingerPrint;
import com.example.chatup.Dialog.DialogMessagesOutside;
import com.example.chatup.Dialog.DialogSettings;
import com.example.chatup.Dialog.DialogWriteFeedback;

public class FAQ extends AppCompatActivity {
    TextView faq_open_settings;
    TextView faq_change_profile;
    TextView faq_enable_fingerprint;
    TextView faq_write_feedback;
    TextView faq_extra;
    TextView faq_send_messages_outside;
    TextView faq_enable_darkmode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);

        faq_open_settings=findViewById(R.id.OpenSettings);
        faq_change_profile=findViewById(R.id.ChangeProfile);
        faq_enable_fingerprint=findViewById(R.id.EnableFingerPrint);
        faq_write_feedback=findViewById(R.id.WriteFeedback);
        faq_extra=findViewById(R.id.ExtraThing);
        faq_send_messages_outside=findViewById(R.id.SendMessage);
        faq_enable_darkmode=findViewById(R.id.DarkMode);


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

        faq_extra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                opendialog();
            }

            private void opendialog() {
                DialogExtra dialog= new DialogExtra();
                dialog.show(getSupportFragmentManager(),"dialog");
            }
        });

        faq_enable_fingerprint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                opendialog();
            }

            private void opendialog() {
                DialogFingerPrint dialog= new DialogFingerPrint();
                dialog.show(getSupportFragmentManager(),"dialog");
            }
        });

        faq_send_messages_outside.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                opendialog();
            }

            private void opendialog() {
                DialogMessagesOutside dialog= new DialogMessagesOutside();
                dialog.show(getSupportFragmentManager(),"dialog");
            }
        });

        faq_enable_darkmode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                opendialog();
            }

            private void opendialog() {
                DialogDarkmode dialog= new DialogDarkmode();
                dialog.show(getSupportFragmentManager(),"dialog");
            }
        });
    }
}
