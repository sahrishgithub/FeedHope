package com.example.feedhope.RiderInterface.SalaryReport;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.feedhope.RiderInterface.Duty.DutyDB;
import java.util.ArrayList;
import com.example.feedhope.R;

public class SalaryDetailActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ArrayList<SalaryModelClass> modalClasses;
    private SalaryReportAdapter rvAdapter;
    private DutyDB dbHandler;

    @SuppressLint("MissingInflatedId")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rv_salary_report);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");}

        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        String userEmail = sharedPreferences.getString("user_email", "");

        modalClasses = new ArrayList<>();
        dbHandler = new DutyDB(SalaryDetailActivity.this);
        modalClasses = dbHandler.getPaymentHistory(userEmail);
        rvAdapter = new SalaryReportAdapter(modalClasses, SalaryDetailActivity.this);
        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(SalaryDetailActivity.this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(rvAdapter);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}