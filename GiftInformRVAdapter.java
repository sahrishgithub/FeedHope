package com.example.feedhope.ReceiverInterface.GiftInform;

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

public class GiftInformRVAdapter extends RecyclerView.Adapter<GiftInformRVAdapter.ViewHolder> {

    private ArrayList<GiftInformModalClass> modalClasses;
    private Context context;
    private String loggedInEmail;

    public GiftInformRVAdapter(ArrayList<GiftInformModalClass> modalClasses, Context context) {
        this.modalClasses = modalClasses;
        this.context = context;
        this.loggedInEmail = loggedInEmail;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_gift_detail, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GiftInformModalClass modal = modalClasses.get(position);

        holder.name.setText(modal.getName());
        holder.quantity.setText(modal.getQuantity());
        holder.category.setText(modal.getCategory());
        holder.condition.setText(modal.getCondition());

        if (modal.getName().equals(loggedInEmail)) {
            holder.update.setVisibility(View.VISIBLE);
            holder.update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, GiftInformUpdateStatus.class);
                    intent.putExtra("email", modal.getName());
                    context.startActivity(intent);
                }
            });
        } else {
            holder.update.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return modalClasses.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name, quantity, category, condition, status;
        Button update;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            quantity = itemView.findViewById(R.id.quantity);
            category = itemView.findViewById(R.id.category);
            condition = itemView.findViewById(R.id.condition);
            status = itemView.findViewById(R.id.status);
            update = itemView.findViewById(R.id.update);
        }
    }
}