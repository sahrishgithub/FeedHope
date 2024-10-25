package com.example.forgotpassword;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class ResetPasswordActivity extends AppCompatActivity {

    DatabaseHelper db;
    EditText etNewPassword, etConfirmPassword;
    Button btnResetPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        db = new DatabaseHelper(this);
        etNewPassword = findViewById(R.id.etNewPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnResetPassword = findViewById(R.id.btnResetPassword);

        // Get email and token from the intent
        Intent intent = getIntent();
        String email = intent.getStringExtra("email");
        String token = intent.getStringExtra("token");

        // If you need to log or display these values, do so here.
        // However, avoid showing them to the user directly unless necessary.

        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newPassword = etNewPassword.getText().toString();
                String confirmPassword = etConfirmPassword.getText().toString();

                if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
                    Toast.makeText(ResetPasswordActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                } else if (!newPassword.equals(confirmPassword)) {
                    Toast.makeText(ResetPasswordActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                } else {
                    // Verify token and reset the password
                    if (db.verifyToken(email, token)) {
                        db.resetPassword(email, newPassword);
                        Toast.makeText(ResetPasswordActivity.this, "Password successfully reset", Toast.LENGTH_SHORT).show();
                        finish(); // Close this activity
                    } else {
                        Toast.makeText(ResetPasswordActivity.this, "Invalid or expired token", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
