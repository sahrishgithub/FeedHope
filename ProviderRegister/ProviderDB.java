package com.example.feedhope.ProviderInterface.ProviderRegister;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class ProviderDB extends SQLiteOpenHelper {
    private static final String DBName="FeedHopeProject.db";
    public ProviderDB(@Nullable Context context) {
        super(context, DBName, null, 42);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table ProviderRegister( " +
                "Name TEXT NOT NULL,\n" +
                "    Phone TEXT NOT NULL,\n" +
                "    Email TEXT PRIMARY KEY,\n" +
                "    Pass TEXT NOT NULL,\n" +
                "    location TEXT NOT NULL)");
       }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS ProviderRegister");
        onCreate(db);
    }

    public boolean insert(String name, String phone, String email, String pass, String location) {
        try(SQLiteDatabase mydb = this.getWritableDatabase()) {
            ContentValues cv = new ContentValues();
            cv.put("Name", name);
            cv.put("Phone", phone);
            cv.put("Email", email);
            cv.put("Pass", pass);
            cv.put("Location", location);
            long result = mydb.insert("ProviderRegister", null, cv);
            Log.d("ProviderDB", "Inserting: " + location);
            mydb.close();
            return result != -1;
        }
    }

    public boolean checkUser(String email, String pass) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM ProviderRegister WHERE email = ? AND pass = ?", new String[]{email, pass});
        if (cursor.getCount() > 0) {
            cursor.close();
            return true;
        }
        cursor.close();
        return false;
    }

    public ProviderModalClass read(String loggedInEmail) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("ProviderRegister", null, "Email = ?", new String[]{loggedInEmail}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow("Name"));
            String phone = cursor.getString(cursor.getColumnIndexOrThrow("Phone"));
            String email = cursor.getString(cursor.getColumnIndexOrThrow("Email"));
            String pass = cursor.getString(cursor.getColumnIndexOrThrow("Pass"));
            String location = cursor.getString(cursor.getColumnIndexOrThrow("location"));
            cursor.close();
            return new ProviderModalClass(name, phone, email, pass, location);
        }
        cursor.close();
        return null;
    }


    public long update(ProviderModalClass provider) {
        if (provider == null || provider.getEmail() == null) {
            Log.e("ProviderRegisterDB", "Provider or email is null");
            return -1;
        }
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("Name", provider.getName());
        values.put("Phone", provider.getPhone());
        values.put("Email", provider.getEmail());
        values.put("Pass", provider.getPass());
        values.put("location",provider.getLocation());
        return db.update("ProviderRegister", values, "Email = ?", new String[]{provider.getEmail()});
    }
}