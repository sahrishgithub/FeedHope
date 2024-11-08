package com.example.feedhope.ReceiverInterface;

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
import com.example.feedhope.R;
import java.util.ArrayList;

public class ReceiverHomePage extends AppCompatActivity {
    private String loggedInEmail;
    TextView UserName;

    @Override
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

        // Load data into the RecyclerView
        ArrayList<InformDonationModalClass> modalClasses = db.readInformationData();

        // Pass loggedInEmail to the RecyclerView adapter
        InformRVAdapter rvAdapter = new InformRVAdapter(modalClasses, this, loggedInEmail);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
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
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}