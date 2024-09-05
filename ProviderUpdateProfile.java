package com.example.unitconverter.ProviderInterface;

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

public class ProviderUpdateProfile extends AppCompatActivity {

    private EditText nameEdit, phoneEdit, emailEdit,passEdit;
    private Button updateButton, cancelButton;
    private ProviderDB registerDB;
    private ProviderModalClass provider;

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

        registerDB = new ProviderDB(this);
        String loggedInEmail = getIntent().getStringExtra("email");
        // Fetch the provider's data
        provider = registerDB.read(loggedInEmail);

        if (provider != null) {
            nameEdit.setText(provider.getName());
            phoneEdit.setText(provider.getPhone());
            emailEdit.setText(provider.getEmail());
            passEdit.setText(provider.getPass());
        }

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newName = nameEdit.getText().toString();
                String newPhone = phoneEdit.getText().toString();
                String newEmail = emailEdit.getText().toString();
                String newPass = passEdit.getText().toString();

                provider.setName(newName);
                provider.setPhone(newPhone);
                provider.setEmail(newEmail);
                provider.setPass(newPass);

                long result = registerDB.update(provider);

                if (result > 0) {
                    Toast.makeText(ProviderUpdateProfile.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                    // Return to ViewProfile activity and refresh data
                    Intent intent = new Intent(ProviderUpdateProfile.this, ProviderViewProfile.class);
                    intent.putExtra("email", provider.getEmail());  // Pass updated email to refresh data
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(ProviderUpdateProfile.this, "Update failed", Toast.LENGTH_SHORT).show();
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