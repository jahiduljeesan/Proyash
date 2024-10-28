package com.dev.jahid.proyash.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.dev.jahid.proyash.R;
import com.dev.jahid.proyash.database.AppData;
import com.dev.jahid.proyash.database.ItemsModel;
import com.dev.jahid.proyash.database.UserAuthentication;
import com.dev.jahid.proyash.databinding.FragmentAddBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class AddFragment extends Fragment {

    private FragmentAddBinding binding;
    String[] blood_group_list,union_list;
    String name = "",phone = "",gender = "",union = "",village= "",group= "",user="Unknown";
    boolean forEveryone = false;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference dbReference;
    AppData appData;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        appData = AppData.getAppData(requireContext());

        binding.spnGender.setAdapter(new ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.gender_list)));
        binding.spnUnion.setAdapter(new ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.union_list)));
        binding.spnGroup.setAdapter(new ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.blood_group_list)));

        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!getData()) return;

                ProgressDialog progressDialog = new ProgressDialog(requireContext());
                progressDialog.setTitle("তথ্য আপডেট করা হচ্ছে...");
                progressDialog.setMessage("অনুগ্রহ করে অপেক্ষা করুন");
                progressDialog.setCancelable(false);
                progressDialog.show();

                firebaseDatabase = FirebaseDatabase.getInstance();
                dbReference = firebaseDatabase.getReference("DonorData");
                ItemsModel itemsModel = new ItemsModel(name,phone,gender,union,village,group,user,forEveryone);

                String id = dbReference.push().getKey();
                dbReference.child(id).setValue(itemsModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        if (progressDialog.isShowing()) progressDialog.dismiss();
                        Toast.makeText(requireContext(), "তথ্য আপডেট করা হয়েছে।", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if (progressDialog.isShowing()) progressDialog.dismiss();
                        Toast.makeText(requireContext(), "তথ্য আপডেট ব্যার্থ হয়েছে।", Toast.LENGTH_SHORT).show();
                    }
                });
                clearAll();
            }
        });
    }

    private void clearAll() {
        binding.etName.setText(null);
        binding.etVillage.setText(null);
        binding.etPhone.setText(null);
    }

    private boolean getData() {
        name = binding.etName.getText().toString();
        if (name.isEmpty()) {
            binding.etNameLayout.setError("অনুগ্রহ করে নাম প্রবেশ করুন!");
            return false;
        }
        phone = binding.etPhone.getText().toString();

        if (phone.isEmpty()) {
            binding.etPhoneLayout.setError("অনুগ্রহ করে ফোন নম্বর প্রবেশ করুন!");
            return false;
        }
        gender = binding.spnGender.getText().toString();

        union = binding.spnUnion.getText().toString();

        village = binding.etVillage.getText().toString();
        if (village.isEmpty()) {
            binding.etVillageLayout.setError("অনুগ্রহ করে গ্রামের নাম প্রবেশ করুন!");
            return false;
        }

        forEveryone = UserAuthentication.isAdmin;

        group = binding.spnGroup.getText().toString();

        return true;
    }

    private boolean matchPhoneNumber(String target){
        for (ItemsModel model : AppData.appData.allList) {
            if (target.equals(model.getPhone())) {
                return true;
            }
        }
        return false;
    }


}