package com.example.feedhope.ProviderInterface.FoodDonation;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import androidx.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;

public class FoodDB extends SQLiteOpenHelper {
    private static final String DBName="FeedHopeProject.db";
    public FoodDB(@Nullable Context context) {
        super(context, DBName, null, 42);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(" create table FoodDonation(Name TEXT NOT NULL,FoodType TEXT Not Null,Quantity TEXT NOT NULL,Storage TEXT Not Null,AvailableDate TEXT Not Null, ExpireDate TEXT Not Null)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(" drop table if exists FoodDonation");
        onCreate(db);
    }

    public boolean insertData(String Name,String FoodType, String Quantity,String Storage,String AvailableDate,String ExpireDate) {
        try (SQLiteDatabase myDB = this.getWritableDatabase()) {
            ContentValues cv = new ContentValues();
            cv.put("Name",Name);
            cv.put("FoodType", FoodType);
            cv.put("Quantity", Quantity);
            cv.put("Storage", Storage);
            cv.put("AvailableDate", AvailableDate);
            cv.put("ExpireDate", ExpireDate);
            long result = myDB.insert("FoodDonation", null, cv);
            myDB.close();
            if (result == -1) {
                Log.e("ProviderFoodDB", "Failed to insert data");
                return false;
            } else {
                Log.d("ProviderFoodDB", "Data inserted successfully");
                return true;
            }
        }
    }

    public ArrayList<FoodProvideModalClass> readFoodData() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM FoodDonation", null);
        ArrayList<FoodProvideModalClass> foodModalClasses = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(0);
                String foodType = cursor.getString(1);
                String quantity = cursor.getString(2);
                String storage = cursor.getString(3);
                String availableDate = cursor.getString(4);
                String expireDate = cursor.getString(5);
                foodModalClasses.add(new FoodProvideModalClass(name, foodType, quantity, storage, availableDate, expireDate));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        // Reverse the list to show the latest data at the top
        Collections.reverse(foodModalClasses);
        return foodModalClasses;
    }

}