package com.dev.jahid.proyash.database;

import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AppData {
    public List<ItemsModel> allList = new ArrayList<>();
    public List<ItemsModel> apList = new ArrayList<>();
    public List<ItemsModel> anList = new ArrayList<>();
    public List<ItemsModel> bpList = new ArrayList<>();
    public List<ItemsModel> bnList = new ArrayList<>();
    public List<ItemsModel> opList = new ArrayList<>();
    public List<ItemsModel> onList = new ArrayList<>();
    public List<ItemsModel> abpList = new ArrayList<>();
    public List<ItemsModel> abnList = new ArrayList<>();

    public static volatile AppData appData;

    AppDataCallback appDataCallback;


    public AppData() {
        initAppData();
    }

    public static AppData getAppData() {
        if (appData == null) {
            appData = new AppData();
        }
        return appData;
    }

    public void setAppDataCallback(AppDataCallback appDataCallback){
        this.appDataCallback = appDataCallback;
    }


    private void initAppData() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("DonorData");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                clearAllList();
                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    ItemsModel itemsModel =  dataSnapshot.getValue(ItemsModel.class);
                    allList.add(itemsModel);
                    String group = itemsModel.getGroup();
                    setData(group,itemsModel);
                }
                appDataCallback.displayData(true);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void clearAllList() {
        allList.clear();
        apList.clear();
        anList.clear();
        bpList.clear();
        bnList.clear();
        opList.clear();
        onList.clear();
        abpList.clear();
        abnList.clear();
    }

    private void setData(String group, ItemsModel itemsModel) {

        switch (group) {
            case "AB+" : abpList.add(itemsModel);
                break;
            case "AB-" : abnList.add(itemsModel);
                break;
            case "A+" : apList.add(itemsModel);
                break;
            case "A-" : anList.add(itemsModel);
                break;
            case "B+" : bpList.add(itemsModel);
                break;
            case "B-" : bnList.add(itemsModel);
                break;
            case "O+" : opList.add(itemsModel);
                break;
            case "O-" : onList.add(itemsModel);
                break;
        }
    }
    public interface AppDataCallback {
        void displayData(Boolean isDataLoaded);
    }
}
