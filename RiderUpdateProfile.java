package com.example.unitconverter.RiderInterface;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.unitconverter.R;

public class RiderUpdateProfile extends AppCompatActivity {

    private EditText nameEdit, phoneEdit, emailEdit,passEdit;
    private Button updateButton, cancelButton;
    private RiderRegisterDB registerDB;
    private RiderModalClass riderModalClass;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_profile);

        nameEdit = findViewById(R.id.name);
        phoneEdit = findViewById(R.id.phone);
        emailEdit = findViewById(R.id.email);
        passEdit = findViewById(R.id.pass);
        updateButton = findViewById(R.id.update);
        cancelButton = findViewById(R.id.cancel);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Display back button and clear default title
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");
        }

        registerDB = new RiderRegisterDB(this);
        String loggedInEmail = getIntent().getStringExtra("email");
        // Fetch the provider's data
        riderModalClass = registerDB.read(loggedInEmail);

        if (riderModalClass != null) {
            nameEdit.setText(riderModalClass.getName());
            phoneEdit.setText(riderModalClass.getPhone());
            emailEdit.setText(riderModalClass.getEmail());
            passEdit.setText(riderModalClass.getPass());
        }

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newName = nameEdit.getText().toString();
                String newPhone = phoneEdit.getText().toString();
                String newEmail = emailEdit.getText().toString();
                String newPass = passEdit.getText().toString();

                riderModalClass.setName(newName);
                riderModalClass.setPhone(newPhone);
                riderModalClass.setEmail(newEmail);
                riderModalClass.setPass(newPass);

                long result = registerDB.update(riderModalClass);

                if (result > 0) {
                    Toast.makeText(RiderUpdateProfile.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                    // Return to ViewProfile activity and refresh data
                    Intent intent = new Intent(RiderUpdateProfile.this, RiderViewProfile.class);
                    intent.putExtra("email", riderModalClass.getEmail());  // Pass updated email to refresh data
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(RiderUpdateProfile.this, "Update failed", Toast.LENGTH_SHORT).show();
                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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