package com.dev.jahid.proyash.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dev.jahid.proyash.R;
import com.dev.jahid.proyash.adapter.ItemAdapter;
import com.dev.jahid.proyash.database.AppData;
import com.dev.jahid.proyash.database.ItemsModel;
import com.dev.jahid.proyash.databinding.FragmentDataBinding;

import java.util.ArrayList;
import java.util.List;

public class DataFragment extends Fragment{

    private FragmentDataBinding binding;
    private List<ItemsModel> itemsList;
    private ItemAdapter itemAdapter;
    static int PERMISSION_CODE = 100;

    public DataFragment(List<ItemsModel> itemsModels) {
        this.itemsList = itemsModels;
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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.dataListView.setLayoutManager(new LinearLayoutManager(requireContext()));
        itemAdapter = new ItemAdapter(requireContext(), itemsList, new ItemAdapter.OnItemClick() {
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
        });
        binding.dataListView.setAdapter(itemAdapter);
    }

    public void getSearch(String text) {
        List<ItemsModel> filteredList = new ArrayList<>();

        for (ItemsModel itemsModel : itemsList) {
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