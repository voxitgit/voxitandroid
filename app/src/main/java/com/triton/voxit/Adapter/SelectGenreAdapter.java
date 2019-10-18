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
import com.triton.voxit.model.GenreData;
import com.triton.voxit.model.RecyclerViewItem;

import java.util.ArrayList;
import java.util.List;

public class SelectGenreAdapter extends RecyclerView.Adapter<SelectGenreAdapter.FoodItemHolder> {
    //Declare List of Recyclerview Items
    ArrayList<GenreData> recyclerViewItems;
    //Header Item Type

    Context mContext;

    public SelectGenreAdapter(ArrayList<GenreData> recyclerViewItems, Context mContext) {
        this.recyclerViewItems = recyclerViewItems;
        this.mContext = mContext;
    }

    @Override
    public FoodItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View row;

            row = inflater.inflate(R.layout.select_genre_list_item, parent, false);
        return new FoodItemHolder(row);

    }

    @Override
    public void onBindViewHolder(FoodItemHolder holder, int position) {
        GenreData recyclerViewItem = recyclerViewItems.get(position);
        //Check holder instance to populate data  according to it

        String upperString = recyclerViewItem.getName().substring(0,1).toUpperCase() + recyclerViewItem.getName().substring(1);
            holder.texViewFoodTitle.setText(upperString);

            Glide.with(mContext).load(recyclerViewItem.getImage()).into(holder.imageViewFood);



    }

    @Override
    public int getItemViewType(int position) {

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
