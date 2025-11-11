package com.example.notesapp.storage;

import android.content.Context;

import com.example.notesapp.model.Note;

import java.util.List;

public interface NoteRepository {
    List<Note> loadAll(Context ctx);
    void saveAll(Context ctx, List<Note> notes);
    void add(Context ctx, Note note);
    void deleteByName(Context ctx, String name);
}
