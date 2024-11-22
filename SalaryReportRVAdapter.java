package com.example.feedhope.RiderInterface.SalaryReport;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.feedhope.R;
import java.util.ArrayList;

public class SalaryReportRVAdapter extends RecyclerView.Adapter<SalaryReportRVAdapter.ViewHolder> {
    ArrayList<SalaryModelClass> paymentReports;
    Context context;

    public SalaryReportRVAdapter(ArrayList<SalaryModelClass> paymentReports, Context context) {
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
        holder.user.setText(report.getEmail());
        holder.amount.setText(String.valueOf(report.getSalary()));
        holder.date.setText((report.getPaymentDate()));
    }

    @Override
    public int getItemCount() {
        return paymentReports.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView user,amount,date;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            user = itemView.findViewById(R.id.email);
            amount = itemView.findViewById(R.id.amount);
            date = itemView.findViewById(R.id.date);
        }
    }
}