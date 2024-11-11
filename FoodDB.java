package com.example.feedhope.ProviderInterface.FoodDonation;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class FoodDB extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "FoodDonate.db";
    private static final int DATABASE_VERSION = 2;
    public static final String TABLE_NAME = "locations";

    // Columns for storing location and form data
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_LATITUDE = "latitude";
    public static final String COLUMN_LONGITUDE = "longitude";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_FOOD_TYPE = "food_type";
    public static final String COLUMN_QUANTITY = "quantity";
    public static final String COLUMN_STORAGE = "storage";
    public static final String COLUMN_AVAILABLE_DATE = "available_date";
    public static final String COLUMN_EXPIRY_DATE = "expiry_date";

    public FoodDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_LATITUDE + " REAL, " +
                COLUMN_LONGITUDE + " REAL, " +
                COLUMN_EMAIL + " TEXT, " +
                COLUMN_FOOD_TYPE + " TEXT, " +
                COLUMN_QUANTITY + " TEXT, " +
                COLUMN_STORAGE + " TEXT, " +
                COLUMN_AVAILABLE_DATE + " TEXT, " +
                COLUMN_EXPIRY_DATE + " TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // Method to store location and form data
    public boolean insertLocationWithFormData(String name, double latitude, double longitude,
                                              String email, String foodType, String quantity,
                                              String storage, String availableDate, String expiryDate) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_NAME, name);
        values.put(COLUMN_LATITUDE, latitude);
        values.put(COLUMN_LONGITUDE, longitude);
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_FOOD_TYPE, foodType);
        values.put(COLUMN_QUANTITY, quantity);
        values.put(COLUMN_STORAGE, storage);
        values.put(COLUMN_AVAILABLE_DATE, availableDate);
        values.put(COLUMN_EXPIRY_DATE, expiryDate);

        long result = db.insert(TABLE_NAME, null, values);
        db.close();
        return result != -1;
    }

    // Optional: Method to retrieve all data
    public Cursor getAllData() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
    }

    // Method to check if a specific location already exists in the database
    public boolean locationExists(double latitude, double longitude) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = COLUMN_LATITUDE + " = ? AND " + COLUMN_LONGITUDE + " = ?";
        String[] selectionArgs = {String.valueOf(latitude), String.valueOf(longitude)};
        Cursor cursor = db.query(TABLE_NAME, null, selection, selectionArgs, null, null, null);

        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return exists;
    }

    public boolean insertData(String loggedInEmail, String selectedFood, String quantity1, String selectedStorage, String available1, String expire1) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        // Insert food donation data
        values.put(COLUMN_EMAIL, loggedInEmail);
        values.put(COLUMN_FOOD_TYPE, selectedFood);
        values.put(COLUMN_QUANTITY, quantity1);
        values.put(COLUMN_STORAGE, selectedStorage);
        values.put(COLUMN_AVAILABLE_DATE, available1);
        values.put(COLUMN_EXPIRY_DATE, expire1);

        // Insert the data into the database
        long result = db.insert(TABLE_NAME, null, values);
        db.close();

        // If the result is -1, the insertion failed
        return result != -1;
    }

    public ArrayList<FoodProvideModalClass> readFoodData() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<FoodProvideModalClass> foodList = new ArrayList<>();

        // Query to retrieve all data from the locations table
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        // Get column indices safely
        int columnNameIndex = cursor.getColumnIndex(COLUMN_NAME);
        int columnLatitudeIndex = cursor.getColumnIndex(COLUMN_LATITUDE);
        int columnLongitudeIndex = cursor.getColumnIndex(COLUMN_LONGITUDE);
        int columnEmailIndex = cursor.getColumnIndex(COLUMN_EMAIL);
        int columnFoodTypeIndex = cursor.getColumnIndex(COLUMN_FOOD_TYPE);
        int columnQuantityIndex = cursor.getColumnIndex(COLUMN_QUANTITY);
        int columnStorageIndex = cursor.getColumnIndex(COLUMN_STORAGE);
        int columnAvailableDateIndex = cursor.getColumnIndex(COLUMN_AVAILABLE_DATE);
        int columnExpiryDateIndex = cursor.getColumnIndex(COLUMN_EXPIRY_DATE);

        // Loop through the result set
        if (cursor.moveToFirst()) {
            do {
                // Check if column index is valid (≥ 0) before accessing data
                String name = columnNameIndex != -1 ? cursor.getString(columnNameIndex) : "";
                double latitude = columnLatitudeIndex != -1 ? cursor.getDouble(columnLatitudeIndex) : 0;
                double longitude = columnLongitudeIndex != -1 ? cursor.getDouble(columnLongitudeIndex) : 0;
                String email = columnEmailIndex != -1 ? cursor.getString(columnEmailIndex) : "";
                String foodType = columnFoodTypeIndex != -1 ? cursor.getString(columnFoodTypeIndex) : "";
                String quantity = columnQuantityIndex != -1 ? cursor.getString(columnQuantityIndex) : "";
                String storage = columnStorageIndex != -1 ? cursor.getString(columnStorageIndex) : "";
                String availableDate = columnAvailableDateIndex != -1 ? cursor.getString(columnAvailableDateIndex) : "";
                String expiryDate = columnExpiryDateIndex != -1 ? cursor.getString(columnExpiryDateIndex) : "";

                // Create a FoodProvideModalClass object and add it to the list
                FoodProvideModalClass foodItem = new FoodProvideModalClass(name, latitude, longitude, email, foodType, quantity, storage, availableDate, expiryDate);
                foodList.add(foodItem);

            } while (cursor.moveToNext());
        }

        // Close the cursor and database
        cursor.close();
        db.close();

        return foodList;
    }


}
