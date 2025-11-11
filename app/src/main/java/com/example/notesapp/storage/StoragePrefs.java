package com.example.notesapp.storage;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class StoragePrefs {
    private static final String TAG = "StoragePrefs";
    private static final String PREFS = "storage_settings";
    private static final String KEY_MODE = "mode";

    public static void setMode(Context ctx, StorageMode mode) {
        Log.d(TAG, "setMode: " + mode);
        SharedPreferences sp = ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        sp.edit().putString(KEY_MODE, mode.name()).apply();
    }

    public static StorageMode getMode(Context ctx) {
        SharedPreferences sp = ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        String v = sp.getString(KEY_MODE, StorageMode.SHARED_PREFS.name());
        StorageMode mode = StorageMode.valueOf(v);
        Log.d(TAG, "getMode: " + mode);
        return mode;
    }
}
