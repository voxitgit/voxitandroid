package com.triton.voxit.model;

public class RecentlyPlayedData {
    private Integer id;
    private String jockey_id;
    private String audio_id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getJockey_id() {
        return jockey_id;
    }

    public void setJockey_id(String jockey_id) {
        this.jockey_id = jockey_id;
    }

    public String getAudio_id() {
        return audio_id;
    }

    public void setAudio_id(String audio_id) {
        this.audio_id = audio_id;
    }
}
