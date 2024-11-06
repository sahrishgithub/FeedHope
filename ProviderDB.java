package com.example.unitconverter.ProviderInterface;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class ProviderDB extends SQLiteOpenHelper {

    private static final String DBName = "FeedHopeProject.db";
    private static final int DB_VERSION = 11;  // Set database version

    // Table and Column Names
    public static final String TABLE_NAME = "food_provide";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_LOCATION_NAME = "location_name";
    public static final String COLUMN_LATITUDE = "latitude";
    public static final String COLUMN_LONGITUDE = "longitude";
    public static final String COLUMN_FOOD_TYPE = "FoodType";
    public static final String COLUMN_QUANTITY = "Quantity";
    public static final String COLUMN_STORAGE = "Storage";
    public static final String COLUMN_AVAILABLE_DATE = "AvailableDate";
    public static final String COLUMN_EXPIRE_DATE = "ExpireDate";
    public static final String COLUMN_EMAIL = "Email";

    // Provider table for registration details
    public static final String PROVIDER_TABLE_NAME = "ProviderRegister";
    public static final String COLUMN_PROVIDER_NAME = "Name";
    public static final String COLUMN_PROVIDER_PHONE = "Phone";
    public static final String COLUMN_PROVIDER_EMAIL = "Email";
    public static final String COLUMN_PROVIDER_PASS = "Pass";

    private static final String TABLE_CREATE_FOOD_PROVIDE = "CREATE TABLE " + TABLE_NAME + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_LOCATION_NAME + " TEXT, " +
            COLUMN_LATITUDE + " REAL, " +
            COLUMN_LONGITUDE + " REAL, " +
            COLUMN_FOOD_TYPE + " TEXT, " +
            COLUMN_QUANTITY + " TEXT, " +
            COLUMN_STORAGE + " TEXT, " +
            COLUMN_AVAILABLE_DATE + " TEXT, " +
            COLUMN_EXPIRE_DATE + " TEXT, " +
            COLUMN_EMAIL + " TEXT);";

    private static final String TABLE_CREATE_PROVIDER_REGISTER = "CREATE TABLE " + PROVIDER_TABLE_NAME + " (" +
            COLUMN_PROVIDER_NAME + " TEXT NOT NULL, " +
            COLUMN_PROVIDER_PHONE + " TEXT NOT NULL, " +
            COLUMN_PROVIDER_EMAIL + " TEXT PRIMARY KEY, " +
            COLUMN_PROVIDER_PASS + " TEXT NOT NULL);";

    public ProviderDB(@Nullable Context context) {
        super(context, DBName, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE_FOOD_PROVIDE);
        db.execSQL(TABLE_CREATE_PROVIDER_REGISTER);
        Log.d("ProviderDB", "Tables created successfully.");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + PROVIDER_TABLE_NAME);
        onCreate(db);
    }

    /**
     * Insert food donation data into the database.
     */
    public boolean insertData(String locationName, double latitude, double longitude, String foodType,
                              String quantity, String storage, String availableDate, String expireDate, String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_LOCATION_NAME, locationName);
        values.put(COLUMN_LATITUDE, latitude);
        values.put(COLUMN_LONGITUDE, longitude);
        values.put(COLUMN_FOOD_TYPE, foodType);
        values.put(COLUMN_QUANTITY, quantity);
        values.put(COLUMN_STORAGE, storage);
        values.put(COLUMN_AVAILABLE_DATE, availableDate);
        values.put(COLUMN_EXPIRE_DATE, expireDate);
        values.put(COLUMN_EMAIL, email);

        long result = db.insert(TABLE_NAME, null, values);
        if (result == -1) {
            Log.e("ProviderDB", "Failed to insert data.");
            return false;
        } else {
            Log.d("ProviderDB", "Data inserted successfully.");
        }
        db.close();
        return true;
    }

    /**
     * Read all food donations data from the database.
     */
    public ArrayList<FoodProvideModalClass> readFoodData() {
        ArrayList<FoodProvideModalClass> foodModalClasses = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        if (cursor.moveToFirst()) {
            do {
                String locationName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LOCATION_NAME));
                double latitude = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_LATITUDE));
                double longitude = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_LONGITUDE));
                String foodType = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FOOD_TYPE));
                String quantity = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_QUANTITY));
                String storage = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STORAGE));
                String availableDate = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_AVAILABLE_DATE));
                String expireDate = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EXPIRE_DATE));
                String email = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL));

                foodModalClasses.add(new FoodProvideModalClass(locationName, latitude, longitude, foodType, quantity, storage, availableDate, expireDate, email));
            } while (cursor.moveToNext());
            Log.d("ProviderDB", "Data read successfully. Total items: " + foodModalClasses.size());
        } else {
            Log.d("ProviderDB", "No data found.");
        }

        cursor.close();
        return foodModalClasses;
    }

    /**
     * Method to retrieve provider data by email.
     */
    public ProviderModalClass read(String loggedInEmail) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(PROVIDER_TABLE_NAME, null, COLUMN_PROVIDER_EMAIL + " = ?", new String[]{loggedInEmail}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PROVIDER_NAME));
            String phone = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PROVIDER_PHONE));
            String email = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PROVIDER_EMAIL));
            String pass = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PROVIDER_PASS));
            cursor.close();

            // Return a new ProviderModalClass object with the retrieved data
            return new ProviderModalClass(name, phone, email, pass);
        } else {
            if (cursor != null) cursor.close();
            Log.d("ProviderDB", "No provider found with email: " + loggedInEmail);
            return null;
        }
    }

    /**
     * Method to check if a user with the given email and password exists.
     * This is typically used for login authentication.
     */
    public boolean checkUser(String email1, String pass1) {
        SQLiteDatabase db = this.getReadableDatabase();

        // Query to find a record with the matching email and password
        Cursor cursor = db.query(PROVIDER_TABLE_NAME,
                null,
                COLUMN_PROVIDER_EMAIL + " = ? AND " + COLUMN_PROVIDER_PASS + " = ?",
                new String[]{email1, pass1},
                null,
                null,
                null);

        boolean exists = false;

        // If the cursor can move to the first row, a matching user exists
        if (cursor != null && cursor.moveToFirst()) {
            exists = true;  // Login successful
            Log.d("ProviderDB", "User found with email: " + email1);
        } else {
            Log.d("ProviderDB", "No user found with email: " + email1);
        }

        if (cursor != null) cursor.close();
        return exists;
    }

    /**
     * Method to insert a new provider's registration details into the database.
     */
    public boolean insert(String name, String phone, String email, String pass) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        // Add provider details to ContentValues
        values.put(COLUMN_PROVIDER_NAME, name);
        values.put(COLUMN_PROVIDER_PHONE, phone);
        values.put(COLUMN_PROVIDER_EMAIL, email);
        values.put(COLUMN_PROVIDER_PASS, pass);

        // Insert the new row, returning the row ID of the new entry, or -1 if an error occurred
        long result = db.insert(PROVIDER_TABLE_NAME, null, values);
        db.close();

        if (result == -1) {
            Log.e("ProviderDB", "Failed to insert provider data.");
            return false;
        } else {
            Log.d("ProviderDB", "Provider data inserted successfully.");
            return true;
        }
    }

    /**
     * Method to update an existing provider's details in the database.
     */
    public long update(ProviderModalClass provider) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        // Update the ContentValues with the new data
        values.put(COLUMN_PROVIDER_NAME, provider.getName());
        values.put(COLUMN_PROVIDER_PHONE, provider.getPhone());
        values.put(COLUMN_PROVIDER_PASS, provider.getPass());

        // Define the WHERE clause and arguments for updating by email
        String selection = COLUMN_PROVIDER_EMAIL + " = ?";
        String[] selectionArgs = { provider.getEmail() };

        // Update the provider's data and get the number of rows affected
        int rowsAffected = db.update(PROVIDER_TABLE_NAME, values, selection, selectionArgs);
        db.close();

        if (rowsAffected > 0) {
            Log.d("ProviderDB", "Provider data updated successfully for email: " + provider.getEmail());
        } else {
            Log.d("ProviderDB", "No provider found to update with email: " + provider.getEmail());
        }

        return rowsAffected;
    }

}
