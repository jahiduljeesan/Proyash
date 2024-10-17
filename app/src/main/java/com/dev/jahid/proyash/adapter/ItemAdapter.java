package com.dev.jahid.proyash.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dev.jahid.proyash.R;
import com.dev.jahid.proyash.database.ItemsModel;

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
        setData(holder,position);
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
        holder.tvAddress.setText(itemPosition.getVillage()+","+itemPosition.getUnion());
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

        public ItemHoder(@NonNull View itemView) {
            super(itemView);
            btnCall = itemView.findViewById(R.id.btnCall);
            tvName = itemView.findViewById(R.id.tvName);
            tvAddress = itemView.findViewById(R.id.tvAddress);
            tvGroup = itemView.findViewById(R.id.tvGroup);
            itemImage = itemView.findViewById(R.id.itemImage);
        }
    }
    public interface OnItemClick{
        void setOnCallButtonClick(String phoneNumber,int position);
    }
}
