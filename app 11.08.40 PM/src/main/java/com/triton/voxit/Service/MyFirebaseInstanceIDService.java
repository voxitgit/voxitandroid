package com.triton.voxit.Service;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.triton.voxit.SessionManager.SessionManager;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by USER on 15-02-2018.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "MyFirebaseIIDService";

    SessionManager session;
    String refreshedToken;

    @Override
    public void onTokenRefresh() {
        refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.e(TAG, "---------->Refreshed token: " + refreshedToken);
        storeToken(refreshedToken);
    }

    private void storeToken(String refreshedToken) {

        session = new SessionManager(getApplicationContext());
        session.updateToken(refreshedToken);
        //saving the token on shared preferences
       // SharedPrefManager.getInstance(getApplicationContext()).saveDeviceToken(refreshedToken);
    }
}
