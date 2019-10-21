package com.triton.voxit.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.triton.voxit.Adapter.SelectGenreAdapter;
import com.triton.voxit.Api.APIClient;
import com.triton.voxit.Api.APIInterface;
import com.triton.voxit.Interface.ResetPasswordInterface;
import com.triton.voxit.R;
import com.triton.voxit.SessionManager.SessionManager;
import com.triton.voxit.app.App;
import com.triton.voxit.model.GenreData;
import com.triton.voxit.model.GenreResponseData;
import com.triton.voxit.model.ResetPasswordData;
import com.triton.voxit.model.ResetPasswordRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResetPasswordActivity extends NavigationDrawer implements ResetPasswordInterface, View.OnClickListener {
    ResetPasswordInterface resetPasswordInterface;
    TextView headertitle;
    BottomNavigationView bottomNavigationView;
    EditText password_edit,confirm_pw_edit;
    Button resetBtn;
    private APIInterface apiInterface;
    private ResetPasswordRequest resetPasswordRequest;
    private ArrayList<ResetPasswordData> resetPasswordData;
    SessionManager session;
    String referral_code;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reset_password_activity);
        resetPasswordInterface = (ResetPasswordInterface)this;
        resetPasswordInterface.initToolbar();
        resetPasswordInterface.getSessionData();
        resetPasswordInterface.initVar();
        resetPasswordInterface.showHeader();
    }

    @Override
    public void initVar() {
        password_edit = (EditText) findViewById(R.id.password_edit);
        confirm_pw_edit = (EditText) findViewById(R.id.confirm_pw_edit);
        resetBtn = (Button) findViewById(R.id.resetBtn);
        resetBtn.setOnClickListener(this);
    }

    @Override
    public void initToolbar() {
        headertitle = (TextView) findViewById(R.id.header_title);
        headertitle.setText("Reset Password");
        bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottom_navigation);
        bottomNavigationView.getMenu().findItem(R.id.profile).setChecked(true);
        navigationMenu();
    }
    private void navigationMenu(){
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                switch (item.getItemId()) {
                    case R.id.home:
                        Intent h = new Intent(ResetPasswordActivity.this,Dashboard.class);
                        h.putExtra("login","F");
                        startActivity(h);
                        break;
                    case R.id.notifi:
                        Intent j = new Intent(ResetPasswordActivity.this,NotificationActivity.class);
                        // j.putExtra("login",flag);
                        startActivity(j);
                        break;
                    case R.id.search:
                        Intent i4 = new Intent(ResetPasswordActivity.this,SearchActivity.class);
                        // i4.putExtra("login",flag);
                        startActivity(i4);
                        break;
                    case R.id.profile:
                        Intent i = new Intent(ResetPasswordActivity.this,ProfileActivity.class);
                        // i.putExtra("login",flag);
                        startActivity(i);
                        break;
//                    case R.id.settings:
//                        // Switch to page three
//                        break;


                }
                return true;
            }
        });
    }
    @Override
    public void getSessionData() {
        session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        referral_code = user.get(SessionManager.KEY_USER_NAME);

        Log.e("user details",referral_code);
        if(session.isLoggedIn()){

        }else {
            startActivity(new Intent(this, LoginActivity.class)
                    .setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
        }
    }

    @Override
    public void showHeader() {
        hideLanguageView();
        showTitleView();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.resetBtn:
                if (!App.appUtils.isNetAvailable()) {
                    alertUserP(ResetPasswordActivity.this, "Connection Error", "No Internet connection available", "OK");
                }else {
                    if (!vaildPassword()) {

                        return;
                    }
                    if (!vaildReEnterPassword()) {

                        return;
                    }
                    getResetPwdData();
                }
                break;
        }
    }

    private void getResetPwdData() {
        Integer jockey_id = 5;
        JSONObject json = new JSONObject();
        try {
            json.put("jockey_id", jockey_id);
            json.put("password", password_edit.getText().toString());
            Log.e("params",json.toString());
        } catch (JSONException e) {
            Log.e("Exception ", e.toString());
        }

        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), String.valueOf(json));
        //Creating an object of our api interface
        apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<ResetPasswordRequest> call = apiInterface.getResetPwdData(body);
        Log.e("url",call.request().url().toString());

        call.enqueue(new Callback<ResetPasswordRequest>() {
            @Override
            public void onResponse(Call<ResetPasswordRequest> call, Response<ResetPasswordRequest> response) {
                //pDialog.dismiss();
                resetPasswordRequest = response.body();
               // resetPasswordData = resetPasswordRequest.getData();

                if(resetPasswordRequest.getStatus().equals("Failure")) {
                    showDialogMethod("Alert", resetPasswordRequest.getStatus());
                } else if(resetPasswordRequest.getStatus().equals("Success")){

                    showDialogMethod("Alert", resetPasswordRequest.getStatus());
                }
            }

            @Override
            public void onFailure(Call<ResetPasswordRequest> call, Throwable t) {
                //pDialog.dismiss();
                Log.e("Error",t.getMessage());
            }
        });
    }
    private void showDialogMethod(String title, String message) {
        KeyboardHide();
        AlertDialog alertDialog = new AlertDialog.Builder(ResetPasswordActivity.this).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        alertDialog.show();

    }
    private void KeyboardHide() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.hideSoftInputFromWindow(etRenterPassword.getWindowToken(), 0);
    }
    public void alertUserP(Context context, String title, String msg, String btn) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
        builder.setMessage(msg).setTitle(title).setCancelable(false)
                .setPositiveButton(btn, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        android.app.AlertDialog alert = builder.create();
        alert.show();
    }
    public boolean vaildPassword() {

        if (password_edit.getText().toString().trim().isEmpty()) {
            // first_name_layout.setError(getString(R.string.err_msg_first_name));
            // requestFocus(first_name);
            final AlertDialog alertDialog = new AlertDialog.Builder(ResetPasswordActivity.this).create();
            // alertDialog.setTitle(title);
            alertDialog.setMessage(getString(R.string.err_msg_Password));
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Ok",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // finish();
                            alertDialog.cancel();
                        }
                    });
            alertDialog.show();

            return false;
        } else{
            password_edit.setError(null);
        }

        return true;
    }
    public boolean vaildReEnterPassword() {

        if (confirm_pw_edit.getText().toString().trim().isEmpty()) {
            // first_name_layout.setError(getString(R.string.err_msg_first_name));
            // requestFocus(first_name);
            final AlertDialog alertDialog = new AlertDialog.Builder(ResetPasswordActivity.this).create();
            // alertDialog.setTitle(title);
            alertDialog.setMessage(getString(R.string.err_msg_confirm_password));
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Ok",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // finish();
                            alertDialog.cancel();
                        }
                    });
            alertDialog.show();

            return false;
        }else if (!password_edit.getText().toString().trim().equals(confirm_pw_edit.getText().toString())) {
            final AlertDialog alertDialog = new AlertDialog.Builder(ResetPasswordActivity.this).create();
            // alertDialog.setTitle(title);
            alertDialog.setMessage(getString(R.string.err_msg_confirm_password_worng));
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Ok",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // finish();
                            alertDialog.cancel();
                        }
                    });
            alertDialog.show();
            return false;
        }
//        }else if (password_edit.getText().toString().trim().equals(confirm_pw_edit.getText().toString())){
//            final AlertDialog alertDialog = new AlertDialog.Builder(ResetPasswordActivity.this).create();
//            // alertDialog.setTitle(title);
//            alertDialog.setMessage(getString(R.string.err_msg_confirm_password_worng));
//            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Ok",
//                    new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int which) {
//                            // finish();
//                            alertDialog.cancel();
//                        }
//                    });
//            alertDialog.show();
//            return false;
//        }
        else {
            confirm_pw_edit.setError(null);
        }

        return true;
    }
}
