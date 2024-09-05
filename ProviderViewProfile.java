package com.example.unitconverter.ProviderInterface;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.unitconverter.R;

public class ProviderViewProfile extends AppCompatActivity {
    private TextView name, phone, email, pass;
    private Button update;
    private ProviderDB registerDB;
    private ProviderModalClass provider;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_profile);

        name = findViewById(R.id.name);
        phone = findViewById(R.id.phone);
        email = findViewById(R.id.email);
        pass = findViewById(R.id.pass);
        update = findViewById(R.id.update);

        registerDB = new ProviderDB(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");
        }

        // Get email from intent
        String loggedInEmail = getIntent().getStringExtra("email");

        if (loggedInEmail == null || loggedInEmail.isEmpty()) {
            Toast.makeText(this, "Email is not entered", Toast.LENGTH_SHORT).show();
            return;
        }

        provider = registerDB.read(loggedInEmail);

        if (provider != null) {
            name.setText(provider.getName());
            phone.setText(provider.getPhone());
            email.setText(provider.getEmail());
            pass.setText(provider.getPass());
        } else {
            Toast.makeText(this, "No profile found", Toast.LENGTH_SHORT).show();
        }

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProviderViewProfile.this, ProviderUpdateProfile.class);
                intent.putExtra("email", loggedInEmail);  // Pass the email to UpdateProfile
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