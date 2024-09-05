package com.example.unitconverter.AdminInterface;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.unitconverter.ProviderInterface.FoodDetailsActivity;
import com.example.unitconverter.ProviderInterface.FoodRVAdapter;
import com.example.unitconverter.ProviderInterface.ProvideFoodModalClass;
import com.example.unitconverter.ProviderInterface.ProviderDB;
import com.example.unitconverter.R;
import com.example.unitconverter.RiderInterface.DutyDetailsActivity;

import java.util.ArrayList;

public class AdminHomePage extends AppCompatActivity {
    CardView register, provider, rider, receiver, money;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage_admin);

        // Initialize all views
        register = findViewById(R.id.register);
        provider = findViewById(R.id.provider);
        rider = findViewById(R.id.rider);
        receiver = findViewById(R.id.receiver);
        money = findViewById(R.id.money);

        // Setup the toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");
        }

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminHomePage.this, LoadRegistrationData.class);
                startActivity(intent);
            }
        });

        provider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminHomePage.this, FoodDetailsActivity.class);
                startActivity(intent);
            }
        });

        rider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminHomePage.this, AdminDutyReport.class);
                startActivity(intent);
            }
        });

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