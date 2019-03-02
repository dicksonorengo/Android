package com.example.midterm_alarm;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;


public class DataBaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "DBCONTENTS";
    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(Data.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + Data.TABLE_NAME);

        onCreate(db);
    }

    public long insertContent(Data content) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(Data.COLUMN_DESCRIPTION, content.getDescription());
        values.put(Data.COLUMN_TIME, content.getTime());


        long id = db.insert(Data.TABLE_NAME, null, values);


        db.close();

        return id;
    }

    public Data getContent(long id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(Data.TABLE_NAME,
                new String[]{Data.COLUMN_ID, Data.COLUMN_DESCRIPTION, Data.COLUMN_TIME},
                Data.COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        Data note = new Data(
                cursor.getInt(cursor.getColumnIndex(Data.COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(Data.COLUMN_DESCRIPTION)),
                cursor.getString(cursor.getColumnIndex(Data.COLUMN_TIME)));


        cursor.close();

        return note;
    }

    public ArrayList<Data> getContents() {
        ArrayList<Data> notes = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + Data.TABLE_NAME + " ORDER BY " +
                Data.COLUMN_TIME +", "+Data.COLUMN_ID+ " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Data note = new Data();
                note.setId(cursor.getInt(cursor.getColumnIndex(Data.COLUMN_ID)));
                note.setDescription(cursor.getString(cursor.getColumnIndex(Data.COLUMN_DESCRIPTION)));
                note.setTime(cursor.getString(cursor.getColumnIndex(Data.COLUMN_TIME)));

                notes.add(note);
            } while (cursor.moveToNext());
        }

        db.close();

        return notes;
    }

    public int numberOfRows() {
        String countQuery = "SELECT  * FROM " + Data.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();


        return count;
    }


    public int updateContent (Data content) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Data.COLUMN_DESCRIPTION, content.getDescription());
        values.put(Data.COLUMN_TIME, content.getTime());


        return db.update(Data.TABLE_NAME, values, Data.COLUMN_ID + " = ?",
                new String[]{String.valueOf(content.getId())});
    }

    public void deleteContent(Data content) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Data.TABLE_NAME, Data.COLUMN_ID + " = ?",
                new String[]{String.valueOf(content.getId())});
        db.close();
    }
}