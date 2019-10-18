package com.triton.voxit.model;

public class GenreData {
    private Integer genre_id;
    private String name;
    private String image;
    private String lang_idl;

    public Integer getGenre_id() {
        return genre_id;
    }

    public void setGenre_id(Integer genre_id) {
        this.genre_id = genre_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLang_idl() {
        return lang_idl;
    }

    public void setLang_idl(String lang_idl) {
        this.lang_idl = lang_idl;
    }
}
