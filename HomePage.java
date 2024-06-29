package com.example.unitconverter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class HomePage extends AppCompatActivity {
    Button money_btn,overview_btn,food_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage);
        money_btn = findViewById(R.id.button1);
        food_btn = findViewById(R.id.button3);
        overview_btn = findViewById(R.id.button5);

        money_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomePage.this, Payment_Method.class);
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

        overview_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomePage.this, How_deliver_your_food.class);
                startActivity(intent);
            }
        });
    }

}