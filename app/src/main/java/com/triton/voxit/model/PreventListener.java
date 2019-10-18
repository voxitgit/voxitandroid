package com.triton.voxit.model;

import android.os.Handler;

public class PreventListener {
    private static PreventListener instance;
    private boolean mIsClickEnable = true;
    private boolean mRunnableEnable = true;


    private PreventListener(){}


    private static synchronized PreventListener getInstance(){
        if(instance == null){
            instance = new PreventListener();
        }
        return instance;
    }



    public static boolean getClickEnable(){



        return getInstance().verifyEnable();


    }



    private boolean verifyEnable(){



        if (mIsClickEnable){
            mIsClickEnable = false;
            if (mRunnableEnable){
                mRunnableEnable = false;
                new Handler().postDelayed(new Runnable() {@Override public void run(){
                    mRunnableEnable = true;
                    mIsClickEnable = true;
                    System.gc();
                }}, 500);
            }
            return true;
        } else {
            return false;
        }}
}
