package com.example.unitconverter.AdminInterface;

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
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;

import com.example.unitconverter.R;
import com.example.unitconverter.ReceiverInterface.ReceiverRegisterDB;

public class InformDonation extends AppCompatActivity {
    private EditText name, quantity, storage, expire;
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

        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db = new ReceiverRegisterDB(InformDonation.this);
                db.getWritableDatabase();
                String name1 = name.getText().toString().trim();
                String quantity1 = quantity.getText().toString().trim();
                String storage1 = storage.getText().toString().trim();
                String expire1 = expire.getText().toString().trim();

                if (name1.isEmpty() || quantity1.isEmpty() || storage1.isEmpty() || expire1.isEmpty()) {
                    Toast.makeText(InformDonation.this, "All fields are required", Toast.LENGTH_LONG).show();
                } else if (db.insert(name1, quantity1, storage1, expire1, "Pending")) {
                    Toast.makeText(InformDonation.this, "Data Inserted", Toast.LENGTH_LONG).show();
                    sendNotification(name1, "Donation Information");
                } else {
                    Toast.makeText(InformDonation.this, "Data not Inserted", Toast.LENGTH_LONG).show();
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