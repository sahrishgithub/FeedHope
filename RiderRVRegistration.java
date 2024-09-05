package com.example.unitconverter.AdminInterface;

import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.unitconverter.R;
import com.example.unitconverter.RiderInterface.RiderModalClass;
import com.example.unitconverter.RiderInterface.RiderRVAdapter;

import java.util.ArrayList;

public class RiderRVRegistration extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RiderRVAdapter riderAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rv_rider_registration);
        recyclerView = findViewById(R.id.recyclerView);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");
        }

        // Retrieve the user list from the intent
        ArrayList<RiderModalClass> riderList = getIntent().getParcelableArrayListExtra("riderList");

        if (riderList == null) {
            riderList = new ArrayList<>();
        }

        // Get the NotificationManager service
        NotificationManager notifManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Pass the activity context (this) and NotificationManager to the adapter
        riderAdapter = new RiderRVAdapter(riderList, this, notifManager);

        // Set up the RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(riderAdapter);
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