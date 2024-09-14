package com.example.unitconverter.AdminInterface;

import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;

import com.example.unitconverter.ProviderInterface.ProvideFood;
import com.example.unitconverter.R;
import com.example.unitconverter.RiderInterface.RiderAssignDutyDB;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AssignDuty extends AppCompatActivity {
    private EditText username, pick, drop, date;
    private Button submit_btn;
    private RiderAssignDutyDB db;

    // Notification settings
    private static final String CHANNEL_ID = "DutyChannel";
    private static final String CHANNEL_NAME = "Duty Notifications";
    private NotificationManager notificationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.assign_duty);

        username = findViewById(R.id.name);
        pick = findViewById(R.id.pick);
        drop = findViewById(R.id.drop);
        date = findViewById(R.id.date);
        Calendar calendar = Calendar.getInstance();

        submit_btn = findViewById(R.id.submit);

        // Setup Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Display back button and clear default title
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");
        }

        date.setOnClickListener(v -> {
            // Open DatePickerDialog
            new DatePickerDialog(AssignDuty.this, (view, year, monthOfYear, dayOfMonth) -> {
                // Set the selected date in the Calendar object
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                // Open TimePickerDialog after DatePickerDialog
                new TimePickerDialog(AssignDuty.this, (view1, hourOfDay, minute) -> {
                    // Set the selected time in the Calendar object
                    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    calendar.set(Calendar.MINUTE, minute);
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
                    date.setText(format.format(calendar.getTime()));
                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
        });

        // Initialize notification manager
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        createNotificationChannel();

        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db = new RiderAssignDutyDB(AssignDuty.this);
                db.getWritableDatabase();

                boolean isValid = true;
                StringBuilder errorMessages = new StringBuilder();

                String name1 = username.getText().toString().trim();
                String pick1 = pick.getText().toString().trim();
                String drop1 = drop.getText().toString().trim();
                String date1 = date.getText().toString().trim();

                username.setError(null);
                pick.setError(null);
                drop.setError(null);
                date.setError(null);

                if (name1.isEmpty()) {
                    errorMessages.append("Name field is required.\n");
                    username.setError("Name field is required");
                    isValid = false;
                }
                if (pick1.isEmpty()) {
                    errorMessages.append("Pick Location field is required.\n");
                    pick.setError("Pick Location field is required");
                    isValid = false;
                }
                if (drop1.isEmpty()) {
                    errorMessages.append("Drop Location field is required.\n");
                    drop.setError("Drop Location field is required");
                    isValid = false;
                }
                if (date1.isEmpty()) {
                    errorMessages.append("Date field is required.\n");
                    date.setError("Date field is required");
                    isValid = false;
                }

                if (isValid) {
                    if(db.assignDuty(name1, pick1, drop1, date1, "Pending")) {
                        Toast.makeText(AssignDuty.this, "Data Inserted", Toast.LENGTH_LONG).show();
                        sendNotification(name1, "Duty Assigned");
                    } else {
                        Toast.makeText(AssignDuty.this, "Data not Inserted", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(AssignDuty.this, errorMessages.toString(), Toast.LENGTH_LONG).show();
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
            channel.setDescription("Channel for duty notifications");
            channel.enableLights(true);
            channel.setLightColor(Color.RED);
            channel.enableVibration(true);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void sendNotification(String riderName, String title) {
        Intent intent = new Intent(this, AssignDuty.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                this,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.notification) // Use your own notification icon here
                .setContentTitle(title)
                .setContentText("Duty assigned to " + riderName)
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