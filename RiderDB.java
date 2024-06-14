package com.example.feedhope;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;
public class RiderDB extends SQLiteOpenHelper {
    private static final String DBName="Project.db";
    public RiderDB(@Nullable Context context) {

        super(context, DBName, null, 4);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table signupTable(username TEXT,phone TEXT,email TEXT,pass TEXT)");
        db.execSQL(" create table ProviderRegister(food TEXT,quantity TEXT,storage TEXT ,available TEXT, expire TEXT, things TEXT)");
        db.execSQL(" create table RiderRegister(IDtype TEXT, IDNumber TEXT, Working_Hours TEXT,Availability_Days TEXT, Bank_detail TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 4) {
            db.execSQL(" create table RiderRegister(IDtype TEXT, IDNumber TEXT, Working_Hours TEXT,Availability_Days TEXT, Bank_Detail TEXT)");
        }
    }
    public boolean insertData(String IDtype, String IDNumber,String Working_Hours,String Availability_Days,String Bank_Detail) {
        try (SQLiteDatabase myDB = this.getWritableDatabase()) {
            ContentValues cv = new ContentValues();
            cv.put("IDtype", IDtype);
            cv.put("IDNumber", IDNumber);
            cv.put("Working_Hours", Working_Hours);
            cv.put("Availability_Days", Availability_Days);
            cv.put("Bank_Detail", Bank_Detail);
            long result = myDB.insert("RiderRegister", null, cv);
            myDB.close();
            if (result == -1) {
                Log.e("RiderDB", "Failed to insert data");
                return false;
            } else {
                Log.d("RiderDB", "Data inserted successfully");
                return true;
            }
        }
    }

}