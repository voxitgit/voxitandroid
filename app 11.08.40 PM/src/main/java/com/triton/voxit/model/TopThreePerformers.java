package com.triton.voxit.model;

import com.triton.voxit.responsepojo.VCornerQuestionsResponse;

import java.util.ArrayList;

public class TopThreePerformers {
    private String status;
    private int code;
    private String Name;
    private String Image;

    private int Rank;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }



    public int getRank() {
        return Rank;
    }

    public void setRank(int rank) {
        Rank = rank;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }




}
