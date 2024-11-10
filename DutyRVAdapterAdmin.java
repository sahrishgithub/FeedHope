package com.example.feedhope.AdminInterface;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.feedhope.R;
import com.example.feedhope.RiderInterface.Duty.DutyModalClass;

import java.util.ArrayList;

public class DutyRVAdapterAdmin extends RecyclerView.Adapter<DutyRVAdapterAdmin.ViewHolder> {

    private ArrayList<DutyModalClass> dutyModalClasses;
    private Context context;

    public DutyRVAdapterAdmin(ArrayList<DutyModalClass> modalClasses, Context context) {
        this.dutyModalClasses = modalClasses;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_admin_duty_report, parent, false);
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

        holder.pendingCount.setText("Pending Count: " + modal.getPendingCount());
        holder.completedCount.setText("Completed Count: " + modal.getCompletedCount());

        holder.status.setText(modal.getStatus());
        if ("Pending".equals(modal.getStatus())) {
            holder.status.setTextColor(Color.RED);
        } else if ("Completed".equals(modal.getStatus())) {
            holder.status.setTextColor(Color.GREEN);
        } else {
            holder.status.setTextColor(Color.BLACK);
        }
    }

    @Override
    public int getItemCount() {
        return dutyModalClasses.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView userName, pickName, dropName, dateName,status,pendingCount,completedCount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.name);
            pickName = itemView.findViewById(R.id.pick);
            dropName = itemView.findViewById(R.id.drop);
            dateName = itemView.findViewById(R.id.date);
            status = itemView.findViewById(R.id.status);

            pendingCount = itemView.findViewById(R.id.pendingCount);
            completedCount = itemView.findViewById(R.id.completedCount);
        }
    }
}