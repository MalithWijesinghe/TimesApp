package com.s22004966.timesapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "TimesApp.db";
    public static final String TABLE_ONE = "login_table";
    public static final String TABLE_TODO = "todo_table";
    public static final String TABLE_SCHEDULE = "schedule_table";
    public static final String TABLE_NOTES = "notes_table";
    public static final String TABLE_SETTINGS = "settings_table";


    public DatabaseHelper(@Nullable Context context) {
        super(context, "TimesApp.db", null, 2);
        //SQLiteDatabase db = this.getWritableDatabase(); -> this line replaced by insertWithOnConflict
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_ONE + " (EMAIL TEXT PRIMARY KEY, NAME TEXT, PASSWORD TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_TODO + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, TODO TEXT, STATUS INTEGER)");
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_SCHEDULE + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, SCHEDULE TEXT, DATE TEXT, TIME TEXT, LOCATION TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NOTES + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, TITLE, NOTE)");
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_SETTINGS + " (ID INTEGER PRIMARY KEY, TIMER INTEGER, GOAL INTEGER, CURRENTLEVEL INTEGER)");

        ContentValues contentValues = new ContentValues();
        contentValues.put("ID", 1);
        contentValues.put("TIMER", 25);
        contentValues.put("GOAL", 2000);
        contentValues.put("CURRENTLEVEL", 0);
        db.insertWithOnConflict(TABLE_SETTINGS, null, contentValues, SQLiteDatabase.CONFLICT_IGNORE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ONE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TODO);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SCHEDULE);
        db.execSQL(("DROP TABLE IF EXISTS " + TABLE_NOTES));
        db.execSQL(("DROP TABLE IF EXISTS " + TABLE_SETTINGS));
        onCreate(db);
    }

    // Login and signup methods
    public boolean insertLoginData(String email, String name, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("EMAIL", email);
        contentValues.put("NAME", name);
        contentValues.put("PASSWORD", password);
        long result = db.insert(TABLE_ONE, null, contentValues);
        return result != -1;
    }

    public boolean checkLogin(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_ONE + " WHERE EMAIL = ? AND PASSWORD = ?", new String[]{email, password});
        boolean result = cursor.getCount() > 0;
        cursor.close();
        return result;
    }

    public boolean validateEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_ONE + " WHERE EMAIL = ?", new String[]{email});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    // To do methods
    public void insertTask(String task) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("TODO", task);
        values.put("STATUS", 0);
        db.insert(TABLE_TODO, null, values);
    }

    public List<ToDoModel> getAllTodo() {
        SQLiteDatabase db = this.getReadableDatabase();
        List<ToDoModel> modelList = new ArrayList<>();
        Cursor cursor = db.query(TABLE_TODO, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            ToDoModel todo = new ToDoModel();
            todo.setId(cursor.getInt(cursor.getColumnIndexOrThrow("ID")));
            todo.setTask(cursor.getString(cursor.getColumnIndexOrThrow("TODO")));
            todo.setStatus(cursor.getInt(cursor.getColumnIndexOrThrow("STATUS")));
            modelList.add(todo);
        }
        cursor.close();
        return modelList;
    }

    public void updateStatus(int id, int status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("STATUS", status);
        db.update(TABLE_TODO, values, "ID=?", new String[]{String.valueOf(id)});
    }

    public void deleteTask(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TODO, "ID=?", new String[]{String.valueOf(id)});
    }

    // Planner methods
    public boolean insertSchedule(String schedule, String date, String time, String location) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("SCHEDULE", schedule);
        contentValues.put("DATE", date);
        contentValues.put("TIME", time);
        contentValues.put("LOCATION", location);
        db.insert(TABLE_SCHEDULE, null, contentValues);
        return false;
    }

    public List<PlannerModel> getAllSchedules() {
        List<PlannerModel> list = new ArrayList<>(); // list to hold PlannerModel objects
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_SCHEDULE, null, null, null, null, null, "ID" + " DESC");

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("ID"));
                String schedule = cursor.getString(cursor.getColumnIndexOrThrow("SCHEDULE"));
                String date = cursor.getString(cursor.getColumnIndexOrThrow("DATE"));
                String time = cursor.getString(cursor.getColumnIndexOrThrow("TIME"));
                String location = cursor.getString(cursor.getColumnIndexOrThrow("LOCATION"));
                list.add(new PlannerModel(id, schedule, date, time, location));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    // Notes methods
    public boolean insertNote(String title, String note) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("TITLE", title);
        cv.put("NOTE", note);
        db.insert(TABLE_NOTES, null, cv);
        return false;
    }

    public List<NoteModel> getAllNotes() {
        List<NoteModel> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NOTES, null, null, null, null, null, "ID" + " DESC");

        if(cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("ID"));
                String title = cursor.getString(cursor.getColumnIndexOrThrow("TITLE"));
                String note = cursor.getString(cursor.getColumnIndexOrThrow("NOTE"));
                list.add(new NoteModel(id, title, note));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    // Save settings
    public void saveSettings(int timerDuration, int waterGoal) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("TIMER", timerDuration);
        contentValues.put("GOAL", waterGoal);

        int rows = db.update(TABLE_SETTINGS, contentValues, "ID=?", new String[]{"1"});
        if (rows == 0) {
            contentValues.put("ID", 1);
            db.insert(TABLE_SETTINGS, null, contentValues);
        }
        db.close();
    }

    // Get settings
    public Cursor getSettings() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM settings_table WHERE id=1", null);
    }

    // update water level
    public void updateCurrentWater(int currentWater) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("CURRENTLEVEL", currentWater);
        db.update(TABLE_SETTINGS, contentValues, "ID=?", new String[]{"1"});
        db.close();
    }
}
