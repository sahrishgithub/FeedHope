package com.example.feedhope.ProviderInterface.ShoeDonation;

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

public class ShoeRVAdapter extends RecyclerView.Adapter<ShoeRVAdapter.ViewHolder> {
    private ArrayList<ShoeModalClass> ModelClasses;
    private Context context;

    public ShoeRVAdapter(ArrayList<ShoeModalClass> ModalArrayList, Context context) {
        this.ModelClasses = ModalArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_shoe, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        ShoeModalClass modal = ModelClasses.get(position);
        holder.name.setText(modal.getName());
        holder.type.setText(modal.getType());
        holder.quantity.setText(modal.getQuantity());
        holder.condition.setText(modal.getCondition());
        holder.gender.setText(modal.getGender());
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
        return ModelClasses.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name,type,quantity,condition,gender,size;
        Button assign,inform;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            type = itemView.findViewById(R.id.type);
            quantity = itemView.findViewById(R.id.quantity);
            condition = itemView.findViewById(R.id.condition);
            gender = itemView.findViewById(R.id.gender);
            size = itemView.findViewById(R.id.size);

            assign = itemView.findViewById(R.id.assign);
            inform = itemView.findViewById(R.id.inform);
        }
    }
}