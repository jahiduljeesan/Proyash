package com.dev.jahid.proyash.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dev.jahid.proyash.R;
import com.dev.jahid.proyash.database.PostModel;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostHolder> {
    private Context context;
    private List<PostModel> postList;

    public PostAdapter(Context context, List<PostModel> postList) {
        this.context = context;
        this.postList = postList;
    }

    @NonNull
    @Override
    public PostAdapter.PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.post_data_style,parent,false);
        return new PostHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostAdapter.PostHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public class PostHolder extends RecyclerView.ViewHolder {
        TextView postPersonName,postGroup,postPatientType,postLocation,postDescription;
        ImageView btnCall1,btnCall2;
         public PostHolder(@NonNull View itemView) {
            super(itemView);
            postPersonName = itemView.findViewById(R.id.postPersonName);
            postGroup = itemView.findViewById(R.id.postGroup);
            postPatientType = itemView.findViewById(R.id.postPatientType);
            postLocation = itemView.findViewById(R.id.postLocation);
            postDescription = itemView.findViewById(R.id.postDescription);
            btnCall1 = itemView.findViewById(R.id.btnCall1);
            btnCall2 = itemView.findViewById(R.id.btnCall2);
        }
    }
}
