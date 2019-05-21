package com.example.teacher_parent;

import android.app.Application;

public class App extends Application {

    private static final String TAG = "App";
    private static App sApp;

    @Override
    public void onCreate() {
        super.onCreate();
        sApp = this;
    }
    public static App getInstance() {
        return sApp;
    }
}
