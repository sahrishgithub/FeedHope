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

import com.example.unitconverter.AdminInterface.DutyAssign;
import com.example.unitconverter.AdminInterface.InformDonation;
import com.example.unitconverter.R;

import java.util.ArrayList;

public class FoodRVAdapter extends RecyclerView.Adapter<FoodRVAdapter.ViewHolder> {

    private ArrayList<FoodProvideModalClass> foodModalClasses;
    private Context context;

    public FoodRVAdapter(ArrayList<FoodProvideModalClass> foodModalArrayList, Context context) {
        this.foodModalClasses = foodModalArrayList;
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

        FoodProvideModalClass modal = foodModalClasses.get(position);

        // Displaying updated details including the location
        holder.nameName.setText(modal.getName());
        holder.foodName.setText(modal.getFood());
        holder.quantityName.setText(modal.getQuantity());
        holder.storageName.setText(modal.getStorage());
        holder.availableName.setText(modal.getAvailable());
        holder.expireName.setText(modal.getExpire());

        // New field: location (latitude, longitude)
        String location = "Lat: " + modal.getLatitude() + ", Lon: " + modal.getLongitude();
        holder.locationName.setText(location);

        // Handling button clicks for "Assign" and "Inform" functionalities
        holder.assign.setOnClickListener(v -> {
            Intent intent = new Intent(context, DutyAssign.class);
            context.startActivity(intent);
        });

        holder.inform.setOnClickListener(v -> {
            Intent intent = new Intent(context, InformDonation.class);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return foodModalClasses.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView nameName, foodName, quantityName, storageName, availableName, expireName, locationName;
        Button assign, inform;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameName = itemView.findViewById(R.id.name);
            foodName = itemView.findViewById(R.id.food);
            quantityName = itemView.findViewById(R.id.quantity);
            storageName = itemView.findViewById(R.id.storage);
            availableName = itemView.findViewById(R.id.available);
            expireName = itemView.findViewById(R.id.expire);

            locationName = itemView.findViewById(R.id.location);  // New TextView for location
            assign = itemView.findViewById(R.id.assign);
            inform = itemView.findViewById(R.id.inform);
        }
    }
}
