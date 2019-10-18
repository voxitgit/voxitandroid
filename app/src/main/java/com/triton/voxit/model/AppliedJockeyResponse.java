package com.triton.voxit.model;

public class AppliedJockeyResponse {
    private Boolean isAppliedForJockey;
    private String status;
    private Integer code;

    public Boolean getAppliedForJockey() {
        return isAppliedForJockey;
    }

    public void setAppliedForJockey(Boolean appliedForJockey) {
        isAppliedForJockey = appliedForJockey;
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
