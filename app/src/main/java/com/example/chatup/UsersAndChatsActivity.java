//package com.example.chatup;
//
//import android.os.Bundle;
//
//import com.example.chatup.Fragments.UserFragment;
//import com.google.android.material.bottomnavigation.BottomNavigationView;
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.annotation.NonNull;
//import androidx.fragment.app.Fragment;
//
//import android.view.MenuItem;
//import android.widget.TextView;
//
//public class UsersAndChatsActivity extends AppCompatActivity {
//    private TextView mTextMessage;
//    private Fragment selectedFragment;
//
//    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
//            = new BottomNavigationView.OnNavigationItemSelectedListener() {
//
//        @Override
//        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//            switch (item.getItemId()) {
//                case R.id.navigation_users:
//                    selectedFragment = new UserFragment();
//                    getSupportFragmentManager().beginTransaction().replace(R.layout.)
//                    mTextMessage.setText("Users");
//                    return true;
//                case R.id.navigation_chats:
//                    mTextMessage.setText("Chats");
//                    return true;
//            }
//            return false;
//        }
//    };
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_users_and_chats);
//        BottomNavigationView navView = findViewById(R.id.nav_view);
//        mTextMessage = findViewById(R.id.message);
//        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
//    }
//
//}
