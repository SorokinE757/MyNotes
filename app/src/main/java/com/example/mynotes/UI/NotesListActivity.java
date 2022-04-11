package com.example.mynotes.UI;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.example.mynotes.Data.InMemoryRepoImp;
import com.example.mynotes.Data.Note;
import com.example.mynotes.Data.Repo;
import com.example.mynotes.Fragments.EditNoteFragment;
import com.example.mynotes.Fragments.NotesListFragment;
import com.example.mynotes.R;
import com.example.mynotes.Recycler.NotesAdapter;

public class NotesListActivity extends AppCompatActivity implements NotesAdapter.OnNoteClickListener, NotesListFragment.Controller {

    private RecyclerView list;
    private Repo repo = InMemoryRepoImp.getInstance();
    private NotesAdapter adapter;
    private Note note;

    private static final String EDIT_NOTE_TAG = "EDIT_NOTE_TAG";

    FragmentManager manager;
    EditNoteFragment editNoteFragment;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        list = findViewById(R.id.list);
//
//        adapter = new NotesAdapter();
//
//        adapter.setOnNoteClickListener(this);
//
//        adapter.setNotes(repo.getAll());
//        list.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
//
//        list.setAdapter(adapter);
//        list.setLayoutManager(new LinearLayoutManager(this));

        NotesListFragment notesListFragment = new NotesListFragment();

        manager = getSupportFragmentManager();

        manager.beginTransaction().replace(R.id.host, notesListFragment).commit();




    }

    public static final int EDIT_NOTE_REQUEST = 5;

    @Override
    public void onNoteClick(Note note) {

        Intent editNoteIntent = new Intent(this, EditNoteActivity.class);
        editNoteIntent.putExtra(Note.NOTE, note);
        startActivityForResult(editNoteIntent, EDIT_NOTE_REQUEST);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == EDIT_NOTE_REQUEST){
            if (resultCode == RESULT_OK && data != null){
                note = (Note) data.getSerializableExtra(Note.NOTE);
                repo.update(note);
                adapter.setNotes(repo.getAll());
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void editNote(Note note) {
        EditNoteFragment editNoteFragment = EditNoteFragment.newInstance(note);
        getSupportFragmentManager().beginTransaction().replace(R.id.host, editNoteFragment).addToBackStack(null).commit();
    }
}