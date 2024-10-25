package com.example.forgotpassword;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class SignupActivity extends AppCompatActivity {
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        db = new DatabaseHelper(this);
        EditText editTextEmail = findViewById(R.id.editTextEmail);
        EditText editTextPassword = findViewById(R.id.editTextPassword);
        Button buttonSignup = findViewById(R.id.buttonSignup);
        TextView textViewLogin = findViewById(R.id.textViewLogin);

        buttonSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextEmail.getText().toString();
                String password = editTextPassword.getText().toString();

                if (db.checkEmail(email)) {
                    Toast.makeText(SignupActivity.this, "Email already exists!", Toast.LENGTH_SHORT).show();
                } else {
                    if (db.insertData(email, password)) {
                        Toast.makeText(SignupActivity.this, "Signup successful!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(SignupActivity.this, loginactivity.class));
                    } else {
                        Toast.makeText(SignupActivity.this, "Signup failed!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        textViewLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignupActivity.this, loginactivity.class));
            }
        });
    }
}
