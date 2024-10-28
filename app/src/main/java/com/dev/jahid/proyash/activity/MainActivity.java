package com.dev.jahid.proyash.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.dev.jahid.proyash.R;
import com.dev.jahid.proyash.databinding.ActivityMainBinding;
import com.dev.jahid.proyash.fragment.AddFragment;
import com.dev.jahid.proyash.fragment.HomeFragment;
import com.dev.jahid.proyash.fragment.PostFragment;
import com.dev.jahid.proyash.fragment.ProfileFragment;
import com.dev.jahid.proyash.services.FirebaseServices;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    int itemId;
    boolean isPressed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Start the Firebase monitoring service
        Intent serviceIntent = new Intent(this, FirebaseServices.class);
        startService(serviceIntent);

        replaceFragment(new HomeFragment());

        binding.bottomNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                itemId = item.getItemId();

                if (itemId == R.id.navHome) replaceFragment(new HomeFragment());
                if (itemId == R.id.navList) replaceFragment(new AddFragment());
                if (itemId == R.id.navProfile) replaceFragment(new ProfileFragment());
                if (itemId == R.id.navRequest) replaceFragment(new PostFragment());

                return true;
            }
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.homeFragmentContainer,fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {

        if (itemId != R.id.navHome) {
            replaceFragment(new HomeFragment());
            binding.bottomNav.setSelectedItemId(R.id.navHome);
        }
        else {
            if (isPressed){
                finish();
                super.onBackPressed();
                return;
            }
            Toast.makeText(this, "Press again to exit!", Toast.LENGTH_SHORT).show();
            isPressed = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    isPressed = false;
                }
            },2000);
        }
    }
}
