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
import android.os.Bundle;
import android.os.Build;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
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
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.example.unitconverter.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;

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
    private static final String CHANNEL_ID = "ReceiverChannel";
    private static final String CHANNEL_NAME = "Receiver Notifications";
    private NotificationManager notificationManager;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_receiver);

        reference = findViewById(R.id.reference);
        type = findViewById(R.id.type);
        member = findViewById(R.id.member);
        requirement = findViewById(R.id.requirement);
        RadioGroup radioGroup = findViewById(R.id.radio_btn);
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
                Toast.makeText(ReceiverRegister.this, "Please wait while admin accepts your request.", Toast.LENGTH_SHORT).show();
                sendNotification(reference1, "Receiver Registration Request");
            } else {
                Toast.makeText(ReceiverRegister.this, errorMessages.toString(), Toast.LENGTH_LONG).show();
            }
        });

        login.setOnClickListener(v -> {
            Intent intent = new Intent(ReceiverRegister.this, ReceiverLogin.class);
            startActivity(intent);
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

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription("Channel for receiver notifications");
            channel.enableLights(true);
            channel.setLightColor(Color.RED);
            channel.enableVibration(true);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void sendNotification(String Organization_Reference, String title) {
        Intent intent = new Intent(this, ReceiverRegister.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                this,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.notification)
                .setContentTitle(title)
                .setContentText(Organization_Reference + " wants to register.")
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