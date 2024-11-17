package com.example.feedhope.RiderInterface.SalaryReport;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.feedhope.R;
import java.util.ArrayList;

public class SalaryReportAdapter extends RecyclerView.Adapter<SalaryReportAdapter.ViewHolder> {
    private ArrayList<SalaryModelClass> paymentReports;
    private Context context;

    public SalaryReportAdapter(ArrayList<SalaryModelClass> paymentReports, Context context) {
        this.paymentReports = paymentReports;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_salary_report, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SalaryModelClass report = paymentReports.get(position);
        holder.dutyID.setText("Duty ID: " + report.getDutyID());
        holder.user.setText("UserName " + report.getEmail());
        holder.amount.setText("Amount : "+ report.getSalary());
        holder.date.setText(("Date :" + report.getPaymentDate()));
    }

    @Override
    public int getItemCount() {
        return paymentReports.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView dutyID,user,amount,date;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dutyID = itemView.findViewById(R.id.dutyID);
            user = itemView.findViewById(R.id.email);
            amount = itemView.findViewById(R.id.amount);
            date = itemView.findViewById(R.id.date);
        }
    }
}