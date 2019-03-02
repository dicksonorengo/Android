package com.example.notes3;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Note implements Comparable<Note>{
    private String text;
    private String date;
    private int ID = -1;
    private int current_color = 0;
    DateFormat formatter;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public Note(String text, Date date, int current_color) {
        this.text = text;
        formatter = new SimpleDateFormat("dd/MM/yyyy");
        this.date = formatter.format(date);
        this.current_color = current_color;
    }
    public Note(String text, String date, int current_color) {
        this.text =text;
        this.date = date;
        this.current_color = current_color;
    }

    public Note(String text) {
        formatter = new SimpleDateFormat("dd/MM/yyyy");
        this.text = text;
        this.date = formatter.format(new Date());
    }

    public int getCurrent_color() {
        return current_color;
    }

    public void setCurrent_color(int current_color) {
        this.current_color = current_color;
    }

    public Note(String text, Date date) {
        this.text = text;
        formatter = new SimpleDateFormat("dd/MM/yyyy");
        this.date = formatter.format(date);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getDate() {
        return date;
    }

    public void setDate(Date date) {
        if (formatter != null)
            this.date = formatter.format(date);
        else{
            formatter = new SimpleDateFormat("dd/MM/yyyy");
            this.date = formatter.format(date);
        }
    }
    @Override
    public int compareTo(Note o) {
        return this.date.compareTo(o.getDate());
    }
}
