package com.triton.voxit.model;

import java.io.Serializable;

public class TrendingResponseBean implements Serializable {
   // private Integer audio_id;
    private String title;
    private String discription;
    private String image_path;
    private String audio_path;
    private String audio_length;
    private String genre;
    private Integer genre_id;
    private String language;
    private Integer lang_id;
    private String create_date;
    private Integer jockey_id;
    private String name;
    private String age;
    private String gender;
    private String emailid;
    private String phoneno;
    private String jockey_type;
    private String audio_id;

    public String getAudio_id() {
        return audio_id;
    }

    public void setAudio_id(String audio_id) {
        this.audio_id = audio_id;
    }
    //    public Integer getAudio_id() {
//        return audio_id;
//    }
//
//    public void setAudio_id(Integer audio_id) {
//        this.audio_id = audio_id;
//    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }

    public String getAudio_path() {
        return audio_path;
    }

    public void setAudio_path(String audio_path) {
        this.audio_path = audio_path;
    }

    public String getAudio_length() {
        return audio_length;
    }

    public void setAudio_length(String audio_length) {
        this.audio_length = audio_length;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getDiscription() {
        return discription;
    }

    public void setDiscription(String discription) {
        this.discription = discription;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getJockey_id() {
        return jockey_id;
    }

    public void setJockey_id(Integer jockey_id) {
        this.jockey_id = jockey_id;
    }
}
