package com.triton.voxit.SessionManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.triton.voxit.Activity.LoginActivity;

import java.util.HashMap;

public class SessionManager {
  String TAG = "SessionManager";
  SharedPreferences pref;
  SharedPreferences.Editor editor;
  Context _context;

  int PRIVATE_MODE = 0;

  private static final String PREF_NAME = "Session Manager";
  private static final String IS_LOGIN = "IsLoggedIn";
  public static final String KEY_USER_NAME = "name";
  public static final String KEY_EMAIL = "emailid";
  public static final String KEY_FIRST_NAME = "first_name";
  public static final String KEY_SECOND_NAME = "second_name";
  public static final String KEY_DESIGNATION = "designation";
  public static final String KEY_STATUS = "status";
  public static final String KEY_DES_ID = "desId";
  public static final String KEY_EMP_ID = "emailid";
  public static final String KEY_MOBILE = "phoneno";
  public static final String KEY_REFERRAL_CODE = "referral_code";
  public static final String KEY_REFERRED_CODE = "referred_code";
  public static final String JOCKEY_ID = "jockey_id";
  public static final String UPDATE_STATUS = "update_status";
  public static final String IMAGE_PATH = "image_path";
  public static final String KEY_TOKEN = "refreshedToken";
  public static final String KEY_USER_MODE = "user_mode";
  public static final String APPLIED_JOCKEY = "isAppliedForJockey";

  public SessionManager(Context context){
    this._context = context;
    pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
    editor = pref.edit();
  }

  public void createAppliedJockeySession(Boolean applied_jockey) {
    editor.putBoolean(APPLIED_JOCKEY, applied_jockey);
    editor.commit();
  }

  public void createLoginSession(int jockey_id, String username, String empID, String mobile, String referral_code, String update_status,
                                 String referred_code, String image_path, String user_mode, Boolean applied_jockey){
    editor.putBoolean(IS_LOGIN, true);
    editor.putString(KEY_USER_NAME, username);
    editor.putString(KEY_EMP_ID, empID);
    editor.putString(KEY_MOBILE, mobile);
    editor.putString(KEY_REFERRAL_CODE,referral_code);
    editor.putString(UPDATE_STATUS,update_status);
    editor.putString(KEY_REFERRED_CODE,referred_code);
    editor.putString(IMAGE_PATH,image_path);
    editor.putString(KEY_USER_MODE,user_mode);
    editor.putInt(JOCKEY_ID, jockey_id);
    editor.putBoolean(APPLIED_JOCKEY, applied_jockey);

    // editor.putString(KEY_COMPANYSIZE_NEW, company_size_new);
    Log.e(TAG, "................................>> session Login Details " + username + empID + mobile + referral_code + jockey_id +
            update_status + referred_code + image_path + user_mode + applied_jockey);

    editor.commit();

  }
  public void updateToken(String refreshedToken){

    //editor.putBoolean(IS_LOGIN, true);
    editor.putString(KEY_TOKEN, refreshedToken);
    editor.commit();

    Log.d(TAG,"------------->update token"+refreshedToken);
  }
  public void createRegisterSession(String username, String empID, String mobile, String referral_code, String referred_code){
    editor.putBoolean(IS_LOGIN, true);
    editor.putString(KEY_USER_NAME, username);
    editor.putString(KEY_EMP_ID, empID);
    editor.putString(KEY_MOBILE, mobile);
    editor.putString(KEY_REFERRAL_CODE,referral_code);
    editor.putString(KEY_REFERRED_CODE,referred_code);

    // editor.putString(KEY_COMPANYSIZE_NEW, company_size_new);
    Log.d(TAG, "................................>> session Login Details " + username + empID + mobile + referral_code + referred_code);

    editor.commit();

  }

  public HashMap<String, String> getUserDetails(){

    HashMap<String, String> user = new HashMap<String, String>();

    user.put(KEY_USER_NAME, pref.getString(KEY_USER_NAME, ""));

    user.put(KEY_EMP_ID, pref.getString(KEY_EMP_ID, ""));

    user.put(KEY_MOBILE, pref.getString(KEY_MOBILE, ""));

    user.put(KEY_DES_ID, pref.getString(KEY_DES_ID, ""));

    user.put(KEY_FIRST_NAME, pref.getString(KEY_FIRST_NAME, ""));

    user.put(KEY_SECOND_NAME, pref.getString(KEY_SECOND_NAME, ""));

    user.put(KEY_DESIGNATION, pref.getString(KEY_DESIGNATION, ""));

    user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, ""));

    user.put(KEY_USER_MODE, pref.getString(KEY_USER_MODE, ""));

    user.put(KEY_STATUS, pref.getString(KEY_STATUS, ""));

    user.put(KEY_REFERRAL_CODE, pref.getString(KEY_REFERRAL_CODE, ""));

    user.put(KEY_REFERRED_CODE, pref.getString(KEY_REFERRED_CODE, ""));

    user.put(UPDATE_STATUS, pref.getString(UPDATE_STATUS, ""));

    user.put(IMAGE_PATH, pref.getString(IMAGE_PATH, ""));

    user.put(JOCKEY_ID, String.valueOf(pref.getInt(JOCKEY_ID, 0)));

    user.put(APPLIED_JOCKEY, String.valueOf(pref.getBoolean(APPLIED_JOCKEY,false)));

    return user;
  }

  public boolean checkLogin(){

    if(!this.isLoggedIn()){

      //  Toast.makeText(_context,"Please Logged In",Toast.LENGTH_LONG).show();

//            Intent i = new Intent(_context, CampusLogin.class);
//            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            _context.startActivity(i);

    }else if(this.isLoggedIn()){

      //Toast.makeText(_context,"You are Logged In",Toast.LENGTH_LONG).show();
    }

    return false;
  }

  public void logoutUser(){

    editor.clear();
    editor.commit();

    //Toast.makeText(_context,"Logout Success ", Toast.LENGTH_LONG).show();

    Intent i = new Intent(_context, LoginActivity.class);
    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
    _context.startActivity(i);

  }

  public boolean isLoggedIn(){

    return pref.getBoolean(IS_LOGIN, false);
  }


}
