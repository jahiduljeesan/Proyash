package com.dev.jahid.proyash.authentication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.dev.jahid.proyash.databinding.ActivitySignupBinding;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignupActivity extends AppCompatActivity {

    private ActivitySignupBinding binding;
    private String firstName = "",lastName = "",fullName = "",email = "", firstPass = "",lastPass = "",passWord = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.btnGoToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignupActivity.this,LoginActivity.class));
            }
        });

        binding.btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!getData()) return;
                UserData userData = new UserData(SignupActivity.this,fullName,email,passWord);
            }
        });

    }


    private boolean getData() {
        firstName = binding.etFirstName.getText().toString();
        if (firstName.isEmpty()) {
            binding.etFirstNameLayout.setError("নাম প্রবেশ করুন!");
            return false;
        }

        lastName = binding.etLastName.getText().toString();
        if (firstName.isEmpty()) {
            binding.etLastNameLayout.setError("নাম প্রবেশ করুন!");
            return false;
        }

        fullName = firstName +" "+ lastName;

        email = binding.etEmail.getText().toString();
        if (firstName.isEmpty()) {
            binding.etEmailLayout.setError("ইমেইল প্রবেশ করুন!");
            return false;
        }

        Log.d("Validecke",email);
        Log.d("Valideck",validEmail(email)+"");
        if (!validEmail(email)) {
            binding.etEmailLayout.setError("সঠিক ইমেইল প্রবেশ করুন!");
            return false;
        }

        firstPass = binding.etPassword.getText().toString();
        if (firstName.isEmpty()) {
            binding.etPasswordLayout.setError("পাসওয়ার্ড প্রবেশ করুন!");
            return false;
        }

        lastPass = binding.etConfirmPassword.getText().toString();
        if (firstName.isEmpty()) {
            binding.etConfirmPasswordLayout.setError("পাসওয়ার্ড প্রবেশ করুন!");
            return false;
        }

        if (!firstPass.equals(lastPass)) {
            binding.etConfirmPasswordLayout.setError("অনুগ্রহ করে একই পাসওায়ার্ড প্রবেশ করুন");
            return false;
        }

        passWord = firstPass;

        return true;
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