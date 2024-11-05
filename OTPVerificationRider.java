package com.example.unitconverter.RiderInterface;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONObject;

import com.example.unitconverter.AppInterface.OTP_Status;
import com.example.unitconverter.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mailjet.client.MailjetClient;
import com.mailjet.client.MailjetRequest;
import com.mailjet.client.MailjetResponse;
import com.mailjet.client.resource.Emailv31;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Random;

public class OTPVerificationRider extends AppCompatActivity {

    private EditText otp0, otp1, otp2, otp3, otp4;
    private TextView resend,emailText;
    private Button verifyButton;
    private int generatedOTP;
    private static final String CHANNEL_ID = "RiderChannel";
    private static final String CHANNEL_NAME = "Rider Notifications";
    private NotificationManager notificationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.otp_verification);

        otp0 = findViewById(R.id.otp0);
        otp1 = findViewById(R.id.otp1);
        otp2 = findViewById(R.id.otp2);
        otp3 = findViewById(R.id.otp3);
        otp4 = findViewById(R.id.otp4);
        emailText = findViewById(R.id.email);
        verifyButton = findViewById(R.id.verifybtn);
        resend = findViewById(R.id.resend);

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String phone = intent.getStringExtra("phone");
        String IDType = intent.getStringExtra("IDtype");
        String IDNumber = intent.getStringExtra("IDNumber");
        String hours = intent.getStringExtra("hours");
        String days = intent.getStringExtra("days");
        String banking = intent.getStringExtra("banking");
        String email = intent.getStringExtra("email");
        String pass = intent.getStringExtra("pass");
        generatedOTP = getIntent().getIntExtra("generated_otp", 0);

        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        createNotificationChannel();

        if (email != null) {
            emailText.setText(email);
        }
        // Set up TextWatchers for automatic focus change
        setupOTPInput();

        verifyButton.setOnClickListener(v -> {
            String enteredOTP = otp0.getText().toString() + otp1.getText().toString() + otp2.getText().toString() + otp3.getText().toString() + otp4.getText().toString();
            if (enteredOTP.equals(String.valueOf(generatedOTP))) {

                ArrayList<RiderModalClass> riderList;
                SharedPreferences sharedPreferences = getSharedPreferences("riderPrefs", Context.MODE_PRIVATE);
                Gson gson = new Gson();
                String json = sharedPreferences.getString("riderList", null);
                Type type = new TypeToken<ArrayList<RiderModalClass>>() {}.getType();
                riderList = gson.fromJson(json, type);
                if (riderList == null) {
                    riderList = new ArrayList<>();
                }

                riderList.add(new RiderModalClass(name, phone,IDType,IDNumber,hours,days,banking, email, pass));
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("riderList", gson.toJson(riderList));
                editor.apply();
                Toast.makeText(OTPVerificationRider.this, "Rider registered successfully!", Toast.LENGTH_SHORT).show();
                sendNotification(name, "Rider Registration Request");
                Intent mainIntent = new Intent(OTPVerificationRider.this, OTP_Status.class);
                startActivity(mainIntent);
                finish();
            } else {
                Toast.makeText(OTPVerificationRider.this, "Incorrect OTP. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });

        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Resend OTP
                generatedOTP = generateRandomOTP();
                sendOTPEmail(email, generatedOTP);
                Toast.makeText(OTPVerificationRider.this, "New OTP sent to email", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupOTPInput() {
        otp0.addTextChangedListener(new OTPTextWatcher(otp0, otp1));
        otp1.addTextChangedListener(new OTPTextWatcher(otp1, otp2));
        otp2.addTextChangedListener(new OTPTextWatcher(otp2, otp3));
        otp3.addTextChangedListener(new OTPTextWatcher(otp3, otp4));
        otp4.addTextChangedListener(new OTPTextWatcher(otp4, null));
    }

    private class OTPTextWatcher implements TextWatcher {

        private final EditText currentView;
        private final EditText nextView;

        public OTPTextWatcher(EditText currentView, EditText nextView) {
            this.currentView = currentView;
            this.nextView = nextView;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.length() == 1 && nextView != null) {
                nextView.requestFocus(); // Move to next EditText
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    }

    private int generateRandomOTP() {
        Random random = new Random();
        return 10000 + random.nextInt(90000); // Generates a random 5-digit number
    }

    // Method to resend OTP via email using Mailjet API
    @SuppressLint("StaticFieldLeak")
    private void sendOTPEmail(final String email, final int otp) {
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
                                                            .put("Name", "user")))
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
                    Toast.makeText(OTPVerificationRider.this, "OTP sent successfully!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(OTPVerificationRider.this, "Failed to send OTP. Please check your email.", Toast.LENGTH_SHORT).show();
                }
            }
        }.execute();
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

    private void sendNotification(String name, String title) {
        Intent intent = new Intent(this, RiderRegister.class);
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
}