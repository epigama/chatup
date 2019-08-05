package com.example.chatup;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.tamir7.contacts.Contact;
import com.github.tamir7.contacts.Email;
import com.github.tamir7.contacts.PhoneNumber;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.IAdapter;
import com.mikepenz.fastadapter.adapters.ItemAdapter;
import com.mikepenz.fastadapter.listeners.OnClickListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static android.Manifest.permission.READ_CONTACTS;
import static android.Manifest.permission.WRITE_CONTACTS;

public class Contacts extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_CODE = 200;

    private static String TAG = MainActivity.class.getSimpleName();
    private Context context = Contacts.this;

    RecyclerView recyclerView;
    List<ContactModel> cardList;
    FastAdapter<ContactModel> fastAdapter;
    ItemAdapter<ContactModel> itemAdapter;
    Map<String, String> userAndPhoneMap;

    List<Contact> contacts;

    public static boolean isAppWentToBg = false;

    public static boolean isWindowFocused = false;

    public static boolean isBackPressed = false;

    ProgressDialog pd;

    ArrayList<String> phoneList;
    ArrayList<String> userList;
    FirebaseDatabase mDatabase;

    String url = "https://chaton-343f1.firebaseio.com/users.json";



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        if (!checkPermission())
            requestPermission();
        getSupportActionBar().hide();

        phoneList = new ArrayList<>();
        userList = new ArrayList<>();

        mDatabase = FirebaseDatabase.getInstance();

        pd = new ProgressDialog(Contacts.this);
        pd.setMessage("Loading...");
        pd.show();

        View view = getLayoutInflater().inflate(R.layout.activity_contact_model, null);
        Button invite = view.findViewById(R.id.button_invite);
        invite.setVisibility(View.GONE);


        com.github.tamir7.contacts.Contacts.initialize(this);
        recyclerView = findViewById(R.id.recycler);

        itemAdapter = new ItemAdapter<>();
        fastAdapter = FastAdapter.with(itemAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(fastAdapter);

        fastAdapter.withOnClickListener(new OnClickListener<ContactModel>() {
            @Override
            public boolean onClick(@Nullable View v, IAdapter<ContactModel> adapter, ContactModel item, int position) {
                String phoneNum = item.getNumber();
                Log.d(TAG, "onClick: " + " clicked");

                Toast.makeText(Contacts.this, "Clicked position " + position, Toast.LENGTH_SHORT).show();

                int index = -1;
                String username = "";
                String phone = "";
                for(int i = 0; i < phoneList.size(); i ++){
                    phone = phoneList.get(i);
                    if(phone.equals(phoneNum))
                        index = i;
                    username = phoneList.get(i);
                    Log.d(TAG, "onClick: " + "username = " + username);
                    Log.d(TAG, "onClick: " + "phone = " + phone); //we need to debug that first phir kuch hoga
                }
                if (username != "") {
                    //open up chat window
                    Toast.makeText(Contacts.this, username, Toast.LENGTH_SHORT).show();
                    UserDetails.setChatWith(username);
                    Intent intent = new Intent(Contacts.this,Chats.class);
                    startActivity(intent);
                }
                else{
                    UserDetails.setChatWith("");
                    Toast.makeText(Contacts.this, "Blank username", Toast.LENGTH_SHORT).show();
                }

                return false;
            }
        });
        contacts = com.github.tamir7.contacts.Contacts.getQuery().find();

        handleAddContacts();

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>(){
            @Override
            public void onResponse(String s) {
                doOnSuccess(s, null);
            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                System.out.println("" + volleyError);
            }
        });

        RequestQueue rQueue = Volley.newRequestQueue(Contacts.this);
        rQueue.add(request);


    }
    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), READ_CONTACTS);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_CONTACTS);

        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
    }

    //request for permission
    private void requestPermission() {

        ActivityCompat.requestPermissions(this, new String[]{READ_CONTACTS, WRITE_CONTACTS}, PERMISSION_REQUEST_CODE);

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
    @Override
    protected void onResume() {
        super.onResume();
        if (!checkPermission()) {
            Log.i(TAG, "Permission not granted ");
            requestPermission();
        }
        else
            Log.i(TAG, "permission granted ");


    }

    public void doOnSuccess(String s, @Nullable String phoneNum){
        try {
            JSONObject obj = new JSONObject(s);
            Iterator i = obj.keys();
            String key = "";
             String phone = "";
            while(i.hasNext()){
                key = i.next().toString();
                if(!key.equals(com.example.chatup.UserDetails.username)) {
                    String tempKey = key;
                    //key is our username node
                    //so now we use mDatabase to access our node
                    DatabaseReference userReference;
                    userReference = mDatabase.getReference(String.format("users/%s",key));
                    userReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                           String phoneNumDb = map.get("phone").toString();


                           phoneList.add(phoneNumDb);
                           userList.add(tempKey);

                           try{
                               if(phoneNum.equals(phoneNumDb)){

                                   Toast.makeText(Contacts.this, "Eurekaa", Toast.LENGTH_SHORT).show();
                               }
                           }
                           catch (NullPointerException e){
                               e.printStackTrace();
                           }
                            Log.d(TAG, "onDataChange: " + phoneNum);
                            Toast.makeText(getApplicationContext(), phoneNum, Toast.LENGTH_SHORT).show();
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) { }
                    });
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        pd.dismiss();
    }

}