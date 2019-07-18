package com.melardev.android.crud.todos.list;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.melardev.android.crud.R;
import com.melardev.android.crud.common.Todo;
import com.melardev.android.crud.common.TodoRepository;
import com.melardev.android.crud.remote.VolleyRepository;
import com.melardev.android.crud.todos.show.TodoDetailsActivity;
import com.melardev.android.crud.todos.write.TodoCreateEditActivity;

public class MainActivity extends AppCompatActivity implements TodoRecyclerAdapter.TodoRowEventListener {

    private TodoRepository todoRepository;

    private RecyclerView recyclerView;
    private TodoRecyclerAdapter todoRecyclerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.todoRepository = VolleyRepository.getInstance();

        recyclerView = findViewById(R.id.rvTodos);

        todoRepository.getAll(todos -> todoRecyclerAdapter.setItems(todos));

        todoRecyclerAdapter = new TodoRecyclerAdapter(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(todoRecyclerAdapter);

    }

    public void createTodo(View view) {
        Intent intent = new Intent(this, TodoCreateEditActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClicked(Todo todo) {
        Intent intent = new Intent(this, TodoDetailsActivity.class);
        intent.putExtra("TODO_ID", todo.getId());
        startActivity(intent);
    }

}
