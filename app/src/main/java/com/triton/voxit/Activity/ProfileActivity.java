package com.triton.voxit.Activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.google.gson.Gson;
import com.triton.voxit.Api.APIClient;
import com.triton.voxit.Api.APIInterface;
import com.triton.voxit.Api.RetrofitClient;
import com.triton.voxit.R;
import com.triton.voxit.SessionManager.SessionManager;
import com.triton.voxit.Utlity.MediaPlayerSingleton;
import com.triton.voxit.Utlity.Utilitys;
import com.triton.voxit.apputils.ImageFilePath;
import com.triton.voxit.apputils.RestUtils;
import com.triton.voxit.apputils.ScalingUtilities;
import com.triton.voxit.requestpojo.UpdateJockeyProfilePicRequest;
import com.triton.voxit.responsepojo.ImageFileUploadResponse;
import com.triton.voxit.responsepojo.UpdateJockeyProfilePicResponse;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends NavigationDrawer implements View.OnClickListener {

    private Button logout, reset, referral_code, my_rewards;
    private static final String PREFERENCES = "";
    private SharedPreferences.Editor LoginString;
    private boolean isLogged;
    private BottomNavigationView bottomNavigationView;
    private TextView headertitle;
    String flag, userChoosenTask;
    SessionManager session;
    String name, email, phoneNo, image_url,jockey_id;
    TextView nameTv, emailTv, phoneNoTv, helpTv;
    ImageView  editProfileTv;
    CircleImageView profile_image;
    private int SELECT_PIC = 100;
    private static final int RESULT_CAMERA = 1;
    private static final int RESULT_READ_EXE = 2;
    private ArrayList<String> ASSERT_IMG = new ArrayList<String>();
    private static String PROGRESS_MSG = "Loading...";
    private static ProgressDialog pdialog;
    private boolean isImageTaken = false;
    private BroadcastReceiver myReceiver;
    private MediaPlayerSingleton mps;
    private MainActivity mp;

    private Uri profile_image_uri = null;
    private final int PICK_IMAGE_CAMERA = 1, PICK_IMAGE_GALLERY = 2;
    private String profile_image_url = "";
    private static final int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 989;

    String ProfilePic;

    String strMyImagePath = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        name = user.get(SessionManager.KEY_USER_NAME);
        email = user.get(SessionManager.KEY_EMAIL);
        phoneNo = user.get(SessionManager.KEY_MOBILE);
        image_url = user.get(SessionManager.IMAGE_PATH);
        jockey_id = user.get(SessionManager.JOCKEY_ID);
        Log.e("user details", name + email + phoneNo + image_url);
        hideLanguageView();
        showTitleView();
        initVIews();
        onclickMethod();
    }

    private void onclickMethod() {
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LoginManager.getInstance().logOut();
                session.logoutUser();


                MediaPlayerSingleton mps = MediaPlayerSingleton.getInstance(ProfileActivity.this);
                mps.releasePlayer();

                mps.setAuthorName("empty");
                mps.setSubType("empty");
                mps.setMediaPlayerStatus("empty");
                mps.setCurrentPlayPos(-1);
                mps.setImageUrl("empty");
                mps.setFileName("empty");

                if (myReceiver != null) {
                    unregisterReceiver(myReceiver);
                }

                clearNotification();

            }
        });
    }

    private void clearNotification() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(1);
    }

    private void initVIews() {
        profile_image = (CircleImageView) findViewById(R.id.itemImage);
        editProfileTv = (ImageView) findViewById(R.id.editTv);
        logout = (Button) findViewById(R.id.logout);
        reset = (Button) findViewById(R.id.reset);
        referral_code = (Button) findViewById(R.id.referral_code);
        my_rewards = (Button) findViewById(R.id.my_rewards);
        nameTv = (TextView) findViewById(R.id.name);
        emailTv = (TextView) findViewById(R.id.email);
        phoneNoTv = (TextView) findViewById(R.id.phone_no);
        headertitle = (TextView) findViewById(R.id.header_title);
//        helpTv = (TextView) findViewById(R.id.help);
//        helpTv.setOnClickListener(this);
        headertitle.setText("Profile");
        bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottom_navigation);
        bottomNavigationView.getMenu().findItem(R.id.profile).setChecked(true);

        if (session.isLoggedIn()) {

        } else {
            startActivity(new Intent(this, LoginActivity.class)
                    .setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
        }

        nameTv.setText(name);
        emailTv.setText(email);
        phoneNoTv.setText(phoneNo);
        // Glide.with(this).load(image_url).into(profileImg);
        navigationMenu();
        reset.setOnClickListener(this);
        referral_code.setOnClickListener(this);
        my_rewards.setOnClickListener(this);
        editProfileTv.setOnClickListener(this);

        if(!image_url.isEmpty()){
            Glide.with(this)
                    .load(image_url)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .error(R.drawable.logo_white)
                    .into(profile_image);
        }
    }

    private void navigationMenu() {
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                switch (item.getItemId()) {
                    case R.id.home:
                        startActivity(new Intent(ProfileActivity.this, Dashboard.class)
                                .setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
                        finish();
                        break;
                    case R.id.notifi:
                        Intent j = new Intent(ProfileActivity.this, NotificationActivity.class);
                        // j.putExtra("login",flag);
                        startActivity(j);
                        break;
                    case R.id.search:
                        Intent i4 = new Intent(ProfileActivity.this, SearchActivity.class);
                        // i4.putExtra("login",flag);
                        startActivity(i4);
                        break;
                    case R.id.profile:
//                        Intent i = new Intent(ProfileActivity.this,ProfileActivity.class);
//                       // i.putExtra("login",flag);
//                        startActivity(i);
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
            case R.id.reset:
                gotoResetPwd();
                break;
            case R.id.referral_code:
                gotoRefCode();
                break;
            case R.id.my_rewards:
                gotoMyRewards();
                break;
//            case R.id.help:
//                Intent i = new Intent(ProfileActivity.this,HelpSupport.class);
//                startActivity(i);
//                break;
            case R.id.editTv:
//                if (read_write_permission == PackageManager.PERMISSION_GRANTED) {
//                    getGalleryImage();
//                } else {
//                    askReadPermision();
//                }
//                if (camera_permission != PackageManager.PERMISSION_GRANTED) {
//                    askCameraPermision();
//                } else {
//                    if (App.appUtils.isNetAvailable()) {
//                        openCamera();
//                    } else {
//                        Toast.makeText(ProfileActivity.this, "Photo not uploaded check Connectivity", Toast.LENGTH_SHORT).show();
//                    }
//                }
                selectProfileImage();

                /*if(profile_image_url != null && !profile_image_url.isEmpty()) {
                    uploadProfileImageRequest();

                }else{
                    ProfilePic = "";
                }*/


                break;
        }
    }

    private void gotoMyRewards() {
        Intent i = new Intent(this, MyRewardsActivity.class);
        startActivity(i);
    }

    private void gotoRefCode() {
        Intent i = new Intent(this, ReferralCodeActivity.class);
        startActivity(i);
    }

    private void gotoResetPwd() {
        Intent i = new Intent(this, ResetPasswordActivity.class);
        startActivity(i);
    }

    private void selectProfileImage() {
        final CharSequence[] options = {"Take Photo", "Choose From Gallery","Cancel"};
        android.app.AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
        builder.setTitle("Select Option");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    dialog.dismiss();
                    if (checkCameraPermissions()) {
                        if (getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)) {

                            File file = new File(Environment.getExternalStorageDirectory(), "Voxit" + System.currentTimeMillis() + ".png");
                            profile_image_uri = Uri.fromFile(file);

                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, profile_image_uri);

                            } else {
                                File file2 = new File(profile_image_uri.getPath());
                                Uri photoUri = FileProvider.getUriForFile(getApplicationContext(), getApplicationContext().getPackageName() + ".provider", file2);
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);

                            }
                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            if (intent.resolveActivity(getApplicationContext().getPackageManager()) != null) {
                                startActivityForResult(intent, PICK_IMAGE_CAMERA);
                            }
                        } else {
                            Toast.makeText(getApplication(), "Camera not supported", Toast.LENGTH_LONG).show();
                        }
                    }
                } else if(options[item].equals("Choose From Gallery")){
                    if (checkCameraPermissions()) {
                        dialog.dismiss();
                        Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(pickPhoto, PICK_IMAGE_GALLERY);
                    }
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }



            }
        });
        builder.show();


    }

    private boolean checkCameraPermissions() {
        List<String> permissionsNeeded = new ArrayList<String>();
        final List<String> permissionsList = new ArrayList<String>();
        if (!addPermission(permissionsList, Manifest.permission.CAMERA))
            permissionsNeeded.add("Camera");
        if (!addPermission(permissionsList, Manifest.permission.READ_EXTERNAL_STORAGE))
            permissionsNeeded.add("Read External Storage");
        if (!addPermission(permissionsList, Manifest.permission.WRITE_EXTERNAL_STORAGE))
            permissionsNeeded.add("Write External Storage");
        if (permissionsList.size() > 0) {
            if (permissionsNeeded.size() > 0) {
                // Need Rationale
                String message = "You need to grant access to " + permissionsNeeded.get(0);
                for (int i = 1; i < permissionsNeeded.size(); i++)
                    message = message + ", " + permissionsNeeded.get(i);
                showMessageOKCancel(message,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(ProfileActivity.this, permissionsList.toArray(new String[permissionsList.size()]), REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                            }
                        });
                return false;
            }
            ActivityCompat.requestPermissions(ProfileActivity.this, permissionsList.toArray(new String[permissionsList.size()]),
                    REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    private boolean addPermission(List<String> permissionsList, String permission) {
        if (ContextCompat.checkSelfPermission(ProfileActivity.this, permission) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(permission);
            // Check for Rationale Option
            if (!ActivityCompat.shouldShowRequestPermissionRationale(ProfileActivity.this, permission))
                return false;
        }
        return true;
    }



    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (ActivityCompat.checkSelfPermission(this, permissions[0]) == PackageManager.PERMISSION_GRANTED) {
            switch (requestCode) {
                case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS: {
                    Map<String, Integer> perms = new HashMap<String, Integer>();
                    // Initial
                    perms.put(Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);
                    perms.put(Manifest.permission.READ_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                    perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                    // Fill with results
                    for (int i = 0; i < permissions.length; i++)
                        perms.put(permissions[i], grantResults[i]);
                    if (perms.get(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                            && perms.get(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                            && perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        // All Permissions Granted
                    } else {
                        // Permission Denied
                        Toast.makeText(this, "Some Permission is Denied", Toast.LENGTH_SHORT).show();
                    }

                }
                break;


                default:
                    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        } else {
            Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PICK_IMAGE_CAMERA:
                profile_image_url = getRealPathFromURI(profile_image_uri);
                profile_image_url = decodeFile(profile_image_url, 500, 500);
                Glide.with(this)
                        .load(profile_image_url)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .centerCrop()
                        .into(profile_image);
                break;

            case PICK_IMAGE_GALLERY:
                String imgpath = ImageFilePath.getPath(this, data.getData());
                profile_image_url = decodeFile(imgpath, 500, 500);
                Glide.with(this)
                        .load(profile_image_url)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .centerCrop()
                        .into(profile_image);
                break;







        }


    }


    private String decodeFile(String path, int DESIREDWIDTH, int DESIREDHEIGHT) {

        Bitmap scaledBitmap = null;

        try {
            // Part 1: Decode image
            Bitmap unscaledBitmap = ScalingUtilities.decodeFile(path, DESIREDWIDTH, DESIREDHEIGHT, ScalingUtilities.ScalingLogic.FIT);

            if (!(unscaledBitmap.getWidth() <= DESIREDWIDTH && unscaledBitmap.getHeight() <= DESIREDHEIGHT)) {
                // Part 2: Scale image
                scaledBitmap = ScalingUtilities.createScaledBitmap(unscaledBitmap, DESIREDWIDTH, DESIREDHEIGHT, ScalingUtilities.ScalingLogic.FIT);
            } else {
                unscaledBitmap.recycle();
                return path;
            }

            // Store to tmp file

            String extr = Environment.getExternalStorageDirectory().toString();
            File mFolder = new File(extr + "/Voxit");
            if (!mFolder.exists()) {
                mFolder.mkdir();
            }

           String s = "tmp.png";

            Log.i("Test",extr+s);

            File f = new File(mFolder.getAbsolutePath(), s);

            strMyImagePath = f.getAbsolutePath();
            Log.i("strMyImagePath",strMyImagePath );

            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(f);
                scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 75, fos);
                fos.flush();
                fos.close();
            } catch (FileNotFoundException e) {

                e.printStackTrace();
            } catch (Exception e) {

                e.printStackTrace();
            }

            scaledBitmap.recycle();
        } catch (Throwable e) {
        }

        if (strMyImagePath == null) {
            return path;
        }

        if(strMyImagePath != null && !strMyImagePath.isEmpty()) {
            uploadProfileImageRequest();
            Log.i("ProfilePic",strMyImagePath );
        }else{
            ProfilePic = "";
        }


        return strMyImagePath;

    }

    public String getRealPathFromURI(Uri contentUri) {
        String uri = "" + contentUri;
        String path = uri.replace("file://", "");
        Log.i("getRealPathFromURI",uri+path);
        return path;
    }



    private void uploadProfileImageRequest(){
        //avi_indicator.setVisibility(View.VISIBLE);
       // avi_indicator.smoothToShow();
      APIInterface ApiService = RetrofitClient.getApiService();
        Call<ImageFileUploadResponse> call = ApiService.getImageStroeResponse(getProfileImagePicMultipart());
        call.enqueue(new Callback<ImageFileUploadResponse>() {
            @Override
            public void onResponse(Call<ImageFileUploadResponse> call, Response<ImageFileUploadResponse> response) {
                //avi_indicator.smoothToHide();
                Log.e("Profpic", "--->" + new Gson().toJson(response.body()));
                int status = response.body().getResponse().get(0).getStatus();
                if(status == 1){
                    if (response.body() != null) {
                        ProfilePic = response.body().getResponse().get(0).getFull_path();
                        Log.i("ProfilePic",ProfilePic );
                        getUpdateJockeyProfilePicRequest();
                    }

                }
            }

            @Override
            public void onFailure(Call<ImageFileUploadResponse> call, Throwable t) {
               // avi_indicator.smoothToHide();
                Log.e("ProfpicFlr", "--->" + t.getMessage());
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }
    private MultipartBody.Part getProfileImagePicMultipart() {
        RequestBody requestFile = RequestBody.create(MediaType.parse("text/plain"), "");
        if (strMyImagePath != null && !strMyImagePath.isEmpty()) {
            File file = new File(strMyImagePath);
            requestFile = RequestBody.create(MediaType.parse("image/*"), file);
        }
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("file", strMyImagePath, requestFile);
        Log.d("ImageStorepath", "getProfileMultiPartRequest: " + new Gson().toJson(filePart));

        Log.i("File",strMyImagePath);
        return filePart;
    }


    private void getUpdateJockeyProfilePicRequest() {
       // avi_indicator.setVisibility(View.VISIBLE);
      //  avi_indicator.smoothToShow();
        APIInterface ApiService = APIClient.getClient().create(APIInterface.class);
        Call<UpdateJockeyProfilePicResponse> call = ApiService.updateJockeyProfilePicResponseCall(RestUtils.getContentType(), updateJockeyProfilePicRequest());
        call.enqueue(new Callback<UpdateJockeyProfilePicResponse>() {
            @Override
            public void onResponse(Call<UpdateJockeyProfilePicResponse> call, Response<UpdateJockeyProfilePicResponse> response) {
               // avi_indicator.smoothToHide();
                Log.e("UpdateProfilePicRes", "--->" + new Gson().toJson(response.body()));

            }

            @Override
            public void onFailure(Call<UpdateJockeyProfilePicResponse> call, Throwable t) {
                //avi_indicator.smoothToHide();
                Log.e("UpdateProfilePicResflr", "--->" + t.getMessage());
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
    private UpdateJockeyProfilePicRequest updateJockeyProfilePicRequest() {
        UpdateJockeyProfilePicRequest updateJockeyProfilePicRequest = new UpdateJockeyProfilePicRequest();
        updateJockeyProfilePicRequest.setImage_path(ProfilePic);
        updateJockeyProfilePicRequest.setJockey_id(Integer.parseInt(jockey_id));
        Log.i("updateProfilePicRequest", "--->" + new Gson().toJson(updateJockeyProfilePicRequest));
        return updateJockeyProfilePicRequest;
    }
}
