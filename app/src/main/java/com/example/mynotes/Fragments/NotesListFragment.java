package com.example.mynotes.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mynotes.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentResultListener;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mynotes.Data.InMemoryRepoImp;
import com.example.mynotes.Data.Note;
import com.example.mynotes.Data.Repo;
import com.example.mynotes.Recycler.NotesAdapter;
import com.example.mynotes.UI.EditNoteActivity;

public class NotesListFragment extends Fragment implements NotesAdapter.OnNoteClickListener {

    private RecyclerView list;
    private Repo repo = InMemoryRepoImp.getInstance();
    private NotesAdapter adapter;
    private Note note;

    private static final String REQUEST_KEY = "REQUEST_KEY";
    private static final String EDITED_NOTE = "EDITED_NOTE";

    @Override
    public void onAttach(@NonNull Context context) {
        if (context instanceof Controller) {
            this.controller = (Controller) context;
        } else {
            throw new IllegalStateException("Activity must implement Controller");
        }
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getChildFragmentManager().setFragmentResultListener(REQUEST_KEY, this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle bundle) {
                // We use a String here, but any type that can be put in a Bundle is supported
               Note note = (Note) bundle.getSerializable(REQUEST_KEY);
                // Do something with the result
                updateNotes(note, note.getId());
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_notes_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        list = view.findViewById(R.id.list);
        list.setLayoutManager(new LinearLayoutManager(requireContext()));
        list.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));

        adapter = new NotesAdapter();
        adapter.setNotes(repo.getAll());
        adapter.setOnNoteClickListener(this);

        list.setAdapter(adapter);

    }

    public interface Controller {
        void editNote(Note note);
    }

    private Controller controller;

    @Override
    public void onNoteClick(Note note) {

        controller.editNote(note);

    }

    public void updateNotes(Note note, int position) {
        if (note.getId() == -1) {
            repo.create(note);
            adapter.notifyItemInserted(repo.getAll().size());
        } else {
            repo.update(note);
            adapter.notifyItemChanged(position);
        }
    }
}
