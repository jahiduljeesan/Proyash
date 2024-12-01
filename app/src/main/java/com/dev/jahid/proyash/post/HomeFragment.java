package com.dev.jahid.proyash.post;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dev.jahid.proyash.R;
import com.dev.jahid.proyash.authentication.LoginActivity;
import com.dev.jahid.proyash.authentication.SignupActivity;
import com.dev.jahid.proyash.databinding.FragmentHomeBinding;
import com.google.android.material.appbar.AppBarLayout;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    static int PERMISSION_CODE = 102;
    private List<PostModel> postList = new ArrayList<>();
    private PostAdapter postAdapter;
    private static AddButtonClicked addButtonClicked;

    public static void getAddButtonClick(AddButtonClicked addButton) {
        addButtonClicked = addButton;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initializing callback interface for app bar visibility
        binding.appbar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                int totalScrollRange = appBarLayout.getTotalScrollRange();

                if (Math.abs(verticalOffset) == totalScrollRange) {
                    binding.toolbar.setVisibility(View.VISIBLE);  // Collapsed
                } else if (verticalOffset == 0) {
                    binding.toolbar.setVisibility(View.INVISIBLE);  // Fully expanded
                } else {
                    binding.toolbar.setVisibility(View.INVISIBLE);  // Somewhere in between
                }
            }
        });

        // Request notification permission if necessary
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.POST_NOTIFICATIONS}, 101);
            }
        }

        Log.d("postListSize", postList.size() + "");

        // Initialize RecyclerView and adapter
        binding.postListView.setLayoutManager(new LinearLayoutManager(requireContext()));
        postAdapter = new PostAdapter();
        binding.postListView.setAdapter(postAdapter);

        PostViewModel postViewModel = new ViewModelProvider(this).get(PostViewModel.class);
        postViewModel.getPostList().observe(requireActivity(),postList -> {
            this.postList = postList;
            postAdapter.setPostList(postList);
        });
        postAdapter.setOnButtonClick(new PostAdapter.OnButtonClick() {
            @Override
            public void setOnFirstCallClick(String phoneNumber, int position) {
                startPhoneCall(phoneNumber);
            }
            @Override
            public void setOnSecondCallClick(String phoneNumber, int position) {
                startPhoneCall(phoneNumber);
            }
        });

        // Add button click listener for post creation
        binding.btnPostData.setOnClickListener(v -> {
            if (!isLoggedIn()) {
                showAlertDialog();
            } else {
                addButtonClicked.setClick();
            }
        });

        // Edit text for creating new post
        binding.etPostData.setOnClickListener(v -> {
            if (!isLoggedIn()) {
                showAlertDialog();
            } else {
                addButtonClicked.setClick();
            }
        });
    }

    private void startPhoneCall(String phoneNumber) {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.CALL_PHONE}, PERMISSION_CODE);
        } else {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + phoneNumber));
            startActivity(callIntent);
        }
    }

    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("প্রয়াস ২০");
        builder.setIcon(R.drawable.proyash_logo);
        builder.setMessage("অনুগ্রহ করে লগইন করুন!");

        builder.setPositiveButton("লগইন", (dialog, which) -> startActivity(new Intent(getActivity(), LoginActivity.class)));
        builder.setNegativeButton("পরে", (dialog, which) -> dialog.cancel());
        builder.setNeutralButton("সাইন আপ", (dialog, which) -> startActivity(new Intent(getActivity(), SignupActivity.class)));
        builder.create();
        builder.show();
    }

    private boolean isLoggedIn() {
        return FirebaseAuth.getInstance().getCurrentUser() != null;
    }

    public interface AddButtonClicked {
        void setClick();
    }

    public interface IsFetched {
        void dataIsFetched();
    }

    public class WrapContentLinearLayoutManager extends LinearLayoutManager {

        public WrapContentLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
            super(context, orientation, reverseLayout);
        }

        //... constructor
        @Override
        public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
            try {
                super.onLayoutChildren(recycler, state);
            } catch (IndexOutOfBoundsException e) {
                Log.e("TAG", "meet a IOOBE in RecyclerView");
            }
        }
    }
}
