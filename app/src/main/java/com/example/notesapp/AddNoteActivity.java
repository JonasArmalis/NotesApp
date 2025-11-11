package com.example.notesapp;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.notesapp.storage.RepositoryFactory;
import com.example.notesapp.storage.NoteRepository;
import androidx.appcompat.app.AppCompatActivity;

import com.example.notesapp.model.Note;

import java.util.List;

public class AddNoteActivity extends AppCompatActivity {
    private static final String TAG = "AddNoteActivity";

    private EditText txtName;
    private EditText txtContent;
    private Button btnSave;
    private NoteRepository repo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate called");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        repo = RepositoryFactory.get(this);
        txtName = findViewById(R.id.txtName);
        txtContent = findViewById(R.id.txtContent);
        btnSave = findViewById(R.id.btnSave);


        btnSave.setOnClickListener(v -> {
            Log.d(TAG, "btnSave clicked");
            String name = txtName.getText().toString().trim();
            String content = txtContent.getText().toString().trim();
            List<Note> allNotes = repo.loadAll(this);
            for (Note n : allNotes) {
                if (n.getName().equalsIgnoreCase(name)) {
                    txtName.setError("Note with this name already exists");
                    txtName.requestFocus();
                    Log.d(TAG, "Duplicate note name detected: " + name);
                    return;
                }
            }

            if (name.isEmpty()) {
                txtName.setError("Name is required");
                txtName.requestFocus();
                Log.d(TAG, "Validation failed: empty name");
                return;
            }
            if (content.isEmpty()) {
                txtContent.setError("Content is required");
                txtContent.requestFocus();
                Log.d(TAG, "Validation failed: empty content");
                return;
            }

            repo = RepositoryFactory.get(this);
            repo.add(this, new Note(name, content));
            Toast.makeText(this, "Note saved", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "Note saved, finishing activity");
            finish();
        });
    }
}
