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
import java.util.ArrayList;

public class FoodInformRVAdapter extends RecyclerView.Adapter<FoodInformRVAdapter.ViewHolder> {

    private ArrayList<FoodInformModalClass> modalClasses;
    private Context context;
    private String loggedInEmail;

    public FoodInformRVAdapter(ArrayList<FoodInformModalClass> modalClasses, Context context) {
        this.modalClasses = modalClasses;
        this.context = context;
        this.loggedInEmail = loggedInEmail;
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

        // Compare the logged-in email with the item's email
        if (modal.getName().equals(loggedInEmail)) {
            // If emails match, show the "Update" button
            holder.update.setVisibility(View.VISIBLE);
            holder.update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, FoodInformUpdateStatus.class);
                    intent.putExtra("email", modal.getName());
                    context.startActivity(intent);
                }
            });
        } else {
            // Hide the "Update" button if emails don't match
            holder.update.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return modalClasses.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name, quantity, storage, expire, status;
        Button update;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            quantity = itemView.findViewById(R.id.quantity);
            storage = itemView.findViewById(R.id.storage);
            expire = itemView.findViewById(R.id.expire);
            status = itemView.findViewById(R.id.status);
            update = itemView.findViewById(R.id.update);
        }
    }
}