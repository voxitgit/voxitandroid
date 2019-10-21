package com.triton.voxit.model;

public class TopGenre extends RecyclerViewItem {

    private String imageUrl;
//    private String headerTitle;

    public TopGenre(String imageUrl) {
//        this.headerTitle = headerTitle;
        this.imageUrl = imageUrl;

    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

//    public String getHeaderTitle() {
//        return headerTitle;
//    }
//
//    public void setHeaderTitle(String headerTitle) {
//        this.headerTitle = headerTitle;
//    }
}
