package com.example.unitconverter.ProviderInterface;

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
import com.example.unitconverter.R;

public class ProviderHomePage extends AppCompatActivity {
    Button money_btn, food_btn, gift_btn;
    private String loggedInEmail;
    TextView UserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage_provider);
        loggedInEmail = getIntent().getStringExtra("email");

        money_btn = findViewById(R.id.money);
        gift_btn = findViewById(R.id.gift);
        food_btn = findViewById(R.id.food);
        UserName = findViewById(R.id.provider);

        // Setup Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Display back button and clear default title
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");
        }

        // Set up the buttons to navigate to different activities
        money_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProviderHomePage.this, Payment_Method.class);
                startActivity(intent);
            }
        });

        gift_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProviderHomePage.this, gift_page.class);
                startActivity(intent);
            }
        });

        food_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProviderHomePage.this, ProvideFood.class);
                startActivity(intent);
            }
        });

        // Fetch the user's name from the database based on the logged-in email
        ProviderDB registerDB = new ProviderDB(this);
        ProviderModalClass provider = registerDB.read(loggedInEmail);

        if (provider != null) {
            // Set the user's name in the TextView
            UserName.setText(provider.getName());
        } else {
            // Handle the case where the user is not found
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
            Intent intent = new Intent(ProviderHomePage.this, ProviderViewProfile.class);
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