package com.dev.jahid.proyash.activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
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

        //Full screen activity
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        appData = AppData.getAppData(this);

//        if (!haveNetwork(this)) {
//            new Handler(getMainLooper()).postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    startActivity(new Intent(SplashActivity.this,MainActivity.class));
//                    finish();
//                }
//            },4000);
//        }
//        else {
//
//        }
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

    private boolean haveNetwork(@NonNull Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

}