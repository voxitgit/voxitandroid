package com.triton.voxit.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.triton.voxit.R;
import com.triton.voxit.model.Genre;
import com.triton.voxit.model.RecyclerViewItem;
import com.triton.voxit.model.Trending;

import java.util.List;

public class FilmyListAdapter extends RecyclerView.Adapter {
    //Declare List of Recyclerview Items
    List<RecyclerViewItem> recyclerViewItems;
    //Header Item Type
    private static final int HEADER_ITEM = 0;
    //Footer Item Type
    private static final int FOOD_ITEM = 1;

    Context mContext;

    public FilmyListAdapter(List<RecyclerViewItem> recyclerViewItems, Context mContext) {
        this.recyclerViewItems = recyclerViewItems;
        this.mContext = mContext;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View row;
        //Check fot view Type inflate layout according to it
        if (viewType == FOOD_ITEM) {
            row = inflater.inflate(R.layout.filmy_single_list_items, parent, false);
            return new FoodItemHolder(row);

        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        RecyclerViewItem recyclerViewItem = recyclerViewItems.get(position);
        //Check holder instance to populate data  according to it
        if (holder instanceof FoodItemHolder) {
            FoodItemHolder foodItemHolder = (FoodItemHolder) holder;
            Trending foodItem = (Trending) recyclerViewItem;
            //set data
            foodItemHolder.texViewFoodTitle.setText(foodItem.getTitle());
            foodItemHolder.texViewDescription.setText(foodItem.getDescription());
            Glide.with(mContext).load(foodItem.getImageUrl()).into(foodItemHolder.imageViewFood);


        }

    }

    @Override
    public int getItemViewType(int position) {
        //here we can set view type
        RecyclerViewItem recyclerViewItem = recyclerViewItems.get(position);
        if (recyclerViewItem instanceof Trending)
            return FOOD_ITEM;
        else
            return super.getItemViewType(position);

    }

    @Override
    public int getItemCount() {
        return recyclerViewItems.size();
    }
    //Food item holder
    private class FoodItemHolder extends RecyclerView.ViewHolder {
        TextView texViewFoodTitle, texViewDescription;
        ImageView imageViewFood;

        FoodItemHolder(View itemView) {
            super(itemView);
            texViewFoodTitle = itemView.findViewById(R.id.tvTitle);
            texViewDescription = itemView.findViewById(R.id.tvDes);
            imageViewFood = itemView.findViewById(R.id.itemImage);

        }
    }


}
