package com.triton.voxit.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.triton.voxit.Activity.Dashboard;
import com.triton.voxit.R;
import com.triton.voxit.SessionManager.SessionManager;
import com.triton.voxit.model.Header;
import com.triton.voxit.model.RecyclerViewItem;
import com.triton.voxit.model.TopGenreResponseBean;

import java.util.ArrayList;
import java.util.List;

public class TopGenreMultiListAdapter extends RecyclerView.Adapter<TopGenreMultiListAdapter.Holder> {
    private final ArrayList<TopGenreResponseBean> recyclerViewItems;
   // private final String flag;
    //    List<RecyclerViewItem> recyclerViewItems;
    Context mContext;
    SessionManager session;
    TopGenreAdapter imageAdapter;
    private Dashboard activity;


    public TopGenreMultiListAdapter(ArrayList<TopGenreResponseBean> recyclerViewItems, Context context,Dashboard activity) {
        this.recyclerViewItems = recyclerViewItems;
        this.mContext = context;
        this.activity = activity;
        //this.flag = flag;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View row;
            View itemLayoutView = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.topgenrevertical_layout, null);
            return new Holder(itemLayoutView);
    }

    @Override
    public void onBindViewHolder(Holder viewHolder, int position) {
        TopGenreResponseBean objectList = recyclerViewItems.get(position);

        String upperString = objectList.getGenre().substring(0,1).toUpperCase() + objectList.getGenre().substring(1);
        viewHolder.Title.setText(upperString);
        Log.e("audio det", String.valueOf(objectList.getAudiodetails().size()));

        TopGenreAdapter imageAdapter = new TopGenreAdapter(objectList.getAudiodetails(), mContext,activity,objectList.getGenre(),upperString);
        viewHolder.horizontalRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        viewHolder.horizontalRecyclerView.setAdapter(imageAdapter);
        viewHolder.horizontalRecyclerView.setNestedScrollingEnabled(false);
        viewHolder.horizontalRecyclerView.setHorizontalScrollBarEnabled(false);
        viewHolder.horizontalRecyclerView.setMotionEventSplittingEnabled(false);
        imageAdapter.notifyDataSetChanged();
    }
//    @Override
//    public void onResume() {
//        super.onResume();
//        imageAdapter.setCanStart(true);
//    }
    @Override
    public int getItemCount() {
        return recyclerViewItems.size();
    }

    public final static class Holder extends RecyclerView.ViewHolder {

        protected TextView Title;
        protected RecyclerView horizontalRecyclerView;

        public Holder(View view) {
            super(view);
            this.Title = (TextView) view.findViewById(R.id.title);
            this.horizontalRecyclerView = (RecyclerView) view.findViewById(R.id.list_recycler_view);
            this.horizontalRecyclerView.setLayoutManager(new LinearLayoutManager(this.horizontalRecyclerView.getContext(), LinearLayoutManager.HORIZONTAL, false));
            this.horizontalRecyclerView.setNestedScrollingEnabled(false);
            horizontalRecyclerView.setAdapter(null);
            horizontalRecyclerView.setMotionEventSplittingEnabled(false);
        }
    }

}
