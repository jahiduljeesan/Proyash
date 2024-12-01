package com.dev.jahid.proyash.authentication;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

public class UserAuthentication {

    public static boolean isAdmin = false;
    public static volatile UserAuthentication userAuthentication;
    public IsAuthenticate isAuthenticate;

    public UserAuthentication (IsAuthenticate isAuthenticate) {
        this.isAuthenticate = isAuthenticate;
        isAdmin();
    }


    public boolean isAdmin(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user == null) {
            isAdmin = false;
            return false;
        }

        String uid = user.getUid();

        FirebaseDatabase.getInstance().getReference("UserData").child(uid).get()
                .addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                    @Override
                    public void onSuccess(DataSnapshot dataSnapshot) {
                        UserModel userModel = dataSnapshot.getValue(UserModel.class);
                        if (userModel != null){
                            isAuthenticate.setIsAuthenticate(userModel.getAdmin());
                            isAdmin = userModel.getAdmin();
                        }
                    }
                });
        return isAdmin;
    }
    public interface IsAuthenticate{
        void setIsAuthenticate(boolean isAdmin);
    }
}
