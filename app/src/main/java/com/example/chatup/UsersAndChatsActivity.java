package com.example.chatup;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.ismaeldivita.chipnavigation.ChipNavigationBar;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.adapters.ItemAdapter;

import java.util.List;

import static com.example.chatup.UserDetails.username;

public class UsersAndChatsActivity extends AppCompatActivity {


    private String TAG = this.getClass().getSimpleName();

    private RecyclerView recyclerView;
    private List<ChatModel> chatList;
    private FastAdapter<ChatModel> fastAdapter;
    private ItemAdapter<ChatModel> itemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_and_chats);
//        getSupportActionBar().hide();

        ChipNavigationBar navigationBar = findViewById(R.id.bottom_menu);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("ChatUp");
        toolbar.setTitleTextColor(getResources().getColor(R.color.blue));

        toolbar.setBackgroundColor(getResources().getColor(R.color.white));
        toolbar.setElevation(0);
        setSupportActionBar(toolbar);

        //Read from sharedpreferences
        SharedPreferences prefs = getSharedPreferences(Constants.SHARED_PREFS_NAME, MODE_PRIVATE);
        String name = prefs.getString("saved_username", "");
        Log.d(TAG, "sharedpreference: " + name);
        Toast.makeText(this, "Sharedpreference: " + name, Toast.LENGTH_SHORT).show();
        UserDetails.setUsername(name);


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


    }
}
