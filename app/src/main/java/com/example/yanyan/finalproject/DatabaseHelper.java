package com.example.yanyan.finalproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by yanyan on 4/6/18.
 */

public class DatabaseHelper extends SQLiteOpenHelper{

    public static final String DATABASE_NAME = "User.db";
    public static final String TABLE_NAME = "Info_table";

    //create the column names

    public static final String COL_1 = "ID";
    public static final String COL_2 ="BOOK";
    public static final String COL_3 ="AUTHOR";
    public static final String COL_4 ="TAGS";
    public static final String COL_5 ="QUOTE";
    public static final String COL_6 ="RATE";
    public static final String COL_7 ="THOUGHTS";
    public static final String COL_8 ="RECORD";
    public static final String COL_9 ="DATETIME";

    public DatabaseHelper(Context context){
        super(context, DATABASE_NAME, null , 1);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table " + TABLE_NAME +
                "(ID INTEGER PRIMARY KEY AUTOINCREMENT, BOOK TEXT, AUTHOR TEXT, TAGS TEXT, QUOTE TEXT, RATE TEXT, THOUGHTS TEXT, RECORD BLOB, DATETIME TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);

    }

    public boolean insertData(String book, String author, String tags, String quote, String rate, String thoughts,String datetime){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv= new ContentValues();
        cv.put(COL_2, book);
        cv.put(COL_3, author);
        cv.put(COL_4, tags);
        cv.put(COL_5, quote);
        cv.put(COL_6, rate);
        cv.put(COL_7, thoughts);
        cv.put(COL_9,datetime);

        long result = db.insert(TABLE_NAME,null,cv);
        return (result != -1);

    }

    public void insertInput(String book, String author, String tags, String quote, String rate, String thoughts,String record,String datetime){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv= new ContentValues();
        cv.put(COL_2, book);
        cv.put(COL_3, author);
        cv.put(COL_4, tags);
        cv.put(COL_5, quote);
        cv.put(COL_6, rate);
        cv.put(COL_7, thoughts);
        cv.put(COL_8, record);
        cv.put(COL_9,datetime);
        db.insert(TABLE_NAME,null,cv);
    }

    public boolean updateData(String id,String book, String author, String tags, String quote, String rate, String thoughts){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv= new ContentValues();
        cv.put(COL_1, id);
        cv.put(COL_2, book);
        cv.put(COL_3, author);
        cv.put(COL_4, tags);
        cv.put(COL_5, quote);
        cv.put(COL_6, rate);
        cv.put(COL_7, thoughts);

        db.update(TABLE_NAME,cv,"ID = ?", new String[]{id});
        return true;
    }
    public Cursor getAllDate(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME, null);
        return res;
    }


}
