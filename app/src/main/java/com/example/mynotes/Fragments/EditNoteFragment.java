package com.example.mynotes.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.mynotes.Data.Note;
import com.example.mynotes.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class EditNoteFragment extends Fragment implements View.OnClickListener {

    private Note note;

    private EditText editTitle;
    private EditText editDescription;
    private Button saveChanges;

    private static final String ARG_PARAM = "PARAM";
    private static final String REQUEST_KEY = "REQUEST_KEY";
    private static final String EDITED_NOTE = "EDITED_NOTE";
    private static final String EDIT_NOTE_TAG = "EDIT_NOTE_TAG";




    public static EditNoteFragment newInstance(Note note){
        EditNoteFragment fragment = new EditNoteFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM, note);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_edit_note, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        editTitle = view.findViewById(R.id.edit_title);
        editDescription = view.findViewById(R.id.edit_description);
        saveChanges = view.findViewById(R.id.button_save_changes);

        Bundle args = getArguments();

        if (args != null && args.containsKey(ARG_PARAM)){
            note = (Note) args.getSerializable(ARG_PARAM);

            //int noteID = note.getId();
            editTitle.setText(note.getTitle());
            editDescription.setText(note.getDescription());
        }

        saveChanges.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        Note editedNote = new Note(editTitle.getText().toString(), editDescription.getText().toString());
        editedNote.setId(note.getId());

        ((EditNoteController) requireActivity()).noteEdited(editedNote);


//        Bundle editedNoteArgs = new Bundle();
//        editedNoteArgs.putSerializable(EDITED_NOTE, editedNote);
//
//       getParentFragmentManager().setFragmentResult(REQUEST_KEY, editedNoteArgs);

        requireActivity().getSupportFragmentManager().popBackStack();


    }

    public interface EditNoteController{
        void noteEdited(Note note);
    }

    private EditNoteController editNoteController;
}
