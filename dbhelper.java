package com.example.helloword;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class dbhelper extends SQLiteOpenHelper {
    public static final String dbname="signup.db";

    public dbhelper(Context context) {
        super(context, "signup.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase MYDB) {
        MYDB.execSQL("create table signup(firstname TEXT ,lastname TEXT,email TEXT,password TEXT,phone TEXT primary key)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase MYDB, int i, int i1) {

        MYDB.execSQL("drop table if exists signup");
    }
    public Boolean insertData(String firstname,String lastname, String email,String password,String phone){
        SQLiteDatabase MYDB=this.getWritableDatabase();
        ContentValues contentvalues=new ContentValues();
        contentvalues.put("firstname",firstname);
        contentvalues.put("lastname",lastname);
        contentvalues.put("email",email);
        contentvalues.put("password",password);
        contentvalues.put("phone",phone);
        long results=MYDB.insert("signup",null,contentvalues);
        if(results==-1)
            return false;
        else
            return true;
    }
    public Boolean checkusername(String username){
        SQLiteDatabase MYDB=this.getWritableDatabase();
        Cursor cursor=MYDB.rawQuery("select * from signup where username=?",new String[]{username});
        if(cursor.getCount()>0)
            return true;
        else
            return false;
    }
    public Boolean checkusernamepassword(String username, String password){
        SQLiteDatabase MYDB=this.getWritableDatabase();
        Cursor cursor=MYDB.rawQuery("select * from signup where username=? and password=?",new String[] {username,password});
        if(cursor.getCount()>0)
            return true;
        else
            return false;
    }
}
