package com.example.unitconverter.RiderInterface;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.unitconverter.R;

public class RiderRegister extends AppCompatActivity {

    private EditText nameEditText, phoneEditText, idNumberEditText, cardEditText, emailEditText, passwordEditText;
    private Spinner idTypeSpinner, workingHoursSpinner, workingDaysSpinner;
    private static final String PREFS_NAME = "LocationPrefs";
    private static final String KEY_LATITUDE = "currentLatitude";
    private static final String KEY_LONGITUDE = "currentLongitude";
    private static final String KEY_LOCATION_NAME = "currentLocationName";

    private TextView locationTextView, loginTextView;
    private String locationName;
    private double latitude, longitude;
    private Button registerButton;
    private RiderRegisterDB dbHelper;  // Add the database helper class

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_rider);

        // Initialize the UI components
        nameEditText = findViewById(R.id.name);
        phoneEditText = findViewById(R.id.phone);
        idTypeSpinner = findViewById(R.id.type);
        idNumberEditText = findViewById(R.id.number);
        workingHoursSpinner = findViewById(R.id.hours);
        workingDaysSpinner = findViewById(R.id.days);
        cardEditText = findViewById(R.id.card);
        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.pass);
        locationTextView = findViewById(R.id.location);
        registerButton = findViewById(R.id.register);
        loginTextView = findViewById(R.id.login);

        // Initialize database helper
        dbHelper = new RiderRegisterDB(this);

        // Set a click listener for the register button
        registerButton.setOnClickListener(v -> registerUser());

        // Retrieve the location data from SharedPreferences
        retrieveCurrentLocation();

        // Set a click listener for the login TextView
        loginTextView.setOnClickListener(v -> {
            Intent intent = new Intent(RiderRegister.this, RiderLogin.class);
            startActivity(intent);
        });
    }

    private void registerUser() {
        String name = nameEditText.getText().toString().trim();
        String phone = phoneEditText.getText().toString().trim();
        String idType = idTypeSpinner.getSelectedItem().toString();
        String idNumber = idNumberEditText.getText().toString().trim();
        String workingHours = workingHoursSpinner.getSelectedItem().toString();
        String workingDays = workingDaysSpinner.getSelectedItem().toString();
        String card = cardEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        // Validate the user input
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(phone) || TextUtils.isEmpty(idNumber)
                || TextUtils.isEmpty(card) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length() < 8) {
            Toast.makeText(this, "Password must be at least 8 characters", Toast.LENGTH_SHORT).show();
            return;
        }

        // Add the registration data into the database
        long result = dbHelper.insertRiderData(name, phone, idType, idNumber, workingHours, workingDays, card, email, password, latitude, longitude, locationName);

        if (result > 0) {
            Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show();
            // Navigate to another activity if needed
            // Intent intent = new Intent(RiderRegister.this, NextActivity.class);
            // startActivity(intent);
            // finish();
        } else {
            Toast.makeText(this, "Registration failed. Please try again.", Toast.LENGTH_SHORT).show();
        }
    }

    private void retrieveCurrentLocation() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        locationName = sharedPreferences.getString(KEY_LOCATION_NAME, ""); // Retrieve location name
        latitude = sharedPreferences.getFloat(KEY_LATITUDE, Float.NaN);
        longitude = sharedPreferences.getFloat(KEY_LONGITUDE, Float.NaN);

        if (!locationName.isEmpty() && !Double.isNaN(latitude) && !Double.isNaN(longitude)) {
            locationTextView.setText("Your current location is: " + locationName);
        } else {
            locationTextView.setText("Location not provided.");
            Toast.makeText(this, "Invalid location data received. Please ensure location access is enabled.", Toast.LENGTH_SHORT).show();
        }
    }
}
