package com.example.chatup.Fragments;

import android.app.ProgressDialog;
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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.chatup.Activities.Chats;
import com.example.chatup.Models.ContactModel;
import com.example.chatup.Models.DatabaseModel;
import com.example.chatup.Models.UserDetails;
import com.example.chatup.R;
import com.github.tamir7.contacts.Contact;
import com.github.tamir7.contacts.Email;
import com.github.tamir7.contacts.PhoneNumber;
import com.github.tamir7.contacts.Query;
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
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.example.chatup.Models.UserDetails.phoneNum;

public class Contacts extends Fragment {
    private static final int PERMISSION_REQUEST_CODE = 200;
    private static String TAG = "";
    RecyclerView recyclerView;
    List<ContactModel> cardList;
    FastAdapter<ContactModel> fastAdapter;
    ItemAdapter<ContactModel> itemAdapter;
    Map<String, String> userAndPhoneMap;
    List<Contact> contacts;
    ProgressDialog pd;
    ArrayList<String> phoneList;
    ArrayList<String> userList;
    FirebaseDatabase mDatabase;
    String url = "https://chaton-343f1.firebaseio.com/users.json";


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_contacts, container, false);


        //onBackPressed();

        phoneList = new ArrayList<>();
        userList = new ArrayList<>();

        mDatabase = FirebaseDatabase.getInstance();

        pd = new ProgressDialog(getContext());
        pd.setMessage("Loading...");
        pd.show();



        com.github.tamir7.contacts.Contacts.initialize(getContext());
        recyclerView = view.findViewById(R.id.recycler);

        itemAdapter = new ItemAdapter<>();
        fastAdapter = FastAdapter.with(itemAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(fastAdapter);

        fastAdapter.withOnClickListener(new OnClickListener<ContactModel>() {
            @Override
            public boolean onClick(@Nullable View v, IAdapter<ContactModel> adapter, ContactModel item, int position) {
                String phoneNum = item.getNumber();
                Log.d(TAG, "onClick: " + " clicked");
                Log.d(TAG, "onClick: " + phoneNum);


                int index = -1;
                String username = "";
                String phone = "";
//                for(int i = 0; i < phoneList.size(); i ++){
//                    phone = phoneList.get(i);
//                    if(phone.equals(item.getNumber()))
//                        index = i;
//                    username = userList.get(i);
//                    Log.d(TAG, "onClick: " + "username = " + username);
//                    Log.d(TAG, "onClick: " + "phone = " + phone); //we need to debug that first phir kuch hoga
//                }
//                if (phoneList.contains(item.getNumber())) {
//                    int i = phoneList.indexOf(item.getNumber());
//                    Log.d(TAG, "onClick: " + "chatwith = " + userList.get(i));
//                }
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
    public void handleAddContacts() {
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject obj = new JSONObject(s);
                    Iterator i = obj.keys();
                    String key = "";
                    while (i.hasNext()) {
                        key = i.next().toString();
                        Log.d(TAG, "doOnSuccess: " + key);
                        if (!key.equals(UserDetails.username)) {
                            String tempKey = key;
                            //key is our phonenum node
                            //so now we use mDatabase to access our node
                            DatabaseReference userReference;
                            userReference = mDatabase.getReference(String.format("users/%s", key));
                            userReference.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    Log.d(TAG, "onDataChange: " + dataSnapshot.getKey());
                                    DatabaseModel newPost = dataSnapshot.getValue(DatabaseModel.class);
                                   try {
                                       String phoneNum = newPost.getPhone();
                                       Log.d(TAG, "onDataChange: " + phoneNum);
                                   }
                                   catch (Exception e){
                                       e.printStackTrace();
                                   }

                                    phoneList.add(phoneNum);
                                    userList.add(tempKey);

                                    Set<String> set = new HashSet<>(phoneList);
                                    phoneList.clear();
                                    phoneList.addAll(set);

                                    Query q = com.github.tamir7.contacts.Contacts.getQuery();
                                    q.whereEqualTo(Contact.Field.PhoneNumber, phoneNum);
                                    List<Contact> contacts = q.find();

                                    if(!contacts.isEmpty()) {
//
                                        Log.d(TAG, "onDataChange: " + phoneNum);

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
                                                }
                                                if (contact.getEmails().size() != 0 || contact.getEmails() != null) {
                                                    for (Email e : contact.getEmails()) {
                                                        email += e.getAddress() + "\n";
                                                    }
                                                }
                                                if (phoneList.contains(number)) {
                                                    Log.d(TAG, "handleAddContacts: " + number);
                                                }
                                                ContactModel x = null;
                                                Log.d(TAG, "handleAddContacts: " + phoneList.toString());
                                                for (String phone : phoneList) {
                                                    Log.d(TAG, "phoneNum: " + phone);
                                                    if (phone.equals(phoneNum)) {
                                                        x = new ContactModel(name, number, email, photoUri);
                                                        itemAdapter.add(x);
                                                        fastAdapter.notifyDataSetChanged();
                                                    }
                                                }

                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                }
                            });
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                pd.dismiss();            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                System.out.println("" + volleyError);
            }
        });

        RequestQueue rQueue = Volley.newRequestQueue(getContext());
        rQueue.add(request);

    }




}