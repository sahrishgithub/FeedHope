package com.example.unitconverter.RiderInterface;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.unitconverter.AppInterface.Login;
import com.example.unitconverter.AppInterface.Registration;
import com.example.unitconverter.ProviderInterface.ProviderRegistrationActivity;
import com.example.unitconverter.R;

public class RiderRegistrationActivity extends AppCompatActivity {

    Button signup,login;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_activity_rider);
        signup = findViewById(R.id.signup);
        login = findViewById(R.id.login);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RiderRegistrationActivity.this, RiderRegister.class);
                startActivity(intent);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RiderRegistrationActivity.this, RiderLogin.class);
                startActivity(intent);
            }
        });
    }
}
