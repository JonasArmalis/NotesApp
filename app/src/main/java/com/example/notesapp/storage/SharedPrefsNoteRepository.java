package com.example.notesapp.storage;

import android.content.Context;

import com.example.notesapp.model.Note;

import java.util.List;

public class SharedPrefsNoteRepository implements NoteRepository {
    @Override
    public List<Note> loadAll(Context ctx) {
        return StorageManager.loadNotes(ctx);
    }

    @Override
    public void saveAll(Context ctx, List<Note> notes) {
        StorageManager.saveNotes(ctx, notes);
    }

    @Override
    public void add(Context ctx, Note note) {
        StorageManager.addNote(ctx, note);
    }

    @Override
    public void deleteByName(Context ctx, String name) {
        List<Note> all = StorageManager.loadNotes(ctx);
        for (int i = 0; i < all.size(); i++) {
            if (all.get(i).getName().equals(name)) {
                all.remove(i);
                break;
            }
        }
        StorageManager.saveNotes(ctx, all);
    }
}
