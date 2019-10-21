package com.triton.voxit.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.triton.voxit.R;

public class QuizQuestionsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_questions);


        progressBar = (ProgressBar)findViewById(R.id.progressBarQustionsandAnswer);

        tvToolbarTittle = (TextView)findViewById(R.id.toolbar_title);
        miniPlayerLayout = (RelativeLayout) findViewById(R.id.miniPlayerLayout);
        imgClose = (ImageView) findViewById(R.id.imgClose);
        imgMiniPlay = (ImageView) findViewById(R.id.imgMiniPlay);
        txtSongName = (TextView) findViewById(R.id.txtSongName);
        txtSongName.setSelected(true);
        txtTypeName = (TextView) findViewById(R.id.typeName);
        imgSong = (ImageView) findViewById(R.id.imgSong);
        txtAuthorName = (TextView) findViewById(R.id.txtAuthorName);

        ll_AudioPlay = (LinearLayout)findViewById(R.id.llAudioPlay);
        ll_AudioPlay.setOnClickListener(this);

        tvQuestions = (TextView)findViewById(R.id.tvquestions);
        tvAns1 = (TextView)findViewById(R.id.tvAns1);
        tvAns2 = (TextView)findViewById(R.id.tvAns2);
        tvAns3 = (TextView)findViewById(R.id.tvAns3);
        tvAns4 = (TextView)findViewById(R.id.tvAns4);
        btnNext = (Button)findViewById(R.id.btnNext);
        btnPrevious = (Button)findViewById(R.id.btnPrevious);
        btnSubmit = (Button)findViewById(R.id.btnSubmit);

        tvAns1.setOnClickListener(this);
        tvAns2.setOnClickListener(this);
        tvAns3.setOnClickListener(this);
        tvAns4.setOnClickListener(this);

        btnNext.setOnClickListener(this);
        btnPrevious.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);

        mps = MediaPlayerSingleton.getInstance(this);


        /*Timer*/
        progressBarCircle = (ProgressBar) findViewById(R.id.progressBarCircle);
        editTextMinute = (EditText) findViewById(R.id.editTextMinute);
        textViewTime = (TextView) findViewById(R.id.textViewTime);
        //imageViewReset = (ImageView) findViewById(R.id.imageViewReset);
       // imageViewStartStop = (ImageView) findViewById(R.id.imageViewStartStop);

        startStop();

        Bundle bundle = getIntent().getExtras();
        EventType = bundle.getString("EVENT");
        Log.i("EventType-->",EventType);

        //  mPager = (ViewPager) findViewById(R.id.viewpagerQuesAns);
        responseBeanList = (ArrayList<ResponseBean>)getIntent().getSerializableExtra("EVENTSBEAN");
        if(responseBeanList != null){
            eventBeanList = new ArrayList<>();
            Log.i("Size","-->"+responseBeanList.size());
            for(int i=0; i<responseBeanList.size(); i++){
                responseBeanList.get(i).getEvent().get(i).getQid();
              List<EventBean>  eventBeanArrayList = responseBeanList.get(i).getEvent();
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

                    for(int j=0;j<eventBeanArrayList.size();j++){
                        EventBean eventBean = new EventBean();
                        eventBean.setTitle(eventBeanArrayList.get(j).getTitle());
                        eventBean.setQuizType(eventBeanArrayList.get(j).getQuizType());
                        eventBean.setAudio_path(eventBeanArrayList.get(j).getAudio_path());
                        eventBean.setAudio_id(eventBeanArrayList.get(j).getAudio_id());
                        eventBean.setQPatternId(eventBeanArrayList.get(j).getQPatternId());
                        eventBean.setQid(eventBeanArrayList.get(j).getQid());
                       List<OptionsBean> optionsBeanArrayList = eventBeanArrayList.get(j).getOptions();

                        ArrayList optionsBeanList = new ArrayList<>();

                        for(int k=0;k<optionsBeanArrayList.size();k++){
                            OptionsBean optionsBean = new OptionsBean();
                            optionsBean.setValue(optionsBeanArrayList.get(k).getValue());
                            optionsBean.setId(optionsBeanArrayList.get(k).getId());
                            optionsBeanList.add(optionsBean);
                            Log.e("Value1",optionsBeanArrayList.get(k).getValue());


                        }
                        eventBean.setOptions(optionsBeanList);
                        eventBeanList.add(eventBean);
                    }

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
            Log.i("EventBeanListSize","-->"+eventBeanList);
            for(int i=0; i<eventBeanList.size();i++){
                tvQuestions.setText(eventBeanList.get(i).getQid()+"."+eventBeanList.get(i).getTitle());
                String quizType=eventBeanList.get(i).getQuizType();

               /*if(isAudioClick){
                    playSongs(eventBeanList.get(i).getAudio_path(),eventBeanList.get(i).getAudio_id(),i);
                }*/

                if("SelectOne".equalsIgnoreCase(quizType)){
                    for (OptionsBean answers:eventBeanList.get(i).getOptions()) {
                        selectOneanswerOptions(answers.getId(),answers.getValue());

                    }

                    tvAns3.setVisibility(View.VISIBLE);
                    tvAns4.setVisibility(View.VISIBLE);

                }else if("TrueOrFalse".equalsIgnoreCase(quizType)){
                    for (OptionsBean answers:eventBeanList.get(i).getOptions()) {
                        trueFalseanswerOptions(answers.getId(),answers.getValue());
                    }
                    tvAns3.setVisibility(View.INVISIBLE);
                    tvAns4.setVisibility(View.INVISIBLE);
                }
                count=0;
                break;
            }

            checkPreviousEnableOrNot();
            checkNextEnableOrNot();

        }
        }



    private void selectedAnswerQidAnsId(int qid, int id, int value) {
        tvAns1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvAns1.setBackgroundResource(R.drawable.textview_bg_change);
                tvAns2.setBackgroundResource(R.drawable.textview_bg);
                tvAns3.setBackgroundResource(R.drawable.textview_bg);
                tvAns4.setBackgroundResource(R.drawable.textview_bg);


            }
        });
        tvAns2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvAns1.setBackgroundResource(R.drawable.textview_bg);
                tvAns2.setBackgroundResource(R.drawable.textview_bg_change);
                tvAns3.setBackgroundResource(R.drawable.textview_bg);
                tvAns4.setBackgroundResource(R.drawable.textview_bg);

            }
        });
        tvAns3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                tvAns1.setBackgroundResource(R.drawable.textview_bg);
                tvAns2.setBackgroundResource(R.drawable.textview_bg);
                tvAns3.setBackgroundResource(R.drawable.textview_bg_change);
                tvAns4.setBackgroundResource(R.drawable.textview_bg);
            }
        });
        tvAns4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                tvAns1.setBackgroundResource(R.drawable.textview_bg);
                tvAns2.setBackgroundResource(R.drawable.textview_bg);
                tvAns3.setBackgroundResource(R.drawable.textview_bg);
                tvAns4.setBackgroundResource(R.drawable.textview_bg_change);
            }
        });
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
                tvAns3.setVisibility(View.GONE);
                break;
            case 4:
                tvAns4.setVisibility(View.GONE);
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
        Log.i("count--",""+count+" "+eventBeanList.size());
        if((count+1)==eventBeanList.size()){
            btnNext.setVisibility(View.INVISIBLE);
            btnSubmit.setVisibility(View.VISIBLE);
        }else{
           btnNext.setVisibility(View.VISIBLE);
           btnSubmit.setVisibility(View.INVISIBLE);
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
            case R.id.llAudioPlay:
               // isAudioClick = true;
                break;

            case R.id.tvAns1:
                break;
            case R.id.tvAns2:
                break;
            case R.id.tvAns3:
                break;
            case R.id.tvAns4:
                break;

            case R.id.btnSubmit:

                break;

        }

    }




    private void previousBtn() {
        if(count>0){
            count = count -1;
            getCurrentQuestion(count);
            Log.i("Count:",""+count);
        }else {
            count = eventBeanList.size() - 1;
            getCurrentQuestion(count);

        }
        checkPreviousEnableOrNot();
        checkNextEnableOrNot();
    }

    private void nextBtn() {
        Log.i("count",""+count);
        Log.i("CountList",""+(eventBeanList.size()-1));

        if(count==eventBeanList.size()){
            count = count;
            getCurrentQuestion(count);
        }else{
            count = count +1;
            getCurrentQuestion(count);
        }
        checkPreviousEnableOrNot();
        checkNextEnableOrNot();
    }

    private void getCurrentQuestion(Integer i){
    try{
        Integer indexPos=i;
        Log.i("eventBeanList",""+indexPos);
        Log.i("eventBeanList",""+eventBeanList.get(indexPos));
        tvQuestions.setText(eventBeanList.get(indexPos).getQid()+"."+eventBeanList.get(indexPos).getTitle());
        String quizType=eventBeanList.get(indexPos).getQuizType();


        /*if(isAudioClick){
            playSongs(eventBeanList.get(i).getAudio_path(),eventBeanList.get(i).getAudio_id(),i);
        }*/
        if("SelectOne".equalsIgnoreCase(quizType)){
            for (OptionsBean answers:eventBeanList.get(indexPos).getOptions()) {
                selectOneanswerOptions(answers.getId(),answers.getValue());

            }

            tvAns3.setVisibility(View.VISIBLE);
            tvAns4.setVisibility(View.VISIBLE);
        }else if("TrueOrFalse".equalsIgnoreCase(quizType)){
            for (OptionsBean answers:eventBeanList.get(indexPos).getOptions()) {
                trueFalseanswerOptions(answers.getId(),answers.getValue());
            }
            tvAns3.setVisibility(View.INVISIBLE);
            tvAns4.setVisibility(View.INVISIBLE);
        }
    }catch (Exception e){
      e.printStackTrace();
 
    }
}
