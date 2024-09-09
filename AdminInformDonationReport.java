package com.example.unitconverter.AdminInterface;

import android.os.Bundle;
import android.view.MenuItem;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.unitconverter.R;
import com.example.unitconverter.ReceiverInterface.InformDonationModalClass;
import com.example.unitconverter.ReceiverInterface.ReceiverRegisterDB;

import java.util.ArrayList;

public class AdminInformDonationReport extends AppCompatActivity {
    private ArrayList<InformDonationModalClass> modalClasses;
    private ReceiverRegisterDB dbHandler;
    private AdminInformRVAdapter rvAdapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rv_admin_information_report);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");
        }

        modalClasses = new ArrayList<>();
        dbHandler = new ReceiverRegisterDB(AdminInformDonationReport.this);
        modalClasses = dbHandler.readInformationData();
        rvAdapter = new AdminInformRVAdapter(modalClasses, AdminInformDonationReport.this);
        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(AdminInformDonationReport.this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(rvAdapter);
    }

    protected void onResume() {
        super.onResume();
        // Refresh the list
        modalClasses.clear();
        modalClasses.addAll(dbHandler.readInformationData());
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
