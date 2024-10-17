package com.dev.jahid.proyash.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.dev.jahid.proyash.R;
import com.dev.jahid.proyash.databinding.FragmentInfoBinding;

public class InfoFragment extends Fragment implements View.OnClickListener{

    private FragmentInfoBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentInfoBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().finish();
            }
        });

        //setting on click listeners to the all button
        binding.btnJahidFb.setOnClickListener(this);
        binding.btnOchienFb.setOnClickListener(this);
        binding.btnFoysalFb.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
       if (v.getId() == R.id.btnJahidFb) loadUrl("https://facebook.com/jahidul107");
       if (v.getId() == R.id.btnOchienFb) loadUrl("https://facebook.com/ws.mochien.1");
       if (v.getId() == R.id.btnFoysalFb) loadUrl("https://facebook.com/khairulislam.foysal01743449167");
    }

    private void loadUrl(String url) {
        try{
            Uri url_uri = Uri.parse(url);
            startActivity(new Intent(Intent.ACTION_VIEW,url_uri));
        }catch (Exception e) {
            Toast.makeText(requireContext(), "ইউ আর এল লোড করতে ব্যার্থ", Toast.LENGTH_SHORT).show();
        }
    }
}