package com.triton.voxit.model;

import java.util.ArrayList;

public class LoginRequest {
    private String code;
    private String message;
    private String status;
    private ArrayList<LoginDatabean> response;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public ArrayList<LoginDatabean> getResponse() {
        return response;
    }

    public void setResponse(ArrayList<LoginDatabean> response) {
        this.response = response;
    }
}
