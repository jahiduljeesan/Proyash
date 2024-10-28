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
        binding.btnJahidGh.setOnClickListener(this);
        binding.btnJahidLi.setOnClickListener(this);
        binding.btnJahidMail.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
       if (v.getId() == R.id.btnJahidFb) loadUrl("https://facebook.com/jahidul107");
       if (v.getId() == R.id.btnJahidMail) sendGmail();
       if (v.getId() == R.id.btnJahidLi) loadUrl("https://www.linkedin.com/in/md-jahidul-islam-a848292b9/?utm_source=share&utm_campaign=share_via&utm_content=profile&utm_medium");
       if (v.getId() == R.id.btnJahidGh) loadUrl("https://github.com/jahiduljeesan");
    }

    private void sendGmail() {
        String gmail_id = "jahiduljeesan@gmail.com";
        String subject = "Proyash 2020";

        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] {gmail_id});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT,subject);
        emailIntent.setType("message/rfc822");
        startActivity(emailIntent);
    }

    private void loadUrl(String url) {
        try{
            Uri url_uri = Uri.parse(url);
            Intent loadIntent = new Intent(Intent.ACTION_VIEW,url_uri);
            startActivity(Intent.createChooser(loadIntent,"Select one"));
        }catch (Exception e) {
            Toast.makeText(requireContext(), "ইউআরএল লোড করতে ব্যার্থ", Toast.LENGTH_SHORT).show();
        }
    }
}