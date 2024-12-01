package com.dev.jahid.proyash.notification;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.dev.jahid.proyash.authentication.UserAuthentication;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.onesignal.Continue;
import com.onesignal.OneSignal;
import com.onesignal.debug.LogLevel;

import java.util.concurrent.TimeUnit;

public class ProyashApplication extends Application {

    private static final String ONESIGNAL_APP_ID = "5ed362d2-3e1a-446b-b45c-0fe5aa95be1c";
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        new UserAuthentication(new UserAuthentication.IsAuthenticate() {
            @Override
            public void setIsAuthenticate(boolean isAdmin) {
                if (isAdmin) UserAuthentication.isAdmin = true;
            }
        });

        FirebaseDatabase.getInstance().getReference("PostData").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //OneSignal start............................................................
        OneSignal.getDebug().setLogLevel(LogLevel.VERBOSE);
        OneSignal.initWithContext(this, ONESIGNAL_APP_ID);
        OneSignal.getNotifications().requestPermission(false, Continue.none());

        PeriodicWorkRequest cleanupRequest = new PeriodicWorkRequest.Builder(CleanupWorker.class, 1, TimeUnit.DAYS).build();
        WorkManager.getInstance(this).enqueue(cleanupRequest);

    }
}
