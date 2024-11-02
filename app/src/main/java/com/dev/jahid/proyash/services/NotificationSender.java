package com.dev.jahid.proyash.services;

import android.content.Context;
import android.util.Log;

import com.google.auth.oauth2.GoogleCredentials;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class NotificationSender {

    private static final String TAG = "NotificationSender";
    private static final String FCM_API_URL = "https://fcm.googleapis.com/v1/projects/proyash-342af/messages:send"; // Replace with your Project ID
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    private static OkHttpClient client = new OkHttpClient();
    private static ExecutorService executorService = Executors.newSingleThreadExecutor();

    // Get the OAuth token using the service account
    private static String getAccessToken(Context context) throws IOException {
        InputStream serviceAccount = context.getAssets().open("proyash_key.json"); // Make sure this file is in your assets folder
        GoogleCredentials googleCredentials = GoogleCredentials.fromStream(serviceAccount)
                .createScoped(Collections.singleton("https://www.googleapis.com/auth/firebase.messaging"));
        googleCredentials.refreshIfExpired();
        return googleCredentials.getAccessToken().getTokenValue();
    }

    // Method to send notification to all users subscribed to a topic
    public static void sendNotificationToTopic(Context context, String title, String body) {
        executorService.execute(() -> {
            try {
                String accessToken = getAccessToken(context);

                // Construct the notification payload
                String jsonPayload = "{"
                        + "\"message\":{"
                        + "\"topic\":\"allUsers\","
                        + "\"notification\":{"
                        + "\"title\":\"" + title + "\","
                        + "\"body\":\"" + body + "\""
                        + "}"
                        + "}"
                        + "}";

                RequestBody requestBody = RequestBody.create(jsonPayload, JSON);
                Request request = new Request.Builder()
                        .url(FCM_API_URL)
                        .addHeader("Authorization", "Bearer " + accessToken)
                        .addHeader("Content-Type", "application/json")
                        .post(requestBody)
                        .build();

                try (Response response = client.newCall(request).execute()) {
                    if (response.isSuccessful()) {
                        Log.d(TAG, "Notification sent successfully: " + response.body().string());
                    } else {
                        Log.e(TAG, "Failed to send notification: " + response.message());
                    }
                }
            } catch (Exception e) {
                Log.e(TAG, "Error in generating access token or sending notification", e);
            }
        });
    }
}
