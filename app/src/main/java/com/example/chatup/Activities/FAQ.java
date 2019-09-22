package com.example.chatup.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.chatup.R;

public class FAQ extends AppCompatActivity {
    TextView faq_open_settings;
    TextView faq_change_profile;
    TextView faq_enable_fingerprint;
    TextView faq_write_feedback;
    TextView faq_extra;
    TextView faq_send_messages_outside;
    TextView faq_enable_darkmode;
    TextView faq_post_questions;

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
        faq_post_questions=findViewById(R.id.PostQuestions);

        faq_open_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCustomDialog();
        }
            private void showCustomDialog() {
                ViewGroup viewGroup = findViewById(android.R.id.content);
                View dialogView = LayoutInflater.from(FAQ.this).inflate(R.layout.custom_alert_box, viewGroup, false);
                AlertDialog.Builder builder = new AlertDialog.Builder(FAQ.this);
                Button button = dialogView.findViewById(R.id.Ok);
                builder.setView(dialogView);
                AlertDialog alertDialog = builder.create();
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       alertDialog.dismiss();
                    }
                });
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                alertDialog.show();
            }
    });


        faq_change_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCustomDialog();
            }
            private void showCustomDialog() {
                ViewGroup viewGroup = findViewById(android.R.id.content);
                View dialogView = LayoutInflater.from(FAQ.this).inflate(R.layout.custom_alert_box, viewGroup, false);
               TextView heading_text=dialogView.findViewById(R.id.HeadingText);
               TextView desc_text=dialogView.findViewById(R.id.DescriptionText);
                AlertDialog.Builder builder = new AlertDialog.Builder(FAQ.this);
                Button button = dialogView.findViewById(R.id.Ok);
                builder.setView(dialogView);
                AlertDialog alertDialog = builder.create();
                heading_text.setText(R.string.faq_change_profile);
                desc_text.setText(R.string.faq_change_profile_desc);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                alertDialog.show();
            }
        });












        faq_write_feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCustomDialog();
            }
            private void showCustomDialog() {
                ViewGroup viewGroup = findViewById(android.R.id.content);
                View dialogView = LayoutInflater.from(FAQ.this).inflate(R.layout.custom_alert_box, viewGroup, false);
                TextView heading_text=dialogView.findViewById(R.id.HeadingText);
                TextView desc_text=dialogView.findViewById(R.id.DescriptionText);
                AlertDialog.Builder builder = new AlertDialog.Builder(FAQ.this);
                Button button = dialogView.findViewById(R.id.Ok);
                builder.setView(dialogView);
                AlertDialog alertDialog = builder.create();
                heading_text.setText(R.string.faq_write_feedback);
                desc_text.setText(R.string.faq_write_feedback_desc);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                alertDialog.show();
            }
        });



        faq_extra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCustomDialog();
            }
            private void showCustomDialog() {
                ViewGroup viewGroup = findViewById(android.R.id.content);
                View dialogView = LayoutInflater.from(FAQ.this).inflate(R.layout.custom_alert_box, viewGroup, false);
                TextView heading_text=dialogView.findViewById(R.id.HeadingText);
                TextView desc_text=dialogView.findViewById(R.id.DescriptionText);
                AlertDialog.Builder builder = new AlertDialog.Builder(FAQ.this);
                Button button = dialogView.findViewById(R.id.Ok);
                builder.setView(dialogView);
                AlertDialog alertDialog = builder.create();
                heading_text.setText(R.string.faq_extra);
                desc_text.setText(R.string.faq_extra_desc);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                alertDialog.show();
            }
        });



        faq_enable_fingerprint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCustomDialog();
            }
            private void showCustomDialog() {
                ViewGroup viewGroup = findViewById(android.R.id.content);
                View dialogView = LayoutInflater.from(FAQ.this).inflate(R.layout.custom_alert_box, viewGroup, false);
                TextView heading_text=dialogView.findViewById(R.id.HeadingText);
                TextView desc_text=dialogView.findViewById(R.id.DescriptionText);
                AlertDialog.Builder builder = new AlertDialog.Builder(FAQ.this);
                Button button = dialogView.findViewById(R.id.Ok);
                builder.setView(dialogView);
                AlertDialog alertDialog = builder.create();
                heading_text.setText(R.string.faq_enable_fingerprint);
                desc_text.setText(R.string.faq_enable_finger_print_desc);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                alertDialog.show();
            }
        });




        faq_send_messages_outside.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCustomDialog();
            }
            private void showCustomDialog() {
                ViewGroup viewGroup = findViewById(android.R.id.content);
                View dialogView = LayoutInflater.from(FAQ.this).inflate(R.layout.custom_alert_box, viewGroup, false);
                TextView heading_text=dialogView.findViewById(R.id.HeadingText);
                TextView desc_text=dialogView.findViewById(R.id.DescriptionText);
                AlertDialog.Builder builder = new AlertDialog.Builder(FAQ.this);
                Button button = dialogView.findViewById(R.id.Ok);
                builder.setView(dialogView);
                AlertDialog alertDialog = builder.create();
                heading_text.setText(R.string.faq_send_message);
                desc_text.setText(R.string.faq_send_message_desc);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                alertDialog.show();
            }
        });



        faq_enable_darkmode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCustomDialog();
            }
            private void showCustomDialog() {
                ViewGroup viewGroup = findViewById(android.R.id.content);
                View dialogView = LayoutInflater.from(FAQ.this).inflate(R.layout.custom_alert_box, viewGroup, false);
                TextView heading_text=dialogView.findViewById(R.id.HeadingText);
                TextView desc_text=dialogView.findViewById(R.id.DescriptionText);
                AlertDialog.Builder builder = new AlertDialog.Builder(FAQ.this);
                Button button = dialogView.findViewById(R.id.Ok);
                builder.setView(dialogView);
                AlertDialog alertDialog = builder.create();
                heading_text.setText(R.string.faq_enable_fingerprint);
                desc_text.setText(R.string.faq_enable_finger_print_desc);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                alertDialog.show();
            }
        });

        faq_post_questions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto","bhavnaharitsa@gmail.com", null));
                intent.putExtra(Intent.EXTRA_SUBJECT, "Questions");
                intent.putExtra(Intent.EXTRA_TEXT, "I would like to ask...   ");
                startActivity(Intent.createChooser(intent, "Choose an Email client :"));
            }
        });



    }
}
