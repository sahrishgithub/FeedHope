package com.example.feedhope.RiderInterface.Register;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class RiderRegisterDB extends SQLiteOpenHelper {
    private static final String DBName = "FeedHopeProject.db";

    public RiderRegisterDB(@Nullable Context context) {
        super(context, DBName, null, 47);
    }

    public ArrayList<String> getAllRiderEmails() {
        ArrayList<String> emails = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT Email FROM RegisterRider";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor != null && cursor.moveToFirst()) {
            int emailColumnIndex = cursor.getColumnIndex("Email"); // Correct column name
            if (emailColumnIndex != -1) {
                do {
                    String email = cursor.getString(emailColumnIndex);
                    emails.add(email);
                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        db.close();
        return emails;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(" create table RegisterRider(Name TEXT NOT NULL,Phone TEXT NOT NULL,LicenseNo TEXT NOT NULL,Working_Hours TEXT,Card INTEGER NOT NULL,Email TEXT Primary key,Pass TEXT NOT NULL,Location TEXT NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS RegisterRider");
        onCreate(db);
    }

    public void deleteTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS ShoeDonation");
        db.close();
    }

    public boolean insertData(String Name, String Phone, String Licence, String Working_Hours, long Card, String Email, String Pass, String Location) {
        try (SQLiteDatabase myDB = this.getWritableDatabase()) {
            ContentValues cv = new ContentValues();
            cv.put("Name", Name);
            cv.put("Phone", Phone);
            cv.put("LicenseNo", Licence);
            cv.put("Working_Hours", Working_Hours);
            cv.put("Card", Card);
            cv.put("Email", Email);
            cv.put("Pass", Pass);
            cv.put("Location", Location);

            long result = myDB.insert("RegisterRider", null, cv);
            myDB.close();
            if (result == -1) {
                Log.e("RiderRegisterDB", "Failed to insert data");
                return false;
            } else {
                Log.d("RiderRegisterDB", "Data inserted successfully");
                return true;
            }
        }
    }

    public boolean checkUser(String email, String pass) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM RegisterRider WHERE email = ? AND pass = ?", new String[]{email, pass});
        if (cursor.getCount() > 0) {
            cursor.close();
            return true;
        }
        cursor.close();
        return false;
    }

    public RiderModalClass read(String loggedInEmail) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("RegisterRider", null, "Email = ?", new String[]{loggedInEmail}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow("Name"));
            String phone = cursor.getString(cursor.getColumnIndexOrThrow("Phone"));
            String email = cursor.getString(cursor.getColumnIndexOrThrow("Email"));
            String pass = cursor.getString(cursor.getColumnIndexOrThrow("Pass"));
            cursor.close();
            return new RiderModalClass(name, phone, email, pass);
        } else {
            return null;
        }
    }

    public long update(RiderModalClass riderModalClass) {
        if (riderModalClass == null || riderModalClass.getEmail() == null) {
            Log.e("RiderRegisterDB", "Rider or email is null");
            return -1;
        }

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("Name", riderModalClass.getName());
        values.put("Phone", riderModalClass.getPhone());
        values.put("Email", riderModalClass.getEmail());
        values.put("Pass", riderModalClass.getPass());

        return db.update("RegisterRider", values, "Email = ?", new String[]{riderModalClass.getEmail()});
    }
}