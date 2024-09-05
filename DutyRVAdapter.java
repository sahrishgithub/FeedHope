package com.example.unitconverter.RiderInterface;

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

public class DutyRVAdapter extends RecyclerView.Adapter<DutyRVAdapter.ViewHolder> {

    private ArrayList<DutyModalClass> dutyModalClasses;
    private Context context;

    public DutyRVAdapter(ArrayList<DutyModalClass> modalClasses, Context context) {
        this.dutyModalClasses = modalClasses;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_assign_duty, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        final int itemPosition = position;
        DutyModalClass modal = dutyModalClasses.get(position);
        holder.userName.setText(modal.getName());
        holder.pickName.setText(modal.getPick());
        holder.dropName.setText(modal.getDrop());
        holder.dateName.setText(modal.getDate());
        holder.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,Update_Status.class);
                intent.putExtra("email", modal.getName());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dutyModalClasses.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView userName, pickName, dropName, dateName;
        Button update;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.name);
            pickName = itemView.findViewById(R.id.pick);
            dropName = itemView.findViewById(R.id.drop);
            dateName = itemView.findViewById(R.id.date);
            update = itemView.findViewById(R.id.update);
        }
    }
}