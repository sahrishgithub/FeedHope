package com.example.feedhope.AdminInterface;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import com.example.feedhope.R;
import com.example.feedhope.ReceiverInterface.FoodInform.FoodInformDB;
import com.example.feedhope.ReceiverInterface.ReceiverRegister.ReceiverRegisterDB;

import java.util.ArrayList;

public class FoodInform extends AppCompatActivity {
    EditText quantity;
    TextView storage,expire,item,limit;
    Spinner email;
    Button submit_btn;
    ReceiverRegisterDB receiverRegisterDB;
    FoodInformDB db;
    static final String CHANNEL_ID = "DonationChannel";
    static final String CHANNEL_NAME = "Donation Notifications";
    NotificationManager notificationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_food_donation_inform);

        email = findViewById(R.id.email);
        quantity = findViewById(R.id.quantity);
        storage = findViewById(R.id.storage);
        expire = findViewById(R.id.expire);
        item = findViewById(R.id.item);
        limit = findViewById(R.id.limit);
        submit_btn = findViewById(R.id.submit);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");
        }

        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        createNotificationChannel();

        String storage1 = getIntent().getStringExtra("storage");
        String expire1 = getIntent().getStringExtra("expire");
        storage.setText(storage1);
        expire.setText(expire1 );

        receiverRegisterDB = new ReceiverRegisterDB(FoodInform.this);
        // Now you can safely call methods on db
        ArrayList<String> emailList = receiverRegisterDB.getAllReceiverName();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, emailList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        email.setAdapter(adapter);

        String passedQuantity = getIntent().getStringExtra("quantity");
        String numericPassedQuantity = passedQuantity.replaceAll("[^0-9]", "");

        SharedPreferences sharedPreferences = getSharedPreferences("FoodData", Context.MODE_PRIVATE);
        int position = getIntent().getIntExtra("position", -1);
        int remainingQuantity = sharedPreferences.getInt("remaining_quantity_" + position, Integer.parseInt(numericPassedQuantity));

        if (passedQuantity != null) {
            limit.setText("≤\t " + remainingQuantity);
        }

        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db = new FoodInformDB(FoodInform.this);
                db.getWritableDatabase();

                boolean isValid = true;
                StringBuilder errorMessages = new StringBuilder();
                String quantity1 = quantity.getText().toString().trim();

                quantity.setError(null);
                if (quantity1.isEmpty()) {
                    quantity.setError("Quantity field is required");
                    isValid = false;
                } else {
                    try {
                        int enteredQuantity = Integer.parseInt(quantity1);
                        int previousQuantity = Integer.parseInt(numericPassedQuantity);
                        if (enteredQuantity > previousQuantity) {
                            quantity.setError("Quantity cannot exceed the available quantity (" + previousQuantity + ")");
                            isValid = false;
                        }
                    } catch (NumberFormatException e) {
                        quantity.setError("Invalid quantity entered");
                        isValid = false;
                    }
                }

                if (isValid) {
                    String numericPassedQuantity = getIntent().getStringExtra("quantity").replaceAll("[^0-9]", "");
                    int remainingQuantity = Integer.parseInt(numericPassedQuantity) - Integer.parseInt(quantity1);
                    // Save the remaining quantity in SharedPreferences
                    SharedPreferences sharedPreferences = getSharedPreferences("FoodData", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    int position = getIntent().getIntExtra("position", -1);
                    editor.putInt("remaining_quantity_" + position, remainingQuantity);
                    editor.apply();
                    // Update the limit TextView
                    limit.setText("≤\t " + remainingQuantity);

                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("remaining_quantity", remainingQuantity);
                    resultIntent.putExtra("position", position);
                    setResult(RESULT_OK, resultIntent);

                    String selectedEmail = email.getSelectedItem().toString();

                    String Quantity = quantity1 + " " + item.getText().toString().trim();
                    if (db.insert(selectedEmail, Quantity, storage1, expire1, "Pending")) {
                        Toast.makeText(FoodInform.this, "Data Inserted", Toast.LENGTH_LONG).show();
                        sendNotification(selectedEmail, "Donation Information");
                        finish();
                    } else {
                        Toast.makeText(FoodInform.this, "Data not Inserted", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(FoodInform.this, errorMessages.toString(), Toast.LENGTH_LONG).show();
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
        Intent intent = new Intent(this, FoodInform.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                this,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.notification)
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
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}