package com.example.unitconverter.ReceiverInterface;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class ReceiverDB extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "ReceiverDB";
    private static final int DATABASE_VERSION = 1;

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
                COLUMN_PASSWORD + " TEXT)";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older tables if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECEIVER);
        onCreate(db);
    }

    // Method to insert receiver data
    public long insertReceiverData(String reference, String type, String member, String requirement,
                                   String frequency, String time, String phone, String email, String password) {
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
        values.put(COLUMN_PASSWORD, password);

        // Inserting row
        long id = db.insert(TABLE_RECEIVER, null, values);
        db.close();
        return id;
    }

    // Method to retrieve all receiver data
    public ArrayList<ReceiverModalClass> getAllReceivers() {
        ArrayList<ReceiverModalClass> receiverList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_RECEIVER;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                ReceiverModalClass receiver = new ReceiverModalClass();

                // Check and fetch each column value safely
                int columnIndex;

                columnIndex = cursor.getColumnIndex(COLUMN_ID);
                if (columnIndex != -1) {
                    receiver.setId(cursor.getInt(columnIndex));
                } else {
                    Log.e("ReceiverDB", "Column 'id' not found in the database.");
                }

                columnIndex = cursor.getColumnIndex(COLUMN_REFERENCE);
                if (columnIndex != -1) {
                    receiver.setReference(cursor.getString(columnIndex));
                } else {
                    Log.e("ReceiverDB", "Column 'reference' not found in the database.");
                }

                columnIndex = cursor.getColumnIndex(COLUMN_TYPE);
                if (columnIndex != -1) {
                    receiver.setType(cursor.getString(columnIndex));
                } else {
                    Log.e("ReceiverDB", "Column 'type' not found in the database.");
                }

                columnIndex = cursor.getColumnIndex(COLUMN_MEMBER);
                if (columnIndex != -1) {
                    receiver.setMember(cursor.getString(columnIndex));
                } else {
                    Log.e("ReceiverDB", "Column 'member' not found in the database.");
                }

                columnIndex = cursor.getColumnIndex(COLUMN_REQUIREMENT);
                if (columnIndex != -1) {
                    receiver.setRequirement(cursor.getString(columnIndex));
                } else {
                    Log.e("ReceiverDB", "Column 'requirement' not found in the database.");
                }

                columnIndex = cursor.getColumnIndex(COLUMN_FREQUENCY);
                if (columnIndex != -1) {
                    receiver.setFrequency(cursor.getString(columnIndex));
                } else {
                    Log.e("ReceiverDB", "Column 'frequency' not found in the database.");
                }

                columnIndex = cursor.getColumnIndex(COLUMN_TIME);
                if (columnIndex != -1) {
                    receiver.setTime(cursor.getString(columnIndex));
                } else {
                    Log.e("ReceiverDB", "Column 'time' not found in the database.");
                }

                columnIndex = cursor.getColumnIndex(COLUMN_PHONE);
                if (columnIndex != -1) {
                    receiver.setPhone(cursor.getString(columnIndex));
                } else {
                    Log.e("ReceiverDB", "Column 'phone' not found in the database.");
                }

                columnIndex = cursor.getColumnIndex(COLUMN_EMAIL);
                if (columnIndex != -1) {
                    receiver.setEmail(cursor.getString(columnIndex));
                } else {
                    Log.e("ReceiverDB", "Column 'email' not found in the database.");
                }

                columnIndex = cursor.getColumnIndex(COLUMN_PASSWORD);
                if (columnIndex != -1) {
                    receiver.setPassword(cursor.getString(columnIndex));
                } else {
                    Log.e("ReceiverDB", "Column 'password' not found in the database.");
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
        }

        if (cursor != null) {
            cursor.close();
        }

        db.close();
        return receiver;
    }

    // Method to read information data (assuming a table for donation or related data)
    public ArrayList<InformDonationModalClass> readInformationData() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<InformDonationModalClass> donationList = new ArrayList<>();

        // Query to get all data from the relevant table
        String selectQuery = "SELECT * FROM " + "YourInformationTable"; // Replace with actual table name

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                InformDonationModalClass donation = new InformDonationModalClass();

                // Safely get column values
                int columnIndex;

                // Assuming InformDonationModalClass has similar fields as ReceiverModalClass
                columnIndex = cursor.getColumnIndex("column_name1"); // Replace with actual column name
                if (columnIndex != -1) {
                    donation.setField1(cursor.getString(columnIndex)); // Replace with actual setter method
                }

                columnIndex = cursor.getColumnIndex("column_name2"); // Replace with actual column name
                if (columnIndex != -1) {
                    donation.setField2(cursor.getString(columnIndex)); // Replace with actual setter method
                }

                // Continue for other fields as necessary...

                donationList.add(donation);
            } while (cursor.moveToNext());
        }

        if (cursor != null) {
            cursor.close();
        }

        db.close();
        return donationList;
    }

    public boolean insert(String name1, String quantity1, String selectedStorage, String expire1, String pending) {
        // Get a writable database instance
        SQLiteDatabase db = this.getWritableDatabase();

        // Prepare ContentValues to hold the data to insert
        ContentValues values = new ContentValues();
        values.put("name", name1);           // Assuming there's a 'name' column
        values.put("quantity", quantity1);   // Assuming there's a 'quantity' column
        values.put("storage", selectedStorage); // Assuming there's a 'storage' column
        values.put("expire", expire1);       // Assuming there's an 'expire' column
        values.put("status", pending);       // Assuming 'pending' represents the donation status (e.g., 'pending' status)

        // Insert the data into the Donations table (or whichever table you need)
        long result = db.insert("Donations", null, values);  // Change 'Donations' to the actual table name

        db.close();

        // Return true if insertion was successful, false otherwise
        return result != -1;
    }

    public boolean insertData(String reference, String type, String member, String requirement, String frequency, String time, String phone, String email, String pass) {
        // Get a writable database instance
        SQLiteDatabase db = this.getWritableDatabase();

        // Prepare ContentValues to hold the data to insert
        ContentValues values = new ContentValues();
        values.put(COLUMN_REFERENCE, reference);       // Insert the reference value
        values.put(COLUMN_TYPE, type);                 // Insert the type value
        values.put(COLUMN_MEMBER, member);             // Insert the member value
        values.put(COLUMN_REQUIREMENT, requirement);   // Insert the requirement value
        values.put(COLUMN_FREQUENCY, frequency);       // Insert the frequency value
        values.put(COLUMN_TIME, time);                 // Insert the time value
        values.put(COLUMN_PHONE, phone);               // Insert the phone value
        values.put(COLUMN_EMAIL, email);               // Insert the email value
        values.put(COLUMN_PASSWORD, pass);             // Insert the password value

        // Insert the data into the Receiver table
        long result = db.insert(TABLE_RECEIVER, null, values); // Insert into the "Receiver" table

        db.close();

        // Return true if insertion was successful, false otherwise
        return result != -1;
    }

    public boolean checkUser(String email1, String pass1) {
        SQLiteDatabase db = this.getReadableDatabase();

        // Define a boolean to return the result
        boolean userExists = false;

        // Query to check if a user with the given email and password exists in the database
        String selectQuery = "SELECT * FROM " + TABLE_RECEIVER + " WHERE " + COLUMN_EMAIL + " = ? AND " + COLUMN_PASSWORD + " = ?";
        Cursor cursor = db.rawQuery(selectQuery, new String[]{email1, pass1});

        // Check if the cursor contains any rows, meaning a matching user was found
        if (cursor != null && cursor.moveToFirst()) {
            userExists = true; // User exists with the provided email and password
        }

        // Close the cursor and database connection
        if (cursor != null) {
            cursor.close();
        }

        db.close();

        return userExists; // Return the result (true if user exists, false otherwise)
    }

    public long update(ReceiverModalClass receiverModalClass) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Prepare ContentValues with the new data for the receiver
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

        // Update the receiver's data in the database where the id matches
        long result = db.update(TABLE_RECEIVER, values, COLUMN_ID + " = ?", new String[]{String.valueOf(receiverModalClass.getId())});

        db.close();

        // Return the number of rows affected, or -1 if there was an error
        return result;
    }

    public boolean updateInformStatus(String name, String selectedStatus) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Prepare ContentValues to hold the new status data
        ContentValues values = new ContentValues();
        values.put("status", selectedStatus);  // Assuming 'status' is a column in the table for the status of the donation

        // Update the status for the specific record where the name matches
        int rowsAffected = db.update("YourDonationTable",  // Replace "YourDonationTable" with the actual table name
                values,
                "name = ?",  // Column to match the donation name
                new String[]{name});  // The name to match

        db.close();

        // If rowsAffected > 0, update was successful, return true. Otherwise, return false
        return rowsAffected > 0;
    }

}
