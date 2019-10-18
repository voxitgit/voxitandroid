package com.triton.voxit.model;

public class BannerResponseBean {
    private Integer banner_id;
    private String image_path;
    private String create_date;
    private String update_date;

    public Integer getBanner_id() {
        return banner_id;
    }

    public void setBanner_id(Integer banner_id) {
        this.banner_id = banner_id;
    }

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }
}
