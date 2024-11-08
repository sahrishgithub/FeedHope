package com.example.feedhope.AppInterface;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.feedhope.R;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.Stripe;
import com.stripe.android.model.PaymentMethod;
import com.stripe.android.model.PaymentMethodCreateParams;
import com.stripe.android.ApiResultCallback;
import com.stripe.android.view.CardMultilineWidget;

public class Payment_Form extends AppCompatActivity {
    private static final String PUBLISHABLE_KEY = "pk_test_51QIE1c00zQwAWWuC74pt5M0Rie9Msf37ZperWzuWqPTQnzcBrN1TQm5oE2mAfpQj3viOWQK5PV3flfRZlWICRJ9K00AsH7zl3W"; // Use your Stripe test mode publishable key
    private Stripe stripe;
    private CardMultilineWidget cardForm;
    private Button btnPay;
    String loggedInEmail;
    private Spinner amountSpinner;
    private Payment_FormDB paymentFormDB;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_form);

        PaymentConfiguration.init(this, PUBLISHABLE_KEY);
        cardForm = findViewById(R.id.cardInputWidget);
        btnPay = findViewById(R.id.btn_pay);
        amountSpinner = findViewById(R.id.amountSpinner);
        loggedInEmail = getIntent().getStringExtra("email");

        paymentFormDB = new Payment_FormDB(this);
        String[] amounts = {"100 PKR", "500 PKR", "1000 PKR", "5000 PKR"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, amounts);
        amountSpinner.setAdapter(adapter);
        btnPay.setOnClickListener(view -> createPaymentMethod());
    }

    private void createPaymentMethod() {
        // Get the payment method details entered by the user
        PaymentMethodCreateParams params = cardForm.getPaymentMethodCreateParams();
        if (params != null) {
            stripe = new Stripe(getApplicationContext(), PUBLISHABLE_KEY);
            stripe.createPaymentMethod(params, new ApiResultCallback<PaymentMethod>() {
                @Override
                public void onSuccess(PaymentMethod paymentMethod) {
                    Toast.makeText(Payment_Form.this, "Payment Method Created Successfully", Toast.LENGTH_SHORT).show();
                    String selectedAmount = amountSpinner.getSelectedItem().toString();
                    paymentFormDB.insertData(loggedInEmail, selectedAmount);
                    finish();
                }

                @Override
                public void onError(Exception e) {
                    Toast.makeText(Payment_Form.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(Payment_Form.this, "Invalid Card Details", Toast.LENGTH_SHORT).show();
        }
    }
}