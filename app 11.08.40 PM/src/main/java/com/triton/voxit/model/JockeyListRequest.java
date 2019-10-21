package com.triton.voxit.model;

import java.util.ArrayList;

public class JockeyListRequest {
    private String status;
    private Integer code;
    private ArrayList<JockeyDetailedData> jockey_details;
    private ArrayList<AudioListData> audiolist;

    public ArrayList<JockeyDetailedData> getJockey_details() {
        return jockey_details;
    }

    public void setJockey_details(ArrayList<JockeyDetailedData> jockey_details) {
        this.jockey_details = jockey_details;
    }

    public ArrayList<AudioListData> getAudiolist() {
        return audiolist;
    }

    public void setAudiolist(ArrayList<AudioListData> audiolist) {
        this.audiolist = audiolist;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
