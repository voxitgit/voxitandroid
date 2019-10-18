package com.triton.voxit.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.triton.voxit.R;
import com.triton.voxit.model.RecyclerViewItem;

import java.util.ArrayList;
import java.util.List;

public class SlidingAdapter  extends RecyclerView.Adapter<SlidingAdapter.ViewHolder>{

    ArrayList<String> urls;
    List<RecyclerViewItem> ImgUrl;
    Context context;
    //DasboardAdapter context;

    //constructor
    public SlidingAdapter(List<RecyclerViewItem> ImgUrl, Context context)
    {
        this.ImgUrl = ImgUrl;
        this.context = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        private ImageView image;

        public ViewHolder(View v)
        {
            super(v);
           // image =(ImageView)v.findViewById(R.id.sliding_img);
        }

        public ImageView getImage(){ return this.image;}
    }

    @Override
    public SlidingAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.sliding_image, parent, false);
       // v.setLayoutParams(new RecyclerView.LayoutParams(250,250));
       // v.setLayoutParams(new RecyclerView.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position)
    {
        Glide.with(this.context)
                .load(ImgUrl.get(position))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.getImage());
    }

    @Override
    public int getItemCount()
    {
//        Log.e("getItemCount", String.valueOf(getItemCount()));
//        if(getItemCount()==1){
//
//        }

        return ImgUrl.size();
    }
    @Override
    public int getItemViewType(int position) {
        Log.e("List", String.valueOf(ImgUrl.get(position)));
        if (position == 0) {
            Log.e("position", String.valueOf(position));
            return 0;
        } else if(position ==1) {
            Log.e("position1", String.valueOf(position));
            return 1;
        } else {
            return 2;
        }

    }
}
