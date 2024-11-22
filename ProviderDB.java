package com.example.feedhope.ProviderInterface.ProviderRegister;

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
        super(context, DBName, null, 47);
    }

    public ArrayList<String> getAllProviderLocations() {
        ArrayList<String> locations = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT Location FROM RegisterProvider", null);
        if (cursor != null) {
            try {
                int columnIndex = cursor.getColumnIndex("Location");
                if (columnIndex != -1) {  // Ensure the column exists
                    while (cursor.moveToNext()) {
                        String location = cursor.getString(columnIndex); // Get the location
                        locations.add(location);
                    }
                } else {
                    Log.e("Database", "Column 'Location' not found.");
                }
            } catch (Exception e) {
                Log.e("Database", "Error retrieving data: " + e.getMessage());
            } finally {
                cursor.close();  // Ensure cursor is always closed
            }
        } else {
            Log.e("Database", "Cursor is null.");
        }
        return locations;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table RegisterProvider(Name TEXT NOT NULL,Phone TEXT NOT NULL,Email TEXT Primary key,Pass TEXT NOT NULL,Location TEXT NOT NULL)");
       }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS RegisterProvider");
        onCreate(db);
    }

    public boolean insert(String name,String phone, String email,String pass,String Location){
        try(SQLiteDatabase mydb = this.getWritableDatabase()) {
            ContentValues cv = new ContentValues();
            cv.put("Name", name);
            cv.put("Phone", phone);
            cv.put("Email", email);
            cv.put("Pass", pass);
            cv.put("Location",Location);
            long result = mydb.insert("RegisterProvider", null, cv);
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

    public boolean checkUser(String email, String pass) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM RegisterProvider WHERE email = ? AND pass = ?", new String[]{email, pass});
        if (cursor.getCount() > 0) {
            cursor.close();
            return true;
        }
        cursor.close();
        return false;
    }

    public ProviderModalClass read(String loggedInEmail) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("RegisterProvider", null, "Email = ?", new String[]{loggedInEmail}, null, null, null);

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

        return db.update("RegisterProvider", values, "Email = ?", new String[]{provider.getEmail()});
    }
}