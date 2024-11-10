package com.example.unitconverter.ReceiverInterface;

import static com.example.unitconverter.AppInterface.GoogleMapActivity.KEY_LATITUDE;
import static com.example.unitconverter.AppInterface.GoogleMapActivity.KEY_LOCATION_NAME;
import static com.example.unitconverter.AppInterface.GoogleMapActivity.KEY_LONGITUDE;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.unitconverter.R;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.Calendar;

public class ReceiverRegister extends AppCompatActivity {

    private static final String PREFS_NAME = "LocationPrefs";
    private TextView locationTextView;
    private String locationName;
    private double latitude, longitude;
    private EditText member, time, phone, email, pass;
    AutoCompleteTextView reference;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_receiver);

        // Initialize SharedPreferences before using it
        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        radioGroup = findViewById(R.id.radio_btn); // Initialize RadioGroup here

        // Initialize AutoCompleteTextView for reference
        reference = findViewById(R.id.reference);
        if (reference != null) {
            String referenceText = reference.getText().toString();
            // Log the reference or handle logic
            Log.d("ReceiverRegister", "Reference Text: " + referenceText);
        } else {
            Log.e("ReceiverRegister", "AutoCompleteTextView reference is null");
        }

        // Initialize other views
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

        // Retrieve location data from SharedPreferences
        retrieveCurrentLocation();

        // Display the current location
        locationTextView.setText("Current Location: " + locationName);

        // Set up other UI components
        setupUI();

        // Handle register button click
        register.setOnClickListener(v -> handleRegisterButtonClick());

        // Handle login button click
        login.setOnClickListener(v -> {
            // Logic for navigating to the login screen
        });
    }

    private void setupUI() {
        // Disable suggestions for phone input field
        phone.setInputType(InputType.TYPE_CLASS_PHONE);
        phone.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);

        // Set up password visibility toggle
        pass.setOnTouchListener((v, event) -> {
            final int DRAWABLE_RIGHT = 2;
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getRawX() >= (pass.getRight() - pass.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                    // Toggle the visibility of the password
                    togglePasswordVisibility();
                    return true;
                }
            }
            return false;
        });

        // Time picker dialog
        time.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(ReceiverRegister.this,
                    (view, hourOfDay, minute1) -> {
                        selectedTime = String.format("%02d:%02d", hourOfDay, minute1);
                        time.setText(selectedTime);
                    }, hour, minute, true);
            timePickerDialog.show();
        });
    }

    private void togglePasswordVisibility() {
        if (isPasswordVisible) {
            pass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            pass.setCompoundDrawablesWithIntrinsicBounds(R.drawable.pass, 0, R.drawable.close_eye, 0);
        } else {
            pass.setInputType(InputType.TYPE_CLASS_TEXT);
            pass.setCompoundDrawablesWithIntrinsicBounds(R.drawable.pass, 0, R.drawable.open_eye, 0);
        }
        isPasswordVisible = !isPasswordVisible;
        pass.setSelection(pass.getText().length());
    }

    private void handleRegisterButtonClick() {
        String reference1 = reference.getText().toString().trim();
        String selectedType = type.getSelectedItem().toString();
        String member1 = member.getText().toString().trim();
        String selectedRequirement = requirement.getSelectedItem().toString();
        int selectedRadioButtonId = radioGroup.getCheckedRadioButtonId();
        RadioButton selectedRadioButton = findViewById(selectedRadioButtonId);
        String frequency1 = selectedRadioButton != null ? selectedRadioButton.getText().toString().trim() : "";
        String time1 = selectedTime;
        String phone1 = phone.getText().toString().trim();
        String email1 = email.getText().toString().trim();
        String pass1 = pass.getText().toString().trim();

        boolean isValid = validateInputs(reference1, selectedType, member1, selectedRequirement, frequency1, time1, phone1, email1, pass1);

        if (isValid) {
            saveReceiverData(reference1, selectedType, member1, selectedRequirement, frequency1, time1, phone1, email1, pass1,locationName,longitude,latitude);
        } else {
            Toast.makeText(ReceiverRegister.this, "Please fix the errors in the form.", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validateInputs(String reference, String selectedType, String member, String selectedRequirement,
                                   String frequency, String time, String phone, String email, String pass) {
        boolean isValid = true;

        if (reference.isEmpty()) {
            isValid = false;
            // Set error on the AutoCompleteTextView reference, not on the String reference.
            this.reference.setError("Reference field is required.");
        }
        if (selectedType.equals("Select Organization Type")) {
            isValid = false;
        }
        if (member.isEmpty()) {
            isValid = false;
            this.member.setError("Member field is required.");
        }
        if (selectedRequirement.equals("Select Food Requirement")) {
            isValid = false;
        }
        if (frequency.isEmpty()) {
            isValid = false;
        }
        if (time.isEmpty()) {
            isValid = false;
            this.time.setError("Time field is required.");
        }
        if (phone.isEmpty()) {
            isValid = false;
            this.phone.setError("Phone field is required.");
        }
        if (email.isEmpty()) {
            isValid = false;
            this.email.setError("Email field is required.");
        }
        if (pass.isEmpty()) {
            isValid = false;
            this.pass.setError("Password field is required.");
        }

        return isValid;
    }

    private void saveReceiverData(String reference, String selectedType, String member, String selectedRequirement,
                                  String frequency, String time, String phone, String email, String pass,String locationName,double longitude,double latitude) {
        ReceiverDB receiverDB = new ReceiverDB(this);
        long id = receiverDB.insertReceiverData(reference, selectedType, member, selectedRequirement, frequency, time, phone, email, pass,longitude,latitude,locationName);
        if (id != -1) {
            Toast.makeText(ReceiverRegister.this, "Receiver data saved with ID: " + id, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(ReceiverRegister.this, "Failed to save data", Toast.LENGTH_SHORT).show();
        }
    }

    private void retrieveCurrentLocation() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        locationName = sharedPreferences.getString(KEY_LOCATION_NAME, ""); // Retrieve location name
        latitude = sharedPreferences.getFloat(KEY_LATITUDE, Float.NaN);
        longitude = sharedPreferences.getFloat(KEY_LONGITUDE, Float.NaN);

        if (!locationName.isEmpty() && !Double.isNaN(latitude) && !Double.isNaN(longitude)) {
            locationTextView.setText("Your current location is: " + locationName);
        } else {
            locationTextView.setText("Location not provided.");
            Toast.makeText(this, "Invalid location data received. Please ensure location access is enabled.", Toast.LENGTH_SHORT).show();
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
