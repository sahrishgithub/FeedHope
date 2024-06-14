package com.example.feedhope;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Rider extends AppCompatActivity {
    private EditText IDtype, IDNumber, hours, banking;
    private Button register;
    private RiderDB db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rider);
        AutoCompleteTextView autoCompleteTextView = findViewById(R.id.days);
        String[] suggestions = getResources().getStringArray(R.array.suggestions_array);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, suggestions);
        autoCompleteTextView.setAdapter(arrayAdapter);

        IDtype = findViewById(R.id.type);
        IDNumber = findViewById(R.id.number);
        hours = findViewById(R.id.hours);
        banking = findViewById(R.id.card);
        register = findViewById(R.id.register);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db = new RiderDB(Rider.this);
                SQLiteDatabase db1 = db.getWritableDatabase();
                String IDtype1, IDNumber1, hours1, banking1,autoText;

                IDtype1 = IDtype.getText().toString().trim();
                IDNumber1 = IDNumber.getText().toString().trim();
                hours1 = hours.getText().toString().trim();
                autoText = autoCompleteTextView.getText().toString().trim();
                banking1 = banking.getText().toString().trim();

                if (IDtype1.isEmpty() || IDNumber1.isEmpty() || hours1.isEmpty() || autoText.isEmpty() || banking1.isEmpty()) {
                    Toast.makeText(Rider.this, "All fields are required", Toast.LENGTH_LONG).show();
                } else if (db.insertData(IDtype1, IDNumber1, hours1, autoText, banking1)) {
                    Toast.makeText(Rider.this, "Data Inserted", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(Rider.this, "Data not Inserted", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}