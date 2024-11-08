package com.example.unitconverter.ProviderInterface;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ShoeDB extends SQLiteOpenHelper {

    // Database Name and Version
    private static final String DATABASE_NAME = "database_shoe.db";
    private static final int DATABASE_VERSION = 18;

    // Table and Columns
    public static final String TABLE_NAME = "donation_shoes";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TYPE = "type";
    public static final String COLUMN_CONDITION = "condition";
    public static final String COLUMN_SIZE = "size";
    public static final String COLUMN_QUANTITY = "quantity";
    public static final String COLUMN_GENDER = "gender";
    public static final String COLUMN_LATITUDE = "latitude";
    public static final String COLUMN_LONGITUDE = "longitude";
    public static final String COLUMN_LOCATION_NAME = "location_name";

    public ShoeDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TYPE + " TEXT, " +
                COLUMN_CONDITION + " TEXT, " +
                COLUMN_SIZE + " TEXT, " +
                COLUMN_QUANTITY + " INTEGER, " +
                COLUMN_GENDER + " TEXT, " +
                COLUMN_LATITUDE + " REAL, " +
                COLUMN_LONGITUDE + " REAL, " +
                COLUMN_LOCATION_NAME + " TEXT" +
                ")";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // Method to add a new donation
    public long addDonation(String type, String condition, String size, int quantity, String gender,
                            double latitude, double longitude, String locationName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TYPE, type);
        values.put(COLUMN_CONDITION, condition);
        values.put(COLUMN_SIZE, size);
        values.put(COLUMN_QUANTITY, quantity);
        values.put(COLUMN_GENDER, gender);
        values.put(COLUMN_LATITUDE, latitude);
        values.put(COLUMN_LONGITUDE, longitude);
        values.put(COLUMN_LOCATION_NAME, locationName);

        long result = db.insert(TABLE_NAME, null, values);
        db.close();
        return result;
    }

    // Method to get all donations
    public Cursor getAllDonations() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;
        return db.rawQuery(query, null);
    }

    // Method to get a specific donation by ID
    public Cursor getDonationById(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_ID + " = ?";
        return db.rawQuery(query, new String[]{String.valueOf(id)});
    }

    // Method to update a donation record
    public int updateDonation(long id, String type, String condition, String size, int quantity,
                              String gender, double latitude, double longitude, String locationName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TYPE, type);
        values.put(COLUMN_CONDITION, condition);
        values.put(COLUMN_SIZE, size);
        values.put(COLUMN_QUANTITY, quantity);
        values.put(COLUMN_GENDER, gender);
        values.put(COLUMN_LATITUDE, latitude);
        values.put(COLUMN_LONGITUDE, longitude);
        values.put(COLUMN_LOCATION_NAME, locationName);

        return db.update(TABLE_NAME, values, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
    }

    // Method to delete a donation record
    public void deleteDonation(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    // Method to count total donations
    public int getDonationCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT COUNT(*) FROM " + TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        return count;
    }
}
