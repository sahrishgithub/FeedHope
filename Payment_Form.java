package com.example.unitconverter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.braintreepayments.cardform.view.CardForm;

public class Payment_Form extends AppCompatActivity {

    CardForm cardform;
    Button btn1;
    AlertDialog.Builder alertBuilder;
//    @SuppressLint("MissingInflatedId")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_form);
        cardform = findViewById(R.id.card_form);
        btn1 = findViewById(R.id.submit);

        cardform.cardRequired(true)
                .expirationRequired(true)
                .cvvRequired(true)
                .postalCodeRequired(true)
                .mobileNumberRequired(true)
                .mobileNumberExplanation("SMS is required on this number")
                .setup(Payment_Form.this);
        cardform.getCvvEditText().setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 if(cardform.isValid()){
                    alertBuilder = new AlertDialog.Builder(Payment_Form.this);
                    alertBuilder.setTitle("Confirm before submit");
                    alertBuilder.setMessage("Card Number: " + cardform.getCardNumber() + "\n" +
                            "Card expiry date: " + cardform.getExpirationDateEditText().getText().toString() + "\n" +
                            "Card CVV: " + cardform.getCvv() + "\n" +
                            "Postal Code: " + cardform.getPostalCode() + "\n" +
                            "Phone Number: " + cardform.getMobileNumber());
                    alertBuilder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int i) {
                            dialog.dismiss();
                            Toast.makeText(Payment_Form.this, "Thank you for Donation", Toast.LENGTH_LONG).show();
                        }
                    });
                    alertBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int i) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog alertDialog = alertBuilder.create();
                    alertDialog.show();
                }else {
                     Toast.makeText(Payment_Form.this, "Please complete the form", Toast.LENGTH_LONG).show();
                 }
            }
        });
    }
}
