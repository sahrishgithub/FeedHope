package com.example.feedhope.AdminInterface;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.feedhope.R;
import com.example.feedhope.RiderInterface.Duty.DutyDB;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class RiderPaySalary extends AppCompatActivity {

    private TextView email, salary, date;
    EditText account;
    private Button pay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.salary_pay);

        // Initialize views
        email = findViewById(R.id.email);
        salary = findViewById(R.id.salary);
        account = findViewById(R.id.account);  // Account field
        date = findViewById(R.id.date);
        pay = findViewById(R.id.pay);

        // Set up the toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String currentDateTime = sdf.format(new Date());
        date.setText(currentDateTime);

        String userName = getIntent().getStringExtra("userName");
        int salaryAmount = getIntent().getIntExtra("calculatedSalary", 0);

        email.setText(userName);
        salary.setText(salaryAmount + " rupees");

        pay.setOnClickListener(v -> {
            int dutyId = getIntent().getIntExtra("DutyID", 1);
            String account1 = account.getText().toString().trim();
            if (account1.isEmpty()) {
                Toast.makeText(RiderPaySalary.this, "Account number cannot be empty.", Toast.LENGTH_SHORT).show();
                account.requestFocus();
                return;
            }
            else if (!isValidCardNumber(account1)) {
                Toast.makeText(RiderPaySalary.this, "Please enter a valid card number.", Toast.LENGTH_SHORT).show();
                account.requestFocus();
                return;
            }

            long accountNo= Long.parseLong(account1);
            DutyDB dbHandler = new DutyDB(this);
            boolean paymentSuccess = dbHandler.paySalary(dutyId, userName, salaryAmount, accountNo, currentDateTime);

            if (paymentSuccess) {
                Toast.makeText(this, "Salary paid successfully", Toast.LENGTH_SHORT).show();
                Intent resultIntent = new Intent();
                resultIntent.putExtra("paymentSuccess", true);
                setResult(RESULT_OK, resultIntent);
                finish();
            } else {
                Toast.makeText(this, "Payment failed. Try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean isValidCardNumber(String cardNumber) {
        int sum = 0;
        boolean alternate = false;
        for (int i = cardNumber.length() - 1; i >= 0; i--) {
            int n = Integer.parseInt(cardNumber.substring(i, i + 1));
            if (alternate) {
                n *= 2;
                if (n > 9) {
                    n = (n % 10) + 1;
                }
            }
            sum += n;
            alternate = !alternate;
        }
        return (sum % 10 == 0);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}