package com.triton.voxit.model;

public class FollowResponseData {
    private String follower_id;
    private String user_jocky_id;
    private String status;

    public String getFollower_id() {
        return follower_id;
    }

    public void setFollower_id(String follower_id) {
        this.follower_id = follower_id;
    }

    public String getUser_jocky_id() {
        return user_jocky_id;
    }

    public void setUser_jocky_id(String user_jocky_id) {
        this.user_jocky_id = user_jocky_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
