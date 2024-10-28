package com.dev.jahid.proyash.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.dev.jahid.proyash.R;
import com.dev.jahid.proyash.activity.MainActivity;
import com.dev.jahid.proyash.database.PostModel;
import com.dev.jahid.proyash.fragment.PostFragment;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseServices extends Service {

    private static final String TAG = "FirebaseMonitoringService";
    private static final String CHANNEL_ID = "firebase_channel";

    private DatabaseReference postReference;

    @Override
    public void onCreate() {
        super.onCreate();

        // Create the notification channel for the foreground service (Android O+)
        createNotificationChannel();

        // Start the service in the foreground with an initial notification
//        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
//                .setContentTitle("Monitoring Firebase")
//                .setContentText("Service is running and monitoring for changes.")
//                .setSmallIcon(R.drawable.proyash_logo)  // Use your app's icon
//                .build();
//
//        startForeground(1, notification);

        // Initialize Firebase database reference
        postReference = FirebaseDatabase.getInstance().getReference("PostData");

        // Start monitoring Firebase for new child additions
        monitorFirebaseChanges();
    }

    // Monitor the Firebase Realtime Database for new data
    private void monitorFirebaseChanges() {
         postReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String previousChildName) {
                // When a new child is added to the "notifications" node
                Log.d(TAG, "New data added: " + dataSnapshot.toString());

                PostModel postModel = dataSnapshot.getValue(PostModel.class);
                String bloodGroup = postModel.getPostGroup();
                String address = postModel.getPostLocation();

                // Get the data (e.g., title, message)
                String title = bloodGroup + " রক্ত প্রয়োজন";
                String message = address+"";

                // Send notification with the new data
                sendNotification(title, message);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, String s) {}

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {}

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, String s) {}

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "Database error: " + databaseError.getMessage());
            }
        });
    }

    // Send a notification with the new data
    private void sendNotification(String title, String message) {
        // Create an Intent to open the MainActivity
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        // Create the PendingIntent
        PendingIntent pendingIntent = PendingIntent.getActivity(
                this,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Build the notification
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.drawable.proyash_logo)  // Use your app's icon here
                .setAutoCancel(true);

        // Show the notification
        notificationManager.notify(2, notificationBuilder.build());
    }

    // Create notification channel for Android O+ devices
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Firebase Monitoring",
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("Channel for Firebase Monitoring");

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
