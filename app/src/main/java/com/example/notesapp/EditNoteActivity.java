package com.example.notesapp;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.notesapp.model.Note;
import com.example.notesapp.storage.NoteRepository;
import com.example.notesapp.storage.RepositoryFactory;

import java.util.List;

public class EditNoteActivity extends AppCompatActivity {

    private static final String TAG = "EditNoteActivity";

    public static final String EXTRA_OLD_NAME = "extra_old_name";
    public static final String EXTRA_OLD_CONTENT = "extra_old_content";

    private EditText txtEditName;
    private EditText txtEditContent;
    private Button btnUpdate;

    private NoteRepository repo;
    private String oldName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate called");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        txtEditName = findViewById(R.id.txtEditName);
        txtEditContent = findViewById(R.id.txtEditContent);
        btnUpdate = findViewById(R.id.btnUpdate);
        repo = RepositoryFactory.get(this);

        oldName = getIntent().getStringExtra(EXTRA_OLD_NAME);
        String oldContent = getIntent().getStringExtra(EXTRA_OLD_CONTENT);

        txtEditName.setText(oldName);
        txtEditContent.setText(oldContent);

        btnUpdate.setOnClickListener(v -> {
            Log.d(TAG, "btnUpdate clicked");

            String newName = txtEditName.getText().toString().trim();
            String newContent = txtEditContent.getText().toString().trim();

            if (newName.isEmpty()) {
                txtEditName.setError("Name cannot be empty");
                txtEditName.requestFocus();
                return;
            }
            if (newName.length() > 30) {
                txtEditName.setError("Name too long (max 30)");
                txtEditName.requestFocus();
                return;
            }
            if (newContent.isEmpty()) {
                txtEditContent.setError("Content cannot be empty");
                txtEditContent.requestFocus();
                return;
            }

            List<Note> allNotes = repo.loadAll(this);
            boolean found = false;
            for (int i = 0; i < allNotes.size(); i++) {
                if (allNotes.get(i).getName().equals(oldName)) {
                    allNotes.set(i, new Note(newName, newContent));
                    found = true;
                    break;
                }
            }

            if (found) {
                repo.saveAll(this, allNotes);
                Toast.makeText(this, "Note updated", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "Updated note: " + newName);
                finish();
            } else {
                Toast.makeText(this, "Note not found", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
