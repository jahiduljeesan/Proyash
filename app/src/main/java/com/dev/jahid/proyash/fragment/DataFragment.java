package com.dev.jahid.proyash.fragment;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

    public DataFragment(List<ItemsModel> itemsModels) {
        this.itemsList = itemsModels;
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

        //AppData appData = AppData.getAppData();
        //appData.setAppDataCallback(this);
        //binding.progressCircular.setVisibility(View.VISIBLE);
        binding.dataListView.setLayoutManager(new LinearLayoutManager(requireContext()));
        itemAdapter = new ItemAdapter(requireContext(),itemsList);
        binding.dataListView.setAdapter(itemAdapter);
        binding.progressCircular.setVisibility(View.GONE);
    }
}