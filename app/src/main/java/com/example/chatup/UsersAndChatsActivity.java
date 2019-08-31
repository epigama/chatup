package com.example.chatup;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetView;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.adapters.ItemAdapter;

import java.util.List;

import static com.example.chatup.UserDetails.username;

public class UsersAndChatsActivity extends AppCompatActivity {
    int currentdaynight; //initialise this


    private String TAG = this.getClass().getSimpleName();

    private RecyclerView recyclerView;
    private List<ChatModel> chatList;
    private FastAdapter<ChatModel> fastAdapter;
    private ItemAdapter<ChatModel> itemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES){
            setTheme(R.style.dark_theme);
        }
        else{
            setTheme(R.style.AppTheme);
        }
        super.onCreate(savedInstanceState);


        Fragment users_fragment= new Users();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_chats,
                users_fragment).commit();
        currentdaynight=AppCompatDelegate.getDefaultNightMode();//call here
        setContentView(R.layout.activity_users_and_chats);

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

        navigationBar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                switch (i) {
                    case R.id.home:
                        Fragment users_fragment= new Users();
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_chats,
                                users_fragment).commit();
                        //Bluetooth
                        break;
                    case R.id.activity:
                      //  startActivity(new Intent(UsersAndChatsActivity.this, Users.class));
                        break;
                    case R.id.favorites:
                        Fragment contacts_fragment= new Contacts();
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_chats,
                                contacts_fragment).commit();



                      //  startActivity(new Intent(UsersAndChatsActivity.this, ContactList.class));
                        break;
                    case R.id.profile:
                        Fragment settings_fragment= new Settings() ;
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_chats,
                                settings_fragment).commit();

                        //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_1, new AllUsersFragment());
                    //    Intent intent = new Intent(UsersAndChatsActivity.this, Users.class);
                      //  startActivity(intent);
                        break;
                }
            }
        });

        TapTargetView.showFor(this,                 // `this` is an Activity
                TapTarget.forView(findViewById(R.id.profile),"Settings page","Open Settings page to edit profile and for other facilities")
        .tintTarget(false)
                        .titleTextColor(R.color.white)
                        .outerCircleColor(R.color.olive_green)
                        .dimColor(R.color.light_olive_green)
        );

        TapTargetView.showFor(this,                 // `this` is an Activity
                TapTarget.forView(findViewById(R.id.activity),"Activity Page","This page will control your activities")
                        .tintTarget(false)
                        .titleTextColor(R.color.white)
                        .outerCircleColor(R.color.purple)
                        .dimColor(R.color.light_purple)
        );

        TapTargetView.showFor(this,                 // `this` is an Activity
                TapTarget.forView(findViewById(R.id.favorites),"Contacts Page","View your contacts here")
                        .tintTarget(false)
                        .titleTextColor(R.color.white)
                        .outerCircleColor(R.color.orange)
                        .dimColor(R.color.light_orange)
        );




    }

    @Override
    protected void onRestart() {
        super.onRestart();//check if not upgrade
        if(currentdaynight!=AppCompatDelegate.getDefaultNightMode()){
            recreate();
        }
    }
}
