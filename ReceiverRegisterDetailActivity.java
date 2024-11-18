package com.example.feedhope.ReceiverInterface.ReceiverRegister;

import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.feedhope.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;

public class ReceiverRegisterDetailActivity extends AppCompatActivity {
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

        // Retrieve the user list from the intent as a JSON string
        String receiverListJson = getIntent().getStringExtra("receiverList");
        ArrayList<ReceiverModalClass> receiverList;
        if (receiverListJson != null) {
            // Deserialize the JSON string back to the list of ReceiverModalClass
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<ReceiverModalClass>>() {}.getType();
            receiverList = gson.fromJson(receiverListJson, type);
            // Reverse the list to bring the new requests at the top
            if (receiverList != null) {
                Collections.reverse(receiverList);
            }
        } else {
            receiverList = new ArrayList<>();
        }
        Log.d("ReceiverRVRegistration", "Receiver list size: " + receiverList.size());

        NotificationManager notifManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        receiverAdapter = new ReceiverRVAdapter(receiverList, this, notifManager);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(receiverAdapter);
    }

    @Override
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