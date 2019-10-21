package com.triton.voxit.model;

public class InsertPointData {
    private Integer id;
    private String points;
    private String jockey_id;
    private String referral_code;
    private String status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public String getJockey_id() {
        return jockey_id;
    }

    public void setJockey_id(String jockey_id) {
        this.jockey_id = jockey_id;
    }

    public String getReferral_code() {
        return referral_code;
    }

    public void setReferral_code(String referral_code) {
        this.referral_code = referral_code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
