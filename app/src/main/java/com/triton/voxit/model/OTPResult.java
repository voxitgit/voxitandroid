package com.triton.voxit.model;

public class OTPResult {
    private String status;
    private String OTP;
    private String Details;
    private String error;

    public OTPResult(String status, String OTP,String Details, String error) {
        this.status=status;
        this.OTP=OTP;
        this.Details=Details;
        this.error=error;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOTP() {
        return OTP;
    }

    public void setOTP(String OTP) {
        this.OTP = OTP;
    }

    public String getDetails() {
        return Details;
    }

    public void setDetails(String details) {
        Details = details;
    }
}
