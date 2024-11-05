package com.example.unitconverter.ReceiverInterface;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Build;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
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
import androidx.core.app.NotificationCompat;

import com.example.unitconverter.ProviderInterface.OTPVerificationProvider;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.example.unitconverter.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mailjet.client.MailjetClient;
import com.mailjet.client.MailjetRequest;
import com.mailjet.client.MailjetResponse;
import com.mailjet.client.resource.Emailv31;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

public class ReceiverRegister extends AppCompatActivity {
    private EditText member, reference, time, phone, email, pass;
    private Spinner type, requirement;
    private Button register;
    private boolean isPasswordVisible = false;
    private TextView login;
    private String selectedTime = "";
    private ArrayList<ReceiverModalClass> receiverList;
    private SharedPreferences sharedPreferences;
    private Gson gson;
    int otp;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_receiver);

        reference = findViewById(R.id.reference);
        type = findViewById(R.id.type);
        member = findViewById(R.id.member);
        requirement = findViewById(R.id.requirement);
        RadioGroup frequency = findViewById(R.id.frequency);
        time = findViewById(R.id.time);
        phone = findViewById(R.id.phone);
        email = findViewById(R.id.email);
        pass = findViewById(R.id.pass);
        register = findViewById(R.id.register);
        login = findViewById(R.id.login);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");
        }

        sharedPreferences = getSharedPreferences("receiverPrefs", Context.MODE_PRIVATE);
        gson = new Gson();
        loadData();

        pass.setOnTouchListener((v, event) -> {
            final int DRAWABLE_RIGHT = 2;
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getRawX() >= (pass.getRight() - pass.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
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

        frequency.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton frequencybtn = findViewById(checkedId);
            String frequency1 = frequencybtn.getText().toString();
            Toast.makeText(ReceiverRegister.this, "Selected: " + frequency1, Toast.LENGTH_SHORT).show();
        });

        register.setOnClickListener(v -> {
            String reference1, time1, member1, frequency1, phone1, email1, pass1;
            boolean isValid = true;
            StringBuilder errorMessages = new StringBuilder();

            reference1 = reference.getText().toString().trim();
            String selectedType = type.getSelectedItem().toString();
            member1 = member.getText().toString().trim();
            String selectedRequirement = requirement.getSelectedItem().toString();

            int frequencyCheckedRadioButtonId = frequency.getCheckedRadioButtonId();
            RadioButton selectedFrequency = findViewById(frequencyCheckedRadioButtonId);
            frequency1 = (selectedFrequency != null) ? selectedFrequency.getText().toString().trim() : "";

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
                receiverList.add(new ReceiverModalClass(reference1, selectedType, member1, selectedRequirement, frequency1, time1, phone1, email1, pass1));
                saveData();
                reference.setText("");
                member.setText("");
                time.setText("");
                phone.setText("");
                email.setText("");
                pass.setText("");
                Toast.makeText(ReceiverRegister.this, "Your OTP was sent successfully! Check your email to continue.", Toast.LENGTH_SHORT).show();

                if (!email1.isEmpty()) {
                    otp = generateRandomOTP();
                    sendOTPEmail(reference1,email1, otp);

                    Intent intent = new Intent(ReceiverRegister.this, OTPVerificationReceiver.class);
                    intent.putExtra("reference", reference1);
                    intent.putExtra("type", selectedType);
                    intent.putExtra("member", member1);
                    intent.putExtra("requirement", selectedRequirement);
                    intent.putExtra("selectedFrequency", frequency1);
                    intent.putExtra("selectedTime",time1 );
                    intent.putExtra("phone", phone1);
                    intent.putExtra("email", email1);
                    intent.putExtra("pass", pass1);
                    intent.putExtra("generated_otp", otp);
                    startActivity(intent);
                } else {
                    Toast.makeText(ReceiverRegister.this, "Please enter your email", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(ReceiverRegister.this, errorMessages.toString(), Toast.LENGTH_LONG).show();
            }
        });

        login.setOnClickListener(v -> {
            Intent intent = new Intent(ReceiverRegister.this, ReceiverLogin.class);
            startActivity(intent);
        });
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
                                                    .put("Name", "Roha Ashraf"))
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
                    Toast.makeText(ReceiverRegister.this, "OTP sent successfully!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ReceiverRegister.this, "Failed to send OTP. Please check your email.", Toast.LENGTH_SHORT).show();
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
        String json = sharedPreferences.getString("receiverList", null);
        Type type = new TypeToken<ArrayList<ReceiverModalClass>>() {}.getType();
        receiverList = gson.fromJson(json, type);

        if (receiverList == null) {
            receiverList = new ArrayList<>();
        }
        // Log data loaded
        Log.d("ReceiverRegister", "Loaded receiver list size: " + receiverList.size());
    }

    private void saveData() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String json = gson.toJson(receiverList);
        editor.putString("receiverList", json);
        editor.apply();
        // Log data saved
        Log.d("ReceiverRegister", "Data saved: " + json);
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