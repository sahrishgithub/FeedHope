package com.example.feedhope;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
public class Needy extends AppCompatActivity {
    private EditText member, refrence, type, requirement, time;
    private Button register;
    private NeedyDB db;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.needy);

        RadioGroup radioGroup = findViewById(R.id.radio_btn);
        member = findViewById(R.id.member);
        refrence = findViewById(R.id.refrence);
        type = findViewById(R.id.type);
        requirement = findViewById(R.id.requirement);
        time = findViewById(R.id.time);
        register = findViewById(R.id.register);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton selectedRadioButton = findViewById(checkedId);
                String selectedText = selectedRadioButton.getText().toString();
                Toast.makeText(Needy.this, "Selected: " + selectedText, Toast.LENGTH_SHORT).show();

                register.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        db = new NeedyDB(Needy.this);
                        SQLiteDatabase db1 = db.getWritableDatabase();
                        String member1, refrence1, type1, requirement1, time1;

                        member1 = member.getText().toString().trim();
                        refrence1 = refrence.getText().toString().trim();
                        type1 = type.getText().toString().trim();
                        requirement1 = requirement.getText().toString().trim();
                        time1 = time.getText().toString().trim();

                        if (member1.isEmpty() || refrence1.isEmpty() || type1.isEmpty() || requirement1.isEmpty() || time1.isEmpty()) {
                            Toast.makeText(Needy.this, "All fields are required", Toast.LENGTH_LONG).show();
                        } else if (db.insertData(member1, refrence1, type1, requirement1, selectedText, time1)) {
                            Toast.makeText(Needy.this, "Data Inserted", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(Needy.this, "Data not Inserted", Toast.LENGTH_LONG).show();
                        }

                    }
                });
            }
        });
    }
}