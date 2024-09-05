package com.example.unitconverter.ReceiverInterface;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class ReceiverRegisterDB extends SQLiteOpenHelper {
    private static final String DBName="FeedHopeProject.db";
    public ReceiverRegisterDB(@Nullable Context context) {

        super(context, DBName, null, 6);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(" create table ReceiverRegister(Organization_Refrence TEXT NOT NULL, Organization_Type TEXT NOT NULL,Members TEXT NOT NULL, Requirement TEXT NOT NULL, Frequency TEXT NOT NULL, Time TEXT NOT NULL,Phone TEXT NOT NULL,Email TEXT Primary key,Pass TEXT NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS ReceiverRegister");
        onCreate(db);
    }
    public boolean insertData(String Organization_Refrence,String Organization_Type,String Members,String Requirement,String Frequency,String Time,String Phone, String Email,String Pass) {
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
    public ReceiverModalClass read(String loggedInEmail) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("ReceiverRegister", null, "Email = ?", new String[]{loggedInEmail}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            String reference = cursor.getString(cursor.getColumnIndexOrThrow("Organization_Refrence"));
            String phone = cursor.getString(cursor.getColumnIndexOrThrow("Phone"));
            String email = cursor.getString(cursor.getColumnIndexOrThrow("Email"));
            String pass = cursor.getString(cursor.getColumnIndexOrThrow("Pass"));
            cursor.close();
            return new ReceiverModalClass(reference, phone, email, pass);
        } else {
            return null;
        }
    }

    public long update(ReceiverModalClass receiver) {
        if (receiver == null || receiver.getEmail() == null) {
            Log.e("ReceiverRegisterDB", "Provider or email is null");
            return -1;
        }

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("Organization_Refrence", receiver.getReference());
        values.put("Phone", receiver.getPhone());
        values.put("Email", receiver.getEmail());
        values.put("Pass", receiver.getPass());

        return db.update("ReceiverRegister", values, "Email = ?", new String[]{receiver.getEmail()});
    }
}