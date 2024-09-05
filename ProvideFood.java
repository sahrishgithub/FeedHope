package com.example.unitconverter.ProviderInterface;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.unitconverter.R;

public class ProvideFood extends AppCompatActivity {
    private EditText food, quantity, storage, available, expire;
    private Button register;
    private ProviderDB db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.donate_food);

        food = findViewById(R.id.food);
        quantity = findViewById(R.id.quantity);
        storage = findViewById(R.id.storage);
        available = findViewById(R.id.available);
        expire = findViewById(R.id.expire);
        register = findViewById(R.id.register);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Display back button and clear default title
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");
        }

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db = new ProviderDB(ProvideFood.this);
                db.getWritableDatabase();
                String food1 = food.getText().toString().trim();
                String quantity1 = quantity.getText().toString().trim();
                String storage1 = storage.getText().toString().trim();
                String available1 = available.getText().toString().trim();
                String expire1 = expire.getText().toString().trim();

                if (food1.isEmpty() || quantity1.isEmpty() || storage1.isEmpty() || available1.isEmpty() || expire1.isEmpty()) {
                    Toast.makeText(ProvideFood.this, "All fields are required", Toast.LENGTH_LONG).show();
                } else if (db.insertData(food1, quantity1, storage1, available1, expire1)) {
                    Toast.makeText(ProvideFood.this, "Data Inserted", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(ProvideFood.this, "Data not Inserted", Toast.LENGTH_LONG).show();
                }
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