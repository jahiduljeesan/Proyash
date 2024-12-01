package com.dev.jahid.proyash.post;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PostViewModel extends ViewModel {
    private MutableLiveData<List<PostModel>> postList;

    public PostViewModel() {
        postList = new MutableLiveData<>();
        loadPostList();
    }

    public LiveData<List<PostModel>> getPostList() {
        return postList;
    }

    private void loadPostList() {
        DatabaseReference postRefs = FirebaseDatabase.getInstance().getReference("PostData");
        postRefs.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<PostModel> posts = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    PostModel postModel = dataSnapshot.getValue(PostModel.class);

                    if (postModel != null && postModel.getCreatedAt() > 0) {
                        long oneMonthAgo = System.currentTimeMillis() - (30L * 24 * 60 * 60 * 1000);

                        if (postModel.getCreatedAt() >= oneMonthAgo) {
                            posts.add(0, postModel);
                        }
                    } else {
                        Log.w("getPostList", "PostModel missing createdAt field");
                    }
                }
                postList.setValue(posts);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("DatabaseCheck","PostDataError");
            }
        });
    }
}
