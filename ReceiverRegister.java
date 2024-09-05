package com.example.unitconverter.ReceiverInterface;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.unitconverter.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class ReceiverRegister extends AppCompatActivity {
    private EditText member, refrence, type, requirement, time, phone, email, pass;
    private Button register;
    private TextView login;
    private ArrayList<ReceiverModalClass> receiverList;
    private SharedPreferences sharedPreferences;
    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_receiver);

        refrence = findViewById(R.id.reference);
        type = findViewById(R.id.type);
        member = findViewById(R.id.member);
        requirement = findViewById(R.id.requirement);
        RadioGroup radioGroup = findViewById(R.id.radio_btn);
        time = findViewById(R.id.time);
        phone = findViewById(R.id.phone);
        email = findViewById(R.id.email);
        pass = findViewById(R.id.pass);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //for back option
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");
        }

        register = findViewById(R.id.register);
        login = findViewById(R.id.login);
        sharedPreferences = getSharedPreferences("receiverPrefs", Context.MODE_PRIVATE);
        gson = new Gson();
        loadData();

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton selectedRadioButton = findViewById(checkedId);
                String selectedText = selectedRadioButton.getText().toString();
                Toast.makeText(ReceiverRegister.this, "Selected: " + selectedText, Toast.LENGTH_SHORT).show();

                register.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String refrence1, type1, member1, requirement1, frequency1, time1, phone1, email1, pass1;

                        refrence1 = refrence.getText().toString().trim();
                        type1 = type.getText().toString().trim();
                        member1 = member.getText().toString().trim();
                        requirement1 = requirement.getText().toString().trim();
                        frequency1 = selectedText.trim();
                        time1 = time.getText().toString().trim();
                        phone1 = phone.getText().toString().trim();
                        email1 = email.getText().toString().trim();
                        pass1 = pass.getText().toString().trim();

                        if (!refrence1.isEmpty() && !type1.isEmpty() && !member1.isEmpty() && !requirement1.isEmpty() && !frequency1.isEmpty() && !time1.isEmpty() && !phone1.isEmpty() && !email1.isEmpty() && !pass1.isEmpty()) {
                            receiverList.add(new ReceiverModalClass(refrence1, type1, member1, requirement1, frequency1, time1, phone1, email1, pass1));
                            saveData();
                            refrence.setText("");
                            type.setText("");
                            member.setText("");
                            requirement.setText("");
                            time.setText("");
                            phone.setText("");
                            email.setText("");
                            pass.setText("");
                            Toast.makeText(ReceiverRegister.this, "Data saved successfully!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ReceiverRegister.this, "All fields must be filled", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                login.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ReceiverRegister.this, ReceiverLogin.class);
                        startActivity(intent);
                    }
                });
            }
        });
    }

    private void loadData() {
        String json = sharedPreferences.getString("receiverList", null);
        Type type = new TypeToken<ArrayList<ReceiverModalClass>>() {}.getType();
        receiverList = gson.fromJson(json, type);

        if (receiverList == null) {
            receiverList = new ArrayList<>();
        }
        // Log data loaded
        Log.d("ReceiverRegister", "Loaded provider list size: " + receiverList.size());
    }

    private void saveData() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String json = gson.toJson(receiverList);
        editor.putString("receiverList", json);
        editor.apply();
        // Log data saved
        Log.d("ReceiverRegister", "Data saved: " + json);
    }
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