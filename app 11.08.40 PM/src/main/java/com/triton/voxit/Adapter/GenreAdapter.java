package com.triton.voxit.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.triton.voxit.Activity.AudioActivity;
import com.triton.voxit.Activity.LoginActivity;
import com.triton.voxit.R;
import com.triton.voxit.SessionManager.SessionManager;
import com.triton.voxit.model.Genre;
import com.triton.voxit.model.GenreResponseBean;
import com.triton.voxit.model.RecyclerViewItem;
import com.triton.voxit.model.Trending;

import java.util.ArrayList;
import java.util.List;

public class GenreAdapter extends RecyclerView.Adapter<GenreAdapter.FoodItemHolder> {
    //Declare List of Recyclerview Items
//    List<RecyclerViewItem> recyclerViewItems;
    //Header Item Type
    private static final int HEADER_ITEM = 0;
    //Footer Item Type
    private static final int FOOD_ITEM = 1;
    private final ArrayList<GenreResponseBean> recyclerViewItems;
   // private final String flag;
    SessionManager session;
    Context mContext;

    public GenreAdapter(ArrayList<GenreResponseBean> recyclerViewItems, Context mContext) {
        this.recyclerViewItems = recyclerViewItems;
        this.mContext = mContext;
       // this.flag = flag;
    }

    @Override
    public FoodItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View row;
        //Check fot view Type inflate layout according to it
//        if (viewType == FOOD_ITEM) {
            row = inflater.inflate(R.layout.genre_layout, parent, false);
            return new FoodItemHolder(row);

//        }
//        return null;
    }

    @Override
    public void onBindViewHolder(FoodItemHolder holder, int position) {
        final GenreResponseBean recyclerViewItem = recyclerViewItems.get(position);
        session = new SessionManager(mContext);
            Glide.with(mContext).load(recyclerViewItem.getImage()).into(holder.imageViewFood);
            String upperString = recyclerViewItem.getName().substring(0,1).toUpperCase() + recyclerViewItem.getName().substring(1);
//            holder.texViewFoodTitle.setText(recyclerViewItem.getName());
            holder.texViewFoodTitle.setText(upperString);

        holder.imageViewFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (session.isLoggedIn()){

                    AudioActivity.toRefresh = true;
//                    mContext.startActivity(new Intent(mContext, AudioActivity.class)
//                            .putExtra("song",recyclerViewItem.getAudio_path())
//                            .putExtra("title",recyclerViewItem.getTitle())
//                            .putExtra("description",recyclerViewItem.getDiscription())
//                            .putExtra("image",recyclerViewItem.getImage_path())
//                            .putExtra("name",recyclerViewItem.getName())
//                            .putExtra("audio_length",recyclerViewItem.getAudio_length())
//                            .setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
                }else{

                    mContext.startActivity(new Intent(mContext, LoginActivity.class)
                            .setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
                }

            }
        });

    }

    @Override
    public int getItemViewType(int position) {
        //here we can set view type
//        RecyclerViewItem recyclerViewItem = recyclerViewItems.get(position);
//        if (recyclerViewItem instanceof Genre)
//            return FOOD_ITEM;
//        else
            return super.getItemViewType(position);

    }

    @Override
    public int getItemCount() {
        return recyclerViewItems.size();
    }
    //Food item holder
    public class FoodItemHolder extends RecyclerView.ViewHolder {
        TextView texViewFoodTitle;
        ImageView imageViewFood;

        FoodItemHolder(View itemView) {
            super(itemView);
            texViewFoodTitle = itemView.findViewById(R.id.tvTitle);
            imageViewFood = itemView.findViewById(R.id.itemImage);

        }
    }


}
