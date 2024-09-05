package com.example.unitconverter.RiderInterface;

import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.unitconverter.R;

public class Update_Status extends AppCompatActivity {
    private Spinner status;
    Button submit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_status);
        status = findViewById(R.id.status);
        submit = findViewById(R.id.submit);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");
        }
// Drop Down Color Change
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.status)) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                // Customize the selected item (the view shown on the spinner when it is not dropped down)
                View view = super.getView(position, convertView, parent);
                TextView textView = (TextView) view;

                // Change color based on selection
                String selectedItem = getItem(position);
                if ("Pending".equals(selectedItem)) {
                    textView.setTextColor(Color.RED);  // Set text color to red for Pending
                } else if ("Completed".equals(selectedItem)) {
                    textView.setTextColor(Color.GREEN);  // Set text color to green for Completed
                } else {
                    textView.setTextColor(Color.BLACK);  // Default color
                }
                return view;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                // Customize the drop-down items (when the spinner is clicked and options are shown)
                View view = super.getDropDownView(position, convertView, parent);
                TextView textView = (TextView) view;

                // Change color based on selection
                String selectedItem = getItem(position);
                if ("Pending".equals(selectedItem)) {
                    textView.setTextColor(Color.RED);  // Set drop-down text color to red for Pending
                } else if ("Completed".equals(selectedItem)) {
                    textView.setTextColor(Color.GREEN);  // Set drop-down text color to green for Completed
                } else {
                    textView.setTextColor(Color.BLACK);  // Default color
                }
                return view;
            }
        };

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        status.setAdapter(adapter);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedStatus = status.getSelectedItem().toString();
                String userEmail = getIntent().getStringExtra("email");

                RiderAssignDutyDB db = new RiderAssignDutyDB(Update_Status.this);
                if (db.updateDutyStatus(userEmail, selectedStatus)) {
                    Toast.makeText(Update_Status.this, "Status updated", Toast.LENGTH_LONG).show();
                    finish();  // Go back to the previous activity
                } else {
                    Toast.makeText(Update_Status.this, "Failed to update status", Toast.LENGTH_LONG).show();
                }
                status.setSelection(0);
            }
        });
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