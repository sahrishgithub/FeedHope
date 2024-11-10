package com.example.feedhope.ProviderInterface.ClothDonation;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class ClothDB extends SQLiteOpenHelper {
    private static final String DBName="FeedHopeProject.db";
    public ClothDB(@Nullable Context context) {
        super(context, DBName, null, 19);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table ClothDonation(Name TEXT NOT NULL,Type TEXT NOT NULL,Condition TEXT NOT NULL,Quantity TEXT ,Category TEXT NOT NULL,Seasonal TEXT NOT NULL,Size TEXT NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS ClothDonation");
        onCreate(db);
    }

    public boolean insert(String name,String type,String condition, String quantity,String category,String seasonal,String size){
        try(SQLiteDatabase mydb = this.getWritableDatabase()) {
            ContentValues cv = new ContentValues();
            cv.put("Name",name);
            cv.put("Type", type);
            cv.put("Condition", condition);
            cv.put("Quantity", quantity);
            cv.put("Category", category);
            cv.put("Seasonal", seasonal);
            cv.put("Size", size);
            long result = mydb.insert("ClothDonation", null, cv);
            mydb.close();
            if (result == -1) {
                Log.e("ClothDB", "Failed to insert data");
                return false;
            } else {
                Log.d("ClothDB", "Data inserted successfully");
                return true;
            }
        }
    }

    public ArrayList<ClothModelClass> readClothData() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM ClothDonation", null);
        ArrayList<ClothModelClass> modalClasses = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(0);
                String type = cursor.getString(1);
                String condition = cursor.getString(2);
                String quantity = cursor.getString(3);
                String category = cursor.getString(4);
                String seasonal = cursor.getString(5);
                String size = cursor.getString(6);
                modalClasses.add(new ClothModelClass(name,type, condition, quantity, category, seasonal,size));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return modalClasses;
    }
}