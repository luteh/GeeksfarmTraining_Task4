package com.example.android.task4;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Luteh on 2/7/2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Transaksi.db";
    public static final String TABLE_INCOME = "income";
    public static final String TABLE_EXPENSES = "expenses";
    public static final String COL_ID = "ID";
    public static final String COL_NAMA = "NAMA";
    public static final String COL_HARGA = "HARGA";
    public static final String CREATE_TABLE_INCOME = "CREATE TABLE " + TABLE_INCOME + " ( " +
            COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COL_NAMA + " TEXT, " +
            COL_HARGA + " INTEGER );";
    public static final String CREATE_TABLE_EXPENSES = "CREATE TABLE " + TABLE_EXPENSES + " ( " +
            COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COL_NAMA + " TEXT, " +
            COL_HARGA + " INTEGER );";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
// creating required tables
        db.execSQL(CREATE_TABLE_INCOME);
        db.execSQL(CREATE_TABLE_EXPENSES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
// on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INCOME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXPENSES);

        onCreate(db);
    }

    public void deleteSQL() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_EXPENSES, null, null);
        db.delete(TABLE_INCOME, null, null);
        db.close();
    }

    public boolean saveIncome(String nama, String harga) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues content_values = new ContentValues();
        content_values.put(COL_NAMA, nama);
        content_values.put(COL_HARGA, harga);
        long result = db.insert(TABLE_INCOME, null, content_values);
        return result != -1;
    }

    public boolean saveExpenses(String nama, String harga) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues content_values = new ContentValues();
        content_values.put(COL_NAMA, nama);
        content_values.put(COL_HARGA, harga);
        long result = db.insert(TABLE_EXPENSES, null, content_values);
        return result != -1;
    }

    public ArrayList<ArrayList<Object>> getAllExpenses() {
        ArrayList<ArrayList<Object>> dataArray = new
                ArrayList<ArrayList<Object>>();
        Cursor cur;
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            cur = db.query(TABLE_EXPENSES,
                    new String[]{COL_ID, COL_NAMA, COL_HARGA}, null, null,
                    null, null, null);
            cur.moveToFirst();

            if (!cur.isAfterLast()) {
                do {
                    ArrayList<Object> dataList = new ArrayList<Object>();
                    dataList.add(cur.getLong(0));
                    dataList.add(cur.getString(1));
                    dataList.add(cur.getLong(2));
                    dataArray.add(dataList);
                } while (cur.moveToNext());
            }
        } catch (Exception e) {
//  TODO  Auto-generated  catch  block
            e.printStackTrace();
            Log.e("DEBE  ERROR", e.toString());
        }
        return dataArray;
    }

    public ArrayList<ArrayList<Object>> getAllIncome() {
        ArrayList<ArrayList<Object>> dataArray = new
                ArrayList<ArrayList<Object>>();
        Cursor cur;
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            cur = db.query(TABLE_INCOME,
                    new String[]{COL_ID, COL_NAMA, COL_HARGA}, null, null,
                    null, null, null);
            cur.moveToFirst();

            if (!cur.isAfterLast()) {
                do {
                    ArrayList<Object> dataList = new ArrayList<Object>();
                    dataList.add(cur.getLong(0));
                    dataList.add(cur.getString(1));
                    dataList.add(cur.getLong(2));
                    dataArray.add(dataList);
                } while (cur.moveToNext());
            }
        } catch (Exception e) {
//  TODO  Auto-generated  catch  block
            e.printStackTrace();
            Log.e("DEBE  ERROR", e.toString());
        }
        return dataArray;
    }

}
