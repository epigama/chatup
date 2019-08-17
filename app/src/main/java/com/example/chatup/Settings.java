package com.example.chatup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.File;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;
import static androidx.constraintlayout.motion.widget.MotionScene.TAG;

public class Settings extends Fragment {
    ImageView profile_pic;
    TextView edit_profile;
    TextView userName;
    TextView bio;
    TextView invite;
    TextView notifications;
    TextView faq;
    TextView feedback;
    TextView darkmode;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_settings, container, false);
        if (UserDetails.getDarkSwitch() == 1) {
            ContextThemeWrapper contextThemeWrapper = new ContextThemeWrapper(getActivity(), R.style.dark_theme);
            inflater = getActivity().getLayoutInflater().cloneInContext(contextThemeWrapper);
        } else {
            ContextThemeWrapper contextThemeWrapper = new ContextThemeWrapper(getActivity(), R.style.AppTheme);
            inflater = getActivity().getLayoutInflater().cloneInContext(contextThemeWrapper);
        }


        invite = view.findViewById(R.id.Settings_Invite);
        notifications = view.findViewById(R.id.Settings_Notifications);
        faq = view.findViewById(R.id.Settings_Faq);
        feedback = view.findViewById(R.id.Settings_Feedback);
        darkmode = view.findViewById(R.id.Settings_DarkMode);
        profile_pic=view.findViewById(R.id.Settings_Profile_Picture);
        userName = view.findViewById(R.id.Settings_Name);
        bio = view.findViewById(R.id.Settings_Bio);
        edit_profile = view.findViewById(R.id.EditProfile);
        edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), ProfilePage.class));
            }
        });

        userName.setText(UserDetails.getUsername());
        bio.setText(UserDetails.getBio());
        faq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), FAQ.class));
            }
        });

        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), Feedback.class));
            }
        });

        darkmode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), DarkMode.class));
            }
        });

        //Extract uri from sharedpreferences, if not blank set it to user_image_view
        SharedPreferences prefs = getContext().getSharedPreferences(Constants.SHARED_PREFS_NAME, MODE_PRIVATE);
        String uriString = prefs.getString(getString(R.string.local_img_uri), "");
        if(!(uriString.equals("") || uriString.equals(" "))){
            Uri uri = Uri.parse(UserDetails.getUri());
           Glide.with(this).load(new File(uri.getPath())).into(profile_pic);
           // profile_pic.setImageURI(uri);
           // Log.d(TAG, " GOT PHOTO " + uri.toString());
//            try {
//                final int takeFlags = (Intent.FLAG_GRANT_READ_URI_PERMISSION
//                        | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
//                getContext().getContentResolver().takePersistableUriPermission(uri, takeFlags);
//                // convert uri to bitmap
//                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), uri);
//                // set bitmap to imagevi
//                profile_pic.setImageBitmap(bitmap);
//            }
//            catch (Exception e){
//                e.printStackTrace();
//            }
        }


        return view;
    }


}
