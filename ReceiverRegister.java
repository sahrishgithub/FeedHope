package com.example.feedhope.ReceiverInterface.ReceiverRegister;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
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
import com.example.feedhope.R;
import com.example.feedhope.ReceiverInterface.ReceiverLogin;
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
import java.util.Calendar;
import java.util.Random;

public class ReceiverRegister extends AppCompatActivity {
    private EditText member,reference, card, phone, email, pass;
    private Spinner type;
    private Button register;
    private boolean isPasswordVisible = false;
    private ArrayList<ReceiverModalClass> receiverList;
    private SharedPreferences sharedPreferences;
    private Gson gson;
    int otp;
    TextView LocationTextView;
    String PREFS_NAME = "LocationPrefs";
    String KEY_LOCATION_NAME = "currentLocationName";
    String LocationName;

    @SuppressLint({"ClickableViewAccessibility", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_receiver);

        reference = findViewById(R.id.reference);
        type = findViewById(R.id.type);
        member = findViewById(R.id.member);
        card = findViewById(R.id.card);
        RadioGroup frequency = findViewById(R.id.frequency);
        phone = findViewById(R.id.phone);
        email = findViewById(R.id.email);
        pass = findViewById(R.id.pass);
        LocationTextView = findViewById(R.id.location);
        register = findViewById(R.id.register);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");
        }

        retrieveCurrentLocation();
        // Display the location to the user
        if (LocationName != null) {
            LocationTextView.setText("Current location: " + LocationName);
        } else {
            LocationTextView.setText("Location not provided.");
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

        phone.addTextChangedListener(new TextWatcher() {
            private boolean isUpdating = false;
            private boolean isToastShown = false;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Do nothing here
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (isUpdating) return;

                isUpdating = true;
                String text = s.toString();

                // Ensure the prefix is "+92"
                if (!text.startsWith("+92")) {
                    text = "+92" + text.replace("+92", ""); // Remove duplicate "+92"
                }

                // Remove invalid characters and ensure only digits after "+92"
                String digits = text.length() > 3 ? text.substring(3).replaceAll("[^0-9]", "") : "";

                // Enforce starting with "3" and limit to 10 digits
                if (digits.length() > 0 && !digits.startsWith("3")) {
                    digits = "3" + digits.replaceFirst("^\\d*", ""); // Ensure it starts with "3"
                }

                if (digits.length() > 10) {
                    digits = digits.substring(0, 10); // Limit to 10 digits
                }

                text = "+92" + digits;
                phone.setText(text);
                phone.setSelection(text.length()); // Move cursor to the end
                isUpdating = false;
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Validate the total number of digits after "+92"
                String text = phone.getText().toString();
                if (text.startsWith("+92")) {
                    String digits = text.substring(3).replaceAll("[^0-9]", ""); // Extract only digits after "+92"

                    if (digits.length() != 10 && !isToastShown) {
                        isToastShown = true; // Prevent showing multiple toasts
                        showToast("Phone number must have exactly 10 digits.");
                    } else if (digits.length() == 10) {
                        isToastShown = false; // Reset if the condition is met
                    }
                }
            }

            private void showToast(String message) {
                Toast.makeText(phone.getContext(), message, Toast.LENGTH_SHORT).show();
            }
        });


        frequency.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton frequencybtn = findViewById(checkedId);
            String frequency1 = frequencybtn.getText().toString();
            Toast.makeText(ReceiverRegister.this, "Selected: " + frequency1, Toast.LENGTH_SHORT).show();
        });

        register.setOnClickListener(v -> {
            String reference1, member1, frequency1, phone1,card1,email1, pass1;
            boolean isValid = true;
            StringBuilder errorMessages = new StringBuilder();

            reference1 = reference.getText().toString().trim();
            String selectedType = type.getSelectedItem().toString();
            member1 = member.getText().toString().trim();
            card1 = card.getText().toString().trim();

            int frequencyCheckedRadioButtonId = frequency.getCheckedRadioButtonId();
            RadioButton selectedFrequency = findViewById(frequencyCheckedRadioButtonId);
            frequency1 = (selectedFrequency != null) ? selectedFrequency.getText().toString().trim() : "";

            phone1 = phone.getText().toString().trim();
            email1 = email.getText().toString().trim();
            pass1 = pass.getText().toString().trim();
            String location1=LocationTextView.getText().toString().trim();

            reference.setError(null);
            member.setError(null);
            phone.setError(null);
            email.setError(null);
            pass.setError(null);
            card.setError(null);
            LocationTextView.setError(null);

            if (!isValidPhoneNumber(phone1)) {
                phone.setError("Please enter a valid phone number.");
                isValid = false;
            }
            if (reference1.isEmpty()) {
                reference.setError("Reference field is required");
                isValid = false;
            }
            if (selectedType.equals("Select Organization Type")) {
                errorMessages.append("Please select a valid Organization Type.\n");
                isValid = false;
            }
            if (member1.isEmpty() || Integer.parseInt(member1) < 20) {
                member.setError("Members must be greater than 20.");
                isValid = false;
            }
            if (!isValidCardNumber(card1)) {
                card.setError("Please enter a valid card number.");
                isValid = false;
            }
            if (frequency1.isEmpty()) {
                errorMessages.append("Please select a frequency.\n");
                isValid = false;
            }
            if (!isValidEmail(email1)) {
                email.setError("Please enter a valid email address");
                isValid = false;
            }if (pass1.length() != 8) {
                pass.setError("Password must be at least 8 characters long");
                isValid = false;
            }

            if (isValid) {
                saveData();
                reference.setText("");
                member.setText("");
                phone.setText("");
                card.setText("");
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
                    intent.putExtra("selectedFrequency", frequency1);
                    intent.putExtra("phone", phone1);
                    intent.putExtra("card",card1);
                    intent.putExtra("email", email1);
                    intent.putExtra("pass", pass1);
                    intent.putExtra("location",location1);
                    intent.putExtra("generated_otp", otp);
                    startActivity(intent);
                } else {
                    Toast.makeText(ReceiverRegister.this, "Please enter your email", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(ReceiverRegister.this, errorMessages.toString(), Toast.LENGTH_LONG).show();
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
    private boolean isValidCardNumber(String cardNumber) {
        int sum = 0;
        boolean alternate = false;
        for (int i = cardNumber.length() - 1; i >= 0; i--) {
            int n = Integer.parseInt(cardNumber.substring(i, i + 1));
            if (alternate) {
                n *= 2;
                if (n > 9) {
                    n = (n % 10) + 1;
                }
            }
            sum += n;
            alternate = !alternate;
        }
        return (sum % 10 == 0);
    }
    private void retrieveCurrentLocation() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        LocationName = sharedPreferences.getString(KEY_LOCATION_NAME, ""); // Retrieve location name

        if (!LocationName.isEmpty()) {
            LocationTextView.setText("Current location: " + LocationName);
        } else {
            LocationTextView.setText("Location not provided.");
            Toast.makeText(this, "Invalid location data received. Please ensure location access is enabled.", Toast.LENGTH_SHORT).show();
        }
    }
    private int generateRandomOTP() {
        Random random = new Random();
        return 10000 + random.nextInt(90000);
    }
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
                    Toast.makeText(ReceiverRegister.this, "OTP sent successfully!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ReceiverRegister.this, "Failed to send OTP. Please check your email.", Toast.LENGTH_SHORT).show();
                }
            }
        }.execute();
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
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}