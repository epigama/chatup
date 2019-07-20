package com.example.chatup;

import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import static android.Manifest.permission.READ_CONTACTS;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS;

public class Invite extends AppCompatActivity {
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 200;
    Button invite_folks;
    String TAG = "";
    private AlertDialog PermissionAccessDialog;
    private static final int PERMISSION_REQUEST_CODE = 200;
    private static final String ENABLED_NOTIFICATION_LISTENERS = "enable_contact_permission";
    private static final String ACTION_GET_CONTACT = "android.settings.ACTION_APPLICATION_DETAILS_SETTINGS";
    //Initial value of isEnablesNLS is  false so that every time user installs the app, he has to manually enable it.
    private boolean isEnabledNLS = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite);
        invite_folks = findViewById(R.id.invite_folks);
        invite_folks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
                    sendIntent.setType("text/plain");
                    startActivity(sendIntent);
                } catch (Exception e) {
                    //e.toString();
                }
            }
        });
        checkPermission();

    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), READ_CONTACTS);
       // int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_);

        return result == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    protected void onResume() {
        super.onResume();
        isEnabledNLS = isEnabled();
        Log.d(TAG, "isEnabledNLS = r" + isEnabledNLS);
        if (!isEnabledNLS) {
            showConfirmDialog();
        }
    }

    // METHOD TO ENABLE NOTIFICATION ACCESS EVERY TIME THE USER INSTALLS APP
    private boolean isEnabled() {
        String pkgName = getPackageName();
        final String flat = Settings.Secure.getString(getContentResolver(),
                ENABLED_NOTIFICATION_LISTENERS);
        if (!TextUtils.isEmpty(flat)) {
            final String[] names = flat.split(":");
            for (int i = 0; i < names.length; i++) {
                final ComponentName cn = ComponentName.unflattenFromString(names[i]);
                if (cn != null) {
                    if (TextUtils.equals(pkgName, cn.getPackageName())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }


    // DIALOG BOX TO ENABLE NOTIFICATION ACCESS FOR THE APP BY USER
    private void showConfirmDialog() {
        new AlertDialog.Builder(this)
                .setMessage("Please enable NotificationMonitor access")
                .setTitle("Notification Access")
                .setIconAttribute(android.R.attr.alertDialogIcon)
                .setCancelable(true)
                .setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                openNotificationAccess();
                            }
                        })
                .setNegativeButton(android.R.string.cancel,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // do nothing
                            }
                        })
                .create().show();
    }

    //OPENS NOTIFICATION ACCESS ON YOUR PHONE
    private void openNotificationAccess() {
        Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + getPackageName()));
        startActivity(intent);
        Toast.makeText(getApplicationContext(),"Enable contacts in Permissions",Toast.LENGTH_SHORT).show();
    }

}
