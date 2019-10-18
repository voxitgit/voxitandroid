package com.triton.voxit.model;

public class NotificationToken {
    private String status;
    private Integer code;
    private Boolean isMust;
    private String message;

    public Boolean getMust() {
        return isMust;
    }

    public void setMust(Boolean must) {
        isMust = must;
    }

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

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
