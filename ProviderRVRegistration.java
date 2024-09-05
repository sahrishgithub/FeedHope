package com.example.unitconverter.AdminInterface;

import android.app.NotificationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.unitconverter.ProviderInterface.ProviderModalClass;
import com.example.unitconverter.ProviderInterface.ProviderRegisterRVAdapter;
import com.example.unitconverter.R;

import java.util.ArrayList;

public class ProviderRVRegistration extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ProviderRegisterRVAdapter providerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rv_provider_registration);
        recyclerView = findViewById(R.id.recyclerView);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");
        }
        // Retrieve the user list from the intent
        ArrayList<ProviderModalClass> providerList = getIntent().getParcelableArrayListExtra("providerList");

        if (providerList == null) {
            providerList = new ArrayList<>();
        }
        // Log provider list size
        Log.d("ProviderRVRegistration", "Provider list size: " + providerList.size());

        // Obtain the NotificationManager
        NotificationManager notifManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        providerAdapter = new ProviderRegisterRVAdapter(providerList, this, notifManager);
        // Set up the RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(providerAdapter);
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