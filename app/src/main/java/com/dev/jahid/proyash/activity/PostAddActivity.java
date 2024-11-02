package com.dev.jahid.proyash.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.dev.jahid.proyash.R;
import com.dev.jahid.proyash.database.PostModel;
import com.dev.jahid.proyash.databinding.ActivityPostAddBinding;
import com.dev.jahid.proyash.fragment.PostFragment;
import com.dev.jahid.proyash.services.NotificationSender;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PostAddActivity extends AppCompatActivity {
    
    private ActivityPostAddBinding binding;
    FirebaseDatabase postDatabase;
    DatabaseReference postReference;
    private String key = "", postPersonName = "",postGroup = "",postPatientType = "",postLocation = "",postDescription = "",phoneNumber1 = "",phoneNumber2 = "",date = "",email = "";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPostAddBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.postGroup.setAdapter(new ArrayAdapter(PostAddActivity.this, android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.blood_group_list)));

        binding.btnDismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.btnPostSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!initData()) return;
                ProgressDialog progressDialog = new ProgressDialog(PostAddActivity.this);
                progressDialog.setTitle("তথ্য আপডেট করা হচ্ছে...");
                progressDialog.setMessage("অনুগ্রহ করে অপেক্ষা করুন");
                progressDialog.setCancelable(false);
                progressDialog.show();

                SimpleDateFormat formater = new SimpleDateFormat("dd/mm/yyyy hh:mm a", Locale.ENGLISH);
                Date now = new Date();
                date = formater.format(now);

                postDatabase = FirebaseDatabase.getInstance();
                postReference = postDatabase.getReference("PostData");
                key = postReference.push().getKey();

                SharedPreferences userSharedPrefs = getSharedPreferences("com.dev.jahid.proyash.userdata", Context.MODE_PRIVATE);
                email = userSharedPrefs.getString("userEmail","");

                PostModel postModel = new PostModel(key,postPersonName,postGroup,postPatientType,postLocation,postDescription,phoneNumber1,phoneNumber2,date,email);

                postReference.child(key).setValue(postModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        if (progressDialog.isShowing()) progressDialog.dismiss();
                        // Get the data (e.g., title, message)
                        String title = postGroup + " রক্ত প্রয়োজন";
                        String body = postLocation+"";
                        NotificationSender.sendNotificationToTopic(PostAddActivity.this, title, body);
                        Toast.makeText(PostAddActivity.this, "পোস্ট করা হয়েছে", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if (progressDialog.isShowing()) progressDialog.dismiss();
                        Toast.makeText(PostAddActivity.this, "পোস্ট করা ব্যার্থ হয়েছে", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        
    }

    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("প্রয়াস ২০");
        builder.setIcon(R.drawable.proyash_logo);
        builder.setMessage("অনুগ্রহ করে লগইন করুন!");

        builder.setPositiveButton("লগইন", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(PostAddActivity.this,LoginActivity.class));
            }
        });
        builder.setNegativeButton("পরে", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.setNeutralButton("সাইন আপ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(PostAddActivity.this,LoginActivity.class));
            }
        });
        builder.create();
        builder.show();
    }

    private boolean isLoggedIn() {
        return FirebaseAuth.getInstance().getCurrentUser() != null;
    }

    private boolean initData() {
        postPersonName = getSharedPreferences("com.dev.jahid.proyash.userdata", Context.MODE_PRIVATE).getString("userName","");
        if (postPersonName.isEmpty()){
            return false;
        }

        postLocation = binding.postLocation.getText().toString();
        if (postLocation.isEmpty()) {
            binding.postLocationLayout.setError("রক্তদানের লোকেশন সিলেক্ট করুন");
            return false;
        }

        postPatientType = binding.postPatientType.getText().toString();
        if (postPatientType.isEmpty()) {
            binding.postLocationLayout.setError("রক্তদানের স্থান নির্বাচন করুন");
            return false;
        }

        phoneNumber1 = binding.postPhone1.getText().toString();
        if (phoneNumber1.isEmpty()) {
            binding.postLocationLayout.setError("মোবাইল নম্বর প্রবেশ করুন");
            return false;
        }

        postGroup = binding.postGroup.getText().toString();
        postDescription = binding.postDescription.getText().toString();
        phoneNumber2 = binding.postPhone2.getText().toString();

        return true;
    }
}