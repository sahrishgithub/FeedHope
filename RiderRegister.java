package com.example.unitconverter.RiderInterface;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.unitconverter.R;

public class RiderRegister extends AppCompatActivity {

    private EditText name, IDNumber, banking, phone, email, pass;
    private Spinner IDtype, hours, days;
    private Button register;
    private TextView login, locationTextView;
    private static final String PREFS_NAME = "LocationPrefs";
    private static final String KEY_LATITUDE = "currentLatitude";
    private static final String KEY_LONGITUDE = "currentLongitude";
    private static final String KEY_LOCATION_NAME = "currentLocationName";
    private String locationName;
    private double latitude, longitude;

    private RiderRegisterDB riderRegisterDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_rider);

        // Initializing views
        name = findViewById(R.id.name);
        IDNumber = findViewById(R.id.number);
        banking = findViewById(R.id.card);
        phone = findViewById(R.id.phone);
        email = findViewById(R.id.email);
        pass = findViewById(R.id.pass);
        register = findViewById(R.id.register);
        login = findViewById(R.id.login);
        locationTextView = findViewById(R.id.currentlocation);

        IDtype = findViewById(R.id.type);
        hours = findViewById(R.id.hours);
        days = findViewById(R.id.days);

        // Setting up toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        // Enable the back button in the toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Initialize RiderRegisterDB to interact with the SQLite database
        riderRegisterDB = new RiderRegisterDB(this);

        // Retrieve current location using the new method
        retrieveCurrentLocation();

        // Register button click listener
        register.setOnClickListener(v -> {
            String name1 = name.getText().toString().trim();
            String phone1 = phone.getText().toString().trim();
            String selectedType = IDtype.getSelectedItem().toString();
            String IDNumber1 = IDNumber.getText().toString().trim();
            String selectedHours = hours.getSelectedItem().toString();
            String selectedDays = days.getSelectedItem().toString();
            String banking1 = banking.getText().toString().trim();
            String email1 = email.getText().toString().trim();
            String pass1 = pass.getText().toString().trim();

            if (name1.isEmpty() || phone1.isEmpty() || IDNumber1.isEmpty() || banking1.isEmpty() || email1.isEmpty() || pass1.isEmpty()) {
                Toast.makeText(RiderRegister.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (IDNumber1.length() != 13) {
                Toast.makeText(RiderRegister.this, "ID number must be 13 digits long", Toast.LENGTH_SHORT).show();
                return;
            }

            // Retrieve location data
            SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            locationName = sharedPreferences.getString(KEY_LOCATION_NAME, "");
            latitude = sharedPreferences.getFloat(KEY_LATITUDE, Float.NaN);
            longitude = sharedPreferences.getFloat(KEY_LONGITUDE, Float.NaN);

            if (!Double.isNaN(latitude) && !Double.isNaN(longitude)) {
                boolean isInserted = riderRegisterDB.insertData(name1, phone1, selectedType, IDNumber1, selectedHours, selectedDays, banking1, email1, pass1, latitude, longitude, locationName);

                if (isInserted) {
                    Toast.makeText(RiderRegister.this, "Registration successful", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RiderRegister.this, RiderLogin.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(RiderRegister.this, "Registration failed", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(RiderRegister.this, "Location not available. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });

        login.setOnClickListener(v -> {
            Intent intent = new Intent(RiderRegister.this, RiderLogin.class);
            startActivity(intent);
        });
    }

    private void retrieveCurrentLocation() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        locationName = sharedPreferences.getString(KEY_LOCATION_NAME, "");
        latitude = sharedPreferences.getFloat(KEY_LATITUDE, Float.NaN);
        longitude = sharedPreferences.getFloat(KEY_LONGITUDE, Float.NaN);

        if (!locationName.isEmpty() && !Double.isNaN(latitude) && !Double.isNaN(longitude)) {
            locationTextView.setText("Your current location is: " + locationName);
        } else {
            locationTextView.setText("Location not provided.");
            Toast.makeText(this, "Invalid location data received. Please ensure location access is enabled.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle back button press
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
