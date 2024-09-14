package com.example.unitconverter.AppInterface;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class toy_database extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "donation.db";
    private static final int DATABASE_VERSION = 2;

    // Table and column names
    public static final String TABLE_DONATIONS = "donations";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_FIRST_NAME = "first_name";
    public static final String COLUMN_LAST_NAME = "last_name";
    public static final String COLUMN_PHONE = "phone";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_STREET_ADDRESS = "street_address";
    public static final String COLUMN_STREET_ADDRESS_LINE2 = "street_address_line2";
    public static final String COLUMN_CITY = "city";
    public static final String COLUMN_STATE = "state";
    public static final String COLUMN_POSTAL_CODE = "postal_code";
    public static final String COLUMN_DONATE_WHAT = "donate_what";
    public static final String COLUMN_NOTES = "notes";

    public static final String COLUMN_DOLL = "doll";
    public static final String COLUMN_PLUSH_TOYS = "plush_toys";
    public static final String COLUMN_CAR = "car";
    public static final String COLUMN_MUSICAL_TOYS = "musical_toys";
    public static final String COLUMN_PUZZLES = "puzzles"; // corrected spelling
    public static final String COLUMN_YO_YOYS = "yo_yoys";
    public static final String COLUMN_BICYCLES = "bicycles";
    public static final String COLUMN_TOY_HORSES = "toy_horses";
    public static final String COLUMN_RATTLES = "rattles";
    public static final String COLUMN_KITCHEN_TOYS = "kitchen_toys";
    public static final String COLUMN_SQUEEZE = "squeeze_and_squeak_toys"; // updated name

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_DONATIONS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COLUMN_FIRST_NAME + " TEXT," +
                    COLUMN_LAST_NAME + " TEXT," +
                    COLUMN_PHONE + " TEXT," +
                    COLUMN_EMAIL + " TEXT," +
                    COLUMN_STREET_ADDRESS + " TEXT," +
                    COLUMN_STREET_ADDRESS_LINE2 + " TEXT," +
                    COLUMN_CITY + " TEXT," +
                    COLUMN_STATE + " TEXT," +
                    COLUMN_POSTAL_CODE + " TEXT," +
                    COLUMN_DONATE_WHAT + " TEXT," +
                    COLUMN_NOTES + " TEXT," +
                    COLUMN_DOLL + " INTEGER," +
                    COLUMN_PLUSH_TOYS + " INTEGER," +
                    COLUMN_CAR + " INTEGER," +
                    COLUMN_MUSICAL_TOYS + " INTEGER," +
                    COLUMN_PUZZLES + " INTEGER," +
                    COLUMN_YO_YOYS + " INTEGER," +
                    COLUMN_BICYCLES + " INTEGER," +
                    COLUMN_TOY_HORSES + " INTEGER," +
                    COLUMN_RATTLES + " INTEGER," +
                    COLUMN_KITCHEN_TOYS + " INTEGER," +
                    COLUMN_SQUEEZE + " INTEGER)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TABLE_DONATIONS;

    public toy_database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
}
