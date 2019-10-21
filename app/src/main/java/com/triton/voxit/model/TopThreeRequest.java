package com.triton.voxit.model;

import java.util.ArrayList;

public class TopThreeRequest {
    public ArrayList<TopThreePerformers> getTopThreePerformers() {
        return TopThreePerformers;
    }

    public void setTopThreePerformers(ArrayList<TopThreePerformers> topThreePerformers) {
        TopThreePerformers = topThreePerformers;
    }

    private ArrayList<TopThreePerformers> TopThreePerformers;

    public ArrayList<ResponseBean> getResponse() {
        return Response;
    }

    public void setResponse(ArrayList<ResponseBean> response) {
        Response = response;
    }

    private ArrayList<ResponseBean> Response;
}
