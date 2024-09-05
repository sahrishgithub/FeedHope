package com.example.unitconverter.AdminInterface;

import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.unitconverter.R;
import com.example.unitconverter.ReceiverInterface.ReceiverModalClass;
import com.example.unitconverter.ReceiverInterface.ReceiverRVAdapter;

import java.util.ArrayList;

public class ReceiverRVRegistration extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ReceiverRVAdapter receiverAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rv_receiver_registration);
        recyclerView = findViewById(R.id.recyclerView);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");
        }

        // Retrieve the user list from the intent
        ArrayList<ReceiverModalClass> receiverList = getIntent().getParcelableArrayListExtra("receiverList");

        if (receiverList == null) {
            receiverList = new ArrayList<>();
        }

        // Log receiver list size
        Log.d("ReceiverRVRegistration", "Receiver list size: " + receiverList.size());

        // Get the NotificationManager service
        NotificationManager notifManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Pass the activity context (this) and NotificationManager to the adapter
        receiverAdapter = new ReceiverRVAdapter(receiverList, this, notifManager);

        // Set up the RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(receiverAdapter);
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // Handle the back button click here
                onBackPressed(); // Go back to the previous activity
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}