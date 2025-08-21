package com.s22004966.timesapp;

public class NoteModel {
    private int id;
    private String title;
    private String note;

    public NoteModel(int id, String title, String note) {
        this.id = id;
        this.title = title;
        this.note = note;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getNote() {
        return note;
    }
}
