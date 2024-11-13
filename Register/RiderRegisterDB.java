package com.example.feedhope.RiderInterface.Register;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class RiderRegisterDB extends SQLiteOpenHelper {
    private static final String DBName="FeedHopeProject.db";
    public RiderRegisterDB(@Nullable Context context) {

        super(context, DBName, null, 42);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(" create table RiderRegister(Name TEXT NOT NULL,Phone TEXT NOT NULL,LicenseNo TEXT NOT NULL,Working_Hours TEXT NOT NULL,Availability_Days TEXT, Card TEXT NOT NULL,Email TEXT Primary key,Pass TEXT NOT NULL,location TEXT NOT NULL)");
      }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS RiderRegister");
        onCreate(db);
    }
//    public void deleteTable() {
//        SQLiteDatabase db = this.getWritableDatabase();
//        db.execSQL("DROP TABLE IF EXISTS RiderRegister");
//        db.close();
//    }
    public boolean insertData(String Name,String Phone, String Licence,String Working_Hours,String Availability_Days,String Card,String Email,String Pass,String location) {
        try (SQLiteDatabase myDB = this.getWritableDatabase()) {
            ContentValues cv = new ContentValues();
            cv.put("Name", Name);
            cv.put("Phone", Phone);
            cv.put("LicenseNo", Licence);
            cv.put("Working_Hours", Working_Hours);
            cv.put("Availability_Days", Availability_Days);
            cv.put("Card", Card);
            cv.put("Email", Email);
            cv.put("Pass", Pass);
            cv.put("location",location);

            long result = myDB.insert("RiderRegister", null, cv);
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
        Cursor cursor = db.rawQuery("SELECT * FROM RiderRegister WHERE email = ? AND pass = ?", new String[]{email, pass});
        if (cursor.getCount() > 0) {
            cursor.close();
            return true;
        }
        cursor.close();
        return false;
    }
    public RiderModalClass read(String loggedInEmail) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("RiderRegister", null, "Email = ?", new String[]{loggedInEmail}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow("Name"));
            String phone = cursor.getString(cursor.getColumnIndexOrThrow("Phone"));
            String email = cursor.getString(cursor.getColumnIndexOrThrow("Email"));
            String pass = cursor.getString(cursor.getColumnIndexOrThrow("Pass"));
            String location=cursor.getString(cursor.getColumnIndexOrThrow("location"));
            cursor.close();
            return new RiderModalClass(name, phone, email, pass,location);
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
        values.put("location",riderModalClass.getLocation());

        return db.update("RiderRegister", values, "Email = ?", new String[]{riderModalClass.getEmail()});
    }
}