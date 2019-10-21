package com.triton.voxit.model;

import java.io.Serializable;

public class AudioListData implements Serializable {
    private Integer jockey_id;
    private String name;
    private Integer audio_id;
    private String title;
    private String discription;
    private String image_path;
    private String audio_path;
    private String audio_length;


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

    public Integer getAudio_id() {
        return audio_id;
    }

    public void setAudio_id(Integer audio_id) {
        this.audio_id = audio_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDiscription() {
        return discription;
    }

    public void setDiscription(String discription) {
        this.discription = discription;
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
}
