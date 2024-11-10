package com.example.feedhope.ProviderInterface.ClothDonation;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.feedhope.ProviderInterface.ProviderDB;
import com.example.feedhope.R;

import java.util.ArrayList;

public class ClothDetailActivity extends AppCompatActivity {

    private ArrayList<ClothModelClass> modalClasses;
    private ClothDB dbHandler;
    private ClothRVAdapter rvAdapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rv_cloth);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");
        }

        modalClasses = new ArrayList<>();
        dbHandler = new ClothDB(ClothDetailActivity.this);
        String loggedInUserName = getIntent().getStringExtra("email");
        modalClasses = dbHandler.readClothData();
        rvAdapter = new ClothRVAdapter(modalClasses, ClothDetailActivity.this);
        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ClothDetailActivity.this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(rvAdapter);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}