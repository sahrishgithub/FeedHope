package com.example.unitconverter.ProviderInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import com.example.unitconverter.AppInterface.Login;
import com.example.unitconverter.AppInterface.Registration;
import com.example.unitconverter.R;

public class ProviderRegistrationActivity extends AppCompatActivity {
    Button btn1, btn2;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_activity_provider);
        btn1 = findViewById(R.id.signup);
        btn2 = findViewById(R.id.login);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProviderRegistrationActivity.this, Registration.class);
                startActivity(intent);
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProviderRegistrationActivity.this, Login.class);
                startActivity(intent);
            }
        });
    }
}