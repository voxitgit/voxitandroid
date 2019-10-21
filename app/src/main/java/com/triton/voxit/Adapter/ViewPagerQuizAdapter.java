package com.triton.voxit.Adapter;

import android.content.Context;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.triton.voxit.R;
import com.triton.voxit.model.BannerResponseBean;
import com.triton.voxit.model.EventBean;
import com.triton.voxit.responsepojo.VCornerQuestionsResponse;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerQuizAdapter extends PagerAdapter {
    private ArrayList<EventBean> eventBeanArrayList;
    private LayoutInflater inflater;
    private Context context;

    int count;


    public ViewPagerQuizAdapter(Context context,  ArrayList<EventBean> eventBeanArrayList) {
        this.context = context;
        this.eventBeanArrayList=eventBeanArrayList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return eventBeanArrayList.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View imageLayout = inflater.inflate(R.layout.quiz_page_adapter, view, false);



        assert imageLayout != null;
        final TextView tvQuestions = (TextView) imageLayout.findViewById(R.id.tvquestions);
        final TextView tvAns1 = (TextView) imageLayout.findViewById(R.id.tvAns1);
        final TextView tvAns2 = (TextView) imageLayout.findViewById(R.id.tvAns2);
        final TextView tvAns3 = (TextView) imageLayout.findViewById(R.id.tvAns3);
        final TextView tvAns4 = (TextView) imageLayout.findViewById(R.id.tvAns4);
        final Button btnNext = (Button)imageLayout.findViewById(R.id.btnNext);

        tvQuestions.setText(""+eventBeanArrayList.get(position).getTitle());


        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(count<eventBeanArrayList.size()-1){
                    count = count+1;
                    Log.i("Count:",""+count);
                }
            }
        });










      //  imageView.setImageResource(IMAGES.get(position));

        view.addView(imageLayout, 0);

        return imageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }


}
