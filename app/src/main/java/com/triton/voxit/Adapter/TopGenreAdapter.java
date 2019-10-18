package com.triton.voxit.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.triton.voxit.Activity.AudioActivity;
import com.triton.voxit.Activity.Dashboard;
import com.triton.voxit.Activity.GenreListActivity;
import com.triton.voxit.Activity.LoginActivity;
import com.triton.voxit.R;
import com.triton.voxit.SessionManager.SessionManager;
import com.triton.voxit.Utlity.MediaPlayerSingleton;
import com.triton.voxit.app.App;
import com.triton.voxit.model.AudioDetailsResponseBean;
import com.triton.voxit.model.Genre;
import com.triton.voxit.model.Header;
import com.triton.voxit.model.PreventListener;
import com.triton.voxit.model.RecyclerViewItem;
import com.triton.voxit.model.TopGenre;

import java.util.ArrayList;
import java.util.List;

public class TopGenreAdapter extends RecyclerView.Adapter<TopGenreAdapter.FoodItemHolder> {

    private final ArrayList<AudioDetailsResponseBean> recyclerViewItems;
    private final String upperString;
    // private final String flag;
    Context mContext;
    SessionManager session;
    private boolean canStart = true;
    private MediaPlayerSingleton mps;
    private Dashboard activity;
    private String generType;

    public TopGenreAdapter(ArrayList<AudioDetailsResponseBean> recyclerViewItems, Context mContext, Dashboard activity, String generType, String upperString) {
        this.recyclerViewItems = recyclerViewItems;
        this.mContext = mContext;
        mps = MediaPlayerSingleton.getInstance(mContext);
        this.activity = activity;
        this.generType = generType;
        this.upperString = upperString;
    }

    @Override
    public FoodItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View row;

        row = inflater.inflate(R.layout.top_genre_list_items, parent, false);
        return new FoodItemHolder(row);

    }

    @Override
    public void onBindViewHolder(final FoodItemHolder holder, final int position) {
        final AudioDetailsResponseBean recyclerViewItem = recyclerViewItems.get(position);
        //Check holder instance to populate data  according to it
        session = new SessionManager(mContext);
        Log.e("audio details1", String.valueOf(recyclerViewItem.getAudio_id()));

        Glide.with(mContext).load(recyclerViewItem.getImage_path()).into(holder.imageViewFood);
        holder.texViewHeaderText.setText(recyclerViewItem.getTitle());

        if (position == 9) {
            holder.moretest.setVisibility(View.VISIBLE);
        } else {
            holder.moretest.setVisibility(View.GONE);
        }
//        holder.imageViewFood.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!App.appUtils.isNetAvailable()) {
//                    alertUserP(mContext, "Connection Error", "No Internet connection available", "OK");
//                }else {
//                    if (session.isLoggedIn()) {
//                        AudioActivity.toRefresh = true;
//                        mContext.startActivity(new Intent(mContext, AudioActivity.class)
//                                .putExtra("jockey_id", String.valueOf(recyclerViewItem.getJockey_id()))
//                                .putExtra("song", recyclerViewItem.getAudio_path())
//                                .putExtra("title", recyclerViewItem.getTitle())
//                                .putExtra("description", recyclerViewItem.getDiscription())
//                                .putExtra("image", recyclerViewItem.getImage_path())
//                                .putExtra("name", recyclerViewItem.getName())
//                                .putExtra("audio_length", recyclerViewItem.getAudio_length())
//                                .putExtra("audio_id", recyclerViewItem.getAudio_id())
//                                .putExtra("type", "TopGenre")
//                                .putExtra("songsList", recyclerViewItems));
////                                .setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
//
//                    } else {
//
//                        mContext.startActivity(new Intent(mContext, LoginActivity.class)
//                                .setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
//                    }
//                }
//
//            }
//        });
        holder.moretest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, GenreListActivity.class)
                        .putExtra("name", upperString)
                        .putExtra("subType", generType)
                        .putExtra("songsList", recyclerViewItems));
            }
        });

        holder.imageViewFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (PreventListener.getClickEnable()) {
                    if (!App.appUtils.isNetAvailable()) {
                        alertUserP(mContext, "Connection Error", "No Internet connection available", "OK");
                    } else {
                        if (session.isLoggedIn()) {
                            //AudioActivity.toRefresh = true;
                            if (mps.getMediaPlayerStatus().equalsIgnoreCase("empty")) {
                                Log.e("TopGenre-----", "empty TopGenre");
                                mps.releasePlayer();
                                //holder.imageViewFood.setEnabled(false);
                                mContext.startActivity(new Intent(mContext, AudioActivity.class)
                                        .putExtra("jockey_id", String.valueOf(recyclerViewItem.getJockey_id()))
                                        .putExtra("song", recyclerViewItem.getAudio_path())
                                        .putExtra("title", recyclerViewItem.getTitle())
                                        .putExtra("description", recyclerViewItem.getDiscription())
                                        .putExtra("image", recyclerViewItem.getImage_path())
                                        .putExtra("name", recyclerViewItem.getName())
                                        .putExtra("audio_length", recyclerViewItem.getAudio_length())
                                        .putExtra("audio_id", recyclerViewItem.getAudio_id())
                                        .putExtra("type", "TopGenre")
                                        .putExtra("subType", generType)
                                        .putExtra("songsList", recyclerViewItems));
                                //.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));

                            } else if (mps.getMediaPlayerStatus().equalsIgnoreCase("pause") || mps.getMediaPlayerStatus().equalsIgnoreCase("playing")) {
                                if (mps.getType().equalsIgnoreCase("TopGenre") || mps.getType().equalsIgnoreCase("Trending")) {
                                    Log.e("TopGenre-----", "if TopGenre");
                                    activity.playSong("TopGenre", recyclerViewItem.getAudio_path(), recyclerViewItem.getName(), recyclerViewItem.getImage_path(), recyclerViewItem.getTitle(), position, generType, recyclerViewItem.getAudio_id());
                                } else {
                                    Log.e("TopGenre-----", "else TopGenre");
                                    mps.releasePlayer();
                                    mContext.startActivity(new Intent(mContext, AudioActivity.class)
                                            .putExtra("jockey_id", String.valueOf(recyclerViewItem.getJockey_id()))
                                            .putExtra("song", recyclerViewItem.getAudio_path())
                                            .putExtra("title", recyclerViewItem.getTitle())
                                            .putExtra("description", recyclerViewItem.getDiscription())
                                            .putExtra("image", recyclerViewItem.getImage_path())
                                            .putExtra("name", recyclerViewItem.getName())
                                            .putExtra("audio_length", recyclerViewItem.getAudio_length())
                                            .putExtra("audio_id", recyclerViewItem.getAudio_id())
                                            .putExtra("type", "TopGenre")
                                            .putExtra("subType", generType)
                                            .putExtra("songsList", recyclerViewItems));
                                    // .setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
                                }

                            } else {
                                mContext.startActivity(new Intent(mContext, LoginActivity.class)
                                        .setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
                            }
                        }
                    }

                }

            }
        });
    }

    public void alertUserP(Context context, String title, String msg, String btn) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
        builder.setMessage(msg).setTitle(title).setCancelable(false)
                .setPositiveButton(btn, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        android.app.AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public int getItemViewType(int position) {

        return super.getItemViewType(position);

    }

    @Override
    public int getItemCount() {
        return Math.min(recyclerViewItems.size(), 10);
//        return recyclerViewItems.size();
    }

    //Food item holder
    public class FoodItemHolder extends RecyclerView.ViewHolder {
        TextView texViewHeaderText;
        LinearLayout moretest;
        ImageView imageViewFood;

        FoodItemHolder(View itemView) {
            super(itemView);
            imageViewFood = itemView.findViewById(R.id.itemImage);
            texViewHeaderText = itemView.findViewById(R.id.tvTitle);
            moretest = itemView.findViewById(R.id.moretest);
        }
    }

}
