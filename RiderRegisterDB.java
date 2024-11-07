package com.example.unitconverter.RiderInterface;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class RiderRegisterDB extends SQLiteOpenHelper {
    private static final String DBName = "FeedHopeProject.db";
    private static final int DB_VERSION = 15;  // Incremented version

    public RiderRegisterDB(@Nullable Context context) {
        super(context, DBName, null, DB_VERSION);  // Updated version
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE RiderRegister(" +
                "Name TEXT NOT NULL, " +
                "Phone TEXT NOT NULL, " +
                "IDtype TEXT NOT NULL, " +
                "IDNumber TEXT NOT NULL, " +
                "Working_Hours TEXT NOT NULL, " +
                "Availability_Days TEXT NOT NULL, " +
                "Bank_Detail TEXT NOT NULL, " +
                "Email TEXT PRIMARY KEY, " +
                "Pass TEXT NOT NULL, " +
                "Latitude REAL, " +           // Latitude column
                "Longitude REAL, " +          // Longitude column
                "LocationName TEXT);");       // Location name column
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 12) {
            db.execSQL("ALTER TABLE RiderRegister ADD COLUMN Latitude REAL;");
            db.execSQL("ALTER TABLE RiderRegister ADD COLUMN Longitude REAL;");
            db.execSQL("ALTER TABLE RiderRegister ADD COLUMN LocationName TEXT;");
        }
    }

    public boolean insertData(String name1, String phone1, String selectedType, String idNumber1,
                              String selectedHours, String selectedDays, String banking1,
                              String email1, String pass1, double latitude, double longitude, String locationName) {
        SQLiteDatabase myDB = null;
        try {
            myDB = this.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put("Name", name1);
            cv.put("Phone", phone1);
            cv.put("IDtype", selectedType);
            cv.put("IDNumber", idNumber1);
            cv.put("Working_Hours", selectedHours);
            cv.put("Availability_Days", selectedDays);
            cv.put("Bank_Detail", banking1);
            cv.put("Email", email1);
            cv.put("Pass", pass1);
            cv.put("Latitude", latitude);
            cv.put("Longitude", longitude);
            cv.put("LocationName", locationName);

            // Log the values being inserted
            Log.d("RiderRegisterDB", "Inserting data: " + cv.toString());

            long result = myDB.insert("RiderRegister", null, cv);

            if (result == -1) {
                Log.e("RiderRegisterDB", "Insert failed.");
                return false;
            } else {
                Log.d("RiderRegisterDB", "Insert successful. Row ID: " + result);
                return true;
            }
        } catch (Exception e) {
            Log.e("RiderRegisterDB", "Insert data failed: " + e.getMessage());
            return false;
        } finally {
            if (myDB != null && myDB.isOpen()) {
                myDB.close();
            }
        }
    }

    public RiderModalClass read(String loggedInEmail) {
        SQLiteDatabase myDB = null;
        Cursor cursor = null;
        RiderModalClass rider = null;

        try {
            myDB = this.getReadableDatabase();
            String query = "SELECT * FROM RiderRegister WHERE Email = ?";
            cursor = myDB.rawQuery(query, new String[]{loggedInEmail});

            if (cursor != null && cursor.moveToFirst()) {
                // Initialize variables
                String name = null, phone = null, idType = null, idNumber = null;
                String workingHours = null, availabilityDays = null, bankDetail = null;
                String email = null, password = null, locationName = null;
                double latitude = 0, longitude = 0;

                // Get column indexes
                int indexName = cursor.getColumnIndex("Name");
                int indexPhone = cursor.getColumnIndex("Phone");
                int indexIDType = cursor.getColumnIndex("IDtype");
                int indexIDNumber = cursor.getColumnIndex("IDNumber");
                int indexWorkingHours = cursor.getColumnIndex("Working_Hours");
                int indexAvailabilityDays = cursor.getColumnIndex("Availability_Days");
                int indexBankDetail = cursor.getColumnIndex("Bank_Detail");
                int indexEmail = cursor.getColumnIndex("Email");
                int indexPass = cursor.getColumnIndex("Pass");
                int indexLatitude = cursor.getColumnIndex("Latitude");
                int indexLongitude = cursor.getColumnIndex("Longitude");
                int indexLocationName = cursor.getColumnIndex("LocationName");

                // Check if the indexes are valid and fetch data
                if (indexName >= 0) name = cursor.getString(indexName);
                if (indexPhone >= 0) phone = cursor.getString(indexPhone);
                if (indexIDType >= 0) idType = cursor.getString(indexIDType);
                if (indexIDNumber >= 0) idNumber = cursor.getString(indexIDNumber);
                if (indexWorkingHours >= 0) workingHours = cursor.getString(indexWorkingHours);
                if (indexAvailabilityDays >= 0) availabilityDays = cursor.getString(indexAvailabilityDays);
                if (indexBankDetail >= 0) bankDetail = cursor.getString(indexBankDetail);
                if (indexEmail >= 0) email = cursor.getString(indexEmail);
                if (indexPass >= 0) password = cursor.getString(indexPass);
                if (indexLatitude >= 0) latitude = cursor.getDouble(indexLatitude);
                if (indexLongitude >= 0) longitude = cursor.getDouble(indexLongitude);
                if (indexLocationName >= 0) locationName = cursor.getString(indexLocationName);

                // Create the RiderModalClass object
                rider = new RiderModalClass(name, phone, idType, idNumber, workingHours, availabilityDays,
                        bankDetail, email, password, latitude, longitude, locationName);
            }
        } catch (Exception e) {
            Log.e("RiderRegisterDB", "Error reading data: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (myDB != null && myDB.isOpen()) {
                myDB.close();
            }
        }

        return rider;
    }

    public boolean checkUser(String email1, String pass1) {
        SQLiteDatabase myDB = null;
        Cursor cursor = null;
        boolean userExists = false;

        try {
            myDB = this.getReadableDatabase();
            String query = "SELECT * FROM RiderRegister WHERE Email = ? AND Pass = ?";
            cursor = myDB.rawQuery(query, new String[]{email1, pass1});

            // Check if a record exists with the given email and password
            userExists = cursor != null && cursor.moveToFirst();
        } catch (Exception e) {
            Log.e("RiderRegisterDB", "Error checking user: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (myDB != null && myDB.isOpen()) {
                myDB.close();
            }
        }

        return userExists;
    }

    public long update(RiderModalClass riderModalClass) {
        SQLiteDatabase myDB = null;
        long result = -1;

        try {
            myDB = this.getWritableDatabase();
            ContentValues cv = new ContentValues();

            // Populate the ContentValues with data from the RiderModalClass object
            cv.put("Name", riderModalClass.getName());
            cv.put("Phone", riderModalClass.getPhone());
            cv.put("IDtype", riderModalClass.getIdType());
            cv.put("IDNumber", riderModalClass.getIdNumber());
            cv.put("Working_Hours", riderModalClass.getWorkingHours());
            cv.put("Availability_Days", riderModalClass.getAvailabilityDays());
            cv.put("Bank_Detail", riderModalClass.getBankDetail());
            cv.put("Pass", riderModalClass.getPassword());
            cv.put("Latitude", riderModalClass.getLatitude());
            cv.put("Longitude", riderModalClass.getLongitude());
            cv.put("LocationName", riderModalClass.getLocationName());

            // Perform the update based on the email (primary key)
            result = myDB.update("RiderRegister", cv, "Email = ?", new String[]{riderModalClass.getEmail()});

            // Log success or failure
            if (result == -1) {
                Log.e("RiderRegisterDB", "Update failed.");
            } else {
                Log.d("RiderRegisterDB", "Update successful for Email: " + riderModalClass.getEmail());
            }
        } catch (Exception e) {
            Log.e("RiderRegisterDB", "Error updating data: " + e.getMessage());
        } finally {
            if (myDB != null && myDB.isOpen()) {
                myDB.close();
            }
        }

        return result;
    }

}
