package com.triton.voxit.preference;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONArray;

public class SFMPreference {


    private SharedPreferences mSharedPreferences;
    private String SINGER_RESPONSE = "emailid";
    private String FLAG = "flag";
//    private String SCHOOL_RESPONSE = "schoolName";


    public SFMPreference(Context context) {

        mSharedPreferences = context.getSharedPreferences("isteer_service_preference", Context.MODE_PRIVATE);

    }

    public String getSINGER_RESPONSE() {
        return mSharedPreferences.getString(SINGER_RESPONSE,"");
    }

    public void setSINGER_RESPONSE(String SINGER_RESPONSE) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(SINGER_RESPONSE,SINGER_RESPONSE);
        editor.commit();
    }

    public String getFLAG() {
        return mSharedPreferences.getString(FLAG,"");
    }

    public void setFLAG(String FLAG) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(FLAG,FLAG);
        editor.commit();
    }

//    public String getSCHOOL_RESPONSE() {
//        return mSharedPreferences.getString(SCHOOL_RESPONSE,"");
//    }
//
//    public void setSCHOOL_RESPONSE(JSONArray school_response) {
//        SharedPreferences.Editor editor = mSharedPreferences.edit();
//        editor.putString(SCHOOL_RESPONSE,school_response.toString());
//        editor.commit();
//    }

}
