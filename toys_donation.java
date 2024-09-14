package com.example.unitconverter.AppInterface;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.unitconverter.R;

public class toys_donation extends AppCompatActivity {

    private EditText firstName, lastName, phone, email;
    private EditText city, postal;
    private TextView donateWhat;
    private CheckBox doll, plushtoys, car, musicaltoys;
    private CheckBox puzzel, yo_yos, bicycles, toy_horses;
    private CheckBox rattles, Kitchentoys, Squeeze;
    private toy_database dbHelper;

    private CheckBox lastChecked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.toys_donation);

        // Initialize UI components
        firstName = findViewById(R.id.firstname);
        lastName = findViewById(R.id.lastname);
        phone = findViewById(R.id.phone);
        email = findViewById(R.id.email);
        city = findViewById(R.id.city);
        postal = findViewById(R.id.postalcode);
        donateWhat = findViewById(R.id.typeOfDonation);
        doll = findViewById(R.id.checkbox1);
        plushtoys = findViewById(R.id.checkbox2);
        car = findViewById(R.id.checkbox3);
        musicaltoys = findViewById(R.id.checkbox4);
        puzzel = findViewById(R.id.checkbox5);
        yo_yos = findViewById(R.id.checkbox6);
        bicycles = findViewById(R.id.checkbox7);
        toy_horses = findViewById(R.id.checkbox8);
        rattles = findViewById(R.id.checkbox9);
        Kitchentoys = findViewById(R.id.checkbox10);
        Squeeze = findViewById(R.id.checkbox11);

        dbHelper = new toy_database(this);

        // Set checkbox listeners
        setCheckBoxListener(doll);
        setCheckBoxListener(plushtoys);
        setCheckBoxListener(car);
        setCheckBoxListener(musicaltoys);
        setCheckBoxListener(puzzel);
        setCheckBoxListener(yo_yos);
        setCheckBoxListener(bicycles);
        setCheckBoxListener(toy_horses);
        setCheckBoxListener(rattles);
        setCheckBoxListener(Kitchentoys);
        setCheckBoxListener(Squeeze);

        findViewById(R.id.submitButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitForm();
            }
        });
    }

    private void setCheckBoxListener(final CheckBox checkBox) {
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lastChecked != null && lastChecked != checkBox) {
                    lastChecked.setChecked(false);
                }
                lastChecked = checkBox;
            }
        });
    }

    private void submitForm() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("first_name", firstName.getText().toString());
        values.put("last_name", lastName.getText().toString());
        values.put("phone", phone.getText().toString());
        values.put("email", email.getText().toString());
        values.put("city", city.getText().toString());
        values.put("postal_code", postal.getText().toString());
        values.put("donate_what", donateWhat.getText().toString());

        // Handle checkboxes
        values.put("doll", doll.isChecked() ? 1 : 0);
        values.put("plush_toys", plushtoys.isChecked() ? 1 : 0);
        values.put("car", car.isChecked() ? 1 : 0);
        values.put("musical_toys", musicaltoys.isChecked() ? 1 : 0);
        values.put("puzzles", puzzel.isChecked() ? 1 : 0);
        values.put("yo_yoys", yo_yos.isChecked() ? 1 : 0);
        values.put("bicycles", bicycles.isChecked() ? 1 : 0);
        values.put("toy_horses", toy_horses.isChecked() ? 1 : 0);
        values.put("rattles", rattles.isChecked() ? 1 : 0);
        values.put("kitchen_toys", Kitchentoys.isChecked() ? 1 : 0);
        values.put("squeeze_and_squeak_toys", Squeeze.isChecked() ? 1 : 0);

        long newRowId = db.insert("donations", null, values);
        if (newRowId != -1) {
            Toast.makeText(this, "Donation submitted successfully!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Error submitting donation.", Toast.LENGTH_SHORT).show();
        }
    }

}
