package com.triton.voxit.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.hsalf.smilerating.SmileRating;
import com.triton.voxit.Api.APIClient;
import com.triton.voxit.Api.APIInterface;
import com.triton.voxit.R;
import com.triton.voxit.helpers.Utilities;
import com.triton.voxit.model.AudioDetailsResponseBean;
import com.triton.voxit.model.ShareData;
import com.triton.voxit.model.ShareRequest;
import com.triton.voxit.model.TrendingResponseBean;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AudioActivityShare extends Activity implements View.OnClickListener, SmileRating.OnSmileySelectionListener,
        SmileRating.OnRatingSelectedListener {
    String audio_id;
    private static final String TAG = "AudioActivityShare";
    private ProgressDialog pDialog;
    private APIInterface apiInterface;
    ArrayList<ShareData> shareDataArrayList;
    ImageView overflow,itemImage;
    RatingBar ratingbar;
    TextView rate,rate_this_audio_tv,nameTv,desTv;
    private SmileRating mSmileRating;
    private Button sexualTv,casteTv,childAbuseTv,violenceTv;
    private Button OkBtn,send_btn;
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
    private String image_path, discription,title,name,audio_path;
    private TextView desc;
    private String totaltime;
    private ImageView likebtn,previous_btn,next_btn;
    private String likeflag = "F";
    private Button share;
    private DateFormat inFormat2, outFormat;
    private String originaltime;
    private long result;
    private int resDurationInt;
    int currentSongIndex = 0;

    int songIndex;
    ArrayList<AudioDetailsResponseBean> list = new ArrayList<AudioDetailsResponseBean>();
    private TextView headerL,headerG;
    private ImageView downIcon,downIcon_g,searchImg;
    private AudioManager audioManager;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.audio_detailed_activity);
        pDialog = new ProgressDialog(AudioActivityShare.this);
        pDialog.setMessage(AudioActivityShare.this.getString(R.string.please_wait));
        pDialog.setIndeterminate(true);
        pDialog.setCancelable(true);
        initUI();
        onClickmethod();
        utils = new Utilities();
        if(shareDataArrayList!=null){

            for(int l=0; l<shareDataArrayList.size(); l++){
//                Log.e("list",audiosongsList.get(l).getAudio_id());
//                Log.e("audio_id1", String.valueOf(audio_id));
                if(audio_id.equals(shareDataArrayList.get(l).getAudio_id())){
                    songIndex = l;
                    playMethod(songIndex);

                    forwardMusic();
                }
            }
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initUI(){
//        headertitle = (TextView) findViewById(R.id.header_title);
//        headertitle.setText("Audio");
        inFormat2 = new SimpleDateFormat("HH:mm:ss");
        outFormat = new SimpleDateFormat("mm:ss");
        overflow = (ImageView)findViewById(R.id.overflow);
        itemImage = (ImageView)findViewById(R.id.itemImage);
        overflow = (ImageView) findViewById(R.id.overflow);
        itemImage = (ImageView) findViewById(R.id.itemImage);

        searchImg = (ImageView)findViewById(R.id.search_img);
        searchImg.setVisibility(View.GONE);
        // overflow.setOnClickListener(this);

        ratingbar=(RatingBar)findViewById(R.id.ratingBar);
        rate = (TextView)findViewById(R.id.rate);
        headerL = (TextView)findViewById(R.id.language);
        headerG = (TextView)findViewById(R.id.genre);
        downIcon = (ImageView)findViewById(R.id.downIcon);
        downIcon_g = (ImageView)findViewById(R.id.downIcon_g);
        nameTv = (TextView)findViewById(R.id.nameTv);
        desTv = (TextView)findViewById(R.id.category);
        rate_this_audio_tv = (TextView)findViewById(R.id.rate_this_audio_tv);
        rate_this_audio_layout = (LinearLayout)findViewById(R.id.rate_this_audio_layout);
        desc = (TextView)findViewById(R.id.dec);
        likebtn = (ImageView)findViewById(R.id.calllogid);
        share = (Button)findViewById(R.id.follow_btn);
        previous_btn = (ImageView) findViewById(R.id.previous_btn);
        previous_btn.setVisibility(View.GONE);
        next_btn = (ImageView)findViewById(R.id.next_btn);
        next_btn.setVisibility(View.GONE);
        // previous_btn.setOnClickListener(this);
        //next_btn.setOnClickListener(this);
        headerG.setVisibility(View.GONE);
        downIcon.setVisibility(View.GONE);
        downIcon_g.setVisibility(View.GONE);
        headerL.setText("Audio");
        headerL.setTextColor(getResources().getColor(R.color.blue));
        // searchImg.setOnClickListener(this);

        ratingbar = (RatingBar) findViewById(R.id.ratingBar);
        rate = (TextView) findViewById(R.id.rate);
        nameTv = (TextView) findViewById(R.id.nameTv);
        desTv = (TextView) findViewById(R.id.category);
        rate_this_audio_tv = (TextView) findViewById(R.id.rate_this_audio_tv);
        rate_this_audio_layout = (LinearLayout) findViewById(R.id.rate_this_audio_layout);
        desc = (TextView) findViewById(R.id.dec);
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

        if (getIntent().getData() != null) {
            Uri data = getIntent().getData();

            Log.w(TAG, "data :" + data.toString());
            try {
                String val = data.toString();
                String value = val.substring(val.indexOf("=") + 1, val.length());
                Log.w(TAG,"value : "+ value);

                byte[] output = android.util.Base64.decode(val.substring(val.indexOf("=") + 1, val.length()), Base64.DEFAULT);

                audio_id = new String(output);
                Log.w(TAG,"audio_id : "+ audio_id);
                getShareData();

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

    private void getShareData(){
        pDialog.show();
        JSONObject json = new JSONObject();
        Integer audioid = Integer.valueOf(audio_id);
        Log.e("TAG:audioid", String.valueOf(audioid));
        try {
            json.put("audio_id", audioid);
            Log.e("jsonArray", String.valueOf(json));
        } catch (JSONException e) {
            Log.e("Exception ", e.toString());
        }

        // RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), String.valueOf(json));
        apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<ShareRequest> call = apiInterface.shareRequestTask(json.toString());

        call.enqueue(new Callback<ShareRequest>() {
            @Override
            public void onResponse(Call<ShareRequest> call, Response<ShareRequest> response) {

                ShareRequest resource = response.body();
                Log.e("Success response", new Gson().toJson(response.body()));
                Log.e("TAG:Response", String.valueOf(resource.getCode()));
                Log.e("TAG:Status",resource.getStatus());
                if (resource != null){
                if(resource.getStatus().equals("Success")){
                    pDialog.dismiss();
                    shareDataArrayList = resource.getData();
                    for (int i = 0; i < shareDataArrayList.size(); i++) {
                        title = shareDataArrayList.get(i).getTitle();
                        name = shareDataArrayList.get(i).getName();
                        discription = shareDataArrayList.get(i).getDiscription();
                        image_path = shareDataArrayList.get(i).getImage_path();
                        audio_path = shareDataArrayList.get(i).getAudio_path();
                        Log.e("details", title + image_path + audio_path);
//                        nameTv.setText(title);
//                        desTv.setText("By"+" "+name);
//                        desc.setText(discription);
                       // Glide.with(AudioActivityShare.this).load(image_path).into(itemImage);
                        songIndex = i;
                        playMethod(songIndex);

                        forwardMusic();
                    }

                }else if(resource.getStatus().equals("Failure")){
                    pDialog.dismiss();
                    shareDataArrayList = resource.getData();
                }

                }

            }

            @Override
            public void onFailure(Call<ShareRequest> call, Throwable t) {
                pDialog.dismiss();
                // showDialogMethod("Alert","Bad Network..");
            }
        });
    }
    private void onClickmethod() {

        likebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(likeflag.equals("F")){
                    likebtn.setImageResource(R.drawable.like_r);
                    likeflag = "S";
                }else if(likeflag.equals("S")){
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

                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = "https://voxit.herokuapp.com/auth/getbanner/" + "audio_id=" + encoded;
                Log.e("shareBody",shareBody);
                //String shareBody = "https://play.google.com/store/apps/details?id=com.triton.voxit";
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Voxit-Music Let's here to Enjoy the audios");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
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
                if (player != null && player.isPlaying()){
                    play.setImageResource(R.drawable.play_blue);
                    player.pause();
                }else{
                    play.setImageResource(R.drawable.pause_blue);
                    player.start();
                }

            }
        });

    }

    //Forward muisc
    private void forwardMusic(){
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                Log.d(TAG,"TAG:seekbar pos"+ (i * 1000));

                if (player != null) {
                    Log.d(TAG,"TAG:Slide moving position"+ player.getCurrentPosition());
                    if (b) {
                        player.seekTo(i * 1000);
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                Log.d(TAG,"TAG:OnTrackTouch"+ player.getCurrentPosition());
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }
    MediaPlayer player;

    private void releasePlayer(){
        if (player != null) {
            player.pause();
            player.reset();
            player.release();
            player = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mHandler != null) {
            mHandler.removeCallbacks(updateRunnable);
        }

        // completeNotification();

        releasePlayer();
    }
    private Runnable updateRunnable;
    int seekbarPosition;
    private void playMethod(final int songIndex) {

//        String url = "http://132.148.140.42/Dumbelltest/DesignImage/MaruvaarthaiPesathey.mp3";
//          String url = songUrl;

        try {

                releasePlayer();
                player = new MediaPlayer();
                player.setAudioStreamType(AudioManager.STREAM_MUSIC);

                //Get Data from list
            player.setAudioStreamType(AudioManager.STREAM_MUSIC);

            //Get Data from list
            player.setDataSource(shareDataArrayList.get(songIndex).getAudio_path());
            nameTv.setText(shareDataArrayList.get(songIndex).getTitle());
            desTv.setText("By"+" "+shareDataArrayList.get(songIndex).getName());
            desc.setText(shareDataArrayList.get(songIndex).getDiscription());
            Glide.with(this).load(shareDataArrayList.get(songIndex).getImage_path()).into(itemImage);

                player.prepare();
                player.start();

                int duration = player.getDuration();
                duration = duration / 1000;
                seekBar.setMax(duration);

                this.runOnUiThread(updateRunnable = new Runnable() {
                    @Override
                    public void run() {

                        if (player != null) {
                            seekbarPosition = player.getCurrentPosition() / 1000;
                            seekBar.setProgress(seekbarPosition);

                            // Displaying time completed playing
                            end_time.setText(""+utils.milliSecondsToTimer(player.getDuration()));
//                            int percentage = (100 * player.getCurrentPosition()) / songDuration;
                            start_time.setText(""+utils.milliSecondsToTimer(player.getCurrentPosition()));

                        }
                        mHandler.postDelayed(this, 1000);
                    }
                });

                player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        Log.w(TAG,"TAG:prepared "+ mp.getDuration());
                    }
                });

                player.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
                    @Override
                    public void onBufferingUpdate(MediaPlayer mp, int percent) {
                        Log.d(TAG,"TAG:buffering "+ mp.getDuration() +" percent "+ percent);

                        if (player != null) {

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
                player.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                    @Override
                    public boolean onInfo(MediaPlayer mp, int what, int extra) {
                        switch (what) {
                            case MediaPlayer.MEDIA_INFO_BUFFERING_START:
                                break;
                            case MediaPlayer.MEDIA_INFO_BUFFERING_END:

                                int duration = player.getDuration();

                                duration = duration / 1000;
                                seekBar.setMax(duration);
                                break;
                        }
                        return false;
                    }
                });
//                Log.w("Duartion ", duration + "");

                player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        play.setImageResource(R.drawable.play_blue);

                        if (player != null){
                            play.setImageResource(R.drawable.pause_blue);
                            playMethod(songIndex);
                            forwardMusic();
                        }


                        if (mHandler != null) {
                            mHandler.removeCallbacks(updateRunnable);
                        }
                        //   completeNotification();
                    }
                });





        } catch (Exception e) {
            // TODO: handle exception
        }
    }
    public void rateDialogMethod(){

        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.rate_this_audio, null);

        mSmileRating = (SmileRating)alertLayout.findViewById(R.id.ratingView);
        OkBtn = (Button) alertLayout.findViewById(R.id.ok_btn);
        mSmileRating.setOnSmileySelectionListener(this);
        mSmileRating.setOnRatingSelectedListener(this);

        final AlertDialog.Builder alert = new AlertDialog.Builder(AudioActivityShare.this);

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
    @Override
    public void onClick(View v) {
        switch(v.getId()){
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
                    argTypes = new Class[] { boolean.class };
                    menuHelper.getClass().getDeclaredMethod("setForceShowIcon",
                            argTypes).invoke(menuHelper, true);
                    ImageView badgeLayout = (ImageView) popupMenu.getMenu().findItem(R.id.counterView);
                    badgeLayout.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.add_playlist));
                } catch (Exception e) {

                    popupMenu.show();
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            if (item.getItemId()== R.id.report_abuse){
                                //Toast.makeText(AudioActivity.this, "At home!", Toast.LENGTH_LONG).show();
                                reportAbuseDialogMethod();
                            }
                            if (item.getItemId() == R.id.add_playlist){

                            }
                            if (item.getItemId() == R.id.go_to_publish){

                            }

                            return false;
                        }
                    });
                    return ;
                }
                popupMenu.show();
                //Set on click listener for the menu

                break;
            case R.id.rate_this_audio_layout:
                rateDialogMethod();
                break;
            case R.id.sexual:
                if(BUTTON_STATE==BUTTON_STATE_ONCE){
                    //sexualTv.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.your_image));
                    sexualTv.setBackgroundResource(R.drawable.rounded_fill);
                    BUTTON_STATE = BUTTON_STATE_TWICE;
                }else if(BUTTON_STATE==BUTTON_STATE_TWICE){
                    sexualTv.setBackgroundResource(R.drawable.rounded_outline_btn);
                    BUTTON_STATE = BUTTON_STATE_ONCE;
                }
                break;
            case R.id.caste:
                if(BUTTON_STATE==BUTTON_STATE_ONCE){
                    //sexualTv.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.your_image));
                    casteTv.setBackgroundResource(R.drawable.rounded_fill);
                    BUTTON_STATE = BUTTON_STATE_TWICE;
                }else if(BUTTON_STATE==BUTTON_STATE_TWICE){
                    casteTv.setBackgroundResource(R.drawable.rounded_outline_btn);
                    BUTTON_STATE = BUTTON_STATE_ONCE;
                }
                break;
            case R.id.child_abuse:
                if(BUTTON_STATE==BUTTON_STATE_ONCE){
                    //sexualTv.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.your_image));
                    childAbuseTv.setBackgroundResource(R.drawable.rounded_fill);
                    BUTTON_STATE = BUTTON_STATE_TWICE;
                }else if(BUTTON_STATE==BUTTON_STATE_TWICE){
                    childAbuseTv.setBackgroundResource(R.drawable.rounded_outline_btn);
                    BUTTON_STATE = BUTTON_STATE_ONCE;
                }
                break;
            case R.id.violence:
                if(BUTTON_STATE==BUTTON_STATE_ONCE){
                    //sexualTv.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.your_image));
                    violenceTv.setBackgroundResource(R.drawable.rounded_fill);
                    BUTTON_STATE = BUTTON_STATE_TWICE;
                }else if(BUTTON_STATE==BUTTON_STATE_TWICE){
                    violenceTv.setBackgroundResource(R.drawable.rounded_outline_btn);
                    BUTTON_STATE = BUTTON_STATE_ONCE;
                }
                break;
//            case R.id.previous_btn:
//                //displayData(audiosongsList);
//                //   Log.e("previous index", String.valueOf(currentSongIndex + audiosongsList.size()));
//                if(type.equals("Trending")){
//                    play.setImageResource(R.drawable.pause_blue);
//                    if(songIndex > 0){
//                        songIndex = songIndex - 1;
//                        playMethod(songIndex);
//                    }else {
//                        songIndex = audiosongsList.size()-1;
//                        playMethod(songIndex);
//                    }
//                }else if(type.equals("TopGenre")){
//                    play.setImageResource(R.drawable.pause_blue);
//                    if(songIndex > 0){
//                        songIndex = songIndex - 1;
//                        playMethod(songIndex);
//                    }else {
//                        songIndex = list.size()-1;
//                        playMethod(songIndex);
//                    }
//                }else if(type.equals("Search")){
//                    play.setImageResource(R.drawable.pause_blue);
//                    if(songIndex > 0){
//                        songIndex = songIndex - 1;
//                        playMethod(songIndex);
//                    }else {
//                        songIndex = audiosongsSearchList.size()-1;
//                        playMethod(songIndex);
//                    }
//                }
//
//                break;
//            case R.id.next_btn:
//                // displayData(audiosongsList);
//                // Log.e("index", String.valueOf(audiosongsList.size()));
//                if(type.equals("Trending")){
//                    play.setImageResource(R.drawable.pause_blue);
//                    if(songIndex<audiosongsList.size()-1){
//                        songIndex = songIndex+1;
//                        playMethod(songIndex);
//                    }else {
//                        songIndex = 0;
//                        playMethod(songIndex);
//                    }
//                }else if(type.equals("TopGenre")){
//                    play.setImageResource(R.drawable.pause_blue);
//                    if(songIndex<list.size()-1){
//                        songIndex = songIndex+1;
//                        playMethod(songIndex);
//                    }else {
//                        songIndex = 0;
//                        playMethod(songIndex);
//                    }
//                }else if(type.equals("Search")){
//                    play.setImageResource(R.drawable.pause_blue);
//                    if(songIndex<audiosongsSearchList.size()-1){
//                        songIndex = songIndex+1;
//                        playMethod(songIndex);
//                    }else {
//                        songIndex = 0;
//                        playMethod(songIndex);
//                    }
//                }
//
//
//
//
////                if(currentSongIndex < (audiosongsList.size() - 1)){
////                    playSong(currentSongIndex + 1);
////                    currentSongIndex = currentSongIndex + 1;
////                    Log.e("if", String.valueOf(currentSongIndex));
////                }else{
////                    // play first song
////                    playSong(0);
////                    currentSongIndex = 0;
////                    Log.e("else", String.valueOf(currentSongIndex));
////                }
//                break;
            case R.id.search_img:
                Intent i = new Intent(this,SearchActivity.class);
                startActivity(i);
                break;
        }

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
    public void reportAbuseDialogMethod(){

        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.report_abuse_alert, null);

//        mSmileRating = (SmileRating)alertLayout.findViewById(R.id.ratingView);
        send_btn = (Button) alertLayout.findViewById(R.id.send_btn);
        sexualTv = (Button) alertLayout.findViewById(R.id.sexual);
        casteTv = (Button) alertLayout.findViewById(R.id.caste);
        childAbuseTv = (Button) alertLayout.findViewById(R.id.child_abuse);
        violenceTv = (Button) alertLayout.findViewById(R.id.violence);

        final AlertDialog.Builder alert = new AlertDialog.Builder(AudioActivityShare.this);

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

        if (player != null && player.isPlaying()){
            player.pause();
            // mediaPlayer.release();
            mHandler.removeCallbacks(updateRunnable);
        }
        // mediaPlayer = null;
        finish();
    }


    @Override
    public void onPause() {
        super.onPause();
        if (player != null && player.isPlaying()) {
            player.pause();
            play.setImageResource(R.drawable.play_blue);
//            mediaPlayer.release();
//            mHandler.removeCallbacks(mUpdateTimeTask);
        }
    }
}
