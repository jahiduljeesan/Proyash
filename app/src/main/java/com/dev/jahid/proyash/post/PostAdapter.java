package com.dev.jahid.proyash.post;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.dev.jahid.proyash.R;
import com.dev.jahid.proyash.authentication.UserAuthentication;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostHolder> {
    private Context context;
    private List<PostModel> postList;
    private OnButtonClick onButtonClick;
    private DatabaseReference postReference;
    private AlertDialog alertDialog;

    public void setOnButtonClick(OnButtonClick onButtonClick) {
        this.onButtonClick = onButtonClick;
    }

    public void setPostList(List<PostModel> postList) {
        this.postList = postList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PostAdapter.PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.post_data_style, parent, false);
        return new PostHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostAdapter.PostHolder holder, int position) {

        PostModel postPosition = postList.get(position);

        // Hiding description if empty
        if (postPosition.getPostDescription().isEmpty()) {
            holder.postDescription.setVisibility(View.GONE);
        } else {
            holder.postDescription.setVisibility(View.VISIBLE);
        }

        // Hiding the second contact number and call button if it's empty
        if (postPosition.getPhoneNumber2().isEmpty()) {
            holder.btnCall2.setVisibility(View.GONE);
            holder.postContactNumber2.setVisibility(View.GONE);
        } else {
            holder.btnCall2.setVisibility(View.VISIBLE);
            holder.postContactNumber2.setVisibility(View.VISIBLE);
        }

        // Setting delete button visibility if the user is an admin or the post owner
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            parseAdmin(holder, holder.getAdapterPosition());
        }

        setValues(holder, holder.getAdapterPosition());
    }

    private void setValues(@NonNull PostAdapter.PostHolder holder, int position) {
        PostModel postPosition = postList.get(position);
        holder.postPersonName.setText(postPosition.getPostPersonName());
        holder.postGroup.setText(postPosition.getPostGroup());
        holder.postLocation.setText(postPosition.getPostLocation());
        holder.postPatientType.setText(postPosition.getPostPatientType());
        holder.postDescription.setText("📝 " + postPosition.getPostDescription());
        holder.postTime.setText(postPosition.getDate());

        // Marquee for scrolling text
        holder.postLocation.setSelected(true);
        holder.postPatientType.setSelected(true);

        holder.btnDelete.setOnClickListener(v -> {
            int adapterPosition = holder.getAdapterPosition(); // Refreshes position
            if (adapterPosition != RecyclerView.NO_POSITION) {
                showAlertDialog("ডিলেট করতে চান?", "আপডেট করুন", "না", "ডিলিট করুন", position);
            }
        });

        holder.btnCall1.setOnClickListener(v -> onButtonClick.setOnFirstCallClick(postPosition.getPhoneNumber1(), holder.getAdapterPosition()));
        holder.btnCall2.setOnClickListener(v -> onButtonClick.setOnSecondCallClick(postPosition.getPhoneNumber2(), holder.getAdapterPosition()));

        holder.constraintLayout.setOnLongClickListener(v -> {
            String textToCopy = "🩸" + postPosition.getPostGroup() + " রক্তের প্রয়োজন\n"
                    + "🚑রোগীর ধরনঃ  " + postPosition.getPostPatientType() + "\n"
                    + "📌লোকেশনঃ  " + postPosition.getPostLocation() + "\n"
                    + "📝" + postPosition.getPostDescription() + "\n"
                    + "📞যোগাযোগঃ " + postPosition.getPhoneNumber1() + ", " + postPosition.getPhoneNumber2();

            ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clipData = ClipData.newPlainText("copied_post", textToCopy);
            clipboardManager.setPrimaryClip(clipData);

            Toast.makeText(context, "পোস্ট কপি করা হয়েছে!", Toast.LENGTH_SHORT).show();
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return postList == null ? 0 : postList.size();
    }

    private void parseAdmin(PostAdapter.PostHolder holder, int adapterPosition) {
        if (UserAuthentication.isAdmin || isOwner(adapterPosition)) {
            holder.btnDelete.setVisibility(View.VISIBLE);
        } else {
            holder.btnDelete.setVisibility(View.INVISIBLE);
        }
    }

    private boolean isOwner(int adapterPosition) {
        PostModel postModel = postList.get(adapterPosition);
        String userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        return postModel.getUserEmail().equals(userEmail);
    }

    void showAlertDialog(String title, String yes, String no, String delete, int adapterPosition) {

        String postId = postList.get(adapterPosition).getId(); // Get post ID for deletion

        new AlertDialog.Builder(context)
                .setTitle(title)
                .setIcon(R.drawable.proyash_logo)
                .setPositiveButton(yes, (dialog, which) -> showUpdateAlert(adapterPosition))
                .setNegativeButton(no, (dialog, which) -> dialog.cancel())
                .setNeutralButton(delete, (dialog, which) -> {
                    // First, remove the item from Firebase
                    postReference = FirebaseDatabase.getInstance().getReference("PostData");
                    postReference.child(postId).removeValue().addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(context, "পোস্ট ডিলিট হয়েছে!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "পোস্ট ডিলিট ব্যার্থ হয়েছে!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }).create().show();
    }


    private void showUpdateAlert(int position) {
        PostModel itemsPosition = postList.get(position);

        AlertDialog.Builder updateDialogBuilder = new AlertDialog.Builder(context);
        updateDialogBuilder.setTitle("আপডেট করুন").setIcon(R.drawable.proyash_logo);

        View dialogView = LayoutInflater.from(context).inflate(R.layout.post_update_style, null);
        updateDialogBuilder.setView(dialogView);

        // Setting views with IDs;
        TextInputEditText updatePhone1 = dialogView.findViewById(R.id.updatePhone1);
        TextInputEditText updatePhone2 = dialogView.findViewById(R.id.updatePhone2);
        AutoCompleteTextView updateGroup = dialogView.findViewById(R.id.updateGroup);
        TextInputEditText updateLocation = dialogView.findViewById(R.id.updateLocation);
        TextInputEditText updateDescription = dialogView.findViewById(R.id.updateDescription);
        TextInputEditText updatePatientType = dialogView.findViewById(R.id.updatePatientType);
        Button btnPostUpdate = dialogView.findViewById(R.id.btnPostUpdate);
        ImageView btnDismiss = dialogView.findViewById(R.id.btnDismiss);

        // Setting pre-existing data
        updatePhone1.setText(itemsPosition.getPhoneNumber1());
        updatePhone2.setText(itemsPosition.getPhoneNumber2());
        updateGroup.setText(itemsPosition.getPostGroup());
        updateLocation.setText(itemsPosition.getPostLocation());
        updateDescription.setText(itemsPosition.getPostDescription());
        updatePatientType.setText(itemsPosition.getPostPatientType());

        // Setting adapter to spinner
        updateGroup.setAdapter(new ArrayAdapter(context, android.R.layout.simple_list_item_1, context.getResources().getStringArray(R.array.blood_group_list)));

        // Cancel button
        btnDismiss.setOnClickListener(v -> alertDialog.dismiss());

        // Update button
        btnPostUpdate.setOnClickListener(v -> {
            String id = itemsPosition.getId();
            String personName = itemsPosition.getPostPersonName();
            String postGroup = updateGroup.getText().toString().trim();
            String postPatientType = updatePatientType.getText().toString().trim();
            String postLocation = updateLocation.getText().toString().trim();
            String postDescription = updateDescription.getText().toString().trim();
            String phoneNumber1 = updatePhone1.getText().toString().trim();
            String phoneNumber2 = updatePhone2.getText().toString().trim();
            String date = itemsPosition.getDate();
            String email = itemsPosition.getUserEmail();
            long createdAt = System.currentTimeMillis();

            updateItem(itemsPosition, position, id, personName, postGroup, postPatientType, postLocation, postDescription, phoneNumber1, phoneNumber2, date, email, createdAt);
        });

        alertDialog = updateDialogBuilder.create();
        alertDialog.show();
    }

    private void updateItem(PostModel itemsPosition, int position, String id, String personName, String postGroup, String postPatientType, String postLocation, String postDescription, String phoneNumber1, String phoneNumber2, String date, String email, long createdAt) {
        // Setting data to items model
        itemsPosition.setId(id);
        itemsPosition.setPostPersonName(personName);
        itemsPosition.setPostGroup(postGroup);
        itemsPosition.setPostPatientType(postPatientType);
        itemsPosition.setPostLocation(postLocation);
        itemsPosition.setPostDescription(postDescription);
        itemsPosition.setPhoneNumber1(phoneNumber1);
        itemsPosition.setPhoneNumber2(phoneNumber2);
        itemsPosition.setDate(date);
        itemsPosition.setUserEmail(email);
        itemsPosition.setCreatedAt(createdAt);

        postReference = FirebaseDatabase.getInstance().getReference("PostData");
        String key = itemsPosition.getId();
        postReference.child(key).setValue(itemsPosition)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        notifyItemChanged(position);
                        Toast.makeText(context, "আপডেট করা হয়েছে", Toast.LENGTH_SHORT).show();
                        alertDialog.dismiss();
                    } else {
                        Toast.makeText(context, "আপডেট ব্যার্থ হয়েছে", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public class PostHolder extends RecyclerView.ViewHolder {
        TextView postPersonName, postGroup, postPatientType, postLocation, postDescription, postContactNumber2, postTime;
        ImageView btnCall1, btnCall2, btnDelete;
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

    public interface OnButtonClick {
        void setOnFirstCallClick(String phoneNumber, int position);
        void setOnSecondCallClick(String phoneNumber, int position);
    }
}
