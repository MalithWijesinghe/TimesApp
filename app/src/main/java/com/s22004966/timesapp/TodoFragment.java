package com.s22004966.timesapp;

import android.graphics.Canvas;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.util.List;

public class TodoFragment extends Fragment {

    private RecyclerView recyclerView;
    private Button addButton;
    private EditText todoInput;
    private DatabaseHelper db;
    private TodoAdapter adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_todo, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        addButton = view.findViewById(R.id.addButton);
        todoInput = view.findViewById(R.id.todoInput);

        db = new DatabaseHelper(requireContext());
        adapter = new TodoAdapter(db);


        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter);

        loadTasks();

        addButton.setOnClickListener(v -> {
            String task = todoInput.getText().toString().trim();
            if (!task.isEmpty()) {
                db.insertTask(task);
                todoInput.setText("");
                loadTasks();
            }
        });

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                ToDoModel taskToDelete = adapter.getTaskAt(position);
                db.deleteTask(taskToDelete.getId()); // delete from database
                adapter.removeTask(position);        // remove from adapter
            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView,
                                    @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY,
                                    int actionState, boolean isCurrentlyActive) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                //todo: Add background color while swiping
            }
        });
        itemTouchHelper.attachToRecyclerView(recyclerView);

        return view;
    }

    private void loadTasks() {
        List<ToDoModel> tasks = db.getAllTodo();
        adapter.setTasks(tasks);
    }
}