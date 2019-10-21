package com.triton.voxit.Activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.triton.voxit.Adapter.ViewPagerAdapter;
import com.triton.voxit.Api.APIClient;
import com.triton.voxit.Api.APIInterface;
import com.triton.voxit.Api.UploadImageInterface;
import com.triton.voxit.Interface.AudioFileInterface;
import com.triton.voxit.R;
import com.triton.voxit.SessionManager.SessionManager;
import com.triton.voxit.Utlity.MediaPlayerSingleton;
import com.triton.voxit.Utlity.Utilitys;
import com.triton.voxit.helpers.Utilities;
import com.triton.voxit.model.AudioFileSubmitRequest;
import com.triton.voxit.model.AudioFileUploadRequest;
import com.triton.voxit.model.BannerResponseBean;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static com.triton.voxit.SessionManager.SessionManager.APPLIED_JOCKEY;

public class AudioFileUpload extends NavigationDrawer implements AudioFileInterface, View.OnClickListener {
    public static ArrayList<BannerResponseBean> BANNERLIST;
    AudioFileInterface audioFileInterface;
    ViewPager viewPager;
    TabLayout tabLayout;
    EditText your_backgEt, des_audioEt, audio_uploadEt;
    TextView submitBt;
    int currentPage = 0;
    Timer timer;
    ViewPagerAdapter viewPagerAdapter;
    final long DELAY_MS = 500;//delay in milliseconds before task is to be executed
    final long PERIOD_MS = 3000;
    private ArrayList<String> ASSERT_IMG = new ArrayList<String>();
    private static String PROGRESS_MSG = "Loading...";
    private static ProgressDialog pdialog;
    private boolean isPicSelect = false;
    private static String profilePicUrl;
    String userChoosenTask, path, fileName;
    private int AUDIO_REQUEST = 100;
    ProgressDialog pDialog;
    private APIInterface apiInterface;
    private AudioFileUploadRequest audioFileResponse;
    private String fullpath;
    TextView headertitle;
    private String jockey_id;
    AudioFileSubmitRequest audioFileSubmitRequest;
    boolean status;
    String sucessStatus;


    private String TAG = "file Upload";
    boolean isRecording = false;


    CountDownTimer t;
    private String mFileName = null;
    private MediaRecorder mRecorder = null;


    ImageView imgPlay, imgRecord,imgdelete;
    TextView txtTime;
    private SeekBar seekBar;

    MediaPlayerSingleton mps;
    Runnable updateRunnable;
    Handler mHandler = new Handler();

    int seekbarPosition;
    private Utilities utils;
    private String username;
    private ImageView imgOk;

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


    public void playSong(String type, String audioUrl, String authorName, String imageUrl, String title, int songIndex, String subType) {
        Log.e("Type", type);

        try {


            imgPlay.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.pause_blue));
            mps.setType(type);
            mps.setFileName(title);
            mps.setAuthorName(authorName);
            mps.setImageUrl(imageUrl);
            mps.setCurrentPlayPos(-1);
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


         /*   txtSongName.setText(title);
            txtAuthorName.setText(authorName);
            txtTypeName.setText(type);*/
            //  Glide.with(this).load(imageUrl).into(imgSong);


            mps.setMediaPlayerStatus("playing");

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

                        txtTime.setText("" + utils.milliSecondsToTimer(mps.mp.getCurrentPosition()));
                        // Displaying time completed playing
                        // end_time.setText("" + utils.milliSecondsToTimer(mps.mp.getDuration()));
                        // int percentage = (100 * player.getCurrentPosition()) / songDuration;

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
                        mps.setMediaPlayerStatus("completed");
                        imgPlay.setImageResource(R.drawable.play_blue);
                        // playMethod(songIndex + 1);

                        //  playNextSong();


                    }


                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //make dir
    public void makeDir() {
        File newDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_MUSIC) + "/voxit");
        if (!newDir.exists()) {
            newDir.mkdirs();
        }
    }


    //start or stop record
    private void onRecord(boolean start) {
        Log.w(TAG, "Inside on record");
        if (start) {

            if (fileName != null) {
                deleteAudioFile();
            }


            seekBar.setVisibility(View.GONE);
            imgPlay.setVisibility(View.GONE);
            imgdelete.setVisibility(View.GONE);

            mps.releasePlayer();

            imgPlay.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.play_blue));

            imgRecord.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.record_stop));

            startRecording();
            audio_uploadEt.setVisibility(View.GONE);
        } else {
            imgRecord.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.record_image));
            stopRecording();

            if (mFileName != null) {
                imgPlay.setVisibility(View.VISIBLE);
                imgdelete.setVisibility(View.VISIBLE);
                imgOk.setVisibility(View.VISIBLE);
                audio_uploadEt.setText(mFileName);
            }

        }
    }


    private static final int REQUEST_MICROPHONE = 1234;

    private boolean askPermissionForRecord() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.RECORD_AUDIO},
                    REQUEST_MICROPHONE);

            return false;

        } else {

            return true;
        }
    }

    private static final int STORAGEPERMISSION =  12;

    private boolean askPermissionForStorage() {
        if (ContextCompat.checkSelfPermission(this,
                WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{WRITE_EXTERNAL_STORAGE},
                    STORAGEPERMISSION);

            return false;

        } else {

            return true;
        }
    }

    public void stopRecord() {
        onRecord(false);
    }

    //timer for record
    private void start_timer() {
        t = new CountDownTimer(Long.MAX_VALUE, 1000) {
            int cnt = 0;
            int min = 0;
            int hour = 0;

            @Override
            public void onTick(long millisUntilFinished) {
                cnt++;
                Log.w(TAG, "Ontick count :" + cnt);

                String time = Integer.valueOf(cnt).toString();
                long millis = cnt;
                //  int seconds = (int) (millis / 60);
                //   int minutes = seconds / 60;

                if (cnt == 60) {
                    cnt = 0;
                    Log.w(TAG, "inside condition :" + cnt);
                    min++;
                }

                if (min == 60) {
                    min = 0;
                    hour++;
                }


                Log.w(TAG, "time " + String.format("%02d:%02d:%02d", hour, min, millis));


                txtTime.setText(String.format("%02d:%02d:%02d", hour, min, millis));
                //  seconds = seconds % 60;
//                txtRecordTime.setText(String.format("%02d:%02d:%02d", hour, min, millis));
            }

            @Override
            public void onFinish() {
            }
        };
    }

    /*delete audio file*/
    private void deleteAudioFile() {
        try {
            txtTime.setText("0:00");
            seekBar.setVisibility(View.GONE);
            imgPlay.setVisibility(View.GONE);
            imgdelete.setVisibility(View.GONE);
            imgOk.setVisibility(View.GONE);
            audio_uploadEt.setVisibility(View.VISIBLE);

            File newDir = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_MUSIC) + "/voxit");
            if (mFileName != null) {
                File file = new File(newDir, mFileName.substring(mFileName.lastIndexOf("/") + 1, mFileName.length()));

                Log.w(TAG, "File path : " + file.getAbsolutePath());

                if (file.exists()) {
                    file.delete();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {

        if (isRecording) {
            alertForRecording();
            Log.w(TAG, "Recording is going");
            deleteAudioFile();
        } else {

            mps.releasePlayer();
            mps.setType("empty");
            mps.setMediaPlayerStatus("empty");
            mps.setSubType("empty");
            mps.setAuthorName("empty");
            super.onBackPressed();

        }
    }


    //start recording
    private void startRecording() {


        Log.w(TAG, "Start recording");
        mFileName = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC) + "/voxit/";
        mFileName += "AUD-" + DateFormat.format("yyyy-MM-dd_hhmmss", new Date()).toString() + ".mp3";
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setOutputFile(mFileName);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);


        if (t != null) {
            txtTime.setText("");
            t.cancel();
        }

        start_timer();

        try {
            mRecorder.prepare();
            t.start();
        } catch (IOException e) {
            Log.e(TAG, "prepare() failed");
        }

        try {
            isRecording = true;
            //showView(txtRecord);
            // txtRecord.setText(recording);
            mRecorder.start();
            // txtStartStopRecord.setText(stopRecord);
            //hideView(imgRecord);
            //showView(imgRecordStop);

        } catch (Exception e) {
            e.printStackTrace();
            isRecording = false;
        }

    }

    //stop recording
    private void stopRecording() {

        Log.w(TAG, "Stop recording method");
        try {
            if (mRecorder != null) {
                mRecorder.stop();
                mRecorder.reset();
                mRecorder.release();
                mRecorder = null;
            }

            isRecording = false;

            // hideView(imgRecordStop);
            // showView(bottomLayout);
            //  hideView(txtRecord);
            // hideView(txtStartStopRecord);
            //txtRecord.setText(recordingStopped);
            t.cancel();

        } catch (RuntimeException stopException) {
            stopException.printStackTrace();
        }

    }

    public void alertForRecording() {
        new AlertDialog.Builder(this)
                .setTitle("Recording")
                .setMessage("Please stop the recording before go to back.....")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })

                // .setIcon(R.mipmap.ic_person_add_black_24dp)
                .show();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.w(TAG, "Ondestroy working");
        if (mRecorder != null) {
            onRecord(false);
        }

        if (updateRunnable != null) {
            mHandler.removeCallbacks(updateRunnable);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.audio_file_upload);
        audioFileInterface = (AudioFileInterface) this;
        audioFileInterface.initVar();
        audioFileInterface.initToolbar();
        audioFileInterface.getSessionData();
        hideLanguageView();
        showTitleView();



        if (askPermissionForStorage()) {
            makeDir();
        }
    }

    @Override
    public void initVar() {
        pDialog = new ProgressDialog(AudioFileUpload.this);
        pDialog.setMessage(AudioFileUpload.this.getString(R.string.please_wait));
        pDialog.setIndeterminate(true);
        pDialog.setCancelable(true);
        viewPager = (ViewPager) findViewById(R.id.pager);
        tabLayout = (TabLayout) findViewById(R.id.tabDots);
        your_backgEt = (EditText) findViewById(R.id.your_backg);
        des_audioEt = (EditText) findViewById(R.id.des_audio);
        audio_uploadEt = (EditText) findViewById(R.id.audio_upload);
        submitBt = (TextView) findViewById(R.id.submit);


        imgPlay = (ImageView) findViewById(R.id.imgPlay);
        imgdelete = (ImageView) findViewById(R.id.imgdelete);
        imgOk = (ImageView) findViewById(R.id.imgOk);
        imgRecord = (ImageView) findViewById(R.id.imgRecord);
        txtTime = (TextView) findViewById(R.id.txtTime);
        seekBar = (SeekBar) findViewById(R.id.seekBar);


        mps = MediaPlayerSingleton.getInstance(this);


        imgRecord.setVisibility(View.VISIBLE);


        submitBt.setOnClickListener(this);
        Log.e("Banner list", String.valueOf(BANNERLIST));
        if (BANNERLIST != null) {
            viewpageData(BANNERLIST);
        }
        audio_uploadEt.setOnClickListener(this);
        session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        jockey_id = user.get(SessionManager.JOCKEY_ID);
        username = user.get(SessionManager.KEY_USER_NAME);
        Log.e("jockey_id", jockey_id);

        forwardMusic();

        utils = new Utilities();

//        if(sucessStatus.equalsIgnoreCase("Success")){
//            Intent i = new Intent(AudioFileUpload.this,AudioUpdatePage.class);
//            startActivity(i);
//
//        }else {
//            Intent i = new Intent(AudioFileUpload.this,AudioFileUpload.class);
//            startActivity(i);
//        }


        imgRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (askPermissionForStorage()) {

                    if (askPermissionForRecord()) {

                        if (isRecording) {
                            onRecord(false);
                        } else {
                            onRecord(true);
                        }
                    }
                }
            }
        });

        imgdelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mFileName != null) {
                    Log.e("AH",mFileName);
                    deleteAudioFile();
                    audio_uploadEt.setText("");
                }
            }
        });

        imgOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mFileName != null){
                    uploadFileData(new File(mFileName));
                }else{
                    showDialogMethodError("Please record your voice");
                }

            }
        });

        imgPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mFileName != null) {

                    if (mps.mp != null && mps.mp.isPlaying()) {

                        imgPlay.setImageDrawable(ContextCompat.getDrawable(AudioFileUpload.this, R.drawable.play_blue));
                        mps.setMediaPlayerStatus("pause");
                        mps.mp.pause();

                    } else {

                        imgPlay.setImageDrawable(ContextCompat.getDrawable(AudioFileUpload.this, R.drawable.pause_blue));
                        if (mps.getMediaPlayerStatus().equalsIgnoreCase("pause")) {

                            if (mps.mp != null) {
                                mps.mp.start();
                            }
                        } else {

                            seekBar.setVisibility(View.VISIBLE);
                            playSong("empty", mFileName, "empty", "empty", "empty", -1, "empty");
                        }
                    }
                }
            }
        });

    }

    private void viewpageData(final ArrayList<BannerResponseBean> bannerlist) {
        tabLayout.setupWithViewPager(viewPager, true);

        viewPagerAdapter = new ViewPagerAdapter(this, bannerlist);
//        viewPagerAdapter = new ViewPagerAdapter(this, slideImageUrls);
        viewPager.setAdapter(viewPagerAdapter);
        /*After setting the adapter use the timer */
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == bannerlist.size()) {
                    currentPage = 0;
                }
                viewPager.setCurrentItem(currentPage++, false);
            }
        };
        timer = new Timer(); // This will create a new Thread
        timer.schedule(new TimerTask() { // task to be scheduled
            @Override
            public void run() {
                handler.post(Update);
            }
        }, DELAY_MS, PERIOD_MS);
    }

    @Override
    public void initToolbar() {
        headertitle = (TextView) findViewById(R.id.header_title);
        headertitle.setText("Become a Voxit Jockey");
    }

    @Override
    public void getSessionData() {

    }

    private void selectFile() {
        final CharSequence[] items = {"Choose from Library",
                "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(AudioFileUpload.this);
        builder.setTitle("Upload your Audio File");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utilitys.checkPermission(AudioFileUpload.this);
                if (items[item].equals("Choose from Library")) {
                    userChoosenTask = "Choose from Library";
                    if (result)
                        getGalleryImage();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void APIcall() {
        pDialog.show();
        Integer jockeyid = Integer.valueOf(jockey_id);

        JSONObject json = new JSONObject();
        try {
            json.put("jockey_id", jockeyid);
            json.put("bio", your_backgEt.getText().toString());
            json.put("description", des_audioEt.getText().toString());
//            json.put("audio_path", mFileName);
            json.put("audio_path", fullpath);
            Log.e("Audio", String.valueOf(json));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), String.valueOf(json));
        //Creating an object of our api interface
        apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<AudioFileSubmitRequest> call = apiInterface.getAudioSubmit(body);

        call.enqueue(new Callback<AudioFileSubmitRequest>() {
            @Override
            public void onResponse(Call<AudioFileSubmitRequest> call, Response<AudioFileSubmitRequest> response) {
                pDialog.dismiss();
                audioFileSubmitRequest = response.body();
                if (audioFileSubmitRequest != null) {
                    sucessStatus = audioFileSubmitRequest.getStatus();
                    if (audioFileSubmitRequest.getStatus().equals("Success")) {
                        String message = audioFileSubmitRequest.getResponse().getApproval_status();
                        status = audioFileSubmitRequest.getResponse().isStatus();
                        Log.e("msg", message);
                        if (sucessStatus.equalsIgnoreCase("Success")) {
                            Intent i = new Intent(AudioFileUpload.this, AudioUpdatePage.class);
                            startActivity(i);

                        } else {
                            Intent i = new Intent(AudioFileUpload.this, AudioFileUpload.class);
                            startActivity(i);
                        }

                        //nextFuntion(message);
                    }
                }
            }

            @Override
            public void onFailure(Call<AudioFileSubmitRequest> call, Throwable t) {


            }
        });
    }

//    private void getRecordAudio() {
//        Intent intent = new Intent(AudioFileUpload.this, RecordActivity.class);
//        startActivity(intent);
//    }

    private void getGalleryImage() {

        Intent videoIntent = new Intent(
                Intent.ACTION_PICK,
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(videoIntent, "Select Audio"), AUDIO_REQUEST);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {

        switch (requestCode) {
            case Utilitys.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (userChoosenTask.equals("Choose from Library"))
                        getGalleryImage();
                } else {
                    //code for deny
                    Log.e("denied", "denied");
                }
                break;

            case STORAGEPERMISSION:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay!
                   makeDir();
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "Permissions Denied to record audio", Toast.LENGTH_LONG).show();
                }
                break;


            case REQUEST_MICROPHONE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay!
                    onRecord(true);
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "Permissions Denied to record audio", Toast.LENGTH_LONG).show();
                }
                break;

        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AUDIO_REQUEST && resultCode == RESULT_OK) {

            //the selected audio.Do some thing with uri
            Uri uri = data.getData();

            Log.e("uri", String.valueOf(uri));
            try {
                String uriString = uri.toString();
                File myFile = new File(uriString);

                path = myFile.getAbsolutePath();

                mFileName = path;
                Log.e("path", path);
                Uri uri1 = Uri.parse(path);
                File file = new File(uri1.getPath());
                Log.e("file", String.valueOf(file));
                String displayName = null;
                String path2 = getAudioPath(uri);

                File f = new File(path2);
                long fileSizeInBytes = f.length();
                long fileSizeInKB = fileSizeInBytes / 1024;
                long fileSizeInMB = fileSizeInKB / 1024;
                if (fileSizeInMB > 8) {
                    String msg = "Couldn't Upload. Sorry, File size is larger than 8MB";
                    showDialogMethod(msg);
                } else {
                    profilePicUrl = path2;
                    File objFile = new File(profilePicUrl);

                    fileName = objFile.getName();
                    Log.e("TAG:filename", profilePicUrl + "" + objFile.getName());
                    ASSERT_IMG.add(profilePicUrl);

                    uploadFileData(f);
                    isPicSelect = true;
                }
            } catch (Exception e) {
                showDialogMethod(e.getMessage());
                //handle exception
                Toast.makeText(AudioFileUpload.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

        }

    }

    private String getAudioPath(Uri uri) {
        String[] data = {MediaStore.Audio.Media.DATA};
        CursorLoader loader = new CursorLoader(getApplicationContext(), uri, data, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.audio_upload:
                selectFile();
                //getGalleryImage();
                break;
            case R.id.submit:

                if (!(mFileName == null)) {
                    APIcall();
                } else {
                    Toast.makeText(AudioFileUpload.this, "All fields are required", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void uploadFileData(File profilePicUrl) {
        Log.e("profilePicUrl", String.valueOf(profilePicUrl));
        pDialog.show();
        String characterFilter = "[^\\p{L}\\p{M}\\p{N}\\p{P}\\p{Z}\\p{Cf}\\p{Cs}\\s]";
        final String emotionless = profilePicUrl.getName().replaceAll(characterFilter, " ");

        Date currentTime = Calendar.getInstance().getTime();

        RequestBody mFile = RequestBody.create(MediaType.parse("audio/*"), profilePicUrl);
        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", emotionless, mFile);
        RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), emotionless);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://tritontutebox.com/voxit/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
//        String lastFormat = jockey_id+username+currentTime;
//        Log.e("lastFormat", lastFormat);

        UploadImageInterface uploadImage = retrofit.create(UploadImageInterface.class);
        Call<AudioFileUploadRequest> call = uploadImage.uploadFile(fileToUpload, filename);

        call.enqueue(new Callback<AudioFileUploadRequest>() {
            @Override
            public void onResponse(Call<AudioFileUploadRequest> call, Response<AudioFileUploadRequest> response) {
                Log.e("Successresponse", new Gson().toJson(response.body()));
                pDialog.dismiss();
//                showDialogMethod("Updated Succesfully");
                audioFileResponse = response.body();
                if (audioFileResponse != null) {
                    fullpath = audioFileResponse.getResponse().get(0).getFull_path();
//                    APIcall();
                    audio_uploadEt.setText(emotionless);
                } else {
                    // showDialogMethod("Response Empty!! \n Audio doesn't upload");
                }

            }

            @Override
            public void onFailure(Call<AudioFileUploadRequest> call, Throwable t) {
                pDialog.dismiss();
                showDialogMethod(t.getMessage());
                Log.e("error", t.getMessage());
            }
        });

    }

    private void showDialogMethod(String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(AudioFileUpload.this).create();
        //alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        Intent i = new Intent(AudioFileUpload.this, AudioUpdatePage.class);
                        startActivity(i);

                    }
                });
        alertDialog.show();
    }

    private void showDialogMethodError(String message) {
        final AlertDialog alertDialog = new AlertDialog.Builder(AudioFileUpload.this).create();
        //alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        alertDialog.cancel();

                    }
                });
        alertDialog.show();
    }
}
