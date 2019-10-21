package com.triton.voxit.Activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;

import com.triton.voxit.R;
import com.triton.voxit.preference.SFMApp;
import com.triton.voxit.preference.SFMPreference;

import java.util.ArrayList;

public class SplashScreen extends Activity {

    private Handler handler = new Handler();
    public static final int SPLASH_TIMEDELAY = 3*1000;
    private static final String PREFERENCES = "";
    private boolean isLogged;
    private SFMPreference sharedpreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.splash_screen);
        hideStatusBar();

        int currentapiVersion = android.os.Build.VERSION.SDK_INT;

        Context context = this.getApplicationContext();
        SharedPreferences settings=context.getSharedPreferences(PREFERENCES, 0);
        isLogged = settings.getBoolean("isLogged", false);

        if (currentapiVersion >= 14) {
            // Do something for 14 and above versions
        } else {
            // do something for phones running an SDK before 14
        }

    }//end oncreate

    @Override
    protected void onResume() {
        super.onResume();

        handler.postDelayed(new Runnable() {

            @Override
            public void run() {

//                if (SFMApp.appPreference.isLoggedIn()) {
//                    AmpsHomePage.toRefresh = true;
//                    gotoSFMMenuScreen();
//                } else {
//                    gotoSFMLoginScreen();
//                }
                 gotoSFMLoginScreen();
            }
        }, SPLASH_TIMEDELAY);
    }

    private void hideStatusBar() {
        if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            View decorView = getWindow().getDecorView();

            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);

            ActionBar actionBar = getActionBar();
            if (actionBar != null)
                actionBar.hide();
        }
    }

    private void gotoSFMLoginScreen() {

        if (isLogged == false){
            startActivity(new Intent(this, Dashboard.class)
                    .putExtra("login","L").setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
            finish();
        }else if(isLogged == true){
            startActivity(new Intent(this, Dashboard.class)
                    .setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
            finish();
        }

    }

//    private void gotoSFMMenuScreen() {
//        startActivity(new Intent(this, AmpsHomePage.class)
//                .setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
//        finish();
//    }
//@Override
//public void onBackPressed() {
//    super.onBackPressed();
//    Intent intent = new Intent(Intent.ACTION_MAIN);
//    intent.addCategory(Intent.CATEGORY_HOME);
//    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//    startActivity(intent);
//}
}
