package com.triton.voxit.preference;

import android.app.Application;

public class SFMApp extends Application {

    public static final String TAG = "SFMApp";

    public static SFMPreference appPreference;



    @Override

    public void onCreate() {

        super.onCreate();

        appPreference = new SFMPreference(this);

    }



}
