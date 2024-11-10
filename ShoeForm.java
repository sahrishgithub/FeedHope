package com.example.feedhope.ProviderInterface.ShoeDonation;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import com.example.feedhope.R;
import com.example.feedhope.ReceiverInterface.ReceiverRegister;

public class ShoeForm extends AppCompatActivity {
    Spinner type,condition;
    EditText quantity,size;
    RadioGroup gender;
    Button register;
    ShoeDB db;
    private String loggedInEmail;
    private NotificationManager notificationManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.donate_shoe);
        type = findViewById(R.id.type);
        quantity = findViewById(R.id.quantity);
        condition = findViewById(R.id.condition);
        gender = findViewById(R.id.gender);
        size = findViewById(R.id.size);

        register = findViewById(R.id.register);
        loggedInEmail = getIntent().getStringExtra("email");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");
        }

        gender.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton genderbtn = findViewById(checkedId);
            String frequency1 = genderbtn.getText().toString();
            Toast.makeText(ShoeForm.this, "Selected: " + frequency1, Toast.LENGTH_SHORT).show();
        });

        register.setOnClickListener(v -> {
            db = new ShoeDB(ShoeForm.this);
            db.getWritableDatabase();

            String quantity1,gender1,size1;
            boolean isValid = true;
            StringBuilder errorMessages = new StringBuilder();

            String selectedType = type.getSelectedItem().toString();
            quantity1 = quantity.getText().toString().trim();
            size1 = size.getText().toString().trim();
            String selectedCondition = condition.getSelectedItem().toString();

            int genderCheckedRadioButtonId = gender.getCheckedRadioButtonId();
            RadioButton selectedGender = findViewById(genderCheckedRadioButtonId);
            gender1 = (selectedGender != null) ? selectedGender.getText().toString().trim() : "";

            if (selectedType.equals("Select Type of Shoes")) {
                errorMessages.append("Please select a valid Shoe Type.\n");
                isValid = false;
            }
            if (quantity1.isEmpty()) {
                quantity.setError("Quantity field is required");
                isValid = false;
            }
            if (selectedCondition.equals("Select Condition")) {
                errorMessages.append("Please select a valid Condition.\n");
                isValid = false;
            }
            if (gender1.isEmpty()) {
                errorMessages.append("Please select a Gender.\n");
                isValid = false;
            }
            if (size1.isEmpty()) {
                size.setError("Size field is required");
                isValid = false;
            }

            if (isValid) {
                if (db.insert(loggedInEmail,selectedType,quantity1,selectedCondition, gender1,size1)) {
                    Toast.makeText(ShoeForm.this, "Data Inserted", Toast.LENGTH_LONG).show();
                    sendNotification("Shoe Donation Detail", "The Shoe details have been added successfully.");
                    finish();
                } else {
                    Toast.makeText(ShoeForm.this, "Data not Inserted", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(ShoeForm.this, errorMessages.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void sendNotification(String title, String message) {
        String channelId = "Donation_notifications";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "Shoe Donation Notifications",
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