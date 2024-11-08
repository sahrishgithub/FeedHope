package com.example.unitconverter.ProviderInterface;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DonationDB extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "donations.db";
    private static final int DATABASE_VERSION = 16;
    private static final String TABLE_NAME = "donations_table";

    public DonationDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "type TEXT, " +
                "condition TEXT, " +
                "quantity TEXT, " +
                "category TEXT, " +
                "season TEXT, " +
                "size TEXT, " +
                "latitude REAL, " +
                "longitude REAL, " +
                "location_name TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insert(String type, String condition, String quantity, String category, String season, String size, double latitude, double longitude, String locationName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("type", type);
        values.put("condition", condition);
        values.put("quantity", quantity);
        values.put("category", category);
        values.put("season", season);
        values.put("size", size);
        values.put("latitude", latitude);
        values.put("longitude", longitude);
        values.put("location_name", locationName);

        Log.d("DonationDB", "Inserting data: " + values);

        long result = db.insert(TABLE_NAME, null, values);
        db.close();

        if (result == -1) {
            Log.e("DonationDB", "Failed to insert data into the database.");
            return false;
        } else {
            Log.d("DonationDB", "Data inserted successfully with row ID: " + result);
            return true;
        }
    }
    // Add this method in DonationDB.java
    public List<ClothItem> getAllDonations() {
        List<ClothItem> clothItems = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                String type = getStringFromCursor(cursor, "type");
                String condition = getStringFromCursor(cursor, "condition");
                String quantity = getStringFromCursor(cursor, "quantity");
                String category = getStringFromCursor(cursor, "category");
                String season = getStringFromCursor(cursor, "season");
                String size = getStringFromCursor(cursor, "size");
                double latitude = getDoubleFromCursor(cursor, "latitude");
                double longitude = getDoubleFromCursor(cursor, "longitude");
                String locationName = getStringFromCursor(cursor, "location_name");

                ClothItem clothItem = new ClothItem(type, condition, quantity, category, season, size, locationName, latitude, longitude);
                clothItems.add(clothItem);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return clothItems;
    }

    // Helper method to get String data safely
    private String getStringFromCursor(Cursor cursor, String columnName) {
        int columnIndex = cursor.getColumnIndex(columnName);
        return columnIndex != -1 ? cursor.getString(columnIndex) : "";
    }

    // Helper method to get double data safely
    private double getDoubleFromCursor(Cursor cursor, String columnName) {
        int columnIndex = cursor.getColumnIndex(columnName);
        return columnIndex != -1 ? cursor.getDouble(columnIndex) : 0.0;
    }


    public ArrayList<ClothItem> readFoodData() {
        ArrayList<ClothItem> clothItems = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Query to fetch only food-related data
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE category = 'Food'";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                String type = getStringFromCursor(cursor, "type");
                String condition = getStringFromCursor(cursor, "condition");
                String quantity = getStringFromCursor(cursor, "quantity");
                String category = getStringFromCursor(cursor, "category");
                String season = getStringFromCursor(cursor, "season");
                String size = getStringFromCursor(cursor, "size");
                double latitude = getDoubleFromCursor(cursor, "latitude");
                double longitude = getDoubleFromCursor(cursor, "longitude");
                String locationName = getStringFromCursor(cursor, "location_name");

                ClothItem clothItem = new ClothItem(type, condition, quantity, category, season, size, locationName, latitude, longitude);
                clothItems.add(clothItem);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return clothItems;
    }

}
