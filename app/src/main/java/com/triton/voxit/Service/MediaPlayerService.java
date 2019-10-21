package com.triton.voxit.Service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.media.Rating;
import android.media.session.MediaController;
import android.media.session.MediaSession;
import android.media.session.MediaSessionManager;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.triton.voxit.R;
import com.triton.voxit.Utlity.MediaPlayerSingleton;

public class MediaPlayerService extends Service {

    public static final String ACTION_PLAY = "action_play";
    public static final String ACTION_PAUSE = "action_pause";
    public static final String ACTION_REWIND = "action_rewind";
    public static final String ACTION_FAST_FORWARD = "action_fast_foward";
    public static final String ACTION_NEXT = "action_next";
    public static final String ACTION_PREVIOUS = "action_previous";
    public static final String ACTION_STOP = "action_stop";

    private MediaPlayer mMediaPlayer;
    private MediaSessionManager mManager;
    private MediaSession mSession;
    private MediaController mController;

    private MediaPlayerSingleton mps;


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void handleIntent(Intent intent) {
        if (intent == null || intent.getAction() == null)
            return;

        String action = intent.getAction();

        if (action.equalsIgnoreCase(ACTION_PLAY)) {
            mController.getTransportControls().play();
        } else if (action.equalsIgnoreCase(ACTION_PAUSE)) {
            mController.getTransportControls().pause();
        } else if (action.equalsIgnoreCase(ACTION_FAST_FORWARD)) {
            mController.getTransportControls().fastForward();
        } else if (action.equalsIgnoreCase(ACTION_REWIND)) {
            mController.getTransportControls().rewind();
        } else if (action.equalsIgnoreCase(ACTION_PREVIOUS)) {
            mController.getTransportControls().skipToPrevious();
        } else if (action.equalsIgnoreCase(ACTION_NEXT)) {
            mController.getTransportControls().skipToNext();
        } else if (action.equalsIgnoreCase(ACTION_STOP)) {
            mController.getTransportControls().stop();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT_WATCH)
    private Notification.Action generateAction(int icon, String title, String intentAction) {
        Intent intent = new Intent(getApplicationContext(), MediaPlayerService.class);
        intent.setAction(intentAction);
        PendingIntent pendingIntent = PendingIntent.getService(getApplicationContext(), 1, intent, 0);
        return new Notification.Action.Builder(icon, title, pendingIntent).build();
    }

    Notification.Builder builder;
    Bitmap icon;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void buildNotification(Notification.Action action) {
        Notification.MediaStyle style = new Notification.MediaStyle().setMediaSession(mSession.getSessionToken());


        icon = BitmapFactory.decodeResource(getResources(),
                R.mipmap.ic_app_icon);


       /* Glide.with(this)
                .asBitmap()
                .load(path)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        imageView.setImageBitmap(resource);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                    }
                });



*/


        Picasso.with(getApplicationContext()).load(mps.getImageUrl()).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                icon = bitmap;
                Log.w("media service", "bitmap loaded");
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
                Log.w("media service", "bitmap failed");

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
                Log.w("media service", "bitmap prepared load");
            }
        });


        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        String CHANNEL_ID = "my_channel_01";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            CharSequence name = "Song";
            String Description = "Song Channel";
            int importance = NotificationManager.IMPORTANCE_LOW;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            mChannel.setDescription(Description);
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.RED);
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            mChannel.setShowBadge(false);
            mChannel.setSound(null, null);
            notificationManager.createNotificationChannel(mChannel);
        }


        Intent intent = new Intent(getApplicationContext(), MediaPlayerService.class);
        intent.setAction(ACTION_STOP);

        PendingIntent pendingIntent = PendingIntent.getService(getApplicationContext(), 1, intent, 0);


        Notification.Builder builder = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            builder = new Notification.Builder(this, CHANNEL_ID)
                    .setSmallIcon(R.mipmap.logo)
                    .setContentTitle(mps.getAuthorName())
                    .setContentText(mps.getFileName())
                    .setDeleteIntent(pendingIntent)
                    .setStyle(style)
                    .setLargeIcon(icon);

            style.setShowActionsInCompactView(1);
            builder.addAction(generateAction(R.drawable.play_blue, "play", ACTION_PLAY));
            builder.addAction(action);
        } else {
            builder = new Notification.Builder(this)
                    .setSmallIcon(R.mipmap.logo)
                    .setContentTitle(mps.getAuthorName())
                    .setContentText(mps.getFileName())
                    .setDeleteIntent(pendingIntent)
                    .setSound(null)
                    .setStyle(style)
                    .setLargeIcon(icon);


            builder.addAction(generateAction(android.R.drawable.ic_media_previous, "Previous", ACTION_PREVIOUS));
            //builder.addAction(generateAction(android.R.drawable.ic_media_rew, "Rewind", ACTION_REWIND));
            builder.addAction(action);
            // builder.addAction(generateAction(android.R.drawable.ic_media_ff, "Fast Foward", ACTION_FAST_FORWARD));
            builder.addAction(generateAction(android.R.drawable.ic_media_next, "Next", ACTION_NEXT));

            style.setShowActionsInCompactView(1);
        }


        notificationManager.notify(1, builder.build());


        Intent i = new Intent("MP_STATUS");
        i.putExtra("status", mps.getMediaPlayerStatus());
        sendBroadcast(i);


    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (mManager == null) {
            initMediaSessions();
        }

        handleIntent(intent);
        return super.onStartCommand(intent, flags, startId);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initMediaSessions() {


        mMediaPlayer = new MediaPlayer();

        mps = MediaPlayerSingleton.getInstance(getApplicationContext());

        mSession = new MediaSession(getApplicationContext(), "simple player session");
        mController = new MediaController(getApplicationContext(), mSession.getSessionToken());

        mSession.setCallback(new MediaSession.Callback() {
                                 @Override
                                 public void onPlay() {
                                     super.onPlay();

                                     if (mps.mp != null) {
                                         mps.mp.start();
                                     }
                                     mps.setMediaPlayerStatus("playing");
                                     buildNotification(generateAction(android.R.drawable.ic_media_pause, "Pause", ACTION_PAUSE));


                                 }

                                 @Override
                                 public void onPause() {
                                     super.onPause();
                                     Log.e("MediaPlayerService", "onPlay");
                                     if (mps.mp != null) {
                                         mps.mp.pause();
                                     }
                                     mps.setMediaPlayerStatus("pause");
                                    /* Intent i = new Intent("MP_STATUS");
                                     i.putExtra("status", "pause");
                                     sendBroadcast(i);*/

                                     Log.e("MediaPlayerService", "onPause");
                                     buildNotification(generateAction(android.R.drawable.ic_media_play, "Play", ACTION_PLAY));


                                 }

                                 @Override
                                 public void onSkipToNext() {
                                     super.onSkipToNext();
                                     Log.e("MediaPlayerService", "onSkipToNext");
                                     //Change media here
                                     buildNotification(generateAction(android.R.drawable.ic_media_pause, "Pause", ACTION_PAUSE));
                                 }

                                 @Override
                                 public void onSkipToPrevious() {
                                     super.onSkipToPrevious();
                                     Log.e("MediaPlayerService", "onSkipToPrevious");
                                     //Change media here
                                     buildNotification(generateAction(android.R.drawable.ic_media_pause, "Pause", ACTION_PAUSE));
                                 }

                                 @Override
                                 public void onFastForward() {
                                     super.onFastForward();
                                     Log.e("MediaPlayerService", "onFastForward");
                                     //Manipulate current media here
                                 }

                                 @Override
                                 public void onRewind() {
                                     super.onRewind();
                                     Log.e("MediaPlayerService", "onRewind");
                                     //Manipulate current media here
                                 }

                                 @Override
                                 public void onStop() {
                                     super.onStop();
                                     Log.e("MediaPlayerService", "onStop");
                                     //Stop media player here
                                     NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
                                     notificationManager.cancel(1);
                                     Intent intent = new Intent(getApplicationContext(), MediaPlayerService.class);
                                     stopService(intent);
                                 }

                                 @Override
                                 public void onSeekTo(long pos) {
                                     super.onSeekTo(pos);
                                 }

                                 @Override
                                 public void onSetRating(Rating rating) {
                                     super.onSetRating(rating);
                                 }
                             }
        );
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onUnbind(Intent intent) {
        mSession.release();
        return super.onUnbind(intent);
    }
}
