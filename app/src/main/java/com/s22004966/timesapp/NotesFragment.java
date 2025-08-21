package com.s22004966.timesapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.util.List;

public class NotesFragment extends Fragment {

    EditText title, note;
    Button addButton;
    RecyclerView recyclerView;
    DatabaseHelper db;
    List<NoteModel> noteList;
    NoteAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notes, container, false);

        title = view.findViewById(R.id.titleInput);
        note = view.findViewById(R.id.noteInput);
        addButton = view.findViewById(R.id.addNoteButton);
        recyclerView = view.findViewById(R.id.notesRecyclerView);
        db = new DatabaseHelper(getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // load items from database
        noteList = db.getAllNotes();
        adapter = new NoteAdapter(noteList);
        recyclerView.setAdapter(adapter);

        addButton.setOnClickListener(v -> {
            String titleString = title.getText().toString().trim();
            String noteString = note.getText().toString().trim();

            if (titleString.isEmpty() || noteString.isEmpty()) {
                Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            db.insertNote(titleString, noteString);
            loadNotes();

            title.setText("");
            note.setText("");

            Toast.makeText(getContext(), "Note added", Toast.LENGTH_SHORT).show();
        });

        return view;
    }

    private void loadNotes() {
        noteList.clear(); // clear old list
        noteList.addAll(db.getAllNotes()); // use class-level db
        adapter.notifyDataSetChanged();
    }
}