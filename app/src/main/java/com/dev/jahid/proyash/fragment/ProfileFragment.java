package com.dev.jahid.proyash.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.dev.jahid.proyash.activity.AboutActivity;
import com.dev.jahid.proyash.authentication.LoginActivity;
import com.dev.jahid.proyash.authentication.UserAuthentication;
import com.dev.jahid.proyash.databinding.FragmentProfileBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private DatabaseReference userReference;
    private FirebaseAuth userAuth;
    private String uid;
    private SharedPreferences userSharedPrefs;
    private String userName,userEmail;
    public static boolean firstTime = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        userSharedPrefs = getActivity().getSharedPreferences("com.dev.jahid.proyash.userdata", Context.MODE_PRIVATE);
        userName = userSharedPrefs.getString("userName","");
        userEmail = userSharedPrefs.getString("userEmail","");

        Log.d("user_name",userName+"  "+userEmail);


        if (!isLoggedIn()) {
            binding.loginOrSupButton.setVisibility(View.VISIBLE);
            binding.profileDivider2.setVisibility(View.INVISIBLE);
            binding.btnLogOut.setVisibility(View.INVISIBLE);
            binding.tvUserName.setVisibility(View.INVISIBLE);
            binding.tvUserEmail.setVisibility(View.INVISIBLE);
            binding.tvFragmentName.setText("প্রোফাইল");
        }else {
            Log.d("userchk",userName);
            Log.d("userchk",userEmail);
            binding.tvUserName.setText(userName);
            binding.tvUserEmail.setText(userEmail);

            binding.loginOrSupButton.setVisibility(View.INVISIBLE);
            binding.profileDivider2.setVisibility(View.VISIBLE);
            binding.btnLogOut.setVisibility(View.VISIBLE);
            binding.tvUserName.setVisibility(View.VISIBLE);
            binding.tvUserEmail.setVisibility(View.VISIBLE);

            parseAdmin();
        }



        binding.loginOrSupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });

        binding.buttonShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareApp();
            }
        });

        binding.btnInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setIntent("info");
            }
        });
        binding.btnAboutApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setIntent("about");
            }
        });
        binding.btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                firebaseAuth.signOut();

                SharedPreferences.Editor editor = userSharedPrefs.edit();
                editor.clear();
                editor.apply();

                ProfileFragment.firstTime = true;
                UserAuthentication.isAdmin = false;

                requireActivity().onBackPressed();
                Toast.makeText(requireContext(), "লগ আউট সফল হয়েছে!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void parseAdmin() {
            if (UserAuthentication.isAdmin){
                binding.tvFragmentName.setText("প্রোফাইল(এডমিন)");
            }
            else {
                binding.tvFragmentName.setText("প্রোফাইল");
            }
    }

    private void setIntent(String tag) {
        Intent intent = new Intent(requireActivity(), AboutActivity.class);
        intent.putExtra("fragment_tag",tag);
        startActivity(intent);
    }

    private void shareApp() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT,"প্রয়াস ২০২০ অ্যাপ ডাউনলোড করুন : https://proyash.wapkiz.com/");
        shareIntent.setType("text/plain");
        startActivity(shareIntent);
    }

    private boolean isLoggedIn() {
        return FirebaseAuth.getInstance().getCurrentUser() != null;
    }
}