package com.dev.jahid.proyash.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.dev.jahid.proyash.R;
import com.dev.jahid.proyash.adapter.PostAdapter;
import com.dev.jahid.proyash.database.UserAuthentication;
import com.dev.jahid.proyash.database.UserData;
import com.dev.jahid.proyash.database.UserModel;
import com.dev.jahid.proyash.databinding.ActivityLoginBinding;
import com.dev.jahid.proyash.databinding.ActivitySignupBinding;
import com.dev.jahid.proyash.fragment.ProfileFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private String email = "",password = "";
    private DatabaseReference userReference;
    private FirebaseAuth userAuth;
    private String uid;
    private SharedPreferences sharedPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        binding.btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,SignupActivity.class);
                startActivity(intent);
            }
        });
        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parseLogin();
            }
        });
    }

    private void parseLogin() {
        if (!setData()) return;

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Logging in");
        progressDialog.setIcon(R.drawable.proyash_logo);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        userReference = FirebaseDatabase.getInstance().getReference("UserData");
        userAuth = FirebaseAuth.getInstance();

        userAuth.signInWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                FirebaseUser user = authResult.getUser();
                if (user != null){
                    uid = user.getUid();
                }
                userReference.child(uid).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (task.isSuccessful()) {
                            if (progressDialog.isShowing()) progressDialog.dismiss();

                            String fullName = task.getResult().getValue(UserModel.class).getFullName();
                            Log.d("FullNameTest",fullName);

                            sharedPrefs = getSharedPreferences("com.dev.jahid.proyash.userdata",Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPrefs.edit();
                            editor.putString("userName",fullName);
                            editor.putString("userEmail",email);
                            editor.commit();

                            ProfileFragment.firstTime = true;
                            PostAdapter.firstTime = true;

                            startActivity(new Intent(LoginActivity.this,MainActivity.class));

                            Toast.makeText(LoginActivity.this, "লগইন সফল হয়েছে!", Toast.LENGTH_SHORT).show();
                        }else {
                            if (progressDialog.isShowing()) progressDialog.dismiss();
                        }
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (progressDialog.isShowing()) progressDialog.dismiss();
                Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean setData(){
        email = binding.etEmail.getText().toString();
        password = binding.etPassword.getText().toString();

        if(email.isEmpty()){
            binding.etEmailLayout.setError("ইমেইল প্রবেশ করুন!");
            return false;
        }
        if (!validEmail(email)) {
            binding.etEmailLayout.setError("ইমেইল সঠিক নয়!");
            return false;
        }
        if(password.isEmpty()){
            binding.etPasswordLayout.setError("পাসওয়ার্ড প্রবেশ করুন!");
            return false;
        }
        return true;
    }

    private void resetPassword() {
        email = binding.etEmail.getText().toString();

        if(email.isEmpty()){
            binding.etEmailLayout.setError("ইমেইল প্রবেশ করুন!");
            return;
        }
        if (!validEmail(email)) {
            binding.etEmailLayout.setError("ইমেইল সঠিক নয়!");
            return;
        }
    }

    private boolean validEmail(@NonNull String email) {

        Pattern p = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(email);

        if (m.find()) {
            return true;
        }
        return false;
    }
}