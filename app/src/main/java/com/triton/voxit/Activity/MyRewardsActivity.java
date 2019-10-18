package com.triton.voxit.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.triton.voxit.Adapter.SelectGenreAdapter;
import com.triton.voxit.Api.APIClient;
import com.triton.voxit.Api.APIInterface;
import com.triton.voxit.Interface.MyRewardsInterface;
import com.triton.voxit.R;
import com.triton.voxit.SessionManager.SessionManager;
import com.triton.voxit.model.DisplayPointsRequest;
import com.triton.voxit.model.GenreData;
import com.triton.voxit.model.GenreResponseData;
import com.triton.voxit.model.InsertPointData;
import com.triton.voxit.model.InsertPointsRequest;
import com.triton.voxit.model.OTPRequest;
import com.triton.voxit.model.OTPResult;
import com.triton.voxit.model.RedeemPointsData;
import com.triton.voxit.model.RedeemPointsRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyRewardsActivity extends NavigationDrawer implements View.OnClickListener,MyRewardsInterface {
    String TAG = "MyRewardsActivity";
    MyRewardsInterface myRewardsInterface;
    ImageView pointsImg;
    TextView total_ref,points_noTv,points,minimum_points,headertitle;
    Button redeemBtn;
    BottomNavigationView bottomNavigationView;
    private ProgressDialog pDialog;
    private APIInterface apiInterface;
    SessionManager session;
    String referred_code;
    private DisplayPointsRequest displayPointsRequest;
    private ArrayList<DisplayPointsData> displayPointsData;
    int jockey_id;
    int AvailablePoints;
    boolean RedeemButtonState;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myrewards_activity);
        myRewardsInterface = (MyRewardsInterface)this;
        myRewardsInterface.initToolbar();
        myRewardsInterface.initVar();
        myRewardsInterface.showHeader();

    }//end of oncreate

    @Override
    public void initVar() {
        pointsImg = (ImageView) findViewById(R.id.pointsImg);
        total_ref = (TextView) findViewById(R.id.total_ref);
        points_noTv = (TextView) findViewById(R.id.points_no);
        points = (TextView) findViewById(R.id.points);
        minimum_points = (TextView) findViewById(R.id.minimum_points);
        redeemBtn = (Button) findViewById(R.id.redeemBtn);
        myRewardsInterface.getSessionData();
        redeemBtn.setEnabled(true);
        displayPoints();
        redeemBtn.setOnClickListener(this);

        if(RedeemButtonState){
            redeemBtn.setVisibility(View.VISIBLE);

        }else if(!RedeemButtonState) {
            redeemBtn.setVisibility(View.GONE);
        }
    }

    private void displayPoints() {
        // show it
        pDialog.show();

        JSONObject json = new JSONObject();
        try {
            json.put("jockey_id", jockey_id);
        } catch (JSONException e) {
            Log.e("Exception ", e.toString());
        }

        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), String.valueOf(json));
        //Creating an object of our api interface
        apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<DisplayPointsRequest> call = apiInterface.getDisplayPointsData(body);
        Log.e("url",call.request().url().toString());

        call.enqueue(new Callback<DisplayPointsRequest>() {
            @Override
            public void onResponse(Call<DisplayPointsRequest> call, Response<DisplayPointsRequest> response) {
                pDialog.dismiss();
                displayPointsRequest = response.body();
                if (displayPointsRequest != null){
                 displayPointsData = displayPointsRequest.getData();

                if(displayPointsRequest.getStatus().equals("Failure")) {
                    //showDialogMethod("Alert", displayPointsRequest.getStatus());
                } else if(displayPointsRequest.getStatus().equals("Success")){

                    AvailablePoints = displayPointsData.get(0).getAvailablePoints();
                    RedeemButtonState = displayPointsData.get(0).isRedeemButtonState();
                    Log.e("AvailablePoints", String.valueOf(AvailablePoints)+RedeemButtonState);
                    points_noTv.setText(String.valueOf(AvailablePoints));
                    if(RedeemButtonState){
                        redeemBtn.setVisibility(View.VISIBLE);

                    }else if(!RedeemButtonState) {
                        redeemBtn.setVisibility(View.GONE);
                    }

                   // showDialogMethod("Alert", displayPointsRequest.getStatus());
                }

                }
            }

            @Override
            public void onFailure(Call<DisplayPointsRequest> call, Throwable t) {
                pDialog.dismiss();
                Log.e("Error",t.getMessage());
            }
        });
    }


    private void showDialogMethod(String title, String message) {
        final AlertDialog alertDialog = new AlertDialog.Builder(MyRewardsActivity.this).create();
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

    @Override
    public void initToolbar() {
        pDialog = new ProgressDialog(MyRewardsActivity.this);
        pDialog.setMessage(MyRewardsActivity.this.getString(R.string.please_wait));
        pDialog.setIndeterminate(true);
        pDialog.setCancelable(true);
        headertitle = (TextView) findViewById(R.id.header_title);
        headertitle.setText("My Rewards");
        bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottom_navigation);
        bottomNavigationView.getMenu().findItem(R.id.profile).setChecked(true);
        navigationMenu();
    }

    @Override
    public void getSessionData() {
        session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        referred_code = user.get(SessionManager.KEY_REFERRED_CODE);
        jockey_id = Integer.parseInt(user.get(SessionManager.JOCKEY_ID));
        Log.e("jockey_id", String.valueOf(jockey_id));
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
            case R.id.redeemBtn:

                getRedeemData();
                break;

        }
    }
    private void getRedeemData(){
        pDialog.show();
        JSONObject json = new JSONObject();
        try {
            json.put("jockey_id", jockey_id);
            json.put("points", AvailablePoints);

            Log.e("jsonArray", String.valueOf(json));
        } catch (JSONException e) {
            Log.e("Exception ", e.toString());
        }
        // RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), String.valueOf(json));
        apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<RedeemPointsRequest> call = apiInterface.RedeemPointRequest(json.toString());

        call.enqueue(new Callback<RedeemPointsRequest>() {
            @Override
            public void onResponse(Call<RedeemPointsRequest> call, Response<RedeemPointsRequest> response) {
                redeemBtn.setEnabled(true);
                pDialog.dismiss();
                RedeemPointsRequest resource = response.body();

                if (resource != null){
                if(resource.getStatus().equals("Success")){
                    redeemBtn.setEnabled(false);
                    RedeemPointsData data = resource.getData();
                    displayPoints();
//                    if (data.getStatus().equals("Success")){
//                        String status = data.getStatus();
//                        Log.e("status",status);

                        showDialogMethod("Success", data.getStatus());
//                    }else if(data.getStatus().equals("Failed")){
//                        showDialogMethod("Warning", data.getStatus());
//                    }

                }else if(resource.getStatus().equals("Failure")){
                    pDialog.dismiss();
                    RedeemPointsData data = resource.getData();
                    showDialogMethod("Warning", data.getError());

//                    if (data.getStatus().equals("Success")){
//                        String status = data.getStatus();
//                        Log.e("status",status);
//                        showDialogMethod("Success", data.getStatus());
//                    }else if(data.getStatus().equals("Failure")){
//                        showDialogMethod("Warning", data.getError());
//                    }
                }

                }

            }

            @Override
            public void onFailure(Call<RedeemPointsRequest> call, Throwable t) {
                pDialog.dismiss();
                showDialogMethod("Alert","Bad Network..");
            }
        });
    }
    private void navigationMenu(){
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                switch (item.getItemId()) {
                    case R.id.home:
                        startActivity(new Intent(MyRewardsActivity.this, Dashboard.class)
                                .setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
                        finish();
                        break;
                    case R.id.notifi:
                        Intent j = new Intent(MyRewardsActivity.this,NotificationActivity.class);
                        // j.putExtra("login",flag);
                        startActivity(j);
                        break;
                    case R.id.search:
                        Intent i4 = new Intent(MyRewardsActivity.this,SearchActivity.class);
                        // i4.putExtra("login",flag);
                        startActivity(i4);
                        break;
                    case R.id.profile:
                        Intent i = new Intent(MyRewardsActivity.this,ProfileActivity.class);
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
