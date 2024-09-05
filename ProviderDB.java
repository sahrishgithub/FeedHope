package com.example.unitconverter.ProviderInterface;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class ProviderDB extends SQLiteOpenHelper {
    private static final String DBName="FeedHopeProject.db";
    public ProviderDB(@Nullable Context context) {
        super(context, DBName, null, 6);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table ProviderRegister(Name TEXT NOT NULL,Phone TEXT NOT NULL,Email TEXT Primary key,Pass TEXT NOT NULL)");
        db.execSQL(" create table FoodDonation(FoodType TEXT Not Null,Quantity TEXT Primary key,Storage TEXT Not Null,AvailableDate TEXT Not Null, ExpireDate TEXT Not Null)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS ProviderRegister");
        db.execSQL(" drop table if exists FoodDonation");
        onCreate(db);
    }

    public boolean insert(String name,String phone, String email,String pass){
        try(SQLiteDatabase mydb = this.getWritableDatabase()) {
            ContentValues cv = new ContentValues();
            cv.put("Name", name);
            cv.put("Phone", phone);
            cv.put("Email", email);
            cv.put("Pass", pass);
            long result = mydb.insert("ProviderRegister", null, cv);
            mydb.close();
            if (result == -1) {
                Log.e("ProviderRegisterDB", "Failed to insert data");
                return false;
            } else {
                Log.d("ProviderRegisterDB", "Data inserted successfully");
                return true;
            }
        }
    }

    public boolean insertData(String FoodType, String Quantity,String Storage,String AvailableDate,String ExpireDate) {
        try (SQLiteDatabase myDB = this.getWritableDatabase()) {
            ContentValues cv = new ContentValues();
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
    public boolean checkUser(String email, String pass) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM ProviderRegister WHERE email = ? AND pass = ?", new String[]{email, pass});
        if (cursor.getCount() > 0) {
            cursor.close();
            return true;
        }
        cursor.close();
        return false;
    }

    public ProviderModalClass read(String loggedInEmail) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("ProviderRegister", null, "Email = ?", new String[]{loggedInEmail}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow("Name"));
            String phone = cursor.getString(cursor.getColumnIndexOrThrow("Phone"));
            String email = cursor.getString(cursor.getColumnIndexOrThrow("Email"));
            String pass = cursor.getString(cursor.getColumnIndexOrThrow("Pass"));
            cursor.close();
            return new ProviderModalClass(name, phone, email, pass);
        } else {
            return null;
        }
    }

    public long update(ProviderModalClass provider) {
        if (provider == null || provider.getEmail() == null) {
            Log.e("ProviderRegisterDB", "Provider or email is null");
            return -1;
        }

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("Name", provider.getName());
        values.put("Phone", provider.getPhone());
        values.put("Email", provider.getEmail());
        values.put("Pass", provider.getPass());

        return db.update("ProviderRegister", values, "Email = ?", new String[]{provider.getEmail()});
    }

    public ArrayList<ProvideFoodModalClass> readFoodData() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM FoodDonation", null);
        ArrayList<ProvideFoodModalClass> foodModalClasses = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                String foodType = cursor.getString(0);
                String quantity = cursor.getString(1);
                String storage = cursor.getString(2);
                String availableDate = cursor.getString(3);
                String expireDate = cursor.getString(4);
                foodModalClasses.add(new ProvideFoodModalClass(foodType, quantity, storage, availableDate, expireDate));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return foodModalClasses;
    }
}