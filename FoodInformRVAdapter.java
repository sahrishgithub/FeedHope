package com.example.feedhope.ReceiverInterface.FoodInform;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.feedhope.R;
import com.example.feedhope.RiderInterface.Duty.DutyUpdateStatus;

import java.util.ArrayList;

public class FoodInformRVAdapter extends RecyclerView.Adapter<FoodInformRVAdapter.ViewHolder> {

    private ArrayList<FoodInformModalClass> modalClasses;
    private Context context;

    public FoodInformRVAdapter(ArrayList<FoodInformModalClass> modalClasses, Context context) {
        this.modalClasses = modalClasses;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_food_detail, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FoodInformModalClass modal = modalClasses.get(position);
        holder.name.setText(modal.getName());
        holder.quantity.setText(modal.getQuantity());
        holder.storage.setText(modal.getStorage());
        holder.expire.setText(modal.getExpire());
        holder.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, FoodInformUpdateStatus.class);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return modalClasses.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name, quantity, storage, expire;
        Button update;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            quantity = itemView.findViewById(R.id.quantity);
            storage = itemView.findViewById(R.id.storage);
            expire = itemView.findViewById(R.id.expire);

            update = itemView.findViewById(R.id.update);
        }
    }
}