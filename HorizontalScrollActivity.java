package com.example.unitconverter;

import android.os.Bundle;
import android.widget.ViewFlipper;
import androidx.appcompat.app.AppCompatActivity;

public class HorizontalScrollActivity extends AppCompatActivity {

    private ViewFlipper flipper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gift_page);

        // Initialize ViewFlipper
        flipper = findViewById(R.id.fliper);
        flipper.setFlipInterval(2000); // flip interval to 2000 milliseconds
        flipper.startFlipping();
    }
}
