package com.triton.voxit.model;

import java.io.Serializable;
import java.util.ArrayList;

public class EventBean implements Serializable {

    public int getQid() {
        return Qid;
    }

    public void setQid(int qid) {
        Qid = qid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getQuizType() {
        return quizType;
    }

    public void setQuizType(String quizType) {
        this.quizType = quizType;
    }

    public String getAudio_path() {
        return audio_path;
    }

    public void setAudio_path(String audio_path) {
        this.audio_path = audio_path;
    }

    public String getAudio_id() {
        return audio_id;
    }

    public void setAudio_id(String audio_id) {
        this.audio_id = audio_id;
    }

    public String getQPatternId() {
        return QPatternId;
    }

    public void setQPatternId(String QPatternId) {
        this.QPatternId = QPatternId;
    }

    private int Qid;
    private String title;
    private String quizType;
    private String audio_path;
    private String audio_id;
    private String QPatternId;

    public ArrayList<OptionsBean> getOptions() {
        return options;
    }

    public void setOptions(ArrayList<OptionsBean> options) {
        this.options = options;
    }

    private ArrayList<OptionsBean> options;

    @Override
    public String toString() {
        return "EventBean{" +
                "Qid=" + Qid +
                ", title='" + title + '\'' +
                ", quizType='" + quizType + '\'' +
                ", audio_path='" + audio_path + '\'' +
                ", audio_id='" + audio_id + '\'' +
                ", QPatternId='" + QPatternId + '\'' +
                ", options=" + options +
                '}';
    }
}
