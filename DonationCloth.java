package com.example.unitconverter.ProviderInterface;

import static com.example.unitconverter.AppInterface.GoogleMapActivity.KEY_LATITUDE;
import static com.example.unitconverter.AppInterface.GoogleMapActivity.KEY_LOCATION_NAME;
import static com.example.unitconverter.AppInterface.GoogleMapActivity.KEY_LONGITUDE;
import static com.example.unitconverter.AppInterface.GoogleMapActivity.PREFS_NAME;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;

import com.example.unitconverter.ProviderInterface.DonationDB;
import com.example.unitconverter.R;

import java.io.IOException;
import java.util.List;

public class DonationCloth extends AppCompatActivity {

    private Spinner type, condition, category, size;
    private TextView quantity, currentLocationText;
    private String loggedInEmail;
    private TextView locationTextView;
    private String locationName;
    private double latitude, longitude;
    private DonationDB db;
    private TextView register;
    private RadioGroup radioGroup;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.donate_cloth);

        // Initialize UI elements
        type = findViewById(R.id.type);
        condition = findViewById(R.id.condition);
        quantity = findViewById(R.id.quantity);
        category = findViewById(R.id.category);
        radioGroup = findViewById(R.id.radio_btn);
        size = findViewById(R.id.size);
        register = findViewById(R.id.register);
        currentLocationText = findViewById(R.id.currentLocationText);
        locationTextView = findViewById(R.id.locationTextView);

        // Get email from intent
        loggedInEmail = getIntent().getStringExtra("email");

        // Retrieve current location from SharedPreferences
        retrieveCurrentLocation();

        // Set up the toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");
        }

        // Radio group selection listener
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton selectedRadioButton = findViewById(checkedId);
            String selectedText = selectedRadioButton.getText().toString();
            Toast.makeText(DonationCloth.this, "Selected: " + selectedText, Toast.LENGTH_SHORT).show();
        });

        // Register button click listener
        register.setOnClickListener(v -> {
            db = new DonationDB(DonationCloth.this);
            db.getWritableDatabase();

            String quanity1, seasonal;
            boolean isValid = true;
            StringBuilder errorMessages = new StringBuilder();

            // Get selected values
            String selectedType = type.getSelectedItem().toString();
            String selectedCondition = condition.getSelectedItem().toString();
            quanity1 = quantity.getText().toString().trim();
            String selectedCategory = category.getSelectedItem().toString();
            int selectedRadioButtonId = radioGroup.getCheckedRadioButtonId();
            RadioButton selectedRadioButton = findViewById(selectedRadioButtonId);
            seasonal = (selectedRadioButton != null) ? selectedRadioButton.getText().toString().trim() : "";
            String selectedSize = size.getSelectedItem().toString();

            // Validation checks
            quantity.setError(null);
            if (selectedType.equals("Select Cloth Type")) {
                errorMessages.append("Please select a valid Cloth Type.\n");
                isValid = false;
            }
            if (selectedCondition.equals("Select Cloth Condition")) {
                errorMessages.append("Please select a valid Cloth Condition.\n");
                isValid = false;
            }
            if (quanity1.isEmpty()) {
                errorMessages.append("Quantity field is required.\n");
                quantity.setError("Quantity field is required");
                isValid = false;
            }
            if (selectedCategory.equals("Select Cloth Category")) {
                errorMessages.append("Please select a valid Cloth Category.\n");
                isValid = false;
            }
            if (seasonal.isEmpty()) {
                errorMessages.append("Please select a seasonal cloth.\n");
                isValid = false;
            }
            if (selectedSize.equals("Select Cloth Size")) {
                errorMessages.append("Please select a valid Cloth Size.\n");
                isValid = false;
            }

            // Insert data if valid
            if (isValid) {
                if (db.insert(loggedInEmail, selectedType, selectedCondition, quanity1, selectedCategory, seasonal, selectedSize, latitude, longitude)) {
                    Toast.makeText(DonationCloth.this, "Data Inserted", Toast.LENGTH_LONG).show();
                    sendNotification("Cloth Donation Detail", "The cloth details have been added successfully.");
                } else {
                    Toast.makeText(DonationCloth.this, "Data not Inserted", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(DonationCloth.this, errorMessages.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void retrieveCurrentLocation() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        locationName = sharedPreferences.getString(KEY_LOCATION_NAME, ""); // Retrieve location name
        latitude = sharedPreferences.getFloat(KEY_LATITUDE, 0.0f);  // Change to float if you're using getFloat
        longitude = sharedPreferences.getFloat(KEY_LONGITUDE, 0.0f);  // Change to float if you're using getFloat

        Log.d("DonationCloth", "Latitude from SharedPreferences: " + latitude + ", Longitude: " + longitude);

        if (!locationName.isEmpty() && latitude != 0.0f && longitude != 0.0f) {
            locationTextView.setText("Your current location is: " + locationName);
//            currentLocationText.setText("Current Location: " + locationName); // Display location in form
        } else {
            // Only display "Location not provided" if data is invalid
            locationTextView.setText("Location not provided.");
//            currentLocationText.setText("Current Location: Not available"); // Set message when location is not available
        }
    }


    private void sendNotification(String title, String message) {
        // Create a NotificationManager instance
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Check if we are running on Android 8.0 (API level 26) or higher for notification channels
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create a Notification Channel if it doesn't exist
            String channelId = "cloth_donations_channel";
            CharSequence channelName = "Cloth Donations";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel notificationChannel = new NotificationChannel(channelId, channelName, importance);

            // Register the channel with the system
            notificationManager.createNotificationChannel(notificationChannel);
        }

        // Create a Notification Builder
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "cloth_donations_channel")
                .setSmallIcon(R.drawable.ic_notification)  // Replace with your notification icon
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);  // Automatically dismiss the notification when clicked

        // Issue the notification
        notificationManager.notify(1, builder.build());
    }
}