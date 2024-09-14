package com.example.unitconverter.ProviderInterface;

import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;

import com.example.unitconverter.R;
import com.example.unitconverter.ReceiverInterface.ReceiverRegister;

import java.util.Calendar;

public class ProvideFood extends AppCompatActivity {
    private EditText quantity, available, expire;
    private Spinner food,storage;
    private Button register;
    private ProviderDB db;
    private NotificationManager notificationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.donate_food);

        // Initialize UI elements
        food = findViewById(R.id.food);
        quantity = findViewById(R.id.quantity);
        storage = findViewById(R.id.storage);
        available = findViewById(R.id.available);
        expire = findViewById(R.id.expire);
        register = findViewById(R.id.register);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Display back button and clear default title
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");
        }

        available.setOnClickListener(v -> {
            // Get the current date
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            // Create DatePickerDialog and set the selected date in EditText
            DatePickerDialog datePickerDialog = new DatePickerDialog(ProvideFood.this,
                    (view, year1, monthOfYear, dayOfMonth) -> {
                        String selectedDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year1;
                        available.setText(selectedDate);
                    }, year, month, day);
            datePickerDialog.show();
        });

        expire.setOnClickListener(v -> {
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            // Create DatePickerDialog and set the selected date in EditText
            DatePickerDialog datePickerDialog = new DatePickerDialog(ProvideFood.this,
                    (view, year1, monthOfYear, dayOfMonth) -> {
                        // monthOfYear is 0-indexed so add 1
                        String selectedDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year1;
                        expire.setText(selectedDate);
                    }, year, month, day);
            datePickerDialog.show();
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db = new ProviderDB(ProvideFood.this);
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
                    errorMessages.append("Quantity field is required.\n");
                    quantity.setError("Quantity field is required");
                    isValid = false;
                }
                if (selectedStorage.equals("Select Storage Requirement")) {
                    errorMessages.append("Please select a valid Storage Requirement.\n");
                    isValid = false;
                }
                if (available1.isEmpty()) {
                    errorMessages.append("Available Date field is required.\n");
                    available.setError("Available Date field is required");
                    isValid = false;
                }
                if (expire1.isEmpty()) {
                    errorMessages.append("Expiry Date field is required.\n");
                    expire.setError("Expiry Date field is required");
                    isValid = false;
                }

                if (isValid) {
                    if (db.insertData(selectedFood, quantity1, selectedStorage, available1, expire1)) {
                        Toast.makeText(ProvideFood.this, "Data Inserted", Toast.LENGTH_LONG).show();
                        sendNotification("Food Donation Detail", "The food details have been added successfully.");
                    } else {
                        Toast.makeText(ProvideFood.this, "Data not Inserted", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(ProvideFood.this, errorMessages.toString(), Toast.LENGTH_LONG).show();
                }
            }
        });
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
                // Handle the back button click here
                onBackPressed(); // Go back to the previous activity
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
