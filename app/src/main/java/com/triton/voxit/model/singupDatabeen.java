package com.triton.voxit.model;

public class singupDatabeen {
    private String success;
    private String error;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public SignupDetails getDetails() {
        return details;
    }

    public void setDetails(SignupDetails details) {
        this.details = details;
    }

    private SignupDetails details;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }
}
