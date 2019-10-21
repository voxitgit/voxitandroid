package com.triton.voxit.model;

public class LoginDataRequest {
    private String userId;
    private String password;

    public LoginDataRequest(String name, String password) {
        this.userId = name;
        this.password = password;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
