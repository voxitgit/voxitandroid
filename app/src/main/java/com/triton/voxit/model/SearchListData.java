package com.triton.voxit.model;

public class SearchListData {
    private String title;
    private String description;
    private String imageUrl;
    private String header_title;
    private String header_des;
    private String header_imageUrl;

    public SearchListData(String title, String description, String imageUrl) {
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
//        this.header_title = header_title;
//        this.header_des = header_des;
//        this.header_imageUrl = header_imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getHeader_title() {
        return header_title;
    }

    public void setHeader_title(String header_title) {
        this.header_title = header_title;
    }

    public String getHeader_des() {
        return header_des;
    }

    public void setHeader_des(String header_des) {
        this.header_des = header_des;
    }

    public String getHeader_imageUrl() {
        return header_imageUrl;
    }

    public void setHeader_imageUrl(String header_imageUrl) {
        this.header_imageUrl = header_imageUrl;
    }
}
