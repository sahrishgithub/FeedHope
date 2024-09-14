package com.example.unitconverter.RiderInterface;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;

import com.example.unitconverter.R;
import com.example.unitconverter.ReceiverInterface.ReceiverModalClass;
import com.example.unitconverter.ReceiverInterface.ReceiverRegister;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class RiderRegister extends AppCompatActivity {
    private EditText name,IDNumber,banking, phone, email, pass;
    private Spinner IDtype, hours,days;
    private Button register;
    private TextView login;
    private boolean isPasswordVisible = false;
    private ArrayList<RiderModalClass> riderList;
    private SharedPreferences sharedPreferences;
    private Gson gson;

    private static final String CHANNEL_ID = "RiderChannel";
    private static final String CHANNEL_NAME = "Rider Notifications";
    private NotificationManager notificationManager;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_rider);

        name = findViewById(R.id.name);
        days = findViewById(R.id.days);
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

        pass.setOnTouchListener((v, event) -> {
            final int DRAWABLE_RIGHT = 2;  // Index for the drawableRight
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getRawX() >= (pass.getRight() - pass.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                    // Toggle the visibility of the password
                    if (isPasswordVisible) {
                        // Hide password
                        pass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        pass.setCompoundDrawablesWithIntrinsicBounds(R.drawable.pass, 0, R.drawable.close_eye, 0);
                    } else {
                        // Show password
                        pass.setInputType(InputType.TYPE_CLASS_TEXT);
                        pass.setCompoundDrawablesWithIntrinsicBounds(R.drawable.pass, 0, R.drawable.open_eye, 0);
                    }
                    isPasswordVisible = !isPasswordVisible;
                    pass.setSelection(pass.getText().length());
                    return true;
                }
            }
            return false;
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name1, phone1, IDNumber1,banking1,email1,pass1;
                boolean isValid = true;
                StringBuilder errorMessages = new StringBuilder();

                name1 = name.getText().toString().trim();
                phone1 = phone.getText().toString().trim();
                String selectedType = IDtype.getSelectedItem().toString();
                IDNumber1 = IDNumber.getText().toString().trim();
                String selectedHours = hours.getSelectedItem().toString();
                String selectedDays = days.getSelectedItem().toString();
                banking1 = banking.getText().toString().trim();
                email1 = email.getText().toString().trim();
                pass1 = pass.getText().toString().trim();

                name.setError(null);
                IDNumber.setError(null);
                banking.setError(null);
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
                if (selectedType.equals("Select ID Type")) {
                    errorMessages.append("Please select a valid ID Type.\n");
                    isValid = false;
                }
                if (IDNumber1.isEmpty()) {
                    errorMessages.append("IDNumber field is required.\n");
                    IDNumber.setError("IDNumber field is required");
                    isValid = false;
                }
                if (selectedHours.equals("Select Working Hours")) {
                    errorMessages.append("Please select a valid Working Hours.\n");
                    isValid = false;
                }
                if (selectedDays.equals("Select Working Days")) {
                    errorMessages.append("Please select a valid Working Days.\n");
                    isValid = false;
                }
                if (banking1.isEmpty()) {
                    errorMessages.append("Banking field is required.\n");
                    banking.setError("Banking field is required");
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
                   riderList.add(new RiderModalClass(name1, phone1, selectedType, IDNumber1, selectedHours, selectedDays, banking1, email1, pass1));
                    saveData();
                    name.setText("");
                    phone.setText("");
                    IDNumber.setText("");
                    banking.setText("");
                    email.setText("");
                    pass.setText("");
                    Toast.makeText(RiderRegister.this, "Please wait while admin accepts your request.", Toast.LENGTH_SHORT).show();
                    sendNotification(name1, "Rider Registration Request");
                } else {
                    Toast.makeText(RiderRegister.this, errorMessages.toString(), Toast.LENGTH_SHORT).show();
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
