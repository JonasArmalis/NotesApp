package com.example.notesapp.storage;

import android.content.Context;

public class RepositoryFactory {
    private static final SharedPrefsNoteRepository SHARED = new SharedPrefsNoteRepository();
    private static final SQLiteNoteRepository SQLITE = new SQLiteNoteRepository();

    public static NoteRepository get(Context ctx) {
        StorageMode mode = StoragePrefs.getMode(ctx);
        return (mode == StorageMode.SQLITE) ? SQLITE : SHARED;
    }
}
