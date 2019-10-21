package com.triton.voxit.requestpojo;

public class UpdateJockeyProfilePicRequest {

    /**
     * jockey_id : 373
     * image_path : http://tritontutebox.com/voxit/image_upload/User_Image.png
     */

    private int jockey_id;
    private String image_path;

    public int getJockey_id() {
        return jockey_id;
    }

    public void setJockey_id(int jockey_id) {
        this.jockey_id = jockey_id;
    }

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }
}
