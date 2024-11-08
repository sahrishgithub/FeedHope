package com.example.unitconverter.ProviderInterface;

import static com.example.unitconverter.AppInterface.GoogleMapActivity.KEY_LATITUDE;
import static com.example.unitconverter.AppInterface.GoogleMapActivity.KEY_LOCATION_NAME;
import static com.example.unitconverter.AppInterface.GoogleMapActivity.KEY_LONGITUDE;
import static com.example.unitconverter.AppInterface.GoogleMapActivity.PREFS_NAME;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.unitconverter.R;

public class DonationShoe extends AppCompatActivity {

    private Spinner typeSpinner, conditionSpinner, sizeSpinner;
    private EditText quantityEditText;
    private RadioGroup genderRadioGroup;
    private Button registerButton;
    private TextView locationTextView;

    private double currentLatitude;
    private double currentLongitude;
    private String locationName;

    private ShoeDB shoeDB;  // Declare the ShoeDB object

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.donate_shoe);

        // Initialize views
        typeSpinner = findViewById(R.id.type);
        conditionSpinner = findViewById(R.id.condition);
        sizeSpinner = findViewById(R.id.size);  // Added size spinner
        quantityEditText = findViewById(R.id.quantity);
        genderRadioGroup = findViewById(R.id.radio_btn);
        registerButton = findViewById(R.id.register);
        locationTextView = findViewById(R.id.location);

        // Initialize the ShoeDB instance
        shoeDB = new ShoeDB(this);

        // Get location data from Intent or SharedPreferences
        retrieveCurrentLocation();

        // Register button click listener
        registerButton.setOnClickListener(v -> registerDonation());
    }

    // Method to retrieve the current location from SharedPreferences
    private void retrieveCurrentLocation() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        locationName = sharedPreferences.getString(KEY_LOCATION_NAME, "");
        currentLatitude = sharedPreferences.getFloat(KEY_LATITUDE, Float.NaN);
        currentLongitude = sharedPreferences.getFloat(KEY_LONGITUDE, Float.NaN);

        // Check if valid location data is available
        if (!locationName.isEmpty() && !Double.isNaN(currentLatitude) && !Double.isNaN(currentLongitude)) {
            locationTextView.setText("Your current location is: " + locationName);
        } else {
            locationTextView.setText("Location not provided.");
            Toast.makeText(this, "Invalid location data received. Please ensure location access is enabled.", Toast.LENGTH_SHORT).show();
        }
    }

    private void registerDonation() {
        // Retrieve input values from the form
        String type = typeSpinner.getSelectedItem().toString();
        String condition = conditionSpinner.getSelectedItem().toString();
        String quantity = quantityEditText.getText().toString().trim();
        String size = sizeSpinner.getSelectedItem().toString();  // Get the selected size

        // Retrieve selected gender from the RadioGroup
        int selectedGenderId = genderRadioGroup.getCheckedRadioButtonId();
        RadioButton genderRadioButton = findViewById(selectedGenderId);
        String gender = genderRadioButton != null ? genderRadioButton.getText().toString().trim() : "";

        // Validate that all required fields are filled in
        if (quantity.isEmpty() || gender.isEmpty() || size.isEmpty()) {  // Check if size is selected
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Store data in the database using ShoeDB class
        long result = shoeDB.addDonation(
                type,
                condition,
                size,
                Integer.parseInt(quantity),
                gender,
                currentLatitude,
                currentLongitude,
                locationName
        );

        if (result == -1) {
            Toast.makeText(this, "Failed to register donation", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Donation registered successfully!", Toast.LENGTH_SHORT).show();
        }

        // Clear the input fields after submission
        clearInputs();
    }

    // Method to clear all input fields
    private void clearInputs() {
        typeSpinner.setSelection(0);
        conditionSpinner.setSelection(0);
        sizeSpinner.setSelection(0);  // Reset size spinner
        quantityEditText.setText("");
        genderRadioGroup.clearCheck();
    }
}
