package com.example.chatup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class profile extends AppCompatActivity {
    private FirebaseAuth firebase_auth;
    private EditText user_first_name, password, confirm_password, user_last_name;
     private  Button profile_button;
    private ProgressBar progressBar;
    Uri pickedImgUri ;
    public static int REQUESTCODE=01;
    public static int PReqCode = 1 ;
    ImageView user_photo;
    private static final int PERMISSION_REQUEST_CODE = 200;
    private AlertDialog PermissionAccessDialog;
    String TAG=""; //yahan se dekhlo kya?abhhey firebase ka profile code

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        firebase_auth=FirebaseAuth.getInstance();
        user_first_name=(EditText)findViewById(R.id.UserName);
        user_last_name=(EditText)findViewById(R.id.LastName);
        password=(EditText)findViewById(R.id.Password);
        confirm_password=(EditText)findViewById(R.id.ConfirmPassword);
        profile_button=(Button)findViewById(R.id.ProfileButton);
        user_photo=(ImageView)findViewById(R.id.UserPhoto);
        user_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Build.VERSION.SDK_INT>=22){
                    checkAndRequestForPermission();
                }
                else{
                    OpenGallery();
                }
            }
        });
        getSupportActionBar().hide();
      /**  if (!checkPermission())
            requestPermission();
       **/
      profile_button.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {

              profile_button.setVisibility(View.INVISIBLE);
              final String user_name=user_first_name.getText().toString();
              final String last_name=user_last_name.getText().toString();
              final String pass=password.getText().toString();
              final String confirm_pass=confirm_password.getText().toString();
              if(user_name.isEmpty() || last_name.isEmpty() ){
                  ShowErrorMessage("Please verify all the details");
                  profile_button.setVisibility(View.VISIBLE);
              }
              else{
                  CreateUserAccount(user_name,last_name,pass);
              }
          }
      });
    }

    public void CreateUserAccount(String email_id, final String last_name, String password){
       firebase_auth.createUserWithEmailAndPassword(email_id,password)
               .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                   @Override
                   public void onComplete(@NonNull Task<AuthResult> task) {
                      if(task.isSuccessful()){
                          Toast.makeText(getApplicationContext(),"Succesfully Created",Toast.LENGTH_SHORT).show();
                          ShowErrorMessage("Account Successfully Created");
                          UpdateUserInfo(last_name,pickedImgUri,firebase_auth.getCurrentUser());
                      }
                      else{
                          Toast.makeText(getApplicationContext(),"Error in creating account",Toast.LENGTH_SHORT).show();
                          profile_button.setVisibility(View.VISIBLE);
                          ShowErrorMessage("Account Creation Failed " +task.getException().getMessage());
                          Log.e(TAG, "onComplete: " + task.getException());

                       }
                   }
               });



    }
    public void UpdateUserInfo(final String name, final Uri user_image, final FirebaseUser fire_base_user){
        StorageReference storage_reference= FirebaseStorage.getInstance().getReference().child("user_photo");
        final StorageReference image_file_path=storage_reference.child(user_image.getLastPathSegment());
        image_file_path.putFile(user_image).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
               image_file_path.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                   @Override
                   public void onSuccess(Uri uri) {
                       UserProfileChangeRequest profile_update=new UserProfileChangeRequest.Builder()
                               .setDisplayName(name)
                               .setPhotoUri(user_image)
                               .build();
                       fire_base_user.updateProfile(profile_update)
                               .addOnCompleteListener(new OnCompleteListener<Void>() {
                                   @Override
                                   public void onComplete(@NonNull Task<Void> task) {
                                       if(task.isSuccessful()){
                                           ShowErrorMessage("Register Complete");
                                          // UpdateUI();
                                       }
                                   }
                               });
                   }
               });
            }
        });
    }

    public void UpdateUI(){
        startActivity(new Intent(this,intro_page.class));
        finish();
    }
    public void ShowErrorMessage(String verify_all_fields){
        Toast.makeText(this, "Please check again", Toast.LENGTH_SHORT).show();

    }
    private void OpenGallery() {
        //TODO: open gallery intent and wait for user to pick an image !

        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,REQUESTCODE);
    }


    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);

        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
    }

    //request for permission
    private void requestPermission() {

        ActivityCompat.requestPermissions(this, new String[]{READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);

    }

    //request for permissions
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {

                    boolean read_storage_Accepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean write_storage_Accepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if (read_storage_Accepted && write_storage_Accepted)
                        Log.i(TAG, "onRequestPermissionsResult: Permission granted");
                    else {
                        Log.i(TAG, "onRequestPermissionsResult: Permsission not granted");
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(READ_EXTERNAL_STORAGE)) {
                                showMessageOKCancel("You need to give permission for the app to work",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    requestPermissions(new String[]{READ_EXTERNAL_STORAGE,WRITE_EXTERNAL_STORAGE},
                                                            PERMISSION_REQUEST_CODE);
                                                }
                                            }
                                        });
                                return;
                            }
                        }

                    }
                }


                break;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == REQUESTCODE && data != null ) {

            // the user has successfully picked an image
            // we need to save its reference to a Uri variable
            pickedImgUri = data.getData() ;
            user_photo.setImageURI(pickedImgUri);


        }


    }


    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        PermissionAccessDialog=new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create();
    }
    private void checkAndRequestForPermission() {


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {

                Toast.makeText(this,"Please accept for required permission",Toast.LENGTH_SHORT).show();

            }

            else
            {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        PReqCode);
            }
        }
        else
            OpenGallery();

    }
}
