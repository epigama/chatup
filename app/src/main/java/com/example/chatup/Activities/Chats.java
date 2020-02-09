package com.example.chatup.Activities;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.SyncStateContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.chatup.Config;
import com.example.chatup.Constants.Constants;
import com.example.chatup.Models.Message;
import com.example.chatup.Models.User;
import com.example.chatup.Fragments.Settings;
import com.example.chatup.Models.UserDetails;
import com.example.chatup.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.stfalcon.chatkit.messages.MessageInput;
import com.stfalcon.chatkit.messages.MessagesList;
import com.stfalcon.chatkit.messages.MessagesListAdapter;
import com.stfalcon.chatkit.utils.DateFormatter;

import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Chats extends AppCompatActivity {
    Context context;
    Uri downloadUri;
    Uri pickedImgUri;
    DatabaseReference parentReference;
    StorageReference storageReference;
    ImageView image;
    String TAG="";
    StorageReference image_storage;
    public static final int RESCODE=1;
    DatabaseReference reference1, reference2;
    FirebaseDatabase mDatabase;
    private BroadcastReceiver mRegistrationBroadcastReciever;
    MessagesListAdapter<Message> adapter;
    MessagesList messagesList;
    MessageInput input;
    String messageText;
    //private TextView txtRegId ;
    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReciever);
        super.onPause();
    }
    @Override
    protected void onResume(){
        super.onResume();
        //Register gcm cloud messaging 'registration complete' recieve
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chats);
        // imageView = new ImageView(Chats.this);

        //Read phone number from sharedpreferences
        SharedPreferences prefs = getSharedPreferences(Constants.SHARED_PREFS_NAME, MODE_PRIVATE);
        String phone =  prefs.getString("phone_number", "");

        input = findViewById(R.id.input);
        messagesList = findViewById(R.id.messagesList);
        adapter = new MessagesListAdapter<>(UserDetails.phoneNum, null);
        messagesList.setAdapter(adapter);
        parentReference = FirebaseDatabase.getInstance().getReference("users");
        mRegistrationBroadcastReciever = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //checking for type intent filter
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    //gcm successfully registered
                    //Now subscrive to 'global' topic to recieve app wide notifications
                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);
                    displayFirebaseRegId();
                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    //New push notification is recieved
                    String message = intent.getStringExtra("message");
                    Toast.makeText(getApplicationContext(), " Push Notification: " + message, Toast.LENGTH_LONG);
                    Log.d(TAG, "onReceieve: " + message);
                }
            }
        };
        displayFirebaseRegId();
        mDatabase = FirebaseDatabase.getInstance();
        //getSupportActionBar().hide();
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(UserDetails.getChatWith());
        toolbar.setTitleTextColor(getResources().getColor(R.color.blue));
        toolbar.setBackgroundColor(getResources().getColor(R.color.white));
        toolbar.setElevation(0);
        setSupportActionBar(toolbar);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel("channelOne", "channelOne", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
        image_storage=FirebaseStorage.getInstance().getReference();
        reference1 = mDatabase.getReference(String.format("messages/%s_%s", phone, UserDetails.getChatWith()));
        reference2 = mDatabase.getReference(String.format("messages/%s_%s", UserDetails.getChatWith(), phone));
        try {
            input.setInputListener(new MessageInput.InputListener()  {
                @Override
                public boolean onSubmit(CharSequence input) {
                    messageText = input.toString();
                    if (!messageText.equals("")) {
                        Map<String, String> map = new HashMap<String, String>();
                        map.put("message", messageText);
//                map.put("image", downloadUri.toString());
                        map.put("user", UserDetails.username);
                        reference1.push().setValue(map);
                        reference2.push().setValue(map);
                    }
                    return false;
                }
            });
        }
        catch (Exception e){
            e.printStackTrace();
        }
        reference1.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                String message = map.get("message").toString();
                String userName = map.get("user").toString();
                                if(!message.equals("")){
                                    map.put("message", message);
                                    map.put("user", UserDetails.username);
                                }
                                if(userName.equals(UserDetails.username)){
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


//        add_content.setOnClickListener(new View.OnClickListener() {
//
//            public void onClick(View v) {
//                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//                startActivityForResult(cameraIntent,RESCODE);
//            }
//        });
    }



//    public void addMessageBox(String message, int type){
//        DateFormat df = new SimpleDateFormat("HH:mm");
//        Calendar calobj = Calendar.getInstance();
//        TextView textView = new TextView(Chats.this);
//        TextView  text= new TextView(Chats.this);
//        //ye bey
//        textView.setText(message);
//        //  text.setText();
//        //  text.setText(df.format(calobj.getTime()));
//        text.setGravity(Gravity.RIGHT);
//        //  imageView.setForegroundGravity(Gravity.RIGHT);
//        textView.setTextColor(getColor(R.color.white));
//        layout.addView(text);
//
//        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//
//        lp2.weight = 7.0f;
//
//        if(type == 1) {
//            lp2.gravity = Gravity.RIGHT;
//            textView.setBackgroundResource(R.drawable.bubble_in);
//            textView.setTextColor(getResources().getColor(R.color.white));
//        }
//        else{
//            lp2.gravity = Gravity.LEFT;
//            textView.setBackgroundResource(R.drawable.bubble_out);
//            // textView.setTextColor(R.color.black);
//        }
//        textView.setLayoutParams(lp2);
//        layout.addView(textView);
//        scrollView.post(new Runnable() {
//
//            @Override
//            public void run() {
//                scrollView.fullScroll(ScrollView.FOCUS_DOWN);
//            }
//        });    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.Settings:
                startActivity(new Intent(getApplicationContext(), Settings.class));
        }
        return true;
    }

    private void createNotification(Context context, String title, String text) {
        RemoteViews notificationLayout = new RemoteViews(getPackageName(), R.layout.custom_notification);
        View view = LayoutInflater.from(Chats.this).inflate(R.layout.custom_notification, null, false);
        TextView heading = view.findViewById(R.id.Heading);
        TextView body = view.findViewById(R.id.Description);
        heading.setText(title);
        body.setText(text);
        Notification customNotification = new NotificationCompat.Builder(context, "channelOne")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                .setCustomContentView(notificationLayout)
                .build();
        NotificationManager manager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        manager.notify((int)System.currentTimeMillis(),customNotification);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESCODE) {
            try {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                image.setImageBitmap(photo);
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) image.getLayoutParams();
                image.setLayoutParams(lp);
                Uri uri = getImageUri(context,photo);

                StorageReference storageReferen = storageReference.child("/images/message/" +  UserDetails.getPhoneNum() +  UserDetails.getChatWith() + ".jpg");
                UploadTask uploadTask = storageReferen.putFile(uri);
                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }

                        // Continue with the task to get the download URL
                        return storageReferen.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            downloadUri= task.getResult();
                            Toast.makeText(Chats.this, "Sent" + downloadUri, Toast.LENGTH_SHORT).show();
                            Log.w(TAG, "SENT IMAGE " +downloadUri.toString());
                            parentReference.child("uri").setValue(downloadUri.toString());
                            UserDetails.setUri(downloadUri.toString());
                        } else {
                            Log.w(TAG, "onComplete: " + "Incomplete upload");
                        }
                    }
                });

            }
            catch (Exception e){
                e.printStackTrace();
            }
        }


        StorageReference storageReferen = storageReference.child("users/" + UserDetails.getPhoneNum() + ".jpg");
        UploadTask uploadTask = storageReferen.putFile(pickedImgUri);
        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return storageReferen.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    downloadUri = task.getResult();
                    Toast.makeText(Chats.this, "" + downloadUri, Toast.LENGTH_SHORT).show();


                    Log.w(TAG, "DOWNLOAD CHATS IMAGES" + downloadUri.toString());
                    parentReference.child("uri").setValue(downloadUri.toString());
                    UserDetails.setUri(downloadUri.toString());
                } else {
                    Log.w(TAG, "onComplete: " + "Incomplete upload");
                }
            }
        });

    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }



    private void displayFirebaseRegId()
    {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        String regId = pref.getString("regId", null);

        Log.e(TAG, "Firebase Registration Id" + regId);
    }

    public static void sendNotificationToUser(String user, final String message) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("parentReference");
        reference.child("username").setValue(user);
        reference.child("message").setValue(message);
    }


    public String format(Date date) {
        if (DateFormatter.isToday(date)) {
            return DateFormatter.format(date, DateFormatter.Template.TIME);
        } else if (DateFormatter.isYesterday(date)) {
            return getString(R.string.date_header_yesterday);
        } else {
            return DateFormatter.format(date, DateFormatter.Template.STRING_DAY_MONTH_YEAR);
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}