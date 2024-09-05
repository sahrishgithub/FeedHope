package com.example.unitconverter.RiderInterface;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.example.unitconverter.R;

public class RiderLogin extends AppCompatActivity {
    RiderRegisterDB db;
    EditText email,pass;
    Button login;
    TextView register;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        db = new RiderRegisterDB(this);
        email = findViewById(R.id.email);
        pass = findViewById(R.id.pass);
        register=findViewById(R.id.register);
        login = findViewById(R.id.login);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //for back option
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");
        }

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email1 = email.getText().toString();
                String pass1 = pass.getText().toString();

                if (email1.isEmpty() || pass1.isEmpty()) {
                    Toast.makeText(RiderLogin.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                } else {
                    boolean isValid = db.checkUser(email1, pass1);
                    if (isValid) {
                        Toast.makeText(RiderLogin.this, "Login successful", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RiderLogin.this, RiderHomePage.class);
                        intent.putExtra("email", email1);
                        startActivity(intent);
                    } else {
                        Toast.makeText(RiderLogin.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
                    }
                };
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RiderLogin.this, RiderRegister.class);
                startActivity(intent);
            }
        });
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