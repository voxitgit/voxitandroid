package com.triton.voxit.model;

import java.util.ArrayList;

public class ResetPasswordRequest {
    private ArrayList<ResetPasswordData> data;
    private String status;
    private Integer code;

    public ArrayList<ResetPasswordData> getData() {
        return data;
    }

    public void setData(ArrayList<ResetPasswordData> data) {
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
