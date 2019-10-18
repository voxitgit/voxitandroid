package com.triton.voxit.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.triton.voxit.Api.APIClient;
import com.triton.voxit.Api.APIInterface;
import com.triton.voxit.Interface.ReferralCodeInterface;
import com.triton.voxit.R;
import com.triton.voxit.SessionManager.SessionManager;
import com.triton.voxit.app.App;
import com.triton.voxit.model.InsertPointData;
import com.triton.voxit.model.InsertPointsRequest;
import com.triton.voxit.model.SignUpRequest;
import com.triton.voxit.model.SingupData;
import com.triton.voxit.model.UpdateData;
import com.triton.voxit.model.UpdatereferalRequest;
import com.triton.voxit.model.singupDatabeen;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReferralCodeActivity extends NavigationDrawer implements ReferralCodeInterface, View.OnClickListener {
    String TAG = "ReferralCodeActivity";
    TextView referral_codeTv,headertitle,text,points_noTv;
    Button shareBtn,submitBtn;
    EditText referral_codeEt;
    BottomNavigationView bottomNavigationView;
    SessionManager session;
    String referral_code,update_status,referred_code;
    ReferralCodeInterface referralCodeInterface;
    ProgressDialog pDialog;
    private APIInterface apiInterface;
    int jockey_id;
    LinearLayout linear;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.refferal_code_activity);
        referralCodeInterface = (ReferralCodeInterface)this;
        referralCodeInterface.initToolbar();
        referralCodeInterface.initVar();
        referralCodeInterface.showHeader();
    }

    @Override
    public void initVar() {
        referral_codeTv = (TextView) findViewById(R.id.referral_codeTv);
//        text = (TextView) findViewById(R.id.text);
        points_noTv = (TextView) findViewById(R.id.points_no);
        shareBtn = (Button) findViewById(R.id.shareBtn);
        submitBtn = (Button) findViewById(R.id.submitBtn);
        linear = (LinearLayout) findViewById(R.id.linear);
        referral_codeEt = (EditText) findViewById(R.id.referral_codeEt);
        referralCodeInterface.getSessionData();
//        linear.setVisibility(View.VISIBLE);
//        submitBtn.setVisibility(View.VISIBLE);
        shareBtn.setOnClickListener(this);
        submitBtn.setOnClickListener(this);
    }
    public boolean vaildReferralCode() {

        if (referral_codeEt.getText().toString().trim().isEmpty()) {
            // first_name_layout.setError(getString(R.string.err_msg_first_name));
            // requestFocus(first_name);
            final AlertDialog alertDialog = new AlertDialog.Builder(ReferralCodeActivity.this).create();
            // alertDialog.setTitle(title);
            alertDialog.setMessage(getString(R.string.err_msg_referral_code));
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Ok",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // finish();
                            alertDialog.cancel();
                        }
                    });
            alertDialog.show();

            return false;
        } else if (referral_codeEt.getText().toString().trim().equals(referral_code)) {
            // first_name_layout.setError(getString(R.string.err_msg_first_name));
            // requestFocus(first_name);
            final AlertDialog alertDialog = new AlertDialog.Builder(ReferralCodeActivity.this).create();
            // alertDialog.setTitle(title);
            alertDialog.setMessage(getString(R.string.err_msg_referral_wrong));
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Ok",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // finish();
                            alertDialog.cancel();
                        }
                    });
            alertDialog.show();

            return false;
        }else {
            referral_codeEt.setError(null);
        }

        return true;
    }
    @Override
    public void initToolbar() {
        pDialog = new ProgressDialog(ReferralCodeActivity.this);
        pDialog.setMessage(ReferralCodeActivity.this.getString(R.string.please_wait));
        pDialog.setIndeterminate(true);
        pDialog.setCancelable(true);
        headertitle = (TextView) findViewById(R.id.header_title);
        headertitle.setText("Referral");
        bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottom_navigation);
        bottomNavigationView.getMenu().findItem(R.id.profile).setChecked(true);
        navigationMenu();
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
    @Override
    public void getSessionData() {
        session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        referral_code = user.get(SessionManager.KEY_REFERRAL_CODE);
        referred_code = user.get(SessionManager.KEY_REFERRED_CODE);
        update_status = user.get(SessionManager.UPDATE_STATUS);
        jockey_id = Integer.parseInt(user.get(SessionManager.JOCKEY_ID));
        Log.e("jockey_id", String.valueOf(jockey_id));
        Log.e("user details",referral_code + update_status);
        referral_codeTv.setText("My referral code");
        points_noTv.setText(referral_code);
        if(session.isLoggedIn()){

        }else {
            startActivity(new Intent(this, LoginActivity.class)
                    .setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
        }
//        if(update_status.equals("Success")){
//            linear.setVisibility(View.GONE);
//            submitBtn.setVisibility(View.GONE);
//            //referral_codeEt.setText("");
////                        referral_codeEt.setBackgroundDrawable(ContextCompat.getDrawable(ReferralCodeActivity.this, R.color.gray));
////                           referral_codeEt.setEnabled(false);
//            //referral_codeEt.setTextColor( getResources().getColor(R.color.gray) );
//        }else {
//
//        }
        if(referred_code.equals("")||referred_code.equals("null")||referred_code==null){
            linear.setVisibility(View.VISIBLE);
            submitBtn.setVisibility(View.VISIBLE);

        }else {
            linear.setVisibility(View.GONE);
            submitBtn.setVisibility(View.GONE);
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
            case R.id.submitBtn:
                if (!App.appUtils.isNetAvailable()) {
                    alertUserP(ReferralCodeActivity.this, "Connection Error", "No Internet connection available", "OK");
                }else {
                    if (!vaildReferralCode()) {

                        return;
                    }
                    updateReferralCode();
                }
                break;
            case R.id.shareBtn:
                if (!App.appUtils.isNetAvailable()) {
                    alertUserP(ReferralCodeActivity.this, "Connection Error", "No Internet connection available", "OK");
                }else {
                    byte[] bytes = new byte[0];
                    try {
                        bytes = referral_code.getBytes("UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    String encoded = android.util.Base64.encodeToString(bytes, android.util.Base64.DEFAULT);
                    //byte[] decoded = Base64.getMimeDecoder().decode(encoded);


                    try {
                        byte[] output = android.util.Base64.decode(encoded, android.util.Base64.DEFAULT);
                        // System.out.println(output);

                        String url = new String(output);
                        Log.e(TAG, "decode value :" + url);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
//                    String tempString=referral_code;
//                    SpannableString spanString = new SpannableString(tempString);
//                    spanString.setSpan(new UnderlineSpan(), 0, spanString.length(), 0);
//                    spanString.setSpan(new StyleSpan(Typeface.BOLD), 0, spanString.length(), 0);
//                    spanString.setSpan(new StyleSpan(Typeface.ITALIC), 0, spanString.length(), 0);
//                    text.setText(spanString);

//                    String str_text = "<a href=http://www.google.com >Google</a>";
//                    text.setMovementMethod(LinkMovementMethod.getInstance());
//                    text.setText(Html.fromHtml(str_text));
//                    text.setLinkTextColor(Color.BLUE);
//                    Log.e("text",text.getText().toString());
                    String msg = "\nI love using Voxit - Voice of World!.";
                    String msg1 = "\nI am inviting you to explore this app.\nUse my referral code: "+ referral_code + "\n";
                    Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    String shareBody = msg +"\n" + "https://play.google.com/store/apps/details?id=com.triton.voxit\n" + msg1 + "\n";
                    Log.e("shareBody",shareBody);
//String shareBody = "https://play.google.com/store/apps/details?id=com.triton.voxit";
                    sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Voxit-Music Let's here to Enjoy the audios");
                    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                    startActivity(Intent.createChooser(sharingIntent, "Share via"));
                }
                break;
        }
    }
    private void updateReferralCode() {
        pDialog.show();

        JSONObject json = new JSONObject();
        try {
            json.put("jockey_id", jockey_id);
            json.put("referred_code", referral_codeEt.getText().toString());
            Log.e("tttt", String.valueOf(json));
        } catch (JSONException e) {
            Log.e("Exception ", e.toString());
        }

        apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<UpdatereferalRequest> call = apiInterface.UpdateReferralRequest(json.toString());

        call.enqueue(new Callback<UpdatereferalRequest>() {
            @Override
            public void onResponse(Call<UpdatereferalRequest> call, Response<UpdatereferalRequest> response) {
                // pDialog.show();
                submitBtn.setEnabled(true);
                if (response.body() != null){
                    UpdatereferalRequest resource = response.body();
                    if (resource.getStatus().equals("Success")){
                        showDialogMethod("Success","Referral code updated successfully");
                        UpdateData data = resource.getData();
                            int jockey_id =data.getJockey_id();
                            String name =data.getName();
                            Log.e("name",name);
                            String email = data.getEmailid();
                            Log.e("email",email);
                            String phone_no = data.getPhoneno();
                            String referral_code = data.getReferral_code();
                            String referred_code = data.getReferred_code();
                            String image_path =data.getImage_path();
                            String user_mode = data.getUser_mode();
                            Boolean applied_jockey = false;
                            String update_status = "Success";
                            session.createLoginSession(jockey_id, name,email,phone_no,referral_code,update_status,referred_code,image_path,user_mode,applied_jockey);
                           // session.createRegisterSession(name,email,phone_no,referral_code,referred_code);
                           submitBtn.setEnabled(false);
                        linear.setVisibility(View.GONE);
                        submitBtn.setVisibility(View.GONE);

//                           referral_codeEt.setText("");
//                        referral_codeEt.setBackgroundDrawable(ContextCompat.getDrawable(ReferralCodeActivity.this, R.color.gray));
//                           referral_codeEt.setEnabled(false);
                        //referral_codeEt.setTextColor( getResources().getColor(R.color.gray) );
                            insertPointsData(referred_code);


                    }else if (resource.getStatus().equals("Failure")){

                            showDialogMethod("Invalid",resource.getData().getError());

                    }
                }else{
                    showDialogMethod("Failed","Something went wrong");
                }

                pDialog.dismiss();
            }

            @Override
            public void onFailure(Call<UpdatereferalRequest> call, Throwable t) {
                pDialog.dismiss();
                showDialogMethod("Alert","Bad Network..");
            }
        });
    }
    private void insertPointsData(String referred_code) {
        pDialog.show();
        JSONObject json = new JSONObject();
        try {
            json.put("referred_code", referred_code);
            Log.e("jsonArray", String.valueOf(json));
        } catch (JSONException e) {
            Log.e("Exception ", e.toString());
        }

        // RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), String.valueOf(json));
        apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<InsertPointsRequest> call = apiInterface.InsertPointRequest(json.toString());

        call.enqueue(new Callback<InsertPointsRequest>() {
            @Override
            public void onResponse(Call<InsertPointsRequest> call, Response<InsertPointsRequest> response) {
//                send_btn.setEnabled(false);
                pDialog.dismiss();
                InsertPointsRequest resource = response.body();

                if (resource != null){
                if(resource.getStatus().equals("Success")){
                    InsertPointData data = resource.getData();
                    pDialog.dismiss();
                    if (data.getStatus().equals("Success")){
                        String status = data.getStatus();
                        Log.e("status",status);

                        showDialogMethod("Success", data.getStatus());
                    }else if(data.getStatus().equals("Failed")){
                        showDialogMethod("Warning", data.getStatus());
                    }

                }else if(resource.getStatus().equals("Failure")){
                    pDialog.dismiss();
                    InsertPointData data = resource.getData();
                    if (data.getStatus().equals("Success")){
                        String status = data.getStatus();
                        Log.e("status",status);
                        showDialogMethod("Success", data.getStatus());
                    }else if(data.getStatus().equals("Failed")){
                        showDialogMethod("Warning", data.getStatus());
                    }
                }

                }

            }

            @Override
            public void onFailure(Call<InsertPointsRequest> call, Throwable t) {
                pDialog.dismiss();
                showDialogMethod("Alert","Bad Network..");
            }
        });
    }
    private void showDialogMethod(String title, String message) {
        final AlertDialog alertDialog = new AlertDialog.Builder(ReferralCodeActivity.this).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // finish();
                        alertDialog.cancel();
                    }
                });
        alertDialog.show();
    }
    private void navigationMenu(){
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                switch (item.getItemId()) {
                    case R.id.home:
                        startActivity(new Intent(ReferralCodeActivity.this, Dashboard.class)
                                .setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
                        finish();
                        break;
                    case R.id.notifi:
                        Intent j = new Intent(ReferralCodeActivity.this,NotificationActivity.class);
                        // j.putExtra("login",flag);
                        startActivity(j);
                        break;
                    case R.id.search:
                        Intent i4 = new Intent(ReferralCodeActivity.this,SearchActivity.class);
                        // i4.putExtra("login",flag);
                        startActivity(i4);
                        break;
                    case R.id.profile:
                        Intent i = new Intent(ReferralCodeActivity.this,ProfileActivity.class);
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
}
