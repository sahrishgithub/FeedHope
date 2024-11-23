package com.example.feedhope.RiderInterface.SalaryReport;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.feedhope.AdminInterface.RiderPaySalary;
import com.example.feedhope.R;
import com.example.feedhope.RiderInterface.Duty.DutyDB;

public class SalaryCount extends AppCompatActivity {

    private TextView name, completed, pending, salary;
    private ImageView back;
    private Button pay;
    private static final int SALARY_PER_COMPLETED_TASK = 1000;
    private static final int PAYMENT_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.salary_count);

        name = findViewById(R.id.name);
        completed = findViewById(R.id.completedCountText);
        pending = findViewById(R.id.pendingCountText);
        salary = findViewById(R.id.salary);
        back = findViewById(R.id.back_arrow);
        pay = findViewById(R.id.pay);

        back.setOnClickListener(v -> onBackPressed());

        // Retrieve intent data
        String userName = getIntent().getStringExtra("userName");
        int completedCount = getIntent().getIntExtra("completedCount", 0);
        int pendingCount = getIntent().getIntExtra("pendingCount", 0);

        // Set values to TextViews
        name.setText(userName);
        completed.setText(String.valueOf(completedCount));
        pending.setText(String.valueOf(pendingCount));

        // Calculate and display salary
        int calculatedSalary = completedCount * SALARY_PER_COMPLETED_TASK;
        salary.setText(calculatedSalary + " rupees");

        // Check salary and set visibility of pay button
        if (calculatedSalary == 0) {
            pay.setVisibility(View.GONE); // Hide button if salary is zero
        } else {
            pay.setVisibility(View.VISIBLE); // Show button if salary is not zero
        }

        // Pay button action
        pay.setOnClickListener(v -> {
            Intent intent = new Intent(SalaryCount.this, RiderPaySalary.class);
            intent.putExtra("userName", userName);
            intent.putExtra("calculatedSalary", calculatedSalary);
            startActivityForResult(intent, PAYMENT_REQUEST_CODE);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PAYMENT_REQUEST_CODE && resultCode == RESULT_OK) {
            boolean paymentSuccess = data != null && data.getBooleanExtra("paymentSuccess", false);
            if (paymentSuccess) {
                Toast.makeText(this, "Duties paid successfully!", Toast.LENGTH_SHORT).show();

                // Update UI after successful payment
                String userName = getIntent().getStringExtra("userName");
                DutyDB dbHandler = new DutyDB(this);
                dbHandler.markDutiesAsPaid(userName);

                completed.setText("0");
                salary.setText("0 rupees");

                // Hide the pay button again after successful payment
                pay.setVisibility(View.GONE);
            } else {
                Toast.makeText(this, "Payment failed. Please try again.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}