package com.example.feedhope.ProviderInterface.ToyDonation;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class ToyDB extends SQLiteOpenHelper {
    private static final String DBName="FeedHopeProject.db";
    public ToyDB(@Nullable Context context) {
        super(context, DBName, null, 19);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table ToyDonation(Name TEXT NOT NULL,ToyName TEXT NOT NULL,Age TEXT NOT NULL,Quantity TEXT NOT NULL,Condition TEXT NOT NULL,Category TEXT NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS ToyDonation");
        onCreate(db);
    }

    public boolean insert(String Name,String ToyName,String Age, String Quantity,String Condition,String Category){
        try(SQLiteDatabase mydb = this.getWritableDatabase()) {
            ContentValues cv = new ContentValues();
            cv.put("Name",Name);
            cv.put("ToyName", ToyName);
            cv.put("Age", Age);
            cv.put("Quantity", Quantity);
            cv.put("Condition", Condition);
            cv.put("Category", Category);
            long result = mydb.insert("ToyDonation", null, cv);
            mydb.close();
            if (result == -1) {
                Log.e("ToyDB", "Failed to insert data");
                return false;
            } else {
                Log.d("ToyDB", "Data inserted successfully");
                return true;
            }
        }
    }

    public ArrayList<ToyModalClass> readToyData() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM ToyDonation", null);
        ArrayList<ToyModalClass> modalClasses = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(0);
                String toyName = cursor.getString(1);
                String age = cursor.getString(2);
                String quantity = cursor.getString(3);
                String condition = cursor.getString(4);
                String category = cursor.getString(5);
                modalClasses.add(new ToyModalClass(name,toyName, age, quantity, condition, category));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return modalClasses;
    }
}