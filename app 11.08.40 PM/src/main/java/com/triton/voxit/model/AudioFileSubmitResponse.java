package com.triton.voxit.model;

public class AudioFileSubmitResponse {

    private Integer id;
    private String jockey_id;
    private String approval_status;
    private Boolean isAppliedForJockey;

    private  boolean status;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getJockey_id() {
        return jockey_id;
    }

    public void setJockey_id(String jockey_id) {
        this.jockey_id = jockey_id;
    }

    public String getApproval_status() {
        return approval_status;
    }

    public void setApproval_status(String approval_status) {
        this.approval_status = approval_status;
    }

    public Boolean getAppliedForJockey() {
        return isAppliedForJockey;
    }

    public void setAppliedForJockey(Boolean appliedForJockey) {
        isAppliedForJockey = appliedForJockey;
    }
}
