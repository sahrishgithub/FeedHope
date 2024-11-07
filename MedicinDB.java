package com.example.unitconverter.ProviderInterface;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MedicinDB extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "medicin_database.db";
    private static final int DATABASE_VERSION = 2; // Incremented version to handle schema changes

    // Medicine Donations table creation query
    private static final String CREATE_TABLE_MEDICINE_DONATIONS =
            "CREATE TABLE MedicineDonations (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "firstName TEXT, " +
                    "lastName TEXT, " +
                    "email TEXT, " +
                    "streetAddress TEXT, " +
                    "city TEXT, " +
                    "postalCode TEXT, " +
                    "phoneNumber TEXT, " +
                    "equipmentType TEXT, " +
                    "description TEXT, " +
                    "comments TEXT);"; // Added comments column here for table creation

    public MedicinDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_MEDICINE_DONATIONS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            // Add the Comments column in case it was missed in previous versions
            db.execSQL("ALTER TABLE MedicineDonations ADD COLUMN Comments TEXT;");
        }
    }

    // Insert method for saving donations
    public boolean insertData(String firstName, String lastName, String email, String streetAddress,
                              String city, String postalCode, String phoneNumber, String equipmentType,
                              String description, String comments) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "INSERT INTO MedicineDonations (firstName, lastName, email, streetAddress, city, postalCode, " +
                "phoneNumber, equipmentType, description, comments) VALUES ('" + firstName + "', '" + lastName + "', '" +
                email + "', '" + streetAddress + "', '" + city + "', '" + postalCode + "', '" + phoneNumber + "', '" +
                equipmentType + "', '" + description + "', '" + comments + "');";
        try {
            db.execSQL(query);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            db.close();
        }
    }
}
