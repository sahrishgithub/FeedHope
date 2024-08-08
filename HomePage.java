package com.example.unitconverter;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;

public class HomePage extends AppCompatActivity {
    private signupDB dbHelper;

    Button money_btn,overview_btn,food_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage);


        money_btn = findViewById(R.id.button1);
        food_btn = findViewById(R.id.button3);
        overview_btn = findViewById(R.id.button5);
        Button gift = findViewById(R.id.giftbtn);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //for back option
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Feed Hope");
        }
        toolbar.setTitle("Feed Hope");

        money_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomePage.this, Payment_Method.class);
                startActivity(intent);
            }
        });
        gift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(HomePage.this,gift.class);
                startActivity(intent);
            }
        });

        food_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomePage.this, Provider.class);
                startActivity(intent);
            }
        });

        //logout code

        dbHelper = new signupDB(this);

        Button logoutButton = findViewById(R.id.logout_button);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
    }
    private void logout() {
        // Clear user session from SQLite database
        dbHelper.clearUserSession();

        // Redirect to LoginActivity or another appropriate screen
        Intent intent = new Intent(HomePage.this, login.class);
        startActivity(intent);

        // Finish the current activity
        finish();
    }
    //end logout code

    public boolean onCreateOptionsMenu(Menu menu) {
        new MenuInflater(this).inflate(R.menu.option_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId=item.getItemId();
        if(itemId==R.id.opt_account)
        {
            Toast.makeText(this, "Show profile", Toast.LENGTH_SHORT).show();
        } else if (itemId==R.id.opt_home) {
            Intent intent=new Intent(this,HomePage.class);
        } else if (itemId==R.id.opt_help) {
            Toast.makeText(this, "Help page", Toast.LENGTH_SHORT).show();
        } else if (itemId==R.id.opt_logout) {
            Toast.makeText(this, "logout button", Toast.LENGTH_SHORT).show();
        } else if (itemId==android.R.id.home) {
            super.onBackPressed();
        }
        else{
            Toast.makeText(this, "Map", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }

}