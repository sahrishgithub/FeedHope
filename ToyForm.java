package com.example.feedhope.ProviderInterface.ToyDonation;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import com.example.feedhope.R;

public class ToyForm extends AppCompatActivity {
    Spinner age,condition,category;
    EditText name,quantity;
    Button register;
    ToyDB db;
    private String loggedInEmail;
    private NotificationManager notificationManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.donate_toy);
        name = findViewById(R.id.name);
        age = findViewById(R.id.age);
        condition = findViewById(R.id.condition);
        quantity = findViewById(R.id.quantity);
        category = findViewById(R.id.category);

        register = findViewById(R.id.register);
        loggedInEmail = getIntent().getStringExtra("email");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");
        }

        register.setOnClickListener(v -> {
            db = new ToyDB(ToyForm.this);
            db.getWritableDatabase();

            String name1,quantity1;
            boolean isValid = true;
            StringBuilder errorMessages = new StringBuilder();

            name1 = name.getText().toString().trim();
            String selectedAge = age.getSelectedItem().toString();
            quantity1 = quantity.getText().toString().trim();
            String selectedCondition = condition.getSelectedItem().toString();
            String selectedCategory = category.getSelectedItem().toString();

            if (name1.isEmpty()) {
                name.setError("Toy Name field is required");
                isValid = false;
            }
            if (selectedAge.equals("Select Age Range")) {
                errorMessages.append("Please select a valid Age.\n");
                isValid = false;
            }
            if (quantity1.isEmpty()) {
                quantity.setError("Quantity field is required");
                isValid = false;
            }
            if (selectedCondition.equals("Select Condition")) {
                errorMessages.append("Please select a valid Cloth Condition.\n");
                isValid = false;
            }
            if (selectedCategory.equals("Select Toys Category")) {
                errorMessages.append("Please select a valid Cloth Category.\n");
                isValid = false;
            }
            if (isValid) {
                if (db.insert(loggedInEmail,name1,selectedAge, quantity1, selectedCondition, selectedCategory)) {
                    Toast.makeText(ToyForm.this, "Data Inserted", Toast.LENGTH_LONG).show();
                    sendNotification("Toy Donation Detail", "The Toy details have been added successfully.");
                    finish();
                } else {
                    Toast.makeText(ToyForm.this, "Data not Inserted", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(ToyForm.this, errorMessages.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void sendNotification(String title, String message) {
        String channelId = "Donation_notifications";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "Toy Donation Notifications",
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