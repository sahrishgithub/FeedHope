package com.example.feedhope.ReceiverInterface.GiftInform;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.feedhope.R;
import java.util.ArrayList;

public class GiftInformDetail extends AppCompatActivity {

    private ArrayList<GiftInformModalClass> modalClasses;
    private GiftInformDB dbHandler;
    private GiftInformRVAdapter rvAdapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rv_gift_detail);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");
        }

        SharedPreferences sharedPreferences = getSharedPreferences("FoodInformPrefs", Context.MODE_PRIVATE);
        String userEmail = sharedPreferences.getString("FoodInform", "");

        modalClasses = new ArrayList<>();
        dbHandler = new GiftInformDB(GiftInformDetail.this);
        modalClasses = dbHandler.readGiftInformation(userEmail);
        rvAdapter = new GiftInformRVAdapter(modalClasses, GiftInformDetail.this);
        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(GiftInformDetail.this, RecyclerView.VERTICAL, false);
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