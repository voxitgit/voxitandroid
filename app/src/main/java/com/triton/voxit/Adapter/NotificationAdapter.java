package com.triton.voxit.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
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
import com.triton.voxit.Activity.NotificationActivity;
import com.triton.voxit.R;
import com.triton.voxit.SessionManager.SessionManager;
import com.triton.voxit.Utlity.MediaPlayerSingleton;
import com.triton.voxit.model.AudioDetailData;
import com.triton.voxit.model.NotifyDataList;

import java.util.ArrayList;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder> {
    private final Context mContext;
    private final ArrayList<NotifyDataList> listData;
    private boolean isCheck = true;
    SessionManager session;
    private MediaPlayerSingleton mps;
    private NotificationActivity activity;

    public NotificationAdapter(Context context, ArrayList<NotifyDataList> notifyList, NotificationActivity activity) {
        this.mContext = context;
        this.listData = notifyList;
        mps = MediaPlayerSingleton.getInstance(context);
        this.activity = activity;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.notify_list_items, viewGroup, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int i) {
        final NotifyDataList data = listData.get(i);
        final AudioDetailData audioData = data.getAudioDetail();

        final int pos = i;

        holder.titleTv.setText(data.getTitle());
        holder.dateTv.setText(data.getCreate_date() + "," + "\t" + data.getCreate_time());
        holder.msgTv.setText(data.getDescription());
        holder.msgTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isCheck) {
                    holder.msgTv.setMaxLines(12);
                    isCheck = false;
                } else {
                    holder.msgTv.setMaxLines(2);
                    isCheck = true;
                }
            }
        });

//        holder.readTV.setText("Unread Message");

        if (data.getImages() != null) {
            holder.image_ll.setVisibility(View.VISIBLE);
            Glide.with(mContext).load(data.getImages()).into(holder.image_noti);
        } else {
            holder.image_ll.setVisibility(View.GONE);
        }

        final String jockeyid = String.valueOf(data.getAudioDetail().getJockey_id());
        final String audioid = String.valueOf(data.getAudioDetail().getAudio_id());

        holder.notifyLt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if ((mps.getMediaPlayerStatus().equalsIgnoreCase("playing") || mps.getMediaPlayerStatus().equalsIgnoreCase("pause")) && mps.getType().equalsIgnoreCase("Notify")) {

                    activity.playSong("Notify", audioData.getAudio_path(), audioData.getName(),
                            audioData.getImage_path(), audioData.getTitle(), pos, "empty", audioData.getAudio_id() + "", audioData.getJockey_id() + "");

                } else {
                    holder.readTV.setText("");
                    // ShowDialogueMethod("Alert",data.getType());
                    holder.readTV.setText("");
                    Log.i("TESTA", "" + data.getType().equals("audio") + data.getType().equals("all"));
                    if (data.getType().equals("audio") || data.getType().equals("all")) {
                        Intent intent = new Intent(mContext, AudioActivity.class);
                        intent.putExtra("jockey_id", jockeyid);
                        intent.putExtra("song", data.getAudioDetail().getAudio_path());
                        intent.putExtra("title", data.getAudioDetail().getTitle());
                        intent.putExtra("description", data.getAudioDetail().getDiscription());
                        intent.putExtra("image", data.getAudioDetail().getImage_path());
                        intent.putExtra("name", data.getAudioDetail().getName());
                        intent.putExtra("audio_length", data.getAudioDetail().getAudio_length());
                        intent.putExtra("audio_id", audioid);
                        intent.putExtra("type", "Notify");
                        intent.putExtra("songsList", listData);
                        mContext.startActivity(intent);
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

    private void ShowDialogueMethod(String title, String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(mContext).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        alertDialog.show();
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView image_noti;
        public TextView titleTv, msgTv, dateTv, readTV;
        public LinearLayout notifyLt;
        public LinearLayout image_ll;

        public MyViewHolder(View view) {
            super(view);
            titleTv = view.findViewById(R.id.notify_title);
            dateTv = view.findViewById(R.id.date);
            msgTv = view.findViewById(R.id.notify_msg);
            readTV = view.findViewById(R.id.read);
            notifyLt = view.findViewById(R.id.ll_notify);
            image_ll = view.findViewById(R.id.image_ll);
            image_noti = view.findViewById(R.id.image_noti);

            session = new SessionManager(mContext);

        }
    }
}
