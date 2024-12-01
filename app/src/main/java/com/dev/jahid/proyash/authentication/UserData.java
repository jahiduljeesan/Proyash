package com.dev.jahid.proyash.authentication;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.dev.jahid.proyash.R;
import com.dev.jahid.proyash.activity.MainActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserData {
    private Context context;
    private String fullName,email,password;
    private DatabaseReference userReference;
    private FirebaseAuth userAuth;
    private String uid;
    private SharedPreferences sharedPrefs;

    public UserData(Context context, String fullName, String email, String password) {
        this.context = context;
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        setDataSignUp();
    }

    private void setDataSignUp() {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Signing up");
        progressDialog.setMessage("Please wait...");
        progressDialog.setIcon(R.drawable.proyash_logo);
        progressDialog.setCancelable(false);
        progressDialog.show();

        userReference = FirebaseDatabase.getInstance().getReference("UserData");
        userAuth = FirebaseAuth.getInstance();

        userAuth.createUserWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                FirebaseUser user = authResult.getUser();
                if (user == null) return;
                uid = user.getUid();
                // saving user full name into database and shared preferences
                userReference.child(uid).setValue(new UserModel(fullName,false,false))
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                if (progressDialog.isShowing()) progressDialog.dismiss();

                                sharedPrefs = context.getSharedPreferences("com.dev.jahid.proyash.userdata",Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPrefs.edit();
                                editor.putString("userName",fullName);
                                editor.putString("userEmail",email);
                                editor.apply();

                                Log.d("Signup test","test5");
                                Toast.makeText(context, "রেজিস্ট্রেশন সফল হয়েছে!", Toast.LENGTH_SHORT).show();
                                context.startActivity(new Intent(context, MainActivity.class));
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("Signup test","test6");
                                Log.d("Firebase", "User data saved successfully.");
                                if (progressDialog.isShowing()) progressDialog.dismiss();

                            }
                        });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("Signup test","test7");
                if (progressDialog.isShowing()) progressDialog.dismiss();
                Toast.makeText(context,e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }


}
