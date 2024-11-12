package com.example.feedhope.AdminInterface;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.feedhope.R;

public class ReceiverReport extends AppCompatActivity {
    Button food,gift,money;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.receiver_report);
        food = findViewById(R.id.food);
        gift = findViewById(R.id.gift);
        money = findViewById(R.id.money);

        food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReceiverReport.this,FoodInformReport.class);
                startActivity(intent);
            }
        });

        gift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReceiverReport.this,GiftInformReport.class);
                startActivity(intent);
            }
        });

//        money.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(ReceiverReport.this,FoodInformReport.class);
//                startActivity(intent);
//            }
//        });
    }
}
