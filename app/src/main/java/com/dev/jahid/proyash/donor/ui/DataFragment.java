package com.dev.jahid.proyash.donor.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.dev.jahid.proyash.databinding.FragmentDataBinding;
import com.dev.jahid.proyash.donor.adapter.DonorAdapter;
import com.dev.jahid.proyash.donor.DonorModel;
import com.dev.jahid.proyash.donor.DonorViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class DataFragment extends Fragment{

    private FragmentDataBinding binding;
    private List<DonorModel> itemsList = new ArrayList<>();
    private DonorAdapter itemAdapter;
    static int PERMISSION_CODE = 100;
    private String listTag;
    private DatabaseReference itemReference;

    public DataFragment(String listTag) {
        this.listTag = listTag;
    }

    public DataFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDataBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        itemAdapter.notifyDataSetChanged();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        itemReference = FirebaseDatabase.getInstance().getReference("DonorData");

        binding.dataListView.setLayoutManager(new LinearLayoutManager(requireContext()));
        itemAdapter = new DonorAdapter();
        binding.dataListView.setAdapter(itemAdapter);

        initArrays();

        itemAdapter.setOnItemClick(new DonorAdapter.OnItemClick() {
            @Override
            public void setOnCallButtonClick(String phoneNumber, int position) {
                if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(requireActivity(),new String[]{Manifest.permission.CALL_PHONE},PERMISSION_CODE);
                }
                else {
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:"+phoneNumber));
                    startActivity(callIntent);
                }
            }

            @Override
            public void onDeleteClick(int position, List<DonorModel> list) {
                if (position >= 0 && position < itemsList.size()) {
                    String itemId = itemsList.get(position).getId();
                    itemReference.child(itemId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(requireContext(), "ডিলিট করা হয়েছে!", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(requireContext(), "ডিলিট ব্যার্থ হয়েছে!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(requireContext(), "ডিলিট ব্যার্থ হয়েছে!", Toast.LENGTH_SHORT).show();
                }
            }

        });

    }

    private void initArrays() {
        DonorViewModel donorViewModel = new ViewModelProvider(this).get(DonorViewModel.class);

        if (listTag == "all") {
            donorViewModel.getAllList().observe(requireActivity(),allList -> {
                itemAdapter.setItemsList(allList);
                itemsList.addAll(allList);
            });
        }
        if (listTag == "abp") {
            donorViewModel.getAbpList().observe(requireActivity(),abpList -> {
                itemAdapter.setItemsList(abpList);
                itemsList.addAll(abpList);
            });
        }
        if (listTag == "abn") {
            donorViewModel.getAbnList().observe(requireActivity(),abnList -> {
                itemAdapter.setItemsList(abnList);
                itemsList.addAll(abnList);
            });
        }
        if (listTag == "ap") {
            donorViewModel.getApList().observe(requireActivity(),apList -> {
                itemAdapter.setItemsList(apList);
                itemsList.addAll(apList);
            });
        }
        if (listTag == "an") {
            donorViewModel.getAnList().observe(requireActivity(),anList -> {
                itemAdapter.setItemsList(anList);
                itemsList.addAll(anList);
            });
        }
        if (listTag == "bp") {
            donorViewModel.getBpList().observe(requireActivity(),bpList -> {
                itemAdapter.setItemsList(bpList);
                itemsList.addAll(bpList);
            });
        }
        if (listTag == "bn") {
            donorViewModel.getBnList().observe(requireActivity(),bnList -> {
                itemAdapter.setItemsList(bnList);
                itemsList.addAll(bnList);
            });
        }
        if (listTag == "op") {
            donorViewModel.getOpList().observe(requireActivity(),opList -> {
                itemAdapter.setItemsList(opList);
                itemsList.addAll(opList);
            });
        }
        if (listTag == "on") {
            donorViewModel.getOnList().observe(requireActivity(),onList -> {
                itemAdapter.setItemsList(onList);
                itemsList.addAll(onList);
            });
        }

    }

    public void getSearch(String text) {
        List<DonorModel> filteredList = new ArrayList<>();

        for (DonorModel itemsModel : itemsList) {
            if (itemsModel.getName().toLowerCase().contains(text.toLowerCase()) ||
                itemsModel.getPhone().toLowerCase().contains(text.toLowerCase()) ||
                itemsModel.getVillage().toLowerCase().contains(text.toLowerCase()) ||
                    itemsModel.getUnion().toLowerCase().contains(text.toLowerCase())){

                filteredList.add(itemsModel);

            }
            if (!filteredList.isEmpty()) {
                itemAdapter.setFilteredList(filteredList);
            }
        }
    }
}