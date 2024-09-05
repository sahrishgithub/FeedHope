package com.example.unitconverter.RiderInterface;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.unitconverter.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class RiderRegister extends AppCompatActivity {
    private EditText name,IDtype, IDNumber, hours, banking ,phone,email,pass;
    private Button register;
    private TextView login;
    private RiderRegisterDB db;
    private ArrayList<RiderModalClass> riderList;
    private SharedPreferences sharedPreferences;
    private Gson gson;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_rider);
        AutoCompleteTextView autoCompleteTextView = findViewById(R.id.days);
        String[] suggestions = getResources().getStringArray(R.array.suggestions_array);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, suggestions);
        autoCompleteTextView.setAdapter(arrayAdapter);

        name = findViewById(R.id.name);
        IDtype = findViewById(R.id.type);
        IDNumber = findViewById(R.id.number);
        hours = findViewById(R.id.hours);
        banking = findViewById(R.id.card);
        phone = findViewById(R.id.phone);
        email = findViewById(R.id.email);
        pass = findViewById(R.id.pass);
        register = findViewById(R.id.register);
        login = findViewById(R.id.login);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //for back option
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");
        }

        sharedPreferences = getSharedPreferences("riderPrefs", Context.MODE_PRIVATE);
        gson = new Gson();
        // Load previously saved data
        loadData();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name1,phone1,IDtype1, IDNumber1, hours1, banking1,autoText,email1,pass1;

                name1 = name.getText().toString().trim();
                phone1 = phone.getText().toString().trim();
                IDtype1 = IDtype.getText().toString().trim();
                IDNumber1 = IDNumber.getText().toString().trim();
                hours1 = hours.getText().toString().trim();
                autoText = autoCompleteTextView.getText().toString().trim();
                banking1 = banking.getText().toString().trim();
                email1 = email.getText().toString().trim();
                pass1 = pass.getText().toString().trim();

                if (!name1.isEmpty() && !phone1.isEmpty() && !IDtype1.isEmpty() && !IDNumber1.isEmpty() && !hours1.isEmpty() && !autoText.isEmpty() && !banking1.isEmpty() && !email1.isEmpty() && !pass1.isEmpty()) {
                    riderList.add(new RiderModalClass(name1,phone1,IDtype1,IDNumber1,hours1,autoText,banking1,email1,pass1));
                    saveData();
                    name.setText("");
                    phone.setText("");
                    IDtype.setText("");
                    IDNumber.setText("");
                    hours.setText("");
                    autoCompleteTextView.setText("");
                    banking.setText("");
                    email.setText("");
                    pass.setText("");
                }
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RiderRegister.this, RiderLogin.class);
                startActivity(intent);
            }
        });
    }
    private void loadData() {
        String json = sharedPreferences.getString("riderList", null);
        Type type = new TypeToken<ArrayList<RiderModalClass>>() {}.getType();
        riderList = gson.fromJson(json, type);

        if (riderList == null) {
            riderList = new ArrayList<>();
        }
    }
    private void saveData() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String json = gson.toJson(riderList);
        editor.putString("riderList", json);
        editor.apply();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // Handle the back button click here
                onBackPressed(); // Go back to the previous activity
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}