package com.example.feedhope.ProviderInterface.MedicineDonation;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class MedicineDB extends SQLiteOpenHelper {
    private static final String DBName="FeedHopeProject.db";
    public MedicineDB(@Nullable Context context) {
        super(context, DBName, null, 19);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table MedicineDonation(Name TEXT NOT NULL,MedicineName TEXT NOT NULL,Form TEXT NOT NULL,Quantity TEXT NOT NULL,Condition TEXT NOT NULL,Manufacture TEXT NOT NULL,Expire TEXT NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS MedicineDonation");
        onCreate(db);
    }

    public boolean insert(String Name,String MedicineName,String Form,String Quantity, String Condition,String Manufacture,String Expire){
        try(SQLiteDatabase mydb = this.getWritableDatabase()) {
            ContentValues cv = new ContentValues();
            cv.put("Name",Name);
            cv.put("MedicineName",MedicineName);
            cv.put("Form", Form);
            cv.put("Quantity", Quantity);
            cv.put("Condition", Condition);
            cv.put("Manufacture", Manufacture);
            cv.put("Expire", Expire);
            long result = mydb.insert("MedicineDonation", null, cv);
            mydb.close();
            if (result == -1) {
                Log.e("MedicineDB", "Failed to insert data");
                return false;
            } else {
                Log.d("MedicineDB", "Data inserted successfully");
                return true;
            }
        }
    }

    public ArrayList<MedicineModalClass> readMedicineData() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM MedicineDonation", null);
        ArrayList<MedicineModalClass> modalClasses = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(0);
                String medicineName = cursor.getString(1);
                String form = cursor.getString(2);
                String quantity = cursor.getString(3);
                String condition = cursor.getString(4);
                String manufacture = cursor.getString(5);
                String expire = cursor.getString(6);
                modalClasses.add(new MedicineModalClass(name,medicineName,form, quantity, condition, manufacture, expire));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return modalClasses;
    }
}