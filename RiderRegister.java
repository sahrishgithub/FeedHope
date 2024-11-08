package com.example.feedhope.RiderInterface;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.example.feedhope.R;
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

public class RiderRegister extends AppCompatActivity {
    private EditText name,licence,card,phone, email, pass;
    private Spinner hours,days;
    private Button register;
    private boolean isPasswordVisible = false;
    private ArrayList<RiderModalClass> riderList;
    private SharedPreferences sharedPreferences;
    private Gson gson;
    int otp;
    String licensePattern = "^[A-Za-z]{3}\\d{6}$";

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_rider);

        name = findViewById(R.id.name);
        days = findViewById(R.id.days);
        licence = findViewById(R.id.licence);
        hours = findViewById(R.id.hours);
        card = findViewById(R.id.card);
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

        sharedPreferences = getSharedPreferences("riderPrefs", Context.MODE_PRIVATE);
        gson = new Gson();
        // Load previously saved data
        loadData();

        pass.setOnTouchListener((v, event) -> {
            final int DRAWABLE_RIGHT = 2;  // Index for the drawableRight
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

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name1, phone1,licence1,card1,email1,pass1;
                boolean isValid = true;
                StringBuilder errorMessages = new StringBuilder();

                name1 = name.getText().toString().trim();
                phone1 = phone.getText().toString().trim();
                licence1 = licence.getText().toString().trim();
                String selectedHours = hours.getSelectedItem().toString();
                String selectedDays = days.getSelectedItem().toString();
                card1 = card.getText().toString().trim();
                email1 = email.getText().toString().trim();
                pass1 = pass.getText().toString().trim();

                name.setError(null);
                licence.setError(null);
                card.setError(null);
                phone.setError(null);
                email.setError(null);
                pass.setError(null);

                if (name1.isEmpty()) {
                    name.setError("Name field is required");
                    isValid = false;
                }if (!isValidPhoneNumber(phone1)) {
                    phone.setError("Please enter a valid phone number.");
                    isValid = false;
                } if (!licence1.matches(licensePattern)) {
                    licence.setError("Use format like ABC123456\"");
                    isValid = false;
                }if (selectedHours.equals("Select Working Hours")) {
                    errorMessages.append("Please select a valid Working Hours.\n");
                    isValid = false;
                }if (!isValidCardNumber(card1)) {
                    card.setError("Please enter a valid card number.");
                    isValid = false;
                }if (!isValidEmail(email1)) {
                    email.setError("Please enter a valid email address");
                    isValid = false;
                }if (pass1.length() != 8) {
                    pass.setError("Password must be at least 8 characters long");
                    isValid = false;
                }
                if (isValid) {
                   saveData();
                    name.setText("");
                    phone.setText("");
                    licence.setText("");
                    card.setText("");
                    email.setText("");
                    pass.setText("");
                    Toast.makeText(RiderRegister.this, "Your OTP was sent successfully! Check your email to continue.", Toast.LENGTH_SHORT).show();

                    if (!email1.isEmpty()) {
                        otp = generateRandomOTP();
                        sendOTPEmail(name1,email1, otp);

                        Intent intent = new Intent(RiderRegister.this, OTPVerificationRider.class);
                        intent.putExtra("name", name1);
                        intent.putExtra("phone", phone1);
                        intent.putExtra("licence", licence1);
                        intent.putExtra("hours", selectedHours);
                        intent.putExtra("days", selectedDays);
                        intent.putExtra("card", card1);
                        intent.putExtra("email", email1);
                        intent.putExtra("pass", pass1);
                        intent.putExtra("generated_otp", otp);
                        startActivity(intent);
                    } else {
                        Toast.makeText(RiderRegister.this, "Please enter your email", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(RiderRegister.this, errorMessages.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
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
                    Toast.makeText(RiderRegister.this, "OTP sent successfully!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(RiderRegister.this, "Failed to send OTP. Please check your email.", Toast.LENGTH_SHORT).show();
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
