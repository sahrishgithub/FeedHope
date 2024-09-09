package com.example.unitconverter.ReceiverInterface;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.unitconverter.AdminInterface.AdminHomePage;
import com.example.unitconverter.ProviderInterface.FoodDetailsActivity;
import com.example.unitconverter.R;

import java.util.ArrayList;

public class ReceiverHomePage extends AppCompatActivity {
    private String loggedInEmail;
    TextView UserName;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage_receiver);
        loggedInEmail = getIntent().getStringExtra("email");
        UserName = findViewById(R.id.receiver);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");
        }

        ReceiverRegisterDB db = new ReceiverRegisterDB(this);
        ReceiverModalClass receiverModalClass = db.read(loggedInEmail);
        if (receiverModalClass != null) {
            UserName.setText(receiverModalClass.getReference());
        } else {
            UserName.setText("User not found");
        }

        ArrayList<InformDonationModalClass> modalClasses = new ArrayList<>();
        db = new ReceiverRegisterDB(ReceiverHomePage.this);
        modalClasses = db.readInformationData();

        // Setup RecyclerView
        InformRVAdapter rvAdapter = new InformRVAdapter(modalClasses, ReceiverHomePage.this);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ReceiverHomePage.this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(rvAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.profile) {
            Intent intent = new Intent(ReceiverHomePage.this, ReceiverViewProfile.class);
            intent.putExtra("email", loggedInEmail);
            startActivity(intent);
            return true;
        } else if (id == android.R.id.home) {
            // Handle the back button in the toolbar
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}