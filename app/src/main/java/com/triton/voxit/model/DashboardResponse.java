package com.triton.voxit.model;

import java.util.ArrayList;

public class DashboardResponse {
    private ArrayList<BannerResponseBean> Banner;
    private ArrayList<T_LResponseBean> T_L;
    private ArrayList<GenreResponseBean> Genre;
    private ArrayList<TopGenreResponseBean> TopGenre;


    public ArrayList<BannerResponseBean> getBanner() {
        return Banner;
    }

    public void setBanner(ArrayList<BannerResponseBean> banner) {
        Banner = banner;
    }

    public ArrayList<T_LResponseBean> getT_L() {
        return T_L;
    }

    public void setT_L(ArrayList<T_LResponseBean> t_L) {
        T_L = t_L;
    }

    public ArrayList<GenreResponseBean> getGenre() {
        return Genre;
    }

    public void setGenre(ArrayList<GenreResponseBean> genre) {
        Genre = genre;
    }

    public ArrayList<TopGenreResponseBean> getTopGenre() {
        return TopGenre;
    }

    public void setTopGenre(ArrayList<TopGenreResponseBean> topGenre) {
        TopGenre = topGenre;
    }
}
