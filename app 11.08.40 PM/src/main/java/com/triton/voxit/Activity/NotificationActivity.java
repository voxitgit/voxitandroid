package com.triton.voxit.Activity;

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
import com.triton.voxit.Service.MediaPlayerService;
import com.triton.voxit.SessionManager.SessionManager;
import com.triton.voxit.Utlity.MediaPlayerSingleton;
import com.triton.voxit.model.AudioDetailData;
import com.triton.voxit.model.AudioListData;
import com.triton.voxit.model.Notification;
import com.triton.voxit.model.NotifyDataList;
import com.triton.voxit.model.NotifyResponseData;
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
    public static ArrayList<NotifyDataList> notifyList = new ArrayList<>();

    long startTime, endTime;

    MediaPlayerSingleton mps;
    private RelativeLayout miniPlayerLayout;
    private ImageView imgClose, imgSong;
    private TextView txtSongName, txtAuthorName, txtTypeName;
    ImageView imgMiniPlay;

    private String TAG = "NotificationActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        pDialog = new ProgressDialog(NotificationActivity.this);
        pDialog.setMessage(NotificationActivity.this.getString(R.string.please_wait));
        pDialog.setIndeterminate(true);
        pDialog.setCancelable(true);

        mps = MediaPlayerSingleton.getInstance(this);
        clearMediaPLayer();

        // notifyList = new ArrayList<>();

        session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        id = user.get(SessionManager.JOCKEY_ID);
        hideLanguageView();
        showTitleView();
        initViews();

        Log.i("Notify-->", "" + notifyList);

        if (notifyList.isEmpty()) {
            APIcall();
        } else {
            Log.i("Notify", "" + notifyList);
            notifiRecycler.setVisibility(View.VISIBLE);
            loadNotifyListAdapter();
        }

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                APITimercall();
                handler.postDelayed(this, 120000); //now is every 2 minutes
                Log.i("Timer", "Timer");
            }
        }, 120000);


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


    private void clearMediaPLayer() {
        mps.releasePlayer();
        mps.setType("empty");
        mps.setMediaPlayerStatus("empty");
        mps.setSubType("empty");
        mps.setAuthorName("empty");
    }

    private void clearMiniPLayer() {
        miniPlayerLayout.setVisibility(View.GONE);
        clearMediaPLayer();
        clearNotification();
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
                    imgMiniPlay.setImageDrawable(ContextCompat.getDrawable(NotificationActivity.this, R.drawable.ic_play));
                    buildNotification(MediaPlayerService.ACTION_PAUSE);


                } else {
                    mps.setMediaPlayerStatus("playing");
                    imgMiniPlay.setImageDrawable(ContextCompat.getDrawable(NotificationActivity.this, R.drawable.ic_pause));
                    mps.mp.start();
                    buildNotification(MediaPlayerService.ACTION_PLAY);
                }
            }
        });


        miniPlayerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mps.getCurrentPlayPos() != -1) {
                    final AudioDetailData audioData = notifyList.get(mps.getCurrentPlayPos()).getAudioDetail();

                    Log.w(TAG, "audio id " + audioData.getAudio_id() + " jockey id " + audioData.getJockey_id());
                    Intent intent = new Intent(NotificationActivity.this, AudioActivity.class);
                    intent.putExtra("jockey_id", audioData.getJockey_id() + "");
                    intent.putExtra("song", audioData.getAudio_path());
                    intent.putExtra("title", audioData.getTitle());
                    intent.putExtra("description", audioData.getDiscription());
                    intent.putExtra("image", audioData.getImage_path());
                    intent.putExtra("name", audioData.getName());
                    intent.putExtra("audio_length", audioData.getAudio_length());
                    intent.putExtra("audio_id", audioData.getAudio_id() + "");
                    intent.putExtra("type", "Notify");
                    intent.putExtra("songsList", notifyList);
                    startActivity(intent);
                }
            }
        });


//        Intent i = getIntent();
//        flag = i.getStringExtra("login");
//        if (flag.equals("L")){
//            startActivity(new Intent(this, LoginActivity.class)
//                    .putExtra("login","F").setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
//        }else{
//
//        }
        if (session.isLoggedIn()) {

        } else {
            startActivity(new Intent(this, LoginActivity.class)
                    .setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
        }
        navigationMenu();
    }

    private BroadcastReceiver myReceiver;


    private void registerReceiver() {
        myReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                Log.w(TAG, "broad cast receiver calling " + intent.getExtras().getString("status"));

                if (intent.getExtras().getString("status").equalsIgnoreCase("playing")) {
                    imgMiniPlay.setImageDrawable(ContextCompat.getDrawable(NotificationActivity.this, R.drawable.ic_pause));
                 /*   play.setImageResource(R.drawable.pause_blue);
                    mps.mp.start();
                    setStatus("playing");*/
//                    buildNotification(MediaPlayerService.ACTION_PLAY);


                } else if (intent.getExtras().getString("status").equalsIgnoreCase("pause")) {
                    /*play.setImageResource(R.drawable.play_blue);
                    mps.mp.pause();
                    setStatus("pause");*/
                    //   buildNotification(MediaPlayerService.ACTION_PAUSE);
                    imgMiniPlay.setImageDrawable(ContextCompat.getDrawable(NotificationActivity.this, R.drawable.ic_play));

                }
            }
        };

        registerReceiver(myReceiver, new IntentFilter("MP_STATUS"));
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

    private void APIrecent(String audioid, String jockeyid) {
        Integer id = Integer.valueOf(jockeyid);
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


    private int findCurrentPlayPos() {
        int pos = mps.getCurrentPlayPos();


        for (int i = 0; i <= notifyList.size() - 1; i++) {
            AudioDetailData audioData = notifyList.get(i).getAudioDetail();
            if (audioData.getAudio_path().equalsIgnoreCase(mps.getFileName())) {
                pos = i;
                break;
            }
        }

        return pos;
    }


    private void playNextSong() {
        Log.w(TAG, "play next song called");
        if (mps.getCurrentPlayPos() != -1) {

            int pos = findCurrentPlayPos();

            Log.w(TAG, "pos " + pos + mps.getCurrentPlayPos() + " list size " + notifyList.size());
            if ((pos + 1) < notifyList.size()) {

                try {

                    pos += 1;
                    final AudioDetailData audioData = notifyList.get(pos).getAudioDetail();

                    playSong("Notify", audioData.getAudio_path(), audioData.getName(),
                            audioData.getImage_path(), audioData.getTitle(), pos, "empty", audioData.getAudio_id() + "", audioData.getJockey_id() + "");

                } catch (IndexOutOfBoundsException e) {
                    e.printStackTrace();
                    clearMiniPLayer();
                }
            } else {
                clearMiniPLayer();

            }
        }
    }

    private void setCompleteListener() {
        if (mps.mp != null) {
            mps.mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    // imgMiniPlay.setImageResource(R.drawable.play_blue);

                    if (mps.mp != null) {
                        setStatus("completed");
                        imgMiniPlay.setImageResource(R.drawable.ic_play);

                        Log.w(TAG, "song completed");
                        // playMethod(songIndex + 1);

                        playNextSong();


                    }


                }
            });
        }
    }

    public void playSong(String type, String audioUrl, String authorName, String imageUrl, String title, int songIndex,
                         String subType, String audioid, String jockeyid) {
        Log.e("index ", songIndex + "");
        APIrecent(audioid, jockeyid);
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


            setCompleteListener();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (myReceiver != null) {
            unregisterReceiver(myReceiver);
        }

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
                Log.d("TAG", endTime - startTime + ":Millisecs");
                notifyResponseData = response.body();
                Log.e("response", String.valueOf(notifyResponseData));
                if (notifyResponseData != null) {
                    if (notifyResponseData.getStatus().equals("Success")) {
                        notifyList = notifyResponseData.getResponse();
                        Log.e("notifyList", "" + notifyList);
                        if (notifyList.size() == 0) {
                            notiView.setVisibility(View.VISIBLE);
                            notifiRecycler.setVisibility(View.GONE);
                        } else {
                            notiView.setVisibility(View.GONE);
                            notifiRecycler.setVisibility(View.VISIBLE);
                            loadNotifyListAdapter();
                        }

                    }

                }
            }

            @Override
            public void onFailure(Call<NotifyResponseData> call, Throwable t) {
                showDialogMethod("Alert", t.getMessage());
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
                Log.d("TAG", endTime - startTime + ":Millisecs");
                notifyResponseData = response.body();
                Log.e("response", String.valueOf(notifyResponseData));
                if (notifyResponseData != null) {
                    if (notifyResponseData.getStatus().equals("Success")) {
                        notifyList = notifyResponseData.getResponse();
                        Log.e("notifyList", "" + notifyList);
                        if (notifyList.size() == 0) {
                            notiView.setVisibility(View.VISIBLE);
                            notifiRecycler.setVisibility(View.GONE);
                        } else {
                            notiView.setVisibility(View.GONE);
                            notifiRecycler.setVisibility(View.VISIBLE);
                            loadNotifyListAdapter();
                        }

                    }

                }
            }

            @Override
            public void onFailure(Call<NotifyResponseData> call, Throwable t) {
                showDialogMethod("Alert", t.getMessage());
            }
        });
    }

    private void loadNotifyListAdapter() {
        LinearLayoutManager lLayout = new LinearLayoutManager(NotificationActivity.this);
        notifiRecycler.setLayoutManager(lLayout);
        notifyAdapter = new NotificationAdapter(this, notifyList, NotificationActivity.this);
        notifiRecycler.setAdapter(notifyAdapter);
    }

    private void navigationMenu() {
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
                        i4.putExtra("login", flag);
                        startActivity(i4);
                        break;
                    case R.id.profile:
                        Intent i = new Intent(NotificationActivity.this, ProfileActivity.class);
                        i.putExtra("login", flag);
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


            setCompleteListener();

        }


    }

    private void loadImageForMiniPlayer() {
        Glide.with(this).load(mps.getImageUrl()).into(imgSong);
    }
}
