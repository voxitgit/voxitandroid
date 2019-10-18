package com.triton.voxit.model;

import java.util.ArrayList;

public class SearchResponseData {
    private ArrayList<SearchData> response;
    private String status;
    private Integer code;
    public ArrayList<SearchData> getResponse() {
        return response;
    }

    public void setResponse(ArrayList<SearchData> response) {
        this.response = response;
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
