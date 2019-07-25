package com.example.chatup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.github.tamir7.contacts.Contact;
import com.github.tamir7.contacts.Email;
import com.github.tamir7.contacts.PhoneNumber;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.IAdapter;
import com.mikepenz.fastadapter.adapters.ItemAdapter;
import com.mikepenz.fastadapter.listeners.OnClickListener;

import java.util.List;

import static com.github.tamir7.contacts.Contact.*;
import static com.github.tamir7.contacts.Contacts.getQuery;

public class Contacts extends AppCompatActivity {

    private static String TAG = MainActivity.class.getSimpleName();
    private Context context = Contacts.this;

    RecyclerView recyclerView;
    List<ContactModel> cardList;
    FastAdapter<ContactModel> fastAdapter;
    ItemAdapter<ContactModel> itemAdapter;

    List<Contact> contacts;

    public static boolean isAppWentToBg = false;

    public static boolean isWindowFocused = false;

    public static boolean isMenuOpened = false;

    public static boolean isBackPressed = false;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        com.github.tamir7.contacts.Contacts.initialize(this);
        recyclerView = findViewById(R.id.recycler);

        itemAdapter = new ItemAdapter<>();
        fastAdapter = FastAdapter.with(itemAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(fastAdapter);

        contacts = com.github.tamir7.contacts.Contacts.getQuery().find();

        handleAddContacts();
       fastAdapter.withOnClickListener(new OnClickListener<ContactModel>() {
           @Override
           public boolean onClick( View v, IAdapter<ContactModel> adapter, ContactModel item, int position) {

               startActivity(new Intent(getApplicationContext(),Invite.class));
               return false;
           }
       });


    }


    @Override
    protected void onStart() {
        Log.d(TAG, "onStart isAppWentToBg " + isAppWentToBg);

        applicationWillEnterForeground();

        super.onStart();
    }

    private void applicationWillEnterForeground() {
        if (isAppWentToBg) {
            isAppWentToBg = false;
            Toast.makeText(getApplicationContext(), "App is in foreground",
                    Toast.LENGTH_SHORT).show();
        }
        contacts = com.github.tamir7.contacts.Contacts.getQuery().find();
        handleAddContacts();

    }

    @Override
    protected void onStop() {
        super.onStop();

        Log.d(TAG, "onStop ");
        applicationdidenterbackground();
    }

    public void applicationdidenterbackground() {
        if (!isWindowFocused) {
            isAppWentToBg = true;

        }

    }

    @Override
    public void onBackPressed() {

        if (this instanceof Contacts) {

        } else {
            isBackPressed = true;
        }

        Log.d(TAG,
                "onBackPressed " + isBackPressed + ""
                        + this.getLocalClassName());
        super.onBackPressed();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {

        isWindowFocused = hasFocus;

        if (isBackPressed && !hasFocus) {
            isBackPressed = false;
            isWindowFocused = true;
        }

        super.onWindowFocusChanged(hasFocus);
    }

    public void handleAddContacts() {
        for (Contact contact : contacts) {
            try {
                String name = contact.getDisplayName();
                String number = "";
                String email = "";
                String photoUri = contact.getPhotoUri();
                if (contact.getPhoneNumbers().size() != 0 || contact.getPhoneNumbers() != null) {
                    for (PhoneNumber phoneNumber : contact.getPhoneNumbers()) {
                        number += phoneNumber.getNumber() + "\n";
                    }
                    Log.d(TAG, "handleAddContacts: " + number);
                }

                if (contact.getEmails().size() != 0 || contact.getEmails() != null) {
                    for (Email e : contact.getEmails()) {
                        email += e.getAddress() + "\n";
                    }
                    Log.d(TAG, "handleAddContacts: " + email);

                }
                ContactModel x = new ContactModel(name, number, email, photoUri);
                itemAdapter.add(x);
                fastAdapter.notifyDataSetChanged();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}