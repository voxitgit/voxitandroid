package com.triton.voxit.Adapter;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.triton.voxit.R;
import com.triton.voxit.listeners.OnLoadMoreListener;
import com.triton.voxit.responsepojo.VCornerQuestionsResponse;



import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class VcornerDashboardAdapter extends  RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_ONE = 1;
    private static final int TYPE_LOADING = 5;
    private static final String TAG = "InboxItemAdapter";

    private List<VCornerQuestionsResponse.ResponseBean> responseBeanList = null;
    private Context context;


    ArrayList<VCornerQuestionsResponse.ResponseBean> arrayList = null;

    private OnLoadMoreListener mOnLoadMoreListener;


    private boolean isLoading;
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    VCornerQuestionsResponse.ResponseBean currentItem;

    Long elapsedDays, elapsedHours, elapsedMinutes, elapsedSeconds;
    String strMSgdaystime;
    String strMsg;



    public VcornerDashboardAdapter(Context context,  List<VCornerQuestionsResponse.ResponseBean> nodesBeanList, RecyclerView inbox_list) {
        this.responseBeanList = nodesBeanList;
        this.context = context;

        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) inbox_list.getLayoutManager();
        inbox_list.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                totalItemCount = linearLayoutManager.getItemCount();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();


                if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                    if (mOnLoadMoreListener != null) {
                        mOnLoadMoreListener.onLoadMore();
                    }
                    isLoading = true;
                }
            }
        });
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {

                handler.postDelayed(this, 120000); //now is every 2 minutes
                Log.i("Timer","Timer");
            }
        }, 120000);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.quizdasboard_cardview, parent, false);
        return new ViewHolderOne(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        initLayoutOne((ViewHolderOne) holder, position);
    }

    private void initLayoutOne(ViewHolderOne holder, int position) {


        currentItem = responseBeanList.get(position);
        for (int i = 0; i < responseBeanList.size(); i++) {
            holder.txt_eventtype.setText(currentItem.getEventType());
            String startDate = currentItem.getStartDate()+" "+currentItem.getStartTime();
            String endDate = currentItem.getEndDate()+" "+currentItem.getEndTime();
            getCalculateTimes(endDate);

             if(checkCurrentDateTimeAndEndDateTime(endDate)){
                 Log.i("endDate",endDate);
                //API CALL
             }else if(checkStartDateIsFutureDateTime(startDate)){
                 Log.i("startDate",startDate);
                 Toast.makeText(context, "Selected row!"+startDate,
                         Toast.LENGTH_LONG).show();
                 holder.txt_endsin.setText(strMsg+" "+strMSgdaystime+" "+elapsedHours+":"+elapsedMinutes+":"+elapsedSeconds);

             }
            Log.i("STARTEND","Start"+startDate+"End"+endDate  );



           /* if(0>elapsedDays){
                strMSgdaystime = "Days "+elapsedDays;
                holder.txt_endsin.setText("Ends in"+strMSgdaystime+" "+strMsgtime);
            }else{
                holder.txt_endsin.setText("Ends in"+" "+strMsgtime);
            }
*/

        }



    }
    private Date getCurrentDateandTime(){
        //Date currentdateandtime = new Date();
        //Log.i("CurrentDateAndTime",""+currentdateandtime);
        return  new Date();
    }

    private Boolean checkCurrentDateTimeAndEndDateTime(String endDateStr){
        Date todayDate=getCurrentDateandTime();
        Date endDate=convertStringToDateTimeWithAMPM(endDateStr);
        if(todayDate.equals(endDate)){
            return true;
        }else{
            return false;
        }
    }

    private Boolean checkStartDateIsFutureDateTime(String dateStr){
        Date todayDate=getCurrentDateandTime();
        Date dateObj=convertStringToDateTimeWithAMPM(dateStr);

        if(0<elapsedDays){
             strMSgdaystime = elapsedDays+" "+"Days ";
        }else{
            strMSgdaystime = "";
        }
        if(dateObj.before(todayDate)){
            strMsg = "Starts in :";
            return true;
        }else if(dateObj.after(todayDate)){
            strMsg = "Ends in :";
            return false;
        }else{
            strMsg = "Starts in :";
            return true;
        }
    }

    private Date convertStringToDateTimeWithAMPM(String dateStr){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
        //SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/M/yyyy hh:mm:ss");

        try {

            Date dateObj = simpleDateFormat.parse(dateStr);
        return  dateObj;
        } catch (ParseException e) {
            e.printStackTrace();
            return  getCurrentDateandTime();
        }

    }
    private void getCalculateTimes(String endDate) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");

        try {
            Date date1 =getCurrentDateandTime();
            Date date2 = simpleDateFormat.parse(endDate);
            calculateDuration(date1,date2);
            Log.i("STARTEND1","Start"+date1+"End"+date2);

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }



    private void calculateDuration(Date startDate, Date endDate) {
        long different = endDate.getTime() - startDate.getTime();
        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

         elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

         elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

         elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

         elapsedSeconds = different / secondsInMilli;

        System.out.printf(
                "%d days, %d hours, %d minutes, %d seconds%n",
                elapsedDays, elapsedHours, elapsedMinutes, elapsedSeconds);

        Log.i("Duration","elapsedDays"+elapsedDays+"elapsedHours"+elapsedHours+"elapsedMinutes"+elapsedMinutes+"elapsedSeconds"+elapsedSeconds);


    }

    @Override
    public int getItemCount() {
        return responseBeanList.size();
    }
    public void setOnLoadMoreListener(OnLoadMoreListener mOnLoadMoreListener) {
        this.mOnLoadMoreListener = mOnLoadMoreListener;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    class ViewHolderOne extends RecyclerView.ViewHolder {
        public TextView txt_eventtype, txt_endsin;
        public LinearLayout llroot;

        public ViewHolderOne(View itemView) {
            super(itemView);

            txt_eventtype = (TextView) itemView.findViewById(R.id.tveventtype);
            txt_endsin = (TextView) itemView.findViewById(R.id.tvendins);
            llroot = (LinearLayout)itemView.findViewById(R.id.llrootcard);




        }


    }










}