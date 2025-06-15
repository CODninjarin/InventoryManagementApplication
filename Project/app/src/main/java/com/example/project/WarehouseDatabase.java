package com.example.project;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Objects;

public class WarehouseDatabase extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "stock.db";
    private static final int VERSION = 1;
    private int id;

    public WarehouseDatabase(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    public final class warehouseTable {
        private static final String TABLE = "warehouses";
        private static final String COL_ID = "id";
        private static final String COL_ITEM = "item";
        private static final String COL_DESCRIPTION = "description";
        private static final String COL_QUANTITY = "quantity";
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + warehouseTable.TABLE + " (" +
                warehouseTable.COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                warehouseTable.COL_ITEM + " TEXT,  " +
                warehouseTable.COL_DESCRIPTION + " TEXT, " +
                warehouseTable.COL_QUANTITY + " INTEGER) ");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,
                          int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + warehouseTable.TABLE);
        onCreate(db);
    }

    public void addInventory(String Item, String Description, String Quantity) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(warehouseTable.COL_ITEM, Item);
        values.put(warehouseTable.COL_DESCRIPTION, Description);
        values.put(warehouseTable.COL_QUANTITY, Quantity);
        db.insert(warehouseTable.TABLE, null, values);
    }

    public Cursor getInventory() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + warehouseTable.TABLE;

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(sql, null);
        }
        return cursor;
    }

    public void deleteRow(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(warehouseTable.TABLE, "id=?", new String[]{id});
    }

    public void updateTable(String Item, String newItem, String description, String quantity) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(warehouseTable.COL_ITEM, newItem);
        values.put(warehouseTable.COL_DESCRIPTION, description);
        values.put(warehouseTable.COL_QUANTITY, quantity);

        db.update(warehouseTable.TABLE, values, "item=?", new String[]{Item});
    }


}
