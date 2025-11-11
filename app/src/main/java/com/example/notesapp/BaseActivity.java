
package com.example.notesapp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class BaseActivity extends AppCompatActivity {

    protected final String LOG_TAG = "NotesAppLog";

    protected void logMethodCall(String methodName) {
        String className = this.getClass().getSimpleName();
        Log.d(LOG_TAG, className + ": " + methodName + "() called.");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        logMethodCall("onCreate");
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        logMethodCall("onResume");
        super.onResume();
    }

    @Override
    protected void onPause() {
        logMethodCall("onPause");
        super.onPause();
    }
}