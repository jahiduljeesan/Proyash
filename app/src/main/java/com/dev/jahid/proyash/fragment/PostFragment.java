package com.dev.jahid.proyash.fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dev.jahid.proyash.R;
import com.dev.jahid.proyash.activity.LoginActivity;
import com.dev.jahid.proyash.activity.PostAddActivity;
import com.dev.jahid.proyash.activity.SignupActivity;
import com.dev.jahid.proyash.adapter.PostAdapter;
import com.dev.jahid.proyash.database.PostModel;
import com.dev.jahid.proyash.databinding.FragmentPostBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PostFragment extends Fragment {

    private FragmentPostBinding binding;
    static int PERMISSION_CODE = 102;
    private List<PostModel> postList = new ArrayList<>();;
    private PostAdapter postAdapter;
    private DatabaseReference postReference;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       binding = FragmentPostBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                ActivityCompat.requestPermissions(requireActivity(),new String[]{Manifest.permission.POST_NOTIFICATIONS},101);
            }
        }


        getPostList();
        Collections.reverse(postList);


        Log.d("postListSize",postList.size()+"");
        binding.postListView.setLayoutManager(new LinearLayoutManager(requireContext()));
        postAdapter = new PostAdapter(requireContext(), postList, new PostAdapter.OnButtonClick() {
            @Override
            public void setOnFirstCallClick(String phoneNumber, int position) {
                startPhoneCall(phoneNumber);

            }

            @Override
            public void setOnSecondCallClick(String phoneNumber, int position) {
                startPhoneCall(phoneNumber);
            }
        });
        binding.postListView.setAdapter(postAdapter);

        binding.btnPostData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isLoggedIn()) {
                    showAlertDialog();
                }else {
                    startActivity(new Intent(requireActivity(), PostAddActivity.class));
                }
            }
        });
        binding.etPostData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isLoggedIn()) {
                    showAlertDialog();
                }
                else {
                    startActivity(new Intent(requireActivity(), PostAddActivity.class));
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        binding.postListView.setAdapter(postAdapter);
    }

    private void startPhoneCall(String phoneNumber) {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(),new String[]{Manifest.permission.CALL_PHONE},PERMISSION_CODE);
        }
        else {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:"+phoneNumber));
            startActivity(callIntent);
        }
    }

    private void getPostList() {
        postReference = FirebaseDatabase.getInstance().getReference("PostData");
        //FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        postReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (haveNetwork(requireContext())) postList.clear();

                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    PostModel postModel = dataSnapshot.getValue(PostModel.class);
                    postList.add(0,postModel);
                    Log.d("PhoneNumebrCheck",postModel.getPhoneNumber1());
                }

                // Log the size after data has been fetched
                Log.d("postListSize", "Size after fetching: " + postList.size());

                // Notify the adapter to refresh the data
                postAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private boolean haveNetwork(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isConnected();
    }

    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("প্রয়াস ২০");
        builder.setIcon(R.drawable.proyash_logo);
        builder.setMessage("অনুগ্রহ করে লগইন করুন!");

        builder.setPositiveButton("লগইন", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(getActivity(), LoginActivity.class));
            }
        });
        builder.setNegativeButton("পরে", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.setNeutralButton("সাইন আপ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(getActivity(), SignupActivity.class));
            }
        });
        builder.create();
        builder.show();
    }

    private boolean isLoggedIn() {
        return FirebaseAuth.getInstance().getCurrentUser() != null;
    }

}