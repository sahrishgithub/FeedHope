package com.example.unitconverter.ReceiverInterface;

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

public class ReceiverViewProfile extends AppCompatActivity {
    private TextView reference, phone, email, pass;
    private Button update;
    private ReceiverRegisterDB registerDB;
    private ReceiverModalClass receiverModalClass;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_profile);

        reference = findViewById(R.id.name);
        phone = findViewById(R.id.phone);
        email = findViewById(R.id.email);
        pass = findViewById(R.id.pass);
        update = findViewById(R.id.update);

        registerDB = new ReceiverRegisterDB(this);

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

        receiverModalClass = registerDB.read(loggedInEmail);

        if (receiverModalClass != null) {
            reference.setText(receiverModalClass.getReference());
            phone.setText(receiverModalClass.getPhone());
            email.setText(receiverModalClass.getEmail());
            pass.setText(receiverModalClass.getPass());
        } else {
            Toast.makeText(this, "No profile found", Toast.LENGTH_SHORT).show();
        }

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReceiverViewProfile.this, ReceiverUpdateProfile.class);
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