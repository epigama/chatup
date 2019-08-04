package com.example.chatup;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;//it's on x should have worked na yes wait
import androidx.recyclerview.widget.RecyclerView;

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

        getSupportActionBar().setTitle("ChatUp");


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


                        Toast.makeText(UsersAndChatsActivity.this, "home selected", Toast.LENGTH_SHORT).show();
                        //Bluetooth
                        break;
                    case R.id.activity:
                      //  startActivity(new Intent(UsersAndChatsActivity.this, Users.class));
                        Toast.makeText(UsersAndChatsActivity.this, "Activity selected", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.favorites:
                        startActivity(new Intent(UsersAndChatsActivity.this,Contacts.class));
                        Toast.makeText(UsersAndChatsActivity.this, "Contacts selected", Toast.LENGTH_SHORT).show();
                      //  startActivity(new Intent(UsersAndChatsActivity.this, ContactList.class));
                        break;
                    case R.id.profile:
                        startActivity(new Intent(UsersAndChatsActivity.this,Chats.class));
                        Toast.makeText(UsersAndChatsActivity.this, "Profile selected", Toast.LENGTH_SHORT).show();
                        //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_1, new AllUsersFragment());
                    //    Intent intent = new Intent(UsersAndChatsActivity.this, Users.class);
                      //  startActivity(intent);
                        break;
                }
            }
        });


    }
}
