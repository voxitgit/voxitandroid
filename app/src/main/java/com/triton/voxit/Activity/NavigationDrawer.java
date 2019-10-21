package com.triton.voxit.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.github.siyamed.shapeimageview.CircularImageView;
import com.triton.voxit.Api.APIClient;
import com.triton.voxit.Api.APIInterface;
import com.triton.voxit.R;
import com.triton.voxit.SessionManager.SessionManager;
import com.triton.voxit.model.AppliedJockeyResponse;
import com.triton.voxit.model.Genre;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NavigationDrawer extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {

    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    LayoutInflater inflater;
    View view,header;
    Toolbar toolbar;

    // Make sure to be using android.support.v7.app.ActionBarDrawerToggle version.
    // The android.support.v4.app.ActionBarDrawerToggle has been deprecated.
    private ActionBarDrawerToggle drawerToggle;
    ImageView drawerImg;
    CircleImageView nav_header_imageView;
    FrameLayout frameLayout;
    TextView header_title,nav_header_textView,nav_header_phone;
    LinearLayout languageLt,genreLt,menuLt;
    SessionManager session;
    String name,image_url,phoneNo,user_mode;
    private String appliedJockey;
    private String jockey_id;
    private APIInterface apiInterface;
    private AppliedJockeyResponse appliedJockeyResponse;
    ProgressDialog pDialog;

    TextView tvWelcomeName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme_NoActionBar);

        inflater = LayoutInflater.from(this);
        view = inflater.inflate(R.layout.navigation_drawer_layout, null);
        // header = inflater.inflate(R.layout.nav_header_main, null);

        session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        name = user.get(SessionManager.KEY_USER_NAME);
        user_mode = user.get(SessionManager.KEY_USER_MODE);
        phoneNo = user.get(SessionManager.KEY_MOBILE);
        image_url = user.get(SessionManager.IMAGE_PATH);
        jockey_id = user.get(SessionManager.JOCKEY_ID);
//        appliedJockey = user.get(SessionManager.APPLIED_JOCKEY);
//        Log.e("darwappliedJockey", appliedJockey);

        Log.e("user details",name + image_url + user_mode);

        initUI(view);
        initToolBar(view);
    }

    private void initUI(View view) {
        pDialog = new ProgressDialog(NavigationDrawer.this);
        pDialog.setMessage(NavigationDrawer.this.getString(R.string.please_wait));
        pDialog.setIndeterminate(true);
        pDialog.setCancelable(true);
        //Initializing NavigationView
        navigationView = (NavigationView) view.findViewById(R.id.nav_view);
        frameLayout = (FrameLayout) view.findViewById(R.id.base_container);
        navigationView.setNavigationItemSelectedListener(this);
        Menu menu =navigationView.getMenu();

        MenuItem target = menu.findItem(R.id.nav_item_seven);

        if(user_mode.equalsIgnoreCase("OL")){
            target.setVisible(true);
        }else {
            target.setVisible(false);
        }
        // Initializing Drawer Layout and ActionBarToggle
        drawerLayout = (DrawerLayout) view.findViewById(R.id.drawer_layout);
        header = navigationView.getHeaderView(0);
        nav_header_imageView = (CircleImageView) header.findViewById(R.id.nav_header_imageView);
        nav_header_textView = (TextView) header.findViewById(R.id.nav_header_textView);
        nav_header_phone = (TextView) header.findViewById(R.id.nav_header_phone);
       // Glide.with(this).load(image_url).into(nav_header_imageView);
        nav_header_textView.setText(name);
        nav_header_phone.setText(phoneNo);

        if(!image_url.isEmpty()){
            Glide.with(this)
                    .load(image_url)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .error(R.drawable.logo_white)
                    .into(nav_header_imageView);
        }
    }
//    private void hidedrawermenu(){
//        Menu menu = navigationView.getMenu();
//        menu.findItem(R.id.nav_item_seven).setVisible(false);
//    }
    private void initToolBar(View view) {
        toolbar = (Toolbar)view.findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerImg = (ImageView)toolbar.findViewById(R.id.tvHeaderIcon);
        header_title = (TextView)toolbar.findViewById(R.id.header_title);
        languageLt = (LinearLayout)toolbar.findViewById(R.id.tvLanguage);
        genreLt = (LinearLayout)toolbar.findViewById(R.id.tvgenre);
        menuLt = (LinearLayout)toolbar.findViewById(R.id.menu);
        tvWelcomeName = (TextView)toolbar.findViewById(R.id.tvWelcome);
        tvWelcomeName.setText("Welcome !"+"\t"+"\t"+name);

        //drawerImg.setOnClickListener(this);
        toggleView();
    }
    public void hideLanguageView() {
        languageLt.setVisibility(View.GONE);
        genreLt.setVisibility(View.GONE);
        menuLt.setVisibility(View.GONE);
    }
    public void showTitleView() {
        header_title.setVisibility(View.VISIBLE);
    }
    private void toggleView() {
        drawerImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.isClickable()) {
                    drawerMethod();
                } else {

                    Intent intent_re = getIntent();
                    overridePendingTransition(0, 0);
                    intent_re.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    finish();
                    overridePendingTransition(0, 0);
                    startActivity(intent_re);

                }
            }
        });
    }

    public void drawerMethod(){

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);

        } else {
            drawerLayout.openDrawer(GravityCompat.START);
        }

    }
    public void setContentView(int layoutId) {

        Log.e("BaseOncreate", "setContentView");
        View activityView = inflater.inflate(layoutId, null);
        frameLayout.addView(activityView);
        super.setContentView(view);
        //drawerMethod();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tvHeaderIcon:
                drawerMethod();
                break;
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        //Checking if the item is in checked state or not, if not make it in checked state
//        if (menuItem.isChecked()) menuItem.setChecked(false);
//        else menuItem.setChecked(true);

        //Closing drawer on item click
        drawerLayout.closeDrawers();

        //Check to see which item was being clicked and perform appropriate action
        switch (menuItem.getItemId()) {


            //Replacing the main content with ContentFragment Which is our Inbox View;
            case R.id.nav_item_one:
               gotoMyRewards();
                return true;

            // For rest of the options we just show a toast on click
            case R.id.nav_item_two:
                gotoRefCode();
                return true;

            case R.id.nav_item_genre:
                gotoGenre();
                return true;

            case R.id.nav_item_three:
               gotoHelpSupport();
                return true;

            case R.id.nav_item_four:
                gotoProfile();
                return true;

            case R.id.nav_item_five:
                gotoAboutUs();
                return true;

            case R.id.nav_item_six:
                gotoTerms();
                return true;
            case R.id.nav_item_seven:
//                gotoAudio();
                pDialog.show();
                goToAPI();
                return true;

            default:
                //Toast.makeText(getApplicationContext(), "Somethings Wrong", Toast.LENGTH_SHORT).show();
                return true;

        }

    }

    private void goToAPI() {
        Integer id = Integer.valueOf(jockey_id);
        JSONObject json = new JSONObject();
        try {
            json.put("jockey_id", id);
        }catch (JSONException e) {
                Log.e("Exception ", e.toString());
            }

        RequestBody body = RequestBody.create(MediaType.parse("application/json"), String.valueOf(json));
        Log.e("jcokey", String.valueOf(json));
        //Creating an object of our api interface
        apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<AppliedJockeyResponse> call = apiInterface.AppliedJockeyRequestTask(body);


        call.enqueue(new Callback<AppliedJockeyResponse>() {
            @Override
            public void onResponse(Call<AppliedJockeyResponse> call, Response<AppliedJockeyResponse> response) {
                pDialog.dismiss();
                appliedJockeyResponse = response.body();
                if (appliedJockeyResponse != null){
                    if (appliedJockeyResponse.getAppliedForJockey()){
                        gotoAlreadyAudio();
                    }else if (!appliedJockeyResponse.getAppliedForJockey()){
                        gotoAudio();
                    }
                }
            }

            @Override
            public void onFailure(Call<AppliedJockeyResponse> call, Throwable t) {

            }
        });

    }

    private void gotoAudio() {
        Intent i = new Intent(this,AudioFileUpload.class);
        startActivity(i);
    }
    private void gotoAlreadyAudio() {
        Intent i = new Intent(this,AudioUpdatePage.class);
        startActivity(i);
    }

    private void gotoTerms() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_BROWSABLE);
        intent.setData(Uri.parse("http://voxitworld.com/privacypolicy.html"));
        startActivity(intent);
    }

    private void gotoAboutUs() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_BROWSABLE);
        intent.setData(Uri.parse("http://voxitworld.com/"));
        startActivity(intent);
    }

    private void gotoProfile() {
        Intent i = new Intent(this,ProfileActivity.class);
        startActivity(i);
    }

    private void gotoHelpSupport() {
        Intent i = new Intent(this,HelpSupport.class);
        startActivity(i);
    }

    private void gotoRefCode() {
        Intent i = new Intent(this,ReferralCodeActivity.class);
        startActivity(i);
    }
    private void gotoMyRewards() {
        Intent i = new Intent(this,MyRewardsActivity.class);
        startActivity(i);
    }

    private void gotoGenre(){
        Intent i = new Intent(this, GenreActivity.class);
        startActivity(i);

    }
}
