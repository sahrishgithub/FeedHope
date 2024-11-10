package com.example.feedhope.ProviderInterface.ClothDonation;

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

public class ClothForm extends AppCompatActivity {
    Spinner type,condition,category,size;
    EditText quantity;
    Button register;
    ClothDB db;
    private String loggedInEmail;
    private NotificationManager notificationManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.donate_cloth);
        type = findViewById(R.id.type);
        condition = findViewById(R.id.condition);
        quantity = findViewById(R.id.quantity);
        category = findViewById(R.id.category);
        RadioGroup radioGroup = findViewById(R.id.radio_btn);
        size = findViewById(R.id.size);

        register = findViewById(R.id.register);
        loggedInEmail = getIntent().getStringExtra("email");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");
        }

        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton selectedRadioButton = findViewById(checkedId);
            String selectedText = selectedRadioButton.getText().toString();
            Toast.makeText(ClothForm.this, "Selected: " + selectedText, Toast.LENGTH_SHORT).show();
        });

        register.setOnClickListener(v -> {

            db = new ClothDB(ClothForm.this);
            db.getWritableDatabase();

            String quantity1,seasonal;
            boolean isValid = true;
            StringBuilder errorMessages = new StringBuilder();

            String selectedType = type.getSelectedItem().toString();
            String selectedCondition = condition.getSelectedItem().toString();
            quantity1 = quantity.getText().toString().trim();
            String selectedCategory = category.getSelectedItem().toString();

            int selectedRadioButtonId = radioGroup.getCheckedRadioButtonId();
            RadioButton selectedRadioButton = findViewById(selectedRadioButtonId);
            seasonal = (selectedRadioButton != null) ? selectedRadioButton.getText().toString().trim() : "";

            String selectedSize = size.getSelectedItem().toString();

            if (selectedType.equals("Select Cloth Type")) {
                errorMessages.append("Please select a valid Cloth Type.\n");
                isValid = false;
            }
            if (selectedCondition.equals("Select Condition")) {
                errorMessages.append("Please select a valid Cloth Condition.\n");
                isValid = false;
            }
            if (quantity1.isEmpty()) {
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
            if (isValid) {
                if (db.insert(loggedInEmail,selectedType,selectedCondition, quantity1, selectedCategory, seasonal, selectedSize)) {
                    Toast.makeText(ClothForm.this, "Data Inserted", Toast.LENGTH_LONG).show();
                    sendNotification("Cloth Donation Detail", "The cloth details have been added successfully.");
                    finish();
                } else {
                    Toast.makeText(ClothForm.this, "Data not Inserted", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(ClothForm.this, errorMessages.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void sendNotification(String title, String message) {
        String channelId = "Donation_notifications";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "Cloth Donation Notifications",
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