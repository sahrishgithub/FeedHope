package com.example.unitconverter.ReceiverInterface;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Build;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import com.example.unitconverter.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class ReceiverRegister extends AppCompatActivity {
    private EditText member, reference, type, requirement, time, phone, email, pass;
    private Button register;
    private TextView login;
    private ArrayList<ReceiverModalClass> receiverList;
    private SharedPreferences sharedPreferences;
    private Gson gson;

    // Notification settings
    private static final String CHANNEL_ID = "ReceiverChannel";
    private static final String CHANNEL_NAME = "Receiver Notifications";
    private NotificationManager notificationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_receiver);

        reference = findViewById(R.id.reference);
        type = findViewById(R.id.type);
        member = findViewById(R.id.member);
        requirement = findViewById(R.id.requirement);
        RadioGroup radioGroup = findViewById(R.id.radio_btn);
        time = findViewById(R.id.time);
        phone = findViewById(R.id.phone);
        email = findViewById(R.id.email);
        pass = findViewById(R.id.pass);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // for back option
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");
        }

        register = findViewById(R.id.register);
        login = findViewById(R.id.login);
        sharedPreferences = getSharedPreferences("receiverPrefs", Context.MODE_PRIVATE);
        gson = new Gson();
        loadData();

        // Initialize notification manager
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        createNotificationChannel();

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton selectedRadioButton = findViewById(checkedId);
                String selectedText = selectedRadioButton.getText().toString();
                Toast.makeText(ReceiverRegister.this, "Selected: " + selectedText, Toast.LENGTH_SHORT).show();

                register.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String reference1, type1, member1, requirement1, frequency1, time1, phone1, email1, pass1;

                        reference1 = reference.getText().toString().trim();
                        type1 = type.getText().toString().trim();
                        member1 = member.getText().toString().trim();
                        requirement1 = requirement.getText().toString().trim();
                        frequency1 = selectedText.trim();
                        time1 = time.getText().toString().trim();
                        phone1 = phone.getText().toString().trim();
                        email1 = email.getText().toString().trim();
                        pass1 = pass.getText().toString().trim();

                        if (!reference1.isEmpty() && !type1.isEmpty() && !member1.isEmpty() && !requirement1.isEmpty() && !frequency1.isEmpty() && !time1.isEmpty() && !phone1.isEmpty() && !email1.isEmpty() && !pass1.isEmpty()) {
                            receiverList.add(new ReceiverModalClass(reference1, type1, member1, requirement1, frequency1, time1, phone1, email1, pass1));
                            saveData();
                            reference.setText("");
                            type.setText("");
                            member.setText("");
                            requirement.setText("");
                            time.setText("");
                            phone.setText("");
                            email.setText("");
                            pass.setText("");
                            Toast.makeText(ReceiverRegister.this, "Please wait while admin accepts your request.", Toast.LENGTH_SHORT).show();
                            sendNotification(member1, "Receiver Registration Request");
                        } else {
                            Toast.makeText(ReceiverRegister.this, "All fields must be filled", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                login.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ReceiverRegister.this, ReceiverLogin.class);
                        startActivity(intent);
                    }
                });
            }
        });
    }

    private void loadData() {
        String json = sharedPreferences.getString("receiverList", null);
        Type type = new TypeToken<ArrayList<ReceiverModalClass>>() {}.getType();
        receiverList = gson.fromJson(json, type);

        if (receiverList == null) {
            receiverList = new ArrayList<>();
        }
        // Log data loaded
        Log.d("ReceiverRegister", "Loaded receiver list size: " + receiverList.size());
    }

    private void saveData() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String json = gson.toJson(receiverList);
        editor.putString("receiverList", json);
        editor.apply();
        // Log data saved
        Log.d("ReceiverRegister", "Data saved: " + json);
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription("Channel for receiver notifications");
            channel.enableLights(true);
            channel.setLightColor(Color.RED);
            channel.enableVibration(true);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void sendNotification(String memberName, String title) {
        Intent intent = new Intent(this, ReceiverRegister.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                this,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.notification)
                .setContentTitle(title)
                .setContentText(memberName + " wants to register.")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        notificationManager.notify((int) System.currentTimeMillis(), builder.build());
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