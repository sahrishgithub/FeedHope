package com.example.unitconverter.ProviderInterface;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.unitconverter.AdminInterface.AdminHomePage;
import com.example.unitconverter.AdminInterface.AssignDuty;
import com.example.unitconverter.R;
import java.util.ArrayList;

public class FoodRVAdapter extends RecyclerView.Adapter<FoodRVAdapter.ViewHolder> {

    private ArrayList<ProvideFoodModalClass> foodModalClasses;
    private Context context;

    public FoodRVAdapter(ArrayList<ProvideFoodModalClass> courseModalArrayList, Context context) {
        this.foodModalClasses = courseModalArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_food, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        final int itemPosition = position;
        ProvideFoodModalClass modal = foodModalClasses.get(position);
        holder.foodName.setText(modal.getFood());
        holder.quantityName.setText(modal.getQuantity());
        holder.storageName.setText(modal.getStorage());
        holder.availableName.setText(modal.getAvailable());
        holder.expireName.setText(modal.getExpire());
        holder.assign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AssignDuty.class);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return foodModalClasses.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView foodName, quantityName, storageName, availableName, expireName;
        Button assign;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            foodName = itemView.findViewById(R.id.food);
            quantityName = itemView.findViewById(R.id.quantity);
            storageName = itemView.findViewById(R.id.storage);
            availableName = itemView.findViewById(R.id.available);
            expireName = itemView.findViewById(R.id.expire);
            assign = itemView.findViewById(R.id.assign);
        }
    }
}