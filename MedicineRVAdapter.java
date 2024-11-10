package com.example.feedhope.ProviderInterface.MedicineDonation;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.feedhope.AdminInterface.DutyAssign;
import com.example.feedhope.AdminInterface.InformDonation;
import com.example.feedhope.R;
import java.util.ArrayList;

public class MedicineRVAdapter extends RecyclerView.Adapter<MedicineRVAdapter.ViewHolder> {
    private ArrayList<MedicineModalClass> ModelClasses;
    private Context context;

    public MedicineRVAdapter(ArrayList<MedicineModalClass> ModalArrayList, Context context) {
        this.ModelClasses = ModalArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_medicine, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        MedicineModalClass modal = ModelClasses.get(position);
        holder.name.setText(modal.getName());
        holder.medicineName.setText(modal.getMedicineName());
        holder.quantity.setText(modal.getQuantity());
        holder.condition.setText(modal.getCondition());
        holder.manufacture.setText(modal.getManufacture());
        holder.expiry.setText(modal.getExpire());

        holder.assign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DutyAssign.class);
                context.startActivity(intent);
            }
        });
        holder.inform.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, InformDonation.class);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return ModelClasses.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name,medicineName,quantity,condition,manufacture,expiry;
        Button assign,inform;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            medicineName = itemView.findViewById(R.id.medicineName);
            quantity = itemView.findViewById(R.id.quantity);
            condition = itemView.findViewById(R.id.condition);
            manufacture = itemView.findViewById(R.id.manufacture);
            expiry = itemView.findViewById(R.id.expiry);

            assign = itemView.findViewById(R.id.assign);
            inform = itemView.findViewById(R.id.inform);
        }
    }
}