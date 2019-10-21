package com.triton.voxit.model;

import java.util.ArrayList;

public class ShareRequest {
    private String status;
    private Integer code;
    private String error;
    ArrayList<ShareData> data;

    public ArrayList<ShareData> getData() {
        return data;
    }

    public void setData(ArrayList<ShareData> data) {
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

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
