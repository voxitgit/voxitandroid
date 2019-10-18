package com.triton.voxit.Utlity;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;

import com.triton.voxit.model.AudioListData;
import com.triton.voxit.model.SearchData;

import java.util.ArrayList;

public final class MediaPlayerSingleton {

    private static volatile MediaPlayerSingleton instance = null;
    public MediaPlayer mp;
    private Context context;

    private String status = "EMPTY";
    private String setType = "empty";
    private String fileName = "empty";
    private String imageUrl = "empty";
    private String authorName = "empty";
    private int currentPlayPos = -1;
    private String subType = "empty";

    private ArrayList<AudioListData> audioListData = new ArrayList<>();

    private ArrayList<SearchData> searchData = new ArrayList<>();


    private MediaPlayerSingleton(Context context) {
        this.context = context;
    }

    public static MediaPlayerSingleton getInstance(Context context) {
        if (instance == null) {
            synchronized (MediaPlayerSingleton.class) {
                if (instance == null) {
                    instance = new MediaPlayerSingleton(context);
                }
            }
        }
        return instance;
    }


    public boolean checkMediaPlayerIsPlaying() {
        return instance.mp != null && instance.mp.isPlaying();
    }


    public void releasePlayer() {
        if (instance.mp != null) {
            instance.mp.pause();
            instance.mp.reset();
            instance.mp.release();
            instance.mp = null;
        }
    }


    public void setMediaPlayerStatus(String status) {
        this.status = status;
    }

    public String getMediaPlayerStatus() {

        return status;
    }

    public void setAuthorName(String name) {
        this.authorName = name;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setImageUrl(String url) {
        this.imageUrl = url;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setCurrentPlayPos(int pos) {
        this.currentPlayPos = pos;
    }

    public int getCurrentPlayPos() {
        return currentPlayPos;
    }

    public void setType(String type) {
        this.setType = type;
    }

    public void setSubType(String subType) {
        this.subType = subType;
    }

    public String getSubType() {
        return subType;
    }

    public String getType() {
        return setType;
    }


    public void setJockeyList(ArrayList<AudioListData> audioListData) {
        this.audioListData = audioListData;
    }

    public ArrayList<AudioListData> getJockeyListData() {
        return audioListData;
    }

    public void setSearchList(ArrayList<SearchData> searchData) {
        this.searchData = searchData;
    }

    public ArrayList<SearchData> getSearchList() {
        return searchData;
    }


    public synchronized void initializePlayer() {

        Log.w("Media Singleton", "media player initialized");

        if (instance.mp == null) {
            instance.mp = new MediaPlayer();
        } /*else {
            instance.mp.reset();
        }*/
      /*  try {
            // instance.mp.setDataSource(context, Uri.parse(APP_RAW_URI_PATH_1 + fileName));
            instance.mp.prepare();
            instance.mp.setVolume(100f, 100f);
            instance.mp.setLooping(false);
            instance.mp.start();
            instance.mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {

                    if (instance.mp != null) {
                        instance.mp.reset();
                        instance.mp = null;
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }
}
