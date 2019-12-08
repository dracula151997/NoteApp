package com.project.semicolon.noteapp.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NoteResponse {
    private int id;
    private String note;
    private String timestamp;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String formatDate(){
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = simpleDateFormat.parse(timestamp);
            SimpleDateFormat out = new SimpleDateFormat("MMM d");
            return out.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return "";
    }

}
