package com.example.feedhope.ProviderInterface.ProviderRegister;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.feedhope.R;
import com.example.feedhope.ReceiverInterface.ReceiverRegister.ReceiverModalClass;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.mailjet.client.MailjetClient;
import com.mailjet.client.MailjetRequest;
import com.mailjet.client.MailjetResponse;
import com.mailjet.client.resource.Emailv31;
import org.json.JSONArray;
import org.json.JSONObject;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Random;

public class ProviderRegister extends AppCompatActivity {
    private static final String PREFS_NAME = "LocationPrefs";
    private static final String KEY_LOCATION_NAME = "currentLocationName";

    private TextView LocationTextView;
    private String LocationName;
    private EditText name, phone, email, pass;
    private boolean isPasswordVisible = false;
    private Button register;
    private ArrayList<ProviderModalClass> providerList;
    private SharedPreferences sharedPreferences;
    private Gson gson;
    int otp;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_provider);
        LocationTextView = findViewById(R.id.location);
        name = findViewById(R.id.name);
        phone = findViewById(R.id.phone);
        email = findViewById(R.id.email);
        pass = findViewById(R.id.pass);
        register = findViewById(R.id.register);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // for back option
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");
        }
        // Retrieve the location data from SharedPreferences
        retrieveCurrentLocation();
        // Display the location to the user
        if (LocationName != null) {
            LocationTextView.setText("Your current location is: " + LocationName);
        } else {
            LocationTextView.setText("Location not provided.");
        }

        sharedPreferences = getSharedPreferences("providerPrefs", Context.MODE_PRIVATE);
        gson = new Gson();
        loadData();

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
                boolean isValid = true;
                StringBuilder errorMessages = new StringBuilder();

                String name1 = name.getText().toString().trim();
                String phone1 = phone.getText().toString().trim();
                String email1 = email.getText().toString().trim();
                String pass1 = pass.getText().toString().trim();
                String location1=LocationTextView.getText().toString().trim();

                name.setError(null);
                phone.setError(null);
                email.setError(null);
                pass.setError(null);
                LocationTextView.setError(null);

                if (name1.isEmpty()) {
                    errorMessages.append("Name field is required.\n");
                    name.setError("Name field is required");
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
                    providerList.add(new ProviderModalClass(name1, phone1, email1, pass1,location1));
                    saveData();
                    name.setText("");
                    phone.setText("");
                    email.setText("");
                    pass.setText("");
                    LocationTextView.setText("");
                    Toast.makeText(ProviderRegister.this, "Your OTP was sent successfully! Check your email to continue.", Toast.LENGTH_SHORT).show();

                    if (!email1.isEmpty()) {
                        otp = generateRandomOTP();
                        sendOTPEmail(name1,email1, otp);

                        Intent intent = new Intent(ProviderRegister.this, OTPVerificationProvider.class);
                        intent.putExtra("name", name1);
                        intent.putExtra("phone", phone1);
                        intent.putExtra("email", email1);
                        intent.putExtra("pass", pass1);
                        intent.putExtra("location",location1);
                        intent.putExtra("generated_otp", otp);
                        startActivity(intent);
                    } else {
                        Toast.makeText(ProviderRegister.this, "Please enter your email", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(ProviderRegister.this, errorMessages.toString(), Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private void retrieveCurrentLocation() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        LocationName = sharedPreferences.getString(KEY_LOCATION_NAME, ""); // Retrieve location name

        if (!LocationName.isEmpty()) {
            LocationTextView.setText("Your current location is: " + LocationName);
        } else {
            LocationTextView.setText("Location not provided.");
            Toast.makeText(this, "Invalid location data received. Please ensure location access is enabled.", Toast.LENGTH_SHORT).show();
        }
    }

    private int generateRandomOTP() {
        Random random = new Random();
        return 10000 + random.nextInt(90000); // Generates a random 5-digit number
    }

    // Method to send OTP email using Mailjet API
    @SuppressLint("StaticFieldLeak")
    private void sendOTPEmail(final String name, final String email, final int otp) {
        new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... voids) {
                try {
                    MailjetClient client = new MailjetClient("39e58097b0b1794f1c673706c2670bbd", "602023b0c0d0e53caebe724f142be291");
                    MailjetRequest request = new MailjetRequest(Emailv31.resource)
                            .property(Emailv31.MESSAGES, new JSONArray()
                                    .put(new JSONObject()
                                            .put(Emailv31.Message.FROM, new JSONObject()
                                                    .put("Email", "rohaashraf7@gmail.com")
                                                    .put("Name", "Feed Hope"))
                                            .put(Emailv31.Message.TO, new JSONArray()
                                                    .put(new JSONObject()
                                                            .put("Email", email)
                                                            .put("Name", name)))
                                            .put(Emailv31.Message.SUBJECT, "Your OTP Code")
                                            .put(Emailv31.Message.TEXTPART, "Your OTP code is: " + otp)
                                            .put(Emailv31.Message.CUSTOMID, "AppOTPVerification")));

                    MailjetResponse response = client.post(request);
                    return response.getStatus() == 200;
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
            }

            protected void onPostExecute(Boolean success) {
                if (success) {
                    Toast.makeText(ProviderRegister.this, "OTP sent successfully!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ProviderRegister.this, "Failed to send OTP. Please check your email.", Toast.LENGTH_SHORT).show();
                }
            }
        }.execute();
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