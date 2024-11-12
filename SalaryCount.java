package com.example.feedhope.RiderInterface;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.feedhope.AdminInterface.RiderPaySalary;
import com.example.feedhope.R;

public class SalaryCount extends AppCompatActivity {

    private TextView name, completed, pending, salary;
    ImageView back;
    Button pay;
    private static final int SALARY_PER_COMPLETED_TASK = 1000;

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

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        String userName = getIntent().getStringExtra("userName");
        int completedCount = getIntent().getIntExtra("completedCount", 0);
        int pendingCount = getIntent().getIntExtra("pendingCount", 0);

        name.setText(userName);
        completed.setText(String.valueOf(completedCount));
        pending.setText(String.valueOf(pendingCount));

        int calculatedSalary = completedCount * SALARY_PER_COMPLETED_TASK;
        salary.setText(calculatedSalary + " rupees");

        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SalaryCount.this, RiderPaySalary.class);
                intent.putExtra("name", (CharSequence) name);
                intent.putExtra("salary", calculatedSalary);
                startActivity(intent);
            }
        });
    }
}
