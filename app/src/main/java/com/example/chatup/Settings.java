package com.example.chatup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class Settings extends Fragment {
    TextView edit_profile;
    TextView userName;
    TextView bio;
    TextView invite;
    TextView notifications;
    TextView faq;
    TextView feedback;
   // TextView darkmode;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_settings, container, false);



        invite=view.findViewById(R.id.Settings_Invite);
        notifications=view.findViewById(R.id.Settings_Notifications);
        faq=view.findViewById(R.id.Settings_Faq);
        feedback=view.findViewById(R.id.Settings_Feedback);
       // darkmode=view.findViewById(R.id.Settings_DarkMode);


        userName = view.findViewById(R.id.Settings_Name);
        bio = view.findViewById(R.id.Settings_Bio);

        edit_profile=view.findViewById(R.id.EditProfile);
        edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(),ProfilePage.class));
            }
        });

        userName.setText(UserDetails.getUsername());
        bio.setText(UserDetails.getBio());
        faq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(),FAQ.class));
            }
        });

        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(),Feedback.class) );
            }
        });




    return  view;
    }
}
