package com.example.forgotpassword;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.UUID;

public class ForgotPasswordActivity extends AppCompatActivity {

    DatabaseHelper db;
    EditText etEmail;
    Button btnSendResetLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        db = new DatabaseHelper(this);
        etEmail = findViewById(R.id.etEmail);
        btnSendResetLink = findViewById(R.id.btnSendResetLink);

        btnSendResetLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = etEmail.getText().toString().trim();  // Trim whitespace
                if (isValidEmail(email)) {
                    String token = UUID.randomUUID().toString();  // Generate unique token

                    // Set token expiration time (e.g., 1 hour)
                    long expirationTime = System.currentTimeMillis() + (60 * 60 * 1000); // 1 hour in milliseconds

                    // Check if the email exists in the database
                    if (db.checkEmail(email)) {
                        // Store the reset token
                        if (db.storeResetToken(email, token, expirationTime)) {
                            String resetLink = "yourapp://reset_password?email=" + email + "&token=" + token;

                            // Send email with the reset link
                            sendResetLinkEmail(email, resetLink, token);
                            Toast.makeText(ForgotPasswordActivity.this, "Reset link sent to " + email, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ForgotPasswordActivity.this, "Could not generate reset token.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(ForgotPasswordActivity.this, "Email not found", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ForgotPasswordActivity.this, "Please enter a valid email", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Method to validate the email format
    private boolean isValidEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    // Method to send the email
    private void sendResetLinkEmail(String userEmail, String resetLink, String token) {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("text/html"); // Set type to HTML

        // Create clickable links for both the reset link and the token
        String emailBody = "Click the link below to reset your password" +
                resetLink + "\">Reset Password</a><br/>" +
                "You can also use this token directly:<br/>" +
                "<a href=\"yourapp://reset_password?token=" + UUID.randomUUID().toString() + "\">" +
                UUID.randomUUID().toString() + "</a>"; // Create a clickable link for the token

        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{userEmail});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Password Reset Request");
        emailIntent.putExtra(Intent.EXTRA_TEXT, emailBody);

        try {
            startActivity(Intent.createChooser(emailIntent, "Send Email..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(ForgotPasswordActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }

}
