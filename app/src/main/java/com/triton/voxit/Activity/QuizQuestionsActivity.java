package com.triton.voxit.Activity;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.triton.voxit.Adapter.ViewPagerQuizAdapter;
import com.triton.voxit.R;
import com.triton.voxit.model.EventBean;
import com.triton.voxit.model.ResponseBean;
import com.triton.voxit.responsepojo.VCornerQuestionsResponse;

import java.util.ArrayList;
import java.util.List;

public class QuizQuestionsActivity extends AppCompatActivity {
   // public static VCornerQuestionsResponse.ResponseBean responseBean;
    private VCornerQuestionsResponse responseBeanObj;

    private ArrayList<ResponseBean> responseBeanList;

    private  ArrayList<EventBean> eventBeanArrayList;

    ViewPagerQuizAdapter viewPagerQuizAdapter;
    private  ViewPager mPager;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_questions);

        mPager = (ViewPager) findViewById(R.id.viewpagerQuesAns);
        responseBeanList = (ArrayList<ResponseBean>)getIntent().getSerializableExtra("EVENTSBEAN");
        if(responseBeanList != null){
            for(int i=0; i<responseBeanList.size(); i++){
                responseBeanList.get(i).getEvent().get(i).getQid();
                eventBeanArrayList = responseBeanList.get(i).getEvent();
                Log.i("QID",  ""+ responseBeanList.get(i).getEvent().get(i).getQid());
                viewPagerQuizAdapter = new ViewPagerQuizAdapter(getApplicationContext(),eventBeanArrayList);
                mPager.setAdapter(viewPagerQuizAdapter);

            }
        }

      /*  if(responseBean != null){
            responseBean.getEvent().get(0).getTitle();

            Log.i("Title", responseBean.getEvent().get(0).getTitle());



            responseBeanList.add(responseBean);*/




        }


    }

