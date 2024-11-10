package com.example.feedhope.ProviderInterface.ToyDonation;

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

public class ToyRVAdapter extends RecyclerView.Adapter<ToyRVAdapter.ViewHolder> {
    private ArrayList<ToyModalClass> ModelClasses;
    private Context context;

    public ToyRVAdapter(ArrayList<ToyModalClass> courseModalArrayList, Context context) {
        this.ModelClasses = courseModalArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_toy, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        ToyModalClass modal = ModelClasses.get(position);
        holder.name.setText(modal.getName());
        holder.toyName.setText(modal.getToyName());
        holder.age.setText(modal.getAge());
        holder.quantity.setText(modal.getQuantity());
        holder.condition.setText(modal.getCondition());
        holder.category.setText(modal.getCategory());

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
        private TextView name,toyName,age,quantity,condition,category;
        Button assign,inform;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            toyName = itemView.findViewById(R.id.toyname);
            age = itemView.findViewById(R.id.age);
            quantity = itemView.findViewById(R.id.quantity);
            condition = itemView.findViewById(R.id.condition);
            category = itemView.findViewById(R.id.category);

            assign = itemView.findViewById(R.id.assign);
            inform = itemView.findViewById(R.id.inform);
        }
    }
}