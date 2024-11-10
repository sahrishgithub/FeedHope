package com.example.unitconverter.ReceiverInterface;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class ReceiverDB extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "ReceiverDB";
    private static final int DATABASE_VERSION = 2;

    // Table name
    private static final String TABLE_RECEIVER = "Receiver";

    // Columns for the Receiver table
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_REFERENCE = "reference";
    private static final String COLUMN_TYPE = "type";
    private static final String COLUMN_MEMBER = "member";
    private static final String COLUMN_REQUIREMENT = "requirement";
    private static final String COLUMN_FREQUENCY = "frequency";
    private static final String COLUMN_TIME = "time";
    private static final String COLUMN_PHONE = "phone";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_PASSWORD = "password";

    // New columns for receiver's current location
    private static final String COLUMN_LATITUDE = "latitude";
    private static final String COLUMN_LONGITUDE = "longitude";
    private static final String COLUMN_LOCATION_NAME = "location_name"; // New column for location name

    public ReceiverDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_RECEIVER + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_REFERENCE + " TEXT, " +
                COLUMN_TYPE + " TEXT, " +
                COLUMN_MEMBER + " TEXT, " +
                COLUMN_REQUIREMENT + " TEXT, " +
                COLUMN_FREQUENCY + " TEXT, " +
                COLUMN_TIME + " TEXT, " +
                COLUMN_PHONE + " TEXT, " +
                COLUMN_EMAIL + " TEXT, " +
                COLUMN_PASSWORD + " TEXT, " +
                COLUMN_LATITUDE + " REAL, " +  // New column for latitude
                COLUMN_LONGITUDE + " REAL, " + // New column for longitude
                COLUMN_LOCATION_NAME + " TEXT)"; // New column for location name
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE " + TABLE_RECEIVER + " ADD COLUMN " + COLUMN_LATITUDE + " REAL");
            db.execSQL("ALTER TABLE " + TABLE_RECEIVER + " ADD COLUMN " + COLUMN_LONGITUDE + " REAL");
            db.execSQL("ALTER TABLE " + TABLE_RECEIVER + " ADD COLUMN " + COLUMN_LOCATION_NAME + " TEXT");
        }
    }


    // Method to insert receiver data with location
//    public long insertReceiverData(String reference, String selectedType, String member, String selectedRequirement,
//                                   String frequency, String time, String phone, String email, String pass,
//                                   double latitude, double longitude, String locationName) {
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        ContentValues values = new ContentValues();
//        values.put(COLUMN_REFERENCE, reference);
//        values.put(COLUMN_TYPE, selectedType);
//        values.put(COLUMN_MEMBER, member);
//        values.put(COLUMN_REQUIREMENT, selectedRequirement);
//        values.put(COLUMN_FREQUENCY, frequency);
//        values.put(COLUMN_TIME, time);
//        values.put(COLUMN_PHONE, phone);
//        values.put(COLUMN_EMAIL, email);
//        values.put(COLUMN_PASSWORD, pass);
//
//        // Insert location data
//        values.put(COLUMN_LATITUDE, latitude);  // Store latitude
//        values.put(COLUMN_LONGITUDE, longitude);  // Store longitude
//        values.put(COLUMN_LOCATION_NAME, locationName); // Store location name
//
//        // Inserting row
//        long id = db.insert(TABLE_RECEIVER, null, values);
//        db.close();
//        return id;
//    }

    // Method to retrieve all receiver data including location
    public ArrayList<ReceiverModalClass> getAllReceivers() {
        ArrayList<ReceiverModalClass> receiverList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_RECEIVER;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                ReceiverModalClass receiver = new ReceiverModalClass();

                // Safely get column values
                int columnIndex;

                columnIndex = cursor.getColumnIndex(COLUMN_ID);
                if (columnIndex != -1) {
                    receiver.setId(cursor.getInt(columnIndex));
                }

                columnIndex = cursor.getColumnIndex(COLUMN_REFERENCE);
                if (columnIndex != -1) {
                    receiver.setReference(cursor.getString(columnIndex));
                }

                columnIndex = cursor.getColumnIndex(COLUMN_TYPE);
                if (columnIndex != -1) {
                    receiver.setType(cursor.getString(columnIndex));
                }

                columnIndex = cursor.getColumnIndex(COLUMN_MEMBER);
                if (columnIndex != -1) {
                    receiver.setMember(cursor.getString(columnIndex));
                }

                columnIndex = cursor.getColumnIndex(COLUMN_REQUIREMENT);
                if (columnIndex != -1) {
                    receiver.setRequirement(cursor.getString(columnIndex));
                }

                columnIndex = cursor.getColumnIndex(COLUMN_FREQUENCY);
                if (columnIndex != -1) {
                    receiver.setFrequency(cursor.getString(columnIndex));
                }

                columnIndex = cursor.getColumnIndex(COLUMN_TIME);
                if (columnIndex != -1) {
                    receiver.setTime(cursor.getString(columnIndex));
                }

                columnIndex = cursor.getColumnIndex(COLUMN_PHONE);
                if (columnIndex != -1) {
                    receiver.setPhone(cursor.getString(columnIndex));
                }

                columnIndex = cursor.getColumnIndex(COLUMN_EMAIL);
                if (columnIndex != -1) {
                    receiver.setEmail(cursor.getString(columnIndex));
                }

                columnIndex = cursor.getColumnIndex(COLUMN_PASSWORD);
                if (columnIndex != -1) {
                    receiver.setPassword(cursor.getString(columnIndex));
                }

                // Retrieve and set latitude, longitude, and location name
                columnIndex = cursor.getColumnIndex(COLUMN_LATITUDE);
                if (columnIndex != -1) {
                    receiver.setLatitude(cursor.getDouble(columnIndex));
                }

                columnIndex = cursor.getColumnIndex(COLUMN_LONGITUDE);
                if (columnIndex != -1) {
                    receiver.setLongitude(cursor.getDouble(columnIndex));
                }

                columnIndex = cursor.getColumnIndex(COLUMN_LOCATION_NAME);
                if (columnIndex != -1) {
                    receiver.setLocationName(cursor.getString(columnIndex)); // Set location name
                }

                receiverList.add(receiver);
            } while (cursor.moveToNext());
        }

        if (cursor != null) {
            cursor.close();
        }

        db.close();
        return receiverList;
    }

    // Method to clear all data
    public void clearReceivers() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_RECEIVER, null, null);
        db.close();
    }

    // Method to read a receiver based on the logged-in email
    public ReceiverModalClass read(String loggedInEmail) {
        SQLiteDatabase db = this.getReadableDatabase();
        ReceiverModalClass receiver = null;

        // Query to find the receiver by email
        String selectQuery = "SELECT * FROM " + TABLE_RECEIVER + " WHERE " + COLUMN_EMAIL + " = ?";
        Cursor cursor = db.rawQuery(selectQuery, new String[]{loggedInEmail});

        if (cursor != null && cursor.moveToFirst()) {
            receiver = new ReceiverModalClass();

            // Safely get column values
            int columnIndex;

            columnIndex = cursor.getColumnIndex(COLUMN_ID);
            if (columnIndex != -1) {
                receiver.setId(cursor.getInt(columnIndex));
            }

            columnIndex = cursor.getColumnIndex(COLUMN_REFERENCE);
            if (columnIndex != -1) {
                receiver.setReference(cursor.getString(columnIndex));
            }

            columnIndex = cursor.getColumnIndex(COLUMN_TYPE);
            if (columnIndex != -1) {
                receiver.setType(cursor.getString(columnIndex));
            }

            columnIndex = cursor.getColumnIndex(COLUMN_MEMBER);
            if (columnIndex != -1) {
                receiver.setMember(cursor.getString(columnIndex));
            }

            columnIndex = cursor.getColumnIndex(COLUMN_REQUIREMENT);
            if (columnIndex != -1) {
                receiver.setRequirement(cursor.getString(columnIndex));
            }

            columnIndex = cursor.getColumnIndex(COLUMN_FREQUENCY);
            if (columnIndex != -1) {
                receiver.setFrequency(cursor.getString(columnIndex));
            }

            columnIndex = cursor.getColumnIndex(COLUMN_TIME);
            if (columnIndex != -1) {
                receiver.setTime(cursor.getString(columnIndex));
            }

            columnIndex = cursor.getColumnIndex(COLUMN_PHONE);
            if (columnIndex != -1) {
                receiver.setPhone(cursor.getString(columnIndex));
            }

            columnIndex = cursor.getColumnIndex(COLUMN_EMAIL);
            if (columnIndex != -1) {
                receiver.setEmail(cursor.getString(columnIndex));
            }

            columnIndex = cursor.getColumnIndex(COLUMN_PASSWORD);
            if (columnIndex != -1) {
                receiver.setPassword(cursor.getString(columnIndex));
            }

            columnIndex = cursor.getColumnIndex(COLUMN_LATITUDE);
            if (columnIndex != -1) {
                receiver.setLatitude(cursor.getDouble(columnIndex));
            }

            columnIndex = cursor.getColumnIndex(COLUMN_LONGITUDE);
            if (columnIndex != -1) {
                receiver.setLongitude(cursor.getDouble(columnIndex));
            }

            columnIndex = cursor.getColumnIndex(COLUMN_LOCATION_NAME);
            if (columnIndex != -1) {
                receiver.setLocationName(cursor.getString(columnIndex));
            }
        }

        if (cursor != null) {
            cursor.close();
        }

        db.close();
        return receiver;
    }

    public boolean insert(String name1, String quantity1, String selectedStorage, String expire1, String pending) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Create ContentValues to store data for the new entry
        ContentValues values = new ContentValues();
        values.put("name", name1); // Replace "name" with the actual column name
        values.put("quantity", quantity1); // Replace "quantity" with the actual column name
        values.put("storage", selectedStorage); // Replace "storage" with the actual column name
        values.put("expire", expire1); // Replace "expire" with the actual column name
        values.put("pending", pending); // Replace "pending" with the actual column name

        // Insert the row and check if the operation was successful
        long result = db.insert("ItemTable", null, values); // Replace "ItemTable" with the actual table name

        db.close();

        // Return true if insertion was successful, false otherwise
        return result != -1;
    }

    public ArrayList<InformDonationModalClass> readInformationData() {
        ArrayList<InformDonationModalClass> informationList = new ArrayList<>();
        String selectQuery = "SELECT * FROM ItemTable"; // Replace "ItemTable" with the actual table name.

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                InformDonationModalClass information = new InformDonationModalClass();

                // Safely get column values and set them in the InformDonationModalClass object
                int columnIndex;

                columnIndex = cursor.getColumnIndex("name"); // Replace "name" with the actual column name
                if (columnIndex != -1) {
                    information.setName(cursor.getString(columnIndex));
                }

                columnIndex = cursor.getColumnIndex("quantity"); // Replace "quantity" with the actual column name
                if (columnIndex != -1) {
                    information.setQuantity(cursor.getString(columnIndex));
                }

                columnIndex = cursor.getColumnIndex("storage"); // Replace "storage" with the actual column name
                if (columnIndex != -1) {
                    information.setStorage(cursor.getString(columnIndex));
                }

                columnIndex = cursor.getColumnIndex("expire"); // Replace "expire" with the actual column name
                if (columnIndex != -1) {
                    information.setExpire(cursor.getString(columnIndex));
                }

                columnIndex = cursor.getColumnIndex("pending"); // Replace "pending" with the actual column name
                if (columnIndex != -1) {
                    information.setPending(cursor.getString(columnIndex));
                }

                // Add the information object to the list
                informationList.add(information);

            } while (cursor.moveToNext());
        }

        if (cursor != null) {
            cursor.close();
        }

        db.close();
        return informationList;
    }

    public boolean updateInformStatus(String email, String status) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Create a ContentValues object to store the updated status
        ContentValues values = new ContentValues();
        values.put("status", status); // "status" should be the actual column name in the database where status is stored

        // Update the row where the email matches the provided email
        int rowsAffected = db.update(TABLE_RECEIVER, values, COLUMN_EMAIL + " = ?", new String[]{email});

        db.close();

        // Return true if at least one row was updated, false otherwise
        return rowsAffected > 0;
    }

    public boolean checkUser(String email1, String pass1) {
        SQLiteDatabase db = this.getReadableDatabase();

        // Query to find the receiver by email and password
        String selectQuery = "SELECT * FROM " + TABLE_RECEIVER + " WHERE " + COLUMN_EMAIL + " = ? AND " + COLUMN_PASSWORD + " = ?";
        Cursor cursor = db.rawQuery(selectQuery, new String[]{email1, pass1});

        // If a matching user is found, return true, otherwise false
        boolean userExists = cursor != null && cursor.moveToFirst();

        if (cursor != null) {
            cursor.close();
        }

        db.close();
        return userExists;
    }

    public long insertData(String reference, String type, String member, String requirement,
                           String frequency, String time, String phone, String email, String pass) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_REFERENCE, reference);
        values.put(COLUMN_TYPE, type);
        values.put(COLUMN_MEMBER, member);
        values.put(COLUMN_REQUIREMENT, requirement);
        values.put(COLUMN_FREQUENCY, frequency);
        values.put(COLUMN_TIME, time);
        values.put(COLUMN_PHONE, phone);
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_PASSWORD, pass);

        // Inserting row and getting the ID
        long id = db.insert(TABLE_RECEIVER, null, values);
        db.close();
        return id;
    }

    public long update(ReceiverModalClass receiverModalClass) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_REFERENCE, receiverModalClass.getReference());
        values.put(COLUMN_TYPE, receiverModalClass.getType());
        values.put(COLUMN_MEMBER, receiverModalClass.getMember());
        values.put(COLUMN_REQUIREMENT, receiverModalClass.getRequirement());
        values.put(COLUMN_FREQUENCY, receiverModalClass.getFrequency());
        values.put(COLUMN_TIME, receiverModalClass.getTime());
        values.put(COLUMN_PHONE, receiverModalClass.getPhone());
        values.put(COLUMN_EMAIL, receiverModalClass.getEmail());
        values.put(COLUMN_PASSWORD, receiverModalClass.getPassword());
        values.put(COLUMN_LATITUDE, receiverModalClass.getLatitude());
        values.put(COLUMN_LONGITUDE, receiverModalClass.getLongitude());
        values.put(COLUMN_LOCATION_NAME, receiverModalClass.getLocationName());

        // Update the receiver based on their ID
        int rowsAffected = db.update(TABLE_RECEIVER, values, COLUMN_ID + " = ?", new String[]{String.valueOf(receiverModalClass.getId())});

        db.close();

        return rowsAffected; // Returns the number of rows affected (should be 1 if the update was successful)
    }

//    public long insertReceiverData(String reference, String selectedType, String member, String selectedRequirement,
//                                   String frequency, String time, String phone, String email, String pass,
//                                   double latitude, double longitude, String locationName) {
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        ContentValues values = new ContentValues();
//        values.put(COLUMN_REFERENCE, reference);
//        values.put(COLUMN_TYPE, selectedType);
//        values.put(COLUMN_MEMBER, member);
//        values.put(COLUMN_REQUIREMENT, selectedRequirement);
//        values.put(COLUMN_FREQUENCY, frequency);
//        values.put(COLUMN_TIME, time);
//        values.put(COLUMN_PHONE, phone);
//        values.put(COLUMN_EMAIL, email);
//        values.put(COLUMN_PASSWORD, pass);
//
//        // Insert location data
//        values.put(COLUMN_LATITUDE, latitude);  // Store latitude
//        values.put(COLUMN_LONGITUDE, longitude);  // Store longitude
//        values.put(COLUMN_LOCATION_NAME, locationName); // Store location name
//
//        // Inserting row
//        long id = db.insert(TABLE_RECEIVER, null, values);
//        db.close();
//        return id;
//    }

    public long insertReceiverData(String reference, String selectedType, String member, String selectedRequirement,
                                   String frequency, String time, String phone, String email, String pass,
                                   double latitude, double longitude, String locationName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_REFERENCE, reference);
        values.put(COLUMN_TYPE, selectedType);
        values.put(COLUMN_MEMBER, member);
        values.put(COLUMN_REQUIREMENT, selectedRequirement);
        values.put(COLUMN_FREQUENCY, frequency);
        values.put(COLUMN_TIME, time);
        values.put(COLUMN_PHONE, phone);
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_PASSWORD, pass);

        // Ensure the location values are correctly inserted
        values.put(COLUMN_LATITUDE, latitude);  // Store latitude
        values.put(COLUMN_LONGITUDE, longitude);  // Store longitude
        values.put(COLUMN_LOCATION_NAME, locationName); // Store location name

        // Inserting row
        long id = db.insert(TABLE_RECEIVER, null, values);
        db.close();
        return id;
    }


}
