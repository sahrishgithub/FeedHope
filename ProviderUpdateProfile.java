package com.example.feedhope.ProviderInterface;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.example.feedhope.ProviderInterface.ProviderRegister.ProviderDB;
import com.example.feedhope.ProviderInterface.ProviderRegister.ProviderModalClass;
import com.example.feedhope.R;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

public class ProviderUpdateProfile extends AppCompatActivity {

    private EditText name, phone,pass;
    private Button update, cancel;
    private ProviderDB registerDB;
    private ProviderModalClass modalClass;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_update);

        name = findViewById(R.id.name);
        phone = findViewById(R.id.phone);
        pass = findViewById(R.id.pass);
        update = findViewById(R.id.update);
        cancel = findViewById(R.id.cancel);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");
        }

        registerDB = new ProviderDB(this);
        String loggedInEmail = getIntent().getStringExtra("email");

        modalClass = registerDB.read(loggedInEmail);

        if (modalClass != null) {
            name.setText(modalClass.getName());
            phone.setText(modalClass.getPhone());
            pass.setText(modalClass.getPass());
        }

        phone.addTextChangedListener(new TextWatcher() {
            private boolean isUpdating = false;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Do nothing here
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (isUpdating) return;
                isUpdating = true;
                String text = s.toString();
                // Ensure the prefix is "+92"
                if (!text.startsWith("+92")) {
                    text = "+92" + text.replace("+92", ""); // Remove any duplicate "+92"
                }
                // Remove invalid characters and ensure it starts with "+923"
                if (text.length() > 3) {
                    String digits = text.substring(3).replaceAll("[^0-9]", ""); // Extract only digits
                    // Enforce that the first digit after "+92" must be "3"
                    if (digits.startsWith("0")) {
                        digits = digits.substring(1); // Remove the leading "0"
                    }
                    if (!digits.startsWith("3")) {
                        digits = "3" + digits.replaceFirst("^\\d*", ""); // Ensure it starts with "3"
                    }
                    digits = digits.length() > 10 ? digits.substring(0, 10) : digits; // Limit to 9 digits
                    text = "+92" + digits;
                }
                phone.setText(text);
                phone.setSelection(text.length()); // Move cursor to the end
                isUpdating = false;
            }
            @Override
            public void afterTextChanged(Editable s) {
                // Do nothing here
            }
        });


        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newName = name.getText().toString();
                String newPhone = phone.getText().toString();
                String newPass = pass.getText().toString();

                if (newPass.length() != 8) {
                    Toast.makeText(ProviderUpdateProfile.this, "Password must be at least 8 characters long", Toast.LENGTH_SHORT).show();
                    pass.requestFocus();
                    return;
                }
                if (!isValidPhoneNumber(newPhone)) {
                    Toast.makeText(ProviderUpdateProfile.this, "Please enter a valid phone number.", Toast.LENGTH_SHORT).show();
                    phone.requestFocus();
                    return;
                }

                modalClass.setName(newName);
                modalClass.setPhone(newPhone);
                modalClass.setPass(newPass);

                long result = registerDB.update(modalClass);

                if (result > 0) {
                    Toast.makeText(ProviderUpdateProfile.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ProviderUpdateProfile.this, ProviderUpdateProfile.class);
                    intent.putExtra("email", modalClass.getEmail());  // Pass updated email to refresh data
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(ProviderUpdateProfile.this, "Update failed", Toast.LENGTH_SHORT).show();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private boolean isValidPhoneNumber(String phoneNumber) {
        PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
        try {
            Phonenumber.PhoneNumber number = phoneNumberUtil.parse(phoneNumber, "Pakistan");
            return phoneNumberUtil.isValidNumber(number);
        } catch (Exception e) {
            Log.e("PhoneValidation", "Invalid phone number: " + phoneNumber, e);
            return false;
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}