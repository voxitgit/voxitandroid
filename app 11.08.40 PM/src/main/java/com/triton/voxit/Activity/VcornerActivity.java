package com.triton.voxit.Activity;

        import android.content.SharedPreferences;
        import android.os.Handler;
        import android.preference.PreferenceManager;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.support.v7.widget.DefaultItemAnimator;
        import android.support.v7.widget.LinearLayoutManager;
        import android.support.v7.widget.RecyclerView;
        import android.util.Log;
        import android.view.View;
        import android.widget.Button;
        import android.widget.ImageView;
        import android.widget.ProgressBar;
        import android.widget.TextView;

        import com.bumptech.glide.Glide;
        import com.bumptech.glide.load.engine.DiskCacheStrategy;
        import com.google.gson.Gson;
        import com.triton.voxit.Adapter.VcornerDashboardAdapter;
        import com.triton.voxit.Api.APIClient;
        import com.triton.voxit.Api.APIInterface;
        import com.triton.voxit.Api.RetrofitClient;
        import com.triton.voxit.R;
        import com.triton.voxit.apputils.Constants;
        import com.triton.voxit.apputils.RestUtils;
        import com.triton.voxit.listeners.OnLoadMoreListener;
        import com.triton.voxit.responsepojo.VCornerQuestionsResponse;

        import java.util.List;
        import java.util.Timer;
        import java.util.TimerTask;

        import de.hdodenhof.circleimageview.CircleImageView;
        import retrofit2.Call;
        import retrofit2.Callback;
        import retrofit2.Response;

public class VcornerActivity extends AppCompatActivity {

    ImageView ivHomebtn;
    private ProgressBar progressBar;

    CircleImageView civfirst,civsecond,civthird;
    Button btnrankfirst,btnranksecond, btnrankthird;
    TextView tvfirstname,tvsecondname,tvthirdname;

    VcornerDashboardAdapter vcornerDashboardAdapter;
    RecyclerView rv_vcornerdashboard;

    List<VCornerQuestionsResponse.ResponseBean> responseBeanList;
    private SharedPreferences preferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vcorner);
       initViews();
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
       getVCornerQuestionsResponse();
    }

    private void initViews() {
        ivHomebtn = (ImageView)findViewById(R.id.ivHomeQuizDashboard);
        progressBar = (ProgressBar)findViewById(R.id.progressBarVcorner);

        civfirst = (CircleImageView)findViewById(R.id.civfirst);
        civsecond = (CircleImageView)findViewById(R.id.civsecond);
        civthird = (CircleImageView)findViewById(R.id.civthird);

        btnrankfirst = (Button)findViewById(R.id.btnfirst);
        btnranksecond = (Button)findViewById(R.id.btnsecond);
        btnrankthird = (Button)findViewById(R.id.btnthird);

        tvfirstname = (TextView)findViewById(R.id.tvfirstname);
        tvsecondname = (TextView)findViewById(R.id.tvsecondname);
        tvthirdname = (TextView)findViewById(R.id.tvthirdname);

        rv_vcornerdashboard = (RecyclerView)findViewById(R.id.rvvcornerdashboard);

        ivHomebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


    private void getVCornerQuestionsResponse() {
        progressBar.setVisibility(View.VISIBLE);
        APIInterface ApiService = APIClient.getClient().create(APIInterface.class);
        Call<VCornerQuestionsResponse> call = ApiService.getVCornerQuestionsResponseCall(RestUtils.getContentType());
        call.enqueue(new Callback<VCornerQuestionsResponse>() {
            @Override
            public void onResponse(Call<VCornerQuestionsResponse> call, Response<VCornerQuestionsResponse> response) {
                progressBar.setVisibility(View.GONE);
                Log.i("VCornerQuestionsRes", "--->" + new Gson().toJson(response.body()));
                if (response.body() != null) {
                    responseBeanList = response.body().getResponse();

                  if(1 == response.body().getTopThreePerformers().get(0).getRank()){
                      tvfirstname.setText(response.body().getTopThreePerformers().get(0).getName());
                      if(!response.body().getTopThreePerformers().get(0).getImage().isEmpty()){
                          Glide.with(getApplicationContext())
                                  .load(response.body().getTopThreePerformers().get(0).getImage())
                                  .diskCacheStrategy(DiskCacheStrategy.NONE)
                                  .skipMemoryCache(true)
                                  .error(R.drawable.logo_white)
                                  .into(civfirst);
                      }

                  }
                    if(2 == response.body().getTopThreePerformers().get(1).getRank()){
                        tvsecondname.setText(response.body().getTopThreePerformers().get(1).getName());
                        if(!response.body().getTopThreePerformers().get(1).getImage().isEmpty()){
                            Glide.with(getApplicationContext())
                                    .load(response.body().getTopThreePerformers().get(1).getImage())
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .error(R.drawable.logo_white)
                                    .into(civsecond);
                        }

                    }
                    if(3 == response.body().getTopThreePerformers().get(2).getRank()){
                        tvthirdname.setText(response.body().getTopThreePerformers().get(2).getName());
                        if(!response.body().getTopThreePerformers().get(2).getImage().isEmpty()){
                            Glide.with(getApplicationContext())
                                    .load(response.body().getTopThreePerformers().get(2).getImage())
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .error(R.drawable.logo_white)
                                    .into(civthird);
                        }

                    }
                }

                setView();
            }

            @Override
            public void onFailure(Call<VCornerQuestionsResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Log.i("VCornerQuestionsResflr", "--->" + t.getMessage());
                // Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void setView() {

        rv_vcornerdashboard.setLayoutManager(new LinearLayoutManager(this));
        rv_vcornerdashboard.setItemAnimator(new DefaultItemAnimator());


        vcornerDashboardAdapter = new VcornerDashboardAdapter(VcornerActivity.this, responseBeanList, rv_vcornerdashboard);

        rv_vcornerdashboard.setAdapter(vcornerDashboardAdapter);

        vcornerDashboardAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (preferences.getInt(Constants.INBOX_TOTAL, 0) > responseBeanList.size()) {
                    Log.e("haint", "Load More");




                }


            }


        });


    }
}
