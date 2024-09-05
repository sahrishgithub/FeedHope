package com.example.unitconverter.RiderInterface;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import androidx.annotation.Nullable;

import com.example.unitconverter.ProviderInterface.ProvideFoodModalClass;

import java.util.ArrayList;

public class RiderAssignDutyDB extends SQLiteOpenHelper {
    private static final String DBName="FeedHopeProject.db";
    public RiderAssignDutyDB(@Nullable Context context) {
        super(context, DBName, null, 6);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
         db.execSQL(" create table RiderAssignDuty(Email TEXT NOT NULL,Pick_Location TEXT NOT NULL,Drop_Location TEXT NOT NULL, Date datetime NOT NULL, Status TEXT NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS RiderAssignDuty");
        onCreate(db);
    }

    public boolean assignDuty(String Email,String Pick,String Drop, String Date,String Status) {
        try (SQLiteDatabase myDB = this.getWritableDatabase()) {
            ContentValues cv = new ContentValues();
            cv.put("Email", Email);
            cv.put("Pick_Location", Pick);
            cv.put("Drop_Location", Drop);
            cv.put("Date", Date);
            cv.put("Status",Status);

            long result = myDB.insert("RiderAssignDuty", null, cv);
            myDB.close();
            if (result == -1) {
                Log.e("RiderRegisterDB", "Failed to insert data");
                return false;
            } else {
                Log.d("RiderRegisterDB", "Data inserted successfully");
                return true;
            }
        }
    }

    public ArrayList<DutyModalClass> readDutyData() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM RiderAssignDuty", null);
        ArrayList<DutyModalClass> dutyModalClasses = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(0);
                String pick = cursor.getString(1);
                String drop = cursor.getString(2);
                String date = cursor.getString(3);
                String status = cursor.getString(4);
                dutyModalClasses.add(new DutyModalClass(name, pick, drop, date,status));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return dutyModalClasses;
    }

    public boolean updateDutyStatus(String Email, String status) {
        try (SQLiteDatabase db = this.getWritableDatabase()) {
            ContentValues cv = new ContentValues();
            cv.put("Status", status);

            int result = db.update("RiderAssignDuty", cv, "Email = ?", new String[]{Email});
            return result > 0;
        }
    }
}