package com.dev.jahid.proyash.donor.ui;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.dev.jahid.proyash.R;
import com.dev.jahid.proyash.authentication.LoginActivity;
import com.dev.jahid.proyash.authentication.SignupActivity;
import com.dev.jahid.proyash.authentication.UserAuthentication;
import com.dev.jahid.proyash.databinding.ActivityPostAddBinding;
import com.dev.jahid.proyash.donor.DonorModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DonorAddActivity extends AppCompatActivity {
    
    private ActivityPostAddBinding binding;
    String[] blood_group_list,union_list;
    String name = "",phone = "",gender = "",union = "",village= "",group= "",user="Unknown";
    boolean forEveryone = false;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference dbReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPostAddBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.spnGender.setAdapter(new ArrayAdapter(this, android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.gender_list)));
        binding.spnUnion.setAdapter(new ArrayAdapter(this, android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.union_list)));
        binding.spnGroup.setAdapter(new ArrayAdapter(this, android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.blood_group_list)));

        binding.btnBack.setOnClickListener(v -> {finish();});

        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isLoggedIn()) {
                    showAlertDialog();
                    return;
                }
                if (!getData()) return;

                ProgressDialog progressDialog = new ProgressDialog(DonorAddActivity.this);
                progressDialog.setTitle("তথ্য আপডেট করা হচ্ছে...");
                progressDialog.setMessage("অনুগ্রহ করে অপেক্ষা করুন");
                progressDialog.setCancelable(false);
                progressDialog.show();

                firebaseDatabase = FirebaseDatabase.getInstance();
                dbReference = firebaseDatabase.getReference("DonorData");

                String id = dbReference.push().getKey();
                DonorModel itemsModel = new DonorModel(id,name,phone,gender,union,village,group,user,forEveryone);

                dbReference.child(id).setValue(itemsModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        if (progressDialog.isShowing()) progressDialog.dismiss();
                        Log.d("PostAct",UserAuthentication.isAdmin+"");
                        if(!UserAuthentication.isAdmin) {
                            new AlertDialog.Builder(DonorAddActivity.this)
                                    .setIcon(R.drawable.proyash_logo)
                                    .setTitle("প্রয়াস'২০")
                                    .setMessage("তথ্য যোগ করা হয়েছে। এডমিন প্যানেলের অনুমতিক্রমে অ্যাপ এ যোগ করা হবে।")
                                    .setPositiveButton("ওকে",((dialog, which) -> finish()))
                                    .create().show();
                        }else {
                            Toast.makeText(DonorAddActivity.this, "তথ্য আপডেট করা হয়েছে।", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if (progressDialog.isShowing()) progressDialog.dismiss();
                        Toast.makeText(DonorAddActivity.this, "তথ্য আপডেট ব্যার্থ হয়েছে।", Toast.LENGTH_SHORT).show();
                    }
                });
                clearAll();
            }

        });
    }

    private void clearAll() {
        binding.etName.setText(null);
        binding.etVillage.setText(null);
        binding.etPhone.setText(null);
    }

    private boolean getData() {
        name = binding.etName.getText().toString();
        if (name.isEmpty()) {
            binding.etNameLayout.setError("অনুগ্রহ করে নাম প্রবেশ করুন!");
            return false;
        }
        phone = binding.etPhone.getText().toString();

        if (phone.isEmpty()) {
            binding.etPhoneLayout.setError("অনুগ্রহ করে ফোন নম্বর প্রবেশ করুন!");
            return false;
        }
        gender = binding.spnGender.getText().toString();

        union = binding.spnUnion.getText().toString();

        village = binding.etVillage.getText().toString();
        if (village.isEmpty()) {
            binding.etVillageLayout.setError("অনুগ্রহ করে গ্রামের নাম প্রবেশ করুন!");
            return false;
        }

        forEveryone = UserAuthentication.isAdmin;

        group = binding.spnGroup.getText().toString();

        return true;
    }

    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("প্রয়াস ২০");
        builder.setIcon(R.drawable.proyash_logo);
        builder.setMessage("অনুগ্রহ করে লগইন করুন!");

        builder.setPositiveButton("লগইন", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(DonorAddActivity.this, LoginActivity.class));
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
                startActivity(new Intent(DonorAddActivity.this, SignupActivity.class));
            }
        });
        builder.create();
        builder.show();
    }

    private boolean isLoggedIn() {
        return FirebaseAuth.getInstance().getCurrentUser() != null;
    }


}