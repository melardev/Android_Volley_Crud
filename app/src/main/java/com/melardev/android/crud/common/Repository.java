package com.melardev.android.crud.common;

import com.melardev.android.crud.remote.ApiResponse;

import java.util.List;

public interface Repository<ID, T> {

    interface GetAllListener<T> {
        void onListLoaded(List<T> response);
    }

    interface ApiResponseListener<T> {
        void onApiResponse(ApiResponse<T> response);
    }

    void getAll(GetAllListener<T> listener);

    long count();

    void getById(ID id, ApiResponseListener<T> listener);

    void insert(T t, ApiResponseListener<T> listener);

    void update(T t, ApiResponseListener<T> listener);

    void delete(T t, ApiResponseListener<Void> listener);

    void deleteById(ID id, ApiResponseListener<Void> listener);

    void deleteAll(ApiResponseListener<Void> listener);
}
