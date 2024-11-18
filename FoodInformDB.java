package com.example.feedhope.ReceiverInterface.FoodInform;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import androidx.annotation.Nullable;

import com.example.feedhope.ReceiverInterface.ReceiverRegister.ReceiverModalClass;
import com.example.feedhope.RiderInterface.Duty.DutyModalClass;

import java.util.ArrayList;

public class FoodInformDB extends SQLiteOpenHelper {
    private static final String DBName="FeedHopeProject.db";
    public FoodInformDB(@Nullable Context context) {

        super(context, DBName, null, 43);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(" create table FoodInform(Organization_Name TEXT NOT NULL, Quantity TEXT NOT NULL,Storage TEXT NOT NULL, Expiry_Date TEXT NOT NULL, Status TEXT NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS FoodInform");
        onCreate(db);
    }

    public boolean insert(String Organization_Name,String Quantity,String Storage,String Date,String Status) {
        try (SQLiteDatabase myDB = this.getWritableDatabase()) {
            ContentValues cv = new ContentValues();
            cv.put("Organization_Name", Organization_Name);
            cv.put("Quantity", Quantity);
            cv.put("Storage", Storage);
            cv.put("Expiry_Date", Date);
            cv.put("Status", Status);
            long result = myDB.insert("FoodInform", null, cv);
            myDB.close();
            if (result == -1) {
                Log.e("ReceiverRegisterDB", "Failed to insert data");
                return false;
            } else {
                Log.d("ReceiverRegisterDB", "Data inserted successfully");
                return true;
            }
        }
    }

    public boolean updateInformStatus(String Organization_Name, String status) {
        try (SQLiteDatabase db = this.getWritableDatabase()) {
            ContentValues cv = new ContentValues();
            cv.put("Status", status);

            int result = db.update("FoodInform", cv, "Organization_Name = ?", new String[]{Organization_Name});
            return result > 0;
        }
    }

    public ArrayList<FoodInformModalClass> readFoodData() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM FoodInform", null);
        ArrayList<FoodInformModalClass> modalClasses = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(0);
                String quantity = cursor.getString(1);
                String storage = cursor.getString(2);
                String expire = cursor.getString(3);
                String status = cursor.getString(4);
                modalClasses.add(new FoodInformModalClass(name, quantity, storage, expire, status));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return modalClasses;
    }

    public ArrayList<FoodInformModalClass> readFoodInformation (String userEmail) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM FoodInform WHERE Organization_Name = ?", new String[]{userEmail});
        ArrayList<FoodInformModalClass> modalClasses = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(0);
                String quantity = cursor.getString(1);
                String storage = cursor.getString(2);
                String expire = cursor.getString(3);
                String status = cursor.getString(4);
                modalClasses.add(new FoodInformModalClass(name, quantity, storage, expire, status));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return modalClasses;
    }
}