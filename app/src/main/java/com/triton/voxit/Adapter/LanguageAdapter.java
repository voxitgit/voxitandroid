package com.triton.voxit.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.triton.voxit.R;
import com.triton.voxit.model.Genre;
import com.triton.voxit.model.LanguageData;
import com.triton.voxit.model.LanguageResponseData;
import com.triton.voxit.model.RecyclerViewItem;

import java.util.ArrayList;
import java.util.List;

public class LanguageAdapter extends RecyclerView.Adapter<LanguageAdapter.FoodItemHolder> {
    //Declare List of Recyclerview Items
    List<LanguageData> recyclerViewItems;

    private static final int FOOD_ITEM = 1;

    Context mContext;

    public LanguageAdapter(ArrayList<LanguageData> recyclerViewItems, Context mContext) {
        this.recyclerViewItems = recyclerViewItems;
        this.mContext = mContext;
    }

    @Override
    public FoodItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View row;
            row = inflater.inflate(R.layout.language_list_item, parent, false);
            return new FoodItemHolder(row);

    }

    @Override
    public void onBindViewHolder(FoodItemHolder holder, int position) {
        LanguageData recyclerViewItem = recyclerViewItems.get(position);

        String upperString = recyclerViewItem.getName().substring(0,1).toUpperCase() + recyclerViewItem.getName().substring(1);
            holder.LanguageTitle.setText(upperString);
            Glide.with(mContext).load(recyclerViewItem.getImage_path()).into(holder.itemImage);


    }

    @Override
    public int getItemViewType(int position) {
        //here we can set view type

            return super.getItemViewType(position);

    }

    @Override
    public int getItemCount() {
        return recyclerViewItems.size();
    }
    //Food item holder
    public class FoodItemHolder extends RecyclerView.ViewHolder {
        TextView LanguageTitle;
        CheckBox checkBox;
        ImageView itemImage;

        FoodItemHolder(View itemView) {
            super(itemView);
            LanguageTitle = itemView.findViewById(R.id.tvTitle);
            checkBox = itemView.findViewById(R.id.checkbox);
            itemImage = itemView.findViewById(R.id.itemImage);
        }
    }

}
