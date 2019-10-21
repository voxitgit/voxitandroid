package com.triton.voxit.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.triton.voxit.Activity.AudioActivity;
import com.triton.voxit.Activity.Dashboard;
import com.triton.voxit.Activity.LoginActivity;
import com.triton.voxit.R;
import com.triton.voxit.SessionManager.SessionManager;
import com.triton.voxit.Utlity.MediaPlayerSingleton;
import com.triton.voxit.app.App;
import com.triton.voxit.model.PreventListener;
import com.triton.voxit.model.TrendingResponseBean;
import com.triton.voxit.preference.SFMPreference;

import java.util.ArrayList;

public class RecentSongAdapter extends RecyclerView.Adapter<RecentSongAdapter.FoodItemHolder> {
    //private final String flag;
    //Declare List of Recyclerview Items
//    List<RecyclerViewItem> recyclerViewItems;
    //Header Item Type
    private static final int HEADER_ITEM = 0;
    //Footer Item Type
    private static final int FOOD_ITEM = 1;
    private static final String PREFERENCES = "";
    private final ArrayList<TrendingResponseBean> recyclerViewItems;

    Context mContext;
    private SharedPreferences.Editor LoginString;
    private SFMPreference sharedpreference;
    SessionManager session;
    private MediaPlayerSingleton mps;
    private Dashboard activity;

    public RecentSongAdapter(ArrayList<TrendingResponseBean> recyclerViewItems, Context mContext, Dashboard activity) {
        this.recyclerViewItems = recyclerViewItems;
        this.mContext = mContext;
        mps = MediaPlayerSingleton.getInstance(mContext);
        this.activity = activity;
        // this.flag = flag;
    }

    @Override
    public FoodItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View row;
        row = inflater.inflate(R.layout.list_single_card, parent, false);
        return new FoodItemHolder(row);
    }

    @Override
    public void onBindViewHolder(FoodItemHolder holder, final int position) {
        final TrendingResponseBean recyclerViewItem = recyclerViewItems.get(position);
        session = new SessionManager(mContext);
        holder.texViewFoodTitle.setText(recyclerViewItem.getTitle());
//            foodItemHolder.texViewDescription.setText(recyclerViewItem.getDiscription());
        // Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(),R.drawable.circularimage);
        //  Bitmap circularBitmap = ImageConverter.getRoundedCornerBitmap(bitmap, 100);
        // circularImageView.setImageBitmap(circularBitmap);

        Glide.with(mContext).load(recyclerViewItem.getImage_path()).into(holder.imageViewFood);
        holder.imageViewFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (PreventListener.getClickEnable()) {
                    if (!App.appUtils.isNetAvailable()) {
                        alertUserP(mContext, "Connection Error", "No Internet connection available", "OK");
                    }else {
                        if (session.isLoggedIn()) {
                            AudioActivity.toRefresh = true;
                            AudioActivity.songsList = recyclerViewItems.get(position);
                            if (mps.getMediaPlayerStatus().equalsIgnoreCase("empty")) {
                                Log.e("Trending", "empty Trending");
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
                                        .putExtra("type", "Trending")
                                        .putExtra("songsList", recyclerViewItems)
                                        .setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));

                            } else if (mps.getMediaPlayerStatus().equalsIgnoreCase("pause") || mps.getMediaPlayerStatus().equalsIgnoreCase("playing")) {
                                if (mps.getType().equalsIgnoreCase("Trending") || mps.getType().equalsIgnoreCase("TopGenre")) {
                                    Log.e("Trending", "if Trending");
                                    activity.playSong("Trending", recyclerViewItem.getAudio_path(), recyclerViewItem.getName(), recyclerViewItem.getImage_path(), recyclerViewItem.getTitle(), position,"empty", recyclerViewItem.getAudio_id());
                                } else {
                                    Log.e("Trending", "else Trending");
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
                                            .putExtra("type", "Trending")
                                            .putExtra("songsList", recyclerViewItems)
                                            .setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
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
//            holder.imageViewFood.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
////                    if (flag.equals("L")){
////                        mContext.startActivity(new Intent(mContext, LoginActivity.class)
////                                .putExtra("login","F").setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
////                    }else{
////                        AudioActivity.toRefresh = true;
////                        AudioActivity.songsList = recyclerViewItems.get(position);
////                        mContext.startActivity(new Intent(mContext, AudioActivity.class)
////                               .putExtra("song",recyclerViewItem.getAudio_path())
////                                .putExtra("title",recyclerViewItem.getTitle())
////                                .putExtra("description",recyclerViewItem.getDiscription())
////                                .putExtra("image",recyclerViewItem.getImage_path())
////                                .putExtra("name",recyclerViewItem.getName())
////                                .putExtra("audio_length",recyclerViewItem.getAudio_length())
////                                .putExtra("audio_id",recyclerViewItem.getAudio_id())
////                                .putExtra("type","Trending")
////                                .putExtra("songsList", recyclerViewItems)
////                                .setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
////                    }
//                    if (!App.appUtils.isNetAvailable()) {
//                        alertUserP(mContext, "Connection Error", "No Internet connection available", "OK");
//                    }else {
//                        if (session.isLoggedIn()) {
//                            AudioActivity.toRefresh = true;
//                            AudioActivity.songsList = recyclerViewItems.get(position);
//                            mContext.startActivity(new Intent(mContext, AudioActivity.class)
//                                    .putExtra("jockey_id", String.valueOf(recyclerViewItem.getJockey_id()))
//                                    .putExtra("song", recyclerViewItem.getAudio_path())
//                                    .putExtra("title", recyclerViewItem.getTitle())
//                                    .putExtra("description", recyclerViewItem.getDiscription())
//                                    .putExtra("image", recyclerViewItem.getImage_path())
//                                    .putExtra("name", recyclerViewItem.getName())
//                                    .putExtra("audio_length", recyclerViewItem.getAudio_length())
//                                    .putExtra("audio_id", recyclerViewItem.getAudio_id())
//                                    .putExtra("type", "Trending")
//                                    .putExtra("songsList", recyclerViewItems)
//                                    .setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
//
//                        } else {
//                            mContext.startActivity(new Intent(mContext, LoginActivity.class)
//                                    .setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
//                        }
//                    }
//
//                }
//            });

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
        return recyclerViewItems.size();
    }
    //Food item holder
    public class FoodItemHolder extends RecyclerView.ViewHolder {
        TextView texViewFoodTitle, texViewDescription;
        //ImageView imageViewFood;
        ImageView imageViewFood;

        FoodItemHolder(View itemView) {
            super(itemView);
            texViewFoodTitle = itemView.findViewById(R.id.tvTitle);
            texViewDescription = itemView.findViewById(R.id.tvDes);
            imageViewFood = itemView.findViewById(R.id.itemImage);
        }
    }

}
