package com.melardev.android.crud;

import android.app.Application;

public class CrudApplication extends Application {

    private static CrudApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static CrudApplication getInstance() {
        return instance;
    }
}
