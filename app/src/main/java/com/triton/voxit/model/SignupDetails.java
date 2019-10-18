package com.triton.voxit.model;

public class SignupDetails {
    private Integer jockey_id;
    private String name;
    private String emailid;
    private String phoneno;
    private String password;
    private String state;
    private String referral_code;
    private String referred_code;
    private String image_path;
    private String user_mode;
    private Boolean isAppliedForJockey;

    public String getUser_mode() {
        return user_mode;
    }

    public void setUser_mode(String user_mode) {
        this.user_mode = user_mode;
    }

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }

    public Integer getJockey_id() {
        return jockey_id;
    }

    public void setJockey_id(Integer jockey_id) {
        this.jockey_id = jockey_id;
    }

    public String getReferred_code() {
        return referred_code;
    }

    public void setReferred_code(String referred_code) {
        this.referred_code = referred_code;
    }

    public String getReferral_code() {
        return referral_code;
    }

    public void setReferral_code(String referral_code) {
        this.referral_code = referral_code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmailid() {
        return emailid;
    }

    public void setEmailid(String emailid) {
        this.emailid = emailid;
    }

    public String getPhoneno() {
        return phoneno;
    }

    public void setPhoneno(String phoneno) {
        this.phoneno = phoneno;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Boolean getAppliedForJockey() {
        return isAppliedForJockey;
    }

    public void setAppliedForJockey(Boolean appliedForJockey) {
        isAppliedForJockey = appliedForJockey;
    }
}
