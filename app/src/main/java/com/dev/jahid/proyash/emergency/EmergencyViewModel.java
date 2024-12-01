package com.dev.jahid.proyash.emergency;

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

public class EmergencyViewModel extends ViewModel {
    private MutableLiveData<List<EmergencyModel>> emergencyList;

    public EmergencyViewModel() {
        emergencyList = new MutableLiveData<>();
        loadEmergencyList();
    }

    public LiveData<List<EmergencyModel>> getEmergencyList(){
        return emergencyList;
    }
    private void loadEmergencyList() {
        DatabaseReference emergencyRefs = FirebaseDatabase.getInstance().getReference("EmergencyData");
        emergencyRefs.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<EmergencyModel> emergencyModels = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    EmergencyModel emergencyModel = dataSnapshot.getValue(EmergencyModel.class);
                    emergencyModels.add(0,emergencyModel);
                }
                emergencyList.setValue(emergencyModels);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}
