package com.example.feedhope.AdminInterface;

import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;

import com.example.feedhope.ProviderInterface.ProviderRegister.ProviderDB;
import com.example.feedhope.R;
import com.example.feedhope.ReceiverInterface.ReceiverRegister.ReceiverRegisterDB;
import com.example.feedhope.RiderInterface.Duty.DutyDB;
import com.example.feedhope.RiderInterface.Register.RiderRegisterDB;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class DutyAssign extends AppCompatActivity {
    private EditText date;
    private Spinner pick, drop, username;
    private Button submit_btn;
    private DutyDB db;
    private ProviderDB providerDB;
    private ReceiverRegisterDB receiverDB;
    // Fetch the list of all rider emails
    RiderRegisterDB riderDB = new RiderRegisterDB(this);
    private ArrayList<String> providerLocations;
    private static final String CHANNEL_ID = "DutyChannel";
    private static final String CHANNEL_NAME = "Duty Notifications";
    private NotificationManager notificationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.duty_assign);

        username = findViewById(R.id.name);
        pick = findViewById(R.id.pick_location);
        providerDB = new ProviderDB(this);
        drop = findViewById(R.id.drop);
        receiverDB = new ReceiverRegisterDB(this);
        date = findViewById(R.id.date);
        Calendar calendar = Calendar.getInstance();
        submit_btn = findViewById(R.id.submit);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");
        }

        date.setOnClickListener(v -> {
            // Advance the calendar by one day to start the selection from the next day
            calendar.add(Calendar.DAY_OF_YEAR, 1);

            // Open DatePickerDialog with the minimum date set to the next day
            DatePickerDialog datePickerDialog = new DatePickerDialog(DutyAssign.this,
                    (view, year, monthOfYear, dayOfMonth) -> {
                        // Set the selected date in the Calendar object
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, monthOfYear);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        // Open TimePickerDialog after DatePickerDialog
                        new TimePickerDialog(DutyAssign.this,
                                (view1, hourOfDay, minute) -> {
                                    // Set the selected time in the Calendar object
                                    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                    calendar.set(Calendar.MINUTE, minute);
                                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
                                    date.setText(format.format(calendar.getTime()));
                                },
                                calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true
                        ).show();
                    },
                    calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)
            );

            // Set the minimum selectable date in the DatePickerDialog to the next day
            datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());

            // Show the DatePickerDialog
            datePickerDialog.show();
        });


        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        createNotificationChannel();


        /// Fetch provider locations and set it to the 'pick' Spinner
        ArrayList<String> providerLocations = providerDB.getAllProviderLocations();
        providerLocations.add(0, "Select Pick Location");  // Add the default item at the beginning

        ArrayAdapter<String> providerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, providerLocations) {
            @Override
            public boolean isEnabled(int position) {
                // Disable the first item (hint item)
                if (position == 0) {
                    return false;  // Make the first item (Select Pick Location) non-selectable
                }
                return true;  // Allow selection for the rest of the items
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                // If the first item is selected, set its text color to gray to indicate it's a placeholder
                if (position == 0) {
                    TextView tv = (TextView) view;
                    tv.setTextColor(Color.GRAY);  // Set the placeholder text to gray
                } else {
                    TextView tv = (TextView) view;
                    tv.setTextColor(Color.BLACK);  // Set other items text to black
                }
                return view;
            }
        };

// Set the drop-down resource and adapter
        providerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pick.setAdapter(providerAdapter);  // Set the adapter to the pick Spinner


// Fetch receiver locations and set it to the 'drop' Spinner
        ArrayList<String> receiverLocations = receiverDB.getAllReceiverLocations();
        receiverLocations.add(0, "Select Drop Location");  // Add the placeholder at the beginning

        ArrayAdapter<String> receiverAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, receiverLocations) {
            @Override
            public boolean isEnabled(int position) {
                // Disable the first item (Select Drop Location)
                if (position == 0) {
                    return false;  // Make the placeholder item non-selectable
                }
                return true;  // Allow selection for the rest of the items
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                // If the first item (Select Drop Location) is selected, set its text color to gray
                if (position == 0) {
                    TextView tv = (TextView) view;
                    tv.setTextColor(Color.GRAY);  // Set the placeholder text to gray to indicate it's not selectable
                } else {
                    TextView tv = (TextView) view;
                    tv.setTextColor(Color.BLACK);  // Set other items' text to black
                }
                return view;
            }
        };

// Set the drop-down resource and adapter
        receiverAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        drop.setAdapter(receiverAdapter);  // Set the adapter to the drop Spinner


        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db = new DutyDB(DutyAssign.this);
                db.getWritableDatabase();

                boolean isValid = true;
                StringBuilder errorMessages = new StringBuilder();

                // Get the selected email from the Spinner (username)
                String name1 = username.getSelectedItem().toString().trim();  // Use getSelectedItem for Spinner

                // Get the selected location from the pick Spinner
                String pick1 = pick.getSelectedItem().toString().trim(); // Use getSelectedItem for Spinner
                // Get the selected location from the drop Spinner
                String drop1 = drop.getSelectedItem().toString().trim(); // Use getSelectedItem for Spinner
                String date1 = date.getText().toString().trim();

                // Reset background colors for username and pick/drop location fields
                username.setBackgroundColor(Color.TRANSPARENT);
                pick.setBackgroundColor(Color.TRANSPARENT);
                drop.setBackgroundColor(Color.TRANSPARENT);

                // Validation for pick location
                if (pick1.isEmpty() || pick1.equals("Select Location")) {
                    pick.setBackgroundColor(Color.RED);  // Indicate error by changing background
                    errorMessages.append("Pick Location field is required\n");
                    isValid = false;
                }

                // Validation for drop location
                if (drop1.isEmpty() || drop1.equals("Select Location")) {
                    drop.setBackgroundColor(Color.RED);  // Indicate error by changing background
                    errorMessages.append("Drop Location field is required\n");
                    isValid = false;
                }

                // Validation checks for username (Spinner)
                if (name1.isEmpty() || name1.equals("Select Rider")) {
                    username.setBackgroundColor(Color.RED);  // Indicate error by changing background
                    errorMessages.append("Rider selection is required\n");
                    isValid = false;
                }

                // Validation for date
                date.setError(null);
                if (date1.isEmpty()) {
                    date.setError("Date field is required");
                    isValid = false;
                }

                // If all fields are valid, proceed with duty assignment
                if (isValid) {
                    if (db.assignDuty(name1, pick1, drop1, date1, "Pending")) {
                        Toast.makeText(DutyAssign.this, "Data Inserted", Toast.LENGTH_LONG).show();
                        sendNotification(name1, "Duty Assigned");
                        finish();
                    } else {
                        Toast.makeText(DutyAssign.this, "Data not Inserted", Toast.LENGTH_LONG).show();
                    }
                } else {
                    // Display the error messages if validation fails
                    Toast.makeText(DutyAssign.this, errorMessages.toString(), Toast.LENGTH_LONG).show();
                }
            }
        });


    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription("Channel for duty notifications");
            channel.enableLights(true);
            channel.setLightColor(Color.RED);
            channel.enableVibration(true);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void sendNotification(String riderName, String title) {
        Intent intent = new Intent(this, DutyAssign.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                this,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.notification) // Use your own notification icon here
                .setContentTitle(title)
                .setContentText("Duty assigned to " + riderName)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        notificationManager.notify((int) System.currentTimeMillis(), builder.build());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed(); // Go back to the previous activity
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}