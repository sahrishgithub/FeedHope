package com.example.feedhope.AdminInterface;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.feedhope.ProviderInterface.ClothDonation.ClothDetailActivity;
import com.example.feedhope.ProviderInterface.FoodDonation.FoodDetailsActivity;
import com.example.feedhope.ProviderInterface.MedicineDonation.MedicineDetailActivity;
import com.example.feedhope.ProviderInterface.ShoeDonation.ShoeDetailActivity;
import com.example.feedhope.ProviderInterface.ToyDonation.ToyDetailActivity;
import com.example.feedhope.R;

public class DonationDetailPage extends AppCompatActivity {
    CardView food,cloth,shoe,medicine,toy;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_provider_icon);
        food = findViewById(R.id.food);
        cloth = findViewById(R.id.cloth);
        shoe = findViewById(R.id.shoe);
        medicine = findViewById(R.id.medicine);
        toy = findViewById(R.id.toy);

        food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DonationDetailPage.this, FoodDetailsActivity.class);
                startActivity(intent);
            }
        });

        cloth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DonationDetailPage.this, ClothDetailActivity.class);
                startActivity(intent);
            }
        });
        shoe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DonationDetailPage.this, ShoeDetailActivity.class);
                startActivity(intent);
            }
        });
        medicine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DonationDetailPage.this, MedicineDetailActivity.class);
                startActivity(intent);
            }
        });
        toy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DonationDetailPage.this, ToyDetailActivity.class);
                startActivity(intent);
            }
        });
    }
}
