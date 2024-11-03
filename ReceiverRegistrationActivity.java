package com.example.unitconverter.ReceiverInterface;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.unitconverter.ProviderInterface.ProviderLogin;
import com.example.unitconverter.ProviderInterface.ProviderRegistrationActivity;
import com.example.unitconverter.R;

public class ReceiverRegistrationActivity extends AppCompatActivity {

    Button signup;
    TextView login;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_activity_receiver);
        signup = findViewById(R.id.signup);
        login = findViewById(R.id.login);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReceiverRegistrationActivity.this, ReceiverRegister.class);
                startActivity(intent);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReceiverRegistrationActivity.this, ReceiverLogin.class);
                startActivity(intent);
            }
        });
    }
}
