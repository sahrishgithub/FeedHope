package com.example.unitconverter.ReceiverInterface;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.example.unitconverter.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;

public class ReceiverRegister extends AppCompatActivity {

    private static final String PREFS_NAME = "LocationPrefs";
    private static final String KEY_LATITUDE = "currentLatitude";
    private static final String KEY_LONGITUDE = "currentLongitude";
    private static final String KEY_LOCATION_NAME = "currentLocationName";

    private TextView locationTextView;
    private String locationName;
    private double latitude, longitude;
    private EditText member, reference, time, phone, email, pass;
    private Spinner type, requirement;
    private Button register;
    private boolean isPasswordVisible = false;
    private TextView login;
    private String selectedTime = "";
    private ArrayList<ReceiverModalClass> receiverList;
    private SharedPreferences sharedPreferences;
    private Gson gson;
    private RadioGroup radioGroup;
    private static final String CHANNEL_ID = "ReceiverChannel";
    private static final String CHANNEL_NAME = "Receiver Notifications";
    private NotificationManager notificationManager;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_receiver);

        // Initialize SharedPreferences before using it
        sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        radioGroup = findViewById(R.id.radio_btn); // Initialize RadioGroup here

        reference = findViewById(R.id.reference);
        type = findViewById(R.id.type);
        member = findViewById(R.id.member);
        requirement = findViewById(R.id.requirement);
        time = findViewById(R.id.time);
        phone = findViewById(R.id.phone);
        email = findViewById(R.id.email);
        pass = findViewById(R.id.pass);
        register = findViewById(R.id.register);
        login = findViewById(R.id.login);
        locationTextView = findViewById(R.id.location);

        // Update location text
        if (locationTextView != null) {
            locationTextView.setText("Loading location...");
        } else {
            Log.e("ReceiverRegister", "TextView is null");
        }

        // Disable suggestions for phone input field
        phone.setInputType(InputType.TYPE_CLASS_PHONE);
        phone.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);

        // Retrieve the location data from SharedPreferences
        retrieveCurrentLocation();

        // Update location text
        if (locationTextView != null) {
            locationTextView.setText("Current Location: " + locationName);
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");
        }

        sharedPreferences = getSharedPreferences("receiverPrefs", Context.MODE_PRIVATE);
        gson = new Gson();
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
                        pass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        pass.setCompoundDrawablesWithIntrinsicBounds(R.drawable.pass, 0, R.drawable.close_eye, 0);
                    } else {
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

        time.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(ReceiverRegister.this,
                    new TimePickerDialog.OnTimeSetListener() {
                        @SuppressLint("DefaultLocale")
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            selectedTime = String.format("%02d:%02d", hourOfDay, minute);
                            time.setText(selectedTime);
                        }
                    }, hour, minute, true // Use 24-hour format
            );
            timePickerDialog.show();
        });

        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton selectedRadioButton = findViewById(checkedId);
            String selectedText = selectedRadioButton.getText().toString();
            Toast.makeText(ReceiverRegister.this, "Selected: " + selectedText, Toast.LENGTH_SHORT).show();
        });

        register.setOnClickListener(v -> {
            String reference1, time1, member1, frequency1, phone1, email1, pass1;
            boolean isValid = true;
            StringBuilder errorMessages = new StringBuilder();

            reference1 = reference.getText().toString().trim();
            String selectedType = type.getSelectedItem().toString();
            member1 = member.getText().toString().trim();
            String selectedRequirement = requirement.getSelectedItem().toString();

            int selectedRadioButtonId = radioGroup.getCheckedRadioButtonId();
            RadioButton selectedRadioButton = findViewById(selectedRadioButtonId);
            frequency1 = (selectedRadioButton != null) ? selectedRadioButton.getText().toString().trim() : "";

            time1 = selectedTime;
            phone1 = phone.getText().toString().trim();
            email1 = email.getText().toString().trim();
            pass1 = pass.getText().toString().trim();

            // Clear previous error messages
            reference.setError(null);
            member.setError(null);
            time.setError(null);
            phone.setError(null);
            email.setError(null);
            pass.setError(null);

            if (phone1.isEmpty()) {
                errorMessages.append("Phone field is required.\n");
                phone.setError("Phone field is required.");
                isValid = false;
            } else if (!isValidPhoneNumber(phone1)) {
                errorMessages.append("Please enter a valid phone number.\n");
                phone.setError("Please enter a valid phone number.");
                isValid = false;
            }
            if (reference1.isEmpty()) {
                errorMessages.append("Reference field is required.\n");
                reference.setError("Reference field is required");
                isValid = false;
            }
            if (selectedType.equals("Select Organization Type")) {
                errorMessages.append("Please select a valid Organization Type.\n");
                isValid = false;
            }
            if (member1.isEmpty()) {
                errorMessages.append("Member field is required.\n");
                member.setError("Member field is required");
                isValid = false;
            }
            if (selectedRequirement.equals("Select Food Requirement")) {
                errorMessages.append("Please select a valid Food Requirement.\n");
                isValid = false;
            }
            if (frequency1.isEmpty()) {
                errorMessages.append("Please select a frequency.\n");
                isValid = false;
            }
            if (time1.isEmpty()) {
                errorMessages.append("Time field is required.\n");
                time.setError("Time field is required");
                isValid = false;
            }
            if (phone1.isEmpty()) {
                errorMessages.append("Phone field is required.\n");
                phone.setError("Phone field is required");
                isValid = false;
            }
            if (email1.isEmpty()) {
                errorMessages.append("Email field is required.\n");
                email.setError("Email field is required");
                isValid = false;
            }
            if (pass1.isEmpty()) {
                errorMessages.append("Password field is required.\n");
                pass.setError("Password field is required");
                isValid = false;
            }

            if (isValid) {
                saveReceiverData(reference1, selectedType, member1, selectedRequirement, frequency1, time1, phone1, email1, pass1);
            } else {
                Toast.makeText(ReceiverRegister.this, "Please fix the following errors:\n" + errorMessages.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        login.setOnClickListener(v -> {
            // Logic for navigating to login screen
        });
    }

    private boolean isValidPhoneNumber(String phoneNumber) {
        PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
        try {
            Phonenumber.PhoneNumber number = phoneNumberUtil.parse(phoneNumber, "US");
            return phoneNumberUtil.isValidNumber(number);
        } catch (Exception e) {
            return false;
        }
    }

    private void saveReceiverData(String reference1, String selectedType, String member1, String selectedRequirement,
                                  String frequency1, String time1, String phone1, String email1, String pass1) {
        ReceiverDB receiverDB = new ReceiverDB(this);

        long id = receiverDB.insertReceiverData(reference1, selectedType, member1, selectedRequirement, frequency1, time1, phone1, email1, pass1);
        if (id != -1) {
            showToast("Receiver data saved with ID: " + id);
        } else {
            showToast("Failed to save data");
        }
    }

    private void showToast(String message) {
        Toast.makeText(ReceiverRegister.this, message, Toast.LENGTH_SHORT).show();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = CHANNEL_NAME;
            String description = "Notifications for receiver registration";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void retrieveCurrentLocation() {
        latitude = sharedPreferences.getFloat(KEY_LATITUDE, 0);
        longitude = sharedPreferences.getFloat(KEY_LONGITUDE, 0);
        locationName = sharedPreferences.getString(KEY_LOCATION_NAME, "Unknown location");
    }

    private void loadData() {
        // Load receiver data from shared preferences
        String json = sharedPreferences.getString("receiverList", "");
        if (!json.isEmpty()) {
            Type type = new TypeToken<ArrayList<ReceiverModalClass>>() {}.getType();
            receiverList = gson.fromJson(json, type);
        } else {
            receiverList = new ArrayList<>();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
