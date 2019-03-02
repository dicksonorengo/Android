package com.example.notes3;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "notes.db";
    public static final String DATABASE_TABLE = "notes";
    public static final String ID = "_id";
    public static final String NOTE_TEXT = "_content";
    public static final String NOTE_DATE = "_date";
    public static final String NOTE_COLOR = "_color";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE table " + DATABASE_TABLE + "(" +
                ID + " integer primary key autoincrement, "+
                NOTE_TEXT + " text not null, " +
                NOTE_DATE + " text not null, " +
                NOTE_COLOR + " integer not null);"
        );
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + DATABASE_TABLE);
        onCreate(db);
    }

    public void insertNote(Note note){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(NOTE_TEXT, note.getText());
        cv.put(NOTE_DATE, note.getDate());
        cv.put(NOTE_COLOR, note.getCurrent_color());
        db.insert(DATABASE_TABLE, null, cv);
    }
    public ArrayList<Note> getNotes(){
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<Note> notes = new ArrayList<>();
        String query = "SELECT * FROM " + DATABASE_TABLE+" ORDER BY " + ID + " DESC";
        Cursor c = db.rawQuery(query,null);
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            Note item = new Note(c.getString(c.getColumnIndex(NOTE_TEXT)),
                    c.getString(c.getColumnIndex(NOTE_DATE)),
                    c.getInt(c.getColumnIndex(NOTE_COLOR)));
            item.setID(c.getInt(c.getColumnIndex(ID)));
            notes.add(item);
        }
        return notes;
    }
    public boolean deleteNote(int id){
        SQLiteDatabase db = getWritableDatabase();
        if (id != -1)
            return db.delete(DATABASE_TABLE, ID + "=" + id, null) > 0;
        else return false;
    }
    public boolean updateNote(int id, String text, int color){
        if (id != -1){
            if(deleteNote(id)){
               Note new_note = new Note(text);
               new_note.setCurrent_color(color);
               insertNote(new_note);
               return true;
            }
            return false;
        }else return false;
    }
    public boolean updateNoteColor(int id, int color_id) {
        SQLiteDatabase db = getWritableDatabase();
        if (id != -1) {
            ContentValues cv = new ContentValues();
            cv.put(NOTE_COLOR, color_id);
            return db.update(DATABASE_TABLE, cv , ID + "=" + id, null) > 0;
        } else return false;
    }
}
