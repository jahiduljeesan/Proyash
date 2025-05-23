package com.dev.jahid.proyash.donor.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.dev.jahid.proyash.R;
import com.dev.jahid.proyash.authentication.UserAuthentication;
import com.dev.jahid.proyash.donor.DonorModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class DonorAdapter extends RecyclerView.Adapter<DonorAdapter.ItemHoder> {

    private Context context;
    private List<DonorModel> itemsList;
    private OnItemClick onItemClick;
    private AlertDialog alertDialog;

    public void setItemsList(List<DonorModel> itemsList) {
        this.itemsList = itemsList;
        notifyDataSetChanged();
    }

    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    @NonNull
    @Override
    public DonorAdapter.ItemHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.data_item_desgin, parent, false);
        return new ItemHoder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DonorAdapter.ItemHoder holder, int position) {
        // Ensure the position is valid before accessing the list
        if (position < 0 || position >= itemsList.size()) {
            return; // Invalid position, do nothing
        }

        DonorModel itemsPosition = itemsList.get(position);

        if ((FirebaseAuth.getInstance().getCurrentUser() == null || !UserAuthentication.isAdmin) && !itemsPosition.isForEveryone()) {
            holder.itemView.setVisibility(View.GONE); // Hides the item
            ViewGroup.LayoutParams params = holder.itemView.getLayoutParams();
            params.height = 0; // Collapse the view's height
            holder.itemView.setLayoutParams(params);
            return;
        }

        if (UserAuthentication.isAdmin) {
            if (!itemsPosition.isForEveryone()) {
                holder.donorItems.setBackgroundColor(ContextCompat.getColor(context, R.color.color_transparent));
                manageDonor(holder, "অ্যাড করতে চান?", "হ্যাঁ", "না", "ডিলিট করুন", holder.getAdapterPosition());
            } else {
                holder.donorItems.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
                manageDonor(holder, "ডিলিট করতে চান?", "আপডেট করুন", "না", "ডিলিট করুন", holder.getAdapterPosition());
            }
        }
        setData(holder, position);
    }

    private void manageDonor(DonorAdapter.ItemHoder holder, String title, String yes, String no, String delete, int position) {
        holder.donorItems.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showAlertDialog(title, yes, no, delete, position);
                return true;
            }
        });
    }

    public void setFilteredList(List<DonorModel> filteredList) {
        this.itemsList = filteredList;
        notifyDataSetChanged();
    }

    private void setData(@NonNull DonorAdapter.ItemHoder holder, int position) {
        if (position < 0 || position >= itemsList.size()) {
            return; // Invalid position, do nothing
        }

        DonorModel itemPosition = itemsList.get(position);

        if (itemPosition.getGender().equals("পুরুষ")) {
            holder.itemImage.setImageDrawable(context.getDrawable(R.drawable.men_icon));
        } else if (itemPosition.getGender().equals("মহিলা")) {
            holder.itemImage.setImageDrawable(context.getDrawable(R.drawable.women_icon));
        }

        holder.tvName.setText(itemPosition.getName());
        holder.tvAddress.setText(itemPosition.getVillage() + "(" + itemPosition.getUnion() + ")");
        holder.tvGroup.setText(itemPosition.getGroup());

        holder.btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick.setOnCallButtonClick(itemPosition.getPhone(), holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemsList == null? 0 : itemsList.size();
    }

    public class ItemHoder extends RecyclerView.ViewHolder {

        ImageView btnCall;
        TextView tvName, tvAddress, tvGroup;
        ImageView itemImage;
        ConstraintLayout donorItems;

        public ItemHoder(@NonNull View itemView) {
            super(itemView);
            btnCall = itemView.findViewById(R.id.btnCall);
            tvName = itemView.findViewById(R.id.tvName);
            tvAddress = itemView.findViewById(R.id.tvTitle);
            tvGroup = itemView.findViewById(R.id.tvGroup);
            itemImage = itemView.findViewById(R.id.itemImage);
            donorItems = itemView.findViewById(R.id.donorItems);
        }
    }

    void showAlertDialog(String title, String yes, String no, String delete, int position) {
        if (position < 0 || position >= itemsList.size()) {
            return; // Invalid position, do nothing
        }

        new AlertDialog.Builder(context)
                .setTitle(title)
                .setIcon(R.drawable.proyash_logo)
                .setPositiveButton(yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (yes.equals("হ্যাঁ")) {
                            approveItem(itemsList.get(position), position, true);
                        } else {
                            showUpdateAlert(position);
                        }
                    }
                }).setNegativeButton(no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).setNeutralButton(delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (position >= 0 && position < itemsList.size()) {
                            onItemClick.onDeleteClick(position,itemsList);
                        }
                    }
                }).create().show();
    }

    private void showUpdateAlert(int position) {
        if (position < 0 || position >= itemsList.size()) return;

        DonorModel itemsPosition = itemsList.get(position);

        AlertDialog.Builder updateDialogBuilder = new AlertDialog.Builder(context);
        updateDialogBuilder.setTitle("আপডেট করুন").setIcon(R.drawable.proyash_logo);

        View dialogView = LayoutInflater.from(context).inflate(R.layout.update_donor_layout, null);
        updateDialogBuilder.setView(dialogView);

        //setting views with id;
        TextInputEditText etUpdateName = dialogView.findViewById(R.id.etUpdateName);
        TextInputEditText etUpdatePhone = dialogView.findViewById(R.id.etUpdatePhone);
        AutoCompleteTextView spnUpdateGender = dialogView.findViewById(R.id.spnUpdateGender);
        AutoCompleteTextView spnUpdateUnion = dialogView.findViewById(R.id.spnUpdateUnion);
        TextInputEditText etUpdateVillage = dialogView.findViewById(R.id.etUpdateVillage);
        AutoCompleteTextView spnUpdateGroup = dialogView.findViewById(R.id.spnUpdateGroup);
        Button btnUpdate = dialogView.findViewById(R.id.btnUpdate);
        ImageView btnDismiss = dialogView.findViewById(R.id.btnDismiss);

        //setting pre added data
        etUpdateName.setText(itemsPosition.getName());
        etUpdatePhone.setText(itemsPosition.getPhone());
        spnUpdateGender.setText(itemsPosition.getGender());
        spnUpdateUnion.setText(itemsPosition.getUnion());
        etUpdateVillage.setText(itemsPosition.getVillage());
        spnUpdateGroup.setText(itemsPosition.getGroup());

        //setting adapter to spinner
        spnUpdateGender.setAdapter(new ArrayAdapter(context, android.R.layout.simple_list_item_1, context.getResources().getStringArray(R.array.gender_list)));
        spnUpdateUnion.setAdapter(new ArrayAdapter(context, android.R.layout.simple_list_item_1, context.getResources().getStringArray(R.array.union_list)));
        spnUpdateGroup.setAdapter(new ArrayAdapter(context, android.R.layout.simple_list_item_1, context.getResources().getStringArray(R.array.blood_group_list)));

        btnDismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        //updateButton
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etUpdateName.getText().toString().trim();
                String phone = etUpdatePhone.getText().toString().trim();
                String gender = spnUpdateGender.getText().toString().trim();
                String union = spnUpdateUnion.getText().toString().trim();
                String village = etUpdateVillage.getText().toString().trim();
                String group = spnUpdateGroup.getText().toString().trim();
                String id = itemsPosition.getId();
                String username = itemsPosition.getId();
                boolean forEveryone = itemsPosition.isForEveryone();

                //updating data to items model
                updateItem(itemsPosition, position, id, name, phone, gender, union, village, group, username, forEveryone);
            }
        });

        alertDialog = updateDialogBuilder.create();
        alertDialog.show();
    }

    private void updateItem(DonorModel itemsPosition, int position, String id, String name, String phone, String gender, String union, String village, String group, String username, boolean forEveryone) {
        // setting data to items model
        itemsPosition.setId(id);
        itemsPosition.setName(name);
        itemsPosition.setPhone(phone);
        itemsPosition.setGender(gender);
        itemsPosition.setUnion(union);
        itemsPosition.setVillage(village);
        itemsPosition.setGroup(group);
        itemsPosition.setUsername(username);
        itemsPosition.setForEveryone(forEveryone);

        String key = itemsPosition.getId();
        DatabaseReference itemReference = FirebaseDatabase.getInstance().getReference("DonorData");
        itemReference.child(key).setValue(itemsPosition)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(context, "আপডেট করা হয়েছে", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "আপডেট ব্যর্থ হয়েছে", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void approveItem(DonorModel itemsPosition, int position, boolean forEveryone) {
        itemsPosition.setForEveryone(true);

        String key = itemsPosition.getId();
        DatabaseReference itemReference = FirebaseDatabase.getInstance().getReference("DonorData");
        itemReference.child(key).setValue(itemsPosition)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            notifyItemChanged(position);
                            Toast.makeText(context, "আপডেট করা হয়েছে", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "আপডেট ব্যর্থ হয়েছে", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public interface OnItemClick {
        void setOnCallButtonClick(String phoneNumber, int position);
        void onDeleteClick(int position,List<DonorModel> list);
    }
}
