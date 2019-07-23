package com.example.chatup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

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

        ChipNavigationBar navigationBar = findViewById(R.id.bottom_menu);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Chat");
        toolbar.setTitleTextColor(getResources().getColor(R.color.black));
        toolbar.setBackgroundColor(getResources().getColor(R.color.white));
        toolbar.setElevation(0);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Chat");

        recyclerView = findViewById(R.id.recycler);
        itemAdapter = new ItemAdapter<>();
        fastAdapter = FastAdapter.with(itemAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(fastAdapter);

        ChatModel model = new ChatModel("Regina", "How do you do?", "12:30");
        ChatModel model1 = new ChatModel("Senorita", "How's the weather like?", "12:32");
        itemAdapter.add(model);
        itemAdapter.add(model1);
        fastAdapter.notifyAdapterDataSetChanged();
    }
}
