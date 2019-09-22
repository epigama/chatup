package com.example.chatup.Activities;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
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
    int currentdaynight; //initialise this


    private String TAG = this.getClass().getSimpleName();

    private RecyclerView recyclerView;
    private List<ChatModel> chatList;
    private FastAdapter<ChatModel> fastAdapter;
    private ItemAdapter<ChatModel> itemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            setTheme(R.style.dark_theme);
        } else {
            setTheme(R.style.AppTheme);
        }
        super.onCreate(savedInstanceState);


        currentdaynight = AppCompatDelegate.getDefaultNightMode();//call here
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

        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            setTheme(R.style.dark_theme);
        } else {
            setTheme(R.style.AppTheme);
        }
        ChipNavigationBar navigationBar = findViewById(R.id.bottom_menu);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("ChatUp");
        toolbar.setTitleTextColor(getResources().getColor(R.color.blue));

        toolbar.setBackgroundColor(getResources().getColor(R.color.white));
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
                        Fragment users_fragment = new Contacts();
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
    protected void onRestart() {
        super.onRestart();//check if not upgrade
        if(currentdaynight!=AppCompatDelegate.getDefaultNightMode()){
            recreate();
        }
    }
}