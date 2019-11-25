package com.example.chatup;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.example.chatup.Activities.Chats;
import com.example.chatup.Constants.Constants;
import com.example.chatup.Fragments.Users;
import com.example.chatup.Models.Message;
import com.example.chatup.Models.User;
import com.example.chatup.Models.UserDetails;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
import com.stfalcon.chatkit.dialogs.DialogsList;
import com.stfalcon.chatkit.dialogs.DialogsListAdapter;
import com.stfalcon.chatkit.messages.MessagesListAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;


public class RecentChats extends Fragment {
    DialogsList dialogsList;
    protected DialogsListAdapter<Dialogs> dialogsAdapter;
    protected com.stfalcon.chatkit.commons.ImageLoader imageLoader;
    Chats chats;
    DatabaseReference reference1;
    FirebaseDatabase mDatabase;
    MessagesListAdapter<Message> adapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recent_chats, container, false);
        dialogsList=view.findViewById(R.id.DialogLists);
        chats = new Chats();
        SharedPreferences prefs = getContext().getSharedPreferences(Constants.SHARED_PREFS_NAME, MODE_PRIVATE);
        String phone =  prefs.getString("phone_number", "");

        mDatabase = FirebaseDatabase.getInstance();
        reference1 = mDatabase.getReference(String.format("messages/%s_%s", phone, UserDetails.getChatWith()));

        reference1.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                String message = map.get("message").toString();
                String userName = map.get("user").toString();
                if(!message.equals("")){
                    //  Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                    map.put("message", message);
                    map.put("user", UserDetails.username);

//                                    reference1.push().setValue(mapX);
//                                    reference2.push().setValue(mapX);
//                                    messageArea.setText("");
                }


                if(userName.equals(UserDetails.username)){
                    ArrayList<User> usersList = new ArrayList<>();
                    User user = new User(UserDetails.getPhoneNum(), UserDetails.getUsername(), null, true);
                    usersList.add(user);
                    Dialogs dialog = new Dialogs(""+System.currentTimeMillis(), userName, null, usersLis);
                    adapter.addToStart(new Message(""+System.currentTimeMillis(), new User(UserDetails.getPhoneNum(), UserDetails.getUsername(), null, true), message), true);
                }
                else{
                    adapter.addToStart(new Message(""+System.currentTimeMillis(), new User(""+System.currentTimeMillis() + 1, UserDetails.getUsername(), null, true), message), true);
                    //createNotification(Chats.this, userName, message);
                } }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) { }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) { }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) { }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
        imageLoader = new com.stfalcon.chatkit.commons.ImageLoader() {
            @Override
            public void loadImage(ImageView imageView, String url, Object payload) {
                Picasso.get().load(url).into(imageView);
            }
        };

        ArrayList<User> usersList = new ArrayList<>();
        User user1 = new User("id_1", "Bhavna", null, true);
        User user2 = new User("id_2", "Jashaswee", null, true);
        usersList.add(user1);
        usersList.add(user2);

        Dialogs dialog = new Dialogs(phone, UserDetails.getChatWith(), null, usersList, new Message("id_message", user1," "), 2);
        DialogsListAdapter dialogsListAdapter = new DialogsListAdapter<Dialogs>(imageLoader);
        try {
            dialogsList.setAdapter(dialogsListAdapter);
            dialogsListAdapter.addItem(dialog);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return view;
    }



}
