package com.example.feedhope.ProviderInterface.ClothDonation;

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

public class ClothRVAdapter extends RecyclerView.Adapter<ClothRVAdapter.ViewHolder> {

    private ArrayList<ClothModelClass> clothModelClasses;
    private Context context;

    public ClothRVAdapter(ArrayList<ClothModelClass> courseModalArrayList, Context context) {
        this.clothModelClasses = courseModalArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_cloth, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        ClothModelClass modal = clothModelClasses.get(position);
        holder.name.setText(modal.getName());
        holder.type.setText(modal.getType());
        holder.condition.setText(modal.getCondition());
        holder.quantity.setText(modal.getQuantity());
        holder.category.setText(modal.getCategory());
        holder.seasonal.setText(modal.getSeasonal());
        holder.size.setText(modal.getSize());

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
        return clothModelClasses.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name,type,condition,quantity,category,seasonal,size;
        Button assign,inform;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            type = itemView.findViewById(R.id.type);
            condition = itemView.findViewById(R.id.condition);
            quantity = itemView.findViewById(R.id.quantity);
            category = itemView.findViewById(R.id.category);
            seasonal = itemView.findViewById(R.id.seasonal);
            size = itemView.findViewById(R.id.size);

            assign = itemView.findViewById(R.id.assign);
            inform = itemView.findViewById(R.id.inform);
        }
    }
}