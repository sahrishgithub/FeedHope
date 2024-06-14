package com.example.feedhope;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class signup extends AppCompatActivity {
    private signupDB db;
    private EditText name, phone, email, pass;
    private TextView login;
    private Button register;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);
        name = findViewById(R.id.name);
        phone = findViewById(R.id.phone);
        email = findViewById(R.id.email);
        pass = findViewById(R.id.pass);
        login = findViewById(R.id.login);
        register = findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db = new signupDB(signup.this);
                SQLiteDatabase db1 = db.getWritableDatabase();
                String name1, phone1,email1,pass1;

                name1 = name.getText().toString().trim();
                phone1 = phone.getText().toString().trim();
                email1 = email.getText().toString().trim();
                pass1 = pass.getText().toString().trim();

                if (name1.isEmpty() || phone1.isEmpty() || email1.isEmpty() || pass1.isEmpty()) {
                    Toast.makeText(signup.this, "All fields are required", Toast.LENGTH_LONG).show();
                    return;
                }

                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email1).matches()) {
                    Toast.makeText(signup.this, "Please enter a valid email address", Toast.LENGTH_LONG).show();
                    return;
                }
                if (db.insert(name1,phone1,email1, pass1)) {
                    Toast.makeText(signup.this, "Data Inserted", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(signup.this, homePage.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(signup.this, "Data not Inserted", Toast.LENGTH_LONG).show();
                }
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(signup.this, login.class);
                startActivity(intent);
            }
        });
    }
}