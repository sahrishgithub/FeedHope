package com.example.feedhope;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class signupDB extends SQLiteOpenHelper {
    private static final String DBName="Project.db";
    public signupDB(@Nullable Context context) {
        super(context, DBName, null, 4);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table signupTable(username TEXT,phone TEXT,email TEXT,pass TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 4) {
            db.execSQL(" create table ProviderRegister(food TEXT,quantity TEXT,storage TEXT ,available TEXT, expire TEXT, things TEXT)");
        }
        }
    public boolean insert(String username,String phone, String email,String pass){
        try(SQLiteDatabase mydb = this.getWritableDatabase()) {
            ContentValues cv = new ContentValues();
            cv.put("username", username);
            cv.put("phone", phone);
            cv.put("email", email);
            cv.put("pass", pass);
            long result = mydb.insert("signupTable", null, cv);
            mydb.close();
            if (result == -1) {
                Log.e("signupDB", "Failed to insert data");
                return false;
            } else {
                Log.d("signupDB", "Data inserted successfully");
                return true;
            }
        }
    }

    public boolean checkUser(String email, String pass) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM signupTable WHERE email = ? AND pass = ?", new String[]{email, pass});
        if (cursor.getCount() > 0) {
            cursor.close();
            return true;
        }
        cursor.close();
        return false;
    }
}
