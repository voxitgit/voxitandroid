package com.triton.voxit.model;

import com.triton.voxit.responsepojo.VCornerQuestionsResponse;

import java.util.ArrayList;

public class QuestionsandAnswerResponse {

    public ArrayList<VCornerQuestionsResponse> getResponse() {
        return Response;
    }

    public void setResponse(ArrayList<VCornerQuestionsResponse> response) {
        Response = response;
    }

    private ArrayList<VCornerQuestionsResponse> Response;


    

}
