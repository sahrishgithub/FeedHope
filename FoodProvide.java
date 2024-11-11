package com.example.feedhope.ProviderInterface.FoodDonation;

import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.example.feedhope.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class FoodProvide extends AppCompatActivity {
    private static final String PREFS_NAME = "LocationPrefs";
    private static final String KEY_LATITUDE = "currentLatitude";
    private static final String KEY_LONGITUDE = "currentLongitude";
    private static final String KEY_LOCATION_NAME = "currentLocationName";

    private TextView locationTextView;
    private Button registerButton;
    private String locationName;
    private double latitude, longitude;
    private EditText quantity, available, expire;
    private Spinner food, storage;
    private Button register;
    private FoodDB db;
    private String loggedInEmail;
    private NotificationManager notificationManager;
    private FoodDB foodDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.donate_food);

        locationTextView = findViewById(R.id.location);
        food = findViewById(R.id.food);
        quantity = findViewById(R.id.quantity);
        storage = findViewById(R.id.storage);
        available = findViewById(R.id.available);
        expire = findViewById(R.id.expire);
        register = findViewById(R.id.register);

        loggedInEmail = getIntent().getStringExtra("email");

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
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");
        }


        quantity.addTextChangedListener(new TextWatcher() {
            private boolean isUpdating;
            private android.os.Handler handler = new android.os.Handler();
            private Runnable runnable;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // No need to implement anything here
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // No need to implement anything here
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!isUpdating) {
                    String text = editable.toString();

                    // Remove the delayed runnable if it's already scheduled
                    if (runnable != null) {
                        handler.removeCallbacks(runnable);
                    }

                    // Schedule a new runnable to append " kg" after a delay
                    runnable = new Runnable() {
                        @Override
                        public void run() {
                            if (!text.isEmpty() && !text.endsWith(" kg")) {
                                try {
                                    // Parse the input as a double to ensure it's a valid number
                                    double value = Double.parseDouble(text);

                                    // If parsing succeeds, append the unit
                                    isUpdating = true;
                                    quantity.setText(String.format("%s kg", value));
                                    quantity.setSelection(quantity.getText().length());  // Move cursor to the end
                                } catch (NumberFormatException e) {
                                    // If parsing fails, it may mean the user is typing or correcting a decimal
                                    // No unit is appended here
                                } finally {
                                    isUpdating = false;
                                }
                            }
                        }
                    };

                    // Delay appending the unit by 500ms to wait for user to finish typing
                    handler.postDelayed(runnable, 500);
                }
            }
        });
        available.setOnClickListener(v -> {
            // Get the current date
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            // Create DatePickerDialog and set the selected date in EditText
            DatePickerDialog datePickerDialog = new DatePickerDialog(FoodProvide.this,
                    (view, year1, monthOfYear, dayOfMonth) -> {
                        String selectedDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year1;
                        available.setText(selectedDate);
                    }, year, month, day);

            // Restrict the date selection to the current date and future dates
            datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
            datePickerDialog.show();
        });

        expire.setOnClickListener(v -> {
            if (available.getText().toString().isEmpty()) {
                // Prompt the user to select the availability date first
                Toast.makeText(FoodProvide.this, "Please select the availability date first", Toast.LENGTH_SHORT).show();
                return;
            }

            // Parse the availability date
            String availabilityDateStr = available.getText().toString();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            Calendar availabilityCalendar = Calendar.getInstance();

            try {
                Date availabilityDate = sdf.parse(availabilityDateStr);
                if (availabilityDate != null) {
                    availabilityCalendar.setTime(availabilityDate);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

            // Initialize current date for DatePickerDialog
            int year = availabilityCalendar.get(Calendar.YEAR);
            int month = availabilityCalendar.get(Calendar.MONTH);
            int day = availabilityCalendar.get(Calendar.DAY_OF_MONTH);

            // Create DatePickerDialog and set the selected date in EditText
            DatePickerDialog datePickerDialog = new DatePickerDialog(FoodProvide.this,
                    (view, year1, monthOfYear, dayOfMonth) -> {
                        String selectedDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year1;
                        expire.setText(selectedDate);
                    }, year, month, day);

            // Set minDate to the availability date
            datePickerDialog.getDatePicker().setMinDate(availabilityCalendar.getTimeInMillis());
            datePickerDialog.show();
        });


        register.setOnClickListener(v -> {
            db = new FoodDB(FoodProvide.this);
            foodDB = new FoodDB(FoodProvide.this); // Initialize GoogleMapDB

            db.getWritableDatabase();
            boolean isValid = true;
            StringBuilder errorMessages = new StringBuilder();

            String selectedFood = food.getSelectedItem().toString();
            String quantity1 = quantity.getText().toString().trim();
            String selectedStorage = storage.getSelectedItem().toString();
            String available1 = available.getText().toString().trim();
            String expire1 = expire.getText().toString().trim();

            quantity.setError(null);
            available.setError(null);
            expire.setError(null);

            if (selectedFood.equals("Select Food Type")) {
                errorMessages.append("Please select a valid Food Type.\n");
                isValid = false;
            }
            if (quantity1.isEmpty()) {
                quantity.setError("Quantity field is required");
                isValid = false;
            }
            if (selectedStorage.equals("Select Storage Requirement")) {
                errorMessages.append("Please select a valid Storage Requirement.\n");
                isValid = false;
            }
            if (available1.isEmpty()) {
                available.setError("Available Date field is required");
                isValid = false;
            }
            if (expire1.isEmpty()) {
                expire.setError("Expiry Date field is required");
                isValid = false;
            }

            if (isValid) {
                // Check if the location already exists in the database
                if (!foodDB.locationExists(latitude, longitude)) {
                    // Insert the location along with the food donation form data into GoogleMapDB
                    boolean locationInserted = foodDB.insertLocationWithFormData(locationName, latitude, longitude, loggedInEmail, selectedFood, quantity1, selectedStorage, available1, expire1);

                    if (locationInserted) {
                        Toast.makeText(FoodProvide.this, "Location and Data Inserted", Toast.LENGTH_LONG).show();
                        sendNotification("Food Donation Detail", "The food details and location have been added successfully.");
                        finish();
                    } else {
                        Toast.makeText(FoodProvide.this, "Location and Data not Inserted", Toast.LENGTH_LONG).show();
                    }
                } else {
                    // If location already exists, just insert the food donation data in the food DB
                    if (db.insertData(loggedInEmail, selectedFood, quantity1, selectedStorage, available1, expire1)) {
                        Toast.makeText(FoodProvide.this, "Data Inserted", Toast.LENGTH_LONG).show();
                        sendNotification("Food Donation Detail", "The food details have been added successfully.");
                        finish();
                    } else {
                        Toast.makeText(FoodProvide.this, "Data not Inserted", Toast.LENGTH_LONG).show();
                    }
                }
            } else {
                Toast.makeText(FoodProvide.this, errorMessages.toString(), Toast.LENGTH_LONG).show();
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

    private void sendNotification(String title, String message) {
        String channelId = "provide_food_notifications";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "Provide Food Notifications",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
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

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
