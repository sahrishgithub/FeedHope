package com.example.unitconverter.ReceiverInterface;

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

public class ReceiverUpdateProfile extends AppCompatActivity {

    private EditText referenceEdit, phoneEdit, emailEdit,passEdit;
    private Button updateButton, cancelButton;
    private ReceiverRegisterDB registerDB;
    private ReceiverModalClass receiverModalClass;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_profile);

        referenceEdit = findViewById(R.id.name);
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

        registerDB = new ReceiverRegisterDB(this);
        String loggedInEmail = getIntent().getStringExtra("email");
        // Fetch the provider's data
        receiverModalClass = registerDB.read(loggedInEmail);

        if (receiverModalClass != null) {
            referenceEdit.setText(receiverModalClass.getReference());
            phoneEdit.setText(receiverModalClass.getPhone());
            emailEdit.setText(receiverModalClass.getEmail());
            passEdit.setText(receiverModalClass.getPass());
        }

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newReference = referenceEdit.getText().toString();
                String newPhone = phoneEdit.getText().toString();
                String newEmail = emailEdit.getText().toString();
                String newPass = passEdit.getText().toString();

                receiverModalClass.setReference(newReference);
                receiverModalClass.setPhone(newPhone);
                receiverModalClass.setEmail(newEmail);
                receiverModalClass.setPass(newPass);

                long result = registerDB.update(receiverModalClass);

                if (result > 0) {
                    Toast.makeText(ReceiverUpdateProfile.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                    // Return to ViewProfile activity and refresh data
                    Intent intent = new Intent(ReceiverUpdateProfile.this, ReceiverViewProfile.class);
                    intent.putExtra("email", receiverModalClass.getEmail());  // Pass updated email to refresh data
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(ReceiverUpdateProfile.this, "Update failed", Toast.LENGTH_SHORT).show();
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