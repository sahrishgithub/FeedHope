package com.example.unitconverter;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.unitconverter.R; // Correct import statement

public class gift extends AppCompatActivity {
    private TextView head1;
    private TextView head2;
    private TextView head3;
    private TextView head4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gift);
        head1=findViewById(R.id.head1);
        head2 = findViewById(R.id.head2);
        head3 = findViewById(R.id.head3);
        head4 = findViewById(R.id.head4);
    }
}