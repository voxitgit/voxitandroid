package com.triton.voxit.model;

import java.util.ArrayList;

public class T_LResponseBean {
    private ArrayList<TrendingResponseBean> Trending;
    private ArrayList<LiveResponseBean> Live;


    public ArrayList<TrendingResponseBean> getTrending() {
        return Trending;
    }

    public void setTrending(ArrayList<TrendingResponseBean> trending) {
        Trending = trending;
    }

    public ArrayList<LiveResponseBean> getLive() {
        return Live;
    }

    public void setLive(ArrayList<LiveResponseBean> live) {
        Live = live;
    }
}
