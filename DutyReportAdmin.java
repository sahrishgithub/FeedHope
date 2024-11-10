package com.example.feedhope.AdminInterface;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.feedhope.R;
import com.example.feedhope.RiderInterface.Duty.DutyDB;
import com.example.feedhope.RiderInterface.Duty.DutyModalClass;

import java.util.ArrayList;

public class DutyReportAdmin extends AppCompatActivity {

    private ArrayList<DutyModalClass> modalClasses;
    private DutyDB dbHandler;
    private DutyRVAdapterAdmin rvAdapter;
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
        dbHandler = new DutyDB(DutyReportAdmin.this);
        modalClasses = dbHandler.readDutyData();
        rvAdapter = new DutyRVAdapterAdmin(modalClasses, DutyReportAdmin.this);
        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(DutyReportAdmin.this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(rvAdapter);
    }

    protected void onResume() {
        super.onResume();
        modalClasses.clear();
        modalClasses.addAll(dbHandler.readDutyData());
        for (DutyModalClass duty : modalClasses) {
            String userEmail = duty.getName();
            duty.setPendingCount(dbHandler.getPendingCount(userEmail));
            duty.setCompletedCount(dbHandler.getCompletedCount(userEmail));
        }
        rvAdapter.notifyDataSetChanged();
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