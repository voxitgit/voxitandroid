package com.triton.voxit.Activity;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.triton.voxit.Adapter.ViewPagerQuizAdapter;
import com.triton.voxit.R;
import com.triton.voxit.model.EventBean;
import com.triton.voxit.model.OptionsBean;
import com.triton.voxit.model.ResponseBean;
import com.triton.voxit.responsepojo.VCornerQuestionsResponse;

import java.util.ArrayList;
import java.util.List;

public class QuizQuestionsActivity extends AppCompatActivity {
   // public static VCornerQuestionsResponse.ResponseBean responseBean;
    private VCornerQuestionsResponse responseBeanObj;

    private ArrayList<ResponseBean> responseBeanList;

    private  ArrayList<EventBean> eventBeanArrayList;

    private ArrayList<OptionsBean> optionsBeanArrayList;

   // ViewPagerQuizAdapter viewPagerQuizAdapter;
  //  private  ViewPager mPager;

    TextView tvQuestions,tvAns1,tvAns2,tvAns3,tvAns4;
    Button btnNext;

    private int Qid;
    private String title;
    private String quizType;
    private String audio_path;
    private String audio_id;
    private String QPatternId;

    int count;

    private int id;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_questions);

        tvQuestions = (TextView)findViewById(R.id.tvquestions);
        tvAns1 = (TextView)findViewById(R.id.tvAns1);
        tvAns2 = (TextView)findViewById(R.id.tvAns2);
        tvAns3 = (TextView)findViewById(R.id.tvAns3);
        tvAns4 = (TextView)findViewById(R.id.tvAns4);
        btnNext = (Button)findViewById(R.id.btnNext);


        //  mPager = (ViewPager) findViewById(R.id.viewpagerQuesAns);
        responseBeanList = (ArrayList<ResponseBean>)getIntent().getSerializableExtra("EVENTSBEAN");
        if(responseBeanList != null){
            for(int i=0; i<responseBeanList.size(); i++){
                responseBeanList.get(i).getEvent().get(i).getQid();
                eventBeanArrayList = responseBeanList.get(i).getEvent();
                title = eventBeanArrayList.get(i).getTitle();
                quizType = eventBeanArrayList.get(i).getQuizType();
                audio_path =  eventBeanArrayList.get(i).getAudio_path();
                audio_id =  eventBeanArrayList.get(i).getAudio_id();
                QPatternId = eventBeanArrayList.get(i).getAudio_id();

                optionsBeanArrayList = eventBeanArrayList.get(i).getOptions();

               /* for(int j=0;j<optionsBeanArrayList.size();j++){
                    String value = optionsBeanArrayList.get(j).getValue();
                    Log.i("Value",  ""+ optionsBeanArrayList.get(j).getValue());
                    setQuestionsAndAnswer(value);
                }*/

                for(int j=0;j<optionsBeanArrayList.size();j++){
                    tvAns1.setText(optionsBeanArrayList.get(j).getValue());
                    tvAns2.setText(optionsBeanArrayList.get(j).getValue());
                    tvAns3.setText(optionsBeanArrayList.get(j).getValue());
                    tvAns4.setText(optionsBeanArrayList.get(j).getValue());
                    Log.e("Value1",optionsBeanArrayList.get(j).getValue());

                }



                Log.i("QID",  ""+ responseBeanList.get(i).getEvent().get(i).getQid());
              //  viewPagerQuizAdapter = new ViewPagerQuizAdapter(getApplicationContext(),eventBeanArrayList);
              //  mPager.setAdapter(viewPagerQuizAdapter);

            }
        }

        if(count<eventBeanArrayList.size()-1){
            count = count+1;
            Log.i("Count:",""+count);
        }



        tvQuestions.setText(""+title);




        }

    private void setQuestionsAndAnswer(String value) {
        tvAns1.setText(value);
        tvAns2.setText(value);
        tvAns3.setText(value);
        tvAns4.setText(value);
        Log.e("Value1",value);

    }


}

