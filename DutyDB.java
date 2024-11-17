package com.example.feedhope.RiderInterface.Duty;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import androidx.annotation.Nullable;
import com.example.feedhope.RiderInterface.SalaryReport.SalaryModelClass;
import java.util.ArrayList;

public class DutyDB extends SQLiteOpenHelper {
    private static final String DBName="FeedHopeProject.db";
    public DutyDB(@Nullable Context context) {
        super(context, DBName, null, 36);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
         db.execSQL(" create table RiderAssignDuty(DutyID INTEGER PRIMARY KEY AUTOINCREMENT,Email TEXT NOT NULL,Pick_Location TEXT NOT NULL,Drop_Location TEXT NOT NULL, Date INTEGER NOT NULL, Status TEXT NOT NULL,Payment_Status TEXT DEFAULT 'Unpaid')");
        db.execSQL("create table PaymentReport(PaymentID INTEGER PRIMARY KEY AUTOINCREMENT, DutyID INTEGER, Email TEXT NOT NULL, Salary INTEGER, PaymentDate TEXT, FOREIGN KEY(DutyID) REFERENCES RiderAssignDuty(DutyID))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS RiderAssignDuty");
        onCreate(db);
    }

    public boolean assignDuty(String Email,String Pick,String Drop, int Date,String Status) {
        try (SQLiteDatabase myDB = this.getWritableDatabase()) {
            ContentValues cv = new ContentValues();
            cv.put("Email", Email);
            cv.put("Pick_Location", Pick);
            cv.put("Drop_Location", Drop);
            cv.put("Date", Date);
            cv.put("Status",Status);
            cv.put("Payment_Status", "Unpaid");

            long result = myDB.insert("RiderAssignDuty", null, cv);
            myDB.close();
            if (result == -1) {
                Log.e("RiderRegisterDB", "Failed to insert data");
                return false;
            } else {
                Log.d("RiderRegisterDB", "Data inserted successfully");
                return true;
            }
        }
    }

    public boolean paySalary(int dutyId, String email, int salary, String paymentDate) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put("Payment_Status", "Paid");
        db.update("RiderAssignDuty", cv, "DutyID = ?", new String[]{String.valueOf(dutyId)});

        ContentValues paymentCV = new ContentValues();
        paymentCV.put("DutyID", dutyId);
        paymentCV.put("Email", email);
        paymentCV.put("Salary", salary);
        paymentCV.put("PaymentDate", paymentDate);

        long result = db.insert("PaymentReport", null, paymentCV);
        db.close();
        return result != -1;
    }

    public ArrayList<SalaryModelClass> getPaymentHistory(String userEmail) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM PaymentReport WHERE Email = ?", new String[]{userEmail});
        ArrayList<SalaryModelClass> paymentReports = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                int paymentID = cursor.getInt(0);
                int dutyID = cursor.getInt(1);
                String email = cursor.getString(2);
                int salary = cursor.getInt(3);
                String paymentDate = cursor.getString(4);
                paymentReports.add(new SalaryModelClass(paymentID, dutyID, email, salary, paymentDate));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return paymentReports;
    }

    public ArrayList<DutyModalClass> readRiderDuty(String userEmail) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM RiderAssignDuty WHERE Payment_Status = 'Unpaid' AND Email = ?", new String[]{userEmail});
        ArrayList<DutyModalClass> dutyModalClasses = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                String pick = cursor.getString(2);
                String drop = cursor.getString(3);
                String date = cursor.getString(4);
                String status = cursor.getString(5);
                dutyModalClasses.add(new DutyModalClass(id,name, pick, drop, date, status));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return dutyModalClasses;
    }

    public ArrayList<DutyModalClass> readAdmin() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM RiderAssignDuty WHERE Payment_Status = 'Unpaid'", null);
        ArrayList<DutyModalClass> dutyModalClasses = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                String pick = cursor.getString(2);
                String drop = cursor.getString(3);
                String date = cursor.getString(4);
                String status = cursor.getString(5);
                dutyModalClasses.add(new DutyModalClass(id,name, pick, drop, date, status));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return dutyModalClasses;
    }
    public boolean updateDutyStatus(int dutyId, String status) {
        try (SQLiteDatabase db = this.getWritableDatabase()) {
            ContentValues cv = new ContentValues();
            cv.put("Status", status);

            int result = db.update("RiderAssignDuty", cv, "DutyID = ?", new String[]{String.valueOf(dutyId)});
            return result > 0;
        }
    }

    public int getPendingCount(String userEmail) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT COUNT(*) FROM RiderAssignDuty WHERE Status = 'Pending' AND Payment_Status = 'Unpaid' AND Email = ?";
        Cursor cursor = db.rawQuery(query, new String[]{userEmail});
        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        return count;
    }


    public int getCompletedCount(String userEmail) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT COUNT(*) FROM RiderAssignDuty WHERE Status = 'Completed' AND Payment_Status = 'Unpaid' AND Email = ?";
        Cursor cursor = db.rawQuery(query, new String[]{userEmail});
        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        return count;
    }

    public void markDutiesAsPaid(String userName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("Payment_Status", "Paid");
        db.update("RiderAssignDuty", cv, "Email = ? AND Status = 'Completed'", new String[]{userName});
        db.close();
    }
}