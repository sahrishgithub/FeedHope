package com.example.unitconverter.AdminInterface;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.example.unitconverter.R;
import com.example.unitconverter.RiderInterface.RiderAssignDutyDB;
import com.example.unitconverter.RiderInterface.RiderRegisterDB;

public class AssignDuty extends AppCompatActivity {
    EditText username,pick,drop,date;
    Button submit_btn;
    RiderAssignDutyDB db;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.assign_duty);

        username = findViewById(R.id.name);
        pick = findViewById(R.id.pick);
        drop = findViewById(R.id.drop);
        date = findViewById(R.id.date);
        submit_btn = findViewById(R.id.submit);
        // Setup Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Display back button and clear default title
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");
        }

        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db = new RiderAssignDutyDB(AssignDuty.this);
                db.getWritableDatabase();
                String name1 = username.getText().toString().trim();
                String pick1 = pick.getText().toString().trim();
                String drop1 = drop.getText().toString().trim();
                String date1 = date.getText().toString().trim();

                if (name1.isEmpty() || pick1.isEmpty() || drop1.isEmpty() || date1.isEmpty()) {
                    Toast.makeText(AssignDuty.this, "All fields are required", Toast.LENGTH_LONG).show();
                } else if (db.assignDuty(name1, pick1, drop1, date1,"Pending")) {
                    Toast.makeText(AssignDuty.this, "Data Inserted", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(AssignDuty.this, "Data not Inserted", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed(); // Go back to the previous activity
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}