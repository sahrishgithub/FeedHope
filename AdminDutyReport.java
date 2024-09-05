package com.example.unitconverter.AdminInterface;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.unitconverter.R;
import com.example.unitconverter.RiderInterface.DutyModalClass;
import com.example.unitconverter.RiderInterface.DutyRVAdapter;
import com.example.unitconverter.RiderInterface.RiderAssignDutyDB;

import java.util.ArrayList;

public class AdminDutyReport extends AppCompatActivity {

    private ArrayList<DutyModalClass> modalClasses;
    private RiderAssignDutyDB dbHandler;
    private AdminDutyRVAdapter rvAdapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rv_admin_duties_report);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");
        }

        modalClasses = new ArrayList<>();
        dbHandler = new RiderAssignDutyDB(AdminDutyReport.this);
        modalClasses = dbHandler.readDutyData();
        rvAdapter = new AdminDutyRVAdapter(modalClasses, AdminDutyReport.this);
        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(AdminDutyReport.this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(rvAdapter);
    }

    protected void onResume() {
        super.onResume();
        // Refresh the list
        modalClasses.clear();
        modalClasses.addAll(dbHandler.readDutyData());
        rvAdapter.notifyDataSetChanged();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // Handle the back button click here
                onBackPressed(); // Go back to the previous activity
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}