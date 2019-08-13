package com.example.chatup;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.NetworkOnMainThreadException;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class ProfilePage extends AppCompatActivity {
    //DECLARING THE VARIABLES
    static int PReqCode = 1;
    static int REQUESCODE = 1;
    String name="ChatUp";
    //FIREBASE AUTHENTICATION VARIABLES
    public FirebaseAuth mAuth;
    FirebaseStorage storage;
    StorageReference storageReference;
    //TAG
    String TAG = "";
    // ImageView imageView;
    EditText user_name, user_bio;
    String userName, userBio;
    Button save_details;
    ImageView user_image_view;
    Uri pickedImgUri;
    String uid;
    //Firebase Realtime Db vars
    DatabaseReference parentReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        //Hidden status bar


        URL url = null;
        try {
            url = new URL("https://avatars2.githubusercontent.com/u/37215508?v=4");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Bitmap bmp = null;
        try {
            bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NetworkOnMainThreadException n) {
            n.printStackTrace();
        }
        try {
            user_image_view.setImageBitmap(bmp);
        } catch (NullPointerException ne) {
            ne.printStackTrace();

        }
        try {
            uid = getIntent().getStringExtra("uid");
        } catch (Exception e) {
            Log.d(TAG, " uid get intent" + e);
        }
        //  Log.d(TAG, "uid "  +uid) ;

        parentReference = FirebaseDatabase.getInstance().getReference("users");

        //INITIALISING THE VARIABLES TO THE XML IDs
        mAuth = FirebaseAuth.getInstance();
        user_image_view = findViewById(R.id.UserImage);
        user_name = findViewById(R.id.UserName);
        user_bio = findViewById(R.id.UserBio);
        save_details = findViewById(R.id.SaveDetails);

        save_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                save_details.setVisibility(View.INVISIBLE);
                //    progressBar.setVisibility(View.INVISIBLE);
                userName = user_name.getText().toString();
                userBio = user_bio.getText().toString();
                UserDetails.setUsername(userName);
                UserDetails.setBio(userBio);

                //Write to shared preferences
                SharedPreferences.Editor editor = getSharedPreferences(Constants.SHARED_PREFS_NAME, MODE_PRIVATE).edit();
                editor.putString("saved_username", userName);
                editor.apply();

                if (userName.isEmpty() || userBio.isEmpty()) {
                    ShowMessage("Please check your details");
                    save_details.setVisibility(View.VISIBLE);

                } else {
                    CreateUserAccount(userName, userBio);
                    startActivity(new Intent(ProfilePage.this, UsersAndChatsActivity.class));
                }

            }
        });


        user_image_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
                Log.d(TAG, "onClick: Uploading Image.");
                        //get the signed in user
                        if(name.equals("")) {
                            StorageReference storageRefer = storageReference.child("images/users/" +  "/" + name + ".jpg");
                            storageRefer.putFile(pickedImgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    // Get a URL to the uploaded content
                                    Uri downloadUrl = taskSnapshot.getUploadSessionUri();
                                    Toast.makeText(getApplicationContext(),"Success",Toast.LENGTH_SHORT).show();


                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    e.printStackTrace();
                                    Toast.makeText(getApplicationContext(),"Failure",Toast.LENGTH_SHORT).show();

                                }
                            })
                            ;
                        }

                    }
                });

            }


    private void checkAndRequestForPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {

                Toast.makeText(this, "Please accept for required permission", Toast.LENGTH_SHORT).show();

            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        PReqCode);
            }

        } else
            openGallery();

    }

    private void openGallery() {
        //TODO: open gallery intent and wait for user to pick an image !

        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, REQUESCODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUESCODE && data != null) {
            Log.d(TAG, " Code check");
            pickedImgUri = data.getData();
            user_image_view.setImageURI(pickedImgUri);
            StorageReference storageReferen = storageReference.child("images/users/" +   ".jpg");
            storageReferen.putFile(pickedImgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // Get a URL to the uploaded content
                    Uri downloadUrl = taskSnapshot.getUploadSessionUri();
                    Log.d(TAG, " DOWNLOAD URL " +downloadUrl);
                    Toast.makeText(getApplicationContext(),"Success",Toast.LENGTH_SHORT).show();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),"Failure",Toast.LENGTH_SHORT).show();

                }
            })
            ;
        }

    }






    public void ShowMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void CreateUserAccount(final String name, String bio) {
        try {
            parentReference.child(userName).child("name").setValue(userName);
            parentReference.child(userName).child("bio").setValue(userBio);
            parentReference.child(userName).child("phone").setValue(UserDetails.getPhoneNum());
        } catch (NullPointerException e) {
            Log.d(TAG, " null pointer" + e);
        }


    }


}

