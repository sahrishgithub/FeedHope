package com.example.unitconverter.Payment;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.unitconverter.R;

public class DonateActivity extends AppCompatActivity {

    private EditText editTextCustomAmount;
    private Button buttonAmount1, buttonAmount2, buttonAmount3, buttonDonate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_payment);

        // Initialize UI elements
        editTextCustomAmount = findViewById(R.id.editTextCustomAmount);
        buttonAmount1 = findViewById(R.id.buttonAmount1);
        buttonAmount2 = findViewById(R.id.buttonAmount2);
        buttonAmount3 = findViewById(R.id.buttonAmount3);
        buttonDonate = findViewById(R.id.buttonDonate);

        // Set up button click listeners
        buttonAmount1.setOnClickListener(v -> setAmount("10"));
        buttonAmount2.setOnClickListener(v -> setAmount("25"));
        buttonAmount3.setOnClickListener(v -> setAmount("50"));
        buttonDonate.setOnClickListener(v -> processDonation());
    }

    private void setAmount(String amount) {
        editTextCustomAmount.setText(amount);
    }

    private void processDonation() {
        String amountString = editTextCustomAmount.getText().toString();
        if (!amountString.isEmpty()) {
            try {
                double amount = Double.parseDouble(amountString);
                // Process the donation with the amount
                Toast.makeText(this, "Donating $" + String.format("%.2f", amount), Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(DonateActivity.this,Payment_Method.class);
                startActivity(intent);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Invalid amount", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Please enter an amount", Toast.LENGTH_SHORT).show();
        }
    }
}
