package com.example.unitconverter.AdminInterface;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.unitconverter.ProviderInterface.ProviderModalClass;
import com.example.unitconverter.R;
import com.example.unitconverter.ReceiverInterface.ReceiverModalClass;
import com.example.unitconverter.RiderInterface.RiderModalClass;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class LoadRegistrationData extends AppCompatActivity {
    Button provider, rider, receiver;
    private ArrayList<ProviderModalClass> providerModalClasses;
    private ArrayList<RiderModalClass> riderModalClasses;
    private ArrayList<ReceiverModalClass> receiverModalClasses;
    private SharedPreferences sharedPreferences;
    private Gson gson;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_load_registration_data);

        provider = findViewById(R.id.provider);
        rider = findViewById(R.id.rider);
        receiver = findViewById(R.id.receiver);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //for back option
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");
        }

        gson = new Gson();

        provider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPreferences = getSharedPreferences("providerPrefs", Context.MODE_PRIVATE);
                loadProviderData();
                if (!providerModalClasses.isEmpty()) {
                    Intent intent = new Intent(LoadRegistrationData.this, ProviderRegisterDetail.class);
                    String providerListJson = gson.toJson(providerModalClasses);
                    intent.putExtra("providerList", providerListJson);
                    startActivity(intent);
                } else {
                    Toast.makeText(LoadRegistrationData.this, "No data to display", Toast.LENGTH_SHORT).show();
                }
            }
        });

        rider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPreferences = getSharedPreferences("riderPrefs", Context.MODE_PRIVATE);
                loadRiderData();
                if (!riderModalClasses.isEmpty()) {
                    Intent intent = new Intent(LoadRegistrationData.this, RiderRegisterDetail.class);
                    String riderListJson = gson.toJson(riderModalClasses);
                    intent.putExtra("riderList", riderListJson);
                    startActivity(intent);
                } else {
                    Toast.makeText(LoadRegistrationData.this, "No data to display", Toast.LENGTH_SHORT).show();
                }
            }
        });

        receiver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPreferences = getSharedPreferences("receiverPrefs", Context.MODE_PRIVATE);
                loadReceiverData();
                if (!receiverModalClasses.isEmpty()) {
                    Intent intent = new Intent(LoadRegistrationData.this, ReceiverRegisterDetail.class);
                    String receiverListJson = gson.toJson(receiverModalClasses);
                    intent.putExtra("receiverList", receiverListJson);
                    startActivity(intent);
                } else {
                    Toast.makeText(LoadRegistrationData.this, "No data to display", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void loadProviderData() {
        String json = sharedPreferences.getString("providerList", null);
        Type type = new TypeToken<ArrayList<ProviderModalClass>>() {}.getType();
        providerModalClasses = gson.fromJson(json, type);

        if (providerModalClasses == null) {
            providerModalClasses = new ArrayList<>();
        }
        Log.d("LoadRegistrationData", "Provider list size: " + providerModalClasses.size());
    }

    private void loadRiderData() {
        String json = sharedPreferences.getString("riderList", null);
        Type type = new TypeToken<ArrayList<RiderModalClass>>() {}.getType();
        riderModalClasses = gson.fromJson(json, type);

        if (riderModalClasses == null) {
            riderModalClasses = new ArrayList<>();
        }
        Log.d("LoadRegistrationData", "Rider list size: " + riderModalClasses.size());
    }

    private void loadReceiverData() {
        String json = sharedPreferences.getString("receiverList", null);
        Type type = new TypeToken<ArrayList<ReceiverModalClass>>() {}.getType();
        receiverModalClasses = gson.fromJson(json, type);

        if (receiverModalClasses == null) {
            receiverModalClasses = new ArrayList<>();
        }
        Log.d("LoadRegistrationData", "Receiver list size: " + receiverModalClasses.size());
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