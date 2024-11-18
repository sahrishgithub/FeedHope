package com.example.feedhope.ProviderInterface.ShoeDonation;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collections;

public class ShoeDB extends SQLiteOpenHelper {
    private static final String DBName="FeedHopeProject.db";
    public ShoeDB(@Nullable Context context) {
        super(context, DBName, null, 42);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table ShoeDonation(Name TEXT NOT NULL,ShoeType TEXT NOT NULL,Quantity TEXT NOT NULL,Condition TEXT NOT NULL,Gender TEXT Not NULL,Size TEXT NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS ShoeDonation");
        onCreate(db);
    }

    public boolean insert(String Name,String ShoeType,String Quantity,String Condition, String Gender,String Size){
        try(SQLiteDatabase mydb = this.getWritableDatabase()) {
            ContentValues cv = new ContentValues();
            cv.put("Name",Name);
            cv.put("ShoeType",ShoeType);
            cv.put("Quantity", Quantity);
            cv.put("Condition", Condition);
            cv.put("Gender", Gender);
            cv.put("Size", Size);
            long result = mydb.insert("ShoeDonation", null, cv);
            mydb.close();
            if (result == -1) {
                Log.e("ShoeDB", "Failed to insert data");
                return false;
            } else {
                Log.d("ShoeDB", "Data inserted successfully");
                return true;
            }
        }
    }

    public ArrayList<ShoeModalClass> readShoeData() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM ShoeDonation", null);
        ArrayList<ShoeModalClass> shoemodalClasses = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(0);
                String type = cursor.getString(1);
                String quantity = cursor.getString(2);
                String condition = cursor.getString(3);
                String gender = cursor.getString(4);
                String size = cursor.getString(5);
                shoemodalClasses.add(new ShoeModalClass(name,type, quantity, condition, gender,size));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        // Reverse the list to show the latest data at the top
        Collections.reverse(shoemodalClasses);
        return shoemodalClasses;
    }
}