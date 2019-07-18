package com.melardev.android.crud.todos.write;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.melardev.android.crud.R;
import com.melardev.android.crud.common.Repository;
import com.melardev.android.crud.common.Todo;
import com.melardev.android.crud.common.TodoRepository;
import com.melardev.android.crud.remote.ApiResponse;
import com.melardev.android.crud.remote.VolleyRepository;

import java.net.HttpURLConnection;

public class TodoCreateEditActivity extends AppCompatActivity implements Repository.ApiResponseListener<Todo> {
    private EditText eTxtDescription;
    private EditText eTxtTitle;
    private Todo todo;
    private boolean editMode;
    private TodoRepository todoRepository;
    private CheckBox eCheckboxCompleted;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_create_edit);

        this.todoRepository = VolleyRepository.getInstance();

        long todoId = getIntent().getLongExtra("TODO_ID", -1);
        if (todoId != -1) {
            editMode = true;
            todoRepository.getById(todoId, this);
        }

        eTxtTitle = findViewById(R.id.eTxtTitle);
        eTxtDescription = findViewById(R.id.eTxtDescription);
        eCheckboxCompleted = findViewById(R.id.eCheckboxCompleted);
    }

    public void saveTodo(View view) {
        String title = eTxtTitle.getText().toString();
        String description = eTxtDescription.getText().toString();

        if (editMode) {

            todo.setTitle(title);
            todo.setDescription(description);
            todo.setCompleted(eCheckboxCompleted.isChecked());
            todoRepository.update(todo, response -> {
                if (response.getStatusCode() == HttpURLConnection.HTTP_CREATED) {
                    Toast.makeText(TodoCreateEditActivity.this, "Todo updated!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(TodoCreateEditActivity.this, TextUtils.join(",", response.getFullMessages()),
                            Toast.LENGTH_LONG).show();
                }
            });

        } else {

            Todo todo = new Todo();
            todo.setTitle(title);
            todo.setDescription(description);
            todo.setCompleted(eCheckboxCompleted.isChecked());

            todoRepository.insert(todo, response -> {
                if (response.getStatusCode() == HttpURLConnection.HTTP_CREATED) {
                    Toast.makeText(TodoCreateEditActivity.this, "Todo Created!", Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    Toast.makeText(TodoCreateEditActivity.this, TextUtils.join(",", response.getFullMessages()),
                            Toast.LENGTH_LONG).show();
                }
            });
        }


    }


    @Override
    public void onApiResponse(ApiResponse<Todo> response) {
        if (response.getStatusCode() == HttpURLConnection.HTTP_OK) {
            todo = response.getData();
            // txtDetailsId.setText(todo.getId().toString());
            eTxtTitle.setText(todo.getTitle());
            eTxtDescription.setText(todo.getDescription());
            // txtDetailsCreatedAt.setText(todo.getCreatedAt());
            // txtDetailsUpdatedAt.setText(todo.getUpdatedAt());
        } else {

        }
    }
}
