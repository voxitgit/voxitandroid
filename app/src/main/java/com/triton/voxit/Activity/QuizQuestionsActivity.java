package com.triton.voxit.Activity;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
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

public class QuizQuestionsActivity extends AppCompatActivity implements View.OnClickListener {
   // public static VCornerQuestionsResponse.ResponseBean responseBean;
    private VCornerQuestionsResponse responseBeanObj;

    private ArrayList<ResponseBean> responseBeanList;

    private  ArrayList<EventBean> eventBeanArrayList;

    private ArrayList<OptionsBean> optionsBeanArrayList;

   // ViewPagerQuizAdapter viewPagerQuizAdapter;
  //  private  ViewPager mPager;

    TextView tvQuestions,tvAns1,tvAns2,tvAns3,tvAns4;
    Button btnNext, btnPrevious;

    private int Qid;
    private String title;
    private String quizType;
    private String audio_path;
    private String audio_id;
    private String QPatternId;

    int count;

    private int id;



   String EventType;

   private List<EventBean> eventBeanList;

    TextView tvToolbarTittle;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_questions);

        tvToolbarTittle = (TextView)findViewById(R.id.toolbar_title);

        tvQuestions = (TextView)findViewById(R.id.tvquestions);
        tvAns1 = (TextView)findViewById(R.id.tvAns1);
        tvAns2 = (TextView)findViewById(R.id.tvAns2);




        tvAns3 = (TextView)findViewById(R.id.tvAns3);
        tvAns4 = (TextView)findViewById(R.id.tvAns4);
        btnNext = (Button)findViewById(R.id.btnNext);
        btnPrevious = (Button)findViewById(R.id.btnPrevious);

        btnNext.setOnClickListener(this);
        btnPrevious.setOnClickListener(this);

        Bundle bundle = getIntent().getExtras();
        EventType = bundle.getString("EVENT");
        Log.i("EventType-->",EventType);

        //  mPager = (ViewPager) findViewById(R.id.viewpagerQuesAns);
        responseBeanList = (ArrayList<ResponseBean>)getIntent().getSerializableExtra("EVENTSBEAN");
        if(responseBeanList != null){
            eventBeanList = new ArrayList<>();
            for(int i=0; i<responseBeanList.size(); i++){
                responseBeanList.get(i).getEvent().get(i).getQid();
                eventBeanArrayList = responseBeanList.get(i).getEvent();
                title = eventBeanArrayList.get(i).getTitle();
                quizType = eventBeanArrayList.get(i).getQuizType();

                int size = eventBeanArrayList.size();
                tvToolbarTittle.setText("Quiz"+"\t"+"\t"+size);
                Log.i("QuestionCountSize",""+size);

              String eventType=responseBeanList.get(i).getEventType();
                if(EventType.equalsIgnoreCase(eventType)){
                    audio_path =  eventBeanArrayList.get(i).getAudio_path();
                    audio_id =  eventBeanArrayList.get(i).getAudio_id();
                    QPatternId = eventBeanArrayList.get(i).getAudio_id();

                    EventBean eventBean = new EventBean();
                    eventBean.setTitle(eventBeanArrayList.get(i).getTitle());
                    eventBean.setQuizType(eventBeanArrayList.get(i).getQuizType());
                    eventBean.setAudio_path(eventBeanArrayList.get(i).getAudio_path());
                    eventBean.setAudio_id(eventBeanArrayList.get(i).getAudio_id());
                    eventBean.setQPatternId(eventBeanArrayList.get(i).getQPatternId());

                    optionsBeanArrayList = eventBeanArrayList.get(i).getOptions();

                    ArrayList optionsBeanList = new ArrayList<>();

                    for(int j=0;j<optionsBeanArrayList.size();j++){
                        OptionsBean optionsBean = new OptionsBean();
                        optionsBean.setValue(optionsBeanArrayList.get(j).getValue());
                        optionsBean.setId(optionsBeanArrayList.get(j).getId());
                        optionsBeanList.add(optionsBean);

                        Log.e("Value1",optionsBeanArrayList.get(j).getValue());


                    }
                    eventBean.setOptions(optionsBeanList);
                    eventBeanList.add(eventBean);

                }

               /* for(int j=0;j<optionsBeanArrayList.size();j++){
                    String value = optionsBeanArrayList.get(j).getValue();
                    Log.i("Value",  ""+ optionsBeanArrayList.get(j).getValue());
                    setQuestionsAndAnswer(value);
                }*/


                Log.i("QID",  ""+ responseBeanList.get(i).getEvent().get(i).getQid());
              //  viewPagerQuizAdapter = new ViewPagerQuizAdapter(getApplicationContext(),eventBeanArrayList);
              //  mPager.setAdapter(viewPagerQuizAdapter);

            }

            for(int i=0; i<eventBeanList.size();i++){
                tvQuestions.setText(eventBeanList.get(i).getTitle());
                String quizType=eventBeanList.get(i).getQuizType();
                if("SelectOne".equalsIgnoreCase(quizType)){
                    for (OptionsBean answers:eventBeanList.get(i).getOptions()) {
                        selectOneanswerOptions(answers.getId(),answers.getValue());
                    }
                }else if("TrueOrFalse".equalsIgnoreCase(quizType)){
                    for (OptionsBean answers:eventBeanList.get(i).getOptions()) {
                        trueFalseanswerOptions(answers.getId(),answers.getValue());
                    }
                }
                count=1;
                break;
            }

            checkPreviousEnableOrNot();
             checkNextEnableOrNot();

        }











        }

    private void setQuestionsAndAnswers(String value) {
        tvAns1.setText(value);
        tvAns2.setText(value);
        tvAns3.setText(value);
        tvAns4.setText(value);
        Log.e("Value1",value);

    }

    private void setQuestionsAndAnswer(int num){
        tvQuestions.setText(""+title);
        tvAns1.setText("");
        tvAns2.setText("");
        tvAns3.setText("");
        tvAns4.setText("");
       /* tv_question.setText(question.getQuestion(num));
        btn_one.setText(question.getchoice1(num));
        btn_two.setText(question.getchoice2(num));
        btn_three.setText(question.getchoice3(num));
        btn_four.setText(question.getchoice4(num));

        answer = question.getCorrectAnswer(num);*/
    }

    private void selectOneanswerOptions(Integer id,String answer){
        switch (id){
            case 1:
                tvAns1.setText(answer);
                break;
            case 2:
                tvAns2.setText(answer);
                break;
            case 3:
                tvAns3.setText(answer);

                break;
            case 4:
                tvAns4.setText(answer);
                break;
        }
    }
    private void trueFalseanswerOptions(Integer id,String answer){
        switch (id){
            case 1:
                tvAns1.setText(answer);
                break;
            case 2:
                tvAns2.setText(answer);
                break;
            case 3:
                tvAns3.setVisibility(View.INVISIBLE);
                break;
            case 4:
                tvAns4.setVisibility(View.INVISIBLE);
                break;

        }
    }

    private void checkPreviousEnableOrNot(){
        if(count>0){
            btnPrevious.setVisibility(View.VISIBLE);

        }else{
            btnPrevious.setVisibility(View.INVISIBLE);
        }
    }

    private void checkNextEnableOrNot(){
        if(count<eventBeanList.size()-1){
            btnNext.setVisibility(View.INVISIBLE);
        }else{
            btnNext.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnPrevious:
                previousBtn();
                break;
            case R.id.btnNext:
                nextBtn();
                break;
        }

    }

    private void previousBtn() {
        if(count>0){
            count = count -1;
            getCurrentQuestion(count);
            checkPreviousEnableOrNot();
           // checkNextEnableOrNot();
            Log.i("Count:",""+count);
        }else {
            count = eventBeanArrayList.size() - 1;
            getCurrentQuestion(count);
            checkPreviousEnableOrNot();
           // checkNextEnableOrNot();
        }
    }

    private void nextBtn() {
        if(count<eventBeanArrayList.size()-1){
            count = count + 1;
            getCurrentQuestion(count);
            checkPreviousEnableOrNot();
           // checkNextEnableOrNot();
        } else {
            count = 0;
            checkPreviousEnableOrNot();
            //checkNextEnableOrNot();

        }
    }


private void getCurrentQuestion(Integer i){
    try{
        Integer indexPos=i-1;
        tvQuestions.setText(eventBeanList.get(indexPos).getTitle());
        String quizType=eventBeanList.get(indexPos).getQuizType();
        if("SelectOne".equalsIgnoreCase(quizType)){
            for (OptionsBean answers:eventBeanList.get(indexPos).getOptions()) {
                selectOneanswerOptions(answers.getId(),answers.getValue());
            }
        }else if("TrueOrFalse".equalsIgnoreCase(quizType)){
            for (OptionsBean answers:eventBeanList.get(indexPos).getOptions()) {
                trueFalseanswerOptions(answers.getId(),answers.getValue());
            }
        }
    }catch (Exception e){
e.printStackTrace();
    }

}

}
