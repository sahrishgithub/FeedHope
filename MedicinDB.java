package com.example.unitconverter.ProviderInterface;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MedicinDB extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "medicinDB";
    private static final int DATABASE_VERSION = 17;

    private static final String TABLE_NAME = "medicine_table";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_QUANTITY = "quantity";
    private static final String COLUMN_MANUFACTURE_DATE = "manufacture_date";
    private static final String COLUMN_EXPIRY_DATE = "expiry_date";
    private static final String COLUMN_FORM = "form";
    private static final String COLUMN_CONDITION = "condition";
    private static final String COLUMN_LOCATION = "location";
    private static final String COLUMN_LATITUDE = "latitude";
    private static final String COLUMN_LONGITUDE = "longitude";

    public MedicinDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_QUANTITY + " TEXT, " +
                COLUMN_MANUFACTURE_DATE + " TEXT, " +
                COLUMN_EXPIRY_DATE + " TEXT, " +
                COLUMN_FORM + " TEXT, " +
                COLUMN_CONDITION + " TEXT, " +
                COLUMN_LOCATION + " TEXT, " +
                COLUMN_LATITUDE + " REAL, " +
                COLUMN_LONGITUDE + " REAL)";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void addMedicine(String name, String quantity, String manufactureDate, String expiryDate,
                            String form, String condition, String location, double latitude, double longitude) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_QUANTITY, quantity);
        values.put(COLUMN_MANUFACTURE_DATE, manufactureDate);
        values.put(COLUMN_EXPIRY_DATE, expiryDate);
        values.put(COLUMN_FORM, form);
        values.put(COLUMN_CONDITION, condition);
        values.put(COLUMN_LOCATION, location);
        values.put(COLUMN_LATITUDE, latitude);
        values.put(COLUMN_LONGITUDE, longitude);

        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public Cursor getAllMedicines() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_NAME, null, null, null, null, null, null);
    }
}
