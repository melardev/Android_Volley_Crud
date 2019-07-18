package com.melardev.android.crud.remote;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.melardev.android.crud.CrudApplication;
import com.melardev.android.crud.common.Todo;
import com.melardev.android.crud.common.TodoRepository;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

public class VolleyRepository implements TodoRepository {

    private RequestQueue mRequestQueue;
    private static VolleyRepository instance;
    private static final Object mutex = new Object();


    public static VolleyRepository getInstance() {
        if (instance == null) {
            synchronized (mutex) {
                if (instance == null)
                    instance = new VolleyRepository();
            }
        }

        return instance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(CrudApplication.getInstance());
        }

        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    @Override
    public void getAll(GetAllListener<Todo> listener) {
        JsonArrayRequest jArr = new JsonArrayRequest("http://169.254.166.232:8080/api/todos", new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                List<Todo> todos = new ArrayList<>();
                // Parsing json
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject obj = response.getJSONObject(i);

                        Todo item = new Todo();

                        item.setId(obj.getLong("id"));
                        item.setTitle(obj.getString("title"));
                        item.setCompleted(obj.getBoolean("completed"));
                        item.setCreatedAt(obj.getString("created_at"));
                        item.setUpdatedAt(obj.getString("updated_at"));


                        todos.add(item);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                listener.onListLoaded(todos);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });


        addToRequestQueue(jArr);
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void getById(Long id, ApiResponseListener<Todo> listener) {

        ApiResponse<Todo> apiResponse = new ApiResponse<>();

        StringRequest strReq = new StringRequest("http://169.254.166.232:8080/api/todos/" + id, response -> {

            Todo todo = new Todo();
            try {
                JSONObject jObj = new JSONObject(response);
                todo.setId(jObj.getLong("id"));
                todo.setTitle(jObj.getString("title"));
                todo.setDescription(jObj.getString("description"));
                todo.setCompleted(jObj.getBoolean("completed"));
                todo.setCreatedAt(jObj.getString("created_at"));
                todo.setUpdatedAt(jObj.getString("updated_at"));

                apiResponse.setData(todo);

                listener.onApiResponse(apiResponse);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                apiResponse.setStatusCode(response.statusCode);
                return super.parseNetworkResponse(response);
            }
        };


        addToRequestQueue(strReq);
    }

    @Override
    public void insert(Todo todo, ApiResponseListener<Todo> listener) {
        JSONObject requestJson = new JSONObject();
        try {
            requestJson.put("title", todo.getTitle());

            requestJson.put("description", todo.getDescription());
            requestJson.put("completed", todo.isCompleted());
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        ApiResponse<Todo> apiResponse = new ApiResponse<Todo>();

        JsonObjectRequest strReq = new JsonObjectRequest(Request.Method.POST, "http://169.254.166.232:8080/api/todos/", requestJson, response -> {

            Todo createdTodo = new Todo();
            try {
                createdTodo.setId(response.getLong("id"));
                createdTodo.setTitle(response.getString("title"));
                createdTodo.setDescription(response.getString("description"));
                apiResponse.setData(createdTodo);

                listener.onApiResponse(apiResponse);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {
            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                apiResponse.setStatusCode(response.statusCode);
                return super.parseNetworkResponse(response);
            }
        };


        addToRequestQueue(strReq);
    }

    @Override
    public void update(Todo t, ApiResponseListener<Todo> listener) {

        JSONObject tJson = new JSONObject();
        try {
            tJson.put("title", t.getTitle());

            tJson.put("description", t.getDescription());
            tJson.put("completed", t.isCompleted());
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        ApiResponse<Todo> apiResponse = new ApiResponse<Todo>();

        JsonObjectRequest strReq = new JsonObjectRequest(Request.Method.PUT, "http://169.254.166.232:8080/api/todos/" + t.getId(), tJson, response -> {

            Todo todo = new Todo();
            try {
                todo.setId(response.getLong("id"));
                todo.setTitle(response.getString("title"));
                todo.setDescription(response.getString("description"));
                apiResponse.setData(todo);

                listener.onApiResponse(apiResponse);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {
            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                apiResponse.setStatusCode(response.statusCode);
                return super.parseNetworkResponse(response);
            }
        };


        addToRequestQueue(strReq);

    }

    @Override
    public void delete(Todo todo, ApiResponseListener<Void> listener) {
        deleteById(todo.getId(), listener);
    }

    @Override
    public void deleteById(Long todoId, ApiResponseListener<Void> listener) {
        ApiResponse<Void> apiResponse = new ApiResponse<>();

        StringRequest strReq = new StringRequest(Request.Method.DELETE, "http://169.254.166.232:8080/api/todos/" + todoId, response -> {

            if (apiResponse.getStatusCode() != HttpURLConnection.HTTP_NO_CONTENT) {
                JSONObject json = null;
                try {
                    json = new JSONObject(response);

                    JSONArray messages = json.getJSONArray("full_messages");

                    List<String> list = new ArrayList<>();
                    for (int i = 0; i < messages.length(); i++) {
                        list.add(messages.getJSONObject(i).getString("name"));
                    }
                    apiResponse.setFullMessages(list.toArray(new String[list.size()]));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            listener.onApiResponse(apiResponse);

        }, error -> {
        }) {
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                apiResponse.setStatusCode(response.statusCode);
                return super.parseNetworkResponse(response);
            }
        };


        addToRequestQueue(strReq);
    }

    @Override
    public void deleteAll(ApiResponseListener listener) {
        ApiResponse<Void> apiResponse = new ApiResponse<>();

        StringRequest strReq = new StringRequest(Request.Method.DELETE, "http://169.254.166.232:8080/api/todos/", response -> {

            if (apiResponse.getStatusCode() != HttpURLConnection.HTTP_NO_CONTENT) {
                JSONObject json = null;
                try {
                    json = new JSONObject(response);

                    JSONArray messages = json.getJSONArray("full_messages");

                    List<String> list = new ArrayList<>();
                    for (int i = 0; i < messages.length(); i++) {
                        list.add(messages.getJSONObject(i).getString("name"));
                    }
                    apiResponse.setFullMessages(list.toArray(new String[list.size()]));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            listener.onApiResponse(apiResponse);

        }, error -> {
        }) {
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                apiResponse.setStatusCode(response.statusCode);
                return super.parseNetworkResponse(response);
            }
        };


        addToRequestQueue(strReq);
    }


}
