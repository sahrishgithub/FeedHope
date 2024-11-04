package com.example.unitconverter.AppInterface;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
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
import com.example.unitconverter.R;
import com.mailjet.client.MailjetClient;
import com.mailjet.client.MailjetRequest;
import com.mailjet.client.MailjetResponse;
import com.mailjet.client.resource.Emailv31;
import java.util.Random;

public class OTPVerification extends AppCompatActivity {

    private EditText otp0, otp1, otp2, otp3, otp4;
    private TextView resend,emailText;
    private Button verifyButton;
    private int generatedOTP;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.otp_verification);

        // Initialize views

        otp0 = findViewById(R.id.otp0);
        otp1 = findViewById(R.id.otp1);
        otp2 = findViewById(R.id.otp2);
        otp3 = findViewById(R.id.otp3);
        otp4 = findViewById(R.id.otp4);
        emailText = findViewById(R.id.email);
        verifyButton = findViewById(R.id.verifybtn);
        resend = findViewById(R.id.resend);

        // Get OTP and email from Intent
        email = getIntent().getStringExtra("email");
        generatedOTP = getIntent().getIntExtra("generated_otp", 0);

        // For set email
        if (email != null) {
            emailText.setText(email);
        }

        // Set up TextWatchers for automatic focus change
        setupOTPInput();

        verifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String enteredOTP = otp0.getText().toString().trim() +
                        otp1.getText().toString().trim() +
                        otp2.getText().toString().trim() +
                        otp3.getText().toString().trim() +
                        otp4.getText().toString().trim();

                if (enteredOTP.equals(String.valueOf(generatedOTP))) {
                    // Open next page if OTP is correct
                    Intent intent = new Intent(OTPVerification.this, OTP_Status.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(OTPVerification.this, "Invalid OTP, try again!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Resend OTP
                generatedOTP = generateRandomOTP();
                sendOTPEmail(email, generatedOTP);
                Toast.makeText(OTPVerification.this, "New OTP sent to email", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(OTPVerification.this, "OTP sent successfully!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(OTPVerification.this, "Failed to send OTP. Please check your email.", Toast.LENGTH_SHORT).show();
                }
            }
        }.execute();
    }
}