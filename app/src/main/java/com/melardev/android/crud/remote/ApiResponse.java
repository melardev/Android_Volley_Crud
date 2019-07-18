package com.melardev.android.crud.remote;

public class ApiResponse<T> {
    private int statusCode;
    private String[] fullMessages;
    private T data;

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String[] getFullMessages() {
        return fullMessages;
    }

    public void setFullMessages(String[] fullMessages) {
        this.fullMessages = fullMessages;
    }
}
