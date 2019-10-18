package com.triton.voxit.model;

import java.io.Serializable;
import java.util.ArrayList;

public class NotifyDataList implements Serializable {
    private String title;
    private String description;
    private String status;
    private String createOn;
    private String type;
    private String audio_id;
    private String images;
    private AudioDetailData audioDetail;

    private String create_date;
    private String create_time;

    public String getCreate_date() {
        return create_date;
    }

    public void setCreate_date(String create_date) {
        this.create_date = create_date;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getCreateOn() {
        return createOn;
    }

    public void setCreateOn(String createOn) {
        this.createOn = createOn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAudio_id() {
        return audio_id;
    }

    public void setAudio_id(String audio_id) {
        this.audio_id = audio_id;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public AudioDetailData getAudioDetail() {
        return audioDetail;
    }

    public void setAudioDetail(AudioDetailData audioDetail) {
        this.audioDetail = audioDetail;
    }
}
