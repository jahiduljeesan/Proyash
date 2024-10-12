package com.dev.jahid.proyash.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.dev.jahid.proyash.R;
import com.dev.jahid.proyash.database.AppData;
import com.dev.jahid.proyash.databinding.ActivitySplashBinding;

public class SplashActivity extends AppCompatActivity{

    private ActivitySplashBinding binding;
    private AppData appData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        appData = AppData.getAppData();
        appData.setAppDataCallback(new AppData.AppDataCallback() {
            @Override
            public void displayData(Boolean isDataLoaded) {
                Log.d("isDataLoaded",isDataLoaded+"");
                if (isDataLoaded) {
                    startActivity(new Intent(SplashActivity.this,MainActivity.class));
                    finish();
                }
            }
        });
    }

}