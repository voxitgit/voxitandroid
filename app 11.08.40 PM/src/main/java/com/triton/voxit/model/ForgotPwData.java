package com.triton.voxit.model;

import java.util.ArrayList;

public class ForgotPwData {
    private String status;
    ArrayList<ForgotDetailData> Details;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<ForgotDetailData> getDetails() {
        return Details;
    }

    public void setDetails(ArrayList<ForgotDetailData> details) {
        Details = details;
    }
}
