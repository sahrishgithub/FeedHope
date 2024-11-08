package com.example.unitconverter.ProviderInterface;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.unitconverter.ProviderInterface.DonationDB;
import com.example.unitconverter.R;

public class DonationCloth extends AppCompatActivity {
    private static final String PREFS_NAME = "LocationPrefs";
    private static final String KEY_LATITUDE = "currentLatitude";
    private static final String KEY_LONGITUDE = "currentLongitude";
    private static final String KEY_LOCATION_NAME = "currentLocationName";

    private TextView locationTextView;
    private String locationName;
    private double latitude, longitude;

    private Spinner typeSpinner, conditionSpinner, categorySpinner, sizeSpinner;
    private EditText quantityEditText;
    private RadioGroup seasonRadioGroup;
    private Button registerButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.donate_cloth);

        // Initialize UI components
        locationTextView = findViewById(R.id.locationTextView);
        typeSpinner = findViewById(R.id.type);
        conditionSpinner = findViewById(R.id.cloth_condition);
        quantityEditText = findViewById(R.id.quantity);
        categorySpinner = findViewById(R.id.cloth_category);
        seasonRadioGroup = findViewById(R.id.radio_btn);
        sizeSpinner = findViewById(R.id.cloth_size);
        registerButton = findViewById(R.id.register);

        // Set up the toolbar with a title and back button
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Retrieve current location
        retrieveCurrentLocation();

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateFields()) {
                    registerDonation();
                }
            }
        });
    }

    // Handle back button press in the toolbar
    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed(); // Navigate back when the back button is pressed
            return true;
        }
        return super.onOptionsItemSelected(item);
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

    private boolean validateFields() {
        if (typeSpinner.getSelectedItemPosition() == 0) {
            Toast.makeText(this, "Please select a type.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (conditionSpinner.getSelectedItemPosition() == 0) {
            Toast.makeText(this, "Please select a condition.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (quantityEditText.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Please enter a quantity.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (categorySpinner.getSelectedItemPosition() == 0) {
            Toast.makeText(this, "Please select a category.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (sizeSpinner.getSelectedItemPosition() == 0) {
            Toast.makeText(this, "Please select a size.", Toast.LENGTH_SHORT).show();
            return false;
        }

        int selectedSeasonId = seasonRadioGroup.getCheckedRadioButtonId();
        if (selectedSeasonId == -1) {
            Toast.makeText(this, "Please select a season.", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void registerDonation() {
        // Get data from UI elements
        String type = typeSpinner.getSelectedItem().toString();
        String condition = conditionSpinner.getSelectedItem().toString();
        String quantity = quantityEditText.getText().toString();
        String category = categorySpinner.getSelectedItem().toString();
        String size = sizeSpinner.getSelectedItem().toString();

        int selectedSeasonId = seasonRadioGroup.getCheckedRadioButtonId();
        RadioButton selectedSeasonButton = findViewById(selectedSeasonId);
        String season = selectedSeasonButton != null ? selectedSeasonButton.getText().toString() : "";

        Log.d("DonationCloth", "Collected donation data: " +
                "Type: " + type + ", " +
                "Condition: " + condition + ", " +
                "Quantity: " + quantity + ", " +
                "Category: " + category + ", " +
                "Season: " + season + ", " +
                "Size: " + size);

        // Save data using DonationDB class
        DonationDB db = new DonationDB(this);
        boolean isRegistered = db.insert(type, condition, quantity, category, season, size, latitude, longitude, locationName);

        if (isRegistered) {
            Toast.makeText(this, "Cloth Donation Registered Successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Failed to register cloth donation", Toast.LENGTH_SHORT).show();
        }
    }
}
