package com.example.feedhope.ProviderInterface.ToyDonation;

import android.os.Bundle;
import android.view.MenuItem;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.feedhope.R;
import java.util.ArrayList;

public class ToyDetailActivity extends AppCompatActivity {
    private ArrayList<ToyModalClass> modalClasses;
    private ToyDB dbHandler;
    private ToyRVAdapter rvAdapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rv_toy);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");
        }

        modalClasses = new ArrayList<>();
        dbHandler = new ToyDB(ToyDetailActivity.this);
        String loggedInUserName = getIntent().getStringExtra("email");
        modalClasses = dbHandler.readToyData();
        rvAdapter = new ToyRVAdapter(modalClasses, ToyDetailActivity.this);
        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ToyDetailActivity.this, RecyclerView.VERTICAL, false);
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