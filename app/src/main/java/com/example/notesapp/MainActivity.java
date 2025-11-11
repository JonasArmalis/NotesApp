// MainActivity.java
package com.example.notesapp;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.example.notesapp.model.Note;
import com.example.notesapp.storage.RepositoryFactory;
import com.example.notesapp.storage.NoteRepository;
import com.example.notesapp.storage.StorageMode;
import com.example.notesapp.storage.StoragePrefs;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private ListView lvNotes;
    private TextView txtEmpty;
    private ArrayAdapter<Note> notesAdapter;
    private final ArrayList<Note> notesData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate called");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        lvNotes = findViewById(R.id.lvNotes);
        txtEmpty = findViewById(R.id.txtEmpty);

        notesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, notesData);
        lvNotes.setAdapter(notesAdapter);
        lvNotes.setOnItemClickListener((parent, view, position, id) -> {
            Log.d(TAG, "onNoteClick position=" + position);
            Note clicked = notesData.get(position);
            Intent i = new Intent(this, NoteDetailsActivity.class);
            i.putExtra(NoteDetailsActivity.EXTRA_NAME, clicked.getName());
            i.putExtra(NoteDetailsActivity.EXTRA_CONTENT, clicked.getContent());
            startActivity(i);
        });
        lvNotes.setEmptyView(txtEmpty);
        Log.d(TAG, "UI initialized");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(TAG, "onCreateOptionsMenu called");
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    private void showStorageModeDialog() {
        Log.d(TAG, "showStorageModeDialog");
        final String[] items = { "SharedPreferences", "SQLite" };
        StorageMode current = StoragePrefs.getMode(this);
        int checked = (current == StorageMode.SQLITE) ? 1 : 0;

        new AlertDialog.Builder(this)
                .setTitle("Choose storage")
                .setSingleChoiceItems(items, checked, (d, which) -> {
                    StorageMode chosen = (which == 1) ? StorageMode.SQLITE : StorageMode.SHARED_PREFS;
                    StoragePrefs.setMode(this, chosen);
                })
                .setPositiveButton("OK", (d, w) -> {
                    reloadNotes();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected: " + item.getItemId());
        int id = item.getItemId();
        if (id == R.id.action_add_note) {
            startActivity(new Intent(this, AddNoteActivity.class));
            return true;
        } else if (id == R.id.action_delete_note) {
            startActivity(new Intent(this, DeleteNoteActivity.class));
            return true;
        } else if (id == R.id.action_storage_mode) {
            showStorageModeDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        Log.d(TAG, "onStart called");
        super.onStart();
    }

    private void reloadNotes() {
        Log.d(TAG, "reloadNotes called");
        NoteRepository repo = RepositoryFactory.get(this);
        notesData.clear();
        notesData.addAll(repo.loadAll(this));
        notesAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume called");
        super.onResume();
        reloadNotes();
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause called");
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "onStop called");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy called");
        super.onDestroy();
    }
}