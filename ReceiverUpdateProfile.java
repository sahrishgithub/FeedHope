package com.example.feedhope.ReceiverInterface;

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
import com.example.feedhope.R;
import com.example.feedhope.ReceiverInterface.FoodInform.FoodInformDB;
import com.example.feedhope.ReceiverInterface.ReceiverRegister.ReceiverModalClass;
import com.example.feedhope.ReceiverInterface.ReceiverRegister.ReceiverRegisterDB;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

public class ReceiverUpdateProfile extends AppCompatActivity {

    private EditText name, phone,pass;
    private Button update, cancel;
    private ReceiverRegisterDB registerDB;
    private ReceiverModalClass receiverModalClass;

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

        registerDB = new ReceiverRegisterDB(this);
        String loggedInEmail = getIntent().getStringExtra("email");

        receiverModalClass = registerDB.read(loggedInEmail);

        if (receiverModalClass != null) {
            name.setText(receiverModalClass.getReference());
            phone.setText(receiverModalClass.getPhone());
            pass.setText(receiverModalClass.getPass());
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
                    Toast.makeText(ReceiverUpdateProfile.this, "Password must be at least 8 characters long", Toast.LENGTH_SHORT).show();
                    pass.requestFocus();
                    return;
                }
                if (!isValidPhoneNumber(newPhone)) {
                    Toast.makeText(ReceiverUpdateProfile.this, "Please enter a valid phone number.", Toast.LENGTH_SHORT).show();
                    phone.requestFocus();
                    return;
                }

                receiverModalClass.setReference(newName);
                receiverModalClass.setPhone(newPhone);
                receiverModalClass.setPass(newPass);

                long result = registerDB.update(receiverModalClass);

                if (result > 0) {
                    Toast.makeText(ReceiverUpdateProfile.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ReceiverUpdateProfile.this, ReceiverViewProfile.class);
                    intent.putExtra("email", receiverModalClass.getEmail());  // Pass updated email to refresh data
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(ReceiverUpdateProfile.this, "Update failed", Toast.LENGTH_SHORT).show();
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