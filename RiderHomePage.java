package com.example.unitconverter.RiderInterface;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.unitconverter.AppInterface.gift_page;
import com.example.unitconverter.Payment.Payment_Method;
import com.example.unitconverter.ProviderInterface.ProvideFood;
import com.example.unitconverter.ProviderInterface.ProviderDB;
import com.example.unitconverter.ProviderInterface.ProviderHomePage;
import com.example.unitconverter.ProviderInterface.ProviderModalClass;
import com.example.unitconverter.ProviderInterface.ProviderViewProfile;
import com.example.unitconverter.R;

public class RiderHomePage extends AppCompatActivity {
    Button duty_btn,salary_btn;
    private String loggedInEmail;
    TextView UserName;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage_rider);
        loggedInEmail = getIntent().getStringExtra("email");

        duty_btn = findViewById(R.id.duty);
        salary_btn = findViewById(R.id.salary);
        UserName = findViewById(R.id.rider);

        // Setup Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Display back button and clear default title
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");
        }

        // Set up the buttons to navigate to different activities
        duty_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RiderHomePage.this, DutyDetailsActivity.class);
                startActivity(intent);
            }
        });

//        salary_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(RiderHomePage.this, ProvideFood.class);
//                startActivity(intent);
//            }
//        });

        // View Profile
        RiderRegisterDB db = new RiderRegisterDB(this);
        RiderModalClass riderModalClass = db.read(loggedInEmail);

        if (riderModalClass != null) {
            UserName.setText(riderModalClass.getName());
        } else {
            UserName.setText("User not found");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.profile) {
            // Pass the logged-in email to the ViewProfile activity
            Intent intent = new Intent(RiderHomePage.this, RiderViewProfile.class);
            intent.putExtra("email", loggedInEmail);
            startActivity(intent);
            return true;
        } else if (id == android.R.id.home) {
            // Handle the back button in the toolbar
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}