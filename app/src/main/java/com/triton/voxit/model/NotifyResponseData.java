package com.triton.voxit.model;

import java.util.ArrayList;

public class NotifyResponseData {
    private ArrayList<NotifyDataList> Response;
    private String status;
    private Integer code;

    public ArrayList<NotifyDataList> getResponse() {
        return Response;
    }

    public void setResponse(ArrayList<NotifyDataList> response) {
        Response = response;
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
