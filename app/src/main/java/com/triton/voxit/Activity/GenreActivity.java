package com.triton.voxit.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.triton.voxit.Adapter.GenreAdapter;
import com.triton.voxit.Adapter.SelectGenreAdapter;
import com.triton.voxit.Api.APIClient;
import com.triton.voxit.Api.APIInterface;
import com.triton.voxit.R;
import com.triton.voxit.SessionManager.SessionManager;
import com.triton.voxit.app.App;
import com.triton.voxit.model.Genre;
import com.triton.voxit.model.GenreData;
import com.triton.voxit.model.GenreResponseData;
import com.triton.voxit.model.RecyclerViewItem;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GenreActivity extends NavigationDrawer {
    RecyclerView recyclerView;
    private ProgressDialog pDialog;
    private APIInterface apiInterface;
    private GenreResponseData genreDataList;
    private ArrayList<GenreData> genreData;
    String flag;
    SessionManager session;
    private TextView headertitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_genre_activity);
        session = new SessionManager(getApplicationContext());
        initUI();
        pDialog = new ProgressDialog(GenreActivity.this);
        pDialog.setMessage(GenreActivity.this.getString(R.string.please_wait));
        pDialog.setIndeterminate(true);
        pDialog.setCancelable(true);
        if (!App.appUtils.isNetAvailable()) {
            alertUserP(GenreActivity.this, "Connection Error", "No Internet connection available", "OK");
        }else {
            APICall();
        }
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
    private void initUI(){
        recyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        headertitle = (TextView) findViewById(R.id.header_title);
        headertitle.setText("Genre");


//        Intent i = getIntent();
//        flag = i.getStringExtra("login");
//        if (flag.equals("L")){
//            startActivity(new Intent(this, LoginActivity.class)
//                    .putExtra("login","F").setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
//        }else{
//
//        }
        if(session.isLoggedIn()){

        }else {
            startActivity(new Intent(this, LoginActivity.class)
                    .putExtra("login","F").setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
        }


    }

    public void APICall(){
        // show it
        pDialog.show();
        Integer langid = 287;
        JSONObject json = new JSONObject();
        try {
            json.put("lang_id", langid);
        } catch (JSONException e) {
            Log.e("Exception ", e.toString());
        }

        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), String.valueOf(json));
        //Creating an object of our api interface
        apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<GenreResponseData> call = apiInterface.getGenreData(body);
        Log.e("url",call.request().url().toString());

        call.enqueue(new Callback<GenreResponseData>() {
            @Override
            public void onResponse(Call<GenreResponseData> call, Response<GenreResponseData> response) {
                pDialog.dismiss();
                genreDataList = response.body();
                genreData = genreDataList.getData();

                if(genreDataList.getStatus().equals("Failure")) {

                } else if(genreDataList.getStatus().equals("Success")){

                    GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(),3,LinearLayoutManager.VERTICAL,false);
                    recyclerView.setLayoutManager(gridLayoutManager);
                    //add space item decoration and pass space you want to give
                    recyclerView.addItemDecoration(new EqualSpacingItemDecoration(5));
                    //finally set adapter
                    recyclerView.setAdapter(new SelectGenreAdapter(genreData, GenreActivity.this));
                }
            }


            @Override
            public void onFailure(Call<GenreResponseData> call, Throwable t) {
                pDialog.dismiss();
                Log.e("Error",t.getMessage());
            }
        });

    }

    private List<RecyclerViewItem> GenreDummyList() {
        List<RecyclerViewItem> recyclerViewItems = new ArrayList<>();

        String[] imageUrls = {"https://cdn.pixabay.com/photo/2016/11/18/17/42/barbecue-1836053_640.jpg",
                "https://cdn.pixabay.com/photo/2016/07/11/03/23/chicken-rice-1508984_640.jpg",
                "https://cdn.pixabay.com/photo/2017/03/30/08/10/chicken-intestine-2187505_640.jpg",
                "https://cdn.pixabay.com/photo/2017/02/15/15/17/meal-2069021_640.jpg",
                "https://cdn.pixabay.com/photo/2017/06/01/07/15/food-2362678_640.jpg"};
        String[] titles = {"suryan",
                "peace",
                "yoga", "suryan", "yoga"};
        String[] descriptions = {"jason ma",
                "rejman",
                "Pink",
                "Harry",
                "Harry"};

        for (int i = 0; i < imageUrls.length; i++) {
            Genre listItem = new Genre(titles[i],imageUrls[i]);
            //add food items
            recyclerViewItems.add(listItem);
        }


        return recyclerViewItems;
    }
    public void setTopGenreAdapter(){

        //comedy_recycler_view.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(),3, LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(gridLayoutManager);
        //add space item decoration and pass space you want to give
        recyclerView.addItemDecoration(new EqualSpacingItemDecoration(5));
        //finally set adapter
//        recyclerView.setAdapter(new SelectGenreAdapter( GenreDummyList(),this));
    }


}
