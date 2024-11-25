package com.example.feedhope.ReceiverInterface.ReceiverRegister;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import androidx.annotation.Nullable;
import java.util.ArrayList;
public class ReceiverRegisterDB extends SQLiteOpenHelper {
    private static final String DBName="FeedHopeProject.db";
    public ReceiverRegisterDB(@Nullable Context context) {

        super(context, DBName, null, 48);
    }
    public ArrayList<String> getAllReceiverLocations() {
        ArrayList<String> locations = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();  // Properly gets the readable database instance
        Cursor cursor = db.rawQuery("SELECT Location FROM RegisterReceiver", null);

        if (cursor != null) {
            try {
                int columnIndex = cursor.getColumnIndex("Location");
                if (columnIndex != -1) {
                    while (cursor.moveToNext()) {
                        String location = cursor.getString(columnIndex);
                        locations.add(location);
                    }
                } else {
                    Log.e("Database", "Column 'Location' not found.");
                }
            } catch (Exception e) {
                Log.e("Database", "Error retrieving data: " + e.getMessage());
            } finally {
                cursor.close();
            }
        } else {
            Log.e("Database", "Cursor is null.");
        }
        return locations;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(" create table RegisterReceiver(Organization_Reference TEXT NOT NULL, Organization_Type TEXT NOT NULL,Members INTEGER NOT NULL, Frequency TEXT NOT NULL,Phone TEXT NOT NULL,CardNo INTEGER NOT NULL,Email TEXT Primary key,Pass TEXT NOT NULL,Location TEXT NOT NULL)");
     }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS RegisterReceiver");
        onCreate(db);
    }
    public boolean insertData(String Organization_Reference,String Organization_Type,int Members,String Frequency,String Phone,long CardNo, String Email,String Pass,String Location) {
        try (SQLiteDatabase myDB = this.getWritableDatabase()) {
            ContentValues cv = new ContentValues();
            cv.put("Organization_Reference", Organization_Reference);
            cv.put("Organization_Type", Organization_Type);
            cv.put("Members", Members);
            cv.put("Frequency", Frequency);
            cv.put("Phone", Phone);
            cv.put("CardNo",CardNo);
            cv.put("Email", Email);
            cv.put("Pass", Pass);
            cv.put("Location",Location);
            long result = myDB.insert("RegisterReceiver", null, cv);
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
        Cursor cursor = db.rawQuery("SELECT * FROM RegisterReceiver WHERE email = ? AND pass = ?", new String[]{email, pass});
        if (cursor.getCount() > 0) {
            cursor.close();
            return true;
        }
        cursor.close();
        return false;
    }

    public ReceiverModalClass read(String loggedInEmail) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("RegisterReceiver", null, "Email = ?", new String[]{loggedInEmail}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            String reference = cursor.getString(cursor.getColumnIndexOrThrow("Organization_Reference"));
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
            Log.e("ReceiverRegisterDB", "Receiver or email is null");
            return -1;
        }

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("Organization_Reference", receiver.getReference());
        values.put("Phone", receiver.getPhone());
        values.put("Email", receiver.getEmail());
        values.put("Pass", receiver.getPass());

        return db.update("RegisterReceiver", values, "Email = ?", new String[]{receiver.getEmail()});
    }
    public ArrayList<String> getAllReceiverName() {
        ArrayList<String> email1 = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();  // Properly gets the readable database instance
        Cursor cursor = db.rawQuery("SELECT Email FROM RegisterReceiver", null);
        if (cursor != null) {
            try {
                int columnIndex = cursor.getColumnIndex("Email");
                if (columnIndex != -1) {
                    while (cursor.moveToNext()) {
                        String email = cursor.getString(columnIndex);
                        email1.add(email);
                    }
                } else {
                    Log.e("Database", "Column 'Email' not found.");
                }
            } catch (Exception e) {
                Log.e("Database", "Error retrieving data: " + e.getMessage());
            } finally {
                cursor.close();
            }
        } else {
            Log.e("Database", "Cursor is null.");
        }
        return email1;
    }
}