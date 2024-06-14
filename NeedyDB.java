package com.example.feedhope;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;
public class NeedyDB extends SQLiteOpenHelper {
    private static final String DBName="Project.db";
    public NeedyDB(@Nullable Context context) {

        super(context, DBName, null, 4);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table signupTable(username TEXT,phone TEXT,email TEXT,pass TEXT)");
        db.execSQL(" create table ProviderRegister(food TEXT,quantity TEXT,storage TEXT ,available TEXT, expire TEXT, things TEXT)");
        db.execSQL(" create table RiderRegister(IDtype TEXT, IDNumber TEXT, Working_Hours TEXT,Availability_Days TEXT, Bank_detail TEXT)");
        db.execSQL(" create table NeedyRegister(Members TEXT,Organization_Refrence TEXT, Organization_Type TEXT, Requirement TEXT, Frequency TEXT, Time TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 4) {
            db.execSQL(" create table NeedyRegister(Members TEXT,Organization_Refrence TEXT, Organization_Type TEXT, Requirement TEXT, Frequency TEXT, Time TEXT)");
        }
    }
    public boolean insertData(String Members, String Organization_Refrence,String Organization_Type,String Requirement,String Frequency,String Time) {
        try (SQLiteDatabase myDB = this.getWritableDatabase()) {
            ContentValues cv = new ContentValues();
            cv.put("Members", Members);
            cv.put("Organization_Refrence", Organization_Refrence);
            cv.put("Organization_Type", Organization_Type);
            cv.put("Requirement", Requirement);
            cv.put("Frequency", Frequency);
            cv.put("Time", Time);
            long result = myDB.insert("NeedyRegister", null, cv);
            myDB.close();
            if (result == -1) {
                Log.e("NeedyDB", "Failed to insert data");
                return false;
            } else {
                Log.d("NeedyDB", "Data inserted successfully");
                return true;
            }
        }
    }

}