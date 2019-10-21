package com.triton.voxit.Service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.triton.voxit.Activity.Dashboard;
import com.triton.voxit.R;
import com.triton.voxit.SessionManager.SessionManager;
import com.triton.voxit.model.NotificationAudio;

import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by USER on 15-02-2018.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    private static final String PREFERENCES = "";
    SessionManager session;
    private String CHANNEL_ID = "MyApp";
    public static final String PREFS_NAME = "MyPrefsFile";
    boolean hasLoggedIn;
    SharedPreferences.Editor editor;
    String title, message, click_action;
    private boolean isLogged;

    int notification_id = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);
    private String imagepath;
    private Bitmap bitmap;
    private String bg_image;
    private Bitmap bg_bitmap;
    private String audio;
//    private ArrayList<NotificationAudio> audio;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Context context = this.getApplicationContext();
//        SharedPreferences settings=context.getSharedPreferences(PREFERENCES, 0);
//        isLogged = settings.getBoolean("isLogged", false);

        session = new SessionManager(getApplicationContext());
//        HashMap<String, String> user = session.getUserDetails();
//        Log.d(TAG, "Message: ");
//
//
//        session.checkLogin();
        // session.checkLogin();

//        if (session.isLoggedIn()) {

//        SharedPreferences settings1 = getSharedPreferences(PREFS_NAME, 0);
//        editor = settings1.edit();
//        hasLoggedIn = settings1.getBoolean("hasLoggedIn", false);


        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {

            JSONObject data = new JSONObject(remoteMessage.getData());

            Log.w(TAG, "data " + data.toString());
        }


        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.w(TAG,"on get notifictiom inside");
            Log.w(TAG, "notification data 1 " + remoteMessage.getNotification().getBodyLocalizationKey());
            title = remoteMessage.getNotification().getTitle(); //get title
            message = remoteMessage.getNotification().getBody();
            bg_image = remoteMessage.getNotification().getIcon();
            click_action = remoteMessage.getNotification().getClickAction(); //get click_action
            bg_bitmap = getBitmapfromUrl(bg_image);


            Log.w(TAG, "Notification Title: " + title);
            Log.w(TAG, "Notification Body: " + message);
            Log.w(TAG, "URI " + imagepath);
            Log.w(TAG, "Notification click_action: " + click_action);
            Log.w(TAG, "audio " + audio);

            sendNotification(title, message, click_action, bg_bitmap);
        }else{
            Log.w(TAG,"on get notifictiom else");
        }
        if (remoteMessage.getData() != null) {
            title = remoteMessage.getData().get("title"); //get title
            message = remoteMessage.getData().get("message");
            imagepath = remoteMessage.getData().get("image_path");
            audio = remoteMessage.getData().get("audio");
            click_action = remoteMessage.getNotification().getClickAction(); //get click_action

            Log.w(TAG, "audio " + audio);

            bitmap = getBitmapfromUrl(imagepath);

            sendNotificationData(title, message, click_action, bitmap, audio);

        }
    }

    private void sendNotificationData(String title, String message, String click_action, Bitmap bitmap, String audio) {
        Intent intent = new Intent(this, Dashboard.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        Bundle bundle = new Bundle();
        bundle.putString("type", "song");
        bundle.putString("data", audio);
        intent.putExtras(bundle);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.mipmap.logo)
                .setContentTitle(title)
                .setContentText(message)
                .setStyle(new NotificationCompat.BigPictureStyle()
                        .bigPicture(bitmap))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)// Set the intent that will fire when the user taps the notification
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(notification_id, mBuilder.build());

        createNotificationChannel();
    }

    private void sendNotification(String title, String messageBody, String click_action, Bitmap imagepath) {

        Intent intent = new Intent(this, Dashboard.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.mipmap.logo)
                .setContentTitle(title)
                .setContentText(messageBody)
                .setStyle(new NotificationCompat.BigPictureStyle()
                        .bigPicture(imagepath))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)// Set the intent that will fire when the user taps the notification
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(notification_id, mBuilder.build());

        createNotificationChannel();

    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.app_name);
            String description = getString(R.string.description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public Bitmap getBitmapfromUrl(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(input);
            return bitmap;

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;

        }
    }
}
