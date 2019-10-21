package com.triton.voxit.Activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.triton.voxit.Adapter.DasboardAdapter;
import com.triton.voxit.Adapter.GenreAdapter;
import com.triton.voxit.Adapter.SlidingAdapter;
import com.triton.voxit.Adapter.TopGenreMultiListAdapter;
import com.triton.voxit.Adapter.ViewPagerAdapter;
import com.triton.voxit.Api.APIClient;
import com.triton.voxit.Api.APIInterface;
import com.triton.voxit.R;
import com.triton.voxit.Service.GPSTracker;
import com.triton.voxit.Service.MediaPlayerService;
import com.triton.voxit.SessionManager.SessionManager;
import com.triton.voxit.Utlity.MediaPlayerSingleton;
import com.triton.voxit.app.App;
import com.triton.voxit.model.AudioDetailData;
import com.triton.voxit.model.AudioDetailsResponseBean;
import com.triton.voxit.model.AudioListData;
import com.triton.voxit.model.BannerResponseBean;
import com.triton.voxit.model.DashboardResponse;
import com.triton.voxit.model.DashboardResponsebean;
import com.triton.voxit.model.GenreResponseBean;
import com.triton.voxit.model.GetPopupDataRequest;
import com.triton.voxit.model.GetPopupResponseData;
import com.triton.voxit.model.LiveResponseBean;
import com.triton.voxit.model.NotificationToken;
import com.triton.voxit.model.RecentlyPlayedResponse;
import com.triton.voxit.model.SearchData;
import com.triton.voxit.model.T_LResponseBean;
import com.triton.voxit.model.TopGenreResponseBean;
import com.triton.voxit.model.TrendingResponseBean;
import com.triton.voxit.model.UserLogResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Dashboard extends NavigationDrawer implements View.OnClickListener {

    public static ArrayList<String> LIST;
    BottomNavigationView bottomNavigationView;
    RecyclerView recyclerView, genre_recycler_view, comedy_recycler_view, filmy_recycler_view;
    private ArrayList<String> ImagesArray = new ArrayList<String>();

    LinearLayoutManager Manager;
    SlidingAdapter slidingAdapter;
    GridLayoutManager gridLayoutManager;
    String[] slideImageUrls = {"https://cdn.pixabay.com/photo/2017/09/30/15/10/pizza-2802332_640.jpg",
            "https://cdn.pixabay.com/photo/2016/07/11/03/23/chicken-rice-1508984_640.jpg",
            "https://cdn.pixabay.com/photo/2017/03/30/08/10/chicken-intestine-2187505_640.jpg",
            "https://cdn.pixabay.com/photo/2017/02/15/15/17/meal-2069021_640.jpg",
            "https://cdn.pixabay.com/photo/2017/06/01/07/15/food-2362678_640.jpg"};
    ViewPager viewPager;
    ViewPagerAdapter viewPagerAdapter;
    LinearLayout languageTv, genreTv;
    ImageView searchImg, imgMiniPlay;
    int currentPage = 0;
    Timer timer;
    final long DELAY_MS = 500;//delay in milliseconds before task is to be executed
    final long PERIOD_MS = 3000;
    private static final String PREFERENCES = "";
    private SharedPreferences.Editor LoginString;
    private Context context;
    private ProgressBar spinner;
    private APIInterface apiInterface;
    private DashboardResponsebean Dashboardresponsebean;
    private String Status;
    private ArrayList<DashboardResponse> dashdataResponse;
    private ArrayList<BannerResponseBean> bannerResponse;
    private TabLayout tabLayout;
    // private String flag = "S";
    private ArrayList<T_LResponseBean> tlresponse;
    private ArrayList<TrendingResponseBean> trendingResponse;
    private ArrayList<LiveResponseBean> liveResponse;
    private ArrayList<GenreResponseBean> genreResponse;
    private ArrayList<TopGenreResponseBean> topgenreResponse;
    SessionManager session;
    SwipeRefreshLayout mSwipeRefreshLayout;
    String token, popup_image_path, appVersionName;
    int jockey_id;
    MediaPlayerSingleton mps;
    TopGenreMultiListAdapter topGenreMultiListAdapter;
    private RelativeLayout miniPlayerLayout;
    private ImageView imgClose, imgSong;
    private TextView txtSongName, txtAuthorName, txtTypeName;

    private BroadcastReceiver myReceiver;
    ArrayList<AudioListData> audioListData = new ArrayList<>();
    private ArrayList<SearchData> searchDataResponse;
    private ArrayList<AudioDetailsResponseBean> TopListgenreResponse;
    boolean loginStatus;
    private GetPopupDataRequest GetPopupDataRequestdata;
    private ArrayList<GetPopupResponseData> imageList;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    LocationManager manager;
    static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private final static int PLAY_SERVICES_REQUEST = 1000;
    private final static int REQUEST_FIRST_CHECK_SETTINGS = 2500;

    double latitude, longitude;
    private String appliedJockey;
    private AlertDialog dialog;
    private RecentlyPlayedResponse recentlyPlayedResponse;
    private LinearLayout moretrend;
    private String type;

    TextView tv_Vcorner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_layout);
        session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        jockey_id = Integer.parseInt(user.get(SessionManager.JOCKEY_ID));
        token = user.get(SessionManager.KEY_TOKEN);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && getApplicationContext().checkSelfPermission(
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && getApplicationContext().checkSelfPermission(
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }

        mps = MediaPlayerSingleton.getInstance(this);


        if (getIntent().getExtras() != null) {

            try {

                String value = getIntent().getExtras().getString("audio");

                Log.w(TAG, "value " + value);

                // String audio = getIntent().getExtras().getString("audio");
                if (value != null) {
                    playSongFromNotification(value);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }


        GPSTracker gps = new GPSTracker(this);
        latitude = gps.getLatitude();
        longitude = gps.getLongitude();
        Log.e("lat", String.valueOf(latitude));
        Log.e("long", String.valueOf(longitude));
//        Log.e("token", token);
        session.checkLogin();
        if (session.isLoggedIn()) {

        } else {
            DialogMethod();
            // showDialogMethod();
        }
        initUI();
        spinner.setVisibility(View.VISIBLE);
        if (!App.appUtils.isNetAvailable()) {
            //spinner.setVisibility(View.GONE);
            alertUserP(Dashboard.this, "Connection Error", "No Internet connection available", "OK");
        } else {

            getVersionName();
//            if (flag.equals("BV")){
//
//            }else{
            getPopupImage();
//            }
            type = "in";
            sendUserLogAPI(type);
            dataIntegration();
        }


        registerReceiver();

    }//end of oncreate


    private void playSongFromNotification(String audio) {
        try {
            JSONArray array = new JSONArray(audio);
            JSONObject obj = array.getJSONObject(0);
            String description = obj.getString("discription");
            int jockeyId = obj.getInt("jockey_id");
            String title = obj.getString("title");
            String audioId = obj.getString("audio_id");
            String image = obj.getString("image_path");
            String name = obj.getString("name");
            String audioPath = obj.getString("audio_path");


            playSong("Notification", audioPath, name, image, title, 0, "empty", audioId);
            setCompleteListener();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.w(TAG, "on new intent called ");

        try {
            if (intent.getExtras() != null) {
                String type = intent.getExtras().getString("type");
                if (type.equalsIgnoreCase("song")) {
                    String audio = intent.getExtras().getString("data");

                    playSongFromNotification(audio);

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void sendUserLogAPI(String type) {
        Integer id = Integer.valueOf(jockey_id);

        JSONObject json = new JSONObject();
        try {
            json.put("jockey_id", id);
            json.put("type", type);
            Log.e("LogggggggAPI", String.valueOf(json));
        } catch (JSONException e) {
            Log.e("Exception ", e.toString());
        }

        RequestBody body = RequestBody.create(MediaType.parse("application/json"), String.valueOf(json));

        //Creating an object of our api interface
        apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<UserLogResponse> call = apiInterface.UserLogRequestTask(body);

        call.enqueue(new Callback<UserLogResponse>() {
            @Override
            public void onResponse(Call<UserLogResponse> call, Response<UserLogResponse> response) {
                UserLogResponse recourse = response.body();
                if (recourse != null) {

                }
            }

            @Override
            public void onFailure(Call<UserLogResponse> call, Throwable t) {

            }
        });

    }

    private void getVersionName() {
        try {

            token = FirebaseInstanceId.getInstance().getToken();

            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            appVersionName = pInfo.versionName;
            int verCode = pInfo.versionCode;
            Log.e("versionName,verCode", appVersionName + verCode);
            updateToken(token, appVersionName);

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (myReceiver != null) {
            unregisterReceiver(myReceiver);
        }

        type = "out";
        sendUserLogAPI(type);
        clearNotification();
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
                         String subType, String audioid) {
        Log.e(TAG, "sub type " + subType);
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

    private void APIrecent(String audioid) {
        Integer id = Integer.valueOf(jockey_id);
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

    private void registerReceiver() {
        myReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                Log.w(TAG, "broad cast receiver calling " + intent.getExtras().getString("status"));

                if (intent.getExtras().getString("status").equalsIgnoreCase("playing")) {
                    imgMiniPlay.setImageDrawable(ContextCompat.getDrawable(Dashboard.this, R.drawable.ic_pause));
                 /*   play.setImageResource(R.drawable.pause_blue);
                    mps.mp.start();
                    setStatus("playing");*/
//                    buildNotification(MediaPlayerService.ACTION_PLAY);


                } else if (intent.getExtras().getString("status").equalsIgnoreCase("pause")) {
                    /*play.setImageResource(R.drawable.play_blue);
                    mps.mp.pause();
                    setStatus("pause");*/
                    //   buildNotification(MediaPlayerService.ACTION_PAUSE);
                    imgMiniPlay.setImageDrawable(ContextCompat.getDrawable(Dashboard.this, R.drawable.ic_play));

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

    public void getPopupImage() {

        spinner.setVisibility(View.VISIBLE);
        //Creating an object of our api interface
        apiInterface = APIClient.getClient().create(APIInterface.class);

        Call<GetPopupDataRequest> call = apiInterface.getpopupImageData();
        //calling the api
        call.enqueue(new Callback<GetPopupDataRequest>() {
            @Override
            public void onResponse(Call<GetPopupDataRequest> call, Response<GetPopupDataRequest> response) {
                //hiding progress dialog
                spinner.setVisibility(View.GONE);
                //List<AudioFileUploadRequest> resource = response.body();
                Log.e("Successresponse", new Gson().toJson(response.body()));
                Log.e("response code", String.valueOf(response.code()));
                //Log.e("response", String.valueOf(resource));

                if (response.body() != null) {
                    if (response.body().getStatus().equals("Success")) {
                        GetPopupDataRequestdata = response.body();
                        imageList = GetPopupDataRequestdata.getResponse();
//                     loginStatus = response.body().getResponse().get(0).isLoginStatus();
                        Log.e("loginStatus", String.valueOf(loginStatus));
                        // popupLoginCheck();
                        if (session.isLoggedIn()) {
                            for (int i = 0; i < imageList.size(); i++) {
                                Boolean Login = imageList.get(i).isLoginStatus();
                                if (Login) {
                                    popup_image_path = imageList.get(i).getImage_path();
                                    if (imageList.get(i).isOpen()) {
                                        popupDialogMethod(popup_image_path);
                                    }

                                }
                            }

                        } else if (!session.isLoggedIn()) {
                            for (int i = 0; i < imageList.size(); i++) {
                                Boolean Login = imageList.get(i).isLoginStatus();
                                if (!Login) {
                                    popup_image_path = imageList.get(i).getImage_path();
                                    if (imageList.get(i).isOpen()) {
                                        popupDialogMethod(popup_image_path);
                                    }
                                }
                            }
                        }

                    } else {

                    }
                }
            }

            @Override
            public void onFailure(Call<GetPopupDataRequest> call, Throwable t) {
                spinner.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void showDialogMethod() {
        AlertDialog alertDialog = new AlertDialog.Builder(Dashboard.this).create();
        //alertDialog.setTitle(title);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setMessage("To Enjoy Voxit Audio talks, Please Login or create an Account");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Continue",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // dialog.cancel();
                        Intent i = new Intent(Dashboard.this, LoginActivity.class);
                        startActivity(i);
                    }
                });
        alertDialog.show();

    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void popupDialogMethod(String image_path) {
        Log.e("url", image_path);

        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.popup_layout, null);

//        mSmileRating = (SmileRating)alertLayout.findViewById(R.id.ratingView);
        RelativeLayout relativeLayout = (RelativeLayout) alertLayout.findViewById(R.id.relativeLayout);
        ImageView imageView = (ImageView) alertLayout.findViewById(R.id.image);
        ImageView close_icon = (ImageView) alertLayout.findViewById(R.id.close_icon);

        final AlertDialog.Builder alert = new AlertDialog.Builder(Dashboard.this);

        alert.setView(alertLayout);
        alert.setCancelable(true);

        final AlertDialog dialog = alert.create();

        Glide.with(this).load(image_path).into(imageView);


        // dialog.setCanceledOnTouchOutside(false);
//        try {
//            URL url = new URL(image_path);
//            Bitmap bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
//            Drawable image = new BitmapDrawable(context.getResources(), bitmap);
//
//            relativeLayout.setBackground(image);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        dialog.setCanceledOnTouchOutside(true);
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();

            }
        });
        close_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();

            }
        });

        dialog.show();
    }

    public void DialogMethod() {
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.login_popup1, null);

//        mSmileRating = (SmileRating)alertLayout.findViewById(R.id.ratingView);
        RelativeLayout relativeLayout = (RelativeLayout) alertLayout.findViewById(R.id.relativeLayout);
        TextView msgTv = (TextView) alertLayout.findViewById(R.id.msgTv);
        Button continueBtn = (Button) alertLayout.findViewById(R.id.continueBtn);

        final AlertDialog.Builder alert = new AlertDialog.Builder(Dashboard.this);

        alert.setView(alertLayout);
        //  alert.setCancelable(true);
        alert.setNegativeButton("", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        })
                .setOnKeyListener(new DialogInterface.OnKeyListener() {
                    @Override
                    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP)
                            finish();
                        return false;
                    }
                });

        dialog = alert.create();

        dialog.setCanceledOnTouchOutside(false);


        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(Dashboard.this, LoginActivity.class);
                startActivity(i);
                // dialog.dismiss();

            }
        });

        dialog.show();
    }

    public void VersionDialogMethod() {
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.login_popup2, null);

        RelativeLayout relativeLayout = (RelativeLayout) alertLayout.findViewById(R.id.relativeLayout);
        TextView msgTv = (TextView) alertLayout.findViewById(R.id.msgTv);
        Button updateBtn = (Button) alertLayout.findViewById(R.id.updateBtn);

        final AlertDialog.Builder alert = new AlertDialog.Builder(Dashboard.this);

        alert.setView(alertLayout);
        alert.setNegativeButton("", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        })
                .setOnKeyListener(new DialogInterface.OnKeyListener() {
                    @Override
                    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP)
                            finish();
                        return false;
                    }
                });

        dialog = alert.create();
        dialog.setCanceledOnTouchOutside(false);


        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.triton.voxit"));
                startActivity(intent);
            }
        });

        dialog.show();
    }

    private void updateToken(String token, String appVersionName) {
//        pDialog.show();
        JSONObject json = new JSONObject();
        try {
            json.put("jockey_id", jockey_id);
            json.put("token", token);
            json.put("appVersionName", appVersionName);
            json.put("latitude", latitude);
            json.put("longitude", longitude);

            Log.e("jsonArray", String.valueOf(json));
        } catch (JSONException e) {
            Log.e("Exception ", e.toString());
        }
        // RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), String.valueOf(json));
        apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<NotificationToken> call = apiInterface.notificationTokenRequest(json.toString());

        call.enqueue(new Callback<NotificationToken>() {
            @Override
            public void onResponse(Call<NotificationToken> call, Response<NotificationToken> response) {

                NotificationToken resource = response.body();
                Log.e("update status", String.valueOf(resource));
                if (resource != null) {
                    if (resource.getMust()) {
                        VersionDialogMethod();
                    }
                }

            }

            @Override
            public void onFailure(Call<NotificationToken> call, Throwable t) {
//                pDialog.dismiss();
                // showDialogMethod("Alert","Bad Network..");
            }
        });
    }

    private void dataIntegration() {

        apiInterface = APIClient.getClient().create(APIInterface.class);
        int jockeyid = 0;

        JSONObject json = new JSONObject();
        try {
            //dashboard 2
//            json.put("jockey_id", jockey_id);
            //dashboard 1
            json.put("jockey_id", 0);

        } catch (JSONException e) {
            Log.e("Exception ", e.toString());
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), String.valueOf(json));

        Call<DashboardResponsebean> call = apiInterface.DashboardRequestTask(body);

        Log.e("dashboard", call.request().url().toString());
        call.enqueue(new Callback<DashboardResponsebean>() {
            @Override
            public void onResponse(Call<DashboardResponsebean> call, Response<DashboardResponsebean> response) {
                Dashboardresponsebean = response.body();

                spinner.setVisibility(View.GONE);
                if (Dashboardresponsebean != null) {
                    Status = Dashboardresponsebean.getStatus();

                    if (Status.equals("Success")) {
                        dashdataResponse = Dashboardresponsebean.getResponse();

                        for (int i = 0; i < dashdataResponse.size(); i++) {
                            bannerResponse = dashdataResponse.get(i).getBanner();
                            tlresponse = dashdataResponse.get(i).getT_L();
                            genreResponse = dashdataResponse.get(i).getGenre();
                            topgenreResponse = dashdataResponse.get(i).getTopGenre();
                            Log.e("top size", String.valueOf(topgenreResponse.size()));
                            AudioFileUpload.BANNERLIST = bannerResponse;

                            if (bannerResponse != null) {
                                viewpageData(bannerResponse);
                            }
                            if (tlresponse != null) {
                                for (int j = 0; j < tlresponse.size(); j++) {
                                    trendingResponse = tlresponse.get(j).getTrending();
                                    liveResponse = tlresponse.get(j).getLive();
                                }
                                setTrendingAdapter(trendingResponse);
                            }
                            if (genreResponse != null) {
                                setGenreAdapter(genreResponse);
                            }
                            if (topgenreResponse != null) {
                                setTopGenreAdapter(topgenreResponse);
                            }

                        }

                    } else if (Status.equals("Failure")) {
                        showDialogMethod(Status, Status);
                    }

                } else {
                    showDialogMethod("Alert", "Data Empty");
                }
            }

            @Override
            public void onFailure(Call<DashboardResponsebean> call, Throwable t) {

                spinner.setVisibility(View.GONE);
            }
        });

    }

    private void viewpageData(final ArrayList<BannerResponseBean> bannerResponse) {
        tabLayout.setupWithViewPager(viewPager, true);

        viewPagerAdapter = new ViewPagerAdapter(this, bannerResponse);
//        viewPagerAdapter = new ViewPagerAdapter(this, slideImageUrls);
        viewPager.setAdapter(viewPagerAdapter);
        /*After setting the adapter use the timer */
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == bannerResponse.size()) {
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

    public void setTrendingAdapter(ArrayList<TrendingResponseBean> trendingResponse) {
//        if (!App.appUtils.isNetAvailable()) {
//            alertUserP(Dashboard.this, "Connection Error", "No Internet connection available", "OK");
//        }else {
//            if(trendingResponse.size()>0){
//                recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
//                //add space item decoration and pass space you want to give
//                recyclerView.addItemDecoration(new EqualSpacingItemDecoration(15));
//                //finally set adapter
//                recyclerView.setAdapter(new DasboardAdapter(trendingResponse,this));
//            }else {
//
//            }
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        //add space item decoration and pass space you want to give
        recyclerView.addItemDecoration(new EqualSpacingItemDecoration(15));
        //finally set adapter
        recyclerView.setAdapter(new DasboardAdapter(trendingResponse, this, Dashboard.this));
        recyclerView.setMotionEventSplittingEnabled(false);

    }

    public void setGenreAdapter(ArrayList<GenreResponseBean> genreResponse) {
        genre_recycler_view.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        genre_recycler_view.setAdapter(new GenreAdapter(genreResponse, this));
        genre_recycler_view.setMotionEventSplittingEnabled(false);
    }

    public void setTopGenreAdapter(ArrayList<TopGenreResponseBean> topgenreResponse) {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 1, LinearLayoutManager.VERTICAL, false);
        comedy_recycler_view.setLayoutManager(gridLayoutManager);
        comedy_recycler_view.setAdapter(new TopGenreMultiListAdapter(topgenreResponse, this, Dashboard.this));
        comedy_recycler_view.setMotionEventSplittingEnabled(false);
    }

//    public void setTopGenreAdapter1(ArrayList<TopGenreResponseBean> topgenreResponse){
//        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(),1,LinearLayoutManager.VERTICAL,false);
//        comedy_recycler_view.setLayoutManager(gridLayoutManager);
//        comedy_recycler_view.setAdapter(new TopGenreMultiListAdapter(LIST,this,flag));
//    }


    private void clearMiniPLayer() {
        miniPlayerLayout.setVisibility(View.GONE);
        clearMediaPLayer();
        clearNotification();
    }

    private void clearMediaPLayer() {
        mps.releasePlayer();
        mps.setType("empty");
        mps.setMediaPlayerStatus("empty");
        mps.setSubType("empty");
        mps.setAuthorName("empty");
        setStatus("empty");
    }

    ArrayList<AudioDetailsResponseBean> list = new ArrayList<>();
    String subType;

    private int findCurrentPlayPos() {

        int pos = mps.getCurrentPlayPos();
        String type = mps.getType();
        switch (type) {
            case "Trending":
                for (int i = 0; i <= trendingResponse.size() - 1; i++) {
                    TrendingResponseBean model = trendingResponse.get(i);
                    if (model.getAudio_path().equalsIgnoreCase(mps.getFileName())) {
                        pos = i;
                        break;
                    }
                }

                break;
            case "JockeyList":
                audioListData = mps.getJockeyListData();
                for (int i = 0; i <= audioListData.size() - 1; i++) {
                    AudioListData model = audioListData.get(i);
                    if (model.getAudio_path().equalsIgnoreCase(mps.getFileName())) {
                        pos = i;
                        break;
                    }
                }

                break;
            case "Search":


                for (int i = 0; i <= mps.getSearchList().size() - 1; i++) {
                    SearchData model = mps.getSearchList().get(i);
                    if (model.getAudio_path().equalsIgnoreCase(mps.getFileName())) {
                        pos = i;
                        break;
                    }
                }
                break;

            case "TopGenre":


                for (int i = 0; i <= topgenreResponse.size() - 1; i++) {

                    Log.w(TAG, "subType " + mps.getSubType());

                    if (topgenreResponse.get(i).getGenre().equalsIgnoreCase(mps.getSubType())) {
                        list = topgenreResponse.get(i).getAudiodetails();
                        subType = topgenreResponse.get(i).getGenre();
                        break;
                    }
                }

                for (int i = 0; i <= list.size() - 1; i++) {
                    AudioDetailsResponseBean data = list.get(i);

                    if (data.getAudio_path().equalsIgnoreCase(mps.getFileName())) {
                        pos = i;
                        break;
                    }
                }
                break;


        }

        return pos;
    }

    private void playNextSong() {

        String type = mps.getType();
        switch (type) {
            case "Trending":
                if (mps.getCurrentPlayPos() != -1) {
                    int pos = findCurrentPlayPos();
                    if ((pos + 1) < trendingResponse.size()) {
                        try {
                            pos += 1;
                            TrendingResponseBean model = trendingResponse.get(pos);
                            playSong("Trending", model.getAudio_path(), model.getName(), model.getImage_path(), model.getTitle(), pos, "empty", model.getAudio_id());
                        } catch (IndexOutOfBoundsException e) {
                            clearMiniPLayer();
                        }

                    } else {
                        clearMiniPLayer();
                    }

                }

                break;
            case "JockeyList":

                if (mps.getCurrentPlayPos() != -1) {
                    int pos = findCurrentPlayPos();
                    if ((pos + 1) < audioListData.size()) {

                        try {
                            pos += 1;
                            AudioListData model = audioListData.get(pos);
                            playSong("JockeyList", model.getAudio_path(), model.getName(), model.getImage_path(), model.getTitle(), pos, "empty", model.getAudio_id() + "");

                        } catch (IndexOutOfBoundsException e) {

                        }
                    } else {
                        clearMiniPLayer();
                    }
                }

                break;
            case "Search":

                if (mps.getCurrentPlayPos() != -1) {
                    int pos = findCurrentPlayPos();

                    if ((pos + 1) < mps.getSearchList().size()) {

                        try {
                            pos += 1;
                            SearchData model = mps.getSearchList().get(pos);
                            playSong("Search", model.getAudio_path(), model.getName(), model.getImage_path(), model.getTitle(), pos, "empty", model.getAudio_id() + "");

                        } catch (IndexOutOfBoundsException e) {
                            clearMiniPLayer();
                        }
                    } else {
                        clearMiniPLayer();
                    }

                }

                break;

            case "TopGenre":

                if (mps.getCurrentPlayPos() != -1) {
                    int pos = findCurrentPlayPos();

                    if ((pos + 1) < list.size()) {
                        pos += 1;
                        try {
                            AudioDetailsResponseBean model = list.get(pos);
                            Log.w(TAG, "sub type " + subType);
                            playSong("TopGenre", model.getAudio_path(), model.getName(), model.getImage_path(), model.getTitle(), pos, subType, model.getAudio_id() + "");
                        } catch (IndexOutOfBoundsException e) {
                            clearMiniPLayer();
                        }

                    } else {
                        clearMiniPLayer();
                    }
                }


                break;

            default:
                clearMiniPLayer();

        }
    }

    private void setCompleteListener() {
        if (mps.mp != null) {
            mps.mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    // imgMiniPlay.setImageResource(R.drawable.play_blue);

                    if (mps.mp != null) {

                        imgMiniPlay.setImageResource(R.drawable.ic_play);

                        Log.w(TAG, "song completed");
                        // playMethod(songIndex + 1);

                        playNextSong();


                    }


                }
            });
        }
    }

    private void initUI() {
        bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottom_navigation);
        bottomNavigationView.getMenu().findItem(R.id.home).setChecked(true);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        genre_recycler_view = (RecyclerView) findViewById(R.id.genre_recycler_view);
        comedy_recycler_view = (RecyclerView) findViewById(R.id.comedy_recycler_view);
//        filmy_recycler_view = (RecyclerView)findViewById(R.id.filmy_recycler_view);
        languageTv = (LinearLayout) findViewById(R.id.tvLanguage);
        searchImg = (ImageView) findViewById(R.id.search_img);
        genreTv = (LinearLayout) findViewById(R.id.tvgenre);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeToRefresh);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.blue);
        languageTv.setOnClickListener(this);
        genreTv.setOnClickListener(this);
        searchImg.setOnClickListener(this);
        spinner = (ProgressBar) findViewById(R.id.progressBar1);
        viewPager = (ViewPager) findViewById(R.id.pager);
        tabLayout = (TabLayout) findViewById(R.id.tabDots);
        moretrend = (LinearLayout) findViewById(R.id.moretrend);

        //test
//        Intent intent = getIntent();
//        flag = intent.getStringExtra("flag");

        miniPlayerLayout = (RelativeLayout) findViewById(R.id.miniPlayerLayout);
        imgClose = (ImageView) findViewById(R.id.imgClose);
        imgMiniPlay = (ImageView) findViewById(R.id.imgMiniPlay);
        txtSongName = (TextView) findViewById(R.id.txtSongName);
        txtSongName.setSelected(true);
        txtTypeName = (TextView) findViewById(R.id.typeName);
        imgSong = (ImageView) findViewById(R.id.imgSong);
        txtAuthorName = (TextView) findViewById(R.id.txtAuthorName);

        tv_Vcorner = (TextView) findViewById(R.id.tvVcorner);

        tv_Vcorner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), VcornerActivity.class));
            }
        });

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
                    imgMiniPlay.setImageDrawable(ContextCompat.getDrawable(Dashboard.this, R.drawable.ic_play));
                    buildNotification(MediaPlayerService.ACTION_PAUSE);


                } else {
                    mps.setMediaPlayerStatus("playing");
                    imgMiniPlay.setImageDrawable(ContextCompat.getDrawable(Dashboard.this, R.drawable.ic_pause));
                    mps.mp.start();
                    buildNotification(MediaPlayerService.ACTION_PLAY);
                }
            }
        });

        miniPlayerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (mps.getType()) {

                    case "Trending":

                        if (mps.getCurrentPlayPos() != -1 && mps.getType().equals("Trending")) {
                            TrendingResponseBean model = trendingResponse.get(mps.getCurrentPlayPos());
                            startActivity(new Intent(Dashboard.this, AudioActivity.class)
                                    .putExtra("jockey_id", String.valueOf(model.getJockey_id()))
                                    .putExtra("song", model.getAudio_path())
                                    .putExtra("title", model.getTitle())
                                    .putExtra("description", model.getDiscription())
                                    .putExtra("image", model.getImage_path())
                                    .putExtra("name", model.getName())
                                    .putExtra("audio_length", model.getAudio_length())
                                    .putExtra("audio_id", model.getAudio_id())
                                    .putExtra("type", "Trending")
                                    .putExtra("songsList", trendingResponse)
                                    .setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
                        }

                        Log.w(TAG, "trendng clicks");
                        break;

                    case "JockeyList":
                        Log.w(TAG, "jockey list");

                        if (mps.getCurrentPlayPos() != -1 && mps.getType().equals("JockeyList")) {

                            audioListData = mps.getJockeyListData();
                            if (audioListData.size() > 0) {
                                AudioListData model = audioListData.get(mps.getCurrentPlayPos());
                                startActivity(new Intent(Dashboard.this, AudioActivity.class)
                                        .putExtra("jockey_id", String.valueOf(model.getJockey_id()))
                                        .putExtra("song", model.getAudio_path())
                                        .putExtra("title", model.getTitle())
                                        .putExtra("description", model.getDiscription())
                                        .putExtra("image", model.getImage_path())
                                        .putExtra("name", model.getName())
                                        .putExtra("audio_length", model.getAudio_length())
                                        .putExtra("audio_id", model.getAudio_id() + "")
                                        .putExtra("type", "JockeyList")
                                        .putExtra("songsList", audioListData)
                                        .setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
                            }
                        }
                        break;

                    case "Search":
                        Log.w(TAG, "search");
                        if (mps.getCurrentPlayPos() != -1 && mps.getType().equals("Search")) {

                            if (mps.getSearchList().size() > 0) {
                                SearchData model = mps.getSearchList().get(mps.getCurrentPlayPos());
                                startActivity(new Intent(Dashboard.this, AudioActivity.class)
                                        .putExtra("jockey_id", String.valueOf(model.getJockey_id()))
                                        .putExtra("song", model.getAudio_path())
                                        .putExtra("title", model.getTitle())
                                        .putExtra("description", model.getDiscription())
                                        .putExtra("image", model.getImage_path())
                                        .putExtra("name", model.getName())
                                        .putExtra("audio_length", model.getAudio_length())
                                        .putExtra("audio_id", model.getAudio_id())
                                        .putExtra("type", "Search")
                                        .putExtra("songsList", mps.getSearchList())
                                        .setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
                            }
                        }
                        break;

                    case "TopGenre":
                        Log.w(TAG, "top genre");
                        if (mps.getCurrentPlayPos() != -1 && mps.getType().equals("TopGenre")) {
                            ArrayList<AudioDetailsResponseBean> list = new ArrayList<>();
                            for (int i = 0; i <= topgenreResponse.size() - 1; i++) {

                                if (topgenreResponse.get(i).getGenre().equalsIgnoreCase(mps.getSubType())) {
                                    list = topgenreResponse.get(i).getAudiodetails();
                                    subType = topgenreResponse.get(i).getGenre();
                                    break;
                                }
                            }


                            Log.w(TAG, "current play pos " + mps.getCurrentPlayPos() + " type " + list.size() + " sub type " + mps.getSubType());

                            if (list.size() > 0) {

                                AudioDetailsResponseBean data = list.get(mps.getCurrentPlayPos());
                                startActivity(new Intent(Dashboard.this, AudioActivity.class)
                                        .putExtra("jockey_id", String.valueOf(data.getJockey_id()))
                                        .putExtra("song", data.getAudio_path())
                                        .putExtra("title", data.getTitle())
                                        .putExtra("description", data.getDiscription())
                                        .putExtra("image", data.getImage_path())
                                        .putExtra("name", data.getName())
                                        .putExtra("audio_length", data.getAudio_length())
                                        .putExtra("audio_id", data.getAudio_id())
                                        .putExtra("type", "TopGenre")
                                        .putExtra("songsList", list)
                                        .putExtra("subType", subType)
                                        .setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
                            }


                            // TopGenreResponseBean model = topgenreResponse.get(mps.getCurrentPlayPos());

                           /* AudioDetailsResponseBean data = TopListgenreResponse.get(mps.getCurrentPlayPos());

//                            AudioDetailsResponseBean
                            startActivity(new Intent(Dashboard.this, AudioActivity.class)
                                    .putExtra("jockey_id", String.valueOf(data.getJockey_id()))
                                    .putExtra("song", data.getAudio_path())
                                    .putExtra("title", data.getTitle())
                                    .putExtra("description", data.getDiscription())
                                    .putExtra("image", data.getImage_path())
                                    .putExtra("name", data.getName())
                                    .putExtra("audio_length", data.getAudio_length())
                                    .putExtra("audio_id", data.getAudio_id())
                                    .putExtra("type", "TopGenre")
                                    .putExtra("songsList", TopListgenreResponse)
                                    .setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));*/
                        }
                        break;


                }
            }
        });

        navigationMenu();
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!App.appUtils.isNetAvailable()) {
                    alertUserP(Dashboard.this, "Connection Error", "No Internet connection available", "OK");
                    mSwipeRefreshLayout.setRefreshing(false);
                } else {
                    dataIntegration();
                    trendingShuffle();
                    generShuffle();
                    topGenereShuffle();
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            }
        });
    }

    private void topGenereShuffle() {
        if (trendingResponse != null && trendingResponse.size() > 0) {
            GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 1, LinearLayoutManager.VERTICAL, false);
            comedy_recycler_view.setLayoutManager(gridLayoutManager);
            comedy_recycler_view.setAdapter(new TopGenreMultiListAdapter(topgenreResponse, this, Dashboard.this));

        } else {

        }
        // Collections.shuffle(topgenreResponse, new Random(System.currentTimeMillis()));
    }

    private void generShuffle() {
        if (trendingResponse != null && trendingResponse.size() > 0) {
            // Collections.shuffle(genreResponse, new Random(System.currentTimeMillis()));
            genre_recycler_view.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            genre_recycler_view.setAdapter(new GenreAdapter(genreResponse, this));
        } else {

        }
    }

    public void trendingShuffle() {
        if (trendingResponse != null && trendingResponse.size() > 0) {
            // Collections.shuffle(trendingResponse, new Random(System.currentTimeMillis()));
            recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            //add space item decoration and pass space you want to give
            recyclerView.addItemDecoration(new EqualSpacingItemDecoration(15));
            //finally set adapter
            recyclerView.setAdapter(new DasboardAdapter(trendingResponse, this, Dashboard.this));

        } else {
        }
    }

    private void navigationMenu() {
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                switch (item.getItemId()) {

                    case R.id.home:
                        // Switch to page one
                        break;
                    case R.id.notifi:
                        Intent j = new Intent(Dashboard.this, NotificationActivity.class);
                        // j.putExtra("login",flag);
                        startActivity(j);
                        break;
                    case R.id.search:
                        if (!App.appUtils.isNetAvailable()) {
                            alertUserP(Dashboard.this, "Connection Error", "No Internet connection available", "OK");
                        } else {
                            Intent i4 = new Intent(Dashboard.this, SearchActivity.class);
                            // i4.putExtra("login",flag);
                            startActivity(i4);
                        }
                        break; // break
                    case R.id.profile:

                        Intent i = new Intent(Dashboard.this, ProfileActivity.class);
                        // i.putExtra("login",flag);
                        startActivity(i);

                        // Switch to page three
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
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.tvLanguage:
                if (!App.appUtils.isNetAvailable()) {
                    alertUserP(Dashboard.this, "Connection Error", "No Internet connection available", "OK");
                } else {
                    Intent intent = new Intent(this, Language.class);
                    //intent.putExtra("login",flag);
                    startActivity(intent);
                    break; // break is optional
                }

            case R.id.tvgenre:
                if (!App.appUtils.isNetAvailable()) {
                    alertUserP(Dashboard.this, "Connection Error", "No Internet connection available", "OK");
                } else {
                    Intent i = new Intent(this, GenreActivity.class);
                    // i.putExtra("login",flag);
                    startActivity(i);
                }
                break; // break is optional
            case R.id.search_img:
                if (!App.appUtils.isNetAvailable()) {
                    alertUserP(Dashboard.this, "Connection Error", "No Internet connection available", "OK");
                } else {
                    Intent i1 = new Intent(this, SearchActivity.class);
                    //i1.putExtra("login",flag);
                    startActivity(i1);
                }
                break; // break

            default:
                // Statements
        }
    }

    private void showDialogMethod(String title, String message) {
//        KeyboardHide();
        AlertDialog alertDialog = new AlertDialog.Builder(Dashboard.this).create();
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

    private String TAG = "Dashboard";

    @Override
    protected void onResume() {
        super.onResume();
        bottomNavigationView.getMenu().getItem(0).setChecked(true);
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


            setCompleteListener();

        }

        // mAdapter.setCanStart(true);
    }

    private void loadImageForMiniPlayer() {
        Glide.with(this).load(mps.getImageUrl()).into(imgSong);
    }


    @Override
    public void onBackPressed() {
//        mps = MediaPlayerSingleton.getInstance(this);
//        registerReceiver();
        mps.releasePlayer();
        super.onBackPressed();
        type = "out";
        sendUserLogAPI(type);

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


}
