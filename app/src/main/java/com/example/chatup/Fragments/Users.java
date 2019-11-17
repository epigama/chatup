package com.example.chatup.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.chatup.Activities.Chats;
import com.example.chatup.Models.User;
import com.example.chatup.R;
import com.example.chatup.Models.UserDetails;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Iterator;

public class Users extends Fragment {
    int currentdaynight;
    ListView usersList;
    TextView noUsersText;
    TextView noUsersText2;
    ArrayList<String> al = new ArrayList<>();
    ArrayList<String> usersAl = new ArrayList<>();
    int totalUsers = 0;
    ProgressDialog pd;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_users, container, false);
        usersList = (ListView) view.findViewById(R.id.usersList);
        noUsersText = (TextView) view.findViewById(R.id.noUsersText);
        noUsersText2 = view.findViewById(R.id.invite_folks);
        pd = new ProgressDialog(getContext());
        pd.setMessage("Loading...");
        pd.show();
        currentdaynight = AppCompatDelegate.getDefaultNightMode();
        String url = "https://chaton-343f1.firebaseio.com/users.json";
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                doOnSuccess(s);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                System.out.println("" + volleyError);
            }
        });

        RequestQueue rQueue = Volley.newRequestQueue(getContext());
        rQueue.add(request);

        usersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UserDetails.setChatWith(al.get(position));
                UserDetails.setChatWithNumber(al.get(position));
                startActivity(new Intent(getContext(), Chats.class));
            }
        });
        return view;
    }


    public void doOnSuccess(String s) {
        try {
            JSONObject obj = new JSONObject(s);

            Iterator i = obj.keys();
            String key = "";

            while (i.hasNext()) {
                key = i.next().toString();
                if (!key.equals(UserDetails.phoneNum)) {
                    al.add(key);
                    String contactName = getContactName(getContext(), key);
                    if(contactName != ""){
                        Log.d("", "onItemClick: " + "Contact exists");
                        usersAl.add(contactName);
                    }
                }
                totalUsers++;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {

            if (totalUsers <= 1) {
                noUsersText.setVisibility(View.VISIBLE);
                usersList.setVisibility(View.GONE);
            } else {
                noUsersText.setVisibility(View.GONE);
                noUsersText2.setVisibility(View.GONE);
                usersList.setVisibility(View.VISIBLE);
                usersList.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, usersAl));
            }

            pd.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getContactName(Context context, String number) {
        Uri lookupUri = Uri.withAppendedPath(
                ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
                Uri.encode(number));
        String[] mPhoneNumberProjection = { ContactsContract.PhoneLookup._ID, ContactsContract.PhoneLookup.NUMBER, ContactsContract.PhoneLookup.DISPLAY_NAME };
        Cursor cur = context.getContentResolver().query(lookupUri,mPhoneNumberProjection, null, null, null);
        try {
            if (cur.moveToFirst()) {
                String name = cur.getString(cur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                return name;
            }
        } finally {
            if (cur != null)
                cur.close();
        }
        return "";
    }
}






