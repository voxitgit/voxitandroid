package com.triton.voxit.model;

public class OTPRequest {
    private String status;
    private Integer code;
    private OTPResult data;
    private String error;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public OTPResult getData() {
        return data;
    }

    public void setData(OTPResult data) {
        this.data = data;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
