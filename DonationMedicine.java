package com.example.unitconverter.ProviderInterface;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.unitconverter.R;

public class DonationMedicine extends AppCompatActivity {

    private EditText firstName, lastName, email, streetAddress, city, postalCode, phoneNumber, description, comment;
    private Spinner equipmentSpinner;
    private Button submitButton;

    private MedicinDB medicinDB;  // Database helper

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.donate_medicine);

        // Initialize UI components
        firstName = findViewById(R.id.firstname);
        lastName = findViewById(R.id.lastname);
        email = findViewById(R.id.email);
        streetAddress = findViewById(R.id.streetaddress);
        city = findViewById(R.id.city);
        postalCode = findViewById(R.id.postalcode);
        phoneNumber = findViewById(R.id.phonenumber);
        description = findViewById(R.id.description);
        equipmentSpinner = findViewById(R.id.equipmentSpinner);
        submitButton = findViewById(R.id.MedicinDonation);
        comment = findViewById(R.id.comment);

        // Initialize database helper
        medicinDB = new MedicinDB(this);

        // Populate equipment spinner with common medical equipment options
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.equipment_list, android.R.layout.simple_spinner_dropdown_item);
        equipmentSpinner.setAdapter(adapter);

        // Initially disable the submit button
        submitButton.setEnabled(false);

        // Add TextChangeListeners to check form fields
        addTextChangeListeners();

        // Set click listener for submit button
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveDonationToDatabase();
            }
        });
    }

    private void addTextChangeListeners() {
        // Add listeners for the fields to enable/disable the submit button
        firstName.addTextChangedListener(new SimpleTextWatcher());
        lastName.addTextChangedListener(new SimpleTextWatcher());
        email.addTextChangedListener(new SimpleTextWatcher());
        streetAddress.addTextChangedListener(new SimpleTextWatcher());
        city.addTextChangedListener(new SimpleTextWatcher());
        postalCode.addTextChangedListener(new SimpleTextWatcher());
        phoneNumber.addTextChangedListener(new SimpleTextWatcher());
        description.addTextChangedListener(new SimpleTextWatcher());
        comment.addTextChangedListener(new SimpleTextWatcher());
    }

    private boolean areAllFieldsFilled() {
        // Check if all the required fields are filled
        return !firstName.getText().toString().trim().isEmpty() &&
                !lastName.getText().toString().trim().isEmpty() &&
                !email.getText().toString().trim().isEmpty() &&
                !streetAddress.getText().toString().trim().isEmpty() &&
                !city.getText().toString().trim().isEmpty() &&
                !postalCode.getText().toString().trim().isEmpty() &&
                !phoneNumber.getText().toString().trim().isEmpty() &&
                !description.getText().toString().trim().isEmpty() &&
                !comment.getText().toString().trim().isEmpty() &&
                equipmentSpinner.getSelectedItemPosition() != 0; // Ensure equipment is selected
    }

    private void saveDonationToDatabase() {
        // Retrieve form data
        String firstNameValue = firstName.getText().toString().trim();
        String lastNameValue = lastName.getText().toString().trim();
        String emailValue = email.getText().toString().trim();
        String streetAddressValue = streetAddress.getText().toString().trim();
        String cityValue = city.getText().toString().trim();
        String postalCodeValue = postalCode.getText().toString().trim();
        String phoneNumberValue = phoneNumber.getText().toString().trim();
        String descriptionValue = description.getText().toString().trim();
        String commentsValue = comment.getText().toString().trim();
        String equipmentType = equipmentSpinner.getSelectedItem().toString();

        // Call the insertData method with all required fields
        boolean isInserted = medicinDB.insertData(
                firstNameValue, lastNameValue, emailValue, streetAddressValue,
                cityValue, postalCodeValue, phoneNumberValue, equipmentType,
                descriptionValue, commentsValue
        );

        if (isInserted) {
            Toast.makeText(this, "Donation recorded successfully!", Toast.LENGTH_SHORT).show();
            clearForm();
        } else {
            Toast.makeText(this, "Failed to record donation. Try again.", Toast.LENGTH_SHORT).show();
        }
    }

    private void clearForm() {
        firstName.setText("");
        lastName.setText("");
        email.setText("");
        streetAddress.setText("");
        city.setText("");
        postalCode.setText("");
        phoneNumber.setText("");
        description.setText("");
        equipmentSpinner.setSelection(0);
        comment.setText("");  // Clear the comment field
    }

    private class SimpleTextWatcher implements android.text.TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
            // Enable the submit button if all fields are filled
            if (areAllFieldsFilled()) {
                submitButton.setEnabled(true);
            } else {
                submitButton.setEnabled(false);
            }
        }

        @Override
        public void afterTextChanged(android.text.Editable editable) {
        }
    }
}