package com.triton.voxit.model;

public class RedeemPointsRequest {
    private String status;
    private Integer code;
    private RedeemPointsData data;

    public RedeemPointsData getData() {
        return data;
    }

    public void setData(RedeemPointsData data) {
        this.data = data;
    }

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
}
