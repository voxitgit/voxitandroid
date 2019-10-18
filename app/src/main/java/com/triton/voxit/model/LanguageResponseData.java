package com.triton.voxit.model;

import java.util.ArrayList;

public class LanguageResponseData {
    private ArrayList<LanguageData> data;
    private String status;
    private Integer code;

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

    public ArrayList<LanguageData> getData() {
        return data;
    }

    public void setData(ArrayList<LanguageData> data) {
        this.data = data;
    }
}
