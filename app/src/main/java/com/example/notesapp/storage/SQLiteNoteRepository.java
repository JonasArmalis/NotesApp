package com.example.notesapp.storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.notesapp.model.Note;

import java.util.ArrayList;
import java.util.List;

public class SQLiteNoteRepository implements NoteRepository {
    private static final String TAG = "SQLiteNoteRepository";

    @Override
    public List<Note> loadAll(Context ctx) {
        Log.d(TAG, "loadAll()");
        NotesDbHelper helper = new NotesDbHelper(ctx);
        SQLiteDatabase db = helper.getReadableDatabase();
        ArrayList<Note> result = new ArrayList<>();
        String[] cols = { NotesDbHelper.C_NAME, NotesDbHelper.C_CONTENT };
        try (Cursor c = db.query(NotesDbHelper.T_NOTES, cols, null, null, null, null, NotesDbHelper.C_NAME + " COLLATE NOCASE ASC")) {
            while (c.moveToNext()) {
                String name = c.getString(0);
                String content = c.getString(1);
                result.add(new Note(name, content));
            }
        }
        db.close();
        return result;
    }

    @Override
    public void saveAll(Context ctx, List<Note> notes) {
        Log.d(TAG, "saveAll() replace all");
        NotesDbHelper helper = new NotesDbHelper(ctx);
        SQLiteDatabase db = helper.getWritableDatabase();
        db.beginTransaction();
        try {
            db.delete(NotesDbHelper.T_NOTES, null, null);
            for (Note n : notes) {
                ContentValues cv = new ContentValues();
                cv.put(NotesDbHelper.C_NAME, n.getName());
                cv.put(NotesDbHelper.C_CONTENT, n.getContent());
                db.insert(NotesDbHelper.T_NOTES, null, cv);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    @Override
    public void add(Context ctx, Note note) {
        Log.d(TAG, "add(): " + note.getName());
        NotesDbHelper helper = new NotesDbHelper(ctx);
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(NotesDbHelper.C_NAME, note.getName());
        cv.put(NotesDbHelper.C_CONTENT, note.getContent());
        db.insertWithOnConflict(NotesDbHelper.T_NOTES, null, cv, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
    }

    @Override
    public void deleteByName(Context ctx, String name) {
        Log.d(TAG, "deleteByName(): " + name);
        NotesDbHelper helper = new NotesDbHelper(ctx);
        SQLiteDatabase db = helper.getWritableDatabase();
        db.delete(NotesDbHelper.T_NOTES, NotesDbHelper.C_NAME + "=?", new String[]{ name });
        db.close();
    }
}
