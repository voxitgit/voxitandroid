package com.triton.voxit.model;

public class InsertPointsRequest {
    private String status;
    private Integer code;
    private InsertPointData data;

    public InsertPointData getData() {
        return data;
    }

    public void setData(InsertPointData data) {
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
}
