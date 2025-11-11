package com.example.notesapp.storage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class NotesDbHelper extends SQLiteOpenHelper {
    private static final String TAG = "NotesDbHelper";

    public static final String DB_NAME = "notes_db.sqlite";
    public static final int DB_VERSION = 1;

    public static final String T_NOTES = "notes";
    public static final String C_ID = "_id";
    public static final String C_NAME = "name";
    public static final String C_CONTENT = "content";

    private static final String SQL_CREATE =
            "CREATE TABLE " + T_NOTES + " (" +
                    C_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    C_NAME + " TEXT NOT NULL UNIQUE, " +
                    C_CONTENT + " TEXT NOT NULL" +
                    ");";

    public NotesDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        Log.d(TAG, "NotesDbHelper constructor");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "onCreate DB");
        db.execSQL(SQL_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "onUpgrade DB " + oldVersion + " -> " + newVersion);
        db.execSQL("DROP TABLE IF EXISTS " + T_NOTES);
        onCreate(db);
    }
}
