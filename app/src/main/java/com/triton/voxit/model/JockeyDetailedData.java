package com.triton.voxit.model;

public class JockeyDetailedData {
    private String image_path;
    private Integer jockey_id;
    private String name;
    private String display_name;

    public String getDisplay_name() {
        return display_name;
    }

    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
    }

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
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
}
