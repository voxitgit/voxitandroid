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
import com.triton.voxit.Activity.FilmyListActivity;
import com.triton.voxit.R;
import com.triton.voxit.model.RecyclerViewItem;
import com.triton.voxit.model.TopGenre;

import java.util.List;

public class FilmyAdapter extends RecyclerView.Adapter implements View.OnClickListener {
    //Declare List of Recyclerview Items
    List<RecyclerViewItem> recyclerViewItems;
    RecyclerViewItem recyclerViewItem;
    //Header Item Type
    private static final int HEADER_ITEM = 0;

    ////Food Item Type
    private static final int FOOD_ITEM = 1;
    Context mContext;

    public FilmyAdapter(List<RecyclerViewItem> recyclerViewItems, Context mContext) {
        this.recyclerViewItems = recyclerViewItems;
        this.mContext = mContext;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View row;
        //Check fot view Type inflate layout according to it
//        if (viewType == HEADER_ITEM) {
//            row = inflater.inflate(R.layout.comedy_header, parent, false);
//            return new HeaderHolder(row);
//
//        } else if (viewType == FOOD_ITEM) {
        if (viewType == FOOD_ITEM) {
            row = inflater.inflate(R.layout.top_genre_list_items, parent, false);
            return new FoodItemHolder(row);

        }
        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        RecyclerViewItem recyclerViewItem = recyclerViewItems.get(position);
        //Check holder instance to populate data  according to it
//        if (holder instanceof HeaderHolder) {
//            HeaderHolder headerHolder = (HeaderHolder) holder;
//            Header header = (Header) recyclerViewItem;
//            //set data
//            headerHolder.texViewHeaderText.setText(header.getHeaderText());
//
//        } else if (holder instanceof FoodItemHolder) {
        if (holder instanceof FoodItemHolder) {
            FoodItemHolder foodItemHolder = (FoodItemHolder) holder;
            final TopGenre foodItem = (TopGenre) recyclerViewItem;
            //set data

            Glide.with(mContext).load(foodItem.getImageUrl()).into(foodItemHolder.imageViewFood);
            //foodItemHolder.imageViewFood.setOnClickListener(this);
            foodItemHolder.imageViewFood.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   // FilmyListActivity.filmy_list = recyclerViewItems.get(position);
                    FilmyListActivity.toRefresh = true;
                    mContext.startActivity(new Intent(mContext, FilmyListActivity.class)
                            .putExtra("imageUrl",foodItem.getImageUrl()).setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));

                }
            });

        }

    }

    @Override
    public int getItemViewType(int position) {
        //here we can set view type
        RecyclerViewItem recyclerViewItem = recyclerViewItems.get(position);
        //if its header then return header item
//        if (recyclerViewItem instanceof Header)
//            return HEADER_ITEM;
//
//            //if its FoodItem then return Food item
//        else if (recyclerViewItem instanceof TopGenre)
        if (recyclerViewItem instanceof TopGenre)
            return FOOD_ITEM;
        else
            return super.getItemViewType(position);

    }

    @Override
    public int getItemCount() {
        return recyclerViewItems.size();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.itemImage:
               // FilmyListActivity.listItems = recyclerViewItems.get(position);
                FilmyListActivity.toRefresh = true;
                mContext.startActivity(new Intent(mContext, FilmyListActivity.class).setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));

                break;
        }
    }

    //Food item holder
    private class FoodItemHolder extends RecyclerView.ViewHolder {

        ImageView imageViewFood;

        FoodItemHolder(View itemView) {
            super(itemView);

            imageViewFood = itemView.findViewById(R.id.itemImage);

        }
    }
    //header holder
    private class HeaderHolder extends RecyclerView.ViewHolder {
        TextView texViewHeaderText;


        HeaderHolder(View itemView) {
            super(itemView);
            texViewHeaderText = itemView.findViewById(R.id.title);

        }
    }

}
