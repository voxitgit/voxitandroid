package com.triton.voxit.requestpojo;

import java.util.List;

public class CreateQuizMappingRequest {


    /**
     * eventType : Quiz 1
     * startDate : 10/27/2019
     * startTime : 5:39:17 PM
     * endDate : 11/5/2019
     * endTime : 5:39:17 PM
     * eventTime : 10
     * questionIds : [1,2,3]
     */

    private String eventType;
    private String startDate;
    private String startTime;
    private String endDate;
    private String endTime;
    private int eventTime;
    private List<Integer> questionIds;

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getEventTime() {
        return eventTime;
    }

    public void setEventTime(int eventTime) {
        this.eventTime = eventTime;
    }

    public List<Integer> getQuestionIds() {
        return questionIds;
    }

    public void setQuestionIds(List<Integer> questionIds) {
        this.questionIds = questionIds;
    }
}
