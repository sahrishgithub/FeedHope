package com.example.unitconverter.AdminInterface;

import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
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
import com.example.unitconverter.ReceiverInterface.ReceiverRegisterDB;
import java.util.Calendar;

public class InformDonation extends AppCompatActivity {
    private EditText name, quantity, expire;
    private Spinner storage;
    private Button submit_btn;
    private ReceiverRegisterDB db;
    // Notification settings
    private static final String CHANNEL_ID = "DonationChannel";
    private static final String CHANNEL_NAME = "Donation Notifications";
    private NotificationManager notificationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inform_donation);

        name = findViewById(R.id.name);
        quantity = findViewById(R.id.quantity);
        storage = findViewById(R.id.storage);
        expire = findViewById(R.id.expire);
        submit_btn = findViewById(R.id.submit);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");
        }

        // Initialize notification manager
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        createNotificationChannel();

        expire.setOnClickListener(v -> {
            // Get the current date
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            // Create DatePickerDialog and set the selected date in EditText
            DatePickerDialog datePickerDialog = new DatePickerDialog(InformDonation.this,
                    (view, year1, monthOfYear, dayOfMonth) -> {
                        // monthOfYear is 0-indexed so add 1
                        String selectedDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year1;
                        expire.setText(selectedDate);
                    }, year, month, day);
            datePickerDialog.show();
        });

        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db = new ReceiverRegisterDB(InformDonation.this);
                db.getWritableDatabase();

                boolean isValid = true;
                StringBuilder errorMessages = new StringBuilder();

                String name1 = name.getText().toString().trim();
                String quantity1 = quantity.getText().toString().trim();
                String selectedStorage = storage.getSelectedItem().toString();
                String expire1 = expire.getText().toString().trim();

                name.setError(null);
                quantity.setError(null);
                expire.setError(null);

                if (name1.isEmpty()) {
                    errorMessages.append("Organization Name field is required.\n");
                    name.setError("Organization Name field is required");
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
                if (expire1.isEmpty()) {
                    errorMessages.append("EXpiry Date field is required.\n");
                    expire.setError("Expiry Date field is required");
                    isValid = false;
                }

                if (isValid) {
                    if (db.insert(name1, quantity1, selectedStorage, expire1, "Pending")) {
                        Toast.makeText(InformDonation.this, "Data Inserted", Toast.LENGTH_LONG).show();
                        sendNotification(name1, "Donation Information");
                    } else {
                        Toast.makeText(InformDonation.this, "Data not Inserted", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(InformDonation.this, errorMessages.toString(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription("Channel for donation notifications");
            channel.enableLights(true);
            channel.setLightColor(Color.RED);
            channel.enableVibration(true);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void sendNotification(String donationName, String title) {
        Intent intent = new Intent(this, InformDonation.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                this,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.notification) // Use your own notification icon here
                .setContentTitle(title)
                .setContentText("Donation of " + donationName + " has been recorded.")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        notificationManager.notify((int) System.currentTimeMillis(), builder.build());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed(); // Go back to the previous activity
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}