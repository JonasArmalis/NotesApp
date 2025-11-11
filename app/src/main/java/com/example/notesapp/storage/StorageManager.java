package com.example.notesapp.storage;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.notesapp.model.Note;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class StorageManager {
    private static final String TAG = "StorageManager";
    private static final String PREFS_NAME = "notes_prefs";
    private static final String KEY_NOTES_JSON = "notes_json";

    public static List<Note> loadNotes(Context ctx) {
        Log.d(TAG, "loadNotes called");
        SharedPreferences sp = ctx.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String json = sp.getString(KEY_NOTES_JSON, "[]");
        ArrayList<Note> result = new ArrayList<>();
        try {
            JSONArray arr = new JSONArray(json);
            for (int i = 0; i < arr.length(); i++) {
                JSONObject o = arr.getJSONObject(i);
                String name = o.optString("name", "");
                String content = o.optString("content", "");
                result.add(new Note(name, content));
            }
        } catch (Exception e) {
            Log.e(TAG, "Failed to parse notes JSON", e);
        }
        return result;
    }

    public static void saveNotes(Context ctx, List<Note> notes) {
        Log.d(TAG, "saveNotes called, count=" + (notes == null ? 0 : notes.size()));
        JSONArray arr = new JSONArray();
        try {
            if (notes != null) {
                for (Note n : notes) {
                    JSONObject o = new JSONObject();
                    o.put("name", n.getName());
                    o.put("content", n.getContent());
                    arr.put(o);
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Failed to build notes JSON", e);
        }
        SharedPreferences sp = ctx.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        sp.edit().putString(KEY_NOTES_JSON, arr.toString()).apply();
    }

    public static void addNote(Context ctx, Note note) {
        Log.d(TAG, "addNote called: " + note.getName());
        List<Note> all = loadNotes(ctx);
        all.add(note);
        saveNotes(ctx, all);
    }
}
