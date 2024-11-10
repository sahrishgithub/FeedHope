package com.example.feedhope.ProviderInterface.PaymentDonation;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.feedhope.ProviderInterface.ToyDonation.ToyModalClass;

import java.util.ArrayList;

public class Payment_FormDB extends SQLiteOpenHelper {
    private static final String DBName = "FeedHopeProject.db";

    public Payment_FormDB(@Nullable Context context) {
        super(context, DBName, null, 19);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table Payment_Donation(Name TEXT NOT NULL,Amount TEXT NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Payment_Donation");
        onCreate(db);
    }

    public boolean insertData(String Name, String Amount) {
        try (SQLiteDatabase mydb = this.getWritableDatabase()) {
            ContentValues cv = new ContentValues();
            cv.put("Name", Name);
            cv.put("Amount", Amount);
            long result = mydb.insert("Payment_Donation", null, cv);
            mydb.close();
            if (result == -1) {
                Log.e("Payment_FormDB", "Failed to insert data");
                return false;
            } else {
                Log.d("Payment_FormDB", "Data inserted successfully");
                return true;
            }
        }
    }
    public ArrayList<PaymentModalClass> readPaymentData() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Payment_Donation", null);
        ArrayList<PaymentModalClass> modalClasses = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(0);
                String amount = cursor.getString(1);
                modalClasses.add(new PaymentModalClass(name,amount));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return modalClasses;
    }
}