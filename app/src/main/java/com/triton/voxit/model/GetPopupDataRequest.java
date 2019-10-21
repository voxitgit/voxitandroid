package com.triton.voxit.model;

import java.util.ArrayList;

public class GetPopupDataRequest {
    private String status;
    private Integer code;
    ArrayList<GetPopupResponseData> Response;

    public ArrayList<GetPopupResponseData> getResponse() {
        return Response;
    }

    public void setResponse(ArrayList<GetPopupResponseData> response) {
        Response = response;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


}
