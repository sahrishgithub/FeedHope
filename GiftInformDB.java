package com.example.feedhope.ReceiverInterface.GiftInform;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class GiftInformDB extends SQLiteOpenHelper {
    private static final String DBName="FeedHopeProject.db";
    public GiftInformDB(@Nullable Context context) {

        super(context, DBName, null, 25);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(" create table DonationGift(Organization_Name TEXT NOT NULL, Quantity INTEGER NOT NULL,Category TEXT NOT NULL, Condition TEXT NOT NULL, Status TEXT NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS DonationGift");
        onCreate(db);
    }

    public boolean insert(String Organization_Name,String Quantity,String Category,String Condition,String Status) {
        try (SQLiteDatabase myDB = this.getWritableDatabase()) {
            ContentValues cv = new ContentValues();
            cv.put("Organization_Name", Organization_Name);
            cv.put("Quantity", Quantity);
            cv.put("Category", Category);
            cv.put("Condition", Condition);
            cv.put("Status", Status);
            long result = myDB.insert("DonationGift", null, cv);
            myDB.close();
            if (result == -1) {
                Log.e("Gift_DonationDB", "Failed to insert data");
                return false;
            } else {
                Log.d("Gift_DonationDB", "Data inserted successfully");
                return true;
            }
        }
    }

    public boolean updateGiftStatus(String Organization_Name, String status) {
        try (SQLiteDatabase db = this.getWritableDatabase()) {
            ContentValues cv = new ContentValues();
            cv.put("Status", status);

            int result = db.update("DonationGift", cv, "Organization_Name = ?", new String[]{Organization_Name});
            return result > 0;
        }
    }

    public ArrayList<GiftInformModalClass> readGiftData() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM DonationGift", null);
        ArrayList<GiftInformModalClass> modalClasses = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(0);
                String quantity = cursor.getString(1);
                String category = cursor.getString(2);
                String condition = cursor.getString(3);
                String status = cursor.getString(4);
                modalClasses.add(new GiftInformModalClass(name, quantity, category, condition, status));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return modalClasses;
    }
}