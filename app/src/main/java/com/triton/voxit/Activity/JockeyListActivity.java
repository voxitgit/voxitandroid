package com.triton.voxit.Activity;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.triton.voxit.Adapter.JockeyListAdapter;
import com.triton.voxit.Api.APIClient;
import com.triton.voxit.Api.APIInterface;
import com.triton.voxit.Interface.JockeyListInterface;
import com.triton.voxit.R;
import com.triton.voxit.Service.MediaPlayerService;
import com.triton.voxit.SessionManager.SessionManager;
import com.triton.voxit.Utlity.MediaPlayerSingleton;
import com.triton.voxit.app.App;
import com.triton.voxit.model.AudioListData;
import com.triton.voxit.model.CheckFollowRequest;
import com.triton.voxit.model.FollowResponseData;
import com.triton.voxit.model.FollowResponseRequest;
import com.triton.voxit.model.JockeyDetailedData;
import com.triton.voxit.model.JockeyListRequest;
import com.triton.voxit.model.RecentlyPlayedResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class JockeyListActivity extends NavigationDrawer implements JockeyListInterface, View.OnClickListener {
    RecyclerView recyclerView;
    ImageView imageView;
    JockeyListInterface jockeyListInterface;
    private ProgressDialog pDialog;
    private APIInterface apiInterface;
    private JockeyListRequest jockeyListRequest;
    private ArrayList<JockeyDetailedData> jockeyDetailedData;
    private ArrayList<AudioListData> audioListData;
    private String user_jocky_id;
    int user_id;
    TextView nameTv, headertitle;
    Button follow_btn;
    BottomNavigationView bottomNavigationView;
    SessionManager session;
    String followStatus;
    private TextView follow_cnt;


    private BroadcastReceiver myReceiver;

    RelativeLayout miniPlayerLayout;
    ImageView imgSong, imgClose, imgMiniPlay;
    TextView txtSongName, txtAuthorName, txtTypeName;

    MediaPlayerSingleton mps;

    public boolean currentListClicked = false;

    private String TAG = "Jockey";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filmy_list_items);
        jockeyListInterface = (JockeyListInterface) this;
        jockeyListInterface.initVar();
        jockeyListInterface.getSessionData();
        jockeyListInterface.initToolbar();
        jockeyListInterface.showHeader();


        mps = MediaPlayerSingleton.getInstance(this);

        Log.w(TAG, "mps type " + mps.getType());


    }// end of oncreate


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (myReceiver != null) {
            unregisterReceiver(myReceiver);
        }


    }

    private void registerReceiver() {
        myReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                Log.w(TAG, "broad cast receiver calling " + intent.getExtras().getString("status"));

                if (intent.getExtras().getString("status").equalsIgnoreCase("playing")) {
                    imgMiniPlay.setImageDrawable(ContextCompat.getDrawable(JockeyListActivity.this, R.drawable.ic_pause));
                 /*   play.setImageResource(R.drawable.pause_blue);
                    mps.mp.start();
                    setStatus("playing");*/
//                    buildNotification(MediaPlayerService.ACTION_PLAY);


                } else if (intent.getExtras().getString("status").equalsIgnoreCase("pause")) {
                    /*play.setImageResource(R.drawable.play_blue);
                    mps.mp.pause();
                    setStatus("pause");*/
                    //   buildNotification(MediaPlayerService.ACTION_PAUSE);
                    imgMiniPlay.setImageDrawable(ContextCompat.getDrawable(JockeyListActivity.this, R.drawable.ic_play));

                }
            }
        };

        registerReceiver(myReceiver, new IntentFilter("MP_STATUS"));
    }


    @Override
    public void initVar() {
        pDialog = new ProgressDialog(JockeyListActivity.this);
        pDialog.setMessage(JockeyListActivity.this.getString(R.string.please_wait));
        pDialog.setIndeterminate(true);
        pDialog.setCancelable(true);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        imageView = (ImageView) findViewById(R.id.itemImage);
        nameTv = (TextView) findViewById(R.id.nameTv);

        follow_btn = (Button) findViewById(R.id.follow_btn);
        follow_cnt = (TextView) findViewById(R.id.followtext);
        follow_btn.setOnClickListener(this);
        Intent i = getIntent();
        user_jocky_id = i.getStringExtra("user_jocky_id");
        Log.e("user_jocky_id", user_jocky_id);
        if (!App.appUtils.isNetAvailable()) {
            alertUserP(JockeyListActivity.this, "Connection Error", "No Internet connection available", "OK");
        } else {
            APICall(user_jocky_id);
        }


        miniPlayerLayout = (RelativeLayout) findViewById(R.id.miniPlayerLayout);
        imgClose = (ImageView) findViewById(R.id.imgClose);
        imgMiniPlay = (ImageView) findViewById(R.id.imgMiniPlay);
        txtSongName = (TextView) findViewById(R.id.txtSongName);
        txtSongName.setSelected(true);
        txtTypeName = (TextView) findViewById(R.id.typeName);
        imgSong = (ImageView) findViewById(R.id.imgSong);
        txtAuthorName = (TextView) findViewById(R.id.txtAuthorName);

        registerReceiver();


        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mps != null) {
                    mps.releasePlayer();
                }
                miniPlayerLayout.setVisibility(View.GONE);

                clearNotification();
            }
        });

        imgMiniPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mps.mp.isPlaying()) {
                    mps.mp.pause();
                    mps.setMediaPlayerStatus("pause");
                    imgMiniPlay.setImageDrawable(ContextCompat.getDrawable(JockeyListActivity.this, R.drawable.ic_play));
                    buildNotification(MediaPlayerService.ACTION_PAUSE);


                } else {
                    mps.setMediaPlayerStatus("playing");
                    imgMiniPlay.setImageDrawable(ContextCompat.getDrawable(JockeyListActivity.this, R.drawable.ic_pause));
                    mps.mp.start();
                    buildNotification(MediaPlayerService.ACTION_PLAY);
                }
            }
        });

        miniPlayerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Log.w(TAG, "current list clicked " + currentListClicked);

                if (!currentListClicked) {
                    JockeyListActivity.super.onBackPressed();
                } else {

                    if (mps.getCurrentPlayPos() != -1) {

                        Log.w(TAG, "audio list " + audioListData.size());

                        if (audioListData.size() > 0) {
                            AudioListData model = audioListData.get(mps.getCurrentPlayPos());

                            Log.w(TAG, "model " + model.getAudio_id());

                            startActivity(new Intent(JockeyListActivity.this, AudioActivity.class)
                                    .putExtra("jockey_id", String.valueOf(model.getJockey_id()))
                                    .putExtra("song", model.getAudio_path())
                                    .putExtra("title", model.getTitle())
                                    .putExtra("description", model.getDiscription())
                                    .putExtra("image", model.getImage_path())
                                    .putExtra("name", model.getName())
                                    .putExtra("audio_length", model.getAudio_length())
                                    .putExtra("audio_id", model.getAudio_id()+"")
                                    .putExtra("type", "JockeyList")
                                    .putExtra("songsList", audioListData)
                                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK));


                            JockeyListActivity.this.finish();
                        }
                    }


                }
            }
        });


    }

    private void setFileName(String fileName) {
        mps.setFileName(fileName);
    }

    private void setCurrentPlayPos(int pos) {

        mps.setCurrentPlayPos(pos);
    }

    private void setImageUrl(String url) {
        mps.setImageUrl(url);
    }

    private void setAuthorName(String name) {
        mps.setAuthorName(name);
    }

    private void setType(String type) {

        mps.setType(type);
    }

    private void setStatus(String status) {

        mps.setMediaPlayerStatus(status);
    }


    private RecentlyPlayedResponse recentlyPlayedResponse;

    private void APIrecent(String audioid) {
        Integer id = Integer.valueOf(user_jocky_id);
        Integer au_id = Integer.valueOf(audioid);
        JSONObject json = new JSONObject();
        try {
            json.put("jockey_id", id);
            json.put("audio_id", au_id);
            Log.e("testtttt", String.valueOf(json));
        } catch (JSONException e) {
            Log.e("Exception ", e.toString());
        }

        RequestBody body = RequestBody.create(MediaType.parse("application/json"), String.valueOf(json));

        //Creating an object of our api interface
        apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<RecentlyPlayedResponse> call = apiInterface.RecentlyPlayedRequestTask(body);

        call.enqueue(new Callback<RecentlyPlayedResponse>() {
            @Override
            public void onResponse(Call<RecentlyPlayedResponse> call, Response<RecentlyPlayedResponse> response) {

                recentlyPlayedResponse = response.body();
                if (recentlyPlayedResponse != null) {
                    Log.e("audio_recent", recentlyPlayedResponse.getStatus());
                }
            }

            @Override
            public void onFailure(Call<RecentlyPlayedResponse> call, Throwable t) {

            }
        });
    }


    public void playSong(String type, String audioUrl, String authorName, String imageUrl, String title, int songIndex,
                         String subType, String audioid) {
        Log.w(TAG, audioid + " pos " + songIndex);
        APIrecent(audioid);
        try {
            setType(type);
            setFileName(title);
            setAuthorName(authorName);
            setImageUrl(imageUrl);
            setCurrentPlayPos(songIndex);
            mps.setSubType(subType);
            // if (!isPlaying) {
            //  mps.releasePlayer();

            if (mps.mp != null) {
                mps.mp.pause();
//                mps.mp.reset();
                //   mps.mp.release();

                mps.mp = null;
            }
            // player = new MediaPlayer();
            mps.initializePlayer();
            mps.mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mps.mp.setDataSource(audioUrl);

            //desTv.setText("By"+" "+audiosongsList.get(songIndex).getName());

            mps.mp.prepare();
            mps.mp.start();

            if (!miniPlayerLayout.isShown()) {
                // miniPlayerLayout.setEnabled(false);
                miniPlayerLayout.setVisibility(View.VISIBLE);
            }

            txtSongName.setText(title);
            txtAuthorName.setText(authorName);
            txtTypeName.setText(type);
            Glide.with(this).load(imageUrl).into(imgSong);

            buildNotification(MediaPlayerService.ACTION_PLAY);

            setStatus("playing");

            int duration = mps.mp.getDuration();
            duration = duration / 1000;

            Log.w(TAG, "duration " + duration);

            //seekBar.setMax(duration);

      /*      this.runOnUiThread(updateRunnable = new Runnable() {
                @Override
                public void run() {

                    if (mps.mp != null) {
                        seekbarPosition = mps.mp.getCurrentPosition() / 1000;
                        seekBar.setProgress(seekbarPosition);

                        // Displaying time completed playing
                        end_time.setText("" + utils.milliSecondsToTimer(mps.mp.getDuration()));
//                            int percentage = (100 * player.getCurrentPosition()) / songDuration;
                        start_time.setText("" + utils.milliSecondsToTimer(mps.mp.getCurrentPosition()));

                    }
                    mHandler.postDelayed(this, 1000);
                }
            });
*/
            mps.mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    Log.w(TAG, "TAG:prepared " + mp.getDuration());
                }
            });

            mps.mp.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
                @Override
                public void onBufferingUpdate(MediaPlayer mp, int percent) {
                    Log.d(TAG, "TAG:buffering " + mp.getDuration() + " percent " + percent);

                    if (mps.mp != null) {

//                            seekbarPosition = player.getCurrentPosition() / 1000;
//                            seekBar.setProgress(seekbarPosition);
//
//                            // Displaying time completed playing
//                            end_time.setText(""+utils.milliSecondsToTimer(player.getDuration()));
////                            int percentage = (100 * player.getCurrentPosition()) / songDuration;
//                            start_time.setText(""+utils.milliSecondsToTimer(player.getCurrentPosition()));

                    }
                }
            });

            //Attempt to seek to past end of file: request = 259000, durationMs = 0
            mps.mp.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                @Override
                public boolean onInfo(MediaPlayer mp, int what, int extra) {
                    switch (what) {
                        case MediaPlayer.MEDIA_INFO_BUFFERING_START:
                            break;
                        case MediaPlayer.MEDIA_INFO_BUFFERING_END:

                            int duration = mps.mp.getDuration();

                            duration = duration / 1000;
                            //seekBar.setMax(duration);
                            break;
                    }
                    return false;
                }
            });
//                Log.w("Duartion ", duration + "");

            mps.mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    // imgMiniPlay.setImageResource(R.drawable.play_blue);

                    if (mps.mp != null) {
                        setStatus("completed");
                        imgMiniPlay.setImageResource(R.drawable.pause_blue);
                        // playMethod(songIndex + 1);

                        //  playNextSong();


                    }


                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void buildNotification(String action) {
        Intent intent = new Intent(getApplicationContext(), MediaPlayerService.class);
        intent.setAction(action);
        startService(intent);
    }

    private void clearNotification() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(1);
    }

    @Override
    public void initToolbar() {
        follow_btn.setEnabled(true);
        headertitle = (TextView) findViewById(R.id.header_title);
        headertitle.setText("Jockey");
        bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottom_navigation);
        bottomNavigationView.getMenu().findItem(R.id.home).setChecked(true);
        navigationMenu();
    }

    @Override
    public void getSessionData() {
        session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        // referred_code = user.get(SessionManager.KEY_REFERRED_CODE);
        user_id = Integer.parseInt(user.get(SessionManager.JOCKEY_ID));
        Log.e("jockey_id", String.valueOf(user_id));
        checkFollowApi();
        if (session.isLoggedIn()) {

        } else {
            startActivity(new Intent(this, LoginActivity.class)
                    .setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
        }
    }

    @Override
    public void showHeader() {
        hideLanguageView();
        showTitleView();
    }

    private void navigationMenu() {
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                switch (item.getItemId()) {
                    case R.id.home:
                        startActivity(new Intent(JockeyListActivity.this, Dashboard.class)
                                .setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
                        finish();
                        break;
                    case R.id.notifi:
                        Intent j = new Intent(JockeyListActivity.this, NotificationActivity.class);
                        // j.putExtra("login",flag);
                        startActivity(j);
                        break;
                    case R.id.search:
                        Intent i4 = new Intent(JockeyListActivity.this, SearchActivity.class);
                        // i4.putExtra("login",flag);
                        startActivity(i4);
                        break;
                    case R.id.profile:
                        Intent i = new Intent(JockeyListActivity.this, ProfileActivity.class);
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

    public void APICall(String user_jocky_id) {
        // show it
        pDialog.show();
        int u_jocky_id = Integer.parseInt(user_jocky_id);

        JSONObject json = new JSONObject();
        try {
            json.put("user_jocky_id", u_jocky_id);
            Log.e("jsonArray", json.toString());
        } catch (JSONException e) {
            Log.e("Exception ", e.toString());
        }

        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), String.valueOf(json));
        //Creating an object of our api interface
        apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<JockeyListRequest> call = apiInterface.getJockeyListData(body);
        Log.e("url", call.request().url().toString());

        call.enqueue(new Callback<JockeyListRequest>() {
            @Override
            public void onResponse(Call<JockeyListRequest> call, Response<JockeyListRequest> response) {
                pDialog.dismiss();
                jockeyListRequest = response.body();
                if (jockeyListRequest != null) {
                    jockeyDetailedData = jockeyListRequest.getJockey_details();
                    audioListData = jockeyListRequest.getAudiolist();

                    // MediaPlayerSingleton mps = MediaPlayerSingleton.getInstance(JockeyListActivity.this);
                    mps.setJockeyList(audioListData);

                    Log.e("jockeyDetailedData", String.valueOf(jockeyDetailedData));
                    Log.e("jockeyListRequest", String.valueOf(jockeyListRequest));
                    Log.e("audioListData", String.valueOf(audioListData));

                    if (jockeyListRequest.getJockey_details().equals("null") || jockeyListRequest.getJockey_details() == null) {

                    } else {
                        String name = jockeyDetailedData.get(0).getDisplay_name();
                        Log.e("name", name);
                        String image_path = jockeyDetailedData.get(0).getImage_path();
                        displayJockeyData(name, image_path);

                        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 1, LinearLayoutManager.VERTICAL, false);
                        recyclerView.setLayoutManager(gridLayoutManager);
                        //add space item decoration and pass space you want to give
                        recyclerView.addItemDecoration(new EqualSpacingItemDecoration(0));
                        //finally set adapter
                        recyclerView.setAdapter(new JockeyListAdapter(JockeyListActivity.this, audioListData, JockeyListActivity.this));

                        recyclerView.setNestedScrollingEnabled(false);

                    }

                }
            }

            @Override
            public void onFailure(Call<JockeyListRequest> call, Throwable t) {
                pDialog.dismiss();
                Log.e("Error", t.getMessage());
            }
        });

    }

    private void displayJockeyData(String name, String image_path) {
        nameTv.setText(name);
        Glide.with(this).load(image_path).into(imageView);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.follow_btn:
                insertFollowApi();
                break;
        }
    }

    private void checkFollowApi() {
        // pDialog.show();
        JSONObject json = new JSONObject();
        int f_id = Integer.parseInt(user_jocky_id);
        try {
            json.put("follower_id", f_id);
            json.put("user_jocky_id", user_id);

            Log.e("check jsonArray", String.valueOf(json));
        } catch (JSONException e) {
            Log.e("Exception ", e.toString());
        }
        // RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), String.valueOf(json));
        apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<CheckFollowRequest> call = apiInterface.CheckFollowResponseRequest(json.toString());

        call.enqueue(new Callback<CheckFollowRequest>() {
            @Override
            public void onResponse(Call<CheckFollowRequest> call, Response<CheckFollowRequest> response) {
                follow_btn.setEnabled(true);
                // pDialog.dismiss();
                CheckFollowRequest resource = response.body();

                if (resource != null) {
                    if (resource.getCount() == null) {
                        follow_cnt.setText("  100+");
                    } else {
                        follow_cnt.setText("  " + resource.getCount());
                    }

                    Log.e("status", resource.getData());
                    if (resource.getData().equals("unfollow")) {
                        follow_btn.setText("Follow");
                        follow_btn.setBackgroundDrawable(ContextCompat.getDrawable(JockeyListActivity.this, R.drawable.rounded_rect));
                    } else {
                        follow_btn.setText("Following");
                        follow_btn.setBackgroundDrawable(ContextCompat.getDrawable(JockeyListActivity.this, R.drawable.round_dark_gray));
                    }

                    if (resource.getStatus().equals("Success")) {


//                    FollowResponseData data = resource.getData();
//                    followStatus = data.getStatus();


                        // showDialogMethod("Success", data.getStatus());
//                    Log.e("status",data.getStatus());

                    } else if (resource.getStatus().equals("Failure")) {
                        // pDialog.dismiss();
//                    FollowResponseData data = resource.getData();
                        // showDialogMethod("Warning", data.getStatus());

                    }

                }

            }

            @Override
            public void onFailure(Call<CheckFollowRequest> call, Throwable t) {
                // pDialog.dismiss();
                // showDialogMethod("Alert","Bad Network..");
            }
        });
    }

    private void insertFollowApi() {
        pDialog.show();
        JSONObject json = new JSONObject();
        int f_id = Integer.parseInt(user_jocky_id);
        try {
            json.put("follower_id", f_id);
            json.put("user_jocky_id", user_id);

            Log.e("jsonArray", String.valueOf(json));
        } catch (JSONException e) {
            Log.e("Exception ", e.toString());
        }
        // RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), String.valueOf(json));
        apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<FollowResponseRequest> call = apiInterface.FollowResponseRequest(json.toString());

        call.enqueue(new Callback<FollowResponseRequest>() {
            @Override
            public void onResponse(Call<FollowResponseRequest> call, Response<FollowResponseRequest> response) {
                follow_btn.setEnabled(true);
                pDialog.dismiss();
                FollowResponseRequest resource = response.body();

                if (resource != null) {
                    if (resource.getStatus().equals("Success")) {
//                    if(follow_btn.getText().toString().equals("Follow")){
//                        follow_btn.setText("Unfollow");
//                    }else {
//                        follow_btn.setText("Follow");
//                    }

//                    follow_btn.setEnabled(false);

                        FollowResponseData data = resource.getData();
                        followStatus = data.getStatus();

                        checkFollowApi();
//                    if (data.getStatus().equals("Success")){
//                        String status = data.getStatus();
//                        Log.e("status",status);
//                    if(resource.getData().equals("unfollow")){
//                        follow_btn.setText("Follow");
//                    }else {
//                        follow_btn.setText("Unfollow");
//                    }
                        //showDialogMethod("Success", data.getStatus());
                        Log.e("status1", data.getStatus());
//                    }else if(data.getStatus().equals("Failed")){
//                        showDialogMethod("Warning", data.getStatus());
//                    }

                    } else if (resource.getStatus().equals("Failure")) {
                        pDialog.dismiss();
                        FollowResponseData data = resource.getData();
                        //showDialogMethod("Warning", data.getStatus());

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
            public void onFailure(Call<FollowResponseRequest> call, Throwable t) {
                pDialog.dismiss();
                showDialogMethod("Alert", "Bad Network..");
            }
        });
    }

    private void showDialogMethod(String title, String message) {
        final AlertDialog alertDialog = new AlertDialog.Builder(JockeyListActivity.this).create();
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


    private void loadImageForMiniPlayer() {
        Glide.with(this).load(mps.getImageUrl()).into(imgSong);
    }


    @Override
    protected void onResume() {
        super.onResume();

        if (mps.mp != null) {
            Log.w("Dashboard", "media player not null " + mps.mp.isPlaying() + " is pause ");
            if (mps.mp.isPlaying()) {
                // miniPlayerLayout.setEnabled(false);
                miniPlayerLayout.setVisibility(View.VISIBLE);
                txtSongName.setText(mps.getFileName());
                txtAuthorName.setText(mps.getAuthorName());
                txtTypeName.setText(mps.getType());
                imgMiniPlay.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_pause));
                loadImageForMiniPlayer();
            } else {
                if (mps.getMediaPlayerStatus().equalsIgnoreCase("pause")) {
                    //  miniPlayerLayout.setEnabled(false);
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


//        @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        startActivity(new Intent(JockeyListActivity.this, Dashboard.class)
//                .setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
//        finish();
//    }
}
