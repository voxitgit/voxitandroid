package com.triton.voxit.Activity;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.triton.voxit.Adapter.GenereListAdapter;
import com.triton.voxit.R;
import com.triton.voxit.Service.MediaPlayerService;
import com.triton.voxit.Utlity.MediaPlayerSingleton;
import com.triton.voxit.model.AudioDetailsResponseBean;

import java.util.ArrayList;

public class GenreListActivity extends NavigationDrawer {

    private ArrayList<AudioDetailsResponseBean> audiosongsList;
    private BottomNavigationView bottomNavigationView;
    private TextView headertitle;
    private RecyclerView recyclerView;
    private TextView album_title;


    String subType;

    private BroadcastReceiver myReceiver;

    RelativeLayout miniPlayerLayout;
    ImageView imgSong, imgClose, imgMiniPlay;
    TextView txtSongName, txtAuthorName, txtTypeName;

    MediaPlayerSingleton mps;

    public boolean currentListClicked = false;

    private String TAG = "Top genre";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_genre_list);


        mps = MediaPlayerSingleton.getInstance(this);


        initViews();
        initToolbar();
    }

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
                    imgMiniPlay.setImageDrawable(ContextCompat.getDrawable(GenreListActivity.this, R.drawable.ic_pause));
                 /*   play.setImageResource(R.drawable.pause_blue);
                    mps.mp.start();
                    setStatus("playing");*/
//                    buildNotification(MediaPlayerService.ACTION_PLAY);


                } else if (intent.getExtras().getString("status").equalsIgnoreCase("pause")) {
                    /*play.setImageResource(R.drawable.play_blue);
                    mps.mp.pause();
                    setStatus("pause");*/
                    //   buildNotification(MediaPlayerService.ACTION_PAUSE);
                    imgMiniPlay.setImageDrawable(ContextCompat.getDrawable(GenreListActivity.this, R.drawable.ic_play));

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


    public void playSong(String type, String audioUrl, String authorName, String imageUrl, String title, int songIndex,
                         String t, String audioid) {
        Log.w(TAG, audioid + " pos " + songIndex);

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


    private void initViews() {
        Intent i = getIntent();
        name = i.getStringExtra("name");
        subType = i.getStringExtra("subType");


        Log.w(TAG, "sub type " + subType);

        mps.setSubType(subType);

        audiosongsList = (ArrayList<AudioDetailsResponseBean>) getIntent().getSerializableExtra("songsList");
        Log.e("llllll", String.valueOf(audiosongsList));

        album_title = (TextView) findViewById(R.id.album_title);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        if (album_title != null) {
            album_title.setText(name);
        }
        if (audiosongsList != null) {
            adapterCall();
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
                    imgMiniPlay.setImageDrawable(ContextCompat.getDrawable(GenreListActivity.this, R.drawable.ic_play));
                    buildNotification(MediaPlayerService.ACTION_PAUSE);


                } else {
                    mps.setMediaPlayerStatus("playing");
                    imgMiniPlay.setImageDrawable(ContextCompat.getDrawable(GenreListActivity.this, R.drawable.ic_pause));
                    mps.mp.start();
                    buildNotification(MediaPlayerService.ACTION_PLAY);
                }
            }
        });


        miniPlayerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AudioDetailsResponseBean data = audiosongsList.get(mps.getCurrentPlayPos());
                startActivity(new Intent(GenreListActivity.this, AudioActivity.class)
                        .putExtra("jockey_id", String.valueOf(data.getJockey_id()))
                        .putExtra("song", data.getAudio_path())
                        .putExtra("title", data.getTitle())
                        .putExtra("description", data.getDiscription())
                        .putExtra("image", data.getImage_path())
                        .putExtra("name", data.getName())
                        .putExtra("audio_length", data.getAudio_length())
                        .putExtra("audio_id", data.getAudio_id())
                        .putExtra("type", "TopGenre")
                        .putExtra("songsList", audiosongsList)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                GenreListActivity.this.finish();
            }
        });


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


    private void buildNotification(String action) {
        Intent intent = new Intent(getApplicationContext(), MediaPlayerService.class);
        intent.setAction(action);
        startService(intent);
    }

    private void clearNotification() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(1);
    }


    private void adapterCall() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 1, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.addItemDecoration(new EqualSpacingItemDecoration(0));
        recyclerView.setAdapter(new GenereListAdapter(GenreListActivity.this, audiosongsList, GenreListActivity.this));
        recyclerView.setNestedScrollingEnabled(false);
    }

    public void initToolbar() {
        headertitle = (TextView) findViewById(R.id.header_title);
        headertitle.setText("Top Genres");
        bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottom_navigation);
        bottomNavigationView.getMenu().findItem(R.id.home).setChecked(true);
        navigationMenu();
        showHeader();
    }


    private void showHeader() {
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
                        startActivity(new Intent(GenreListActivity.this, Dashboard.class)
                                .setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
                        finish();
                        break;
                    case R.id.notifi:
                        Intent j = new Intent(GenreListActivity.this, NotificationActivity.class);
                        startActivity(j);
                        break;
                    case R.id.search:
                        Intent i4 = new Intent(GenreListActivity.this, SearchActivity.class);
                        startActivity(i4);
                        break;
                    case R.id.profile:
                        Intent i = new Intent(GenreListActivity.this, ProfileActivity.class);
                        startActivity(i);
                        break;

                }
                return true;
            }
        });
    }

}
