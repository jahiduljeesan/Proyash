package com.dev.jahid.proyash.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.dev.jahid.proyash.R;
import com.dev.jahid.proyash.database.ItemsModel;
import com.dev.jahid.proyash.database.UserAuthentication;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemHoder> {

    private Context context;
    private List<ItemsModel> itemsList;
    private OnItemClick onItemClick;

    public ItemAdapter(Context context, List<ItemsModel> itemsList,OnItemClick onItemClick) {
        this.context = context;
        this.itemsList = itemsList;
        this.onItemClick = onItemClick;
    }

    @NonNull
    @Override
    public ItemAdapter.ItemHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.data_item_desgin,parent,false);
        return new ItemHoder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemAdapter.ItemHoder holder, int position) {
        ItemsModel itemsPosition = itemsList.get(position);

        if (FirebaseAuth.getInstance().getCurrentUser() == null && !UserAuthentication.isAdmin && !itemsPosition.isForEveryone()) {
            holder.itemView.setVisibility(View.GONE); // Hides the item
            ViewGroup.LayoutParams params = holder.itemView.getLayoutParams();
            params.height = 0; // Collapse the view's height
            holder.itemView.setLayoutParams(params);
            return;
        }


        if (UserAuthentication.isAdmin) {
            if (!itemsPosition.isForEveryone()) {
                holder.donorItems.setBackgroundColor(ContextCompat.getColor(context,R.color.color_transparent));
                mangageDonor(holder,"অ্যাড করতে চান?","হ্যাঁ","না","ডিলিট করুন");
            }
            else {
                mangageDonor(holder,"ডিলিট করতে চান?","","না","ডিলিট করুন");
            }
        }
        setData(holder,position);
    }

    private void mangageDonor(ItemAdapter.ItemHoder holder,String title, String yes,String no, String delete) {
        holder.donorItems.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showAlertDialog(title,yes,no,delete);
                return true;
            }
        });
    }

    public void setFilteredList(List<ItemsModel> filteredList) {
        this.itemsList = filteredList;
        notifyDataSetChanged();
    }

    private void setData(@NonNull ItemAdapter.ItemHoder holder, int position) {
        ItemsModel itemPosition = itemsList.get(position);

        if (itemPosition.getGender().equals("পুরুষ")) {
            holder.itemImage.setImageDrawable(context.getDrawable(R.drawable.men_icon));

        } else if (itemPosition.getGender().equals("মহিলা")) {
            holder.itemImage.setImageDrawable(context.getDrawable(R.drawable.women_icon));
        }

        holder.tvName.setText(itemPosition.getName());
        holder.tvAddress.setText(itemPosition.getVillage()+", ("+itemPosition.getUnion()+")");
        holder.tvGroup.setText(itemPosition.getGroup());

        holder.btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onItemClick.setOnCallButtonClick(itemPosition.getPhone(),holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemsList.size();
    }

    public class ItemHoder extends RecyclerView.ViewHolder {

        ImageView btnCall;
        TextView tvName,tvAddress,tvGroup;
        ImageView itemImage;
        ConstraintLayout donorItems;

        public ItemHoder(@NonNull View itemView) {
            super(itemView);
            btnCall = itemView.findViewById(R.id.btnCall);
            tvName = itemView.findViewById(R.id.tvName);
            tvAddress = itemView.findViewById(R.id.tvAddress);
            tvGroup = itemView.findViewById(R.id.tvGroup);
            itemImage = itemView.findViewById(R.id.itemImage);
            donorItems = itemView.findViewById(R.id.donorItems);
        }
    }

    void showAlertDialog(String title, String yes,String no, String delete){
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setIcon(R.drawable.proyash_logo)
                .setPositiveButton(yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setNegativeButton(no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setNeutralButton(delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).create().show();
    }

    public interface OnItemClick{
        void setOnCallButtonClick(String phoneNumber,int position);
    }
}
