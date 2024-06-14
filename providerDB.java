package com.example.feedhope;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;
public class providerDB extends SQLiteOpenHelper {
    private static final String DBName="Project.db";
    public providerDB(@Nullable Context context) {

        super(context, DBName, null, 4);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table signupTable(username TEXT,phone TEXT,email TEXT,pass TEXT)");
        db.execSQL(" create table ProviderRegister(food TEXT,quantity TEXT,storage TEXT ,available TEXT, expire TEXT, things TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion <4) {
            db.execSQL(" create table ProviderRegister(food TEXT,quantity TEXT,storage TEXT ,available TEXT, expire TEXT, things TEXT)");
        }
    }
    public boolean insertData(String food, String quantity,String storage,String available,String expire,String things) {
        try (SQLiteDatabase myDB = this.getWritableDatabase()) {
            ContentValues cv = new ContentValues();
            cv.put("food", food);
            cv.put("quantity", quantity);
            cv.put("storage", storage);
            cv.put("available", available);
            cv.put("expire", expire);
            cv.put("things", things);
            long result = myDB.insert("ProviderRegister", null, cv);
            myDB.close();
            if (result == -1) {
                Log.e("debhelper", "Failed to insert data");
                return false;
            } else {
                Log.d("debhelper", "Data inserted successfully");
                return true;
            }
        }
    }

}