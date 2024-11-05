package com.example.unitconverter.ProviderInterface;

import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;

import com.example.unitconverter.R;

import java.util.Calendar;

public class FoodProvide extends AppCompatActivity {

    private static final String PREFS_NAME = "LocationPrefs";  // Define the same preferences name
    private static final String KEY_LATITUDE = "currentLatitude";
    private static final String KEY_LONGITUDE = "currentLongitude";
    private static final String KEY_LOCATION_NAME = "currentLocationName";

    private TextView locationTextView;
    private Button registerButton;
    private String locationName;
    private double latitude, longitude;
    private EditText quantity, available, expire;
    private Spinner food, storage;
    private ProviderDB db;
    private String loggedInEmail;
    private NotificationManager notificationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.donate_food);

        // Initialize UI components
        locationTextView = findViewById(R.id.location);
        registerButton = findViewById(R.id.register);
        food = findViewById(R.id.food);
        quantity = findViewById(R.id.quantity);
        storage = findViewById(R.id.storage);
        available = findViewById(R.id.available);
        expire = findViewById(R.id.expire);

        db = new ProviderDB(this); // Initialize ProviderDB

        // Retrieve the location data from SharedPreferences
        retrieveCurrentLocation();

        // Display the location to the user
        if (locationName != null) {
            locationTextView.setText("Your current location is: " + locationName);
        } else {
            locationTextView.setText("Location not provided.");
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Display back button and clear default title
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");
        }

        // Setup date pickers for `available` and `expire`
        setupDatePicker(available);
        setupDatePicker(expire);

        // Set up the register button to save the form data
        registerButton.setOnClickListener(v -> {
            if (storeDataInDatabase(locationName, latitude, longitude)) {
                Toast.makeText(FoodProvide.this, "Location and form data saved successfully!", Toast.LENGTH_SHORT).show();
                sendNotification("Food Donation Detail", "The food details have been added successfully.");
                finish();
            } else {
                Toast.makeText(FoodProvide.this, "Failed to save data.", Toast.LENGTH_SHORT).show();
            }
        });
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

    private void setupDatePicker(EditText dateField) {
        dateField.setOnClickListener(v -> {
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            // Create DatePickerDialog and set the selected date in EditText
            DatePickerDialog datePickerDialog = new DatePickerDialog(FoodProvide.this,
                    (view, year1, monthOfYear, dayOfMonth) -> {
                        String selectedDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year1;
                        dateField.setText(selectedDate);
                    }, year, month, day);

            // Restrict the date selection to the current date and future dates
            datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());

            datePickerDialog.show();
        });
    }

    private boolean storeDataInDatabase(String locationName, double latitude, double longitude) {
        SQLiteDatabase dbInstance = db.getWritableDatabase();
        ContentValues values = new ContentValues();

        // Store location details
        values.put(ProviderDB.COLUMN_LOCATION_NAME, locationName);
        values.put(ProviderDB.COLUMN_LATITUDE, latitude);
        values.put(ProviderDB.COLUMN_LONGITUDE, longitude);

        // Store additional form fields
        values.put(ProviderDB.COLUMN_FOOD_TYPE, food.getSelectedItem().toString());
        values.put(ProviderDB.COLUMN_QUANTITY, quantity.getText().toString().trim());
        values.put(ProviderDB.COLUMN_STORAGE, storage.getSelectedItem().toString());
        values.put(ProviderDB.COLUMN_AVAILABLE_DATE, available.getText().toString().trim());
        values.put(ProviderDB.COLUMN_EXPIRE_DATE, expire.getText().toString().trim());
        values.put(ProviderDB.COLUMN_EMAIL, loggedInEmail);

        long result = dbInstance.insert(ProviderDB.TABLE_NAME, null, values);
        dbInstance.close();
        return result != -1; // Return true if insertion was successful
    }

    private void sendNotification(String title, String message) {
        String channelId = "provide_food_notifications";

        // Initialize Notification Manager
        if (notificationManager == null) {
            notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "Provide Food Notifications",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            notificationManager.createNotificationChannel(channel);
        }

        Notification notification = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.notification)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .build();
        notificationManager.notify(1, notification);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // Handle the back button click here
            onBackPressed(); // Go back to the previous activity
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
