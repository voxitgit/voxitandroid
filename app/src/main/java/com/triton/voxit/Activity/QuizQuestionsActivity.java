package com.triton.voxit.Activity;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.triton.voxit.Adapter.ViewPagerQuizAdapter;
import com.triton.voxit.Api.APIClient;
import com.triton.voxit.Api.APIInterface;
import com.triton.voxit.R;
import com.triton.voxit.Service.MediaPlayerService;
import com.triton.voxit.Utlity.MediaPlayerSingleton;
import com.triton.voxit.apputils.RestUtils;
import com.triton.voxit.model.EventBean;
import com.triton.voxit.model.OptionsBean;
import com.triton.voxit.model.ResponseBean;
import com.triton.voxit.requestpojo.CreateQuizAnswersRequest;
import com.triton.voxit.responsepojo.CreateQuizAnswersResponse;
import com.triton.voxit.responsepojo.VCornerQuestionsResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuizQuestionsActivity extends AppCompatActivity implements View.OnClickListener {
   // public static VCornerQuestionsResponse.ResponseBean responseBean;
    private VCornerQuestionsResponse responseBeanObj;

    private ArrayList<ResponseBean> responseBeanList;

    //private  ArrayList<EventBean> eventBeanArrayList;

    private ArrayList<OptionsBean> optionsBeanArrayList;

   // ViewPagerQuizAdapter viewPagerQuizAdapter;
  //  private  ViewPager mPager;

    private List<CreateQuizAnswersRequest.AnswerBean> answerBeanList;
    CreateQuizAnswersRequest.AnswerBean answerBean = new CreateQuizAnswersRequest.AnswerBean();

private TextView tvQuestions,tvAns1,tvAns2,tvAns3,tvAns4;
  private   Button btnNext, btnPrevious,btnSubmit;

    private int Qid;
    private String title;
    private String quizType;
    private String audio_path;
    private String audio_id;
    private String QPatternId;

    int count;

    private int id;



   String EventType,EventTime;

   private List<EventBean> eventBeanList=new ArrayList<>();

    TextView tvToolbarTittle;
    private ProgressBar progressBar;
    MediaPlayerSingleton mps;
    private RelativeLayout miniPlayerLayout;
    private ImageView imgClose, imgSong;
    private TextView txtSongName, txtAuthorName, txtTypeName;
    ImageView searchImg, imgMiniPlay;

    LinearLayout ll_AudioPlay;

    Boolean isAudioClick = false;



    private long timeCountInMilliSeconds = 1 * 60000;

    private enum TimerStatus {
        STARTED,
        STOPPED
    }

    private TimerStatus timerStatus = TimerStatus.STOPPED;

    private ProgressBar progressBarCircle;
    private EditText editTextMinute;
    private TextView textViewTime;
    private ImageView imageViewReset;
    private ImageView imageViewStartStop;
    private CountDownTimer countDownTimer;


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
        EventTime = bundle.getString("EVENTTIME");



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



    private void selectedAnswerQidAnsId(int qid, int id, int ans) {
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

    private void playSongs(String audio_path, String audio_id, Integer i) {
        playSong(audio_path,audio_id,i);
    }


    public void playSong(String audioUrl,String audioid,int songIndex) {
        try {

            setCurrentPlayPos(songIndex);
            // if (!isPlaying) {
            //  mps.releasePlayer();

            if (mps.mp != null) {
                mps.mp.pause();
//                mps.mp.reset();
                //   mps.mp.release();

                mps.mp = null;
            }
            // player = new MediaPlayer();
            mps.initializePlayer();
            mps.mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mps.mp.setDataSource(audioUrl);

            //desTv.setText("By"+" "+audiosongsList.get(songIndex).getName());

            mps.mp.prepare();
            mps.mp.start();

            if (!miniPlayerLayout.isShown()) {
                // miniPlayerLayout.setEnabled(false);
                miniPlayerLayout.setVisibility(View.VISIBLE);
            }



            buildNotification(MediaPlayerService.ACTION_PLAY);

            setStatus("playing");

            int duration = mps.mp.getDuration();
            duration = duration / 1000;

            Log.e( "duration " ,""+ duration);

            //seekBar.setMax(duration);

      /*      this.runOnUiThread(updateRunnable = new Runnable() {
                @Override
                public void run() {

                    if (mps.mp != null) {
                        seekbarPosition = mps.mp.getCurrentPosition() / 1000;
                        seekBar.setProgress(seekbarPosition);

                        // Displaying time completed playing
                        end_time.setText("" + utils.milliSecondsToTimer(mps.mp.getDuration()));
//                            int percentage = (100 * player.getCurrentPosition()) / songDuration;
                        start_time.setText("" + utils.milliSecondsToTimer(mps.mp.getCurrentPosition()));

                    }
                    mHandler.postDelayed(this, 1000);
                }
            });
*/
            mps.mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    Log.e( "TAG:prepared " ,""+ mp.getDuration());
                }
            });

            mps.mp.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
                @Override
                public void onBufferingUpdate(MediaPlayer mp, int percent) {
                    Log.d("TAG:buffering " , mp.getDuration() + " percent " + percent);

                    if (mps.mp != null) {


                    }
                }
            });

            //Attempt to seek to past end of file: request = 259000, durationMs = 0
            mps.mp.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                @Override
                public boolean onInfo(MediaPlayer mp, int what, int extra) {
                    switch (what) {
                        case MediaPlayer.MEDIA_INFO_BUFFERING_START:
                            break;
                        case MediaPlayer.MEDIA_INFO_BUFFERING_END:

                            int duration = mps.mp.getDuration();

                            duration = duration / 1000;
                            //seekBar.setMax(duration);
                            break;
                    }
                    return false;
                }
            });
//                Log.w("Duartion ", duration + "");

            mps.mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    // imgMiniPlay.setImageResource(R.drawable.play_blue);

                    if (mps.mp != null) {
                        setStatus("completed");
                        imgMiniPlay.setImageResource(R.drawable.pause_blue);
                        // playMethod(songIndex + 1);

                        //  playNextSong();


                    }


                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void setCurrentPlayPos(int pos) {
        mps.setCurrentPlayPos(pos);
    }
    private void setStatus(String status) {

        mps.setMediaPlayerStatus(status);
    }
    private void buildNotification(String action) {
        Intent intent = new Intent(getApplicationContext(), MediaPlayerService.class);
        intent.setAction(action);
        startService(intent);
    }


    /**
     * method to start and stop count down timer
     */
    private void startStop() {
        if (timerStatus == TimerStatus.STOPPED) {

            // call to initialize the timer values
            setTimerValues();
            // call to initialize the progress bar values
            setProgressBarValues();
            // showing the reset icon
            // imageViewReset.setVisibility(View.VISIBLE);
            // changing play icon to stop icon
            // imageViewStartStop.setImageResource(R.drawable.icon_stop);
            // making edit text not editable
            editTextMinute.setEnabled(false);
            // changing the timer status to started
            timerStatus = TimerStatus.STARTED;
            // call to start the count down timer
            startCountDownTimer();

        } else {

            // hiding the reset icon
            // imageViewReset.setVisibility(View.GONE);
            // changing stop icon to start icon
            //imageViewStartStop.setImageResource(R.drawable.icon_start);
            // making edit text editable
            editTextMinute.setEnabled(true);
            // changing the timer status to stopped
            timerStatus = TimerStatus.STOPPED;
            stopCountDownTimer();

        }

    }

    /**
     * method to initialize the values for count down timer
     */
    private void setTimerValues() {
        int time = Integer.parseInt(EventTime);
        if (!editTextMinute.getText().toString().isEmpty()) {
            // fetching value from edit text and type cast to integer
            time = Integer.parseInt(editTextMinute.getText().toString().trim());
        } else {
            // toast message to fill edit text
            Toast.makeText(getApplicationContext(), getString(R.string.message_minutes), Toast.LENGTH_LONG).show();
        }
        // assigning values after converting to milliseconds
        timeCountInMilliSeconds = time * 60 * 1000;
    }

    /**
     * method to start count down timer
     */
    private void startCountDownTimer() {

        countDownTimer = new CountDownTimer(timeCountInMilliSeconds, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

                textViewTime.setText(hmsTimeFormatter(millisUntilFinished));

                progressBarCircle.setProgress((int) (millisUntilFinished / 1000));

            }

            @Override
            public void onFinish() {

                textViewTime.setText(hmsTimeFormatter(timeCountInMilliSeconds));
                // call to initialize the progress bar values
                setProgressBarValues();
                // hiding the reset icon
                // imageViewReset.setVisibility(View.GONE);
                // changing stop icon to start icon
                //   imageViewStartStop.setImageResource(R.drawable.icon_start);
                // making edit text editable
                editTextMinute.setEnabled(true);
                // changing the timer status to stopped
                timerStatus = TimerStatus.STOPPED;
            }

        }.start();
        countDownTimer.start();
    }

    /**
     * method to stop count down timer
     */
    private void stopCountDownTimer() {
        countDownTimer.cancel();
    }

    /**
     * method to set circular progress bar values
     */
    private void setProgressBarValues() {

        progressBarCircle.setMax((int) timeCountInMilliSeconds / 1000);
        progressBarCircle.setProgress((int) timeCountInMilliSeconds / 1000);
    }


    /**
     * method to convert millisecond to time format
     *
     * @param milliSeconds
     * @return HH:mm:ss time formatted string
     */
    private String hmsTimeFormatter(long milliSeconds) {

        String hms = String.format("%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(milliSeconds),
                TimeUnit.MILLISECONDS.toMinutes(milliSeconds) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(milliSeconds)),
                TimeUnit.MILLISECONDS.toSeconds(milliSeconds) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliSeconds)));

        return hms;


    }

    private void createQuizMappingResponseCall() {
        progressBar.setVisibility(View.VISIBLE);

        APIInterface ApiService = APIClient.getClient().create(APIInterface.class);
        Call<CreateQuizAnswersResponse> call = ApiService.CreateQuizAnswersResponseCall(RestUtils.getContentType(), createQuizAnswersRequest());
        call.enqueue(new Callback<CreateQuizAnswersResponse>() {
            @Override
            public void onResponse(Call<CreateQuizAnswersResponse> call, Response<CreateQuizAnswersResponse> response) {
               // avi_indicator.smoothToHide();
                Log.e("LoginRes", "--->" + new Gson().toJson(response.body()));
                progressBar.setVisibility(View.INVISIBLE);

                if(200 == response.body().getCode()){








                }else{
                    //Toasty.error(getApplicationContext(), Message, Toast.LENGTH_SHORT, true).show();
                }

            }

            @Override
            public void onFailure(Call<CreateQuizAnswersResponse> call, Throwable t) {
                progressBar.setVisibility(View.INVISIBLE);
                Log.e("LoginResFlr", "--->" + t.getMessage());
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
    private CreateQuizAnswersRequest createQuizAnswersRequest() {
        CreateQuizAnswersRequest createQuizAnswersRequest = new CreateQuizAnswersRequest();

        createQuizAnswersRequest.setAnswer(answerBeanList);

        Log.i("LoginReq", "--->" + new Gson().toJson(createQuizAnswersRequest));
        return createQuizAnswersRequest;
    }




}

