package com.triton.voxit.model;

public class ForgotPasswordRequest {
    private Integer code;
    private String status;
    private String message;
    private ForgotPwData data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public ForgotPwData getData() {
        return data;
    }

    public void setData(ForgotPwData data) {
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
