package com.example.unitconverter.RiderInterface;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class RiderRegisterDB extends SQLiteOpenHelper {

    // Database name and version
    private static final String DATABASE_NAME = "RiderDatabase";
    private static final int DATABASE_VERSION = 1;

    // Table and columns
    private static final String TABLE_RIDER = "rider";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_PHONE = "phone";
    private static final String COLUMN_ID_TYPE = "id_type";
    private static final String COLUMN_ID_NUMBER = "id_number";
    private static final String COLUMN_WORKING_HOURS = "working_hours";
    private static final String COLUMN_WORKING_DAYS = "working_days";
    private static final String COLUMN_CARD = "card";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_LATITUDE = "latitude";
    private static final String COLUMN_LONGITUDE = "longitude";
    private static final String COLUMN_LOCATION_NAME = "location_name";

    // Constructor
    public RiderRegisterDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // SQL query to create the rider table
        String CREATE_RIDER_TABLE = "CREATE TABLE " + TABLE_RIDER + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_PHONE + " TEXT, " +
                COLUMN_ID_TYPE + " TEXT, " +
                COLUMN_ID_NUMBER + " TEXT, " +
                COLUMN_WORKING_HOURS + " TEXT, " +
                COLUMN_WORKING_DAYS + " TEXT, " +
                COLUMN_CARD + " TEXT, " +
                COLUMN_EMAIL + " TEXT, " +
                COLUMN_PASSWORD + " TEXT, " +
                COLUMN_LATITUDE + " REAL, " +
                COLUMN_LONGITUDE + " REAL, " +
                COLUMN_LOCATION_NAME + " TEXT);";

        db.execSQL(CREATE_RIDER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop the table if it exists and recreate it
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RIDER);
        onCreate(db);
    }

    // Insert rider data
    public long insertRiderData(String name, String phone, String idType, String idNumber, String workingHours,
                                String workingDays, String card, String email, String password, double latitude,
                                double longitude, String locationName) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_PHONE, phone);
        values.put(COLUMN_ID_TYPE, idType);
        values.put(COLUMN_ID_NUMBER, idNumber);
        values.put(COLUMN_WORKING_HOURS, workingHours);
        values.put(COLUMN_WORKING_DAYS, workingDays);
        values.put(COLUMN_CARD, card);
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_PASSWORD, password);
        values.put(COLUMN_LATITUDE, latitude);
        values.put(COLUMN_LONGITUDE, longitude);
        values.put(COLUMN_LOCATION_NAME, locationName);

        long result = -1;
        try {
            result = db.insert(TABLE_RIDER, null, values);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        db.close();
        return result;
    }

    // Read rider data based on logged-in email
    public RiderModalClass read(String loggedInEmail) {
        SQLiteDatabase db = this.getReadableDatabase();

        // Define the columns we want to retrieve
        String[] columns = {
                COLUMN_ID, COLUMN_NAME, COLUMN_PHONE, COLUMN_ID_TYPE, COLUMN_ID_NUMBER,
                COLUMN_WORKING_HOURS, COLUMN_WORKING_DAYS, COLUMN_CARD, COLUMN_EMAIL,
                COLUMN_PASSWORD, COLUMN_LATITUDE, COLUMN_LONGITUDE, COLUMN_LOCATION_NAME
        };

        // Define the selection criteria (where email matches)
        String selection = COLUMN_EMAIL + " = ?";
        String[] selectionArgs = { loggedInEmail };

        // Perform the query to retrieve the rider's data
        Cursor cursor = db.query(TABLE_RIDER, columns, selection, selectionArgs, null, null, null);

        RiderModalClass rider = null;

        // Check if we got a result
        if (cursor != null && cursor.moveToFirst()) {
            // Extract data directly without extra getColumnIndex checks
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));  // Throws exception if column is missing
            String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME));
            String phone = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PHONE));
            String idType = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ID_TYPE));
            String idNumber = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ID_NUMBER));
            String workingHours = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_WORKING_HOURS));
            String workingDays = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_WORKING_DAYS));
            String card = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CARD));
            String email = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL));
            String password = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PASSWORD));
            double latitude = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_LATITUDE));
            double longitude = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_LONGITUDE));
            String locationName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LOCATION_NAME));

            // Create a RiderModalClass object with the retrieved data
            rider = new RiderModalClass(id, name, phone, idType, idNumber, workingHours, workingDays, card, email, password, latitude, longitude, locationName);
        }

        // Close the cursor and database
        if (cursor != null) {
            cursor.close();
        }
        db.close();

        // Return the RiderModalClass object (could be null if no rider is found)
        return rider;
    }

    public boolean insertData(String name, String phone, String type, String idcard, String hours, String days, String card, String email, String pass, double latitude, double longitude, String locationName) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Create ContentValues object to insert data into the database
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_PHONE, phone);
        values.put(COLUMN_ID_TYPE, type);
        values.put(COLUMN_ID_NUMBER, idcard);
        values.put(COLUMN_WORKING_HOURS, hours);
        values.put(COLUMN_WORKING_DAYS, days);
        values.put(COLUMN_CARD, card);
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_PASSWORD, pass);
        values.put(COLUMN_LATITUDE, latitude);
        values.put(COLUMN_LONGITUDE, longitude);
        values.put(COLUMN_LOCATION_NAME, locationName);

        long result = -1;
        try {
            // Insert data into the rider table
            result = db.insert(TABLE_RIDER, null, values);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        db.close();

        // Return true if insertion was successful (result > 0)
        return result != -1;
    }

    public boolean checkUser(String email1, String pass1) {
        SQLiteDatabase db = this.getReadableDatabase();

        // Define the selection criteria (where email and password match)
        String selection = COLUMN_EMAIL + " = ? AND " + COLUMN_PASSWORD + " = ?";
        String[] selectionArgs = { email1, pass1 };

        // Perform the query to check if a user exists with the given email and password
        Cursor cursor = db.query(TABLE_RIDER, null, selection, selectionArgs, null, null, null);

        boolean userExists = false;

        // Check if we got a result
        if (cursor != null && cursor.moveToFirst()) {
            // If there is at least one matching row, the user exists
            userExists = true;
        }

        // Close the cursor and database
        if (cursor != null) {
            cursor.close();
        }
        db.close();

        // Return whether the user exists
        return userExists;
    }

    public long update(RiderModalClass riderModalClass) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Create ContentValues object to hold the updated data
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, riderModalClass.getName());
        values.put(COLUMN_PHONE, riderModalClass.getPhone());
        values.put(COLUMN_ID_TYPE, riderModalClass.getIdType());
        values.put(COLUMN_ID_NUMBER, riderModalClass.getIdNumber());
        values.put(COLUMN_WORKING_HOURS, riderModalClass.getWorkingHours());
        values.put(COLUMN_WORKING_DAYS, riderModalClass.getWorkingDays());
        values.put(COLUMN_CARD, riderModalClass.getCard());
        values.put(COLUMN_EMAIL, riderModalClass.getEmail());
        values.put(COLUMN_PASSWORD, riderModalClass.getPassword());
        values.put(COLUMN_LATITUDE, riderModalClass.getLatitude());
        values.put(COLUMN_LONGITUDE, riderModalClass.getLongitude());
        values.put(COLUMN_LOCATION_NAME, riderModalClass.getLocationName());

        // Define the where clause to specify which record to update (based on rider id or email)
        String selection = COLUMN_ID + " = ?";
        String[] selectionArgs = { String.valueOf(riderModalClass.getId()) };

        // Perform the update operation
        long result = db.update(TABLE_RIDER, values, selection, selectionArgs);

        db.close();
        return result;
    }

}

