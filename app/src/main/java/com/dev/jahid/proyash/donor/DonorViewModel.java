package com.dev.jahid.proyash.donor;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DonorViewModel extends ViewModel {
    private MutableLiveData<List<DonorModel>> allList;
    private MutableLiveData<List<DonorModel>> apList;
    private MutableLiveData<List<DonorModel>> anList;
    private MutableLiveData<List<DonorModel>> bpList;
    private MutableLiveData<List<DonorModel>> bnList;
    private MutableLiveData<List<DonorModel>> opList;
    private MutableLiveData<List<DonorModel>> onList;
    private MutableLiveData<List<DonorModel>> abpList;
    private MutableLiveData<List<DonorModel>> abnList;

    private List<DonorModel> all = new ArrayList<>();
    private List<DonorModel> ap= new ArrayList<>();
    private List<DonorModel> an= new ArrayList<>();
    private List<DonorModel> bp= new ArrayList<>();
    private List<DonorModel> bn= new ArrayList<>();
    private List<DonorModel> op= new ArrayList<>();
    private List<DonorModel> on= new ArrayList<>();
    private List<DonorModel> abp = new ArrayList<>();
    private List<DonorModel> abn = new ArrayList<>();

    public DonorViewModel() {
        allList = new MutableLiveData<>();
        apList = new MutableLiveData<>();
        anList = new MutableLiveData<>();
        bpList = new MutableLiveData<>();
        bnList = new MutableLiveData<>();
        opList = new MutableLiveData<>();
        onList = new MutableLiveData<>();
        abpList = new MutableLiveData<>();
        abnList = new MutableLiveData<>();

        loadDonorData();
    }

    public LiveData<List<DonorModel>> getAllList() {
        return allList;
    }

    public LiveData<List<DonorModel>> getApList() {
        return apList;
    }

    public LiveData<List<DonorModel>> getAnList() {
        return anList;
    }

    public LiveData<List<DonorModel>> getBpList() {
        return bpList;
    }

    public LiveData<List<DonorModel>> getBnList() {
        return bnList;
    }

    public LiveData<List<DonorModel>> getOpList() {
        return opList;
    }

    public LiveData<List<DonorModel>> getOnList() {
        return onList;
    }

    public LiveData<List<DonorModel>> getAbpList() {
        return abpList;
    }

    public LiveData<List<DonorModel>> getAbnList() {
        return abnList;
    }

    private void loadDonorData() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("DonorData");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                clearAllList();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    DonorModel itemsModel = dataSnapshot.getValue(DonorModel.class);
                    all.add(0, itemsModel);
                    Log.d("DonorCheck",itemsModel.getGroup());
                    setData(itemsModel.getGroup(), itemsModel);
                    Log.d("DonorCheck",itemsModel.getName());
                }

                allList.setValue(all);
                apList.setValue(ap);
                anList.setValue(an);
                bpList.setValue(bp);
                bnList.setValue(bn);
                opList.setValue(op);
                onList.setValue(on);
                abpList.setValue(abp);
                abnList.setValue(abn);



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("AppData", "Database error: " + error.getMessage());

            }
        });
    }

    private void clearAllList() {
        all.clear();
    }

    private void setData(String group, DonorModel itemsModel) {
        switch (group) {
            case "AB+":abp.add(0,itemsModel); break;
            case "AB-":abn.add(0,itemsModel); break;
            case "A+": ap.add(0,itemsModel); break;
            case "A-": an.add(0,itemsModel); break;
            case "B+": bp.add(0,itemsModel); break;
            case "B-": bn.add(0,itemsModel); break;
            case "O+": op.add(0,itemsModel); break;
            case "O-": on.add(0,itemsModel); break;
            default: Log.d("AppData", "Unknown blood group: " + group);
        }
    }
}
