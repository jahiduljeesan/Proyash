package com.dev.jahid.proyash.emergency;

import static com.dev.jahid.proyash.R.drawable.ambulance_icon;
import static com.dev.jahid.proyash.R.drawable.bus_icon;
import static com.dev.jahid.proyash.R.drawable.fire_icon;
import static com.dev.jahid.proyash.R.drawable.pall_icon;
import static com.dev.jahid.proyash.R.drawable.police_icon;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dev.jahid.proyash.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class EmergencyAdapter extends RecyclerView.Adapter<EmergencyAdapter.EmergencyHolder> {
    private Context context;
    private List<EmergencyModel> emergencyList;
    private OnButtonClick onButtonClick;

    public void setOnButtonClick(OnButtonClick onButtonClick) {
        this.onButtonClick = onButtonClick;
    }

    public void setEmergencyList(List<EmergencyModel> emergencyList) {
        this.emergencyList = emergencyList;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public EmergencyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.emergency_contact_style,parent,false);
        return new EmergencyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EmergencyHolder holder, int position) {
        setData(holder,position);
        EmergencyModel emergencyModel = emergencyList.get(position);
        holder.btnCall.setOnClickListener(v -> onButtonClick.onCallClick(emergencyModel.getPhone()));
    }

    public void setFilteredList(List<EmergencyModel> emergencyList) {
        this.emergencyList = emergencyList;
        notifyDataSetChanged();
    }

    private void setData(EmergencyHolder holder, int position) {
        EmergencyModel emergencyModel = emergencyList.get(position);
        switch (emergencyModel.getCategory()) {
            case "পুলিশ": holder.itemImage.setImageDrawable(context.getDrawable(police_icon));
            break;
            case "অ্যাম্বুলেন্স": holder.itemImage.setImageDrawable(context.getDrawable(ambulance_icon));
            break;
            case "পল্লি বিদ্যুৎ অভিযোগ": holder.itemImage.setImageDrawable(context.getDrawable(pall_icon));
            break;
            case "ফায়ার সার্ভিস": holder.itemImage.setImageDrawable(context.getDrawable(fire_icon));
            break;
            default: holder.itemImage.setImageDrawable(context.getDrawable(bus_icon));
        }
        holder.tvName.setText(emergencyModel.getName());
        holder.tvTitle.setText(emergencyModel.getTitle());
        holder.tvPhone.setText(emergencyModel.getPhone());
    }

    @Override
    public int getItemCount() {
        return emergencyList == null? 0 : emergencyList.size();
    }

    public class EmergencyHolder extends RecyclerView.ViewHolder {
        CircleImageView itemImage;
        TextView tvName,tvTitle,tvPhone;
        ImageView btnCall;

        public EmergencyHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tvName);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvPhone = itemView.findViewById(R.id.tvPhone);
            btnCall = itemView.findViewById(R.id.btnCallE);
            itemImage = itemView.findViewById(R.id.itemImage);
        }
    }

    public interface OnButtonClick{
        void onCallClick(String phone);
    }
}
