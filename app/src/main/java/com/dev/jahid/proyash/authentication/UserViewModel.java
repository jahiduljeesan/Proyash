package com.dev.jahid.proyash.authentication;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

public class UserViewModel extends ViewModel {
    MutableLiveData<String> fullName;
    MutableLiveData<Boolean> userIsAdmin;

    public UserViewModel() {
        fullName = new MutableLiveData<>();
        userIsAdmin = new MutableLiveData<>();
        loadData();
    }

    public LiveData<String> getFullName(){
        return fullName;
    }

    public LiveData<Boolean> userIsAdmin(){
        return userIsAdmin;
    }

    private void loadData() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user == null) {
            fullName.setValue("");
            userIsAdmin.setValue(false);
            return;
        }

        String uid = user.getUid();

        FirebaseDatabase.getInstance().getReference("UserData").child(uid).get()
                .addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                    @Override
                    public void onSuccess(DataSnapshot dataSnapshot) {
                        UserModel userModel = dataSnapshot.getValue(UserModel.class);
                        if (userModel != null){
                            fullName.setValue(userModel.getFullName());
                            userIsAdmin.setValue(userModel.getAdmin());
                        }
                    }
                });
    }
}
