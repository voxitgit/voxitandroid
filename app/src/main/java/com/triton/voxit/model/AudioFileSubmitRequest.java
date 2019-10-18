package com.triton.voxit.model;

public class AudioFileSubmitRequest {
    private String status;
    private Integer code;
    AudioFileSubmitResponse Response;

    public AudioFileSubmitResponse getResponse() {
        return Response;
    }

    public void setResponse(AudioFileSubmitResponse response) {
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
