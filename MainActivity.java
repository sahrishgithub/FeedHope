package com.example.unitconverter.AppInterface;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.unitconverter.AdminInterface.AdminRegistrationActivity;
import com.example.unitconverter.ProviderInterface.ProviderRegistrationActivity;
import com.example.unitconverter.R;
import com.example.unitconverter.ReceiverInterface.ReceiverRegistrationActivity;
import com.example.unitconverter.RiderInterface.RiderRegistrationActivity;

public class MainActivity extends AppCompatActivity {
    LinearLayout admin,provider,rider,receiver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        admin = findViewById(R.id.admin);
        provider = findViewById(R.id.provider);
        rider = findViewById(R.id.rider);
        receiver = findViewById(R.id.receiver);

        admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AdminRegistrationActivity.class);
                startActivity(intent);
            }
        });

        provider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ProviderRegistrationActivity.class);
                startActivity(intent);
            }
        });

        rider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RiderRegistrationActivity.class);
                startActivity(intent);
            }
        });

        receiver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ReceiverRegistrationActivity.class);
                startActivity(intent);
            }
        });
    }
}
