package com.example.feedhope;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class login extends AppCompatActivity {
    signupDB db;
    EditText email,pass;
    Button login;
    TextView register;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        db = new signupDB(this);
        email = findViewById(R.id.email);
        pass = findViewById(R.id.pass);
        register=findViewById(R.id.register);
        login = findViewById(R.id.login);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email1 = email.getText().toString();
                String pass1 = pass.getText().toString();

                if (email1.isEmpty() || pass1.isEmpty()) {
                    Toast.makeText(login.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                } else {
                    boolean isValid = db.checkUser(email1, pass1);
                    if (isValid) {
                        Toast.makeText(login.this, "Login successful", Toast.LENGTH_SHORT).show();
                        // Navigate to another activity if login is successful
                        Intent intent = new Intent(login.this, homePage.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(login.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(login.this, signup.class);
                startActivity(intent);
            }
        });
    }
}