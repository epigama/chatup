package com.example.chatup.Notifications;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.chatup.Activities.MainActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();

    private NotificationUtils notificationUtils;


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e(TAG, "From: " + remoteMessage.getFrom());

        if (remoteMessage == null) {
            return;
        }

        //Check if remoteMessage contains a notification payload
        if(remoteMessage != null)
        {
            Log.e(TAG, "Notification Body: " + remoteMessage.getNotification().getBody());
            handleNotification(remoteMessage.getNotification().getBody());
        }

        //Check if remoteMessage contains a data payload
        if(remoteMessage.getData().size() > 0)
        {
            Log.e(TAG, "Data payload : " + remoteMessage.getData().toString());

            try{
                JSONObject json = new JSONObject(remoteMessage.getData().toString());
                handleDataMessage(json);
            }
            catch (Exception e)
            {
                Log.e(TAG, "Exception: " + e.getMessage());
            }
        }
    }

    private void handleNotification(String message)
    {
        if(!NotificationUtils.isAppInBackground(getApplicationContext()))
        {
            //App is in foreground. Broadcast the push message

            Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
            pushNotification.putExtra("message", message);
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

            //Play Notification Sound
            NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
            notificationUtils.playNotificationSound();
        }
        else
        {
            //if app is in background, firebase handles the notification itself
        }
    }

    private void handleDataMessage(JSONObject json)
    {
        Log.e(TAG, "push json: " + json.toString());

        try{
            JSONObject data = json.getJSONObject("data");

            String title = data.getString("title");
            String message = data.getString("message");
            boolean isBackground = data.getBoolean("is_background");
            String imageUrl = data.getString("image");
            String timeStamp = data.getString("timestamp");
            JSONObject payload = data.getJSONObject("payload");

            Log.e(TAG, "title: " + title);
            Log.e(TAG, "message: " + message);
            Log.e(TAG, "isBackground: " + isBackground);
            Log.e(TAG, "imageUrl: " + imageUrl);
            Log.e(TAG, "timeStamp: " + timeStamp);

            if(!NotificationUtils.isAppInBackground(getApplicationContext()))
            {
                //App is in foreground. Broadcast the push message

                Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
                pushNotification.putExtra("message", message);
                LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);


                //Play notification sound
                new NotificationUtils(getApplicationContext()).playNotificationSound();
            }
            else
            {
                //App is in background. Show the notification in notification tray

                Intent resultIntent = new Intent(getApplicationContext(), MainActivity.class);
                resultIntent.putExtra("message", message);

                //Check for image attachment
                if(TextUtils.isEmpty(imageUrl))
                {
                    showNotificationMessage(getApplicationContext(), title, message, timeStamp, resultIntent);
                }
                else
                {
                    //Image is present. Show notification with image
                    showNotificationWithBigImage(getApplicationContext(), title, message, timeStamp, resultIntent, imageUrl);
                }
            }

        }
        catch (JSONException e)
        {
            Log.e(TAG, "Json Exception " + e.getMessage());
        }
        catch (Exception e)
        {
            Log.e(TAG, "Exception " + e.getMessage());

        }
    }

    //Showing Notification with text only

    private void showNotificationMessage(Context context, String title, String message, String timeStamp, Intent intent)
    {
        NotificationUtils notificationUtils = new NotificationUtils(context);
        intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK | intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title,message,timeStamp,intent);
    }

    //Showing notification with text and images

    private void showNotificationWithBigImage(Context context, String title, String message, String timeStamp, Intent intent, String imageUrl)
    {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK | intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent, imageUrl);
    }
}