package com.example.mynotes.UI;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.example.mynotes.Data.InMemoryRepoImp;
import com.example.mynotes.Data.Note;
import com.example.mynotes.Data.Repo;
import com.example.mynotes.R;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class EditNoteActivity extends AppCompatActivity {

    private Note note;

    private EditText editTitle;
    private EditText editDescription;
    private Button saveChanges;
    private final Repo repo = InMemoryRepoImp.getInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        note = (Note) getIntent().getSerializableExtra(Note.NOTE);

        setContentView(R.layout.activity_edit_note);

        editTitle = findViewById(R.id.edit_title);
        editDescription = findViewById(R.id.edit_description);
        saveChanges = findViewById(R.id.button_save_changes);

        editTitle.setText(note.getTitle());
        editDescription.setText(note.getDescription());

        saveChanges.setOnClickListener(view -> saveNote());

    }

    void saveNote(){

        int noteID = note.getId();
        Note editedNote = note;

        editedNote.setTitle(editTitle.getText().toString());
        editedNote.setDescription(editDescription.getText().toString());
        editedNote.setId(noteID);
        repo.update(editedNote);

        Intent result = new Intent();
        result.putExtra(Note.NOTE, this.note);
        setResult(RESULT_OK, result);
        finish();
    }

}
