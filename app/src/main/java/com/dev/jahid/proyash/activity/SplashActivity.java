package com.dev.jahid.proyash.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.dev.jahid.proyash.authentication.LoginActivity;
import com.dev.jahid.proyash.authentication.UserAuthentication;
import com.dev.jahid.proyash.databinding.ActivitySplashBinding;
import com.google.firebase.auth.FirebaseAuth;

public class SplashActivity extends AppCompatActivity {

    private ActivitySplashBinding binding;
    private SharedPreferences sharedPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Full screen activity
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        sharedPrefs = getSharedPreferences("com.dev.jahid.proyash.firstime",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putBoolean("FirstTime",true);
        editor.commit();

        if (!sharedPrefs.getBoolean("FirstTime",true)) {
            if (FirebaseAuth.getInstance().getCurrentUser() == null) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        navigateToMainActivity();
                    }
                },2500);
            } else {
                startApp();
            }
        }else {
            navigateToLoginActivity();
        }

    }

    private void navigateToLoginActivity() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent splashToLogin = new Intent(SplashActivity.this, LoginActivity.class);
                splashToLogin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(splashToLogin);
                finish();
            }
        },2500);
    }

    void startApp() {
        new UserAuthentication(new UserAuthentication.IsAuthenticate() {
            @Override
            public void setIsAuthenticate(boolean isAdmin) {
                    navigateToMainActivity();
            }
        });
    }

    private void navigateToMainActivity() {
        Log.d("SplashActivity", "Navigating to MainActivity");
            Intent splashToMain = new Intent(SplashActivity.this, MainActivity.class);
            splashToMain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(splashToMain);
            finish();
    }

}
