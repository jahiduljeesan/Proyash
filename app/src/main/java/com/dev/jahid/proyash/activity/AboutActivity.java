package com.dev.jahid.proyash.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.dev.jahid.proyash.R;
import com.dev.jahid.proyash.databinding.ActivityAboutBinding;
import com.dev.jahid.proyash.fragment.AboutAppFragment;
import com.dev.jahid.proyash.fragment.InfoFragment;

public class AboutActivity extends AppCompatActivity {
    private ActivityAboutBinding binding;
    private String tag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAboutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        tag = getIntent().getStringExtra("fragment_tag");

        if (tag != null) {
            switch (tag) {
                case "info": addFragment(new InfoFragment());
                    break;
                case "about": addFragment(new AboutAppFragment());
                    break;
            }
        }


    }

    private void addFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.aboutFragmentContainer,fragment);
        fragmentTransaction.commit();
    }
}