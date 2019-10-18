package com.triton.voxit.model;

public class UpdatereferalRequest {
    private UpdateData data;
    private String status;
    private Integer code;
    private String message;

    public UpdateData getData() {
        return data;
    }

    public void setData(UpdateData data) {
        this.data = data;
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
