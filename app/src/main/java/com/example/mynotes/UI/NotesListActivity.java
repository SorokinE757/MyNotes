package com.example.mynotes.UI;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.mynotes.Data.InMemoryRepoImp;
import com.example.mynotes.Data.Note;
import com.example.mynotes.Data.Repo;
import com.example.mynotes.Fragments.EditNoteFragment;
import com.example.mynotes.Fragments.NotesListFragment;
import com.example.mynotes.Fragments.ShowInfo;
import com.example.mynotes.R;
import com.example.mynotes.Recycler.NotesAdapter;
import com.google.android.material.navigation.NavigationView;

public class NotesListActivity extends AppCompatActivity implements NotesAdapter.OnNoteClickListener, NotesListFragment.Controller, EditNoteFragment.EditNoteController {

    private RecyclerView list;
    private Repo repo = InMemoryRepoImp.getInstance();
    private NotesAdapter adapter;
    private Note note;
    NotesListFragment notesListFragment;

    private static final String EDIT_NOTE_TAG = "EDIT_NOTE_TAG";

    FragmentManager manager;

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

//        initToolbar();
        initToolbarAndDrawer();

        notesListFragment = new NotesListFragment();

        manager = getSupportFragmentManager();

        if (savedInstanceState == null) {
            manager.beginTransaction().replace(R.id.host, new NotesListFragment(), EDIT_NOTE_TAG).commit();
        }

        if (isLandScape()) {
            note = new Note(-1, "New note", "New description");
            getSupportFragmentManager().beginTransaction().replace(R.id.edit_note_host, EditNoteFragment.newInstance(note)).replace(R.id.host, new NotesListFragment(), EDIT_NOTE_TAG).commit();
        }

        //initToolbarAndDrawer();

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

        if (requestCode == EDIT_NOTE_REQUEST) {
            if (resultCode == RESULT_OK && data != null) {
                note = (Note) data.getSerializableExtra(Note.NOTE);
                repo.update(note);
                adapter.setNotes(repo.getAll());
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void editNote(Note note) {
        if (isLandScape()) {
            EditNoteFragment editNoteFragment = EditNoteFragment.newInstance(note);
            getSupportFragmentManager().beginTransaction().replace(R.id.edit_note_host, editNoteFragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit();
        } else {
            EditNoteFragment editNoteFragment = EditNoteFragment.newInstance(note);
            getSupportFragmentManager().beginTransaction().replace(R.id.host, editNoteFragment).addToBackStack(null)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit();
        }
    }

    @Override
    public void noteEdited(Note note) {
        repo.update(note);
        ((NotesListFragment) manager.findFragmentByTag(EDIT_NOTE_TAG)).updateNotes(note, note.getId());
    }

    private boolean isLandScape() {
        return getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_add:

                note = new Note(-1, "New note", "New description");

                if (isLandScape()) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.edit_note_host, EditNoteFragment.newInstance(note))
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit();
                } else {
                    getSupportFragmentManager().beginTransaction().replace(R.id.host, EditNoteFragment.newInstance(note)).addToBackStack(null)
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

//    private void initToolbar() {
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);


    private void initToolbarAndDrawer() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initDrawer(toolbar);
    }


    private void initDrawer(Toolbar toolbar) {

        final DrawerLayout drawer = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.navi_menu:
                        showInfo();
                        drawer.close();
                        return true;
                }
                return false;
            }
        });
    }

    private void showInfo() {
        if (isLandScape()) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.host, new ShowInfo())
                    .addToBackStack(null)
                    .commit();
        }
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.host, new ShowInfo())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onBackPressed() {

        if (getSupportFragmentManager().getBackStackEntryCount() != 0) {
            super.onBackPressed();
        }else {
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Закрытие приложения")
                    .setMessage("Вы точно хотите выйти?")
                    .setPositiveButton("Да", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                            Toast.makeText(NotesListActivity.this, "Пока, пока", Toast.LENGTH_SHORT).show();
                        }

                    })
                    .setNegativeButton("Нет", null)
                    .show();
        }


    }

    }

