package com.example.chatup.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.chatup.Activities.Chats;
import com.example.chatup.Models.ContactModel;
import com.example.chatup.Models.UserDetails;
import com.example.chatup.R;
import com.github.tamir7.contacts.Contact;
import com.github.tamir7.contacts.Email;
import com.github.tamir7.contacts.PhoneNumber;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.IAdapter;
import com.mikepenz.fastadapter.adapters.ItemAdapter;
import com.mikepenz.fastadapter.listeners.OnClickListener;
import java.util.List;

public class Contacts extends Fragment {
    private static String TAG = "";
    RecyclerView recyclerView;
    FastAdapter<ContactModel> fastAdapter;
    ItemAdapter<ContactModel> itemAdapter;
    List<Contact> contacts;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_contacts, container, false);


        //onBackPressed();
        com.github.tamir7.contacts.Contacts.initialize(getContext());
        recyclerView = view.findViewById(R.id.recycler);

        itemAdapter = new ItemAdapter<>();
        fastAdapter = FastAdapter.with(itemAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(fastAdapter);

        fastAdapter.withOnClickListener(new OnClickListener<ContactModel>() {
            @Override
            public boolean onClick(@Nullable View v, IAdapter<ContactModel> adapter, ContactModel item, int position) {
                onInviteClicked();

                String phoneNum = item.getNumber();
                Log.d(TAG, "onClick: " + " clicked");
                Log.d(TAG, "onClick: " + phoneNum);


                String username = "";
                if (username != "") {
                    //open up chat window

                    UserDetails.setChatWith(username);
                    Intent intent = new Intent(getContext(), Chats.class);
                    startActivity(intent);
                } else {
                    UserDetails.setChatWith("");
                }

                return false;
            }
        });
        contacts = com.github.tamir7.contacts.Contacts.getQuery().find();

        handleAddContacts();

 return view;
    }
    public void handleAddContacts(){
        for(Contact contact : contacts){
            try {
                String name = contact.getDisplayName();
                String number = "";
                String email = "";
                String photoUri = contact.getPhotoUri();
                if(contact.getPhoneNumbers().size() != 0 || contact.getPhoneNumbers() != null){
                    for(PhoneNumber phoneNumber : contact.getPhoneNumbers()){
                        number += phoneNumber.getNumber() + "\n";
                    }
                    Log.d(TAG, "handleAddContacts: " + number);
                }

                if(contact.getEmails().size() != 0 || contact.getEmails() != null){
                    for(Email e : contact.getEmails()){
                        email += e.getAddress() + "\n";
                    }
                    Log.d(TAG, "handleAddContacts: " + email);

                }
                ContactModel x = new ContactModel(name, number, email, photoUri);
                itemAdapter.add(x);
                fastAdapter.notifyDataSetChanged();
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private void onInviteClicked() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "Hey, I found this terrific app. Do try it out!!");
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }


}