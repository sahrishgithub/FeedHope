package com.example.feedhope.ProviderInterface;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.feedhope.ProviderInterface.ClothDonation.ClothForm;
import com.example.feedhope.ProviderInterface.MedicineDonation.MedicineForm;
import com.example.feedhope.ProviderInterface.ShoeDonation.ShoeForm;
import com.example.feedhope.ProviderInterface.ToyDonation.ToyForm;
import com.example.feedhope.R;

public class DonationGiftPage extends AppCompatActivity {
    LinearLayout cloth,shoe,toys,medicine;
    private String loggedInEmail;
    ImageView back;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.donate_gift_page);
        cloth=findViewById(R.id.cloth);
        shoe=findViewById(R.id.shoe);
        toys=findViewById(R.id.toy);
        medicine=findViewById(R.id.medicine);
        back = findViewById(R.id.back_arrow);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        loggedInEmail = getIntent().getStringExtra("email");

        shoe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(DonationGiftPage.this, ShoeForm.class);
                intent.putExtra("email", loggedInEmail);
                startActivity(intent);
            }
        });
        cloth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(DonationGiftPage.this, ClothForm.class);
                intent.putExtra("email", loggedInEmail);
                startActivity(intent);
            }
        });
        medicine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(DonationGiftPage.this, MedicineForm.class);
                intent.putExtra("email", loggedInEmail);
                startActivity(intent);
            }
        });
        toys.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(DonationGiftPage.this, ToyForm.class);
                intent.putExtra("email", loggedInEmail);
                startActivity(intent);
            }
        });
    }
}