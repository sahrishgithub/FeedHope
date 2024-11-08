package com.example.unitconverter.ProviderInterface;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.unitconverter.R;

import java.util.Calendar;

public class DonationMedicine extends AppCompatActivity {

    private EditText nameEditText, quantityEditText, manufactureEditText, expireEditText;
    private Spinner formSpinner, conditionSpinner;
    private Button registerButton;
    private static final String PREFS_NAME = "LocationPrefs";
    private static final String KEY_LATITUDE = "currentLatitude";
    private static final String KEY_LONGITUDE = "currentLongitude";
    private static final String KEY_LOCATION_NAME = "currentLocationName";

    private TextView locationTextView;
    private String locationName;
    private double latitude, longitude;
    private Calendar calendar;
    private MedicinDB medicinDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.donate_medicine);

        // Initialize views
        nameEditText = findViewById(R.id.name);
        quantityEditText = findViewById(R.id.quantity);
        manufactureEditText = findViewById(R.id.manufacture);
        expireEditText = findViewById(R.id.expire);
        formSpinner = findViewById(R.id.form);
        conditionSpinner = findViewById(R.id.condition);
        registerButton = findViewById(R.id.register);
        locationTextView = findViewById(R.id.location);

        // Initialize database helper
        medicinDB = new MedicinDB(this);

        // Retrieve the location data from SharedPreferences
        retrieveCurrentLocation();

        if (locationName != null) {
            locationTextView.setText("Your current location is: " + locationName);
        } else {
            locationTextView.setText("Location not provided.");
        }

        // Initialize calendar for date pickers
        calendar = Calendar.getInstance();

        // Set date picker for manufacture date (restrict to future dates)
        manufactureEditText.setOnClickListener(v -> showDatePickerDialog(manufactureEditText));

        // Set date picker for expiry date (restrict to future dates)
        expireEditText.setOnClickListener(v -> showDatePickerDialog(expireEditText));

        // Register button click listener
        registerButton.setOnClickListener(v -> registerMedicine());
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

    private void showDatePickerDialog(final EditText dateEditText) {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, selectedYear, selectedMonth, selectedDay) -> {
            String date = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
            dateEditText.setText(date);
        }, year, month, day);

        // Restrict to future dates
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000); // -1000 to avoid exact milliseconds delay

        datePickerDialog.show();
    }

    private void registerMedicine() {
        String name = nameEditText.getText().toString().trim();
        String quantity = quantityEditText.getText().toString().trim();
        String manufactureDate = manufactureEditText.getText().toString().trim();
        String expiryDate = expireEditText.getText().toString().trim();
        String form = formSpinner.getSelectedItem().toString();
        String condition = conditionSpinner.getSelectedItem().toString();

        // Validate inputs
        if (name.isEmpty() || quantity.isEmpty() || manufactureDate.isEmpty() || expiryDate.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Save data to the database
        medicinDB.addMedicine(name, quantity, manufactureDate, expiryDate, form, condition, locationName, latitude, longitude);

        // Show success message
        Toast.makeText(this, "Medicine registered successfully!", Toast.LENGTH_SHORT).show();

        // Clear inputs
        clearInputs();
    }

    private void clearInputs() {
        nameEditText.setText("");
        quantityEditText.setText("");
        manufactureEditText.setText("");
        expireEditText.setText("");
        formSpinner.setSelection(0);
        conditionSpinner.setSelection(0);
    }
}
