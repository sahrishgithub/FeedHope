package com.example.feedhope.ProviderInterface.MedicineDonation;

import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;

import com.example.feedhope.R;

import java.util.Calendar;

public class MedicineForm extends AppCompatActivity {
    Spinner form,condition;
    TextView pack;
    EditText name,quantity,manufacture,expire;
    Button register;
    MedicineDB db;
    private String loggedInEmail;
    private NotificationManager notificationManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.donate_medicine);
        name = findViewById(R.id.name);
        form = findViewById(R.id.form);
        quantity = findViewById(R.id.quantity);
        condition = findViewById(R.id.condition);
        manufacture = findViewById(R.id.manufacture);
        expire = findViewById(R.id.expire);
        pack = findViewById(R.id.pack);

        register = findViewById(R.id.register);
        loggedInEmail = getIntent().getStringExtra("email");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");
        }

        manufacture.setOnClickListener(v -> {
            // Get the current date
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            // Set the calendar for the minimum selectable date (2 years before current date)
            final Calendar minDate = Calendar.getInstance();
            minDate.add(Calendar.YEAR, -2); // Two years prior to the current date

            // Create DatePickerDialog and set the selected date in EditText
            DatePickerDialog datePickerDialog = new DatePickerDialog(MedicineForm.this,
                    (view, year1, monthOfYear, dayOfMonth) -> {
                        String selectedDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year1;
                        manufacture.setText(selectedDate);
                    },
                    year, month, day);

            // Restrict the date selection to two years prior to the current date and today
            datePickerDialog.getDatePicker().setMinDate(minDate.getTimeInMillis());
            datePickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());
            datePickerDialog.show();
        });

        expire.setOnClickListener(v -> {
            // Get the current date
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            // Set the calendar for the maximum selectable date (1 year from the current date)
            final Calendar maxDate = Calendar.getInstance();
            maxDate.add(Calendar.YEAR, 1); // One year after the current date

            // Create DatePickerDialog and set the selected date in EditText
            DatePickerDialog datePickerDialog = new DatePickerDialog(MedicineForm.this,
                    (view, year1, monthOfYear, dayOfMonth) -> {
                        String selectedDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year1;
                        expire.setText(selectedDate);
                    },
                    year, month, day);

            // Restrict the date selection to the current date and one year from the current date
            datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
            datePickerDialog.getDatePicker().setMaxDate(maxDate.getTimeInMillis());
            datePickerDialog.show();
        });

        register.setOnClickListener(v -> {
            db = new MedicineDB(MedicineForm.this);
            db.getWritableDatabase();

            String name1,quantity1,manufacture1,expire1;
            boolean isValid = true;
            StringBuilder errorMessages = new StringBuilder();

            name1 = name.getText().toString().trim();
            String selectedForm = form.getSelectedItem().toString();
            quantity1 = quantity.getText().toString().trim();
            String selectedCondition = condition.getSelectedItem().toString();
            manufacture1 = manufacture.getText().toString().trim();
            expire1 = expire.getText().toString().trim();

            if (name1.isEmpty()) {
                name.setError("Name field is required");
                isValid = false;
            }
            if (selectedForm.equals("Select Medicine Form")) {
                errorMessages.append("Please select a valid Form.\n");
                isValid = false;
            }
            if (quantity1.isEmpty()) {
                quantity.setError("Quantity field is required");
                isValid = false;
            }
            if (selectedCondition.equals("Select Purpose")) {
                errorMessages.append("Please select a valid Medicine Purpose.\n");
                isValid = false;
            }
            if (manufacture1.isEmpty()) {
                manufacture.setError("Manufacture field is required");
                isValid = false;
            }
            if (expire1.isEmpty()) {
                expire.setError("Expire field is required");
                isValid = false;
            }

            if (isValid) {
                String Quantity = quantity1+" "+pack.getText().toString().trim();
                if (db.insert(loggedInEmail,name1,selectedForm, Quantity, selectedCondition, manufacture1,expire1)) {
                    Toast.makeText(MedicineForm.this, "Data Inserted", Toast.LENGTH_LONG).show();
                    sendNotification("Medicine Donation Detail", "The Medicine details have been added successfully.");
                    finish();
                } else {
                    Toast.makeText(MedicineForm.this, "Data not Inserted", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(MedicineForm.this, errorMessages.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void sendNotification(String title, String message) {
        String channelId = "Donation_notifications";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "Toy Donation Notifications",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
        }
        Notification notification = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.notification)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .build();
        notificationManager.notify(1, notification);
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