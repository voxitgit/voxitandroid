package com.triton.voxit.model;

import java.util.ArrayList;

public class HelpResponseData {
   // private HelpData Response;
    ArrayList<HelpData> Response;
    private String status;
    private Integer code;

//    public HelpData getResponse() {
//        return Response;
//    }
//
//    public void setResponse(HelpData response) {
//        Response = response;
//    }

    public ArrayList<HelpData> getResponse() {
        return Response;
    }

    public void setResponse(ArrayList<HelpData> response) {
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
