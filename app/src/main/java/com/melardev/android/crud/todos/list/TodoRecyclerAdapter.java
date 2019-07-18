package com.melardev.android.crud.todos.list;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.melardev.android.crud.R;
import com.melardev.android.crud.common.Todo;

import java.util.ArrayList;
import java.util.List;

public class TodoRecyclerAdapter extends RecyclerView.Adapter<TodoRecyclerAdapter.ViewHolder> {

    interface TodoRowEventListener {
        void onClicked(Todo todo);
    }

    private final ArrayList<Todo> todos;
    private final TodoRowEventListener todoRowEventListener;

    public TodoRecyclerAdapter(TodoRowEventListener todoRowEventListener) {
        this.todoRowEventListener = todoRowEventListener;
        this.todos = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemBinding = layoutInflater.inflate(R.layout.todo_row, parent, false);
        return new ViewHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        Todo todo = todos.get(position);

        viewHolder.txtId.setText(String.valueOf(todo.getId()));
        viewHolder.txtTitle.setText(todo.getTitle());
        viewHolder.txtDescription.setText(todo.getDescription());
        viewHolder.checkboxCompleted.setChecked(todo.isCompleted());
        viewHolder.txtCreatedAt.setText(todo.getCreatedAt());
        viewHolder.txtUpdatedAt.setText(todo.getUpdatedAt());

        viewHolder.itemView.setOnClickListener(v -> todoRowEventListener.onClicked(todo));
    }

    @Override
    public int getItemCount() {
        return todos.size();
    }

    public void addItems(List<Todo> todos) {
        this.todos.clear();
        setItems(todos);
    }

    public void setItems(List<Todo> todos) {
        if (todos == null)
            return;
        int startPosition = this.todos.size();
        this.todos.addAll(todos);
        notifyItemRangeChanged(startPosition, todos.size());
    }

    public Todo getItem(int position) {
        return todos.get(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView txtId;
        private final TextView txtTitle;
        private final TextView txtDescription;
        private final TextView txtCreatedAt;
        private final TextView txtUpdatedAt;
        private final CheckBox checkboxCompleted;

        public ViewHolder(View view) {
            super(view);
            txtId = view.findViewById(R.id.txtId);
            txtTitle = view.findViewById(R.id.txtTitle);
            txtDescription = view.findViewById(R.id.txtDescription);
            checkboxCompleted = view.findViewById(R.id.checkboxCompleted);
            txtCreatedAt = view.findViewById(R.id.txtCreatedAt);
            txtUpdatedAt = view.findViewById(R.id.txtUpdatedAt);

        }

    }
}
