package com.triton.voxit.app;

import android.app.Application;

import com.triton.voxit.SessionManager.SessionManager;

public class PFApp extends Application {
    public static final String TAG = "PFApp";
    public static SessionManager appPreference;

    @Override
    public void onCreate() {
        super.onCreate();
        appPreference = new SessionManager(this);

    }

}
