package com.example.forgotpassword;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "user_db"; // Database name
    private static final int DATABASE_VERSION = 1; // Database version
    private static final String TABLE_USERS = "users"; // Table name

    // Columns
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_PASSWORD = "password"; // User password
    private static final String COLUMN_TOKEN = "token"; // Password reset token
    private static final String COLUMN_TOKEN_EXPIRATION = "token_expiration"; // Token expiration time

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_USERS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_EMAIL + " TEXT NOT NULL UNIQUE, " +  // Ensure email is unique
                COLUMN_PASSWORD + " TEXT NOT NULL, " + // Ensure to store user passwords
                COLUMN_TOKEN + " TEXT, " + // Token can be NULL initially
                COLUMN_TOKEN_EXPIRATION + " INTEGER);"; // Token expiration can be NULL initially
        db.execSQL(createTable);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    // Method to insert a new user into the database
    public boolean insertUser(String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_PASSWORD, password);

        long result = db.insert(TABLE_USERS, null, values);
        db.close();

        // Return true if the insert was successful, otherwise false
        return result != -1;
    }

    // Method to verify if the token is valid for the given email
    public boolean verifyToken(String email, String token) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_USERS +
                " WHERE " + COLUMN_EMAIL + " = ? AND " +
                COLUMN_TOKEN + " = ? AND " +
                COLUMN_TOKEN_EXPIRATION + " > ?";
        long currentTimeMillis = System.currentTimeMillis();

        try (Cursor cursor = db.rawQuery(query, new String[]{email, token, String.valueOf(currentTimeMillis)})) {
            return cursor.getCount() > 0; // If there's a result, the token is valid
        } finally {
            db.close();
        }
    }

    // Method to check if a user exists based on their email
    public boolean checkUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_EMAIL + " = ?";

        try (Cursor cursor = db.rawQuery(query, new String[]{email})) {
            return cursor.getCount() > 0; // If there's a result, the user exists
        } finally {
            db.close();
        }
    }

    // Method to check if the email is valid (exists in the database)
    public boolean checkEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_EMAIL + " = ?";

        try (Cursor cursor = db.rawQuery(query, new String[]{email})) {
            return cursor.getCount() > 0; // If there's a result, the email exists
        } finally {
            db.close();
        }
    }

    // Method to check if the email and password match
    public boolean checkEmailPassword(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_EMAIL + " = ? AND " + COLUMN_PASSWORD + " = ?";

        try (Cursor cursor = db.rawQuery(query, new String[]{email, password})) {
            return cursor.getCount() > 0; // If there's a result, the email and password match
        } finally {
            db.close();
        }
    }

    // Method to store the reset token for a user
    public boolean storeResetToken(String email, String token, long expiration) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_TOKEN, token);
        values.put(COLUMN_TOKEN_EXPIRATION, expiration);

        // Update the token and expiration if the user exists
        int rowsAffected = db.update(TABLE_USERS, values, COLUMN_EMAIL + " = ?", new String[]{email});
        db.close();

        return rowsAffected > 0; // Return true if token was updated, false if user does not exist
    }

    // Method to retrieve the user password based on the email
    public String getPasswordByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + COLUMN_PASSWORD + " FROM " + TABLE_USERS + " WHERE " + COLUMN_EMAIL + " = ?";

        Cursor cursor = null;
        try {
            cursor = db.rawQuery(query, new String[]{email});

            if (cursor != null && cursor.moveToFirst()) {
                int passwordColumnIndex = cursor.getColumnIndex(COLUMN_PASSWORD);
                if (passwordColumnIndex != -1) { // Ensure the column exists
                    return cursor.getString(passwordColumnIndex); // Return the password
                } else {
                    // Handle case where the column does not exist
                    return null; // or throw an exception, or return an appropriate message
                }
            }
        } finally {
            if (cursor != null) {
                cursor.close(); // Ensure cursor is closed
            }
            db.close(); // Always close the database
        }
        return null; // No password found
    }

    // Method to reset the user password
    public boolean resetPassword(String email, String newPassword) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PASSWORD, newPassword);

        int rowsAffected = db.update(TABLE_USERS, values, COLUMN_EMAIL + " = ?", new String[]{email});
        db.close();

        return rowsAffected > 0; // Return true if password was updated
    }

    // Method to insert user data into the database
    public boolean insertData(String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_PASSWORD, password);

        long result = db.insert(TABLE_USERS, null, values);
        db.close();

        // Return true if the insert was successful, otherwise false
        return result != -1;
    }

    // Additional methods for inserting/updating/deleting users, etc. can be added here
}
