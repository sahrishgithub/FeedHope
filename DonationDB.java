package com.example.unitconverter.ProviderInterface;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import androidx.annotation.Nullable;

import java.io.IOException;
import java.util.List;

public class DonationDB extends SQLiteOpenHelper {

    private static final String DB_NAME = "FeedHopeProject.db";
    private static final int DB_VERSION = 13; // Increment version to apply changes
    private Context context;  // Declare context here

    // Modify the constructor to accept Context
    public DonationDB(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context; // Initialize context
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Creating the table with columns including Latitude, Longitude, and Address
        db.execSQL("CREATE TABLE ClothDonation (" +
                "Name TEXT NOT NULL, " +
                "Type TEXT NOT NULL, " +
                "Condition TEXT NOT NULL, " +
                "Quantity TEXT, " +
                "Category TEXT NOT NULL, " +
                "Seasonal TEXT NOT NULL, " +
                "Size TEXT NOT NULL, " +
                "Latitude REAL, " +     // Column for latitude
                "Longitude REAL, " +    // Column for longitude
                "Address TEXT" +        // Column for address
                ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop old table if exists and create a new one
        db.execSQL("DROP TABLE IF EXISTS ClothDonation");
        onCreate(db);
    }

    // Insert method modified to accept the context as a parameter
    public boolean insert(String loggedInEmail, String selectedType, String selectedCondition, String quanity1, String selectedCategory, String seasonal, String selectedSize, double latitude, double longitude) {
        // Get address from latitude and longitude using the context
        String address = getAddressFromLocation(latitude, longitude);

        // Open the database for writing
        try (SQLiteDatabase mydb = this.getWritableDatabase()) {
            // Prepare the data to be inserted into the database
            ContentValues cv = new ContentValues();
            cv.put("Name", loggedInEmail);              // User email as Name
            cv.put("Type", selectedType);               // Selected Cloth Type
            cv.put("Condition", selectedCondition);     // Selected Condition
            cv.put("Quantity", quanity1);               // Quantity of the item
            cv.put("Category", selectedCategory);       // Selected Category
            cv.put("Seasonal", seasonal);               // Seasonal information (from RadioGroup)
            cv.put("Size", selectedSize);               // Selected Size
            cv.put("Latitude", latitude);               // Latitude of the location
            cv.put("Longitude", longitude);             // Longitude of the location
            cv.put("Address", address);                 // Address of the location

            // Insert the data into the ClothDonation table
            long result = mydb.insert("ClothDonation", null, cv);

            // If insertion fails, result will be -1
            return result != -1;
        } catch (Exception e) {
            // Handle any exceptions that occur during database operations
            Log.e("DonationDB", "Insertion error", e);
            return false;
        }
    }

    // Method to get the address from latitude and longitude using context
    private String getAddressFromLocation(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(context);  // Use the context here
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                // Return the full address as a string
                return address.getAddressLine(0); // You can also use address.getLocality() or address.getCountryName() for specific details
            }
        } catch (IOException e) {
            Log.e("DonationDB", "Geocoder error", e);
        }
        return "Unknown Location"; // Return default value if location cannot be resolved
    }
}
