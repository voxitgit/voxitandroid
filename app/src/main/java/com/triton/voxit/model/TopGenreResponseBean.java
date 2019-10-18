package com.triton.voxit.model;

import java.util.ArrayList;

public class TopGenreResponseBean {
    private String genre;
    private String language;
    private ArrayList<AudioDetailsResponseBean> audiodetails;

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public ArrayList<AudioDetailsResponseBean> getAudiodetails() {
        return audiodetails;
    }

    public void setAudiodetails(ArrayList<AudioDetailsResponseBean> audiodetails) {
        this.audiodetails = audiodetails;
    }
}
