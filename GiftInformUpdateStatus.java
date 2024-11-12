package com.example.feedhope.ReceiverInterface.GiftInform;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.example.feedhope.R;

public class GiftInformUpdateStatus extends AppCompatActivity {
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

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedStatus = status.getSelectedItem().toString();
                String Name = getIntent().getStringExtra("email");
                GiftInformDB db = new GiftInformDB(GiftInformUpdateStatus.this);
                boolean result = db.updateGiftStatus(Name, selectedStatus);
                if (selectedStatus.equals("Select Status")) {
                    Toast.makeText(GiftInformUpdateStatus.this, "Please select a valid Status", Toast.LENGTH_SHORT).show();
                }else if (result) {
                    Toast.makeText(GiftInformUpdateStatus.this, "Status updated", Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    Toast.makeText(GiftInformUpdateStatus.this, "Failed to update status", Toast.LENGTH_LONG).show();
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