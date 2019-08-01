package com.example.chatup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
        getSupportActionBar().hide();

        ChipNavigationBar navigationBar = findViewById(R.id.bottom_menu);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("ChatUp");
        toolbar.setTitleTextColor(getResources().getColor(R.color.blue));

        toolbar.setBackgroundColor(getResources().getColor(R.color.white));
        toolbar.setElevation(0);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("ChatUp");


        recyclerView = findViewById(R.id.recycler);
        itemAdapter = new ItemAdapter<>();
        fastAdapter = FastAdapter.with(itemAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(fastAdapter);

        ChatModel model = new ChatModel("Regina", "How do you do?", "12:30");
            itemAdapter.add(model);
        fastAdapter.notifyAdapterDataSetChanged();

        navigationBar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                switch (i) {
                    case R.id.home:
                        Toast.makeText(UsersAndChatsActivity.this, "home selected", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.activity:
                        startActivity(new Intent(UsersAndChatsActivity.this,Users.class));
                        Toast.makeText(UsersAndChatsActivity.this, "Activity selected", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.favorites:
                        Toast.makeText(UsersAndChatsActivity.this, "Contacts selected", Toast.LENGTH_SHORT).show();
                       startActivity(new Intent(UsersAndChatsActivity.this,ContactList.class));
                        break;
                    case R.id.profile:
                        Toast.makeText(UsersAndChatsActivity.this, "Profile selected", Toast.LENGTH_SHORT).show();
                        //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_1, new AllUsersFragment());
                        Intent intent = new Intent(UsersAndChatsActivity.this, Users.class);
                        startActivity(intent);
                        break;
                }
            }
        });


    }
}
