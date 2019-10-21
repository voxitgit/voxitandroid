package com.triton.voxit.Activity;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ProgressBar;

import com.triton.voxit.Adapter.DasboardAdapter;
import com.triton.voxit.Adapter.LanguageAdapter;
import com.triton.voxit.Adapter.TopGenreAdapter;
import com.triton.voxit.Api.APIClient;
import com.triton.voxit.Api.APIInterface;
import com.triton.voxit.R;
import com.triton.voxit.SessionManager.SessionManager;
import com.triton.voxit.app.App;
import com.triton.voxit.model.Genre;
import com.triton.voxit.model.Header;
import com.triton.voxit.model.LanguageData;
import com.triton.voxit.model.LanguageResponseData;
import com.triton.voxit.model.RecyclerViewItem;
import com.triton.voxit.model.TopGenre;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Language extends Activity {
    RecyclerView recyclerView;
    private APIInterface apiInterface;
    private ProgressDialog pDialog;
    private LanguageResponseData languageDataList;
    private ArrayList<LanguageData> languageData;
    String flag;
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.language_activity);
        session = new SessionManager(getApplicationContext());
        initUI();
        pDialog = new ProgressDialog(Language.this);
        pDialog.setMessage(Language.this.getString(R.string.please_wait));
        pDialog.setIndeterminate(true);
        pDialog.setCancelable(true);
        if (!App.appUtils.isNetAvailable()) {
            alertUserP(Language.this, "Connection Error", "No Internet connection available", "OK");
        }else {
            APICall();
        }

    }//end of oncreate
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
        //Creating an object of our api interface
        apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<LanguageResponseData> call = apiInterface.getLanguageData();
        Log.e("url",call.request().url().toString());

        call.enqueue(new Callback<LanguageResponseData>() {
            @Override
            public void onResponse(Call<LanguageResponseData> call, Response<LanguageResponseData> response) {
                            pDialog.dismiss();
                languageDataList = response.body();
                languageData = languageDataList.getData();

                if(languageDataList.getStatus().equals("Failure")) {

                } else if(languageDataList.getStatus().equals("Success")){

                    GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(),3,LinearLayoutManager.VERTICAL,false);
                    recyclerView.setLayoutManager(gridLayoutManager);
            //add space item decoration and pass space you want to give
                    recyclerView.addItemDecoration(new EqualSpacingItemDecoration(5));
            //finally set adapter
                    recyclerView.setAdapter(new LanguageAdapter(languageData, Language.this));
                }
            }


            @Override
            public void onFailure(Call<LanguageResponseData> call, Throwable t) {
                pDialog.dismiss();
                Log.e("Error",t.getMessage());
            }
        });

    }

}
