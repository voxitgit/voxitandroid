package com.triton.voxit.Activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.triton.voxit.Adapter.NotificationAdapter;
import com.triton.voxit.Api.APIClient;
import com.triton.voxit.Api.APIInterface;
import com.triton.voxit.R;
import com.triton.voxit.SessionManager.SessionManager;
import com.triton.voxit.Utlity.MediaPlayerSingleton;
import com.triton.voxit.model.NotifyDataList;
import com.triton.voxit.model.NotifyResponseData;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationActivity extends NavigationDrawer {

    private TextView headertitle;
    private BottomNavigationView bottomNavigationView;
    String flag;
    SessionManager session;
    private LinearLayout notiView;
    private RecyclerView notifiRecycler;
    private NotificationAdapter notifyAdapter;
    private APIInterface apiInterface;
    private ProgressDialog pDialog;
    private String id;
    private NotifyResponseData notifyResponseData;
    public static ArrayList<NotifyDataList> notifyList=new ArrayList<>();

    long startTime,endTime;

    MediaPlayerSingleton mps;
    private RelativeLayout miniPlayerLayout;
    private ImageView imgClose, imgSong;
    private TextView txtSongName, txtAuthorName, txtTypeName;
    ImageView imgMiniPlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        pDialog = new ProgressDialog(NotificationActivity.this);
        pDialog.setMessage(NotificationActivity.this.getString(R.string.please_wait));
        pDialog.setIndeterminate(true);
        pDialog.setCancelable(true);

        mps = MediaPlayerSingleton.getInstance(this);

       // notifyList = new ArrayList<>();

        session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        id = user.get(SessionManager.JOCKEY_ID);
        hideLanguageView();
        showTitleView();
        initViews();

        Log.i("Notify-->",""+notifyList);

        if (notifyList.isEmpty()){
            APIcall();
        }else{
            Log.i("Notify",""+notifyList);
            notifiRecycler.setVisibility(View.VISIBLE);
            loadNotifyListAdapter();
        }

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                APITimercall();
                handler.postDelayed(this, 120000); //now is every 2 minutes
                Log.i("Timer","Timer");
            }
        }, 120000);




    }

    private void initViews() {
        headertitle = (TextView) findViewById(R.id.header_title);
        headertitle.setText("Notifications");
        bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottom_navigation);
        bottomNavigationView.getMenu().findItem(R.id.notifi).setChecked(true);

        notiView = (LinearLayout) findViewById(R.id.text_noti);
        notifiRecycler = (RecyclerView) findViewById(R.id.notifi_item);


        miniPlayerLayout = (RelativeLayout) findViewById(R.id.miniPlayerLayout);
        imgClose = (ImageView) findViewById(R.id.imgClose);
        imgMiniPlay = (ImageView) findViewById(R.id.imgMiniPlay);
        txtSongName = (TextView) findViewById(R.id.txtSongName);
        txtSongName.setSelected(true);
        txtTypeName = (TextView) findViewById(R.id.typeName);
        imgSong = (ImageView) findViewById(R.id.imgSong);
        txtAuthorName = (TextView) findViewById(R.id.txtAuthorName);

//        Intent i = getIntent();
//        flag = i.getStringExtra("login");
//        if (flag.equals("L")){
//            startActivity(new Intent(this, LoginActivity.class)
//                    .putExtra("login","F").setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
//        }else{
//
//        }
        if(session.isLoggedIn()){

        }else {
            startActivity(new Intent(this, LoginActivity.class)
                   .setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
        }
        navigationMenu();
    }



    private void APIcall() {

        startTime = System.currentTimeMillis();
        pDialog.show();
        Integer jockeyid = Integer.valueOf(id);
        Log.e("jockey", jockeyid.toString());
        JSONObject json = new JSONObject();
        try {
            json.put("jockey_id", jockeyid);
        } catch (JSONException e) {
            Log.e("Exception ", e.toString());
        }
        Log.e("jockey_json", String.valueOf(json));
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), String.valueOf(json));
        //Creating an object of our api interface
        apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<NotifyResponseData> call = apiInterface.getnotifyData(body);

        call.enqueue(new Callback<NotifyResponseData>() {
            @Override
            public void onResponse(Call<NotifyResponseData> call, Response<NotifyResponseData> response) {
                pDialog.dismiss();
                endTime = System.currentTimeMillis();
                Log.d("TAG",endTime-startTime + ":Millisecs");
                notifyResponseData = response.body();
                Log.e("response",String.valueOf(notifyResponseData));
                if (notifyResponseData != null){
                    if (notifyResponseData.getStatus().equals("Success")){
                        notifyList = notifyResponseData.getResponse();
                        Log.e("notifyList",""+notifyList);
                        if (notifyList.size() == 0){
                            notiView.setVisibility(View.VISIBLE);
                            notifiRecycler.setVisibility(View.GONE);
                        }else {
                            notiView.setVisibility(View.GONE);
                            notifiRecycler.setVisibility(View.VISIBLE);
                            loadNotifyListAdapter();
                        }

                    }

                }
            }

            @Override
            public void onFailure(Call<NotifyResponseData> call, Throwable t) {
                showDialogMethod("Alert",t.getMessage());
            }
        });
    }

    private void APITimercall() {
        notifyList.clear();
        Integer jockeyid = Integer.valueOf(id);
        Log.e("jockey", jockeyid.toString());
        JSONObject json = new JSONObject();
        try {
            json.put("jockey_id", jockeyid);
        } catch (JSONException e) {
            Log.e("Exception ", e.toString());
        }
        Log.e("jockey_json", String.valueOf(json));
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), String.valueOf(json));
        //Creating an object of our api interface
        apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<NotifyResponseData> call = apiInterface.getnotifyData(body);

        call.enqueue(new Callback<NotifyResponseData>() {
            @Override
            public void onResponse(Call<NotifyResponseData> call, Response<NotifyResponseData> response) {
                Log.d("TAG",endTime-startTime + ":Millisecs");
                notifyResponseData = response.body();
                Log.e("response",String.valueOf(notifyResponseData));
                if (notifyResponseData != null){
                    if (notifyResponseData.getStatus().equals("Success")){
                        notifyList = notifyResponseData.getResponse();
                        Log.e("notifyList",""+notifyList);
                        if (notifyList.size() == 0){
                            notiView.setVisibility(View.VISIBLE);
                            notifiRecycler.setVisibility(View.GONE);
                        }else {
                            notiView.setVisibility(View.GONE);
                            notifiRecycler.setVisibility(View.VISIBLE);
                            loadNotifyListAdapter();
                        }

                    }

                }
            }

            @Override
            public void onFailure(Call<NotifyResponseData> call, Throwable t) {
                showDialogMethod("Alert",t.getMessage());
            }
        });
    }

    private void loadNotifyListAdapter() {
        LinearLayoutManager lLayout = new LinearLayoutManager(NotificationActivity.this);
        notifiRecycler.setLayoutManager(lLayout);
        notifyAdapter = new NotificationAdapter(this, notifyList);
        notifiRecycler.setAdapter(notifyAdapter);
    }

    private void navigationMenu(){
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                switch (item.getItemId()) {
                    case R.id.notifi:
                        Toast.makeText(NotificationActivity.this, "Already you are in Notification", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.home:
                        startActivity(new Intent(NotificationActivity.this, Dashboard.class)
                                .setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
                        finish();
                        break;

                    case R.id.search:
                        Intent i4 = new Intent(NotificationActivity.this, SearchActivity.class);
                        i4.putExtra("login",flag);
                        startActivity(i4);
                        break;
                    case R.id.profile:
                        Intent i = new Intent(NotificationActivity.this, ProfileActivity.class);
                        i.putExtra("login",flag);
                        startActivity(i);
                        break;

                }
                return true;
            }
        });
    }

    private void showDialogMethod(String title, String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(NotificationActivity.this).create();
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

    @Override
    protected void onResume() {
        super.onResume();
        bottomNavigationView.getMenu().getItem(2).setChecked(true);

        if (mps.mp != null) {
            Log.w("Dashboard", "media player not null " + mps.mp.isPlaying() + " is pause ");
            if (mps.mp.isPlaying()) {
                miniPlayerLayout.setVisibility(View.VISIBLE);
                txtSongName.setText(mps.getFileName());
                txtAuthorName.setText(mps.getAuthorName());
                txtTypeName.setText(mps.getType());
                imgMiniPlay.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_pause));
                loadImageForMiniPlayer();
            } else {
                if (mps.getMediaPlayerStatus().equalsIgnoreCase("pause")) {
                    miniPlayerLayout.setVisibility(View.VISIBLE);
                    txtSongName.setText(mps.getFileName());
                    txtAuthorName.setText(mps.getAuthorName());
                    txtTypeName.setText(mps.getType());
                    imgMiniPlay.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_play));
                    loadImageForMiniPlayer();
                }

            }

        }




    }

    private void loadImageForMiniPlayer() {
        Glide.with(this).load(mps.getImageUrl()).into(imgSong);
    }
}
