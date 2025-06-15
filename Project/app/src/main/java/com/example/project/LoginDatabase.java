package com.example.project;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Objects;

public class LoginDatabase extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "logins.db";
    private static final int VERSION = 1;

    public LoginDatabase(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    private final class LoginTable {
        private static final String TABLE = "logins";
        private static final String COL_USERNAME = "username";
        private static final String COL_PASSWORD = "password";
        private static final String COL_SECURITYPIN = "Pin";
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + LoginTable.TABLE + " (" +
                LoginTable.COL_USERNAME + " text,  " +
                LoginTable.COL_PASSWORD + " text, " +
                LoginTable.COL_SECURITYPIN + " integer) ");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,
                          int newVersion) {
        db.execSQL("drop table if exists " + LoginTable.TABLE);
        onCreate(db);
    }

    public void addUser(String Username, String Password, int Pin) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(LoginTable.COL_USERNAME, Username);
        values.put(LoginTable.COL_PASSWORD, Password);
        values.put(LoginTable.COL_SECURITYPIN, Pin);
        db.insert(LoginTable.TABLE, null,values);
    }

    public boolean Login(String Username, String password) {
        SQLiteDatabase db = getReadableDatabase();

        String sql = "select * from " + LoginTable.TABLE + " where username = ?";
        Cursor cursor = db.rawQuery(sql, new String[]{Username});
        if (cursor.moveToFirst()) {
            if (cursor.getString(0).equals(Username)){
                if(cursor.getString(1).equals(password)){
                    cursor.close();
                    return true;
                }
            }
        } while (cursor.moveToNext()) ;
        cursor.close();
        return false;
    }

    public boolean usernameExists(String Username) {

        SQLiteDatabase db = getReadableDatabase();

        String sql = "SELECT * FROM " + LoginTable.TABLE + " WHERE username = ?";
        Cursor cursor = db.rawQuery(sql, new String[]{Username});
        if (cursor.moveToFirst()) {
            if (cursor.getString(0).equals(Username)){
                    return true;
            }
        } while (cursor.moveToNext()) ;
        cursor.close();
        return false;
    }

    public boolean verifyPin(String Username, int Pin){
        SQLiteDatabase db = getReadableDatabase();

        String sql = "SELECT * FROM " + LoginTable.TABLE + " WHERE username = ?";
        Cursor cursor = db.rawQuery(sql, new String[]{Username});
        if (cursor.moveToFirst()) {
            if (cursor.getString(0).equals(Username)){
                if(Integer.parseInt(cursor.getString(2)) == Pin){
                    return true;
                }
            }
        } while (cursor.moveToNext()) ;
        cursor.close();
        return false;
    }
}
