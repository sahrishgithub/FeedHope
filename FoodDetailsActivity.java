package com.example.unitconverter.ProviderInterface;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.unitconverter.R;

import java.util.ArrayList;

public class FoodDetailsActivity extends AppCompatActivity {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private ArrayList<FoodProvideModalClass> modalClasses;
    private ProviderDB dbHandler;
    private FoodRVAdapter rvAdapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rv_food);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Food Details");
        }

        // Initialize database handler and RecyclerView
        dbHandler = new ProviderDB(FoodDetailsActivity.this);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

        // Load and display data from the database
        loadDataAndDisplay();
    }

    private void loadDataAndDisplay() {
        // Fetch data from ProviderDB
        modalClasses = dbHandler.readFoodData();

        if (modalClasses == null || modalClasses.isEmpty()) {
            Log.e("FoodDetailsActivity", "No data found in ProviderDB.");
            return;
        }

        Log.d("FoodDetailsActivity", "Data loaded from ProviderDB, total items: " + modalClasses.size());

        // Set up the RecyclerView adapter and attach it
        rvAdapter = new FoodRVAdapter(modalClasses, this);
        recyclerView.setAdapter(rvAdapter);
        rvAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
