package com.triton.voxit.model;

public class RecentlyPlayedResponse {
    private String status;
    private Integer code;
    private RecentlyPlayedData Response;

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

    public RecentlyPlayedData getResponse() {
        return Response;
    }

    public void setResponse(RecentlyPlayedData response) {
        Response = response;
    }
}
