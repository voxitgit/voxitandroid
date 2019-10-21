package com.triton.voxit.responsepojo;

import java.util.List;

public class ImageStoreResponse {
    private int Status;
    private int Code;
    private String Message;
    private List<ImageurlBean> imageurl;

    public int getStatus() {
        return Status;
    }

    public void setStatus(int Status) {
        this.Status = Status;
    }

    public int getCode() {
        return Code;
    }

    public void setCode(int Code) {
        this.Code = Code;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String Message) {
        this.Message = Message;
    }

    public List<ImageurlBean> getImageurl() {
        return imageurl;
    }

    public void setImageurl(List<ImageurlBean> imageurl) {
        this.imageurl = imageurl;
    }

    public static class ImageurlBean {


        private String ImageName;
        private String Imagepath;

        public String getImageName() {
            return ImageName;
        }

        public void setImageName(String ImageName) {
            this.ImageName = ImageName;
        }

        public String getImagepath() {
            return Imagepath;
        }

        public void setImagepath(String Imagepath) {
            this.Imagepath = Imagepath;
        }
    }
}
