package com.triton.voxit.model;

import java.util.ArrayList;

public class SignUpRequest {
    private String error;
    private Integer code;
    private singupDatabeen data;

    private String status;
//    private ArrayList<MessageData> message;

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

    public singupDatabeen getData() {
        return data;
    }

    public void setData(singupDatabeen data) {
        this.data = data;
    }

//    public ArrayList<MessageData> getMessage() {
//        return message;
//    }
//
//    public void setMessage(ArrayList<MessageData> message) {
//        this.message = message;
//    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
