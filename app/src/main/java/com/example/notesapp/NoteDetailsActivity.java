package com.example.notesapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;


public class NoteDetailsActivity extends AppCompatActivity {

    private static final String TAG = "NoteDetailsActivity";
    public static final String EXTRA_NAME = "extra_name";
    public static final String EXTRA_CONTENT = "extra_content";

    private TextView tvNoteName;
    private TextView tvNoteContent;
    private Button btnShare;
    private Button btnEdit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate called");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_details);



        tvNoteName = findViewById(R.id.tvNoteName);
        tvNoteContent = findViewById(R.id.tvNoteContent);


        String name = getIntent().getStringExtra(EXTRA_NAME);
        String content = getIntent().getStringExtra(EXTRA_CONTENT);

        tvNoteName.setText(name);
        tvNoteContent.setText(content);

        btnShare = findViewById(R.id.btnShare);
        btnShare.setOnClickListener(v -> {
            Log.d(TAG, "btnShare clicked");
            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("text/plain");
            share.putExtra(Intent.EXTRA_SUBJECT, "Note: " + name);
            share.putExtra(Intent.EXTRA_TEXT, name + "\n\n" + content);
            startActivity(Intent.createChooser(share, "Share note"));
        });

        btnEdit = findViewById(R.id.btnEdit);
        btnEdit.setOnClickListener(v -> {
            Log.d(TAG, "btnEdit clicked");
            Intent editIntent = new Intent(this, EditNoteActivity.class);
            editIntent.putExtra(EditNoteActivity.EXTRA_OLD_NAME, name);
            editIntent.putExtra(EditNoteActivity.EXTRA_OLD_CONTENT, content);
            startActivity(editIntent);
            finish();
        });
    }
}
