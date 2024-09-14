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
import com.example.unitconverter.RiderInterface.RiderModalClass;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class ProviderRegisterDetail extends AppCompatActivity {
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

        String providerJson = getIntent().getStringExtra("providerList");
        ArrayList<ProviderModalClass> providerList;
        if (providerJson != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<ProviderModalClass>>() {}.getType();
            providerList = gson.fromJson(providerJson, type);
        }else{
            providerList = new ArrayList<>();
        }
        Log.d("ProviderRVRegistration", "Provider list size: " + providerList.size());

        NotificationManager notifManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        providerAdapter = new ProviderRegisterRVAdapter(providerList, this, notifManager);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(providerAdapter);
    }

    @Override
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