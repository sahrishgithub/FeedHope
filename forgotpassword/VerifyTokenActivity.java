package com.example.forgotpassword;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class VerifyTokenActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private String email, token;
    private Button btnVerifyToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_token);

        db = new DatabaseHelper(this);
        btnVerifyToken = findViewById(R.id.btnVerifyToken);

        // Get email and token from Intent data
        Intent intent = getIntent();
        email = intent.getData().getQueryParameter("email");
        token = intent.getData().getQueryParameter("token");

        // Check if email or token is null
        if (email == null || token == null) {
            Toast.makeText(this, "Email or token is missing", Toast.LENGTH_SHORT).show();
            finish(); // Close this activity if necessary
            return; // Exit on null input
        }

        // Proceed to reset password activity
        btnVerifyToken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Verify the token
                if (db.verifyToken(email, token)) {
                    Intent resetIntent = new Intent(VerifyTokenActivity.this, ResetPasswordActivity.class);
                    resetIntent.putExtra("email", email);
                    resetIntent.putExtra("token", token); // Pass token for verification in ResetPasswordActivity
                    startActivity(resetIntent);
                    finish(); // Optionally finish this activity
                } else {
                    Toast.makeText(VerifyTokenActivity.this, "Invalid or expired token", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
