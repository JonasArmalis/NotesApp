package com.example.notesapp;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.notesapp.model.Note;
import com.example.notesapp.storage.NoteRepository;
import com.example.notesapp.storage.RepositoryFactory;

import java.util.ArrayList;
import java.util.List;

public class DeleteNoteActivity extends AppCompatActivity {

    private static final String TAG = "DeleteNoteActivity";

    private Spinner spinnerNotes;
    private Button btnDelete;
    private NoteRepository repo;

    private List<Note> notesList = new ArrayList<>();
    private ArrayAdapter<String> spinnerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate called");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_note);

        spinnerNotes = findViewById(R.id.spinnerNotes);
        btnDelete = findViewById(R.id.btnDelete);
        repo = RepositoryFactory.get(this);

        loadNotesToSpinner();

        btnDelete.setOnClickListener(v -> {
            Log.d(TAG, "btnDelete clicked");

            if (notesList == null || notesList.isEmpty()) {
                Toast.makeText(this, "No notes to delete", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "Delete aborted: empty list");
                return;
            }

            int position = spinnerNotes.getSelectedItemPosition();
            if (position < 0 || position >= notesList.size()) {
                Toast.makeText(this, "Please select a note", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "Delete aborted: invalid spinner position=" + position);
                return;
            }

            Note noteToDelete = notesList.get(position);
            String toDeleteName = noteToDelete.getName();

            repo.deleteByName(this, toDeleteName);
            Log.d(TAG, "Deleted note in repo: " + toDeleteName);

            Toast.makeText(this, "Deleted: " + toDeleteName, Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    private void loadNotesToSpinner() {
        Log.d(TAG, "loadNotesToSpinner called");
        notesList = new ArrayList<>(repo.loadAll(this));

        List<String> names = new ArrayList<>();
        for (Note n : notesList) names.add(n.getName());

        spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, names);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerNotes.setAdapter(spinnerAdapter);
        btnDelete.setEnabled(!notesList.isEmpty());
    }
}
