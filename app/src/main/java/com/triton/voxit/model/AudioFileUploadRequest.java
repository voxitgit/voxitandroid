package com.triton.voxit.model;

import java.util.ArrayList;

public class AudioFileUploadRequest {
  ArrayList<AudioFileResponseData> response;

    public ArrayList<AudioFileResponseData> getResponse() {
        return response;
    }

    public void setResponse(ArrayList<AudioFileResponseData> response) {
        this.response = response;
    }
}
