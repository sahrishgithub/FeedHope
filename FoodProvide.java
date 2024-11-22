package com.example.feedhope.ProviderInterface.FoodDonation;

import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
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

import com.example.feedhope.R;

import java.util.Calendar;

public class FoodProvide extends AppCompatActivity {
    private EditText quantity, available, expire;
    private TextView item;
    private Spinner food,storage;
    private Button register;
    private FoodDB db;
    private String loggedInEmail;
    private NotificationManager notificationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.donate_food);

        food = findViewById(R.id.food);
        quantity = findViewById(R.id.quantity);
        storage = findViewById(R.id.storage);
        available = findViewById(R.id.available);
        expire = findViewById(R.id.expire);
        register = findViewById(R.id.register);
        item = findViewById(R.id.item);

        loggedInEmail = getIntent().getStringExtra("email");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
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
            DatePickerDialog datePickerDialog = new DatePickerDialog(FoodProvide.this,
                    (view, year1, monthOfYear, dayOfMonth) -> {
                        String selectedDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year1;
                        available.setText(selectedDate);

                        // Update the expiry date logic to start from the next day after the availability date
                        final Calendar expireCalendar = Calendar.getInstance();
                        expireCalendar.set(year1, monthOfYear, dayOfMonth);
                        expireCalendar.add(Calendar.DAY_OF_MONTH, 1); // Expiry date starts the day after availability

                        expire.setOnClickListener(expireView -> {
                            DatePickerDialog expireDatePickerDialog = new DatePickerDialog(FoodProvide.this,
                                    (expireView1, expireYear, expireMonth, expireDay) -> {
                                        String selectedExpireDate = expireDay + "/" + (expireMonth + 1) + "/" + expireYear;
                                        expire.setText(selectedExpireDate);
                                    },
                                    expireCalendar.get(Calendar.YEAR),
                                    expireCalendar.get(Calendar.MONTH),
                                    expireCalendar.get(Calendar.DAY_OF_MONTH));

                            // Restrict expiry date to start from the day after the availability date
                            expireDatePickerDialog.getDatePicker().setMinDate(expireCalendar.getTimeInMillis());
                            expireDatePickerDialog.show();
                        });
                    },
                    year, month, day);

            // Restrict availability date selection to the current date and beyond
            datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
            datePickerDialog.show();
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db = new FoodDB(FoodProvide.this);
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
                    String Quantity = quantity1+ " "+ item.getText().toString().trim();
                    if (db.insertData(loggedInEmail,selectedFood, Quantity, selectedStorage, available1, expire1)) {
                        Toast.makeText(FoodProvide.this, "Data Inserted", Toast.LENGTH_LONG).show();
                        sendNotification("Food Donation Detail", "The food details have been added successfully.");
                        finish();
                    } else {
                        Toast.makeText(FoodProvide.this, "Data not Inserted", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(FoodProvide.this, errorMessages.toString(), Toast.LENGTH_LONG).show();
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
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
