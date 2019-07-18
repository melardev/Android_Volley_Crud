package com.melardev.android.crud.todos.show;


import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.melardev.android.crud.R;
import com.melardev.android.crud.common.Todo;
import com.melardev.android.crud.common.TodoRepository;
import com.melardev.android.crud.remote.VolleyRepository;
import com.melardev.android.crud.todos.list.MainActivity;
import com.melardev.android.crud.todos.write.TodoCreateEditActivity;

import java.net.HttpURLConnection;

public class TodoDetailsActivity extends AppCompatActivity {

    private TodoRepository todoRepository;
    private Todo todo;
    private Button btnDetailsGoHome;
    private Button btnDetailsEditTodo;
    private Button btnDetailsDeleteTodo;

    private TextView txtDetailsId;
    private TextView txtDetailsTitle;
    private TextView txtDetailsDescription;
    private TextView txtDetailsCreatedAt;
    private TextView txtDetailsUpdatedAt;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_details);

        this.todoRepository = VolleyRepository.getInstance();

        btnDetailsGoHome = findViewById(R.id.btnDetailsGoHome);
        btnDetailsEditTodo = findViewById(R.id.btnDetailsEditTodo);
        btnDetailsDeleteTodo = findViewById(R.id.btnDetailsDeleteTodo);

        txtDetailsId = findViewById(R.id.txtDetailsId);
        txtDetailsTitle = findViewById(R.id.txtDetailsTitle);
        txtDetailsDescription = findViewById(R.id.txtDetailsDescription);
        txtDetailsCreatedAt = findViewById(R.id.txtDetailsCreatedAt);
        txtDetailsUpdatedAt = findViewById(R.id.txtDetailsUpdatedAt);

        Intent intent = getIntent();
        long todoId = intent.getLongExtra("TODO_ID", -1);
        if (todoId == -1) {
            Toast.makeText(this, "Invalid Todo Id", Toast.LENGTH_SHORT).show();
            finish();
        }

        todoRepository.getById(todoId, response -> {
            this.todo = response.getData();

            if (this.todo == null) {
                Toast.makeText(this, "Todo Does not exist", Toast.LENGTH_SHORT).show();
                finish();
            }

            txtDetailsId.setText(todo.getId().toString());
            txtDetailsTitle.setText(todo.getTitle());
            txtDetailsDescription.setText(todo.getDescription());
            txtDetailsCreatedAt.setText(todo.getCreatedAt());
            txtDetailsUpdatedAt.setText(todo.getUpdatedAt());

        });

    }

    void delete() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are you sure You want to delete this todo?");
        alertDialogBuilder.setPositiveButton("Yes",
                (dialogInterface, id) -> todoRepository.deleteById(todo.getId(), response -> {
                    if (response.getStatusCode() == HttpURLConnection.HTTP_NO_CONTENT) {
                        Toast.makeText(TodoDetailsActivity.this, "Todo Deleted successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }));

        alertDialogBuilder.setNegativeButton("No", (dialog, which) -> dialog.dismiss());
    }

    public void onButtonClicked(View view) {

        if (btnDetailsEditTodo == view) {
            Intent intent = new Intent(this, TodoCreateEditActivity.class);
            startActivity(intent);
        } else if (btnDetailsDeleteTodo == view) {
            delete();
            return;
        } else if (btnDetailsGoHome == view) {
            finish();
        }

    }
}
