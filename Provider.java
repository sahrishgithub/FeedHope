package com.example.feedhope;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
public class Provider extends AppCompatActivity {
    private EditText food, quantity, storage, available, expire,things;
    private Button register;
    private providerDB db;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.provider);

        food = findViewById(R.id.food);
        quantity = findViewById(R.id.quantity);
        storage = findViewById(R.id.storage);
        available = findViewById(R.id.available);
        expire = findViewById(R.id.expire);
        things = findViewById(R.id.things);
        register = findViewById(R.id.register);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db = new providerDB(Provider.this);
                SQLiteDatabase db1 = db.getWritableDatabase();
                String food1, quantity1, storage1, available1,expire1, things1;

                food1 = food.getText().toString().trim();
                quantity1 = quantity.getText().toString().trim();
                storage1 = storage.getText().toString().trim();
                available1 = available.getText().toString().trim();
                expire1 = expire.getText().toString().trim();
                things1 = things.getText().toString().trim();

                if (food1.isEmpty() || quantity1.isEmpty() || storage1.isEmpty() || available1.isEmpty() || expire1.isEmpty()) {
                    Toast.makeText(Provider.this, "All fields are required", Toast.LENGTH_LONG).show();
                }
                else if (db.insertData(food1, quantity1, storage1, available1, expire1,things1)) {
                    Toast.makeText(Provider.this, "Data Inserted", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(Provider.this, "Data not Inserted", Toast.LENGTH_LONG).show();
                }

            }
        });
    }
}