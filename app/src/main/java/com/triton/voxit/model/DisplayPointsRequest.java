package com.triton.voxit.model;

import com.triton.voxit.Activity.DisplayPointsData;

import java.util.ArrayList;

public class DisplayPointsRequest {
    private String status;
    private Integer code;
    private ArrayList<DisplayPointsData> data;

    public ArrayList<DisplayPointsData> getData() {
        return data;
    }

    public void setData(ArrayList<DisplayPointsData> data) {
        this.data = data;
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
