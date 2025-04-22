package com.dev.jahid.proyash.tools;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class CleanupWorker extends Worker {
    public CleanupWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        deleteOldData();
        return Result.success();
    }

    private void deleteOldData() {
        DatabaseReference postReference = FirebaseDatabase.getInstance().getReference("PostData");
        long oneMonthAgo = System.currentTimeMillis() - (30L * 24 * 60 * 60 * 1000); // 30 days ago in milliseconds

        // Query to find posts older than one month
        Query oldDataQuery = postReference.orderByChild("createdAt").endAt(oneMonthAgo);
        oldDataQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Delete each child that meets the criteria
                    snapshot.getRef().removeValue();
                    Log.d("CleanupWorker", "Deleted post with ID: " + snapshot.getKey());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("CleanupWorker", "Error deleting old data", databaseError.toException());
            }
        });
    }
}
