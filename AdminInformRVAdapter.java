package com.example.unitconverter.AdminInterface;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.unitconverter.R;
import com.example.unitconverter.ReceiverInterface.InformDonationModalClass;
import com.example.unitconverter.RiderInterface.DutyModalClass;

import java.util.ArrayList;

public class AdminInformRVAdapter extends RecyclerView.Adapter<AdminInformRVAdapter.ViewHolder> {

    private ArrayList<InformDonationModalClass> modalClasses;
    private Context context;

    public AdminInformRVAdapter(ArrayList<InformDonationModalClass> modalClasses, Context context) {
        this.modalClasses = modalClasses;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_admin_infrom_donation_report, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        final int itemPosition = position;
        InformDonationModalClass modal = modalClasses.get(position);
        holder.name.setText(modal.getName());
        holder.quantity.setText(modal.getQuantity());
        holder.storage.setText(modal.getStorage());
        holder.expire.setText(modal.getExpire());
        holder.status.setText(modal.getStatus());

        if ("Pending".equals(modal.getStatus())) {
            holder.status.setTextColor(Color.RED);
        } else if ("Completed".equals(modal.getStatus())) {
            holder.status.setTextColor(Color.GREEN);
        } else {
            holder.status.setTextColor(Color.BLACK);
        }
    }

    @Override
    public int getItemCount() {
        return modalClasses.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name, quantity, storage, expire,status;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            quantity = itemView.findViewById(R.id.quantity);
            storage = itemView.findViewById(R.id.storage);
            expire = itemView.findViewById(R.id.expire);
            status = itemView.findViewById(R.id.status);
        }
    }
}