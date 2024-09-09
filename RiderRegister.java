package com.example.unitconverter.RiderInterface;

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
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;

import com.example.unitconverter.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class RiderRegister extends AppCompatActivity {
    private EditText name, IDtype, IDNumber, hours, banking, phone, email, pass;
    private Button register;
    private TextView login;
    private AutoCompleteTextView autoCompleteTextView;
    private ArrayList<RiderModalClass> riderList;
    private SharedPreferences sharedPreferences;
    private Gson gson;

    // Notification settings
    private static final String CHANNEL_ID = "RiderChannel";
    private static final String CHANNEL_NAME = "Rider Notifications";
    private NotificationManager notificationManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_rider);

        autoCompleteTextView = findViewById(R.id.days);
        String[] suggestions = getResources().getStringArray(R.array.suggestions_array);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, suggestions);
        autoCompleteTextView.setAdapter(arrayAdapter);

        name = findViewById(R.id.name);
        IDtype = findViewById(R.id.type);
        IDNumber = findViewById(R.id.number);
        hours = findViewById(R.id.hours);
        banking = findViewById(R.id.card);
        phone = findViewById(R.id.phone);
        email = findViewById(R.id.email);
        pass = findViewById(R.id.pass);
        register = findViewById(R.id.register);
        login = findViewById(R.id.login);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // for back option
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");
        }

        sharedPreferences = getSharedPreferences("riderPrefs", Context.MODE_PRIVATE);
        gson = new Gson();
        // Load previously saved data
        loadData();

        // Initialize notification manager
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        createNotificationChannel();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name1, phone1, IDtype1, IDNumber1, hours1, banking1, autoText, email1, pass1;

                name1 = name.getText().toString().trim();
                phone1 = phone.getText().toString().trim();
                IDtype1 = IDtype.getText().toString().trim();
                IDNumber1 = IDNumber.getText().toString().trim();
                hours1 = hours.getText().toString().trim();
                autoText = autoCompleteTextView.getText().toString().trim();
                banking1 = banking.getText().toString().trim();
                email1 = email.getText().toString().trim();
                pass1 = pass.getText().toString().trim();

                if (!name1.isEmpty() && !phone1.isEmpty() && !IDtype1.isEmpty() && !IDNumber1.isEmpty() && !hours1.isEmpty() && !autoText.isEmpty() && !banking1.isEmpty() && !email1.isEmpty() && !pass1.isEmpty()) {
                    riderList.add(new RiderModalClass(name1, phone1, IDtype1, IDNumber1, hours1, autoText, banking1, email1, pass1));
                    saveData();
                    name.setText("");
                    phone.setText("");
                    IDtype.setText("");
                    IDNumber.setText("");
                    hours.setText("");
                    autoCompleteTextView.setText("");
                    banking.setText("");
                    email.setText("");
                    pass.setText("");
                    Toast.makeText(RiderRegister.this, "Please wait while admin accepts your request.", Toast.LENGTH_SHORT).show();
                    sendNotification(name1, "Rider Registration Request");
                } else {
                    Toast.makeText(RiderRegister.this, "All fields must be filled", Toast.LENGTH_SHORT).show();
                }
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RiderRegister.this, RiderLogin.class);
                startActivity(intent);
            }
        });
    }

    private void loadData() {
        String json = sharedPreferences.getString("riderList", null);
        Type type = new TypeToken<ArrayList<RiderModalClass>>() {}.getType();
        riderList = gson.fromJson(json, type);

        if (riderList == null) {
            riderList = new ArrayList<>();
        }
    }

    private void saveData() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String json = gson.toJson(riderList);
        editor.putString("riderList", json);
        editor.apply();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription("Channel for rider notifications");
            channel.enableLights(true);
            channel.setLightColor(Color.RED);
            channel.enableVibration(true);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void sendNotification(String riderName, String title) {
        Intent intent = new Intent(this, RiderRegister.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                this,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.notification) // Use your own notification icon here
                .setContentTitle(title)
                .setContentText(riderName + " wants to register.")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        notificationManager.notify((int) System.currentTimeMillis(), builder.build());
    }

    @Override
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
