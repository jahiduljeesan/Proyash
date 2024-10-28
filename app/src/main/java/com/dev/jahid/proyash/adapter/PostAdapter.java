package com.dev.jahid.proyash.adapter;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.dev.jahid.proyash.R;
import com.dev.jahid.proyash.database.PostModel;
import com.dev.jahid.proyash.database.UserAuthentication;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostHolder> {
    private Context context;
    private List<PostModel> postList;
    private OnButtonClick onButtonClick;
    public static boolean firstTime = true;


    public PostAdapter(Context context, List<PostModel> postList, OnButtonClick onButtonClick) {
        this.context = context;
        this.postList = postList;
        this.onButtonClick = onButtonClick;
    }

    @NonNull
    @Override
    public PostAdapter.PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.post_data_style,parent,false);
        return new PostHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostAdapter.PostHolder holder, int position) {

        PostModel postPosition = postList.get(position);

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            parseAdmin(holder);
            String  userEmail =  FirebaseAuth.getInstance().getCurrentUser().getEmail();
            if (postPosition.getUserEmail().equals(userEmail)){
                holder.btnDelete.setVisibility(View.VISIBLE);
            }
        }

        if (postPosition.getPostDescription().isEmpty()) holder.postDescription.setVisibility(View.GONE);
        if (postPosition.getPhoneNumber2().isEmpty()){
            holder.btnCall2.setVisibility(View.INVISIBLE);
            holder.postContactNumber2.setVisibility(View.INVISIBLE);
        }


        setValues(holder,position);

    }

    private void setValues(@NonNull PostAdapter.PostHolder holder, int position) {
        PostModel postPosition = postList.get(position);
        holder.postPersonName.setText(postPosition.getPostPersonName());
        holder.postGroup.setText(postPosition.getPostGroup());
        holder.postLocation.setText(postPosition.getPostLocation());
        holder.postPatientType.setText(postPosition.getPostPatientType());
        holder.postDescription.setText("📝 "+postPosition.getPostDescription());
        holder.postTime.setText(postPosition.getDate());

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        holder.btnCall1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonClick.setOnFirstCallClick(postPosition.getPhoneNumber1(),holder.getAdapterPosition());
            }
        });

        holder.btnCall2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonClick.setOnSecondCallClick(postPosition.getPhoneNumber2(),holder.getAdapterPosition());
            }
        });

        holder.constraintLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {


                String textToCopy = "🩸"+postPosition.getPostGroup()+" রক্তের প্রয়োজন"+"\n"
                        +"🚑রোগীর ধরনঃ  "+postPosition.getPostPatientType()+"\n"
                        +"📌লোকেশনঃ  "+postPosition.getPostLocation()+"\n"
                        +"📝"+postPosition.getPostDescription()+"\n"
                        +"📞যোগাযোগঃ "+postPosition.getPhoneNumber1()+", "+postPosition.getPhoneNumber2();

                ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("copied_post",textToCopy);
                clipboardManager.setPrimaryClip(clipData);

                Toast.makeText(context, "পোস্ট কপি করা হয়েছে!", Toast.LENGTH_SHORT).show();

                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    private void parseAdmin(PostAdapter.PostHolder holder) {
        if (firstTime){
            new UserAuthentication(new UserAuthentication.IsAuthenticate() {
                @Override
                public void setIsAuthenticate(boolean isAdmin) {
                    if (isAdmin){
                        holder.btnDelete.setVisibility(View.VISIBLE);
                    }
                    else {
                        holder.btnDelete.setVisibility(View.INVISIBLE);
                    }
                    firstTime = false;
                }
            });
        }else {
            if (UserAuthentication.isAdmin){
                holder.btnDelete.setVisibility(View.VISIBLE);
            }
            else {
                holder.btnDelete.setVisibility(View.INVISIBLE);
            }
        }
    }

    public class PostHolder extends RecyclerView.ViewHolder {
        TextView postPersonName,postGroup,postPatientType,postLocation,postDescription,postContactNumber2,postTime;
        ImageView btnCall1,btnCall2,btnDelete;
        ConstraintLayout constraintLayout;
         public PostHolder(@NonNull View itemView) {
            super(itemView);
            postPersonName = itemView.findViewById(R.id.postPersonName);
            postGroup = itemView.findViewById(R.id.postGroup);
            postPatientType = itemView.findViewById(R.id.postPatientType);
            postLocation = itemView.findViewById(R.id.postLocation);
            postDescription = itemView.findViewById(R.id.postDescription);
            btnCall1 = itemView.findViewById(R.id.btnCall1);
            btnCall2 = itemView.findViewById(R.id.btnCall2);
            postContactNumber2 = itemView.findViewById(R.id.postContactNumber2);
            constraintLayout = itemView.findViewById(R.id.dataStyleId);
            postTime = itemView.findViewById(R.id.postTime);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }

    public interface OnButtonClick{
        void setOnFirstCallClick(String phoneNumber,int position);
        void setOnSecondCallClick(String phoneNumber,int position);
    }
}
