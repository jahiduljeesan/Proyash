package com.dev.jahid.proyash.emergency;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.dev.jahid.proyash.R;
import com.dev.jahid.proyash.authentication.UserAuthentication;
import com.dev.jahid.proyash.databinding.FragmentEmergencyBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class EmergencyFragment extends Fragment {
    private FragmentEmergencyBinding binding;
    private TextInputEditText etNameE,etPhoneE,etTitleE;
    private AutoCompleteTextView etCategoryE;
    private Button btnSubmit;
    static int PERMISSION_CODE = 102;
    private EmergencyAdapter emergencyAdapter;
    private List<EmergencyModel> emergencyList = new ArrayList<>();
    private AlertDialog alertDialog;
    private ImageView btnDismiss;
    String name = "",title = "",phone = "",category = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentEmergencyBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.emergencyListView.setLayoutManager(new LinearLayoutManager(requireContext()));
        emergencyAdapter = new EmergencyAdapter();
        binding.emergencyListView.setAdapter(emergencyAdapter);

        EmergencyViewModel emergencyViewModel = new ViewModelProvider(this).get(EmergencyViewModel.class);
        emergencyViewModel.getEmergencyList().observe(requireActivity(),emergencyList ->{
            this.emergencyList = emergencyList;
            emergencyAdapter.setEmergencyList(emergencyList);
        });

        emergencyAdapter.setOnButtonClick(phoneNumber-> startPhoneCall(phoneNumber));

        binding.chipGroup.setOnCheckedStateChangeListener(new ChipGroup.OnCheckedStateChangeListener() {
            @Override
            public void onCheckedChanged(@NonNull ChipGroup group, @NonNull List<Integer> checkedIds) {
                Log.d("EmergencyFragment", "ChipGroup selection changed");

                boolean isAllSelected = checkedIds.contains(binding.chipAll.getId());

                if (isAllSelected) {
                    for (Integer checkedId : checkedIds) {
                        Chip chip = group.findViewById(checkedId);
                        if (chip != null && chip.getId() != binding.chipAll.getId()) {
                            chip.setChecked(false);
                        }
                    }
                    setFilter("সব");
                } else {
                    if (!checkedIds.isEmpty()) {
                        Chip selectedChip = group.findViewById(checkedIds.get(0));
                        if (selectedChip != null) {
                            String selectedCategory = selectedChip.getText().toString();
                            Log.d("EmergencyFragment", "Selected Category: " + selectedCategory);
                            setFilter(selectedCategory);
                        }
                    }
                }
            }
        });

        binding.bnBack.setOnClickListener(v -> getActivity().onBackPressed());

        binding.btnAdd.setOnClickListener(v-> {
                if (UserAuthentication.isAdmin){
                    showDialog();
                }else {
                    Toast.makeText(requireContext(), "শুধুমাত্র এডমিন প্যানেলের জন্য।", Toast.LENGTH_SHORT).show();
                }
        });

        // Swipe to delete setup
      if (UserAuthentication.isAdmin) {
          swipeToDelete();
      }
    }

    private void swipeToDelete(){
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false; // No move action
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                EmergencyModel emergencyModel = emergencyList.get(position);

                deleteEmergencyItem(emergencyModel, position);
            }
        });
        itemTouchHelper.attachToRecyclerView(binding.emergencyListView);
    }

    private void deleteEmergencyItem(EmergencyModel emergencyModel, int position) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("EmergencyData");

        String itemKey = emergencyModel.getKey();  // Assuming `EmergencyModel` has a `key` field that holds the Firebase reference ID

        databaseReference.child(itemKey).removeValue().addOnSuccessListener(aVoid -> {
            emergencyAdapter.notifyItemRemoved(position);
            Toast.makeText(requireContext(), "Item deleted successfully.", Toast.LENGTH_SHORT).show();
        }).addOnFailureListener(e -> {
            Toast.makeText(requireContext(), "Failed to delete item.", Toast.LENGTH_SHORT).show();
        });
    }


    private void showDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(requireContext());
        alertDialogBuilder.setCancelable(true);

        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.emergency_contact_alert_style,null);
        alertDialogBuilder.setView(dialogView);

        etNameE = dialogView.findViewById(R.id.etNameE);
        etPhoneE = dialogView.findViewById(R.id.etPhoneE);
        etTitleE = dialogView.findViewById(R.id.etTitleE);
        etCategoryE = dialogView.findViewById(R.id.etCategoryE);
        btnSubmit = dialogView.findViewById(R.id.btnSubmitE);
        btnDismiss = dialogView.findViewById(R.id.btnDismiss);

        etCategoryE.setAdapter(new ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.category_list)));

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!setData()) return;
                setDataFirebase(alertDialog);
            }
        });

        btnDismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void setDataFirebase(AlertDialog alertDialog) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("EmergencyData");

        String key = databaseReference.push().getKey();
        EmergencyModel emergencyModel = new EmergencyModel(name,title,phone,category,key);
        databaseReference.child(key).setValue(emergencyModel).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(requireContext(), "ডাটা যোগ করা হয়েছে।", Toast.LENGTH_SHORT).show();
                alertDialog.cancel();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(requireContext(), "ডাটা যোগ ব্যার্থ হয়েছে।", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void setFilter(String category) {
        Log.d("EmergencyFragment", "Filtering with category: " + category);
        List<EmergencyModel> filteredList = new ArrayList<>();

        if (category.equals("সব")) {
            emergencyAdapter.setFilteredList(emergencyList);
            return;
        }

        for (EmergencyModel emergencyModel : emergencyList) {
            if (category.equals(emergencyModel.getCategory())) {
                filteredList.add(emergencyModel);
            }
        }

        Log.d("EmergencyFragment", "Filtered list size: " + filteredList.size());
        emergencyAdapter.setFilteredList(filteredList);
    }

    private void startPhoneCall(String phoneNumber) {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.CALL_PHONE}, PERMISSION_CODE);
        } else {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + phoneNumber));
            startActivity(callIntent);
        }
    }

    private boolean setData() {
        name = etNameE.getText().toString().trim();
        phone = etPhoneE.getText().toString().trim();
        category = etCategoryE.getText().toString().trim();
        title = etTitleE.getText().toString().trim();

        if (name.isEmpty() && phone.isEmpty() && category.isEmpty() && title.isEmpty()) {
            Toast.makeText(requireContext(), "ডাটা সঠিকভাবে প্রবেশ করুন!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}