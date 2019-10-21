package com.triton.voxit.Activity;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hsalf.smilerating.SmileRating;
import com.triton.voxit.Api.APIClient;
import com.triton.voxit.Api.APIInterface;
import com.triton.voxit.R;
import com.triton.voxit.Service.MediaPlayerService;
import com.triton.voxit.SessionManager.SessionManager;
import com.triton.voxit.Utlity.DetectSwipeGestureListener;
import com.triton.voxit.Utlity.MediaPlayerSingleton;
import com.triton.voxit.app.App;
import com.triton.voxit.helpers.Utilities;
import com.triton.voxit.model.AudioDetailsResponseBean;
import com.triton.voxit.model.AudioListData;
import com.triton.voxit.model.AudioLogResponse;
import com.triton.voxit.model.NotifyDataList;
import com.triton.voxit.model.RecentlyPlayedResponse;
import com.triton.voxit.model.RecyclerViewItem;
import com.triton.voxit.model.SearchData;
import com.triton.voxit.model.TrendingResponseBean;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AudioActivity extends NavigationDrawer implements View.OnClickListener, SmileRating.OnSmileySelectionListener,
        SmileRating.OnRatingSelectedListener {
    private static final String TAG = "AudioActivity";
    public static boolean toRefresh;
    public static RecyclerViewItem filmy_list = new RecyclerViewItem();
    public static TrendingResponseBean songsList = new TrendingResponseBean();
    ImageView overflow, itemImage;
    RatingBar ratingbar;
    TextView rate, rate_this_audio_tv, nameTv, desTv;
    private SmileRating mSmileRating;
    private Button sexualTv, casteTv, childAbuseTv, violenceTv;
    private Button OkBtn, send_btn;
    LinearLayout rate_this_audio_layout;
    private int BUTTON_STATE = 0;
    private final int BUTTON_STATE_ONCE = 0;
    private final int BUTTON_STATE_TWICE = 1;
    private ImageView play;
    private SeekBar seekBar;
    private TextView start_time, end_time;
    MediaPlayer mediaPlayer;
    private Utilities utils;
    private Handler mHandler = new Handler();
    private String songUrl;
    private String imageUrl, description, title, byname;
    private TextView desc, headertitle;
    private String totaltime;
    private ImageView likebtn, previous_btn, next_btn;
    private String likeflag = "F";
    private Button share;
    private DateFormat inFormat2, outFormat;
    private String originaltime;
    private long result;
    private int resDurationInt;
    int currentSongIndex = 0;
    ArrayList<TrendingResponseBean> audiosongsList;
    ArrayList<AudioListData> audiosongsJockeyList;
    ArrayList<AudioDetailsResponseBean> audiosongsgenreList;
    ArrayList<SearchData> audiosongsSearchList;
    ArrayList<String> audioList;
    //int audio_id;
    String audio_id, type, jockey_id;
    int songIndex = 0;
    ArrayList<AudioDetailsResponseBean> list = new ArrayList<AudioDetailsResponseBean>();
    private TextView headerL, headerG;
    private ImageView downIcon, downIcon_g, searchImg;
    private AudioManager audioManager;

    MediaPlayerSingleton mps;

    private boolean isPlaying = false;

    private GestureDetectorCompat gestureDetectorCompat = null;
    private APIInterface apiInterface;
    private RecentlyPlayedResponse recentlyPlayedResponse;
    private int userid;
    private boolean isCheck = true;
    private ArrayList<NotifyDataList> audiosongsNotify = new ArrayList<>();
    ArrayList<NotifyDataList> notifylist = new ArrayList<NotifyDataList>();

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetectorCompat.onTouchEvent(event);
        return true;
    }

    private NotificationManager mNotifyManager;
    private android.support.v4.app.NotificationCompat.Builder build;
    int id = 1;

    private void buildNotification(String action) {
        Intent intent = new Intent(getApplicationContext(), MediaPlayerService.class);
        intent.setAction(action);
        startService(intent);
    }

    private void clearNotification() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(1);
    }


    BroadcastReceiver myReceiver;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.audio_detailed_activity);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            setFinishOnTouchOutside(false);
        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH);
        }


        mps = MediaPlayerSingleton.getInstance(this);


        //registerReceiver();


        buildNotification(MediaPlayerService.ACTION_PLAY);


        DetectSwipeGestureListener gestureListener = new DetectSwipeGestureListener();

        // Set activity in the listener.
        gestureListener.setActivity(this);

        // Create the gesture detector with the gesture listener.
        gestureDetectorCompat = new GestureDetectorCompat(this, gestureListener);

        if (mps.mp != null) {
            if (mps.mp.isPlaying()) {

                songIndex = mps.getCurrentPlayPos();
                Log.w(TAG, "playing");
                isPlaying = true;
            } else {

                if (mps.getMediaPlayerStatus().equalsIgnoreCase("pause")) {
                    isPlaying = true;
                    songIndex = mps.getCurrentPlayPos();
                }

            }

            Log.w(TAG, "media player not null " + mps.checkMediaPlayerIsPlaying());
        } else {
            isPlaying = false;
        }


        initUI();

        onClickmethod();
        utils = new Utilities();
        if (!App.appUtils.isNetAvailable()) {
            alertUserP(AudioActivity.this, "Connection Error", "No Internet connection available", "OK");
        } else {
            displayData();
        }


        //Based on list it progres

        if (audiosongsList != null) {
            audiosongsList = (ArrayList<TrendingResponseBean>) getIntent().getSerializableExtra("songsList");
            for (int l = 0; l < audiosongsList.size(); l++) {
//                Log.e("list",audiosongsList.get(l).getAudio_id());
//                Log.e("audio_id1", String.valueOf(audio_id));
                if (audio_id.equals(audiosongsList.get(l).getAudio_id())) {
                    songIndex = l;
                    playMethod(songIndex);
                    displayData();
                    forwardMusic();
                }
            }
        }


        if (audiosongsgenreList != null) {
            audiosongsgenreList = (ArrayList<AudioDetailsResponseBean>) getIntent().getSerializableExtra("songsList");

            for (int l = 0; l < audiosongsgenreList.size(); l++) {
                Log.e("list", audiosongsgenreList.get(l).getAudio_id());
                Log.e("audio_id1", String.valueOf(audio_id));
                if (audio_id.equals(audiosongsgenreList.get(l).getAudio_id())) {
                    songIndex = l;
                    Log.e("l", String.valueOf(l));
                    playMethod(songIndex);
                    displayData();
                    forwardMusic();
                }
            }
        }


        if (audiosongsSearchList != null) {
            audiosongsSearchList = (ArrayList<SearchData>) getIntent().getSerializableExtra("songsList");

            for (int l = 0; l < audiosongsSearchList.size(); l++) {
//                Log.e("list", String.valueOf(audiosongsSearchList.get(l).getAudio_id()));
//                Log.e("audio_id1", String.valueOf(audio_id));
                if (audio_id.equals(audiosongsSearchList.get(l).getAudio_id())) {
                    songIndex = l;
                    Log.e("l", String.valueOf(l));
                    playMethod(songIndex);
                    displayData();
                    forwardMusic();
                }
            }
        }
        if (audiosongsJockeyList != null) {
            audiosongsJockeyList = (ArrayList<AudioListData>) getIntent().getSerializableExtra("songsList");

            for (int l = 0; l < audiosongsJockeyList.size(); l++) {
                Log.e("list", String.valueOf(audiosongsJockeyList.get(l).getAudio_id()));
                Log.e("jockeylistaudio_id", String.valueOf(audio_id));
                if (audio_id.equals(String.valueOf(audiosongsJockeyList.get(l).getAudio_id()))) {
                    songIndex = l;
                    Log.e("jockeylist", String.valueOf(l));
                    playMethod(songIndex);
                    displayData();
                    forwardMusic();
                }
            }
        }


        if (audiosongsNotify.size() > 0) {
            Log.w(TAG, "inside notify on create " + audiosongsNotify.size());
            notifylist = (ArrayList<NotifyDataList>) getIntent().getSerializableExtra("songsList");
            //  Integer audioid = Integer.valueOf(audio_id);
            for (int l = 0; l < notifylist.size(); l++) {
                if (audio_id.equals(notifylist.get(l).getAudio_id())) {
                    songIndex = l;
                    Log.e("l", String.valueOf(l));
                    playMethod(0);
                    displayData();
                    forwardMusic();
                    break;
                }
            }
        }

    }

    private void APICall() {
        Integer id = Integer.valueOf(jockey_id);
        Integer au_id = Integer.valueOf(audio_id);
        JSONObject json = new JSONObject();
        try {
            json.put("jockey_id", userid);
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


    public void registerReceiver() {
        myReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                Log.w(TAG, "broad cast receiver calling " + intent.getExtras().getString("status"));

                if (intent.getExtras().getString("status").equalsIgnoreCase("playing")) {

                    play.setImageResource(R.drawable.pause_blue);
                    if (mps.mp != null) {
                        if (!mps.mp.isPlaying()) {
                            mps.mp.start();
                        }
                    }
                    setStatus("playing");
//                    buildNotification(MediaPlayerService.ACTION_PLAY);

                } else if (intent.getExtras().getString("status").equalsIgnoreCase("pause")) {
                    play.setImageResource(R.drawable.play_blue);
                    mps.mp.pause();
                    setStatus("pause");
                    //   buildNotification(MediaPlayerService.ACTION_PAUSE);

                }
            }
        };

        registerReceiver(myReceiver, new IntentFilter("MP_STATUS"));
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

    private void onClickmethod() {

        likebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (likeflag.equals("F")) {
                    likebtn.setImageResource(R.drawable.like_r);
                    likeflag = "S";
                } else if (likeflag.equals("S")) {
                    likebtn.setImageResource(R.drawable.heart);
                    likeflag = "F";
                }
            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                byte[] bytes = new byte[0];
                try {
                    bytes = audio_id.getBytes("UTF-8");
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

                String msg = "\nEnjoy this Audio Streaming & Listening App - Voxit.\n";
                String msg1 = "\nIf you have the app already, You will Love Listening to this Audio from the Voxit App.\n";
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = msg + "https://play.google.com/store/apps/details?id=com.triton.voxit\n" + msg1 + " " + "https://voxit.herokuapp.com/auth/getbanner/" + "audio_id=" + encoded + "\n";
                Log.e("shareBody", shareBody);
//String shareBody = "https://play.google.com/store/apps/details?id=com.triton.voxit";
                sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Voxit-Music Let's here to Enjoy the audios");
                sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
            }
        });

//        share.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
//                sharingIntent.setType("text/plain");
//                String shareBody = "https://play.google.com/store/apps/details?id=com.triton.voxit";
//                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Voxit-Music Let's here to Enjoy the video");
//                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
//                startActivity(Intent.createChooser(sharingIntent, "Share via"));
//            }
//        });


        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Log.w(TAG, "mp not null " + mps.getMediaPlayerStatus());
                switch (mps.getMediaPlayerStatus()) {

                    case "pause":
                        play.setImageResource(R.drawable.pause_blue);
                        mps.mp.start();
                        setStatus("playing");
                        buildNotification(MediaPlayerService.ACTION_PLAY);
                        break;

                    case "playing":
                        play.setImageResource(R.drawable.play_blue);
                        mps.mp.pause();
                        setStatus("pause");
                        buildNotification(MediaPlayerService.ACTION_PAUSE);
                        break;
                }

              /*  if (mps.mp.isPlaying()) {
                    play.setImageResource(R.drawable.play_blue);
                    mps.mp.pause();

                    setStatus("pause");
                } else {

                    play.setImageResource(R.drawable.pause_blue);
                    mps.mp.start();
                    setStatus("playing");
                }*/

            }
        });

    }

    //Forward muisc
    private void forwardMusic() {
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                Log.d(TAG, "TAG:seekbar pos" + (i * 1000));

                if (mps.mp != null) {
                    Log.d(TAG, "TAG:Slide moving position" + mps.mp.getCurrentPosition());
                    if (b) {
                        mps.mp.seekTo(i * 1000);
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                Log.d(TAG, "TAG:OnTrackTouch" + mps.mp.getCurrentPosition());
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

//    public void playSong(int songIndex, String type){
//        Log.e("songIndex", String.valueOf(songIndex));
//        // Play song
//        try {
//            player.reset();
//            if(type.equals("Trending")){
//                player.setDataSource(audiosongsList.get(songIndex).getAudio_path());
//                Log.e("title",audiosongsList.get(songIndex).getTitle());
//                nameTv.setText(audiosongsList.get(songIndex).getTitle());
//                desTv.setText("By"+" "+audiosongsList.get(songIndex).getName());
//                desc.setText(audiosongsList.get(songIndex).getDiscription());
//                Glide.with(this).load(audiosongsList.get(songIndex).getImage_path()).into(itemImage);
//            }else if(type.equals("TopGenre")){
//                player.setDataSource(list.get(songIndex).getAudio_path());
//                nameTv.setText(list.get(songIndex).getTitle());
//                desTv.setText("By"+" "+list.get(songIndex).getName());
//                desc.setText(list.get(songIndex).getDiscription());
//                Glide.with(this).load(list.get(songIndex).getImage_path()).into(itemImage);
//            }
//
//            player.prepare();
//            player.start();
//
//            String input = audiosongsList.get(songIndex).getAudio_length();
//
//            Date date2 = null;
//            try {
//
//                date2 = inFormat2.parse(input);
//
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//            if (date2 != null) {
//                originaltime = outFormat.format(date2);
//
//                long min = Integer.parseInt(originaltime.substring(0, 2));
//                long sec = Integer.parseInt(originaltime.substring(3));
//
//                long t = (min * 60L) + sec;
//
//                result = TimeUnit.SECONDS.toMillis(t);
//                resDurationInt = (int) result;
//                Log.e("result", String.valueOf(resDurationInt));
//            }
//            // Updating progress bar
//           // updateProgressBar();
//        } catch (IllegalArgumentException e) {
//            e.printStackTrace();
//        } catch (IllegalStateException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//    public void playSong1(int songIndex){
//        Log.e("songIndex", String.valueOf(songIndex));
//        // Play song
//        try {
//            player.reset();
//            player.setDataSource(list.get(songIndex).getAudio_path());
//            player.prepare();
//            player.start();
//            Log.e("title",list.get(songIndex).getTitle());
//            nameTv.setText(list.get(songIndex).getTitle());
//            desTv.setText("By"+" "+list.get(songIndex).getName());
//            desc.setText(list.get(songIndex).getDiscription());
//            Glide.with(this).load(list.get(songIndex).getImage_path()).into(itemImage);
//            String input = list.get(songIndex).getAudio_length();
//
//            Date date2 = null;
//            try {
//
//                date2 = inFormat2.parse(input);
//
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//            if (date2 != null) {
//                originaltime = outFormat.format(date2);
//
//                long min = Integer.parseInt(originaltime.substring(0, 2));
//                long sec = Integer.parseInt(originaltime.substring(3));
//
//                long t = (min * 60L) + sec;
//
//                result = TimeUnit.SECONDS.toMillis(t);
//                resDurationInt = (int) result;
//                Log.e("result", String.valueOf(resDurationInt));
//            }
//            // Updating progress bar
//            //updateProgressBar();
//        } catch (IllegalArgumentException e) {
//            e.printStackTrace();
//        } catch (IllegalStateException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }


    //  MediaPlayer player;

    private void releasePlayer() {
        if (mps.mp != null) {
            mps.mp.pause();
            mps.mp.reset();
            mps.mp.release();
            //  mps.mp = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mHandler != null) {
            mHandler.removeCallbacks(updateRunnable);
        }

        if (myReceiver != null) {
            unregisterReceiver(myReceiver);
        }

        // completeNotification();

        //releasePlayer();
    }


//    private class GetDurationForSong extends AsyncTask<Void,Void,Integer>{
//        @Override
//        protected Integer doInBackground(Void... voids) {
//            player = new MediaPlayer();
//
//            int duration = 0;
//            player.setAudioStreamType(AudioManager.STREAM_MUSIC);
//            Log.d(TAG,"path :"+audiosongsList.get(0).getAudio_path());
//
//            try {
//                player.setDataSource("http://bloodambulance.com/bloodapp/Ringtone.mp3");
//
//                player.prepare();
//                duration = player.getDuration();
//
//                Log.w(TAG,"Async duration 1 "+duration);
//
//                player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//                    @Override
//                    public void onPrepared(MediaPlayer mp) {
//                        Log.w(TAG,"Async duration 1 "+mp.getDuration());
//
//                    }
//                });
//
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            return duration;
//        }
//
//
//
//        @Override
//        protected void onPostExecute(Integer i) {
//            super.onPostExecute(i);
//            Log.w(TAG,"Async Class duration "+ i);
//        }
//    }


    private Runnable updateRunnable;
    int seekbarPosition;


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

    private void playMethod(final int songIndex) {
        APICall();
//        String url = "http://132.148.140.42/Dumbelltest/DesignImage/MaruvaarthaiPesathey.mp3";
//          String url = songUrl;

        try {

            if (type.equals("Trending")) {
                setType(type);
                String fileName = audiosongsList.get(songIndex).getTitle();
                setFileName(fileName);
                setAuthorName(audiosongsList.get(songIndex).getName());
                setImageUrl(audiosongsList.get(songIndex).getImage_path());
                setCurrentPlayPos(songIndex);
                if (!isPlaying) {
                    releasePlayer();
                    // player = new MediaPlayer();
                    mps.initializePlayer();
                    mps.mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    mps.mp.setDataSource(audiosongsList.get(songIndex).getAudio_path());
                }
                nameTv.setText(audiosongsList.get(songIndex).getTitle());
                //desTv.setText("By"+" "+audiosongsList.get(songIndex).getName());
                String text = "<font color=#000></font> By <font color=#00aee0>" + audiosongsList.get(songIndex).getName() + "</font>";
                desTv.setText(Html.fromHtml(text));
                // desTv.setOnClickListener(this);
                desTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mps.mp.pause();

                        setStatus("pause");

                        Intent i = new Intent(AudioActivity.this, JockeyListActivity.class);
                        i.putExtra("user_jocky_id", String.valueOf(audiosongsList.get(songIndex).getJockey_id()));
                        startActivity(i);
                    }
                });
                desc.setText(audiosongsList.get(songIndex).getDiscription());
                Glide.with(this).load(audiosongsList.get(songIndex).getImage_path()).into(itemImage);


                if (!isPlaying) {
                    mps.mp.prepare();
                    mps.mp.start();
                }

                buildNotification(MediaPlayerService.ACTION_PLAY);

                setStatus("playing");

                int duration = mps.mp.getDuration();
                duration = duration / 1000;

                Log.w(TAG, "duration " + duration);

                seekBar.setMax(duration);

                this.runOnUiThread(updateRunnable = new Runnable() {
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
                                seekBar.setMax(duration);
                                break;
                        }
                        return false;
                    }
                });
//                Log.w("Duartion ", duration + "");

                mps.mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        play.setImageResource(R.drawable.play_blue);

                        if (mps.mp != null) {
                            setStatus("completed");
                            play.setImageResource(R.drawable.pause_blue);
                            // playMethod(songIndex + 1);
//                            audioLogUpdateAPI(mps.mp.getCurrentPosition(),mps.mp.getDuration());
                            // clearCallbacks();
                            playNextSong();

                            forwardMusic();
                        }


//                        if (player.getCurrentPosition() == player.getDuration()){
//                            play.setImageResource(R.drawable.play_blue);
//                        }

//                    if(type.equals("Trending")){
//                        play.setImageResource(R.drawable.pause_blue);
//                        if(songIndex<audiosongsList.size()-1){
//                            songIndex = songIndex+1;
//                            playSong(songIndex,type);
//                        }else {
//                            songIndex = 0;
//                            playSong(songIndex, type);
//                        }
//                    }else if(type.equals("TopGenre")){
//                        play.setImageResource(R.drawable.pause_blue);
//                        if(songIndex<list.size()-1){
//                            songIndex = songIndex+1;
//                            playSong(songIndex,type);
//                        }else {
//                            songIndex = 0;
//                            playSong(songIndex,type);
//                        }
//                    }

                        //   completeNotification();
                    }
                });


            } else if (type.equals("JockeyList")) {

                setType(type);
                String fileName = audiosongsJockeyList.get(songIndex).getTitle();
                setFileName(fileName);
                setCurrentPlayPos(songIndex);
                setAuthorName(audiosongsJockeyList.get(songIndex).getName());
                setImageUrl(audiosongsJockeyList.get(songIndex).getImage_path());

                if (!isPlaying) {
                    releasePlayer();
                    // player = new MediaPlayer();
                    mps.initializePlayer();
                    mps.mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    mps.mp.setDataSource(audiosongsJockeyList.get(songIndex).getAudio_path());
                }
                //  player = new MediaPlayer();
                nameTv.setText(audiosongsJockeyList.get(songIndex).getTitle());
                String text = "<font color=#000></font> By <font color=#00aee0>" + audiosongsJockeyList.get(songIndex).getName() + "</font>";
                desTv.setText(Html.fromHtml(text));
                //desTv.setOnClickListener(this);
                desTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setStatus("pause");
                        //mps.mp.pause();
                        Intent i = new Intent(AudioActivity.this, JockeyListActivity.class);
                        i.putExtra("user_jocky_id", String.valueOf(audiosongsJockeyList.get(songIndex).getJockey_id()));
                        startActivity(i);
                    }
                });
                // desTv.setText("By"+" "+audiosongsSearchList.get(songIndex).getName());
                desc.setText(audiosongsJockeyList.get(songIndex).getDiscription());
                Glide.with(this).load(audiosongsJockeyList.get(songIndex).getImage_path()).into(itemImage);

                if (!isPlaying) {
                    mps.mp.prepare();
                    mps.mp.start();
                }
                int duration = mps.mp.getDuration();

                duration = duration / 1000;
                seekBar.setMax(duration);

                this.runOnUiThread(updateRunnable = new Runnable() {
                    @Override
                    public void run() {
                        if (mps.mp != null) {
//                            Log.d(TAG,"Audio duration"+ player.getDuration());
//                            Log.d(TAG,"Run method current pos "+ (player.getCurrentPosition()/1000));

                            seekbarPosition = mps.mp.getCurrentPosition() / 1000;
                            seekBar.setProgress(seekbarPosition);

                            // Displaying time completed playing
                            end_time.setText("" + utils.milliSecondsToTimer(mps.mp.getDuration()));
                            start_time.setText("" + utils.milliSecondsToTimer(mps.mp.getCurrentPosition()));

                        }
                        mHandler.postDelayed(this, 1000);
                    }
                });

                mps.mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
//                        Log.w(TAG,"duration on prepared "+ mp.getDuration());
                    }
                });

                mps.mp.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
                    @Override
                    public void onBufferingUpdate(MediaPlayer mp, int percent) {
//                        Log.d(TAG,"duration buffering "+ mp.getDuration() +" percent "+ percent);
                    }
                });

                //Attempt to seek to past end of file: request = 259000, durationMs = 0
                mps.mp.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                    @Override
                    public boolean onInfo(MediaPlayer mp, int what, int extra) {
                        switch (what) {
                            case MediaPlayer.MEDIA_INFO_BUFFERING_START:
//                                Log.d(TAG, "buffering start");
                                break;
                            case MediaPlayer.MEDIA_INFO_BUFFERING_END:
//                                Log.d(TAG, "buffering end");
                                int duration = mps.mp.getDuration();

                                duration = duration / 1000;
                                seekBar.setMax(duration);
//                                Log.w("Duartion ", duration + "");
                                //  dismiss_dialog();
                                break;
                        }
                        return false;
                    }
                });
//                Log.w("Duartion ", duration + "");

                mps.mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {

                        play.setImageResource(R.drawable.play_blue);

                        if (mps.mp != null) {
                            setStatus("completed");
                            play.setImageResource(R.drawable.pause_blue);
//                            audioLogUpdateAPI(mps.mp.getCurrentPosition(), mps.mp.getDuration());
                            playNextSong();
//                            if(songIndex<audiosongsJockeyList.size()-1){
//                                playMethod(songIndex+1);
//                                forwardMusic();
//                            }else {
//                                play.setImageResource(R.drawable.play_blue);
//                            }
                        }


                        //   completeNotification();
                    }
                });
            } else if (type.equals("Search")) {


                setType(type);
                String fileName = audiosongsSearchList.get(songIndex).getTitle();
                setFileName(fileName);
                setCurrentPlayPos(songIndex);
                setAuthorName(audiosongsSearchList.get(songIndex).getName());
                setImageUrl(audiosongsSearchList.get(songIndex).getImage_path());


                if (!isPlaying) {
                    releasePlayer();
                    // player = new MediaPlayer();
                    mps.initializePlayer();
                    mps.mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    mps.mp.setDataSource(audiosongsSearchList.get(songIndex).getAudio_path());
                }
                nameTv.setText(audiosongsSearchList.get(songIndex).getTitle());
                // desTv.setText("By"+" "+audiosongsSearchList.get(songIndex).getName());
                String text = "<font color=#000></font> By <font color=#00aee0>" + audiosongsSearchList.get(songIndex).getName() + "</font>";
                desTv.setText(Html.fromHtml(text));
                // desTv.setOnClickListener(this);
                desTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setStatus("pause");
                        // mps.mp.pause();
                        Intent i = new Intent(AudioActivity.this, JockeyListActivity.class);
                        i.putExtra("user_jocky_id", String.valueOf(audiosongsSearchList.get(songIndex).getJockey_id()));
                        startActivity(i);
                    }
                });
                desc.setText(audiosongsSearchList.get(songIndex).getDiscription());
                Glide.with(this).load(audiosongsSearchList.get(songIndex).getImage_path()).into(itemImage);

                if (!isPlaying) {
                    mps.mp.prepare();
                    mps.mp.start();
                }
                int duration = mps.mp.getDuration();

                duration = duration / 1000;
                seekBar.setMax(duration);

                this.runOnUiThread(updateRunnable = new Runnable() {
                    @Override
                    public void run() {
                        if (mps.mp != null) {
//                            Log.d(TAG,"Audio duration"+ player.getDuration());
//                            Log.d(TAG,"Run method current pos "+ (player.getCurrentPosition()/1000));

                            seekbarPosition = mps.mp.getCurrentPosition() / 1000;
                            seekBar.setProgress(seekbarPosition);

                            // Displaying time completed playing
                            end_time.setText("" + utils.milliSecondsToTimer(mps.mp.getDuration()));
                            start_time.setText("" + utils.milliSecondsToTimer(mps.mp.getCurrentPosition()));

                        }
                        mHandler.postDelayed(this, 1000);
                    }
                });

                mps.mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {

                        Log.w(TAG, "on prepared done");
                        mp.start();
//                        Log.w(TAG,"duration on prepared "+ mp.getDuration());
                    }
                });

                mps.mp.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
                    @Override
                    public void onBufferingUpdate(MediaPlayer mp, int percent) {
//                        Log.d(TAG,"duration buffering "+ mp.getDuration() +" percent "+ percent);
                    }
                });

                //Attempt to seek to past end of file: request = 259000, durationMs = 0
                mps.mp.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                    @Override
                    public boolean onInfo(MediaPlayer mp, int what, int extra) {
                        switch (what) {
                            case MediaPlayer.MEDIA_INFO_BUFFERING_START:
//                                Log.d(TAG, "buffering start");
                                break;
                            case MediaPlayer.MEDIA_INFO_BUFFERING_END:
//                                Log.d(TAG, "buffering end");
                                int duration = mps.mp.getDuration();

                                duration = duration / 1000;
                                seekBar.setMax(duration);
//                                Log.w("Duartion ", duration + "");
                                //  dismiss_dialog();
                                break;
                        }
                        return false;
                    }
                });
//                Log.w("Duartion ", duration + "");

                mps.mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {

                        play.setImageResource(R.drawable.play_blue);

                        if (mps.mp != null) {
                            play.setImageResource(R.drawable.pause_blue);
//                            audioLogUpdateAPI(mps.mp.getCurrentPosition(), mps.mp.getDuration());
                            playNextSong();
                        }


                        //   completeNotification();
                    }
                });


            } else if (type.equals("TopGenre")) {

                setType(type);
                String fileName = list.get(songIndex).getTitle();
                setFileName(fileName);
                setCurrentPlayPos(songIndex);
                setAuthorName(list.get(songIndex).getName());
                setImageUrl(list.get(songIndex).getImage_path());


                mps.setSubType(getIntent().getExtras().getString("subType"));


                Log.e("audiosongsList", String.valueOf(list));
                if (!isPlaying) {
                    releasePlayer();
                    // player = new MediaPlayer();
                    mps.initializePlayer();
                    mps.mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    mps.mp.setDataSource(list.get(songIndex).getAudio_path());
                }


                nameTv.setText(list.get(songIndex).getTitle());
                //desTv.setText("By"+" "+list.get(songIndex).getName());
                String text = "<font color=#000></font> By <font color=#00aee0>" + list.get(songIndex).getName() + "</font>";
                desTv.setText(Html.fromHtml(text));
                // desTv.setOnClickListener(this);
                desTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setStatus("pause");
                        mps.mp.pause();
                        Intent i = new Intent(AudioActivity.this, JockeyListActivity.class);
                        i.putExtra("user_jocky_id", String.valueOf(list.get(songIndex).getJockey_id()));
                        startActivity(i);
                    }
                });
                desc.setText(list.get(songIndex).getDiscription());
                Glide.with(this).load(list.get(songIndex).getImage_path()).into(itemImage);
                if (!isPlaying) {
                    mps.mp.prepare();
                    mps.mp.start();
                }
                int duration = mps.mp.getDuration();

                duration = duration / 1000;
                seekBar.setMax(duration);

                this.runOnUiThread(updateRunnable = new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG, "run method calling");
                        if (mps.mp != null) {
                            Log.d(TAG, "run method calling inside player not null " + mps.mp.getDuration());
                            Log.d(TAG, "Run method current pos " + (mps.mp.getCurrentPosition() / 1000));

                            seekbarPosition = mps.mp.getCurrentPosition() / 1000;
                            seekBar.setProgress(seekbarPosition);

                            // Displaying time completed playing
                            end_time.setText("" + utils.milliSecondsToTimer(mps.mp.getDuration()));
                            start_time.setText("" + utils.milliSecondsToTimer(mps.mp.getCurrentPosition()));

                        }
                        mHandler.postDelayed(this, 1000);
                    }
                });

                mps.mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        Log.w(TAG, "duration on prepared " + mp.getDuration());
                    }
                });

                mps.mp.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
                    @Override
                    public void onBufferingUpdate(MediaPlayer mp, int percent) {
                        Log.d(TAG, "duration buffering " + mp.getDuration() + " percent " + percent);
                    }
                });

                //Attempt to seek to past end of file: request = 259000, durationMs = 0
                mps.mp.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                    @Override
                    public boolean onInfo(MediaPlayer mp, int what, int extra) {
                        switch (what) {
                            case MediaPlayer.MEDIA_INFO_BUFFERING_START:
                                Log.d(TAG, "buffering start");
                                //   show_dialog(SuccessStoriesAudioActivity.this);
                                break;
                            case MediaPlayer.MEDIA_INFO_BUFFERING_END:
                                Log.d(TAG, "buffering end");
                                int duration = mps.mp.getDuration();

                                duration = duration / 1000;
                                seekBar.setMax(duration);
                                Log.w("Duartion ", duration + "");
                                //  dismiss_dialog();
                                break;
                        }
                        return false;
                    }
                });
                Log.w("Duartion ", duration + "");

                mps.mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        play.setImageResource(R.drawable.play_blue);
                        setStatus("completed");

                        if (mps.mp != null) {
                            play.setImageResource(R.drawable.pause_blue);
//                            audioLogUpdateAPI(mps.mp.getCurrentPosition(), mps.mp.getDuration());
                            playNextSong();
                        }


                        //   completeNotification();
                    }
                });


            } else if (type.equals("Notify")) {

                Log.w(TAG, "notify calling" + notifylist.get(songIndex).getAudioDetail().getAudio_path());

                setType(type);
                String fileName = notifylist.get(songIndex).getTitle();
                setFileName(fileName);
                setCurrentPlayPos(songIndex);
                setAuthorName(notifylist.get(songIndex).getAudioDetail().getName());
                setImageUrl(notifylist.get(songIndex).getAudioDetail().getImage_path());


                //mps.setSubType(getIntent().getExtras().getString("subType"));


                Log.w("notifysongsList", String.valueOf(notifylist));
                if (!isPlaying) {
                    releasePlayer();
                    // player = new MediaPlayer();
                    mps.initializePlayer();
                    mps.mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    mps.mp.setDataSource(notifylist.get(songIndex).getAudioDetail().getAudio_path());
                }


                nameTv.setText(notifylist.get(songIndex).getTitle());
                //desTv.setText("By"+" "+list.get(songIndex).getName());
                String text = "<font color=#000></font> By <font color=#00aee0>" + notifylist.get(songIndex).getAudioDetail().getName() + "</font>";
                desTv.setText(Html.fromHtml(text));
                desTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setStatus("pause");
                        mps.mp.pause();
                        Intent i = new Intent(AudioActivity.this, JockeyListActivity.class);
                        i.putExtra("user_jocky_id", String.valueOf(notifylist.get(songIndex).getAudioDetail().getJockey_id()));
                        startActivity(i);
                    }
                });
                desc.setText(notifylist.get(songIndex).getAudioDetail().getDiscription());
                Glide.with(this).load(notifylist.get(songIndex).getAudioDetail().getImage_path()).into(itemImage);
                if (!isPlaying) {
                    mps.mp.prepare();
                    mps.mp.start();
                }
                int duration = mps.mp.getDuration();

                duration = duration / 1000;
                seekBar.setMax(duration);

                this.runOnUiThread(updateRunnable = new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG, "run method calling");
                        if (mps.mp != null) {
                            Log.d(TAG, "run method calling inside player not null " + mps.mp.getDuration());
                            Log.d(TAG, "Run method current pos " + (mps.mp.getCurrentPosition() / 1000));

                            seekbarPosition = mps.mp.getCurrentPosition() / 1000;
                            seekBar.setProgress(seekbarPosition);

                            // Displaying time completed playing
                            end_time.setText("" + utils.milliSecondsToTimer(mps.mp.getDuration()));
                            start_time.setText("" + utils.milliSecondsToTimer(mps.mp.getCurrentPosition()));

                        }
                        mHandler.postDelayed(this, 1000);
                    }
                });

                mps.mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        Log.w(TAG, "duration on prepared " + mp.getDuration());
                    }
                });

                mps.mp.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
                    @Override
                    public void onBufferingUpdate(MediaPlayer mp, int percent) {
                        Log.d(TAG, "duration buffering " + mp.getDuration() + " percent " + percent);
                    }
                });

                //Attempt to seek to past end of file: request = 259000, durationMs = 0
                mps.mp.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                    @Override
                    public boolean onInfo(MediaPlayer mp, int what, int extra) {
                        switch (what) {
                            case MediaPlayer.MEDIA_INFO_BUFFERING_START:
                                Log.d(TAG, "buffering start");
                                //   show_dialog(SuccessStoriesAudioActivity.this);
                                break;
                            case MediaPlayer.MEDIA_INFO_BUFFERING_END:
                                Log.d(TAG, "buffering end");
                                int duration = mps.mp.getDuration();

                                duration = duration / 1000;
                                seekBar.setMax(duration);
                                Log.w("Duartion ", duration + "");
                                //  dismiss_dialog();
                                break;
                        }
                        return false;
                    }
                });
                Log.w("Duartion ", duration + "");

                mps.mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        play.setImageResource(R.drawable.play_blue);
                        setStatus("completed");

                        if (mps.mp != null) {
                            play.setImageResource(R.drawable.pause_blue);
//                            audioLogUpdateAPI(mps.mp.getCurrentPosition(), mps.mp.getDuration());
                            playNextSong();
                        }


                        //   completeNotification();
                    }
                });


            }

            if (myReceiver == null) {
                registerReceiver();
            }

        } catch (Exception e) {
            // TODO: handle exception
        }

    }

    private void audioLogUpdateAPI(int duration, int mpDuration) {
        Log.e("testaudiompDuration", String.valueOf(mpDuration));
        long seconds = TimeUnit.MILLISECONDS.toSeconds(duration);
        long totalseconds = TimeUnit.MILLISECONDS.toSeconds(mpDuration);
        Log.e("testaudiototalseconds", String.valueOf(totalseconds));
        Integer id = Integer.valueOf(userid);
        Integer audioid = Integer.valueOf(audio_id);
        String lisent_time = String.valueOf(seconds);
        String total_time = String.valueOf(totalseconds);

        JSONObject json = new JSONObject();
        try {
            json.put("jockey_id", id);
            json.put("audio_id", audioid);
            json.put("listened_time", lisent_time);
            json.put("audio_time", total_time);
            Log.e("testtAPI", String.valueOf(json));
        } catch (JSONException e) {
            Log.e("Exception ", e.toString());
        }

        RequestBody body = RequestBody.create(MediaType.parse("application/json"), String.valueOf(json));

        //Creating an object of our api interface
        apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<AudioLogResponse> call = apiInterface.AudioLogRequestTask(body);

        call.enqueue(new Callback<AudioLogResponse>() {
            @Override
            public void onResponse(Call<AudioLogResponse> call, Response<AudioLogResponse> response) {
                AudioLogResponse resource = response.body();
                if (resource != null) {

                }
            }

            @Override
            public void onFailure(Call<AudioLogResponse> call, Throwable t) {

            }
        });
    }


    private void displayData() {

        nameTv.setText(title);
        //desTv.setText("By"+" "+byname);
        String text = "<font color=#000></font> By <font color=#00aee0>" + byname + "</font>";
        desTv.setText(Html.fromHtml(text));
        //desTv.setText("By"+" "+byname);
        //desTv.setOnClickListener(this);
        desTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  mps.mp.pause();
                Intent i = new Intent(AudioActivity.this, JockeyListActivity.class);
                i.putExtra("user_jocky_id", jockey_id);
                startActivity(i);
            }
        });
        desc.setText(description);
        desc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isCheck) {
                    desc.setMaxLines(12);
                    isCheck = false;
                } else {
                    desc.setMaxLines(2);
                    isCheck = true;
                }
            }
        });

        Glide.with(this).load(imageUrl).into(itemImage);
    }

    public void rateDialogMethod() {

        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.rate_this_audio, null);

        mSmileRating = (SmileRating) alertLayout.findViewById(R.id.ratingView);
        OkBtn = (Button) alertLayout.findViewById(R.id.ok_btn);
        mSmileRating.setOnSmileySelectionListener(this);
        mSmileRating.setOnRatingSelectedListener(this);

        final AlertDialog.Builder alert = new AlertDialog.Builder(AudioActivity.this);

        alert.setView(alertLayout);
        alert.setCancelable(true);
        final AlertDialog dialog = alert.create();
        dialog.setCanceledOnTouchOutside(true);


        //alertmsg.setSelected(true);

        OkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();

            }
        });

        dialog.show();

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)

    private void initUI() {
        headertitle = (TextView) findViewById(R.id.header_title);
        headertitle.setText("Audio");
        hideLanguageView();
        showTitleView();
        inFormat2 = new SimpleDateFormat("HH:mm:ss");
        outFormat = new SimpleDateFormat("mm:ss");
        overflow = (ImageView) findViewById(R.id.overflow);
        itemImage = (ImageView) findViewById(R.id.itemImage);
        overflow = (ImageView) findViewById(R.id.overflow);
        itemImage = (ImageView) findViewById(R.id.itemImage);

        searchImg = (ImageView) findViewById(R.id.search_img);
        searchImg.setVisibility(View.GONE);
        overflow.setOnClickListener(this);

        ratingbar = (RatingBar) findViewById(R.id.ratingBar);
        rate = (TextView) findViewById(R.id.rate);
        headerL = (TextView) findViewById(R.id.language);
        headerG = (TextView) findViewById(R.id.genre);
        downIcon = (ImageView) findViewById(R.id.downIcon);
        downIcon_g = (ImageView) findViewById(R.id.downIcon_g);
        nameTv = (TextView) findViewById(R.id.nameTv);
        desTv = (TextView) findViewById(R.id.category);
        rate_this_audio_tv = (TextView) findViewById(R.id.rate_this_audio_tv);
        rate_this_audio_layout = (LinearLayout) findViewById(R.id.rate_this_audio_layout);
        desc = (TextView) findViewById(R.id.dec);
        likebtn = (ImageView) findViewById(R.id.calllogid);
        share = (Button) findViewById(R.id.follow_btn);
        previous_btn = (ImageView) findViewById(R.id.previous_btn);
        next_btn = (ImageView) findViewById(R.id.next_btn);
        previous_btn.setOnClickListener(this);
        next_btn.setOnClickListener(this);
        headerG.setVisibility(View.GONE);
        downIcon.setVisibility(View.GONE);
        downIcon_g.setVisibility(View.GONE);
        headerL.setText("Audio");
        headerL.setTextColor(getResources().getColor(R.color.blue));
        searchImg.setOnClickListener(this);

        ratingbar = (RatingBar) findViewById(R.id.ratingBar);
        rate = (TextView) findViewById(R.id.rate);
        nameTv = (TextView) findViewById(R.id.nameTv);
        desTv = (TextView) findViewById(R.id.category);
        rate_this_audio_tv = (TextView) findViewById(R.id.rate_this_audio_tv);
        rate_this_audio_layout = (LinearLayout) findViewById(R.id.rate_this_audio_layout);
//        desc = (TextView) findViewById(R.id.dec);
        likebtn = (ImageView) findViewById(R.id.calllogid);
        share = (Button) findViewById(R.id.follow_btn);

        //new add
        play = (ImageView) findViewById(R.id.play);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        start_time = (TextView) findViewById(R.id.start_time);
        end_time = (TextView) findViewById(R.id.end_time);
        seekBar.setProgressTintList(ColorStateList.valueOf(Color.parseColor("#01AEE0")));
//        LayerDrawable stars = (LayerDrawable) ratingbar.getProgressDrawable();
//        stars.getDrawable(2).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);
        LayerDrawable stars = (LayerDrawable) ratingbar.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(getResources().getColor(R.color.yellow1), PorterDuff.Mode.SRC_ATOP);
        stars.getDrawable(1).setColorFilter(getResources().getColor(R.color.yellow1), PorterDuff.Mode.SRC_ATOP);
        stars.getDrawable(0).setColorFilter(getResources().getColor(R.color.gray), PorterDuff.Mode.SRC_ATOP);

        // DrawableCompat.setTint(stars.getDrawable(0), 0x33000000);
        rate_this_audio_layout.setOnClickListener(this);
        ratingbar.setRating(5.0f);
        // ratingbar.setEnabled(false);

        HashMap<String, String> user = session.getUserDetails();
        userid = Integer.parseInt(user.get(SessionManager.JOCKEY_ID));

        Intent i = getIntent();

        songUrl = i.getStringExtra("song");
        jockey_id = i.getStringExtra("jockey_id");
        imageUrl = i.getStringExtra("image");
        description = i.getStringExtra("description");
        title = i.getStringExtra("title");
        byname = i.getStringExtra("name");
        totaltime = i.getStringExtra("audio_length");
        audio_id = i.getStringExtra("audio_id");
        type = i.getStringExtra("type");
        Log.w(TAG, type + jockey_id + " audio id " + audio_id);


        if (type.equals("Trending")) {
            audiosongsList = (ArrayList<TrendingResponseBean>) getIntent().getSerializableExtra("songsList");
            Log.e("audiosongsList", String.valueOf(audiosongsList));


        } else if (type.equals("TopGenre")) {
            String language = "";
            String genre = "";
            audiosongsgenreList = (ArrayList<AudioDetailsResponseBean>) getIntent().getSerializableExtra("songsList");
            Log.e("audiosongsgenreList", String.valueOf(audiosongsgenreList));
            for (int l = 0; l < audiosongsgenreList.size(); l++) {
                if (audio_id.equals(audiosongsgenreList.get(l).getAudio_id())) {
                    language = audiosongsgenreList.get(l).getLanguage();
                    genre = audiosongsgenreList.get(l).getGenre();
                    Log.e("language", language + genre);
                }
            }

            for (int k = 0; k < audiosongsgenreList.size(); k++) {
                if (language.equals(audiosongsgenreList.get(k).getLanguage()) && genre.equals(audiosongsgenreList.get(k).getGenre())) {
                    AudioDetailsResponseBean audioDetailsResponseBean = new AudioDetailsResponseBean();
                    audioDetailsResponseBean.setJockey_id(audiosongsgenreList.get(k).getJockey_id());
                    audioDetailsResponseBean.setAudio_path(audiosongsgenreList.get(k).getAudio_path());
                    audioDetailsResponseBean.setTitle(audiosongsgenreList.get(k).getTitle());
                    audioDetailsResponseBean.setDiscription(audiosongsgenreList.get(k).getDiscription());
                    audioDetailsResponseBean.setImage_path(audiosongsgenreList.get(k).getImage_path());
                    audioDetailsResponseBean.setName(audiosongsgenreList.get(k).getName());
                    audioDetailsResponseBean.setAudio_length(audiosongsgenreList.get(k).getAudio_length());
                    list.add(audioDetailsResponseBean);
                    // Dashboard.LIST = list;
                    // audiosongsList = audiosongsgenreList.get(k);
                    Log.e("list count", String.valueOf(list.size()));
                }

            }

        } else if (type.equals("Search")) {
            audiosongsSearchList = (ArrayList<SearchData>) getIntent().getSerializableExtra("songsList");
            Log.e("audiosongsList", String.valueOf(audiosongsSearchList));
        } else if (type.equals("JockeyList")) {
            audiosongsJockeyList = (ArrayList<AudioListData>) getIntent().getSerializableExtra("songsList");
            Log.e("audiosongsJockeyList", String.valueOf(audiosongsJockeyList));
        } else if (type.equals("Notify")) {
            audiosongsNotify = (ArrayList<NotifyDataList>) getIntent().getSerializableExtra("songsList");
            Log.w(TAG, "init ui");

//            for (int e = 0; e < audiosongsNotify.size(); e++) {
//                NotifyDataList notifyDataList = new NotifyDataList();
//                AudioDetailData data = notifyDataList.setAudioDetail();
//                data.setJockey_id(audiosongsNotify.get(e).getAudioDetail().getJockey_id());
//                data.setAudio_path(audiosongsNotify.get(e).getAudioDetail().getAudio_path());
//                notifyDataList.getAudioDetail().setTitle(audiosongsNotify.get(e).getAudioDetail().getTitle());
//                notifyDataList.getAudioDetail().setDiscription(audiosongsNotify.get(e).getAudioDetail().getDiscription());
//                notifyDataList.getAudioDetail().setImage_path(audiosongsNotify.get(e).getAudioDetail().getImage_path());
//                notifyDataList.getAudioDetail().setName(audiosongsNotify.get(e).getAudioDetail().getName());
//                notifyDataList.getAudioDetail().setAudio_length(audiosongsNotify.get(e).getAudioDetail().getAudio_length());
//                notifylist.add(notifyDataList);
//                Log.e("notify list", String.valueOf(notifylist.size()));
//            }
        }

        Log.e("input", totaltime);
        // String string2 = "2016-09-23 19:10:22";
        DateFormat inFormat2 = new SimpleDateFormat("HH:mm:ss");
        DateFormat outFormat = new SimpleDateFormat("mm:ss");
        String input = totaltime;


        if (isPlaying) {

            if (mps.getMediaPlayerStatus().equalsIgnoreCase("pause")) {
                play.setImageResource(R.drawable.play_blue);
                buildNotification(MediaPlayerService.ACTION_PAUSE);
            }
        }

        Date date2 = null;
        try {

            date2 = inFormat2.parse(input);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (date2 != null) {
            originaltime = outFormat.format(date2);

            long min = Integer.parseInt(originaltime.substring(0, 2));
            long sec = Integer.parseInt(originaltime.substring(3));

            long t = (min * 60L) + sec;

            result = TimeUnit.SECONDS.toMillis(t);
            resDurationInt = (int) result;
            Log.e("result", String.valueOf(resDurationInt));
        }
//        ratingbar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
//            @Override
//            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
//
//                Toast.makeText(AudioActivity.this, String.valueOf(ratingBar.getRating()), Toast.LENGTH_SHORT).show();
//                rate.setText(String.valueOf(ratingBar.getRating()));
//
//            }
//        });


    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//
//        getMenuInflater().inflate(R.menu.overflow_menu, menu);
//
//        return true;
//
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//
//        // Handle action bar actions click
//
//        super.onOptionsItemSelected(item);
//        if(item.getItemId() == R.id.facebook){
//            Toast.makeText(this, "Option pressed= facebook",Toast.LENGTH_LONG).show();
//        }
//        else if(item.getItemId() == R.id.Youtube){
//            Toast.makeText(this, "Option pressed= youtube",Toast.LENGTH_LONG).show();
//        }
//        else if(item.getItemId() == R.id.Twitter){
//            Toast.makeText(this, "Option pressed= twitter",Toast.LENGTH_LONG).show();
//        }
//        return true;
//
//    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.overflow:
                Context wrapper = new ContextThemeWrapper(this, R.style.PopupMenu);
                PopupMenu popupMenu = new PopupMenu(wrapper, v);
                popupMenu.inflate(R.menu.overflow_menu);

                Object menuHelper;
                Class[] argTypes;
                try {
                    Field fMenuHelper = PopupMenu.class.getDeclaredField("mPopup");
                    fMenuHelper.setAccessible(true);
                    menuHelper = fMenuHelper.get(popupMenu);
                    argTypes = new Class[]{boolean.class};
                    menuHelper.getClass().getDeclaredMethod("setForceShowIcon",
                            argTypes).invoke(menuHelper, true);
                    ImageView badgeLayout = (ImageView) popupMenu.getMenu().findItem(R.id.counterView);
                    badgeLayout.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.add_playlist));
                } catch (Exception e) {

                    popupMenu.show();
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            if (item.getItemId() == R.id.report_abuse) {
                                //Toast.makeText(AudioActivity.this, "At home!", Toast.LENGTH_LONG).show();
                                reportAbuseDialogMethod();
                            }
                            if (item.getItemId() == R.id.add_playlist) {

                            }
                            if (item.getItemId() == R.id.go_to_publish) {

                            }

                            return false;
                        }
                    });
                    return;
                }
                popupMenu.show();
                //Set on click listener for the menu

                break;
            case R.id.rate_this_audio_layout:
                rateDialogMethod();
                break;
            case R.id.sexual:
                if (BUTTON_STATE == BUTTON_STATE_ONCE) {
                    //sexualTv.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.your_image));
                    sexualTv.setBackgroundResource(R.drawable.rounded_fill);
                    BUTTON_STATE = BUTTON_STATE_TWICE;
                } else if (BUTTON_STATE == BUTTON_STATE_TWICE) {
                    sexualTv.setBackgroundResource(R.drawable.rounded_outline_btn);
                    BUTTON_STATE = BUTTON_STATE_ONCE;
                }
                break;
            case R.id.caste:
                if (BUTTON_STATE == BUTTON_STATE_ONCE) {
                    //sexualTv.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.your_image));
                    casteTv.setBackgroundResource(R.drawable.rounded_fill);
                    BUTTON_STATE = BUTTON_STATE_TWICE;
                } else if (BUTTON_STATE == BUTTON_STATE_TWICE) {
                    casteTv.setBackgroundResource(R.drawable.rounded_outline_btn);
                    BUTTON_STATE = BUTTON_STATE_ONCE;
                }
                break;
            case R.id.child_abuse:
                if (BUTTON_STATE == BUTTON_STATE_ONCE) {
                    //sexualTv.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.your_image));
                    childAbuseTv.setBackgroundResource(R.drawable.rounded_fill);
                    BUTTON_STATE = BUTTON_STATE_TWICE;
                } else if (BUTTON_STATE == BUTTON_STATE_TWICE) {
                    childAbuseTv.setBackgroundResource(R.drawable.rounded_outline_btn);
                    BUTTON_STATE = BUTTON_STATE_ONCE;
                }
                break;
            case R.id.violence:
                if (BUTTON_STATE == BUTTON_STATE_ONCE) {
                    //sexualTv.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.your_image));
                    violenceTv.setBackgroundResource(R.drawable.rounded_fill);
                    BUTTON_STATE = BUTTON_STATE_TWICE;
                } else if (BUTTON_STATE == BUTTON_STATE_TWICE) {
                    violenceTv.setBackgroundResource(R.drawable.rounded_outline_btn);
                    BUTTON_STATE = BUTTON_STATE_ONCE;
                }
                break;
            case R.id.previous_btn:
                audioLogUpdateAPI(mps.mp.getCurrentPosition(), mps.mp.getDuration());
                //displayData(audiosongsList);
                //   Log.e("previous index", String.valueOf(currentSongIndex + audiosongsList.size()));
                clearCallbacks();

//                if (mps.mp != null){

//                }

                if (type.equals("Trending")) {

                    play.setImageResource(R.drawable.pause_blue);
                    if (songIndex > 0) {
                        songIndex = songIndex - 1;
                        playMethod(songIndex);
                    } else {
                        songIndex = audiosongsList.size() - 1;
                        playMethod(songIndex);
                    }
                } else if (type.equals("TopGenre")) {
                    play.setImageResource(R.drawable.pause_blue);
                    if (songIndex > 0) {
                        songIndex = songIndex - 1;
                        playMethod(songIndex);
                    } else {
                        songIndex = list.size() - 1;
                        playMethod(songIndex);
                    }
                } else if (type.equals("Search")) {
                    play.setImageResource(R.drawable.pause_blue);
                    if (songIndex > 0) {
                        songIndex = songIndex - 1;
                        playMethod(songIndex);
                    } else {
                        songIndex = audiosongsSearchList.size() - 1;
                        playMethod(songIndex);
                    }
                } else if (type.equals("JockeyList")) {
                    play.setImageResource(R.drawable.pause_blue);
                    if (songIndex > 0) {
                        songIndex = songIndex - 1;
                        playMethod(songIndex);
                    } else {
                        songIndex = audiosongsJockeyList.size() - 1;
                        playMethod(songIndex);
                    }
                } else if (type.equals("Notify")) {
                    play.setImageResource(R.drawable.pause_blue);
                    if (songIndex > 0) {
                        songIndex = songIndex - 1;
                        playMethod(songIndex);
                    } else {
                        songIndex = list.size() - 1;
                        playMethod(songIndex);
                    }
                }

                buildNotification(MediaPlayerService.ACTION_PLAY);

                break;
            case R.id.next_btn:
                // displayData(audiosongsList);
                // Log.e("index", String.valueOf(audiosongsList.size()));

                playNextSong();

//                if(currentSongIndex < (audiosongsList.size() - 1)){
//                    playSong(currentSongIndex + 1);
//                    currentSongIndex = currentSongIndex + 1;
//                    Log.e("if", String.valueOf(currentSongIndex));
//                }else{
//                    // play first song
//                    playSong(0);
//                    currentSongIndex = 0;
//                    Log.e("else", String.valueOf(currentSongIndex));
//                }
                break;
            case R.id.search_img:
                Intent i = new Intent(this, SearchActivity.class);
                startActivity(i);
                break;
        }

    }


    private void playNextSong() {
        audioLogUpdateAPI(mps.mp.getCurrentPosition(), mps.mp.getDuration());
        clearCallbacks();
        if (type.equals("Trending")) {
            play.setImageResource(R.drawable.pause_blue);
            if (songIndex < audiosongsList.size() - 1) {
                songIndex = songIndex + 1;
                playMethod(songIndex);
            } else {
                songIndex = 0;
                playMethod(songIndex);
            }
        } else if (type.equals("TopGenre")) {
            play.setImageResource(R.drawable.pause_blue);
            if (songIndex < list.size() - 1) {
                songIndex = songIndex + 1;
                playMethod(songIndex);
            } else {
                songIndex = 0;
                playMethod(songIndex);
            }
        } else if (type.equals("Search")) {
            play.setImageResource(R.drawable.pause_blue);
            if (songIndex < audiosongsSearchList.size() - 1) {
                songIndex = songIndex + 1;
                playMethod(songIndex);
            } else {
                songIndex = 0;
                playMethod(songIndex);
            }
        } else if (type.equals("JockeyList")) {
            play.setImageResource(R.drawable.pause_blue);
            if (songIndex < audiosongsJockeyList.size() - 1) {
                songIndex = songIndex + 1;
                playMethod(songIndex);
            } else {
                songIndex = 0;
                playMethod(songIndex);
            }
        } else if (type.equals("Notify")) {
            play.setImageResource(R.drawable.pause_blue);
            if (songIndex < list.size() - 1) {
                songIndex = songIndex + 1;
                playMethod(songIndex);
            } else {
                songIndex = 0;
                playMethod(songIndex);
            }
        }

        buildNotification(MediaPlayerService.ACTION_PLAY);
    }


    private void clearCallbacks() {
        isPlaying = false;

        if (mps.mp != null) {
            mps.mp.pause();
            mps.mp.reset();
            mps.mp.release();
            mps.mp = null;
        }

        if (updateRunnable != null) {
            mHandler.removeCallbacks(updateRunnable);
        }

        seekBar.setProgress(0);
    }

    @Override
    public void onSmileySelected(int smiley, boolean reselected) {

        switch (smiley) {
            case SmileRating.BAD:
                Log.i(TAG, "Bad");
                break;
            case SmileRating.GOOD:
                Log.i(TAG, "Good");
                break;
            case SmileRating.GREAT:
                Log.i(TAG, "Great");
                break;
            case SmileRating.OKAY:
                Log.i(TAG, "Okay");
                break;
            case SmileRating.TERRIBLE:
                Log.i(TAG, "Terrible");
                break;
            case SmileRating.NONE:
                Log.i(TAG, "None");
                break;
        }
    }

    @Override
    public void onRatingSelected(int level, boolean reselected) {

        Log.i(TAG, "Rated as: " + level + " - " + reselected);
    }

    public void reportAbuseDialogMethod() {

        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.report_abuse_alert, null);

//        mSmileRating = (SmileRating)alertLayout.findViewById(R.id.ratingView);
        send_btn = (Button) alertLayout.findViewById(R.id.send_btn);
        sexualTv = (Button) alertLayout.findViewById(R.id.sexual);
        casteTv = (Button) alertLayout.findViewById(R.id.caste);
        childAbuseTv = (Button) alertLayout.findViewById(R.id.child_abuse);
        violenceTv = (Button) alertLayout.findViewById(R.id.violence);

        final AlertDialog.Builder alert = new AlertDialog.Builder(AudioActivity.this);

        alert.setView(alertLayout);
        alert.setCancelable(true);


        final AlertDialog dialog = alert.create();
        sexualTv.setOnClickListener(this);
        casteTv.setOnClickListener(this);
        childAbuseTv.setOnClickListener(this);
        violenceTv.setOnClickListener(this);
        // dialog.setCanceledOnTouchOutside(false);

        dialog.setCanceledOnTouchOutside(true);
        send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();

            }
        });

        dialog.show();
    }

    @Override
    public void onBackPressed() {


        goBack();
        // mediaPlayer = null;
        // finish();
//        Intent i = new Intent(this,MainActivity.class);
//        startActivity(i);
    }


    private void goBack() {
//        if (mps.mp != null && mps.mp.isPlaying()) {
        //   player.pause();
        //player.isPlaying();
        // mediaPlayer.release();
        if (type.equalsIgnoreCase("Search")) {
            startActivity(new Intent(AudioActivity.this, Dashboard.class)
                    .setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
            // finish();

        } else if (type.equalsIgnoreCase("JockeyList")) {
            startActivity(new Intent(AudioActivity.this, Dashboard.class)
                    .setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
            // finish();
        } else {
            if (updateRunnable != null) {
                mHandler.removeCallbacks(updateRunnable);
            }
        }

        //}


        super.onBackPressed();
    }

    @Override
    public void onPause() {
        super.onPause();
       /* if (mps.mp != null && mps.mp.isPlaying()) {
          //  player.pause();
           // player.isPlaying();
            mps.mp.start();
           // play.setImageResource(R.drawable.play_blue);
           // mediaPlayer.release();
//            mHandler.removeCallbacks(mUpdateTimeTask);
        }*/
    }

    public void displayMessage(String swipe_to_up) {

        goBack();

    }


}
