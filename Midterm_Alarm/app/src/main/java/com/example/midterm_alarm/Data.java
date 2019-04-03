package com.example.midterm_alarm;

public class Data {
    public static final String TABLE_NAME = "contents";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_TIME= "time";
    public static final String COLUMN_DATE = "date";
    private int id;
    private String description;
    private String time;
    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_DESCRIPTION + " TEXT,"
                    + COLUMN_TIME + " TEXT"
                    + ")";
    public Data(int id, String description, String time) {
        this.id = id;
        this.description = description;
        this.time = time;
    }
    public Data(){}
    public int getId() {
        return id;
    }
    public String getDescription() {
        return description;
    }
    public String getTime() {
        return time;
    }
    public void setId(int id) {
        this.id = id;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setTime(String time) {
        this.time = time;
    }
}
