package com.triton.voxit.model;

public class LoginDatabean {
    private String emailid;
    private String image_path;
    private Integer jockey_id;
    private String jockey_type;
    private String country;
    private String name;
    private String phoneno;
    private String user_mode;
    private String referral_code;
    private String referred_code;
    private Boolean isAppliedForJockey;

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

    public String getEmailid() {
        return emailid;
    }

    public void setEmailid(String emailid) {
        this.emailid = emailid;
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

    public String getJockey_type() {
        return jockey_type;
    }

    public void setJockey_type(String jockey_type) {
        this.jockey_type = jockey_type;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneno() {
        return phoneno;
    }

    public void setPhoneno(String phoneno) {
        this.phoneno = phoneno;
    }

    public String getUser_mode() {
        return user_mode;
    }

    public void setUser_mode(String user_mode) {
        this.user_mode = user_mode;
    }

    public Boolean getAppliedForJockey() {
        return isAppliedForJockey;
    }

    public void setAppliedForJockey(Boolean appliedForJockey) {
        isAppliedForJockey = appliedForJockey;
    }
}
