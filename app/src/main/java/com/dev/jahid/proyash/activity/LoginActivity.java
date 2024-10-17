package com.dev.jahid.proyash.activity;

import android.content.Intent;
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
import com.dev.jahid.proyash.databinding.ActivityLoginBinding;
import com.dev.jahid.proyash.databinding.ActivitySignupBinding;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    String email = "",password = "";

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
    }
    public void setData(){
        email = binding.etEmail.getText().toString();
        password = binding.etPassword.getText().toString();

        if(email.isEmpty() || password.isEmpty())
            Toast.makeText(this, "Enter email and password properly", Toast.LENGTH_SHORT).show();
        if (!validEmail(email)) {
            binding.etEmailLayout.setError("Enter valid email.");
        }
    }
    private boolean validEmail(@NonNull String email) {

        if (email.isEmpty()) return false;

        Pattern p = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(email);

        Log.d("email is valid",m.find()+"");
        return m.find();
    }
}