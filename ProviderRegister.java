package com.example.unitconverter.ProviderInterface;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Build;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import com.example.unitconverter.R;
import com.example.unitconverter.RiderInterface.RiderModalClass;
import com.example.unitconverter.RiderInterface.RiderRegister;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class ProviderRegister extends AppCompatActivity {
    private EditText name, phone, email, pass;
    private TextView login;
    private Button register;
    private ArrayList<ProviderModalClass> providerList;
    private SharedPreferences sharedPreferences;
    private Gson gson;

    // Notification settings
    private static final String CHANNEL_ID = "ProviderChannel";
    private static final String CHANNEL_NAME = "Provider Notifications";
    private NotificationManager notificationManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_provider);
        name = findViewById(R.id.name);
        phone = findViewById(R.id.phone);
        email = findViewById(R.id.email);
        pass = findViewById(R.id.pass);
        login = findViewById(R.id.login);
        register = findViewById(R.id.register);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // for back option
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");
        }

        sharedPreferences = getSharedPreferences("providerPrefs", Context.MODE_PRIVATE);
        gson = new Gson();
        loadData();

        // Initialize notification manager
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        createNotificationChannel();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isValid = true;
                StringBuilder errorMessages = new StringBuilder();

                String name1 = name.getText().toString().trim();
                String phone1 = phone.getText().toString().trim();
                String email1 = email.getText().toString().trim();
                String pass1 = pass.getText().toString().trim();

                name.setError(null);
                phone.setError(null);
                email.setError(null);
                pass.setError(null);

                if (name1.isEmpty()) {
                    errorMessages.append("Reference field is required.\n");
                    name.setError("Reference field is required");
                    isValid = false;
                }
                if (phone1.isEmpty()) {
                    errorMessages.append("Phone field is required.\n");
                    phone.setError("Phone field is required.");
                    isValid = false;
                } else if (!isValidPhoneNumber(phone1)) {
                    errorMessages.append("Please enter a valid phone number.\n");
                    phone.setError("Please enter a valid phone number.");
                    isValid = false;
                }
                if (email1.isEmpty()) {
                    errorMessages.append("Email field is required.\n");
                    email.setError("Email field is required");
                    isValid = false;
                } else if (!isValidEmail(email1)) {
                    errorMessages.append("Please enter a valid email address.\n");
                    email.setError("Please enter a valid email address");
                    isValid = false;
                }if (pass1.isEmpty()) {
                    errorMessages.append("Password field is required.\n");
                    pass.setError("Password field is required");
                    isValid = false;
                }else if (pass1.length() != 8) {
                    errorMessages.append("Password must be at least 8 characters long.\n");
                    pass.setError("Password must be at least 8 characters long");
                    isValid = false;
                }
                if (isValid) {
                    providerList.add(new ProviderModalClass(name1, phone1, email1, pass1));
                    saveData();
                    name.setText("");
                    phone.setText("");
                    email.setText("");
                    pass.setText("");
                    Toast.makeText(ProviderRegister.this, "Please wait while admin accepts your request.", Toast.LENGTH_SHORT).show();
                    sendNotification(name1, "Provider Registration Request");
                } else {
                    Toast.makeText(ProviderRegister.this, errorMessages.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProviderRegister.this, ProviderLogin.class);
                startActivity(intent);
            }
        });
    }

    private boolean isValidPhoneNumber(String phoneNumber) {
        PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
        try {
            Phonenumber.PhoneNumber number = phoneNumberUtil.parse(phoneNumber, "Pakistan");
            return phoneNumberUtil.isValidNumber(number);
        } catch (Exception e) {
            Log.e("PhoneValidation", "Invalid phone number: " + phoneNumber, e);
            return false;
        }
    }
    private boolean isValidEmail(String email) {
        String emailPattern = "^[a-zA-Z0-9._%+-]+@(gmail\\.com|hotmail\\.com|yahoo\\.com|outlook\\.com)$";
        return email.matches(emailPattern);
    }
    private void loadData() {
        String json = sharedPreferences.getString("providerList", null);
        Type type = new TypeToken<ArrayList<ProviderModalClass>>() {}.getType();
        providerList = gson.fromJson(json, type);

        if (providerList == null) {
            providerList = new ArrayList<>();
        }
        // Log data loaded
        Log.d("ProviderRegister", "Loaded provider list size: " + providerList.size());
    }

    private void saveData() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String json = gson.toJson(providerList);
        editor.putString("providerList", json);
        editor.apply();
        // Log data saved
        Log.d("ProviderRegister", "Data saved: " + json);
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription("Channel for provider notifications");
            channel.enableLights(true);
            channel.setLightColor(Color.RED);
            channel.enableVibration(true);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void sendNotification(String name, String title) {
        Intent intent = new Intent(this, ProviderRegister.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                this,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.notification)
                .setContentTitle(title)
                .setContentText(name + " wants to register.")
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