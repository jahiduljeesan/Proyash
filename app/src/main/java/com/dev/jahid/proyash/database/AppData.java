package com.dev.jahid.proyash.database;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

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

    Context context;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;


    public AppData(Context context) {
        this.context = context;
        initAppData();
    }

    public static AppData getAppData(Context context) {
        if (appData == null) {
            appData = new AppData(context);
        }
        return appData;
    }

    public void setAppDataCallback(AppDataCallback appDataCallback){
        this.appDataCallback = appDataCallback;
    }


    private void initAppData() {
        Log.d("Jahidul","Jahidul islam8");
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseDatabase.setPersistenceEnabled(true);
        databaseReference = firebaseDatabase.getReference("DonorData");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("Jahidul","Jahidul islam1");
                if (haveNetwork(context)) {
                    Log.d("Jahidul","Jahidul islam2");
                    clearAllList();
                }
                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    Log.d("Jahidul","Jahidul islam3");
                    ItemsModel itemsModel =  dataSnapshot.getValue(ItemsModel.class);
                    allList.add(itemsModel);
                    String group = itemsModel.getGroup();
                    setData(group,itemsModel);
                    Log.d("Jahidul1","Jahidul islam4");
                }
                appDataCallback.displayData(true);
                Log.d("Jahidul1","Jahidul islam5");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void clearAllList() {
        Log.d("Jahidul","Jahidul islam6");
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
        Log.d("Jahidul","Jahidul islam7");
    }
    public interface AppDataCallback {
        void displayData(Boolean isDataLoaded);
    }
    private boolean haveNetwork(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isConnected();
    }

}
