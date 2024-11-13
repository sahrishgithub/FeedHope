package com.example.feedhope.ReceiverInterface.ReceiverRegister;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.feedhope.ReceiverInterface.InformDonation.InformDonationModalClass;

import java.util.ArrayList;

public class ReceiverRegisterDB extends SQLiteOpenHelper {
    private static final String DBName="FeedHopeProject.db";
    public ReceiverRegisterDB(@Nullable Context context) {

        super(context, DBName, null, 42);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(" create table ReceiverRegister(Organization_Refrence TEXT NOT NULL, Organization_Type TEXT NOT NULL,Members TEXT NOT NULL, Requirement TEXT NOT NULL, Frequency TEXT NOT NULL, Time TEXT NOT NULL,Phone TEXT NOT NULL,Email TEXT Primary key,Pass TEXT NOT NULL,location TEXT NOT NULL)");
     }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS ReceiverRegister");
        onCreate(db);
    }
    public boolean insertData(String Organization_Refrence,String Organization_Type,String Members,String Requirement,String Frequency,String Time,String Phone, String Email,String Pass,String location) {
        try (SQLiteDatabase myDB = this.getWritableDatabase()) {
            ContentValues cv = new ContentValues();
            cv.put("Organization_Refrence", Organization_Refrence);
            cv.put("Organization_Type", Organization_Type);
            cv.put("Members", Members);
            cv.put("Requirement", Requirement);
            cv.put("Frequency", Frequency);
            cv.put("Time", Time);
            cv.put("Phone", Phone);
            cv.put("Email", Email);
            cv.put("Pass", Pass);
            cv.put("location",location);
            long result = myDB.insert("ReceiverRegister", null, cv);
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

    public boolean checkUser(String email, String pass) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM ReceiverRegister WHERE email = ? AND pass = ?", new String[]{email, pass});
        if (cursor.getCount() > 0) {
            cursor.close();
            return true;
        }
        cursor.close();
        return false;
    }
}