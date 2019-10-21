package com.triton.voxit.responsepojo;

import java.util.List;

public class CreateQuizMappingResponse {


    /**
     * status : Failure
     * message : ["should have required property eventType"]
     * code : 400
     */

    private String status;
    private int code;
    private List<String> message;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<String> getMessage() {
        return message;
    }

    public void setMessage(List<String> message) {
        this.message = message;
    }
}


