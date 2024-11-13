package com.example.feedhope.RiderInterface.Register;

import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.feedhope.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class RiderRegisterDetailActivity extends AppCompatActivity {
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

        // Retrieve the JSON string from the intent
        String riderJson = getIntent().getStringExtra("riderList");
        ArrayList<RiderModalClass> riderList;
        if (riderJson != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<RiderModalClass>>() {}.getType();
            riderList = gson.fromJson(riderJson, type);
        }else{
            riderList = new ArrayList<>();
        }
        Log.d("RiderRVRegistration", "Rider list size: " + riderList.size());

        NotificationManager notifManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        riderAdapter = new RiderRVAdapter(riderList, this, notifManager);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(riderAdapter);
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