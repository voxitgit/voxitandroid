package com.triton.voxit.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.triton.voxit.Activity.AudioActivity;
import com.triton.voxit.Activity.GenreListActivity;
import com.triton.voxit.R;
import com.triton.voxit.SessionManager.SessionManager;
import com.triton.voxit.Utlity.MediaPlayerSingleton;
import com.triton.voxit.app.App;
import com.triton.voxit.model.AudioDetailsResponseBean;
import com.triton.voxit.model.PreventListener;

import java.util.ArrayList;

public class GenereListAdapter extends RecyclerView.Adapter<GenereListAdapter.MyViewHolder> {
    private final Context context;
    private final ArrayList<AudioDetailsResponseBean> audioList;
    private final MediaPlayerSingleton mps;
    private GenreListActivity activity;

    public GenereListAdapter(Context context, ArrayList<AudioDetailsResponseBean> audiosongsList, GenreListActivity activity) {
        this.context = context;
        this.audioList = audiosongsList;
        mps = MediaPlayerSingleton.getInstance(context);
        this.activity = activity;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.genre_list_items, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
       final AudioDetailsResponseBean objectList = audioList.get(position);
        holder.nameTv.setText(objectList.getTitle());
        holder.desTv.setText(objectList.getDiscription());
        Glide.with(context).load(objectList.getImage_path()).into(holder.imageView);

        holder.llView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!App.appUtils.isNetAvailable()) {
                    alertUserP(context, "Connection Error", "No Internet connection available", "OK");
                } else {
                    Log.e("audio id", String.valueOf(audioList.get(position).getAudio_id()));


                    activity.playSong("TopGenre", objectList.getAudio_path(), objectList.getName(), objectList.getImage_path(), objectList.getTitle(), position, "empty", objectList.getAudio_id());
                   /* mps.releasePlayer();
                    context.startActivity(new Intent(context, AudioActivity.class)
                            .putExtra("jockey_id", String.valueOf(audioList.get(position).getJockey_id()))
                            .putExtra("song", audioList.get(position).getAudio_path())
                            .putExtra("title", audioList.get(position).getTitle())
                            .putExtra("description", audioList.get(position).getDiscription())
                            .putExtra("image", audioList.get(position).getImage_path())
                            .putExtra("name", audioList.get(position).getName())
                            .putExtra("audio_length", audioList.get(position).getAudio_length())
                            .putExtra("type", "TopGenre")
                            .putExtra("audio_id", String.valueOf(audioList.get(position).getAudio_id()))
                            .putExtra("songsList", audioList));*/

                }
            }
        });

    }

    @Override
    public int getItemCount() {
        Log.e("lllss", String.valueOf(audioList.size()));
        return audioList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView nameTv, desTv;
        public ImageView imageView;
        public LinearLayout llView;

        public MyViewHolder(View view) {
            super(view);
            nameTv = view.findViewById(R.id.tvTitle);
            desTv = view.findViewById(R.id.tvDes);
            imageView = view.findViewById(R.id.itemImage);
            llView = view.findViewById(R.id.llview);
        }
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


}

