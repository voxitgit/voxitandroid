package com.triton.voxit.model;

import java.io.Serializable;

public class AudioDetailData implements Serializable {
    private Integer jockey_id;
    private Integer audio_id;
    private String title;
    private String discription;
    private String image_path;
    private String audio_path;
    private String audio_length;
    private String genre;
    private Integer genre_id;
    private String jockey_type;
    private String language;
    private Integer lang_id;
    private String create_date;
    private String name;
    private String age;

    public Integer getAudio_id() {
        return audio_id;
    }

    public void setAudio_id(Integer audio_id) {
        this.audio_id = audio_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDiscription() {
        return discription;
    }

    public void setDiscription(String discription) {
        this.discription = discription;
    }

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }

    public String getAudio_path() {
        return audio_path;
    }

    public void setAudio_path(String audio_path) {
        this.audio_path = audio_path;
    }

    public String getAudio_length() {
        return audio_length;
    }

    public void setAudio_length(String audio_length) {
        this.audio_length = audio_length;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public Integer getGenre_id() {
        return genre_id;
    }

    public void setGenre_id(Integer genre_id) {
        this.genre_id = genre_id;
    }

    public String getJockey_type() {
        return jockey_type;
    }

    public void setJockey_type(String jockey_type) {
        this.jockey_type = jockey_type;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Integer getLang_id() {
        return lang_id;
    }

    public void setLang_id(Integer lang_id) {
        this.lang_id = lang_id;
    }

    public String getCreate_date() {
        return create_date;
    }

    public void setCreate_date(String create_date) {
        this.create_date = create_date;
    }

    public Integer getJockey_id() {
        return jockey_id;
    }

    public void setJockey_id(Integer jockey_id) {
        this.jockey_id = jockey_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }
}
