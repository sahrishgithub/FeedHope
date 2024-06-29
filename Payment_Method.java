package com.example.unitconverter;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Payment_Method extends AppCompatActivity {
    EditText editText;
    Button btn1;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_methods);
        editText=findViewById(R.id.add1);
        btn1=findViewById(R.id.button1);

        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Payment_Method.this, Add_Payment.class);
                startActivity(intent);
            }
        });

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               String add = editText.getText().toString().trim();
                if (add.isEmpty()) {
                    Toast.makeText(Payment_Method.this, "Add Payment Method", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(Payment_Method.this, Add_Payment.class);
                    startActivity(intent);
                }
            }
        });
//
//        Spannable text1 = new SpannableString("  No payment method connected");
//        text1.setSpan(new ForegroundColorSpan(Color.BLACK),0,text1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        editText.setText(text1);
//
//        Spannable text2 = new SpannableString(" + Add");
//        text2.setSpan(new ForegroundColorSpan(Color.BLUE),1,text2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        editText.setText(text2);
//
    }
}
