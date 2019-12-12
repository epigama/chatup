package com.example.chatup.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.example.chatup.Constants.Constants;
import com.example.chatup.R;
import com.example.chatup.Models.UserDetails;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.net.MalformedURLException;
import java.net.URL;

public class ProfilePage extends AppCompatActivity {
    int currentdaynight;
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
    String userName, userBio, phone;
    Button save_details;
    ImageView user_image_view;
    Uri pickedImgUri;
    String uid;
    //Firebase Realtime Db vars
    DatabaseReference parentReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (AppCompatDelegate.getDefaultNightMode()
                ==AppCompatDelegate.MODE_NIGHT_YES) {
            setTheme(R.style.dark_theme);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);
        checkAndRequestForPermission();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        //Hidden status bar
       currentdaynight=AppCompatDelegate.getDefaultNightMode();

        URL url = null;
        try {
            url = new URL("https://avatars2.githubusercontent.com/u/37215508?v=4");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Bitmap bmp = null;
        try {
            //bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            Glide.with(this).load(url).into(user_image_view);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            //user_image_view.setImageBitmap(bmp);
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

        user_name.setText(UserDetails.getUsername());
        user_bio.setText(UserDetails.getBio());

        //Extract uri from sharedpreferences, if not blank set it to user_image_view
        SharedPreferences prefs = getSharedPreferences(Constants.SHARED_PREFS_NAME, MODE_PRIVATE);
        String uriString = prefs.getString(getString(R.string.local_img_uri), "");
        if(!(uriString.equals("") || uriString.equals(" "))){
            Glide.with(this).load(uriString).into(user_image_view);

        }
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
                editor.putString("saved_bio", userBio);
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
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, REQUESCODE);
              //  openGallery();
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
                        PReqCode); }
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
            pickedImgUri= data.getData();
            CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1,1)
                    .start(this);
            //this shall crop

            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if(resultCode==REQUESCODE){
                   // Uri result_url=result.getUri();
                    StorageReference file_path=storageReference.child(uid + ". jpg");
                    file_path.putFile(pickedImgUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                           if(task.isSuccessful()){
                               Toast.makeText(ProfilePage.this,"Profile image upload success",Toast.LENGTH_SHORT).show();
                               final String download_url=task.getResult().getUploadSessionUri().toString();
                               parentReference.child("Users").child(uid).child("image")
                                       .setValue(download_url)
                                       .addOnCompleteListener(new OnCompleteListener<Void>() {
                                           @Override
                                           public void onComplete(@NonNull Task<Void> task) {
                                               if(task.isSuccessful()){
                                                   Toast.makeText(ProfilePage.this,"Imagesuccessfully oploaded to database",Toast.LENGTH_SHORT).show();
                                               }
                                               else{
                                                   String message=task.getException().toString();
                                               }

                                           }
                                       });
                           }
                           else{
                               String message=task.getException().toString();
                               Toast.makeText(ProfilePage.this,"Error  ",Toast.LENGTH_SHORT).show();
                           }
                           //this wil upload to firebase
                        }
                    });
                }

            }

            Log.d(TAG, " Code check");
           // pickedImgUri = data.getData();
            user_image_view.setImageURI(pickedImgUri);


/**
            StorageReference storageReferen = storageReference.child("/images/users/" +  UserDetails.getUsername() +  ".jpg");
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
                     try{
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        Toast.makeText(ProfilePage.this, "" + downloadUri, Toast.LENGTH_SHORT).show();
                        //Persist pickedImgUri so that it stays
//                        SharedPreferences.Editor editor = getSharedPreferences(Constants.SHARED_PREFS_NAME, MODE_PRIVATE).edit();
//                        editor.putString(getString(R.string.local_img_uri), downloadUri.toString());
//                        editor.apply();

                        Log.w(TAG, "DOWNLOAD " + downloadUri.toString());
                        parentReference.child(userName).child("uri").setValue(downloadUri.toString());
                        UserDetails.setUri(downloadUri.toString());
                    } else {
                        Log.w(TAG, "onComplete: " + "Incomplete upload");
                    }
                }
                     catch (Exception e){
                         e.printStackTrace();
                     }
            }
**/
            //});

        }

    }






    public void ShowMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void CreateUserAccount(final String name, String bio) {
        try {
            parentReference.child(UserDetails.getPhoneNum()).child("name").setValue(userName);
            parentReference.child(UserDetails.getPhoneNum()).child("bio").setValue(userBio);
            parentReference.child(UserDetails.getPhoneNum()).child("phone").setValue(UserDetails.getPhoneNum());
        } catch (NullPointerException e) {
            Log.d(TAG, " null pointer" + e);
        }


    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if(currentdaynight!=AppCompatDelegate.getDefaultNightMode()){
            recreate();
        }
    }
}

