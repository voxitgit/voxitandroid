package com.triton.voxit.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.triton.voxit.Adapter.SearchAdapter;
import com.triton.voxit.Api.APIClient;
import com.triton.voxit.Api.APIInterface;
import com.triton.voxit.R;
import com.triton.voxit.SessionManager.SessionManager;
import com.triton.voxit.Utlity.MediaPlayerSingleton;
import com.triton.voxit.app.App;
import com.triton.voxit.model.SearchData;
import com.triton.voxit.model.SearchResponseData;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends NavigationDrawer {
    EditText searchEt;
    RecyclerView recyclerView;
    SearchAdapter searchAdapter;
    private ProgressDialog pDialog;
    private APIInterface apiInterface;
    private SearchResponseData searchDataList;
    public static ArrayList<SearchData> searchData = new ArrayList<>();
    private BottomNavigationView bottomNavigationView;
    private String flag = "S";
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_search_list);
        session = new SessionManager(getApplicationContext());
        hideLanguageView();
        showTitleView();
        initUI();
        pDialog = new ProgressDialog(SearchActivity.this);
        pDialog.setMessage(SearchActivity.this.getString(R.string.please_wait));
        pDialog.setIndeterminate(true);
        pDialog.setCancelable(true);
       /* if (!App.appUtils.isNetAvailable()) {
            alertUserP(SearchActivity.this, "Connection Error", "No Internet connection available", "OK");
        } else {
            APICall();
        }*/

        if (searchData.isEmpty()){
            if (!App.appUtils.isNetAvailable()) {
                alertUserP(SearchActivity.this, "Connection Error", "No Internet connection available", "OK");
            }else{
                APICall();
            }

        }else{
            recyclerView.setVisibility(View.VISIBLE);
            loadNotifyListAdapter();
        }

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                APITimerCall();
                handler.postDelayed(this, 120000); //now is every 2 minutes
                Log.i("Timer","Timer");
            }
        }, 120000);

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

    private void initUI() {
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        searchEt = (EditText) findViewById(R.id.search_et);
        bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottom_navigation);
        bottomNavigationView.getMenu().findItem(R.id.search).setChecked(true);

        if (session.isLoggedIn()) {

        } else {
            startActivity(new Intent(this, LoginActivity.class)
                    .setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
        }
        navigationMenu();
    }

    private void navigationMenu() {
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                switch (item.getItemId()) {

                    case R.id.home:
                        startActivity(new Intent(SearchActivity.this, Dashboard.class)
                                .setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
                        finish();
                        break;
                    case R.id.notifi:
                        Intent j = new Intent(SearchActivity.this, NotificationActivity.class);
                        // j.putExtra("login",flag);
                        startActivity(j);
                        break;
                    case R.id.search:
//                        Intent i4 = new Intent(SearchActivity.this,SearchActivity.class);
//                       // i4.putExtra("login",flag);
//                        startActivity(i4);
                        break;
                    case R.id.profile:
                        Intent i = new Intent(SearchActivity.this, ProfileActivity.class);
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


    public void APICall() {
        // show it
        pDialog.show();
        String filter = "";
        JSONObject json = new JSONObject();
        try {
            json.put("filter", filter);
        } catch (JSONException e) {
            Log.e("Exception ", e.toString());
        }

        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), String.valueOf(json));
        //Creating an object of our api interface
        apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<SearchResponseData> call = apiInterface.getSearchData(body);
        Log.e("url", call.request().url().toString());

        call.enqueue(new Callback<SearchResponseData>() {
            @Override
            public void onResponse(Call<SearchResponseData> call, Response<SearchResponseData> response) {
                pDialog.dismiss();
                searchDataList = response.body();
                searchData = searchDataList.getResponse();
                Log.e("response", String.valueOf(searchData));
                Log.e("response1", String.valueOf(searchDataList));

                if (searchDataList.getResponse().equals("null") || searchDataList.getResponse() == null) {

                } else {


                    MediaPlayerSingleton mps = MediaPlayerSingleton.getInstance(SearchActivity.this);
                    mps.setSearchList(searchData);
                   /* LinearLayoutManager lLayout = new LinearLayoutManager(SearchActivity.this);
                    recyclerView.setLayoutManager(lLayout);
                    searchAdapter = new SearchAdapter(SearchActivity.this, searchData);
                    recyclerView.setAdapter(searchAdapter);*/

                    loadNotifyListAdapter();

                    recyclerView.setNestedScrollingEnabled(false);

                    searchEt.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {


                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                            searchAdapter.getFilter().filter(s);
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<SearchResponseData> call, Throwable t) {
                pDialog.dismiss();
                Log.e("Error", t.getMessage());
            }
        });

    }

    public void APITimerCall() {
        searchData.clear();
        // show it
        String filter = "";
        JSONObject json = new JSONObject();
        try {
            json.put("filter", filter);
        } catch (JSONException e) {
            Log.e("Exception ", e.toString());
        }

        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), String.valueOf(json));
        //Creating an object of our api interface
        apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<SearchResponseData> call = apiInterface.getSearchData(body);
        Log.e("url", call.request().url().toString());

        call.enqueue(new Callback<SearchResponseData>() {
            @Override
            public void onResponse(Call<SearchResponseData> call, Response<SearchResponseData> response) {
                searchDataList = response.body();
                searchData = searchDataList.getResponse();
                Log.e("response", String.valueOf(searchData));
                Log.e("response1", String.valueOf(searchDataList));

                if (searchDataList.getResponse().equals("null") || searchDataList.getResponse() == null) {

                } else {


                    MediaPlayerSingleton mps = MediaPlayerSingleton.getInstance(SearchActivity.this);
                    mps.setSearchList(searchData);
                   /* LinearLayoutManager lLayout = new LinearLayoutManager(SearchActivity.this);
                    recyclerView.setLayoutManager(lLayout);
                    searchAdapter = new SearchAdapter(SearchActivity.this, searchData);
                    recyclerView.setAdapter(searchAdapter);*/

                    loadNotifyListAdapter();

                    recyclerView.setNestedScrollingEnabled(false);

                    searchEt.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {


                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                            searchAdapter.getFilter().filter(s);
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<SearchResponseData> call, Throwable t) {
                Log.e("Error", t.getMessage());
            }
        });

    }

    private void loadNotifyListAdapter() {
        LinearLayoutManager lLayout = new LinearLayoutManager(SearchActivity.this);
        recyclerView.setLayoutManager(lLayout);
        searchAdapter = new SearchAdapter(SearchActivity.this, searchData);
        recyclerView.setAdapter(searchAdapter);
    }

//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        startActivity(new Intent(SearchActivity.this, Dashboard.class)
//                .setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
//        finish();
//    }
}
