package com.triton.voxit.model;

import java.util.ArrayList;

public class DashboardResponsebean {
    private ArrayList<DashboardResponse> response;
    private String status;
    private Integer code;

    public ArrayList<DashboardResponse> getResponse() {
        return response;
    }

    public void setResponse(ArrayList<DashboardResponse> response) {
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
