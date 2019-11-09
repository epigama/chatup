package com.example.chatup.Activities;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.widget.Toast;

import androidx.annotation.AttrRes;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatup.Constants.Constants;
import com.example.chatup.Fragments.Contacts;
import com.example.chatup.Fragments.Settings;
import com.example.chatup.Fragments.Users;
import com.example.chatup.Models.ChatModel;
import com.example.chatup.Models.UserDetails;
import com.example.chatup.R;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.adapters.ItemAdapter;

import java.util.List;

public class UsersAndChatsActivity extends AppCompatActivity {
    private String TAG = this.getClass().getSimpleName();

    private RecyclerView recyclerView;
    private List<ChatModel> chatList;
    private FastAdapter<ChatModel> fastAdapter;
    private ItemAdapter<ChatModel> itemAdapter;
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {

                // Permission is not granted, so request it right away!
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_CONTACTS},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);
        }

        setContentView(R.layout.activity_users_and_chats);
        Fragment users_fragment = new Users();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_chats,
                users_fragment).commit();

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();

                        // Log and toast
                        String msg = getString(R.string.msg_token_fmt, token); //see this
                        Log.d(TAG, msg);
                        Toast.makeText(UsersAndChatsActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });
        FirebaseMessaging.getInstance().subscribeToTopic("general")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = getString(R.string.msg_subscribed);
                        if (!task.isSuccessful()) {
                            msg = getString(R.string.msg_subscribe_failed);
                        }
                        Log.d(TAG, msg);
                        Toast.makeText(UsersAndChatsActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("channelOne", "channelOne", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }


//        // Get the primary text color of the theme
//        TypedValue typedValue = new TypedValue();
//        Resources.Theme theme = this.getTheme();
//        theme.resolveAttribute(android.R.attr.textColorPrimary, typedValue, true);
//        TypedArray arr =
//                this.obtainStyledAttributes(typedValue.data, new int[]{
//                        android.R.attr.textColorPrimary, android.R.attr.colorBackground});
//        int textColor = arr.getColor(0, -1);
//        int backgroundColor = arr.getColor(1, -1);
        int textColorPrimaryColor =
                themeAttributeToColor(
                        android.R.attr.textColorPrimary,
                        this);
        int backgroundColorPrimaryColor = themeAttributeToColor(android.R.attr.colorBackground, this);
        ChipNavigationBar navigationBar = findViewById(R.id.bottom_menu);
        navigationBar.setBackgroundColor(backgroundColorPrimaryColor);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("ChatUp");
        toolbar.setTitleTextColor(textColorPrimaryColor);

//        toolbar.setBackgroundColor(backgroundColorPrimaryColor);
        toolbar.setElevation(0);
//        setSupportActionBar(toolbar);

        //Read from sharedpreferences
        SharedPreferences prefs = getSharedPreferences(Constants.SHARED_PREFS_NAME, MODE_PRIVATE);
        String name = prefs.getString("saved_username", "");
        String bio = prefs.getString("saved_bio", "");
        UserDetails.setUsername(name);
        UserDetails.setBio(bio);


//        recyclerView = findViewById(R.id.recycler);
        //   itemAdapter = new ItemAdapter<>();
        // fastAdapter = FastAdapter.with(itemAdapter);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.setAdapter(fastAdapter);

        //    ChatModel model = new ChatModel("Regina", "How do you do?", "12:30");
        //  ChatModel model1 = new ChatModel("Senorita", "How's the weather like?", "12:32");
        //itemAdapter.add(model);
        //itemAdapter.add(model1);
        //L̥fastAdapter.notifyAdapterDataSetChanged();

        navigationBar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                switch (i) {
                    case R.id.home:
                        Fragment users_fragment = new Users();
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_chats,
                                users_fragment).commit();
                        //Bluetooth
                        break;
                    case R.id.activity:
                        //  startActivity(new Intent(UsersAndChatsActivity.this, Users.class));
                        break;
                    case R.id.favorites:
                        Fragment contacts_fragment = new Contacts();
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_chats,
                                contacts_fragment).commit();


                        //  startActivity(new Intent(UsersAndChatsActivity.this, ContactList.class));
                        break;
                    case R.id.profile:
                        Fragment settings_fragment = new Settings();
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_chats,
                                settings_fragment).commit();

                        //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_1, new AllUsersFragment());
                        //    Intent intent = new Intent(UsersAndChatsActivity.this, Users.class);
                        //  startActivity(intent);
                        break;
                }
            }
        });
        try{
        TapTargetView.showFor(this,                 // `this` is an Activity
                TapTarget.forView(findViewById(R.id.profile), "Settings page", "Open Settings page to edit profile and for other facilities")
                        .tintTarget(false)
                        .titleTextColor(R.color.white)
                        .outerCircleColor(R.color.olive_green)
                        .dimColor(R.color.light_olive_green)
        );

        TapTargetView.showFor(this,                 // `this` is an Activity
                TapTarget.forView(findViewById(R.id.activity), "Activity Page", "This page will control your activities")
                        .tintTarget(false)
                        .titleTextColor(R.color.white)
                        .outerCircleColor(R.color.purple)
                        .dimColor(R.color.light_purple)
        );


        TapTargetView.showFor(this,                 // `this` is an Activity
                TapTarget.forView(findViewById(R.id.favorites), "Contacts Page", "View your contacts here")
                        .tintTarget(false)
                        .titleTextColor(R.color.white)
                        .outerCircleColor(R.color.orange)
                        .dimColor(R.color.light_orange)
        );
    }
catch (Exception e){
        e.printStackTrace();
    }
    }




    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay!
                    Toast.makeText(this, "Contacts permission granted Yayyy!", Toast.LENGTH_SHORT).show();

                } else {
                    // permission denied, boo!
                    //TODO: Hide the contacts button itself, maybe.
                    //Ask the permission recursively once again, for now!
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.READ_CONTACTS},
                            MY_PERMISSIONS_REQUEST_READ_CONTACTS);

                }
                return;
            }
        }
    }

    public static int themeAttributeToColor(int themeAttributeId,
                                            Context context) {
        TypedValue outValue = new TypedValue();
        Resources.Theme theme = context.getTheme();
        boolean wasResolved =
                theme.resolveAttribute(
                        themeAttributeId, outValue, true);
        if (wasResolved) {
            return ContextCompat.getColor(
                    context, outValue.resourceId);
        } else {
            // fallback colour handling
            return 0;
        }
    }
}
